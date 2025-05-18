//<Beginning of snippet n. 0>

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;

private static final Logger logger = Logger.getLogger(YourClassName.class.getName());
private static final String CERTS_PKCS7_DER = "x509/certs-pk7.der";

private final X509Certificate getCertificate(CertificateFactory f, String name) throws IOException, CertificateException {
    try (InputStream is = Support_Resources.getStream(name)) {
        return (X509Certificate) f.generateCertificate(is);
    } catch (IOException | CertificateException e) {
        logger.log(Level.WARNING, "Exception encountered checking " + name);
        throw e; // Rethrow to allow caller to handle it
    }
}

@Test
public void testSerialization() throws Exception {
    CertificateFactory factory = CertificateFactory.getInstance("X.509");
    X509Certificate originalCert = getCertificate(factory, CERTS_PKCS7_DER);

    byte[] serializedCert = originalCert.getEncoded();
    X509Certificate deserializedCert = (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(serializedCert));

    Assert.assertEquals(originalCert.getSubjectDN(), deserializedCert.getSubjectDN());
    Assert.assertEquals(originalCert.getIssuerDN(), deserializedCert.getIssuerDN());
    Assert.assertEquals(originalCert.getSerialNumber(), deserializedCert.getSerialNumber());
    Assert.assertArrayEquals(originalCert.getSignature(), deserializedCert.getSignature());
}

@Override
protected void setUp() throws Exception {
    super.setUp();
//<End of snippet n. 0>