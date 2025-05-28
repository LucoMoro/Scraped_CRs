
//<Beginning of snippet n. 0>


hasUnsupportedCriticalExtension(f);
getEncoded(f);
verify(f);
generateCertificates_X509_PEM(f);
generateCertificates_X509_DER(f);
generateCertificates_PKCS7_PEM(f);
generateCertificates_PKCS7_DER(f);
generateCertificates_Empty(f);
} catch (Throwable e) {
out.append("Error encountered checking " + p.getName() + "\n");
e.printStackTrace(out);
assertEquals(Arrays.toString(cBytes), Arrays.toString(c.getEncoded()));
}

private void generateCertificates_X509_DER(CertificateFactory f) throws Exception {
/* DER-encoded list of certificates */
Collection<? extends X509Certificate> certs = getCertificates(f, CERTS_X509_DER);
final InputStream is = new ByteArrayInputStream(new byte[0]);

final Collection<? extends Certificate> certs;
try {
certs = f.generateCertificates(is);
            // DRLCertFactory is broken
if ("DRLCertFactory".equals(f.getProvider().getName())) {
fail("should throw when no certificates present");
}
}
throw e;
}
assertNotNull(certs);
assertEquals(0, certs.size());
}

@Override
protected void setUp() throws Exception {
super.setUp();

//<End of snippet n. 0>








