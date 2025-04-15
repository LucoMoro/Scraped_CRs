/*Remove invalid X509Certificate2Test tests

The "CERT_CORRECT" is actually an invalid X.509 certificate, but these
test cases are now covered in more detail by
luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java

The decoding of the alternative names in the certificate look like this
via openssl command line:

            X509v3 Subject Alternative Name: critical
                0[..rfc@822.Name..dNSName..1.0...U.
..Organization..http://uniform.Resource.Id........*..\...

Change-Id:I41d4753b926715b093f558b41d102842e6b3a2da*/




//Synthetic comment -- diff --git a/luni/src/test/java/tests/security/cert/X509Certificate2Test.java b/luni/src/test/java/tests/security/cert/X509Certificate2Test.java
//Synthetic comment -- index 8595d77..2c9e2fb 100644

//Synthetic comment -- @@ -475,31 +475,6 @@
assertEquals(Integer.MAX_VALUE, generateCert(CERT_WITH_BASIC_CA_NO_PATH_LENGTH).getBasicConstraints());
}

public void testCertificateException() throws Exception {
try {
generateCert(CERT_TAMPERED);







