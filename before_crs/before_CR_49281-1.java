/*Fix Harmony's generateCertificates for empty input

The docs say that if an InputStream does not have any certificates, it
will return an empty collection.

Change-Id:I470c05c097de6c3fa80571294b31cf7ffd10f003*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/provider/cert/X509CertFactoryImpl.java b/luni/src/main/java/org/apache/harmony/security/provider/cert/X509CertFactoryImpl.java
//Synthetic comment -- index 7207002..5d02594 100644

//Synthetic comment -- @@ -197,7 +197,8 @@
// some Certificates have been read
return result;
} else if (ch == -1) {
                throw new CertificateException("There is no data in the stream");
}
// else: check if it is PKCS7
if (second_asn1_tag == ASN1Constants.TAG_OID) {








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java b/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java
//Synthetic comment -- index 461cde5..a5665d2 100644

//Synthetic comment -- @@ -1169,20 +1169,7 @@
private void generateCertificates_Empty(CertificateFactory f) throws Exception {
final InputStream is = new ByteArrayInputStream(new byte[0]);

        final Collection<? extends Certificate> certs;

        // DRLCertFactory is broken
        try {
            certs = f.generateCertificates(is);
            if ("DRLCertFactory".equals(f.getProvider().getName())) {
                fail("should throw when no certificates present");
            }
        } catch (CertificateException e) {
            if ("DRLCertFactory".equals(f.getProvider().getName())) {
                return;
            }
            throw e;
        }

assertNotNull(certs);
assertEquals(0, certs.size());







