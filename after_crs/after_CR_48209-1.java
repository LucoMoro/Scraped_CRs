/*SignApk: perform the whole file signature in a single streaming pass.*/




//Synthetic comment -- diff --git a/tools/signapk/SignApk.java b/tools/signapk/SignApk.java
//Synthetic comment -- index 07aefa7..b33b349 100644

//Synthetic comment -- @@ -35,6 +35,7 @@
import org.bouncycastle.util.encoders.Base64;

import java.io.BufferedReader;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
//Synthetic comment -- @@ -339,31 +340,6 @@
}
}

/** Sign data and write the digital signature to 'out'. */
private static void writeSignatureBlock(
CMSTypedData data, X509Certificate publicKey, PrivateKey privateKey,
//Synthetic comment -- @@ -395,75 +371,6 @@
dos.writeObject(asn1.readObject());
}

/**
* Copy all the files in a manifest from input to output.  We set
* the modification times in the output to a fixed time, so as to
//Synthetic comment -- @@ -499,6 +406,221 @@
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

        public CMSSigner(JarFile inputJar, File publicKeyFile, X509Certificate publicKey, PrivateKey privateKey, OutputStream outputStream) {
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
                signFile(manifest, inputJar, publicKeyFile, publicKey, privateKey, outputJar);
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

        public void writeSignatureBlock(ByteArrayOutputStream temp) throws IOException,
               CertificateEncodingException,
               OperatorCreationException,
               CMSException {
            SignApk.writeSignatureBlock(this, publicKey, privateKey, temp);
        }

        public WholeFileSignerOutputStream getSigner() {
            return signer;
        }
    }

    public static void signWholeFile(JarFile inputJar, File publicKeyFile, X509Certificate publicKey, PrivateKey privateKey, OutputStream outputStream) throws Exception {
        CMSSigner cmsOut = new CMSSigner(inputJar, publicKeyFile, publicKey, privateKey, outputStream);

        ByteArrayOutputStream temp = new ByteArrayOutputStream();

        // put a readable message and a null char at the start of the
        // archive comment, so that tools that display the comment
        // (hopefully) show something sensible.
        // TODO: anything more useful we can put in this message?
        byte[] message = "signed by SignApk".getBytes("UTF-8");
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

        outputStream.write(total_size & 0xff);
        outputStream.write((total_size >> 8) & 0xff);
        temp.writeTo(outputStream);
    }

    public static void signFile(Manifest manifest, JarFile inputJar, File publicKeyFile, X509Certificate publicKey, PrivateKey privateKey, JarOutputStream outputJar) throws Exception {
        // Assume the certificate is valid for at least an hour.
        long timestamp = publicKey.getNotBefore().getTime() + 3600L * 1000;

        JarEntry je;

        // Everything else
        copyFiles(manifest, inputJar, outputJar, timestamp);

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
    }

public static void main(String[] args) {
if (args.length != 4 && args.length != 5) {
System.err.println("Usage: signapk [-w] " +
//Synthetic comment -- @@ -518,79 +640,33 @@
}

JarFile inputJar = null;
FileOutputStream outputFile = null;

try {
File publicKeyFile = new File(args[argstart+0]);
X509Certificate publicKey = readPublicKey(publicKeyFile);

PrivateKey privateKey = readPrivateKey(new File(args[argstart+1]));
inputJar = new JarFile(new File(args[argstart+2]), false);  // Don't verify.

            outputFile = new FileOutputStream(args[argstart+3]);

            if (signWholeFile) {
                SignApk.signWholeFile(inputJar, publicKeyFile, publicKey, privateKey, outputFile);
            }
            else {
                JarOutputStream outputJar = new JarOutputStream(outputFile);

                // For signing .apks, use the maximum compression to make
                // them as small as possible (since they live forever on
                // the system partition).  For OTA packages, use the
                // default compression level, which is much much faster
                // and produces output that is only a tiny bit larger
                // (~0.1% on full OTA packages I tested).
outputJar.setLevel(9);

                signFile(addDigestsToManifest(inputJar), inputJar, publicKeyFile, publicKey, privateKey, outputJar);
                outputJar.close();
}
} catch (Exception e) {
e.printStackTrace();







