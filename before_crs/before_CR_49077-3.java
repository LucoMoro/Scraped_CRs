/*X509CertificateTest: test serialization

Serializing and deserializing should work for X509Certificate

Change-Id:Id32df1a50e25e48194702744dce6b9be4fd6929d*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java b/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java
//Synthetic comment -- index 461cde5..d0f540a 100644

//Synthetic comment -- @@ -26,6 +26,8 @@
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.KeyFactory;
//Synthetic comment -- @@ -108,6 +110,11 @@

private static final String CERTS_PKCS7_DER = "x509/certs-pk7.der";

private final X509Certificate getCertificate(CertificateFactory f, String name)
throws Exception {
final InputStream is = Support_Resources.getStream(name);
//Synthetic comment -- @@ -279,6 +286,7 @@
generateCertificates_X509_DER_TrailingData(f);
generateCertificates_PKCS7_PEM_TrailingData(f);
generateCertificates_PKCS7_DER_TrailingData(f);
} catch (Throwable e) {
out.append("Error encountered checking " + p.getName() + "\n");
e.printStackTrace(out);
//Synthetic comment -- @@ -1284,6 +1292,33 @@
}
}

@Override
protected void setUp() throws Exception {
super.setUp();







