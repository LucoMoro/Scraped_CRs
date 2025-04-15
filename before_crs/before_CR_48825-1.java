/*Test for certificate generation stopping at right

Make sure that generateCertificate and generateCertificates end at the
right point when there is a end marker.

The support for this seems spotty at best. Various implementations have
broken support in different parts.

Change-Id:Ib8a3f7c451adca4e05fcb3e8409e93c319557497*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java b/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java
//Synthetic comment -- index 9950a10..461cde5 100644

//Synthetic comment -- @@ -268,11 +268,17 @@
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
//Synthetic comment -- @@ -1102,6 +1108,31 @@
assertEquals(Arrays.toString(cBytes), Arrays.toString(c.getEncoded()));
}

private void generateCertificates_X509_DER(CertificateFactory f) throws Exception {
/* DER-encoded list of certificates */
Collection<? extends X509Certificate> certs = getCertificates(f, CERTS_X509_DER);
//Synthetic comment -- @@ -1139,9 +1170,10 @@
final InputStream is = new ByteArrayInputStream(new byte[0]);

final Collection<? extends Certificate> certs;
try {
certs = f.generateCertificates(is);
            // DRLCertFactory is broken
if ("DRLCertFactory".equals(f.getProvider().getName())) {
fail("should throw when no certificates present");
}
//Synthetic comment -- @@ -1151,10 +1183,107 @@
}
throw e;
}
assertNotNull(certs);
assertEquals(0, certs.size());
}

@Override
protected void setUp() throws Exception {
super.setUp();







