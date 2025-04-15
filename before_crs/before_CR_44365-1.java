/*Extra log output for ConcurrentCloseTest flakiness.

No clue what's wrong here, but we're seeing some flakiness in the
"connect" tests. Hopefully there will be a clue in this extra
output next time we see a failure.

Bug: 6971145
Change-Id:Ifed716cba5a17cab84e9535ec054785ee7d09aa7*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/net/ConcurrentCloseTest.java b/luni/src/test/java/libcore/java/net/ConcurrentCloseTest.java
//Synthetic comment -- index 06a546a..6988f6f 100644

//Synthetic comment -- @@ -38,12 +38,12 @@
*/
public class ConcurrentCloseTest extends junit.framework.TestCase {
public void test_accept() throws Exception {
        ServerSocket s = new ServerSocket(0);
        new Killer(s).start();
try {
System.err.println("accept...");
            s.accept();
            fail("accept returned!");
} catch (SocketException expected) {
assertEquals("Socket closed", expected.getMessage());
}
//Synthetic comment -- @@ -54,9 +54,9 @@
Socket s = new Socket();
new Killer(s).start();
try {
            System.err.println("connect...");
s.connect(ss.getLocalSocketAddress());
            fail("connect returned!");
} catch (SocketException expected) {
assertEquals("Socket closed", expected.getMessage());
} finally {
//Synthetic comment -- @@ -69,9 +69,9 @@
Socket s = new Socket();
new Killer(s).start();
try {
            System.err.println("connect (with timeout)...");
s.connect(ss.getLocalSocketAddress(), 3600 * 1000);
            fail("connect returned!");
} catch (SocketException expected) {
assertEquals("Socket closed", expected.getMessage());
} finally {
//Synthetic comment -- @@ -84,13 +84,13 @@
SocketChannel s = SocketChannel.open();
new Killer(s.socket()).start();
try {
            System.err.println("connect (non-blocking)...");
s.configureBlocking(false);
s.connect(ss.getLocalSocketAddress());
while (!s.finishConnect()) {
// Spin like a mad thing!
}
            fail("connect returned!");
} catch (SocketException expected) {
assertEquals("Socket closed", expected.getMessage());
} catch (AsynchronousCloseException alsoOkay) {
//Synthetic comment -- @@ -111,7 +111,7 @@
try {
System.err.println("read...");
int i = s.getInputStream().read();
            fail("read returned " + i);
} catch (SocketException expected) {
assertEquals("Socket closed", expected.getMessage());
}
//Synthetic comment -- @@ -136,7 +136,7 @@
try {
System.err.println("read...");
int i = s.getInputStream().read();
                            fail("read returned " + i);
} catch (SocketException expected) {
assertEquals("Socket closed", expected.getMessage());
}
//Synthetic comment -- @@ -238,7 +238,7 @@
try {
System.err.println("sleep...");
Thread.sleep(2000);
                System.err.println("close...");
s.getClass().getMethod("close").invoke(s);
} catch (Exception ex) {
ex.printStackTrace();







