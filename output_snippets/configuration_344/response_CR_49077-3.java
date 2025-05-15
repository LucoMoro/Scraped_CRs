//<Beginning of snippet n. 0>

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import org.junit.Test;
import static org.junit.Assert.*;

private static final String CERTS_PKCS7_DER = "x509/certs-pk7.der";

private final X509Certificate getCertificate(CertificateFactory f, String name)
        throws Exception {
    try (InputStream is = Support_Resources.getStream(name)) {
        // Certificates generation logic here
        generateCertificates_X509_DER_TrailingData(f);
        generateCertificates_PKCS7_PEM_TrailingData(f);
        generateCertificates_PKCS7_DER_TrailingData(f);
        // Assume certificate extraction logic from input stream
    } catch (Throwable e) {
        out.append("Error encountered checking " + name + "\n");
        e.printStackTrace(out);
    }
    return null; // Placeholder return to satisfy compilation
}

private void writeObject(ObjectOutputStream out) throws IOException {
    // Serialize relevant fields of the X509Certificate
    out.defaultWriteObject();
}

private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // Deserialize relevant fields of the X509Certificate
    in.defaultReadObject();
}

@Test
public void testSerialization() throws Exception {
    X509Certificate originalCert = getCertificate(CertificateFactory.getInstance("X.509"), CERTS_PKCS7_DER);

    // Serialize
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
        oos.writeObject(originalCert);
    }

    // Deserialize
    X509Certificate deserializedCert;
    try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()))) {
        deserializedCert = (X509Certificate) ois.readObject();
    }

    // Verify equality
    assertEquals(originalCert.getSubjectDN(), deserializedCert.getSubjectDN());
    assertEquals(originalCert.getIssuerDN(), deserializedCert.getIssuerDN());
    assertEquals(originalCert.getSerialNumber(), deserializedCert.getSerialNumber());
    assertTrue(originalCert.equals(deserializedCert));
}

//<End of snippet n. 0>