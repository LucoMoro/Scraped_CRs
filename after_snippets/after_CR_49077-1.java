
//<Beginning of snippet n. 0>


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.security.Provider;
import java.security.Security;
assertEquals(entry1, entry2);
}

    private void test_Serialization(CertificateFactory f) throws Exception {
        X509CRL expected = getCRL(f, CRL_RSA);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(expected);
        oos.close();

        byte[] crlBytes = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(crlBytes);
        ObjectInputStream ois = new ObjectInputStream(bais);

        X509CRL actual = (X509CRL) ois.readObject();

        assertEquals(expected, actual);
    }

@Override
protected void setUp() throws Exception {
super.setUp();

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.KeyFactory;
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
        X509Certificate expected = getCertificate(f, CERT_RSA);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(expected);
        oos.close();

        byte[] certBytes = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(certBytes);
        ObjectInputStream ois = new ObjectInputStream(bais);

        X509Certificate actual = (X509Certificate) ois.readObject();

        assertEquals(expected, actual);
    }

@Override
protected void setUp() throws Exception {
super.setUp();

//<End of snippet n. 1>








