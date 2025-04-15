/*Use most specific attributes for distinguished name display

Bug: 7894348
Bug:http://code.google.com/p/android/issues/detail?id=41662Change-Id:I8048a53b8a9a17b384f12b6a4f40071bb3dd3e04*/
//Synthetic comment -- diff --git a/core/java/android/net/http/SslCertificate.java b/core/java/android/net/http/SslCertificate.java
//Synthetic comment -- index fe6d4eb..5b60c0d 100644

//Synthetic comment -- @@ -334,9 +334,11 @@

/**
* A distinguished name helper class: a 3-tuple of:
     * - common name (CN),
     * - organization (O),
     * - organizational unit (OU)
*/
public class DName {
/**
//Synthetic comment -- @@ -360,8 +362,15 @@
private String mUName;

/**
         * Creates a new distinguished name
         * @param dName The distinguished name
*/
public DName(String dName) {
if (dName != null) {
//Synthetic comment -- @@ -374,18 +383,24 @@

for (int i = 0; i < oid.size(); i++) {
if (oid.elementAt(i).equals(X509Name.CN)) {
                            mCName = (String) val.elementAt(i);
continue;
}

if (oid.elementAt(i).equals(X509Name.O)) {
                            mOName = (String) val.elementAt(i);
                            continue;
}

if (oid.elementAt(i).equals(X509Name.OU)) {
                            mUName = (String) val.elementAt(i);
                            continue;
}
}
} catch (IllegalArgumentException ex) {
//Synthetic comment -- @@ -402,21 +417,21 @@
}

/**
         * @return The Common-name (CN) component of this name
*/
public String getCName() {
return mCName != null ? mCName : "";
}

/**
         * @return The Organization (O) component of this name
*/
public String getOName() {
return mOName != null ? mOName : "";
}

/**
         * @return The Organizational Unit (OU) component of this name
*/
public String getUName() {
return mUName != null ? mUName : "";








//Synthetic comment -- diff --git a/core/tests/coretests/src/android/net/http/SslCertificateTest.java b/core/tests/coretests/src/android/net/http/SslCertificateTest.java
//Synthetic comment -- index 147816b..6a30c6c 100644

//Synthetic comment -- @@ -45,11 +45,70 @@

@LargeTest
public void testSslCertificateWithEmptyIssuer() throws Exception {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate x509Certificate = (X509Certificate)
            certificateFactory.generateCertificate(new ByteArrayInputStream(Issue1597Certificate.getBytes()));
        assertEquals(x509Certificate.getIssuerDN().getName(), "");
SslCertificate sslCertificate = new SslCertificate(x509Certificate);
        assertEquals(sslCertificate.getIssuedBy().getDName(), "");
}
}







