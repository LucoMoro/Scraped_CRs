//<Beginning of snippet n. 0>
hasUnsupportedCriticalExtension(f);
getEncoded(f);
verify(f);
generateCertificates_X509_PEM(f);
generateCertificates_X509_DER(f);
generateCertificates_PKCS7_PEM(f);
generateCertificates_PKCS7_DER(f);
generateCertificates_Empty(f);
} catch (SpecificCertificateException e) {
    logger.error("Error encountered checking " + p.getName(), e);
}
assertEquals(Arrays.toString(cBytes), Arrays.toString(c.getEncoded()));
}

private void generateCertificates_X509_DER(CertificateFactory f) throws SpecificCertificateException {
    Collection<? extends X509Certificate> certs = getCertificates(f, CERTS_X509_DER);
    final InputStream is = new ByteArrayInputStream(new byte[0]);
    
    try {
        certs = f.generateCertificates(is);
        if ("DRLCertFactory".equals(f.getProvider().getName())) {
            fail("should throw when no certificates present");
        }
    } catch (CertificateException e) {
        throw new SpecificCertificateException("Certificate generation failed", e);
    }
    
    assertNotNull(certs);
    assertEquals(0, certs.size());
}

@Override
protected void setUp() throws SpecificSetupException {
    super.setUp();
//<End of snippet n. 0>