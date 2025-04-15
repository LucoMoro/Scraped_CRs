/*SignApk: perform the whole file signature in a single streaming pass.

Conflicts:

	tools/signapk/SignApk.java

Change-Id:I58a68fa4bd4c0c3bb0e025d4311186195fb90e5a*/




//Synthetic comment -- diff --git a/tools/signapk/SignApk.java b/tools/signapk/SignApk.java
//Synthetic comment -- index 1055704..dcf15a2 100644

//Synthetic comment -- @@ -35,6 +35,7 @@
import org.bouncycastle.util.encoders.Base64;

import java.io.BufferedReader;
import java.io.BufferedOutputStream;
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

/** Sign data and write the digital signature to 'out'. */
private static void writeSignatureBlock(
CMSTypedData data, X509Certificate publicKey, PrivateKey privateKey,
//Synthetic comment -- @@ -395,23 +371,173 @@
dos.writeObject(asn1.readObject());
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

    private static class WholeFileSignerOutputStream extends FilterOutputStream {
        private boolean closing = false;
        private ByteArrayOutputStream footer = new ByteArrayOutputStream();
        private OutputStream tee;

        public WholeFileSignerOutputStream(OutputStream out, OutputStream tee) {
            super(out);
            this.tee = tee;
        }

        public void notifyClosing() {
            closing = true;
        }

        public void finish() throws IOException {
            closing = false;

            byte[] data = footer.toByteArray();
            if (data.length < 2)
                throw new IOException("Less than two bytes written to footer");
            write(data, 0, data.length - 2);
        }

        public byte[] getTail() {
            return footer.toByteArray();
        }

        @Override
            public void write(byte[] b) throws IOException {
            write(b, 0, b.length);
        }

        @Override
            public void write(byte[] b, int off, int len) throws IOException {
            if (closing) {
                // if the jar is about to close, save the footer that will be written
                footer.write(b, off, len);
            }
            else {
                // write to both output streams. out is the CMSTypedData signer and tee is the file.
                out.write(b, off, len);
                tee.write(b, off, len);
            }
        }

        @Override
            public void write(int b) throws IOException {
            if (closing) {
                // if the jar is about to close, save the footer that will be written
                footer.write(b);
            }
            else {
                // write to both output streams. out is the CMSTypedData signer and tee is the file.
                out.write(b);
                tee.write(b);
            }
        }
    }

    private static class CMSSigner implements CMSTypedData {
        private JarFile inputJar;
        private File publicKeyFile;
        private X509Certificate publicKey;
        private PrivateKey privateKey;
        private String outputFile;
        private OutputStream outputStream;
        private final ASN1ObjectIdentifier type;
        private WholeFileSignerOutputStream signer;

        public CMSSigner(JarFile inputJar, File publicKeyFile,
                         X509Certificate publicKey, PrivateKey privateKey,
                         OutputStream outputStream) {
            this.inputJar = inputJar;
            this.publicKeyFile = publicKeyFile;
            this.publicKey = publicKey;
            this.privateKey = privateKey;
            this.outputStream = outputStream;
            this.type = new ASN1ObjectIdentifier(CMSObjectIdentifiers.data.getId());
        }

        public Object getContent() {
            throw new UnsupportedOperationException();
        }

        public ASN1ObjectIdentifier getContentType() {
            return type;
        }

        public void write(OutputStream out) throws IOException {
            try {
                signer = new WholeFileSignerOutputStream(out, outputStream);
                JarOutputStream outputJar = new JarOutputStream(signer);

                Manifest manifest = addDigestsToManifest(inputJar);
                signFile(manifest, inputJar,
                         new X509Certificate[]{ publicKey },
                         new PrivateKey[]{ privateKey },
                         outputJar);
                // Assume the certificate is valid for at least an hour.
                long timestamp = publicKey.getNotBefore().getTime() + 3600L * 1000;
                addOtacert(outputJar, publicKeyFile, timestamp, manifest);

                signer.notifyClosing();
                outputJar.close();
                signer.finish();
            }
            catch (Exception e) {
                throw new IOException(e);
            }
        }

        public void writeSignatureBlock(ByteArrayOutputStream temp)
            throws IOException,
                   CertificateEncodingException,
                   OperatorCreationException,
                   CMSException {
            SignApk.writeSignatureBlock(this, publicKey, privateKey, temp);
        }

        public WholeFileSignerOutputStream getSigner() {
            return signer;
        }
    }

    private static void signWholeFile(JarFile inputJar, File publicKeyFile,
                                      X509Certificate publicKey, PrivateKey privateKey,
                                      OutputStream outputStream) throws Exception {
        // XXX does this go somewhere?
        //addOtacert(outputJar, firstPublicKeyFile, timestamp, manifest);

        CMSSigner cmsOut = new CMSSigner(inputJar, publicKeyFile,
                                         publicKey, privateKey, outputStream);

ByteArrayOutputStream temp = new ByteArrayOutputStream();

//Synthetic comment -- @@ -423,8 +549,20 @@
temp.write(message);
temp.write(0);

        cmsOut.writeSignatureBlock(temp);

        byte[] zipData = cmsOut.getSigner().getTail();

        // For a zip with no archive comment, the
        // end-of-central-directory record will be 22 bytes long, so
        // we expect to find the EOCD marker 22 bytes from the end.
        if (zipData[zipData.length-22] != 0x50 ||
            zipData[zipData.length-21] != 0x4b ||
            zipData[zipData.length-20] != 0x05 ||
            zipData[zipData.length-19] != 0x06) {
            throw new IllegalArgumentException("zip data already has an archive comment");
        }

int total_size = temp.size() + 6;
if (total_size > 0xffff) {
throw new IllegalArgumentException("signature is too big for ZIP file comment");
//Synthetic comment -- @@ -458,44 +596,48 @@
}
}

outputStream.write(total_size & 0xff);
outputStream.write((total_size >> 8) & 0xff);
temp.writeTo(outputStream);
}

    private static void signFile(Manifest manifest, JarFile inputJar,
                                 X509Certificate[] publicKey, PrivateKey[] privateKey,
                                 JarOutputStream outputJar)
        throws Exception {
        // Assume the certificate is valid for at least an hour.
        long timestamp = publicKey[0].getNotBefore().getTime() + 3600L * 1000;

        JarEntry je;

        // Everything else
        copyFiles(manifest, inputJar, outputJar, timestamp);

        // MANIFEST.MF
        je = new JarEntry(JarFile.MANIFEST_NAME);
        je.setTime(timestamp);
        outputJar.putNextEntry(je);
        manifest.write(outputJar);

        int numKeys = publicKey.length;
        for (int k = 0; k < numKeys; ++k) {
            // CERT.SF / CERT#.SF
            je = new JarEntry(numKeys == 1 ? CERT_SF_NAME :
                              (String.format(CERT_SF_MULTI_NAME, k)));
            je.setTime(timestamp);
            outputJar.putNextEntry(je);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            writeSignatureFile(manifest, baos);
            byte[] signedData = baos.toByteArray();
            outputJar.write(signedData);

            // CERT.RSA / CERT#.RSA
            je = new JarEntry(numKeys == 1 ? CERT_RSA_NAME :
                              (String.format(CERT_RSA_MULTI_NAME, k)));
            je.setTime(timestamp);
            outputJar.putNextEntry(je);
            writeSignatureBlock(new CMSProcessableByteArray(signedData),
                                publicKey[k], privateKey[k], outputJar);
}
}

//Synthetic comment -- @@ -531,7 +673,6 @@
String outputFilename = args[args.length-1];

JarFile inputJar = null;
FileOutputStream outputFile = null;

try {
//Synthetic comment -- @@ -555,76 +696,26 @@
}
inputJar = new JarFile(new File(inputFilename), false);  // Don't verify.

            outputFile = new FileOutputStream(outputFilename);


if (signWholeFile) {
                SignApk.signWholeFile(inputJar, firstPublicKeyFile,
                                      publicKey[0], privateKey[0], outputFile);
} else {
                JarOutputStream outputJar = new JarOutputStream(outputFile);

                // For signing .apks, use the maximum compression to make
                // them as small as possible (since they live forever on
                // the system partition).  For OTA packages, use the
                // default compression level, which is much much faster
                // and produces output that is only a tiny bit larger
                // (~0.1% on full OTA packages I tested).
outputJar.setLevel(9);

                signFile(addDigestsToManifest(inputJar), inputJar,
                         publicKey, privateKey, outputJar);
                outputJar.close();
}
} catch (Exception e) {
e.printStackTrace();







