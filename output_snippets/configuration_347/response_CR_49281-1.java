//<Beginning of snippet n. 0>
if (ch == -1) {
    return Collections.emptyList();
} else if (second_asn1_tag == ASN1Constants.TAG_OID) {
    // additional processing logic
    // handles cases for ASN.1 OID
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
private void generateCertificates_Empty(CertificateFactory f) throws Exception {
    final InputStream is = new ByteArrayInputStream(new byte[0]);
    final Collection<? extends Certificate> certs = f.generateCertificates(is);
    
    assertNotNull(certs);
    assertEquals(0, certs.size());
}
//<End of snippet n. 1>