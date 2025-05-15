//<Beginning of snippet n. 0>


/** Write to another stream and also feed it to the Signature object. */
private static class SignatureOutputStream extends FilterOutputStream {
    private Signature mSignature;
    private static final int SIGNATURE_LENGTH_LIMIT = 1024;

    public SignatureOutputStream(OutputStream out, Signature sig) {
        super(out);
        this.mSignature = sig;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        try {
            super.write(b, off, len);
            mSignature.update(b, off, len);
            checkSignatureLength();
        } catch (Exception e) {
            throw new IOException("SignatureException: " + e.getMessage(), e);
        }
    }

    @Override
    public void write(int b) throws IOException {
        try {
            super.write(b);
            mSignature.update((byte) b);
            checkSignatureLength();
        } catch (Exception e) {
            throw new IOException("SignatureException: " + e.getMessage(), e);
        }
    }

    private void checkSignatureLength() throws IOException {
        if (mSignature.getLength() % SIGNATURE_LENGTH_LIMIT == 0) {
            super.write("\r\n".getBytes());
        }
    }
}

/** Writes a .SF file with a digest to the manifest. */
private void writeSignatureFile(OutputStream out) throws IOException, GeneralSecurityException {
    Manifest sf = new Manifest();
    Attributes main = sf.getMainAttributes();
    // Add necessary attributes and digests here

    sf.write(out);
    if (out.size() % 1024 == 0) {
        out.write("\r\n".getBytes());
    }
}

/** Write the certificate file with a digital signature. */

//<End of snippet n. 0>