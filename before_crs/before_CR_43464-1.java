/*Fix ConcurrentCloseTest flakiness.

We were running the risk that the server's end of a client socket would get
closed by the finalizer thread, resulting in read(2) returning 0 to the client,
and the test failing like this:

  junit.framework.AssertionFailedError: read returned -1
      at libcore.java.net.ConcurrentCloseTest.test_read(ConcurrentCloseTest.java:121)
      at java.lang.reflect.Method.invokeNative(Native Method)

Bug: 7206024
Change-Id:I3f228dc4d3bfb59f6595ebe255de2cf4047d06b8*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/net/ConcurrentCloseTest.java b/luni/src/test/java/libcore/java/net/ConcurrentCloseTest.java
//Synthetic comment -- index d33c5f3..06a546a 100644

//Synthetic comment -- @@ -16,11 +16,13 @@

package libcore.java.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedChannelException;
//Synthetic comment -- @@ -102,16 +104,7 @@
}

public void test_read() throws Exception {
        final ServerSocket ss = new ServerSocket(0);
        new Thread(new Runnable() {
            public void run() {
                try {
                    ss.accept();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
Socket s = new Socket();
s.connect(ss.getLocalSocketAddress());
new Killer(s).start();
//Synthetic comment -- @@ -126,16 +119,7 @@
}

public void test_read_multiple() throws Throwable {
        final ServerSocket ss = new ServerSocket(0);
        new Thread(new Runnable() {
            public void run() {
                try {
                    ss.accept();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
final Socket s = new Socket();
s.connect(ss.getLocalSocketAddress());

//Synthetic comment -- @@ -173,6 +157,8 @@
for (Throwable exception : thrownExceptions) {
throw exception;
}
}

public void test_recv() throws Exception {
//Synthetic comment -- @@ -190,21 +176,7 @@
}

public void test_write() throws Exception {
        final ServerSocket ss = new ServerSocket(0);
        new Thread(new Runnable() {
            public void run() {
                try {
                    System.err.println("accepting...");

                    Socket client = ss.accept();
                    System.err.println("accepted...");
                    Thread.sleep(30 * 1000);
                    System.err.println("server exiting...");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
Socket s = new Socket();
s.connect(ss.getLocalSocketAddress());
new Killer(s).start();
//Synthetic comment -- @@ -224,6 +196,37 @@
ss.close();
}

static class Killer<T> extends Thread {
private final T s;








