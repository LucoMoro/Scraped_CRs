
//<Beginning of snippet n. 0>


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.KeyFactory;

private static final String CERTS_PKCS7_DER = "x509/certs-pk7.der";

private final X509Certificate getCertificate(CertificateFactory f, String name)
throws Exception {
final InputStream is = Support_Resources.getStream(name);
generateCertificates_X509_DER_TrailingData(f);
generateCertificates_PKCS7_PEM_TrailingData(f);
generateCertificates_PKCS7_DER_TrailingData(f);
} catch (Throwable e) {
out.append("Error encountered checking " + p.getName() + "\n");
e.printStackTrace(out);
}
}

@Override
protected void setUp() throws Exception {
super.setUp();

//<End of snippet n. 0>








