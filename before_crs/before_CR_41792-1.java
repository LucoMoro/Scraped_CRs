/*Restore ability for SSLSocket.close() to interrupt reads and writes

SSLSocketTest.test_SSLSocket_interrupt didn't catch this regression so
added new test_SSLSocket_interrupt_read to cover this case
specifically.  Also cleanup SSLSocketTest to use Executors like
NativeCryptoTest instead of Threads for better error checking.

Bug: 7014266
Change-Id:I1160cd283310a0c6197cd3271a25830e0e2b1524*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLSocketImpl.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLSocketImpl.java
//Synthetic comment -- index 6334d41..065dd28 100644

//Synthetic comment -- @@ -915,11 +915,12 @@
}

synchronized (this) {
synchronized (writeLock) {
synchronized (readLock) {

                    NativeCrypto.SSL_interrupt(sslNativePointer);

// Shut down the SSL connection, per se.
try {
if (handshakeStarted) {








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/javax/net/ssl/SSLSocketTest.java b/luni/src/test/java/libcore/javax/net/ssl/SSLSocketTest.java
//Synthetic comment -- index 8b9cb11..8c9239e 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package libcore.javax.net.ssl;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
//Synthetic comment -- @@ -30,6 +31,10 @@
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.KeyManager;
//Synthetic comment -- @@ -38,7 +43,6 @@
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLProtocolException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
//Synthetic comment -- @@ -271,32 +275,28 @@
SSLSocket client = (SSLSocket) c.clientContext.getSocketFactory().createSocket(c.host,
c.port);
final SSLSocket server = (SSLSocket) c.serverSocket.accept();
        Thread thread = new Thread(new Runnable () {
            public void run() {
try {
                    server.startHandshake();
                    assertNotNull(server.getSession());
                    try {
                        server.getSession().getPeerCertificates();
                        fail();
                    } catch (SSLPeerUnverifiedException expected) {
                    }
                    Certificate[] localCertificates = server.getSession().getLocalCertificates();
                    assertNotNull(localCertificates);
                    TestKeyStore.assertChainLength(localCertificates);
                    assertNotNull(localCertificates[0]);
                    TestSSLContext.assertServerCertificateChain(c.serverTrustManager,
                                                                localCertificates);
                    TestSSLContext.assertCertificateInKeyStore(localCertificates[0],
                                                               c.serverKeyStore);
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
}
}
});
        thread.start();
client.startHandshake();
assertNotNull(client.getSession());
assertNull(client.getSession().getLocalCertificates());
//Synthetic comment -- @@ -307,7 +307,7 @@
TestSSLContext.assertServerCertificateChain(c.clientTrustManager,
peerCertificates);
TestSSLContext.assertCertificateInKeyStore(peerCertificates[0], c.serverKeyStore);
        thread.join();
client.close();
server.close();
c.close();
//Synthetic comment -- @@ -321,25 +321,24 @@
// RI used to throw SSLException on accept, now throws on startHandshake
if (StandardNames.IS_RI) {
final SSLSocket server = (SSLSocket) c.serverSocket.accept();
            Thread thread = new Thread(new Runnable () {
                public void run() {
try {
server.startHandshake();
} catch (SSLHandshakeException expected) {
                    } catch (RuntimeException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
}
}
});
            thread.start();
try {
client.startHandshake();
fail();
} catch (SSLHandshakeException expected) {
}
            thread.join();
server.close();
} else {
try {
//Synthetic comment -- @@ -359,20 +358,16 @@
SSLSocket client = (SSLSocket)
clientContext.getSocketFactory().createSocket(c.host, c.port);
final SSLSocket server = (SSLSocket) c.serverSocket.accept();
        Thread thread = new Thread(new Runnable () {
            public void run() {
                try {
                    server.startHandshake();
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
}
});
        thread.start();
client.startHandshake();
        thread.join();
client.close();
server.close();
c.close();
//Synthetic comment -- @@ -383,18 +378,14 @@
final SSLSocket client = (SSLSocket)
c.clientContext.getSocketFactory().createSocket(c.host, c.port);
final SSLSocket server = (SSLSocket) c.serverSocket.accept();
        Thread thread = new Thread(new Runnable () {
            public void run() {
                try {
                    server.startHandshake();
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
}
});
        thread.start();
final boolean[] handshakeCompletedListenerCalled = new boolean[1];
client.addHandshakeCompletedListener(new HandshakeCompletedListener() {
public void handshakeCompleted(HandshakeCompletedEvent event) {
//Synthetic comment -- @@ -472,7 +463,7 @@
}
});
client.startHandshake();
        thread.join();
if (!TestSSLContext.sslServerSocketSupportsSessionTickets()) {
assertNotNull(c.serverContext.getServerSessionContext().getSession(
client.getSession().getId()));
//Synthetic comment -- @@ -492,25 +483,21 @@
final SSLSocket client = (SSLSocket)
c.clientContext.getSocketFactory().createSocket(c.host, c.port);
final SSLSocket server = (SSLSocket) c.serverSocket.accept();
        Thread thread = new Thread(new Runnable () {
            public void run() {
                try {
                    server.startHandshake();
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
}
});
        thread.start();
client.addHandshakeCompletedListener(new HandshakeCompletedListener() {
public void handshakeCompleted(HandshakeCompletedEvent event) {
throw new RuntimeException("RuntimeException from handshakeCompleted");
}
});
client.startHandshake();
        thread.join();
client.close();
server.close();
c.close();
//Synthetic comment -- @@ -551,6 +538,46 @@
}
}

public void test_SSLSocket_setUseClientMode_afterHandshake() throws Exception {

// can't set after handshake
//Synthetic comment -- @@ -567,72 +594,25 @@
}
}

    private void test_SSLSocket_setUseClientMode(final boolean clientClientMode,
                                                 final boolean serverClientMode)
            throws Exception {
        TestSSLContext c = TestSSLContext.create();
        SSLSocket client = (SSLSocket) c.clientContext.getSocketFactory().createSocket(c.host,
                                                                                       c.port);
        final SSLSocket server = (SSLSocket) c.serverSocket.accept();

        final SSLHandshakeException[] sslHandshakeException = new SSLHandshakeException[1];
        final SocketTimeoutException[] socketTimeoutException = new SocketTimeoutException[1];
        Thread thread = new Thread(new Runnable () {
            public void run() {
                try {
                    if (!serverClientMode) {
                        server.setSoTimeout(1 * 1000);
                    }
                    server.setUseClientMode(serverClientMode);
                    server.startHandshake();
                } catch (SSLHandshakeException e) {
                    sslHandshakeException[0] = e;
                } catch (SocketTimeoutException e) {
                    socketTimeoutException[0] = e;
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
        if (!clientClientMode) {
            client.setSoTimeout(1 * 1000);
        }
        client.setUseClientMode(clientClientMode);
        client.startHandshake();
        thread.join();
        if (sslHandshakeException[0] != null) {
            throw sslHandshakeException[0];
        }
        if (socketTimeoutException[0] != null) {
            throw socketTimeoutException[0];
        }
        client.close();
        server.close();
        c.close();
    }

public void test_SSLSocket_untrustedServer() throws Exception {
TestSSLContext c = TestSSLContext.create(TestKeyStore.getClientCA2(),
TestKeyStore.getServer());
SSLSocket client = (SSLSocket) c.clientContext.getSocketFactory().createSocket(c.host,
c.port);
final SSLSocket server = (SSLSocket) c.serverSocket.accept();
        Thread thread = new Thread(new Runnable () {
            public void run() {
try {
server.startHandshake();
} catch (SSLHandshakeException expected) {
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
}
}
});
        thread.start();
try {
client.startHandshake();
fail();
//Synthetic comment -- @@ -641,7 +621,7 @@
}
client.close();
server.close();
        thread.join();
}

public void test_SSLSocket_clientAuth() throws Exception {
//Synthetic comment -- @@ -650,43 +630,38 @@
SSLSocket client = (SSLSocket) c.clientContext.getSocketFactory().createSocket(c.host,
c.port);
final SSLSocket server = (SSLSocket) c.serverSocket.accept();
        Thread thread = new Thread(new Runnable () {
            public void run() {
                try {
                    assertFalse(server.getWantClientAuth());
                    assertFalse(server.getNeedClientAuth());

                    // confirm turning one on by itself
                    server.setWantClientAuth(true);
                    assertTrue(server.getWantClientAuth());
                    assertFalse(server.getNeedClientAuth());

                    // confirm turning setting on toggles the other
                    server.setNeedClientAuth(true);
                    assertFalse(server.getWantClientAuth());
                    assertTrue(server.getNeedClientAuth());

                    // confirm toggling back
                    server.setWantClientAuth(true);
                    assertTrue(server.getWantClientAuth());
                    assertFalse(server.getNeedClientAuth());

                    server.startHandshake();

                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
}
});
        thread.start();
client.startHandshake();
assertNotNull(client.getSession().getLocalCertificates());
TestKeyStore.assertChainLength(client.getSession().getLocalCertificates());
TestSSLContext.assertClientCertificateChain(c.clientTrustManager,
client.getSession().getLocalCertificates());
        thread.join();
client.close();
server.close();
c.close();
//Synthetic comment -- @@ -727,22 +702,20 @@
SSLSocket client = (SSLSocket) clientContext.getSocketFactory().createSocket(c.host,
c.port);
final SSLSocket server = (SSLSocket) c.serverSocket.accept();
        Thread thread = new Thread(new Runnable () {
            public void run() {
try {
server.setNeedClientAuth(true);
server.startHandshake();
fail();
} catch (SSLHandshakeException expected) {
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
}
}
});

        thread.start();
try {
client.startHandshake();
fail();
//Synthetic comment -- @@ -750,7 +723,7 @@
// before we would get a NullPointerException from passing
// due to the null PrivateKey return by the X509KeyManager.
}
        thread.join();
client.close();
server.close();
c.close();
//Synthetic comment -- @@ -773,29 +746,25 @@
SSLSocket client = (SSLSocket) c.clientContext.getSocketFactory().createSocket(c.host,
c.port);
final SSLSocket server = (SSLSocket) c.serverSocket.accept();
        Thread thread = new Thread(new Runnable () {
            public void run() {
try {
                    server.setEnableSessionCreation(false);
                    try {
                        server.startHandshake();
                        fail();
                    } catch (SSLException expected) {
                    }
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
}
}
});
        thread.start();
try {
client.startHandshake();
fail();
} catch (SSLException expected) {
}
        thread.join();
client.close();
server.close();
c.close();
//Synthetic comment -- @@ -806,29 +775,25 @@
SSLSocket client = (SSLSocket) c.clientContext.getSocketFactory().createSocket(c.host,
c.port);
final SSLSocket server = (SSLSocket) c.serverSocket.accept();
        Thread thread = new Thread(new Runnable () {
            public void run() {
try {
                    try {
                        server.startHandshake();
                        fail();
                    } catch (SSLException expected) {
                    }
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
}
}
});
        thread.start();
client.setEnableSessionCreation(false);
try {
client.startHandshake();
fail();
} catch (SSLException expected) {
}
        thread.join();
client.close();
server.close();
c.close();
//Synthetic comment -- @@ -1012,32 +977,25 @@
c.host.getHostName(),
c.port,
false);
        Thread clientThread = new Thread(new Runnable () {
            public void run() {
                try {
                    try {
                        wrapping.startHandshake();
                        wrapping.getOutputStream().write(42);
                        // close the underlying socket,
                        // so that no SSL shutdown is sent
                        underlying.close();
                        wrapping.close();
                    } catch (SSLException expected) {
                    }
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
}
});
        clientThread.start();

SSLSocket server = (SSLSocket) c.serverSocket.accept();
server.startHandshake();
server.getInputStream().read();
// wait for thread to finish so we know client is closed.
        clientThread.join();
// close should cause an SSL_shutdown which will fail
// because the peer has closed, but it shouldn't throw.
server.close();
//Synthetic comment -- @@ -1127,17 +1085,15 @@

private void test_SSLSocket_interrupt_case(Socket toRead, final Socket toClose)
throws Exception {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1 * 1000);
                    toClose.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
}
        }.start();
try {
toRead.setSoTimeout(5 * 1000);
toRead.getInputStream().read();
//Synthetic comment -- @@ -1146,6 +1102,43 @@
throw e;
} catch (SocketException expected) {
}
}

public void test_TestSSLSocketPair_create() {








//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/xnet/provider/jsse/NativeCryptoTest.java b/luni/src/test/java/org/apache/harmony/xnet/provider/jsse/NativeCryptoTest.java
//Synthetic comment -- index 51c00fe..7a8b855 100644

//Synthetic comment -- @@ -577,7 +577,7 @@
ExecutorService executor = Executors.newSingleThreadExecutor();
Future<TestSSLHandshakeCallbacks> future = executor.submit(
new Callable<TestSSLHandshakeCallbacks>() {
            public TestSSLHandshakeCallbacks call() throws Exception {
Socket socket = (client
? new Socket(listener.getInetAddress(),
listener.getLocalPort())







