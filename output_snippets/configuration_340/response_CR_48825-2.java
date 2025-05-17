//<Beginning of snippet n. 0>
try {
    hasUnsupportedCriticalExtension(f);
    getEncoded(f);
    verify(f);
    generateCertificates_X509_PEM(f);
    generateCertificates_X509_DER(f);
    generateCertificates_PKCS7_PEM(f);
    generateCertificates_PKCS7_DER(f);
    generateCertificates_Empty(f);
} catch (CertificateException e) {
    out.append("Error encountered checking " + p.getName() + "\n");
    Logger.logError("Error checking certificate: ", e);
    assertEquals(Arrays.toString(cBytes), Arrays.toString(c.getEncoded()));
} catch (IOException e) {
    out.append("I/O error encountered: " + e.getMessage() + "\n");
    Logger.logError("I/O error: ", e);
}

private void generateCertificates_X509_DER(CertificateFactory f) throws CertificateException, IOException {
    Collection<? extends X509Certificate> certs = getCertificates(f, CERTS_X509_DER);
    try (InputStream is = new ByteArrayInputStream(new byte[0])) {
        certs = f.generateCertificates(is);
        if ("DRLCertFactory".equals(f.getProvider().getName())) {
            fail("should throw when no certificates present");
        }
    }
    assertNotNull(certs);
    assertEquals(0, certs.size());
}

@Override
protected void setUp() throws Exception {
    super.setUp();
//<End of snippet n. 0>