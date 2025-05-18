//<Beginning of snippet n. 0>

hasUnsupportedCriticalExtension(f);
getEncoded(f);
verify(f);
generateCertificates_X509_PEM(f);
generateCertificates_X509_DER(f);
generateCertificates_PKCS7_PEM(f);
generateCertificates_PKCS7_DER(f);
generateCertificates_Empty(f);
} catch (CertificateException e) {
    logger.error("Error encountered checking " + p.getName(), e);
    throw new CertificateGenerationException("Failure during certificate generation", e);
} catch (IOException e) {
    logger.error("I/O error encountered during certificate generation", e);
    throw new CertificateGenerationException("I/O error during certificate generation", e);
} catch (IllegalArgumentException e) {
    logger.error("Illegal argument error encountered during certificate generation", e);
    throw new CertificateGenerationException("Illegal argument error during certificate generation", e);
} catch (RuntimeException e) {
    logger.error("Runtime error encountered during certificate generation", e);
    throw new CertificateGenerationException("Unexpected error during certificate generation", e);
}
assertNotNull(cBytes);
assertEquals(Arrays.toString(cBytes), Arrays.toString(c.getEncoded()));
}

private void generateCertificates_X509_DER(CertificateFactory f) throws CertificateException {
    /* DER-encoded list of certificates */
    Collection<? extends X509Certificate> certs = getCertificates(f, CERTS_X509_DER);
    final InputStream is = new ByteArrayInputStream(new byte[0]);

    if (!is.markSupported()) {
        throw new CertificateGenerationException("InputStream does not support marking");
    }
    
    try {
        is.mark(0);  // arbitrary mark limit
        certs = f.generateCertificates(is);
        is.reset();
        if ("DRLCertFactory".equals(f.getProvider().getName())) {
            fail("should throw when no certificates present");
        }
    } catch (CertificateException e) {
        throw new CertificateGenerationException("Error generating certificates", e);
    } finally {
        is.close();
    }
    assertNotNull(certs);
    assertEquals(0, certs.size());
}

@Override
protected void setUp() throws Exception {
    super.setUp();
//<End of snippet n. 0>