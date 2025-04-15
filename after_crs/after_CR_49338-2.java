/*verifyCertificateChain should convert unknown exceptions to CertificateException

Bug:http://code.google.com/p/android/issues/detail?id=42533Change-Id:Id0e0eb8f007987decb4fee94135be8a92d2f8981*/




//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLSocketImpl.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLSocketImpl.java
//Synthetic comment -- index 4cc16e6..2ca434d 100644

//Synthetic comment -- @@ -593,10 +593,8 @@

} catch (CertificateException e) {
throw e;
} catch (Exception e) {
            throw new CertificateException(e);
}
}









//Synthetic comment -- diff --git a/luni/src/test/java/libcore/javax/net/ssl/SSLSocketTest.java b/luni/src/test/java/libcore/javax/net/ssl/SSLSocketTest.java
//Synthetic comment -- index 4095081..342c92f 100644

//Synthetic comment -- @@ -45,11 +45,13 @@
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLProtocolException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;
import junit.framework.TestCase;
import libcore.java.security.StandardNames;
import libcore.java.security.TestKeyStore;
//Synthetic comment -- @@ -730,6 +732,47 @@
c.close();
}

    public void test_SSLSocket_TrustManagerRuntimeException() throws Exception {
        TestSSLContext c = TestSSLContext.create();
        SSLContext clientContext = SSLContext.getInstance("TLS");
        X509TrustManager trustManager = new X509TrustManager() {
            @Override public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
                throw new AssertionError();
            }
            @Override public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
                throw new RuntimeException();  // throw a RuntimeException from custom TrustManager
            }
            @Override public X509Certificate[] getAcceptedIssuers() {
                throw new AssertionError();
            }
        };
        clientContext.init(null, new TrustManager[] { trustManager }, null);
        SSLSocket client = (SSLSocket) clientContext.getSocketFactory().createSocket(c.host,
                                                                                     c.port);
        final SSLSocket server = (SSLSocket) c.serverSocket.accept();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Void> future = executor.submit(new Callable<Void>() {
            @Override public Void call() throws Exception {
                server.startHandshake();
                return null;
            }
        });

        executor.shutdown();
        try {
            client.startHandshake();
            fail();
        } catch (SSLHandshakeException expected) {
            // before we would get a RuntimeException from checkServerTrusted.
        }
        future.get();
        client.close();
        server.close();
        c.close();
    }

public void test_SSLSocket_getEnableSessionCreation() throws Exception {
TestSSLContext c = TestSSLContext.create();
SSLSocket client = (SSLSocket) c.clientContext.getSocketFactory().createSocket(c.host,







