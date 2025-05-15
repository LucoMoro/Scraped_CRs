//<Beginning of snippet n. 0>

private static class SignatureOutputStream extends FilterOutputStream {
    private Signature mSignature;

    public SignatureOutputStream(OutputStream out, Signature sig) {
        super(out);
        this.mSignature = sig;
    }

    @Override
    public void write(byte[] b) throws IOException {
        try {
            super.write(b);
        } catch (IOException e) {
            throw new IOException("SignatureException: " + e.getMessage(), e);
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        try {
            super.write(b, off, len);
        } catch (IOException e) {
            throw new IOException("SignatureException: " + e.getMessage(), e);
        }
    }
}

/** Writes a .SF file with a digest to the manifest. */
private void writeSignatureFile(OutputStream out) throws IOException, GeneralSecurityException {
    Manifest sf = new Manifest();
    Attributes main = sf.getMainAttributes();
    
    // Existing logic to populate 'main' attributes goes here

    sf.write(out);
    if (out.size() % 1024 == 0) {
        out.write("\r\n".getBytes());
    }
}

//<End of snippet n. 0>