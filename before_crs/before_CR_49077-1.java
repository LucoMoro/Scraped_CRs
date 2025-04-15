/*X509{Certificate,CRL}Test: test serialization

Serializing and deserializing should work for these classes.

Change-Id:Id32df1a50e25e48194702744dce6b9be4fd6929d*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/cert/X509CRLTest.java b/luni/src/test/java/libcore/java/security/cert/X509CRLTest.java
//Synthetic comment -- index 161a8d5..5dc9833 100644

//Synthetic comment -- @@ -24,6 +24,8 @@
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.security.Provider;
import java.security.Security;
//Synthetic comment -- @@ -358,6 +360,25 @@
assertEquals(entry1, entry2);
}

@Override
protected void setUp() throws Exception {
super.setUp();








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java b/luni/src/test/java/libcore/java/security/cert/X509CertificateTest.java
//Synthetic comment -- index 461cde5..878b526 100644

//Synthetic comment -- @@ -26,6 +26,8 @@
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.KeyFactory;
//Synthetic comment -- @@ -279,6 +281,7 @@
generateCertificates_X509_DER_TrailingData(f);
generateCertificates_PKCS7_PEM_TrailingData(f);
generateCertificates_PKCS7_DER_TrailingData(f);
} catch (Throwable e) {
out.append("Error encountered checking " + p.getName() + "\n");
e.printStackTrace(out);
//Synthetic comment -- @@ -1284,6 +1287,25 @@
}
}

@Override
protected void setUp() throws Exception {
super.setUp();







