
//<Beginning of snippet n. 0>


/** Write to another stream and also feed it to the Signature object. */
private static class SignatureOutputStream extends FilterOutputStream {
private Signature mSignature;
        private int mCount = 0;

public SignatureOutputStream(OutputStream out, Signature sig) {
super(out);
throw new IOException("SignatureException: " + e);
}
super.write(b);
            mCount++;
}

@Override
throw new IOException("SignatureException: " + e);
}
super.write(b, off, len);
            mCount += len;
        }

        public int size() {
            return mCount;
}
}

}

/** Writes a .SF file with a digest to the manifest. */
    private void writeSignatureFile(SignatureOutputStream out)
throws IOException, GeneralSecurityException {
Manifest sf = new Manifest();
Attributes main = sf.getMainAttributes();
}

sf.write(out);

        // A bug in the java.util.jar implementation of Android platforms
        // up to version 1.6 will cause a spurious IOException to be thrown
        // if the length of the signature file is a multiple of 1024 bytes.
        // As a workaround, add an extra CRLF in this case.
        if ((out.size() % 1024) == 0) {
            out.write('\r');
            out.write('\n');
        }
}

/** Write the certificate file with a digital signature. */

//<End of snippet n. 0>








