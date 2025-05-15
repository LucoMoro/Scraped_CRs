
//<Beginning of snippet n. 0>


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.KeyFactory;

private static final String CERTS_PKCS7_DER = "x509/certs-pk7.der";

    /** A list of certs that are all slightly different. */
    private static final String[] VARIOUS_CERTS = new String[] {
            CERT_RSA, CERT_DSA, CERT_EC,
    };

private final X509Certificate getCertificate(CertificateFactory f, String name)
throws Exception {
final InputStream is = Support_Resources.getStream(name);
generateCertificates_X509_DER_TrailingData(f);
generateCertificates_PKCS7_PEM_TrailingData(f);
generateCertificates_PKCS7_DER_TrailingData(f);
                test_Serialization(f);
} catch (Throwable e) {
out.append("Error encountered checking " + p.getName() + "\n");
e.printStackTrace(out);
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

//<End of snippet n. 0>








