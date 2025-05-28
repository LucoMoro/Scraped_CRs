
//<Beginning of snippet n. 0>


/** Write to another stream and also feed it to the Signature object. */
private static class SignatureOutputStream extends FilterOutputStream {
private Signature mSignature;

public SignatureOutputStream(OutputStream out, Signature sig) {
super(out);
throw new IOException("SignatureException: " + e);
}
super.write(b);
}

@Override
throw new IOException("SignatureException: " + e);
}
super.write(b, off, len);
}
}

}

/** Writes a .SF file with a digest to the manifest. */
    private void writeSignatureFile(OutputStream out)
throws IOException, GeneralSecurityException {
Manifest sf = new Manifest();
Attributes main = sf.getMainAttributes();
}

sf.write(out);
}

/** Write the certificate file with a digital signature. */

//<End of snippet n. 0>








