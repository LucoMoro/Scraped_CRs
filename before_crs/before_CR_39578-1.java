/*Remove another batch of Support_PortManager cruft.

Bug: 2441548
Change-Id:I24331a5e89690d6b2d5b7f4dba1616d8b55b374d*/
//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/javax/net/ssl/HandshakeCompletedEventTest.java b/luni/src/test/java/tests/api/javax/net/ssl/HandshakeCompletedEventTest.java
//Synthetic comment -- index 8a02f9c..c075cea 100644

//Synthetic comment -- @@ -42,7 +42,6 @@
import junit.framework.TestCase;
import libcore.io.Base64;
import org.apache.harmony.xnet.tests.support.mySSLSession;
import tests.support.Support_PortManager;

/**
* Tests for <code>HandshakeCompletedEvent</code> class constructors and methods.
//Synthetic comment -- @@ -50,7 +49,7 @@
*/
public class HandshakeCompletedEventTest extends TestCase {

    String certificate = "-----BEGIN CERTIFICATE-----\n"
+ "MIICZTCCAdICBQL3AAC2MA0GCSqGSIb3DQEBAgUAMF8xCzAJBgNVBAYTAlVTMSAw\n"
+ "HgYDVQQKExdSU0EgRGF0YSBTZWN1cml0eSwgSW5jLjEuMCwGA1UECxMlU2VjdXJl\n"
+ "IFNlcnZlciBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTAeFw05NzAyMjAwMDAwMDBa\n"
//Synthetic comment -- @@ -200,11 +199,10 @@
// TrustManager


    SSLSocket socket;
    SSLSocket serverSocket;
    MyHandshakeListener listener;
    int port = Support_PortManager.getNextPort();
    String host = "localhost";

private String PASSWORD = "android";

//Synthetic comment -- @@ -398,35 +396,34 @@

private boolean provideKeys;

