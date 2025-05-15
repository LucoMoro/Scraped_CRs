
//<Beginning of snippet n. 0>


// some Certificates have been read
return result;
} else if (ch == -1) {
                /* No data in the stream, so return the empty collection. */
                return result;
}
// else: check if it is PKCS7
if (second_asn1_tag == ASN1Constants.TAG_OID) {

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


private void generateCertificates_Empty(CertificateFactory f) throws Exception {
final InputStream is = new ByteArrayInputStream(new byte[0]);

        final Collection<? extends Certificate> certs = f.generateCertificates(is);

assertNotNull(certs);
assertEquals(0, certs.size());

//<End of snippet n. 1>








