/*SignApk: perform the whole file signature in a single streaming pass.

Author: Koushik Dutta <koushd@gmail.com>

Change-Id:I58a68fa4bd4c0c3bb0e025d4311186195fb90e5a*/
//Synthetic comment -- diff --git a/tools/signapk/SignApk.java b/tools/signapk/SignApk.java
//Synthetic comment -- index 1055704..e207aae 100644

//Synthetic comment -- @@ -35,6 +35,7 @@
import org.bouncycastle.util.encoders.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
//Synthetic comment -- @@ -96,7 +97,7 @@
Pattern.quote(JarFile.MANIFEST_NAME) + ")$");

private static X509Certificate readPublicKey(File file)
            throws IOException, GeneralSecurityException {
FileInputStream input = new FileInputStream(file);
try {
CertificateFactory cf = CertificateFactory.getInstance("X.509");
//Synthetic comment -- @@ -133,7 +134,7 @@
* @param keyFile The file containing the private key
*/
private static KeySpec decryptPrivateKey(byte[] encryptedPrivateKey, File keyFile)
            throws GeneralSecurityException {
EncryptedPrivateKeyInfo epkInfo;
try {
epkInfo = new EncryptedPrivateKeyInfo(encryptedPrivateKey);
//Synthetic comment -- @@ -160,7 +161,7 @@

/** Read a PKCS 8 format private key. */
private static PrivateKey readPrivateKey(File file)
            throws IOException, GeneralSecurityException {
DataInputStream input = new DataInputStream(new FileInputStream(file));
try {
byte[] bytes = new byte[(int) file.length()];
//Synthetic comment -- @@ -183,7 +184,7 @@

/** Add the SHA1 of every file to the manifest, creating it if necessary. */
private static Manifest addDigestsToManifest(JarFile jar)
            throws IOException, GeneralSecurityException {
Manifest input = jar.getManifest();
Manifest output = new Manifest();
Attributes main = output.getMainAttributes();
//Synthetic comment -- @@ -275,13 +276,13 @@
}

@Override
        public void write(int b) throws IOException {
super.write(b);
mCount++;
}

@Override
        public void write(byte[] b, int off, int len) throws IOException {
super.write(b, off, len);
mCount += len;
}
//Synthetic comment -- @@ -301,8 +302,8 @@

MessageDigest md = MessageDigest.getInstance("SHA1");
PrintStream print = new PrintStream(
                new DigestOutputStream(new ByteArrayOutputStream(), md),
                true, "UTF-8");

// Digest of the entire manifest
manifest.write(print);
//Synthetic comment -- @@ -339,31 +340,6 @@
}
}

    private static class CMSByteArraySlice implements CMSTypedData {
        private final ASN1ObjectIdentifier type;
        private final byte[] data;
        private final int offset;
        private final int length;
        public CMSByteArraySlice(byte[] data, int offset, int length) {
            this.data = data;
            this.offset = offset;
            this.length = length;
            this.type = new ASN1ObjectIdentifier(CMSObjectIdentifiers.data.getId());
        }

        public Object getContent() {
            throw new UnsupportedOperationException();
        }

        public ASN1ObjectIdentifier getContentType() {
            return type;
        }

        public void write(OutputStream out) throws IOException {
            out.write(data, offset, length);
        }
    }

/** Sign data and write the digital signature to 'out'. */
private static void writeSignatureBlock(
CMSTypedData data, X509Certificate publicKey, PrivateKey privateKey,
//Synthetic comment -- @@ -395,23 +371,170 @@
dos.writeObject(asn1.readObject());
}

    private static void signWholeOutputFile(byte[] zipData,
                                            OutputStream outputStream,
                                            X509Certificate publicKey,
                                            PrivateKey privateKey)
        throws IOException,
               CertificateEncodingException,
               OperatorCreationException,
               CMSException {
        // For a zip with no archive comment, the
        // end-of-central-directory record will be 22 bytes long, so
        // we expect to find the EOCD marker 22 bytes from the end.
        if (zipData[zipData.length-22] != 0x50 ||
            zipData[zipData.length-21] != 0x4b ||
            zipData[zipData.length-20] != 0x05 ||
            zipData[zipData.length-19] != 0x06) {
            throw new IllegalArgumentException("zip data already has an archive comment");
}

ByteArrayOutputStream temp = new ByteArrayOutputStream();

//Synthetic comment -- @@ -423,8 +546,20 @@
temp.write(message);
temp.write(0);

        writeSignatureBlock(new CMSByteArraySlice(zipData, 0, zipData.length-2),
                            publicKey, privateKey, temp);
int total_size = temp.size() + 6;
if (total_size > 0xffff) {
throw new IllegalArgumentException("signature is too big for ZIP file comment");
//Synthetic comment -- @@ -458,44 +593,48 @@
}
}

        outputStream.write(zipData, 0, zipData.length-2);
outputStream.write(total_size & 0xff);
outputStream.write((total_size >> 8) & 0xff);
temp.writeTo(outputStream);
}

    /**
     * Copy all the files in a manifest from input to output.  We set
     * the modification times in the output to a fixed time, so as to
     * reduce variation in the output file and make incremental OTAs
     * more efficient.
     */
    private static void copyFiles(Manifest manifest,
        JarFile in, JarOutputStream out, long timestamp) throws IOException {
        byte[] buffer = new byte[4096];
        int num;

        Map<String, Attributes> entries = manifest.getEntries();
        ArrayList<String> names = new ArrayList<String>(entries.keySet());
        Collections.sort(names);
        for (String name : names) {
            JarEntry inEntry = in.getJarEntry(name);
            JarEntry outEntry = null;
            if (inEntry.getMethod() == JarEntry.STORED) {
                // Preserve the STORED method of the input entry.
                outEntry = new JarEntry(inEntry);
            } else {
                // Create a new entry so that the compressed len is recomputed.
                outEntry = new JarEntry(name);
            }
            outEntry.setTime(timestamp);
            out.putNextEntry(outEntry);

            InputStream data = in.getInputStream(inEntry);
            while ((num = data.read(buffer)) > 0) {
                out.write(buffer, 0, num);
            }
            out.flush();
}
}

//Synthetic comment -- @@ -531,7 +670,6 @@
String outputFilename = args[args.length-1];

JarFile inputJar = null;
        JarOutputStream outputJar = null;
FileOutputStream outputFile = null;

try {
//Synthetic comment -- @@ -555,76 +693,26 @@
}
inputJar = new JarFile(new File(inputFilename), false);  // Don't verify.

            OutputStream outputStream = null;
if (signWholeFile) {
                outputStream = new ByteArrayOutputStream();
} else {
                outputStream = outputFile = new FileOutputStream(outputFilename);
            }
            outputJar = new JarOutputStream(outputStream);

            // For signing .apks, use the maximum compression to make
            // them as small as possible (since they live forever on
            // the system partition).  For OTA packages, use the
            // default compression level, which is much much faster
            // and produces output that is only a tiny bit larger
            // (~0.1% on full OTA packages I tested).
            if (!signWholeFile) {
outputJar.setLevel(9);
            }

            JarEntry je;

            Manifest manifest = addDigestsToManifest(inputJar);

            // Everything else
            copyFiles(manifest, inputJar, outputJar, timestamp);

            // otacert
            if (signWholeFile) {
                addOtacert(outputJar, firstPublicKeyFile, timestamp, manifest);
            }

            // MANIFEST.MF
            je = new JarEntry(JarFile.MANIFEST_NAME);
            je.setTime(timestamp);
            outputJar.putNextEntry(je);
            manifest.write(outputJar);

            // In the case of multiple keys, all the .SF files will be
            // identical, but as far as I can tell the jarsigner docs
            // don't allow there to be just one copy in the zipfile;
            // there hase to be one per .RSA file.

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            writeSignatureFile(manifest, baos);
            byte[] signedData = baos.toByteArray();

            for (int k = 0; k < numKeys; ++k) {
                // CERT.SF / CERT#.SF
                je = new JarEntry(numKeys == 1 ? CERT_SF_NAME :
                                  (String.format(CERT_SF_MULTI_NAME, k)));
                je.setTime(timestamp);
                outputJar.putNextEntry(je);
                outputJar.write(signedData);

                // CERT.RSA / CERT#.RSA
                je = new JarEntry(numKeys == 1 ? CERT_RSA_NAME :
                                  (String.format(CERT_RSA_MULTI_NAME, k)));
                je.setTime(timestamp);
                outputJar.putNextEntry(je);
                writeSignatureBlock(new CMSProcessableByteArray(signedData),
                                    publicKey[k], privateKey[k], outputJar);
            }

            outputJar.close();
            outputJar = null;
            outputStream.flush();

            if (signWholeFile) {
                outputFile = new FileOutputStream(outputFilename);
                signWholeOutputFile(((ByteArrayOutputStream)outputStream).toByteArray(),
                                    outputFile, publicKey[0], privateKey[0]);
}
} catch (Exception e) {
e.printStackTrace();







