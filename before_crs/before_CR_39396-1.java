/*Start removing Support_PortManager.

A bad idea, badly implemented.

Bug: 2441548
Change-Id:I34c990f6fd9d746771846f186a7ab3ab59e78a9f*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/net/OldDatagramPacketTest.java b/luni/src/test/java/libcore/java/net/OldDatagramPacketTest.java
//Synthetic comment -- index a77a44d..8ca4067 100644

//Synthetic comment -- @@ -21,71 +21,40 @@
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import tests.support.Support_PortManager;

public class OldDatagramPacketTest extends junit.framework.TestCase {

    DatagramPacket dp;

    volatile boolean started = false;

    public void test_getPort() throws IOException {
        dp = new DatagramPacket("Hello".getBytes(), 5, InetAddress.getLocalHost(), 1000);
assertEquals("Incorrect port returned", 1000, dp.getPort());

        InetAddress localhost = InetAddress.getByName("localhost");

        int[] ports = Support_PortManager.getNextPortsForUDP(2);
        final int port = ports[0];
        final Object lock = new Object();

Thread thread = new Thread(new Runnable() {
public void run() {
                DatagramSocket socket = null;
try {
                    socket = new DatagramSocket(port);
                    synchronized (lock) {
                        started = true;
                        lock.notifyAll();
                    }
                    socket.setSoTimeout(3000);
                    DatagramPacket packet = new DatagramPacket(new byte[256],
                            256);
                    socket.receive(packet);
                    socket.send(packet);
                    socket.close();
} catch (IOException e) {
System.out.println("thread exception: " + e);
                    if (socket != null)
                        socket.close();
}
}
});
thread.start();

        DatagramSocket socket = null;
try {
            socket = new DatagramSocket(ports[1]);
            socket.setSoTimeout(3000);
            DatagramPacket packet = new DatagramPacket(new byte[] { 1, 2, 3, 4,
                    5, 6 }, 6, localhost, port);
            synchronized (lock) {
                try {
                    if (!started)
                        lock.wait();
                } catch (InterruptedException e) {
                    fail(e.toString());
                }
            }
            socket.send(packet);
            socket.receive(packet);
            socket.close();
            assertTrue("datagram received wrong port: " + packet.getPort(),
                    packet.getPort() == port);
} finally {
            if (socket != null) {
                socket.close();
            }
}
}

//Synthetic comment -- @@ -104,7 +73,7 @@
}

public void test_setData$BII() {
        dp = new DatagramPacket("Hello".getBytes(), 5);
try {
dp.setData(null, 2, 3);
fail("NullPointerException was not thrown.");
//Synthetic comment -- @@ -113,7 +82,7 @@
}

public void test_setData$B() {
        dp = new DatagramPacket("Hello".getBytes(), 5);
try {
dp.setData(null);
fail("NullPointerException was not thrown.");








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/net/OldServerSocketTest.java b/luni/src/test/java/libcore/java/net/OldServerSocketTest.java
//Synthetic comment -- index cf35489..6518897 100644

//Synthetic comment -- @@ -33,7 +33,6 @@
import java.nio.channels.ServerSocketChannel;
import java.security.Permission;
import java.util.Properties;
import tests.support.Support_PortManager;

public class OldServerSocketTest extends OldSocketTestCase {

//Synthetic comment -- @@ -86,10 +85,9 @@
}

public void test_ConstructorII() throws IOException {
        int freePortNumber = Support_PortManager.getNextPort();
        s = new ServerSocket(freePortNumber, 1);
s.setSoTimeout(2000);
        startClient(freePortNumber);
sconn = s.accept();
sconn.close();
s.close();
//Synthetic comment -- @@ -133,10 +131,9 @@
}

public void test_ConstructorI() throws Exception {
        int portNumber = Support_PortManager.getNextPort();
        s = new ServerSocket(portNumber);
try {
            new ServerSocket(portNumber);
fail("IOException was not thrown.");
} catch(IOException ioe) {
//expected
//Synthetic comment -- @@ -162,11 +159,9 @@
}

public void test_ConstructorIILjava_net_InetAddress() throws IOException {
        int freePortNumber = Support_PortManager.getNextPort();

        ServerSocket ss = new ServerSocket(freePortNumber, 10, InetAddress.getLocalHost());
try {
            new ServerSocket(freePortNumber, 10, InetAddress.getLocalHost());
fail("IOException was not thrown.");
} catch(IOException expected) {
}
//Synthetic comment -- @@ -217,9 +212,7 @@
}

public void test_accept() throws IOException {
        int portNumber = Support_PortManager.getNextPort();

        ServerSocket newSocket = new ServerSocket(portNumber);
newSocket.setSoTimeout(500);
try {
Socket accepted = newSocket.accept();








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/net/OldURLClassLoaderTest.java b/luni/src/test/java/libcore/java/net/OldURLClassLoaderTest.java
//Synthetic comment -- index 2646f98..3a5608c 100644

//Synthetic comment -- @@ -35,7 +35,6 @@
import java.util.jar.Manifest;
import org.apache.harmony.security.tests.support.TestCertUtils;
import tests.support.Support_Configuration;
import tests.support.Support_PortManager;
import tests.support.Support_TestWebData;
import tests.support.Support_TestWebServer;
import tests.support.resource.Support_Resources;
//Synthetic comment -- @@ -210,13 +209,12 @@

@SideEffect("Support_TestWebServer requires isolation.")
public void test_findResourceLjava_lang_String() throws Exception {
        int port = Support_PortManager.getNextPort();
File tmp = File.createTempFile("test", ".txt");

Support_TestWebServer server = new Support_TestWebServer();
try {

            server.initServer(port, tmp.getAbsolutePath(), "text/html");

URL[] urls = { new URL("http://localhost:" + port + "/") };
ucl = new URLClassLoader(urls);
//Synthetic comment -- @@ -244,9 +242,8 @@
tempFile2.deleteOnExit();

Support_TestWebServer server = new Support_TestWebServer();
        int port = Support_PortManager.getNextPort();
try {
            server.initServer(port, false);

String tempPath1 = tempFile1.getParentFile().getAbsolutePath() + "/";
InputStream is = getClass().getResourceAsStream(








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/nio/channels/OldServerSocketChannelTest.java b/luni/src/test/java/libcore/java/nio/channels/OldServerSocketChannelTest.java
//Synthetic comment -- index fb63512..51f288a 100644

//Synthetic comment -- @@ -25,26 +25,17 @@
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import junit.framework.TestCase;
import tests.support.Support_PortManager;

/*
 * test for ServerSocketChannel
 */
