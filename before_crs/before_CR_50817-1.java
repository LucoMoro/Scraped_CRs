/*Add some more X.509 certificate tests

Add a behavior test to see what happens when CertificateFactory is fed
an empty stream.

Fix some date comparisons on X509CRLTest.

Change-Id:Ida9df52859e12c5aced0810cc625b58498d5fa32*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/cert/CertificateFactoryTest.java b/luni/src/test/java/libcore/java/security/cert/CertificateFactoryTest.java
//Synthetic comment -- index 58472ae..ee1e8e6 100644

//Synthetic comment -- @@ -93,7 +93,8 @@
CertificateFactory cf = CertificateFactory.getInstance("X509", p);
try {
test_generateCertificate(cf);
                testGenerateCertificate_InputStream_Offset_Correct(cf);
} catch (Exception e) {
throw new Exception("Problem testing " + p.getName(), e);
}
//Synthetic comment -- @@ -113,7 +114,21 @@
}
}

    private void testGenerateCertificate_InputStream_Offset_Correct(CertificateFactory cf)
throws Exception {
byte[] valid = VALID_CERTIFICATE_PEM.getBytes();









//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/cert/X509CRLTest.java b/luni/src/test/java/libcore/java/security/cert/X509CRLTest.java
//Synthetic comment -- index 161a8d5..ee7c15b 100644

//Synthetic comment -- @@ -41,7 +41,6 @@
import java.util.Set;

import junit.framework.TestCase;
import libcore.java.security.StandardNames;

public class X509CRLTest extends TestCase {
private Provider[] mX509Providers;
//Synthetic comment -- @@ -69,7 +68,9 @@
final InputStream is = Support_Resources.getStream(name);
assertNotNull("File does not exist: " + name, is);
try {
            return (X509Certificate) f.generateCertificate(is);
} finally {
try {
is.close();
//Synthetic comment -- @@ -82,7 +83,9 @@
final InputStream is = Support_Resources.getStream(name);
assertNotNull("File does not exist: " + name, is);
try {
            return (X509CRL) f.generateCRL(is);
} finally {
try {
is.close();
//Synthetic comment -- @@ -205,8 +208,8 @@
assertNotNull(lastUpdate);
assertNotNull(nextUpdate);

            assertEquals(lastUpdate, crl.getThisUpdate());
            assertEquals(nextUpdate, crl.getNextUpdate());
}

{
//Synthetic comment -- @@ -219,8 +222,8 @@
assertNotNull(lastUpdate);
assertNotNull(nextUpdate);

            assertEquals(lastUpdate, crl.getThisUpdate());
            assertEquals(nextUpdate, crl.getNextUpdate());
}
}

//Synthetic comment -- @@ -276,16 +279,27 @@

byte[] crlRsaBytes = getResourceAsBytes(CRL_RSA);

        assertEquals(Arrays.toString(crlRsa.getEncoded()), Arrays.toString(crlRsaBytes));
}

private void assertRsaCrl(CertificateFactory f, X509CRLEntry rsaEntry) throws Exception {
X509Certificate rsaCert = getCertificate(f, CERT_RSA);
Map<String, Date> dates = getCrlDates(CRL_RSA_DSA_DATES);
Date expectedDate = dates.get("lastUpdate");

assertEquals(rsaCert.getSerialNumber(), rsaEntry.getSerialNumber());
        assertEquals(expectedDate, rsaEntry.getRevocationDate());
assertNull(rsaEntry.getCertificateIssuer());
assertFalse(rsaEntry.hasExtensions());
assertNull(rsaEntry.getCriticalExtensionOIDs());
//Synthetic comment -- @@ -298,7 +312,7 @@
Date expectedDate = dates.get("lastUpdate");

assertEquals(dsaCert.getSerialNumber(), dsaEntry.getSerialNumber());
        assertEquals(expectedDate, dsaEntry.getRevocationDate());
assertNull(dsaEntry.getCertificateIssuer());
assertTrue(dsaEntry.hasExtensions());
/* TODO: get the OID */







