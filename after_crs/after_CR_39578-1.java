/*Remove another batch of Support_PortManager cruft.

Bug: 2441548
Change-Id:I24331a5e89690d6b2d5b7f4dba1616d8b55b374d*/




//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/javax/net/ssl/HandshakeCompletedEventTest.java b/luni/src/test/java/tests/api/javax/net/ssl/HandshakeCompletedEventTest.java
//Synthetic comment -- index 8a02f9c..c075cea 100644

//Synthetic comment -- @@ -42,7 +42,6 @@
import junit.framework.TestCase;
import libcore.io.Base64;
import org.apache.harmony.xnet.tests.support.mySSLSession;

/**
* Tests for <code>HandshakeCompletedEvent</code> class constructors and methods.
//Synthetic comment -- @@ -50,7 +49,7 @@
*/
public class HandshakeCompletedEventTest extends TestCase {

    private String certificate = "-----BEGIN CERTIFICATE-----\n"
+ "MIICZTCCAdICBQL3AAC2MA0GCSqGSIb3DQEBAgUAMF8xCzAJBgNVBAYTAlVTMSAw\n"
+ "HgYDVQQKExdSU0EgRGF0YSBTZWN1cml0eSwgSW5jLjEuMCwGA1UECxMlU2VjdXJl\n"
+ "IFNlcnZlciBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTAeFw05NzAyMjAwMDAwMDBa\n"
//Synthetic comment -- @@ -200,11 +199,10 @@
// TrustManager


    private SSLSocket socket;
    private SSLServerSocket serverSocket;
    private MyHandshakeListener listener;
    private String host = "localhost";

private String PASSWORD = "android";

//Synthetic comment -- @@ -398,35 +396,34 @@

private boolean provideKeys;

        public TestServer(boolean provideKeys, int clientAuth, String keys) throws Exception {
this.keys = keys;
this.clientAuth = clientAuth;
this.provideKeys = provideKeys;

trustManager = new TestTrustManager();

            KeyManager[] keyManagers = provideKeys ? getKeyManagers(keys) : null;
            TrustManager[] trustManagers = new TrustManager[] { trustManager };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagers, trustManagers, null);

            serverSocket = (SSLServerSocket) sslContext.getServerSocketFactory().createServerSocket();

            if (clientAuth == CLIENT_AUTH_WANTED) {
                serverSocket.setWantClientAuth(true);
            } else if (clientAuth == CLIENT_AUTH_NEEDED) {
                serverSocket.setNeedClientAuth(true);
            } else {
                serverSocket.setWantClientAuth(false);
            }

            serverSocket.bind(new InetSocketAddress(0));
}

