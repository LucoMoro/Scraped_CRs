//<Beginning of snippet n. 0>

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

// Serialization and Deserialization methods for X509Certificate
public class CertificateUtils {

    public static byte[] serializeCertificate(X509Certificate certificate) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(certificate);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new IOException("Error serializing X509Certificate: " + e.getMessage(), e);
        }
    }

    public static X509Certificate deserializeCertificate(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            return (X509Certificate) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IOException("Error deserializing X509Certificate: " + e.getMessage(), e);
        }
    }

    private static final String CERTS_PKCS7_DER = "x509/certs-pk7.der";

    private final X509Certificate getCertificate(CertificateFactory f, String name) throws Exception {
        try (InputStream is = Support_Resources.getStream(name)) {
            return (X509Certificate) f.generateCertificate(is);
        } catch (Throwable e) {
            out.append("Error encountered checking " + name + "\n");
            e.printStackTrace(out);
            throw new Exception("Failed to get certificate: " + e.getMessage(), e);
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
}

//<End of snippet n. 0>