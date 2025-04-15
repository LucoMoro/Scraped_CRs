/*SignApk: perform the whole file signature in a single streaming pass.*/
//Synthetic comment -- diff --git a/tools/signapk/SignApk.java b/tools/signapk/SignApk.java
//Synthetic comment -- index 07aefa7..10562b9 100644

//Synthetic comment -- @@ -35,6 +35,7 @@
import org.bouncycastle.util.encoders.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
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
//Synthetic comment -- @@ -395,75 +371,6 @@
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

        // put a readable message and a null char at the start of the
        // archive comment, so that tools that display the comment
        // (hopefully) show something sensible.
        // TODO: anything more useful we can put in this message?
        byte[] message = "signed by SignApk".getBytes("UTF-8");
        temp.write(message);
        temp.write(0);

        writeSignatureBlock(new CMSByteArraySlice(zipData, 0, zipData.length-2),
                            publicKey, privateKey, temp);
        int total_size = temp.size() + 6;
        if (total_size > 0xffff) {
            throw new IllegalArgumentException("signature is too big for ZIP file comment");
        }
        // signature starts this many bytes from the end of the file
        int signature_start = total_size - message.length - 1;
        temp.write(signature_start & 0xff);
        temp.write((signature_start >> 8) & 0xff);
        // Why the 0xff bytes?  In a zip file with no archive comment,
        // bytes [-6:-2] of the file are the little-endian offset from
        // the start of the file to the central directory.  So for the
        // two high bytes to be 0xff 0xff, the archive would have to
        // be nearly 4GB in size.  So it's unlikely that a real
        // commentless archive would have 0xffs here, and lets us tell
        // an old signed archive from a new one.
        temp.write(0xff);
        temp.write(0xff);
        temp.write(total_size & 0xff);
        temp.write((total_size >> 8) & 0xff);
        temp.flush();

        // Signature verification checks that the EOCD header is the
        // last such sequence in the file (to avoid minzip finding a
        // fake EOCD appended after the signature in its scan).  The
        // odds of producing this sequence by chance are very low, but
        // let's catch it here if it does.
        byte[] b = temp.toByteArray();
        for (int i = 0; i < b.length-3; ++i) {
            if (b[i] == 0x50 && b[i+1] == 0x4b && b[i+2] == 0x05 && b[i+3] == 0x06) {
                throw new IllegalArgumentException("found spurious EOCD header at " + i);
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
//Synthetic comment -- @@ -499,6 +406,221 @@
}
}

public static void main(String[] args) {
if (args.length != 4 && args.length != 5) {
System.err.println("Usage: signapk [-w] " +
//Synthetic comment -- @@ -518,79 +640,33 @@
}

JarFile inputJar = null;
        JarOutputStream outputJar = null;
FileOutputStream outputFile = null;

try {
File publicKeyFile = new File(args[argstart+0]);
X509Certificate publicKey = readPublicKey(publicKeyFile);

            // Assume the certificate is valid for at least an hour.
            long timestamp = publicKey.getNotBefore().getTime() + 3600L * 1000;

PrivateKey privateKey = readPrivateKey(new File(args[argstart+1]));
inputJar = new JarFile(new File(args[argstart+2]), false);  // Don't verify.

            OutputStream outputStream = null;
            if (signWholeFile) {
                outputStream = new ByteArrayOutputStream();
            } else {
                outputStream = outputFile = new FileOutputStream(args[argstart+3]);
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
                addOtacert(outputJar, publicKeyFile, timestamp, manifest);
            }

            // MANIFEST.MF
            je = new JarEntry(JarFile.MANIFEST_NAME);
            je.setTime(timestamp);
            outputJar.putNextEntry(je);
            manifest.write(outputJar);

            // CERT.SF
            je = new JarEntry(CERT_SF_NAME);
            je.setTime(timestamp);
            outputJar.putNextEntry(je);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            writeSignatureFile(manifest, baos);
            byte[] signedData = baos.toByteArray();
            outputJar.write(signedData);

            // CERT.RSA
            je = new JarEntry(CERT_RSA_NAME);
            je.setTime(timestamp);
            outputJar.putNextEntry(je);
            writeSignatureBlock(new CMSProcessableByteArray(signedData),
                                publicKey, privateKey, outputJar);

            outputJar.close();
            outputJar = null;
            outputStream.flush();

            if (signWholeFile) {
                outputFile = new FileOutputStream(args[argstart+3]);
                signWholeOutputFile(((ByteArrayOutputStream)outputStream).toByteArray(),
                                    outputFile, publicKey, privateKey);
}
} catch (Exception e) {
e.printStackTrace();







