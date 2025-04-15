/*Add tests for CertificateFactory byte offset

Make sure that CertificateFactory ends at the place that it should when
reading an InputStream that supports mark and reset.

Change-Id:I3bc20c1e9766f80f1597908707e69d65a6c3b216*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/cert/CertificateFactoryTest.java b/luni/src/test/java/libcore/java/security/cert/CertificateFactoryTest.java
//Synthetic comment -- index e9a91dd..58472ae 100644

//Synthetic comment -- @@ -17,6 +17,10 @@
package libcore.java.security.cert;

import java.io.ByteArrayInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
//Synthetic comment -- @@ -84,10 +88,18 @@
+ "-----END CERTIFICATE-----";

public void test_generateCertificate() throws Exception {
        test_generateCertificate(CertificateFactory.getInstance("X509"));
        test_generateCertificate(CertificateFactory.getInstance("X509", "BC"));
        test_generateCertificate(CertificateFactory.getInstance("X509", "DRLCertFactory"));
}
private void test_generateCertificate(CertificateFactory cf) throws Exception {
byte[] valid = VALID_CERTIFICATE_PEM.getBytes();
Certificate c = cf.generateCertificate(new ByteArrayInputStream(valid));
//Synthetic comment -- @@ -101,4 +113,91 @@
}
}

}








//Synthetic comment -- diff --git a/luni/src/test/java/tests/security/cert/X509Certificate2Test.java b/luni/src/test/java/tests/security/cert/X509Certificate2Test.java
//Synthetic comment -- index 58cc100..8595d77 100644

//Synthetic comment -- @@ -51,8 +51,9 @@

// extension value is empty sequence
byte[] extnValue = pemCert.getExtensionValue("2.5.29.35");
        assertTrue(Arrays
                .equals(new byte[] {0x04, 0x02, 0x30, 0x00}, extnValue));
assertNotNull(pemCert.toString());
// End regression for HARMONY-3384
}
//Synthetic comment -- @@ -499,7 +500,7 @@
assertEquals(0, coll.size());
}

    public void testCerficateException() throws Exception {
try {
generateCert(CERT_TAMPERED);
fail();







