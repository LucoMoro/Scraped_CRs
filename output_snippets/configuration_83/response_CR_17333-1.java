//<Beginning of snippet n. 0>


/** Write to another stream and also feed it to the Signature object. */
private static class SignatureOutputStream extends FilterOutputStream {
    private Signature mSignature;
    
    public SignatureOutputStream(OutputStream out, Signature sig) throws IOException {
        super(out);
        this.mSignature = sig;
    }

    @Override
    public synchronized void write(byte[] b) throws IOException {
        try {
            if (b == null) {
                throw new IllegalArgumentException("Input byte array is null");
            }
            mSignature.update(b);
            super.write(b);
        } catch (IOException e) {
            throw new IOException("Error updating signature while writing bytes: " + e.getMessage(), e);
        }
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new IllegalArgumentException("Input byte array is null");
        }
        if (off < 0 || len < 0 || off + len > b.length) {
            throw new IndexOutOfBoundsException("Invalid offset or length parameters");
        }
        try {
            mSignature.update(b, off, len);
            super.write(b, off, len);
        } catch (IOException e) {
            throw new IOException("Error updating signature while writing bytes: " + e.getMessage(), e);
        }
    }
}

/** Writes a .SF file with a digest to the manifest. */
private void writeSignatureFile(OutputStream out) throws IOException, GeneralSecurityException {
    if (out == null) {
        throw new IllegalArgumentException("Output stream must not be null");
    }

    Manifest sf = new Manifest();
    Attributes main = sf.getMainAttributes();
    
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    
    sf.write(out);
    byte[] contentBytes = sf.toString().getBytes();
    digest.update(contentBytes);
    
    byte[] signatureDigest = digest.digest();
    
    int totalSize = contentBytes.length + signatureDigest.length;
    out.write(signatureDigest);
    
    if ((totalSize % 1024) == 0) {
        out.write("\r\n".getBytes()); // Append extra CRLF if needed
    }
}

//<End of snippet n. 0>