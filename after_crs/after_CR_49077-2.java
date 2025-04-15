/*X509{Certificate,CRL}Test: test serialization

Serializing and deserializing should work for these classes.

Change-Id:Id32df1a50e25e48194702744dce6b9be4fd6929d*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/cert/X509CRLTest.java b/luni/src/test/java/libcore/java/security/cert/X509CRLTest.java
//Synthetic comment -- index 161a8d5..7da6f9d 100644

//Synthetic comment -- @@ -24,6 +24,8 @@
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.security.Provider;
import java.security.Security;
//Synthetic comment -- @@ -152,6 +154,7 @@
getSigAlgOID(f);
test_toString(f);
test_equals(f);
                test_Serialization(f);
} catch (Throwable e) {
out.append("Error encountered checking " + p.getName() + "\n");
e.printStackTrace(out);
//Synthetic comment -- @@ -358,6 +361,31 @@
assertEquals(entry1, entry2);
}

    private void test_Serialization(CertificateFactory f) throws Exception {
        X509CRL expected = getCRL(f, CRL_RSA);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        try {
            oos.writeObject(expected);
        } finally {
            oos.close();
        }

        byte[] crlBytes = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(crlBytes);
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);

            X509CRL actual = (X509CRL) ois.readObject();

            assertEquals(expected, actual);
        } finally {
            bais.close();
        }
    }

@Override
protected void setUp() throws Exception {
super.setUp();








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java b/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java
//Synthetic comment -- index 461cde5..96bb96c 100644

//Synthetic comment -- @@ -26,6 +26,8 @@
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.KeyFactory;
//Synthetic comment -- @@ -279,6 +281,7 @@
generateCertificates_X509_DER_TrailingData(f);
generateCertificates_PKCS7_PEM_TrailingData(f);
generateCertificates_PKCS7_DER_TrailingData(f);
                test_Serialization(f);
} catch (Throwable e) {
out.append("Error encountered checking " + p.getName() + "\n");
e.printStackTrace(out);
//Synthetic comment -- @@ -1284,6 +1287,31 @@
}
}

    private void test_Serialization(CertificateFactory f) throws Exception {
        X509Certificate expected = getCertificate(f, CERT_RSA);

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

            assertEquals(expected, actual);
        } finally {
            bais.close();
        }
    }

@Override
protected void setUp() throws Exception {
super.setUp();