public void run() {
try {
SSLSocket clientSocket = (SSLSocket)serverSocket.accept();

InputStream istream = clientSocket.getInputStream();
//Synthetic comment -- @@ -497,7 +494,7 @@

SSLSocket socket = (SSLSocket)sslContext.getSocketFactory().createSocket();

                socket.connect(serverSocket.getLocalSocketAddress());
socket.addHandshakeCompletedListener(listener);
socket.startHandshake();









//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/javax/net/ssl/SSLServerSocketTest.java b/luni/src/test/java/tests/api/javax/net/ssl/SSLServerSocketTest.java
//Synthetic comment -- index cc96782..5086f65 100644

//Synthetic comment -- @@ -20,8 +20,6 @@

import libcore.io.Base64;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
//Synthetic comment -- @@ -120,127 +118,78 @@
/**
* javax.net.ssl.SSLServerSocket#SSLServerSocket()
*/
    public void testConstructor() throws Exception {
        SSLServerSocket ssl = new mySSLServerSocket();
}

/**
* javax.net.ssl.SSLServerSocket#SSLServerSocket(int port)
*/
    public void testConstructor_I() throws Exception {
int[] port_invalid = {-1, 65536, Integer.MIN_VALUE, Integer.MAX_VALUE};

        SSLServerSocket ssl = new mySSLServerSocket(0);

for (int i = 0; i < port_invalid.length; i++) {
try {
                new mySSLServerSocket(port_invalid[i]);
fail("IllegalArgumentException should be thrown");
            } catch (IllegalArgumentException expected) {
}
}

try {
            new mySSLServerSocket(ssl.getLocalPort());
fail("IOException Expected when opening an already opened port");
        } catch (IOException expected) {
}
}

/**
* javax.net.ssl.SSLServerSocket#SSLServerSocket(int port, int backlog)
*/
    public void testConstructor_II() throws Exception {
        mySSLServerSocket ssl = new mySSLServerSocket(0, 1);
int[] port_invalid = {-1, Integer.MIN_VALUE, Integer.MAX_VALUE};

for (int i = 0; i < port_invalid.length; i++) {
try {
                new mySSLServerSocket(port_invalid[i], 1);
fail("IllegalArgumentException should be thrown");
            } catch (IllegalArgumentException expected) {
}
}

try {
            new mySSLServerSocket(ssl.getLocalPort(), 1);
fail("IOException should be thrown");
        } catch (IOException expected) {
}
}

/**
* javax.net.ssl.SSLServerSocket#SSLServerSocket(int port, int backlog, InetAddress address)
*/
    public void testConstructor_IIInetAddress() throws Exception {
        // A null InetAddress is okay.
        new mySSLServerSocket(0, 0, null);

int[] port_invalid = {-1, 65536, Integer.MIN_VALUE, Integer.MAX_VALUE};

        mySSLServerSocket ssl = new mySSLServerSocket(0, 0, InetAddress.getLocalHost());

for (int i = 0; i < port_invalid.length; i++) {
try {
                new mySSLServerSocket(port_invalid[i], 1, InetAddress.getLocalHost());
fail("IllegalArgumentException should be thrown");
            } catch (IllegalArgumentException expected) {
}
}

try {
            new mySSLServerSocket(ssl.getLocalPort(), 0, InetAddress.getLocalHost());
            fail("IOException should be thrown for");
        } catch (IOException expected) {
}
}









//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/javax/net/ssl/SSLSessionTest.java b/luni/src/test/java/tests/api/javax/net/ssl/SSLSessionTest.java
//Synthetic comment -- index ec23cae..5084422 100644

//Synthetic comment -- @@ -39,12 +39,7 @@
import libcore.io.Base64;
import tests.api.javax.net.ssl.HandshakeCompletedEventTest.MyHandshakeListener;
import tests.api.javax.net.ssl.HandshakeCompletedEventTest.TestTrustManager;

public class SSLSessionTest extends TestCase {

// set to true if on Android, false if on RI
//Synthetic comment -- @@ -57,7 +52,7 @@
public void test_getPeerHost() throws Exception {
SSLSession s = clientSession;
assertEquals(InetAddress.getLocalHost().getHostName(), s.getPeerHost());
        assertEquals(serverSocket.getLocalPort(), s.getPeerPort());
}

/**
//Synthetic comment -- @@ -258,12 +253,10 @@
TestClient client;

@Override
    protected void setUp() throws Exception {
String serverKeys = (useBKS ? SERVER_KEYS_BKS : SERVER_KEYS_JKS);
String clientKeys = (useBKS ? CLIENT_KEYS_BKS : CLIENT_KEYS_JKS);
        server = new TestServer(true, TestServer.CLIENT_AUTH_WANTED, serverKeys);
client = new TestClient(true, clientKeys);

serverThread = new Thread(server);
//Synthetic comment -- @@ -453,8 +446,7 @@
+ "NMGpCX6qmjbkJQLVK/Yfo1ePaUexPSOX0G9m8+DoV3iyNw6at01NRw==";


    SSLServerSocket serverSocket;
MyHandshakeListener listener;
String host = "localhost";
boolean notFinished = true;
//Synthetic comment -- @@ -489,36 +481,35 @@

private KeyStore store;

        public TestServer(boolean provideKeys, int clientAuth, String keys) throws Exception {
this.keys = keys;
this.clientAuth = clientAuth;
this.provideKeys = provideKeys;

trustManager = new TestTrustManager();

            store = provideKeys ? getKeyStore(keys) : null;
            KeyManager[] keyManagers = store != null ? getKeyManagers(store) : null;
            TrustManager[] trustManagers = new TrustManager[] { trustManager };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagers, trustManagers, null);

            serverSocket = (SSLServerSocket)sslContext.getServerSocketFactory().createServerSocket();

            if (clientAuth == CLIENT_AUTH_WANTED) {
                serverSocket.setWantClientAuth(true);
            } else if (clientAuth == CLIENT_AUTH_NEEDED) {
                serverSocket.setNeedClientAuth(true);
            } else {
                serverSocket.setWantClientAuth(false);
            }

            serverSocket.bind(null);
}

public void run() {
try {
SSLSocket clientSocket = (SSLSocket)serverSocket.accept();

InputStream istream = clientSocket.getInputStream();
//Synthetic comment -- @@ -589,7 +580,7 @@

SSLSocket socket = (SSLSocket)clientSslContext.getSocketFactory().createSocket();

                socket.connect(serverSocket.getLocalSocketAddress());
OutputStream ostream = socket.getOutputStream();
ostream.write(testData.getBytes());
ostream.flush();








//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/javax/net/ssl/SSLSocketTest.java b/luni/src/test/java/tests/api/javax/net/ssl/SSLSocketTest.java
//Synthetic comment -- index ab60f72..b4cbde2 100644

//Synthetic comment -- @@ -38,7 +38,6 @@
import junit.framework.TestCase;
import libcore.io.Base64;
import tests.api.javax.net.ssl.HandshakeCompletedEventTest.TestTrustManager;
import libcore.java.security.StandardNames;

public class SSLSocketTest extends TestCase {
//Synthetic comment -- @@ -51,7 +50,7 @@
/**
* javax.net.ssl.SSLSocket#SSLSocket()
*/
    public void testConstructor() throws Exception {
SSLSocket ssl = getSSLSocket();
assertNotNull(ssl);
ssl.close();
//Synthetic comment -- @@ -60,7 +59,7 @@
/**
* javax.net.ssl.SSLSocket#SSLSocket(InetAddress address, int port)
*/
    public void testConstructor_InetAddressI() throws Exception {
int sport = startServer("Cons InetAddress,I");
int[] invalidPort = {-1, Integer.MIN_VALUE, 65536, Integer.MAX_VALUE};

//Synthetic comment -- @@ -88,15 +87,13 @@
* javax.net.ssl.SSLSocket#SSLSocket(InetAddress address, int port,
*                                          InetAddress clientAddress, int clientPort)
*/
    public void testConstructor_InetAddressIInetAddressI() throws Exception {
int sport = startServer("Cons InetAddress,I,InetAddress,I");

SSLSocket ssl = getSSLSocket(InetAddress.getLocalHost(), sport,
                                     InetAddress.getLocalHost(), 0);
assertNotNull(ssl);
assertEquals(sport, ssl.getPort());
ssl.close();

try {
//Synthetic comment -- @@ -137,7 +134,7 @@
/**
* javax.net.ssl.SSLSocket#SSLSocket(String host, int port)
*/
    public void testConstructor_StringI() throws Exception {
int sport = startServer("Cons String,I");
int[] invalidPort = {-1, Integer.MIN_VALUE, 65536, Integer.MAX_VALUE};

//Synthetic comment -- @@ -171,28 +168,25 @@
* javax.net.ssl.SSLSocket#SSLSocket(String host, int port, InetAddress clientAddress,
*           int clientPort)
*/
    public void testConstructor_StringIInetAddressI() throws Exception {
int sport = startServer("Cons String,I,InetAddress,I");
int[] invalidPort = {-1, Integer.MIN_VALUE, 65536, Integer.MAX_VALUE};

SSLSocket ssl = getSSLSocket(InetAddress.getLocalHost().getHostName(), sport,
                                     InetAddress.getLocalHost(), 0);
assertNotNull(ssl);
assertEquals(sport, ssl.getPort());

try {
            getSSLSocket(InetAddress.getLocalHost().getHostName(), 8081, InetAddress.getLocalHost(), 8082);
fail();
} catch (IOException expected) {
}

for (int i = 0; i < invalidPort.length; i++) {
try {
getSSLSocket(InetAddress.getLocalHost().getHostName(), invalidPort[i],
                             InetAddress.getLocalHost(), 0);
fail();
} catch (IllegalArgumentException expected) {
}
//Synthetic comment -- @@ -204,9 +198,8 @@
}
}

try {
            getSSLSocket("bla-bla", sport, InetAddress.getLocalHost(), 0);
fail();
} catch (UnknownHostException expected) {
}
//Synthetic comment -- @@ -422,12 +415,10 @@
ssl.close();
}

    private boolean useBKS = !StandardNames.IS_RI;

private String PASSWORD = "android";

private boolean serverReady = false;

/**
//Synthetic comment -- @@ -551,7 +542,7 @@
SSLServerSocket serverSocket = (SSLServerSocket)
sslContext.getServerSocketFactory().createServerSocket();
try {
                    serverSocket.bind(new InetSocketAddress(0));
sport = serverSocket.getLocalPort();
serverReady = true;








