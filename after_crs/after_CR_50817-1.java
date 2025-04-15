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
                test_generateCertificate_InputStream_Offset_Correct(cf);
                test_generateCertificate_InputStream_Empty(cf);
} catch (Exception e) {
throw new Exception("Problem testing " + p.getName(), e);
}
//Synthetic comment -- @@ -113,7 +114,21 @@
}
}

    private void test_generateCertificate_InputStream_Empty(CertificateFactory cf) throws Exception {
        try {
            Certificate c = cf.generateCertificate(new ByteArrayInputStream(new byte[0]));
            if (!"BC".equals(cf.getProvider().getName())) {
                fail("should throw CertificateException: " + cf.getProvider().getName());
            }
            assertNull(c);
        } catch (CertificateException e) {
            if ("BC".equals(cf.getProvider().getName())) {
                fail("should return null: " + cf.getProvider().getName());
            }
        }
    }

    private void test_generateCertificate_InputStream_Offset_Correct(CertificateFactory cf)
throws Exception {
byte[] valid = VALID_CERTIFICATE_PEM.getBytes();









//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/cert/X509CRLTest.java b/luni/src/test/java/libcore/java/security/cert/X509CRLTest.java
//Synthetic comment -- index 161a8d5..ee7c15b 100644

//Synthetic comment -- @@ -41,7 +41,6 @@
import java.util.Set;

import junit.framework.TestCase;

public class X509CRLTest extends TestCase {
private Provider[] mX509Providers;
//Synthetic comment -- @@ -69,7 +68,9 @@
final InputStream is = Support_Resources.getStream(name);
assertNotNull("File does not exist: " + name, is);
try {
            X509Certificate c = (X509Certificate) f.generateCertificate(is);
            assertNotNull(c);
            return c;
} finally {
try {
is.close();
//Synthetic comment -- @@ -82,7 +83,9 @@
final InputStream is = Support_Resources.getStream(name);
assertNotNull("File does not exist: " + name, is);
try {
            X509CRL crl = (X509CRL) f.generateCRL(is);
            assertNotNull(crl);
            return crl;
} finally {
try {
is.close();
//Synthetic comment -- @@ -205,8 +208,8 @@
assertNotNull(lastUpdate);
assertNotNull(nextUpdate);

            assertDateEquals(lastUpdate, crl.getThisUpdate());
            assertDateEquals(nextUpdate, crl.getNextUpdate());
}

{
//Synthetic comment -- @@ -219,8 +222,8 @@
assertNotNull(lastUpdate);
assertNotNull(nextUpdate);

            assertDateEquals(lastUpdate, crl.getThisUpdate());
            assertDateEquals(nextUpdate, crl.getNextUpdate());
}
}

//Synthetic comment -- @@ -276,16 +279,27 @@

byte[] crlRsaBytes = getResourceAsBytes(CRL_RSA);

        assertEquals(Arrays.toString(crlRsaBytes), Arrays.toString(crlRsa.getEncoded()));
    }

    private static void assertDateEquals(Date date1, Date date2) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");

        String result1 = formatter.format(date1);
        String result2 = formatter.format(date2);

        assertEquals(result1, result2);
}

private void assertRsaCrl(CertificateFactory f, X509CRLEntry rsaEntry) throws Exception {
        assertNotNull(rsaEntry);

X509Certificate rsaCert = getCertificate(f, CERT_RSA);
Map<String, Date> dates = getCrlDates(CRL_RSA_DSA_DATES);
Date expectedDate = dates.get("lastUpdate");

assertEquals(rsaCert.getSerialNumber(), rsaEntry.getSerialNumber());
        assertDateEquals(expectedDate, rsaEntry.getRevocationDate());
assertNull(rsaEntry.getCertificateIssuer());
assertFalse(rsaEntry.hasExtensions());
assertNull(rsaEntry.getCriticalExtensionOIDs());
//Synthetic comment -- @@ -298,7 +312,7 @@
Date expectedDate = dates.get("lastUpdate");

assertEquals(dsaCert.getSerialNumber(), dsaEntry.getSerialNumber());
        assertDateEquals(expectedDate, dsaEntry.getRevocationDate());
assertNull(dsaEntry.getCertificateIssuer());
assertTrue(dsaEntry.hasExtensions());
/* TODO: get the OID */







