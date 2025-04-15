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

public class OldDatagramPacketTest extends junit.framework.TestCase {

    public void test_getPort() throws Exception {
        DatagramPacket dp = new DatagramPacket("Hello".getBytes(), 5, InetAddress.getLocalHost(), 1000);
assertEquals("Incorrect port returned", 1000, dp.getPort());

        final DatagramSocket ss = new DatagramSocket();
Thread thread = new Thread(new Runnable() {
public void run() {
try {
                    DatagramPacket packet = new DatagramPacket(new byte[256], 256);
                    ss.setSoTimeout(3000);
                    ss.receive(packet);
                    ss.send(packet);
} catch (IOException e) {
System.out.println("thread exception: " + e);
}
}
});
thread.start();

        DatagramSocket cs = new DatagramSocket();
try {
            byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6 };
            DatagramPacket packet = new DatagramPacket(bytes, 6, InetAddress.getByName("localhost"), ss.getLocalPort());
            cs.send(packet);
            cs.setSoTimeout(3000);
            cs.receive(packet);
            cs.close();
            assertEquals(packet.getPort(), ss.getLocalPort());
} finally {
            cs.close();
            ss.close();
}
}

//Synthetic comment -- @@ -104,7 +73,7 @@
}

public void test_setData$BII() {
        DatagramPacket dp = new DatagramPacket("Hello".getBytes(), 5);
try {
dp.setData(null, 2, 3);
fail("NullPointerException was not thrown.");
//Synthetic comment -- @@ -113,7 +82,7 @@
}

public void test_setData$B() {
        DatagramPacket dp = new DatagramPacket("Hello".getBytes(), 5);
try {
dp.setData(null);
fail("NullPointerException was not thrown.");








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/net/OldServerSocketTest.java b/luni/src/test/java/libcore/java/net/OldServerSocketTest.java
//Synthetic comment -- index cf35489..6518897 100644

//Synthetic comment -- @@ -33,7 +33,6 @@
import java.nio.channels.ServerSocketChannel;
import java.security.Permission;
import java.util.Properties;

public class OldServerSocketTest extends OldSocketTestCase {

//Synthetic comment -- @@ -86,10 +85,9 @@
}

public void test_ConstructorII() throws IOException {
        s = new ServerSocket(0, 1);
s.setSoTimeout(2000);
        startClient(s.getLocalPort());
sconn = s.accept();
sconn.close();
s.close();
//Synthetic comment -- @@ -133,10 +131,9 @@
}

public void test_ConstructorI() throws Exception {
        s = new ServerSocket(0);
try {
            new ServerSocket(s.getLocalPort());
fail("IOException was not thrown.");
} catch(IOException ioe) {
//expected
//Synthetic comment -- @@ -162,11 +159,9 @@
}

public void test_ConstructorIILjava_net_InetAddress() throws IOException {
        ServerSocket ss = new ServerSocket(0, 10, InetAddress.getLocalHost());
try {
            new ServerSocket(ss.getLocalPort(), 10, InetAddress.getLocalHost());
fail("IOException was not thrown.");
} catch(IOException expected) {
}
//Synthetic comment -- @@ -217,9 +212,7 @@
}

public void test_accept() throws IOException {
        ServerSocket newSocket = new ServerSocket(0);
newSocket.setSoTimeout(500);
try {
Socket accepted = newSocket.accept();








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/net/OldURLClassLoaderTest.java b/luni/src/test/java/libcore/java/net/OldURLClassLoaderTest.java
//Synthetic comment -- index 2646f98..3a5608c 100644

//Synthetic comment -- @@ -35,7 +35,6 @@
import java.util.jar.Manifest;
import org.apache.harmony.security.tests.support.TestCertUtils;
import tests.support.Support_Configuration;
import tests.support.Support_TestWebData;
import tests.support.Support_TestWebServer;
import tests.support.resource.Support_Resources;
//Synthetic comment -- @@ -210,13 +209,12 @@

@SideEffect("Support_TestWebServer requires isolation.")
public void test_findResourceLjava_lang_String() throws Exception {
File tmp = File.createTempFile("test", ".txt");

Support_TestWebServer server = new Support_TestWebServer();
try {

            int port = server.initServer(tmp.getAbsolutePath(), "text/html");

URL[] urls = { new URL("http://localhost:" + port + "/") };
ucl = new URLClassLoader(urls);
//Synthetic comment -- @@ -244,9 +242,8 @@
tempFile2.deleteOnExit();

Support_TestWebServer server = new Support_TestWebServer();
try {
            int port = server.initServer();

String tempPath1 = tempFile1.getParentFile().getAbsolutePath() + "/";
InputStream is = getClass().getResourceAsStream(








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/nio/channels/OldServerSocketChannelTest.java b/luni/src/test/java/libcore/java/nio/channels/OldServerSocketChannelTest.java
//Synthetic comment -- index fb63512..51f288a 100644

//Synthetic comment -- @@ -25,26 +25,17 @@
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import junit.framework.TestCase;

public class OldServerSocketChannelTest extends TestCase {

private static final int TIME_UNIT = 200;

private ServerSocketChannel serverChannel;

private SocketChannel clientChannel;

protected void setUp() throws Exception {
super.setUp();
this.serverChannel = ServerSocketChannel.open();
this.clientChannel = SocketChannel.open();
}
//Synthetic comment -- @@ -87,7 +78,7 @@
public void test_accept_Block_NoConnect_interrupt() throws IOException {
assertTrue(this.serverChannel.isBlocking());
ServerSocket gotSocket = this.serverChannel.socket();
        gotSocket.bind(null);

class MyThread extends Thread {
public String errMsg = null;








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/nio/channels/OldSocketChannelTest.java b/luni/src/test/java/libcore/java/nio/channels/OldSocketChannelTest.java
//Synthetic comment -- index f182375..750be35 100644

//Synthetic comment -- @@ -35,11 +35,7 @@
import java.nio.channels.UnsupportedAddressTypeException;
import java.nio.channels.spi.SelectorProvider;
import junit.framework.TestCase;

public class OldSocketChannelTest extends TestCase {

private static final int CAPACITY_NORMAL = 200;
//Synthetic comment -- @@ -58,11 +54,10 @@

protected void setUp() throws Exception {
super.setUp();
this.channel1 = SocketChannel.open();
this.channel2 = SocketChannel.open();
        this.server1 = new ServerSocket(0);
        this.localAddr1 = (InetSocketAddress) server1.getLocalSocketAddress();
}

protected void tearDown() throws Exception {
//Synthetic comment -- @@ -292,12 +287,9 @@
server.start();
Thread.currentThread().sleep(1000);

// First test with array based byte buffer
SocketChannel sc = SocketChannel.open();
        sc.connect(server.getLocalSocketAddress());

ByteBuffer buf = ByteBuffer.allocate(data.length);
buf.limit(data.length / 2);
//Synthetic comment -- @@ -313,7 +305,7 @@

// Now test with direct byte buffer
sc = SocketChannel.open();
        sc.connect(server.getLocalSocketAddress());

buf = ByteBuffer.allocateDirect(data.length);
buf.limit(data.length / 2);
//Synthetic comment -- @@ -339,17 +331,15 @@
}

public static boolean done = false;
public static byte[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

static class ServerThread extends Thread {
        private ServerSocketChannel ssc;

        @Override public void run() {
try {
                ssc = ServerSocketChannel.open();
                ssc.socket().bind(null, 0);

ByteBuffer buf = ByteBuffer.allocate(10);
buf.put(data);
//Synthetic comment -- @@ -363,6 +353,10 @@
// ignore
}
}

        public SocketAddress getLocalSocketAddress() {
            return ssc.socket().getLocalSocketAddress();
        }
}

class MockSocketChannel extends SocketChannel {








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/util/OldScannerTest.java b/luni/src/test/java/libcore/java/util/OldScannerTest.java
//Synthetic comment -- index b51cfbc..9bac1f4 100644

//Synthetic comment -- @@ -28,7 +28,6 @@
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import junit.framework.TestCase;

public final class OldScannerTest extends TestCase {

//Synthetic comment -- @@ -492,15 +491,12 @@
assertEquals(102400, matchResult.end());
}

    public void test_Constructor_LReadableByteChannel() throws IOException {
ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(null);

SocketChannel sc = SocketChannel.open();
        sc.connect(ssc.socket().getLocalSocketAddress());
sc.configureBlocking(false);
assertFalse(sc.isBlocking());









//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/luni/tests/java/net/URLConnectionTest.java b/luni/src/test/java/org/apache/harmony/luni/tests/java/net/URLConnectionTest.java
//Synthetic comment -- index 9f414c3..ab675e2 100644

//Synthetic comment -- @@ -19,7 +19,6 @@
import dalvik.annotation.BrokenTest;
import junit.framework.TestCase;
import tests.support.Support_Configuration;
import tests.support.Support_TestWebData;
import tests.support.Support_TestWebServer;
import tests.support.resource.Support_Resources;
//Synthetic comment -- @@ -222,9 +221,8 @@

//        ftpURL = new URL(Support_Configuration.testFTPURL);

server = new Support_TestWebServer();
        port = server.initServer();
url = new URL("http://localhost:" + port + "/test1");
uc = url.openConnection();
url2 =  new URL("http://localhost:" + port + "/test2");








//Synthetic comment -- diff --git a/luni/src/test/java/tests/api/javax/net/ssl/SSLSocketFactoryTest.java b/luni/src/test/java/tests/api/javax/net/ssl/SSLSocketFactoryTest.java
//Synthetic comment -- index 02abcc2..0d91116 100644

//Synthetic comment -- @@ -25,16 +25,13 @@

import junit.framework.TestCase;

public class SSLSocketFactoryTest extends TestCase {

private ServerSocket ss;

protected int startServer(String name) {
try {
            ss = new ServerSocket(0);
} catch (IOException e) {
fail(name + ": " + e);
}








//Synthetic comment -- diff --git a/support/src/test/java/tests/support/Support_TestWebServer.java b/support/src/test/java/tests/support/Support_TestWebServer.java
//Synthetic comment -- index 4d6b0d1..5f3cd85 100644

//Synthetic comment -- @@ -94,51 +94,21 @@
}

/**
* @param servePath the path to the dynamic web test data
* @param contentType the type of the dynamic web test data
*/
    public int initServer(String servePath, String contentType) throws Exception {
Support_TestWebData.initDynamicTestWebData(servePath, contentType);
        return initServer();
}

    public int initServer() throws Exception {
        mTimeout = DEFAULT_TIMEOUT;
        mLog = false;
keepAlive = true;
if (acceptT == null) {
acceptT = new AcceptThread();
            mPort = acceptT.init();
acceptT.start();
}
return mPort;
//Synthetic comment -- @@ -253,8 +223,8 @@
* @param port the port to use, or 0 to let the OS choose.
* Hard-coding ports is evil, so always pass 0!
*/
        public int init() throws IOException {
            ss = new ServerSocket(0);
ss.setSoTimeout(5000);
ss.setReuseAddress(true);
return ss.getLocalPort();







