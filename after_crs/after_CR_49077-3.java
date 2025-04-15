/*X509CertificateTest: test serialization

Serializing and deserializing should work for X509Certificate

Change-Id:Id32df1a50e25e48194702744dce6b9be4fd6929d*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java b/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java
//Synthetic comment -- index 461cde5..d0f540a 100644

//Synthetic comment -- @@ -26,6 +26,8 @@
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.KeyFactory;
//Synthetic comment -- @@ -108,6 +110,11 @@

private static final String CERTS_PKCS7_DER = "x509/certs-pk7.der";

    /** A list of certs that are all slightly different. */
    private static final String[] VARIOUS_CERTS = new String[] {
            CERT_RSA, CERT_DSA, CERT_EC,
    };

private final X509Certificate getCertificate(CertificateFactory f, String name)
throws Exception {
final InputStream is = Support_Resources.getStream(name);
//Synthetic comment -- @@ -279,6 +286,7 @@
generateCertificates_X509_DER_TrailingData(f);
generateCertificates_PKCS7_PEM_TrailingData(f);
generateCertificates_PKCS7_DER_TrailingData(f);
                test_Serialization(f);
} catch (Throwable e) {
out.append("Error encountered checking " + p.getName() + "\n");
e.printStackTrace(out);
//Synthetic comment -- @@ -1284,6 +1292,33 @@
}
}

    private void test_Serialization(CertificateFactory f) throws Exception {
        for (String certName : VARIOUS_CERTS) {
            X509Certificate expected = getCertificate(f, certName);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            try {
                oos.writeObject(expected);
            } finally {
                oos.close();
            }

            byte[] certBytes = baos.toByteArray();

            ByteArrayInputStream bais = new ByteArrayInputStream(certBytes);
            try {
                ObjectInputStream ois = new ObjectInputStream(bais);

                X509Certificate actual = (X509Certificate) ois.readObject();

                assertEquals(certName, expected, actual);
            } finally {
                bais.close();
            }
        }
    }

@Override
protected void setUp() throws Exception {
super.setUp();