        public TestServer(boolean provideKeys, int clientAuth, String keys) {
this.keys = keys;
this.clientAuth = clientAuth;
this.provideKeys = provideKeys;

trustManager = new TestTrustManager();
}

public void run() {
try {
                KeyManager[] keyManagers = provideKeys ? getKeyManagers(keys) : null;
                TrustManager[] trustManagers = new TrustManager[] { trustManager };

                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(keyManagers, trustManagers, null);

                SSLServerSocket serverSocket = (SSLServerSocket)
                        sslContext.getServerSocketFactory().createServerSocket();

                if (clientAuth == CLIENT_AUTH_WANTED) {
                    serverSocket.setWantClientAuth(true);
                } else if (clientAuth == CLIENT_AUTH_NEEDED) {
                    serverSocket.setNeedClientAuth(true);
                } else {
                    serverSocket.setWantClientAuth(false);
                }

                serverSocket.bind(new InetSocketAddress(port));

SSLSocket clientSocket = (SSLSocket)serverSocket.accept();

InputStream istream = clientSocket.getInputStream();
//Synthetic comment -- @@ -497,7 +494,7 @@

SSLSocket socket = (SSLSocket)sslContext.getSocketFactory().createSocket();

                socket.connect(new InetSocketAddress(port));
socket.addHandshakeCompletedListener(listener);
socket.startHandshake();









//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/javax/net/ssl/SSLServerSocketTest.java b/luni/src/test/java/tests/api/javax/net/ssl/SSLServerSocketTest.java
//Synthetic comment -- index cc96782..5086f65 100644

//Synthetic comment -- @@ -20,8 +20,6 @@

import libcore.io.Base64;

import tests.support.Support_PortManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
//Synthetic comment -- @@ -120,127 +118,78 @@
/**
* javax.net.ssl.SSLServerSocket#SSLServerSocket()
*/
    public void testConstructor_01() {
        try {
            SSLServerSocket ssl = new mySSLServerSocket();
        } catch (Exception ex) {
            fail("Unexpected exception was thrown " + ex);
        }
}

/**
* javax.net.ssl.SSLServerSocket#SSLServerSocket(int port)
*/
    public void testConstructor_02() {
        SSLServerSocket ssl;
        int portNumber = Support_PortManager.getNextPort();
int[] port_invalid = {-1, 65536, Integer.MIN_VALUE, Integer.MAX_VALUE};

        try {
            ssl = new mySSLServerSocket(portNumber);
            assertEquals(portNumber, ssl.getLocalPort());
        } catch (Exception ex) {
            fail("Unexpected exception was thrown " + ex);
        }

for (int i = 0; i < port_invalid.length; i++) {
try {
                ssl = new mySSLServerSocket(port_invalid[i]);
fail("IllegalArgumentException should be thrown");
            } catch (IllegalArgumentException iae) {
                //expected
            } catch (Exception e) {
                fail(e + " was thrown instead of IllegalArgumentException");
}
}

try {
            ssl = new mySSLServerSocket(portNumber);
            new mySSLServerSocket(portNumber);
fail("IOException Expected when opening an already opened port");
        } catch (IOException ioe) {
            // expected
        } catch (Exception ex) {
            fail("Unexpected exception was thrown " + ex);
}
}

/**
* javax.net.ssl.SSLServerSocket#SSLServerSocket(int port, int backlog)
*/
    public void testConstructor_03() {
        mySSLServerSocket ssl;
        int portNumber = Support_PortManager.getNextPort();
int[] port_invalid = {-1, Integer.MIN_VALUE, Integer.MAX_VALUE};

        try {
            ssl = new mySSLServerSocket(portNumber, 1);
            assertEquals(portNumber, ssl.getLocalPort());
        } catch (Exception ex) {
            fail("Unexpected exception was thrown");
        }

for (int i = 0; i < port_invalid.length; i++) {
try {
                ssl = new mySSLServerSocket(port_invalid[i], 1);
fail("IllegalArgumentException should be thrown");
            } catch (IllegalArgumentException iae) {
                // expected
            } catch (Exception e) {
                fail(e + " was thrown instead of IllegalArgumentException");
}
}

        portNumber = Support_PortManager.getNextPort();
try {
            ssl = new mySSLServerSocket(portNumber, 1);
            new mySSLServerSocket(portNumber, 1);
fail("IOException should be thrown");
        } catch (IOException ioe) {
}
}

/**
* javax.net.ssl.SSLServerSocket#SSLServerSocket(int port, int backlog, InetAddress address)
*/
    public void testConstructor_04() {
        mySSLServerSocket ssl;
        InetAddress ia = null;
        int portNumber = Support_PortManager.getNextPort();
int[] port_invalid = {-1, 65536, Integer.MIN_VALUE, Integer.MAX_VALUE};

        try {
            ssl = new mySSLServerSocket(portNumber, 0, ia);
            assertEquals(portNumber, ssl.getLocalPort());
        } catch (Exception ex) {
            fail("Unexpected exception was thrown");
        }

        portNumber = Support_PortManager.getNextPort();
        try {
            ssl = new mySSLServerSocket(portNumber, 0, InetAddress.getLocalHost());
            assertEquals(portNumber, ssl.getLocalPort());
        } catch (Exception ex) {
            fail("Unexpected exception was thrown");
        }

for (int i = 0; i < port_invalid.length; i++) {
try {
                ssl = new mySSLServerSocket(port_invalid[i], 1, InetAddress.getLocalHost());
fail("IllegalArgumentException should be thrown");
            } catch (IllegalArgumentException iae) {
                // expected
            } catch (Exception e) {
                fail(e + " was thrown instead of IllegalArgumentException");
}
}

        portNumber = Support_PortManager.getNextPort();
try {
           ssl = new mySSLServerSocket(portNumber, 0, InetAddress.getLocalHost());
           new mySSLServerSocket(portNumber, 0, InetAddress.getLocalHost());
           fail("IOException should be thrown for");
        } catch (IOException ioe) {
}
}









//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/javax/net/ssl/SSLSessionTest.java b/luni/src/test/java/tests/api/javax/net/ssl/SSLSessionTest.java
//Synthetic comment -- index ec23cae..5084422 100644

//Synthetic comment -- @@ -39,12 +39,7 @@
import libcore.io.Base64;
import tests.api.javax.net.ssl.HandshakeCompletedEventTest.MyHandshakeListener;
import tests.api.javax.net.ssl.HandshakeCompletedEventTest.TestTrustManager;
import tests.support.Support_PortManager;

/**
 * Tests for SSLSession class
 *
 */
public class SSLSessionTest extends TestCase {

// set to true if on Android, false if on RI
//Synthetic comment -- @@ -57,7 +52,7 @@
public void test_getPeerHost() throws Exception {
SSLSession s = clientSession;
assertEquals(InetAddress.getLocalHost().getHostName(), s.getPeerHost());
        assertEquals(port, s.getPeerPort());
}

/**
//Synthetic comment -- @@ -258,12 +253,10 @@
TestClient client;

@Override
    protected void setUp() {
        port = Support_PortManager.getNextPort();
String serverKeys = (useBKS ? SERVER_KEYS_BKS : SERVER_KEYS_JKS);
String clientKeys = (useBKS ? CLIENT_KEYS_BKS : CLIENT_KEYS_JKS);
        server = new TestServer(true,
                TestServer.CLIENT_AUTH_WANTED, serverKeys);
client = new TestClient(true, clientKeys);

serverThread = new Thread(server);
//Synthetic comment -- @@ -453,8 +446,7 @@
+ "NMGpCX6qmjbkJQLVK/Yfo1ePaUexPSOX0G9m8+DoV3iyNw6at01NRw==";


    int port;
    SSLSocket serverSocket;
MyHandshakeListener listener;
String host = "localhost";
boolean notFinished = true;
//Synthetic comment -- @@ -489,36 +481,35 @@

private KeyStore store;

        public TestServer(boolean provideKeys, int clientAuth, String keys) {
this.keys = keys;
this.clientAuth = clientAuth;
this.provideKeys = provideKeys;

trustManager = new TestTrustManager();
}

public void run() {
try {
                store = provideKeys ? getKeyStore(keys) : null;
                KeyManager[] keyManagers = store != null ? getKeyManagers(store) : null;
                TrustManager[] trustManagers = new TrustManager[] { trustManager };

                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(keyManagers, trustManagers, null);

                SSLServerSocket serverSocket = (SSLServerSocket)sslContext
                        .getServerSocketFactory().createServerSocket();

                if (clientAuth == CLIENT_AUTH_WANTED) {
                    serverSocket.setWantClientAuth(true);
                } else if (clientAuth == CLIENT_AUTH_NEEDED) {
                    serverSocket.setNeedClientAuth(true);
                } else {
                    serverSocket.setWantClientAuth(false);
                }

                serverSocket.bind(new InetSocketAddress(port));

SSLSocket clientSocket = (SSLSocket)serverSocket.accept();

InputStream istream = clientSocket.getInputStream();
//Synthetic comment -- @@ -589,7 +580,7 @@

SSLSocket socket = (SSLSocket)clientSslContext.getSocketFactory().createSocket();

                socket.connect(new InetSocketAddress(port));
OutputStream ostream = socket.getOutputStream();
ostream.write(testData.getBytes());
ostream.flush();








//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/javax/net/ssl/SSLSocketTest.java b/luni/src/test/java/tests/api/javax/net/ssl/SSLSocketTest.java
//Synthetic comment -- index ab60f72..b4cbde2 100644

//Synthetic comment -- @@ -38,7 +38,6 @@
import junit.framework.TestCase;
import libcore.io.Base64;
import tests.api.javax.net.ssl.HandshakeCompletedEventTest.TestTrustManager;
import tests.support.Support_PortManager;
import libcore.java.security.StandardNames;

public class SSLSocketTest extends TestCase {
//Synthetic comment -- @@ -51,7 +50,7 @@
/**
* javax.net.ssl.SSLSocket#SSLSocket()
*/
    public void testConstructor_01() throws Exception {
SSLSocket ssl = getSSLSocket();
assertNotNull(ssl);
ssl.close();
//Synthetic comment -- @@ -60,7 +59,7 @@
/**
* javax.net.ssl.SSLSocket#SSLSocket(InetAddress address, int port)
*/
    public void testConstructor_02() throws UnknownHostException, IOException {
int sport = startServer("Cons InetAddress,I");
int[] invalidPort = {-1, Integer.MIN_VALUE, 65536, Integer.MAX_VALUE};

//Synthetic comment -- @@ -88,15 +87,13 @@
* javax.net.ssl.SSLSocket#SSLSocket(InetAddress address, int port,
*                                          InetAddress clientAddress, int clientPort)
*/
    public void testConstructor_03() throws UnknownHostException, IOException {
int sport = startServer("Cons InetAddress,I,InetAddress,I");
        int portNumber = Support_PortManager.getNextPort();

SSLSocket ssl = getSSLSocket(InetAddress.getLocalHost(), sport,
                                     InetAddress.getLocalHost(), portNumber);
assertNotNull(ssl);
assertEquals(sport, ssl.getPort());
        assertEquals(portNumber, ssl.getLocalPort());
ssl.close();

try {
//Synthetic comment -- @@ -137,7 +134,7 @@
/**
* javax.net.ssl.SSLSocket#SSLSocket(String host, int port)
*/
    public void testConstructor_04() throws UnknownHostException, IOException {
int sport = startServer("Cons String,I");
int[] invalidPort = {-1, Integer.MIN_VALUE, 65536, Integer.MAX_VALUE};

//Synthetic comment -- @@ -171,28 +168,25 @@
* javax.net.ssl.SSLSocket#SSLSocket(String host, int port, InetAddress clientAddress,
*           int clientPort)
*/
    public void testConstructor_05() throws UnknownHostException, IOException {
int sport = startServer("Cons String,I,InetAddress,I");
        int portNumber = Support_PortManager.getNextPort();
int[] invalidPort = {-1, Integer.MIN_VALUE, 65536, Integer.MAX_VALUE};

SSLSocket ssl = getSSLSocket(InetAddress.getLocalHost().getHostName(), sport,
                                     InetAddress.getLocalHost(), portNumber);
assertNotNull(ssl);
assertEquals(sport, ssl.getPort());
        assertEquals(portNumber, ssl.getLocalPort());

try {
            getSSLSocket("localhost", 8081, InetAddress.getLocalHost(), 8082);
fail();
} catch (IOException expected) {
}

for (int i = 0; i < invalidPort.length; i++) {
            portNumber = Support_PortManager.getNextPort();
try {
getSSLSocket(InetAddress.getLocalHost().getHostName(), invalidPort[i],
                             InetAddress.getLocalHost(), portNumber);
fail();
} catch (IllegalArgumentException expected) {
}
//Synthetic comment -- @@ -204,9 +198,8 @@
}
}

        portNumber = Support_PortManager.getNextPort();
try {
            getSSLSocket("bla-bla", sport, InetAddress.getLocalHost(), portNumber);
fail();
} catch (UnknownHostException expected) {
}
//Synthetic comment -- @@ -422,12 +415,10 @@
ssl.close();
}

    boolean useBKS = !StandardNames.IS_RI;

private String PASSWORD = "android";

    private int port = Support_PortManager.getNextPort();

private boolean serverReady = false;

/**
//Synthetic comment -- @@ -551,7 +542,7 @@
SSLServerSocket serverSocket = (SSLServerSocket)
sslContext.getServerSocketFactory().createServerSocket();
try {
                    serverSocket.bind(new InetSocketAddress(port));
sport = serverSocket.getLocalPort();
serverReady = true;








