/*Fix getIssuerAlternativeNames() test

The test of "getIssuerAlternativeNames()" actually called
"getSubjectAlternativeNames()" instead.

Change-Id:I9b47bb4f83349157086cb4b70fe930897f38859a*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java b/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java
//Synthetic comment -- index 4e1a3a8..9e0e2f8 100644

//Synthetic comment -- @@ -1025,7 +1025,7 @@

private void getIssuerAlternativeNames(CertificateFactory f) throws Exception {
X509Certificate c = getCertificate(f, CERT_RSA);
        Collection<List<?>> col = c.getSubjectAlternativeNames();

checkAlternativeNames(col);
}