public class OldServerSocketChannelTest extends TestCase {

private static final int TIME_UNIT = 200;

    private InetSocketAddress localAddr1;

private ServerSocketChannel serverChannel;

private SocketChannel clientChannel;

protected void setUp() throws Exception {
super.setUp();
        this.localAddr1 = new InetSocketAddress(
                "127.0.0.1", Support_PortManager
                        .getNextPort());
this.serverChannel = ServerSocketChannel.open();
this.clientChannel = SocketChannel.open();
}
//Synthetic comment -- @@ -87,7 +78,7 @@
public void test_accept_Block_NoConnect_interrupt() throws IOException {
assertTrue(this.serverChannel.isBlocking());
ServerSocket gotSocket = this.serverChannel.socket();
        gotSocket.bind(localAddr1);

class MyThread extends Thread {
public String errMsg = null;








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/nio/channels/OldSocketChannelTest.java b/luni/src/test/java/libcore/java/nio/channels/OldSocketChannelTest.java
//Synthetic comment -- index f182375..750be35 100644

//Synthetic comment -- @@ -35,11 +35,7 @@
import java.nio.channels.UnsupportedAddressTypeException;
import java.nio.channels.spi.SelectorProvider;
import junit.framework.TestCase;
import tests.support.Support_PortManager;

/**
 * Tests for SocketChannel and its default implementation.
 */
public class OldSocketChannelTest extends TestCase {

private static final int CAPACITY_NORMAL = 200;
//Synthetic comment -- @@ -58,11 +54,10 @@

protected void setUp() throws Exception {
super.setUp();
        this.localAddr1 = new InetSocketAddress("127.0.0.1",
                Support_PortManager.getNextPort());
this.channel1 = SocketChannel.open();
this.channel2 = SocketChannel.open();
        this.server1 = new ServerSocket(localAddr1.getPort());
}

protected void tearDown() throws Exception {
//Synthetic comment -- @@ -292,12 +287,9 @@
server.start();
Thread.currentThread().sleep(1000);

        InetSocketAddress address = new InetSocketAddress(InetAddress
                .getByName("localhost"), port);

// First test with array based byte buffer
SocketChannel sc = SocketChannel.open();
        sc.connect(address);

ByteBuffer buf = ByteBuffer.allocate(data.length);
buf.limit(data.length / 2);
//Synthetic comment -- @@ -313,7 +305,7 @@

// Now test with direct byte buffer
sc = SocketChannel.open();
        sc.connect(address);

buf = ByteBuffer.allocateDirect(data.length);
buf.limit(data.length / 2);
//Synthetic comment -- @@ -339,17 +331,15 @@
}

public static boolean done = false;
    public static int port = Support_PortManager.getNextPort();
public static byte[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

static class ServerThread extends Thread {
        @Override
        public void run() {
try {
                ServerSocketChannel ssc = ServerSocketChannel.open();
                InetSocketAddress addr = new InetSocketAddress(InetAddress
                        .getByAddress(new byte[] {0, 0, 0, 0}), port);
                ssc.socket().bind(addr, 0);

ByteBuffer buf = ByteBuffer.allocate(10);
buf.put(data);
//Synthetic comment -- @@ -363,6 +353,10 @@
// ignore
}
}
}

class MockSocketChannel extends SocketChannel {








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/util/OldScannerTest.java b/luni/src/test/java/libcore/java/util/OldScannerTest.java
//Synthetic comment -- index b51cfbc..9bac1f4 100644

//Synthetic comment -- @@ -28,7 +28,6 @@
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import junit.framework.TestCase;
import tests.support.Support_PortManager;

public final class OldScannerTest extends TestCase {

//Synthetic comment -- @@ -492,15 +491,12 @@
assertEquals(102400, matchResult.end());
}

    public void test_Constructor_LReadableByteChannel()
            throws IOException {
        InetSocketAddress localAddr = new InetSocketAddress("127.0.0.1",
                Support_PortManager.getNextPort());
ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(localAddr);

SocketChannel sc = SocketChannel.open();
        sc.connect(localAddr);
sc.configureBlocking(false);
assertFalse(sc.isBlocking());









//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/luni/tests/java/net/URLConnectionTest.java b/luni/src/test/java/org/apache/harmony/luni/tests/java/net/URLConnectionTest.java
//Synthetic comment -- index 9f414c3..ab675e2 100644

//Synthetic comment -- @@ -19,7 +19,6 @@
import dalvik.annotation.BrokenTest;
import junit.framework.TestCase;
import tests.support.Support_Configuration;
import tests.support.Support_PortManager;
import tests.support.Support_TestWebData;
import tests.support.Support_TestWebServer;
import tests.support.resource.Support_Resources;
//Synthetic comment -- @@ -222,9 +221,8 @@

//        ftpURL = new URL(Support_Configuration.testFTPURL);

        port = Support_PortManager.getNextPort();
server = new Support_TestWebServer();
        server.initServer(port, false);
url = new URL("http://localhost:" + port + "/test1");
uc = url.openConnection();
url2 =  new URL("http://localhost:" + port + "/test2");








//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/javax/net/ssl/SSLSocketFactoryTest.java b/luni/src/test/java/tests/api/javax/net/ssl/SSLSocketFactoryTest.java
//Synthetic comment -- index 02abcc2..0d91116 100644

//Synthetic comment -- @@ -25,16 +25,13 @@

import junit.framework.TestCase;

import tests.support.Support_PortManager;

public class SSLSocketFactoryTest extends TestCase {

private ServerSocket ss;

protected int startServer(String name) {
        int portNumber = Support_PortManager.getNextPort();
try {
            ss = new ServerSocket(portNumber);
} catch (IOException e) {
fail(name + ": " + e);
}








//Synthetic comment -- diff --git a/support/src/test/java/tests/support/Support_TestWebServer.java b/support/src/test/java/tests/support/Support_TestWebServer.java
//Synthetic comment -- index 4d6b0d1..5f3cd85 100644

//Synthetic comment -- @@ -94,51 +94,21 @@
}

/**
     * Initialize a new server with default port and timeout.
     * @param log Set true if you want trace output
     */
    public int initServer(boolean log) throws Exception {
        return initServer(0, DEFAULT_TIMEOUT, log);
    }

    /**
     * Initialize a new server with default timeout.
     * @param port Sets the server to listen on this port, or 0 to let the OS choose.
     *             Hard-coding ports is evil, so always pass 0.
     * @param log Set true if you want trace output
     */
    public int initServer(int port, boolean log) throws Exception {
        return initServer(port, DEFAULT_TIMEOUT, log);
    }

    /**
     * Initialize a new server with default timeout and disabled log.
     * @param port Sets the server to listen on this port, or 0 to let the OS choose.
     *             Hard-coding ports is evil, so always pass 0.
* @param servePath the path to the dynamic web test data
* @param contentType the type of the dynamic web test data
*/
    public int initServer(int port, String servePath, String contentType)
            throws Exception {
Support_TestWebData.initDynamicTestWebData(servePath, contentType);
        return initServer(port, DEFAULT_TIMEOUT, false);
}

    /**
     * Initialize a new server with default port and timeout.
     * @param port Sets the server to listen on this port, or 0 to let the OS choose.
     *             Hard-coding ports is evil, so always pass 0.
     * @param timeout Indicates the period of time to wait until a socket is
     *                closed
     * @param log Set true if you want trace output
     */
    public int initServer(int port, int timeout, boolean log) throws Exception {
        mTimeout = timeout;
        mLog = log;
keepAlive = true;
if (acceptT == null) {
acceptT = new AcceptThread();
            mPort = acceptT.init(port);
acceptT.start();
}
return mPort;
//Synthetic comment -- @@ -253,8 +223,8 @@
* @param port the port to use, or 0 to let the OS choose.
* Hard-coding ports is evil, so always pass 0!
*/
        public int init(int port) throws IOException {
            ss = new ServerSocket(port);
ss.setSoTimeout(5000);
ss.setReuseAddress(true);
return ss.getLocalPort();







