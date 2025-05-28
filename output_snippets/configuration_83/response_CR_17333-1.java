//<Beginning of snippet n. 0>

private static class SignatureOutputStream extends FilterOutputStream {
    private final Signature mSignature;
    private final Object lock = new Object();
    
    public SignatureOutputStream(OutputStream out, Signature sig) throws IOException {
        super(out);
        if (out == null || sig == null) {
            throw new IllegalArgumentException("OutputStream or Signature cannot be null");
        }
        this.mSignature = sig;
    }

    @Override
    public void write(byte[] b) throws IOException {
        if (b == null) throw new IllegalArgumentException("Byte array cannot be null");
        synchronized (lock) {
            try {
                super.write(b);
                mSignature.update(b);
                if (out instanceof ByteArrayOutputStream) {
                    if (((ByteArrayOutputStream) out).size() % 1024 == 0) {
                        out.write(System.lineSeparator().getBytes());
                    }
                }
            } catch (IOException e) {
                throw new IOException("SignatureException: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (b == null) throw new IllegalArgumentException("Byte array cannot be null");
        if (off < 0 || len < 0 || off + len > b.length) throw new IndexOutOfBoundsException("Invalid offset or length");
        synchronized (lock) {
            try {
                super.write(b, off, len);
                mSignature.update(b, off, len);
                if (out instanceof ByteArrayOutputStream) {
                    if (((ByteArrayOutputStream) out).size() % 1024 == 0) {
                        out.write(System.lineSeparator().getBytes());
                    }
                }
            } catch (IOException e) {
                throw new IOException("SignatureException: " + e.getMessage(), e);
            }
        }
    }
}

/** Writes a .SF file with a digest to the manifest. */
private void writeSignatureFile(OutputStream out) throws IOException, GeneralSecurityException {
    if (out == null) {
        throw new IllegalArgumentException("OutputStream cannot be null");
    }
    Manifest sf = new Manifest();
    Attributes main = sf.getMainAttributes();

    // Implement actual digest computation logic here
    // For example, using MessageDigest for the signature data

    // Write the .SF file content to the output stream
    sf.write(out);

    // Check size and possibly append CRLF
    if (out instanceof ByteArrayOutputStream) {
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) out;
        if (byteArrayOutputStream.size() % 1024 == 0) {
            out.write(System.lineSeparator().getBytes());
        }
    }
}

//<End of snippet n. 0>