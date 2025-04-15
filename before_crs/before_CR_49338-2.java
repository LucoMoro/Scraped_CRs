/*verifyCertificateChain should convert unknown exceptions to CertificateException

Bug:http://code.google.com/p/android/issues/detail?id=42533Change-Id:Id0e0eb8f007987decb4fee94135be8a92d2f8981*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLSocketImpl.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLSocketImpl.java
//Synthetic comment -- index 4cc16e6..2ca434d 100644

//Synthetic comment -- @@ -593,10 +593,8 @@

} catch (CertificateException e) {
throw e;
        } catch (RuntimeException e) {
            throw e;
} catch (Exception e) {
            throw new RuntimeException(e);
}
}









//Synthetic comment -- diff --git a/luni/src/test/java/libcore/javax/net/ssl/SSLSocketTest.java b/luni/src/test/java/libcore/javax/net/ssl/SSLSocketTest.java
//Synthetic comment -- index 4095081..342c92f 100644

//Synthetic comment -- @@ -45,11 +45,13 @@
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLProtocolException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509KeyManager;
import junit.framework.TestCase;
import libcore.java.security.StandardNames;
import libcore.java.security.TestKeyStore;
//Synthetic comment -- @@ -730,6 +732,47 @@
c.close();
}

public void test_SSLSocket_getEnableSessionCreation() throws Exception {
TestSSLContext c = TestSSLContext.create();
SSLSocket client = (SSLSocket) c.clientContext.getSocketFactory().createSocket(c.host,







