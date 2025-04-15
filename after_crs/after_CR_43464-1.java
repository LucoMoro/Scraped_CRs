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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedChannelException;
//Synthetic comment -- @@ -102,16 +104,7 @@
}

public void test_read() throws Exception {
        SilentServer ss = new SilentServer();
Socket s = new Socket();
s.connect(ss.getLocalSocketAddress());
new Killer(s).start();
//Synthetic comment -- @@ -126,16 +119,7 @@
}

public void test_read_multiple() throws Throwable {
        SilentServer ss = new SilentServer();
final Socket s = new Socket();
s.connect(ss.getLocalSocketAddress());

//Synthetic comment -- @@ -173,6 +157,8 @@
for (Throwable exception : thrownExceptions) {
throw exception;
}

        ss.close();
}

public void test_recv() throws Exception {
//Synthetic comment -- @@ -190,21 +176,7 @@
}

public void test_write() throws Exception {
        final SilentServer ss = new SilentServer();
Socket s = new Socket();
s.connect(ss.getLocalSocketAddress());
new Killer(s).start();
//Synthetic comment -- @@ -224,6 +196,37 @@
ss.close();
}

    // This server accepts connections, but doesn't read or write anything.
    // It holds on to the Socket connecting to the client so it won't be GCed.
    // Call "close" to close both the server socket and its client connection.
    static class SilentServer {
        private final ServerSocket ss;
        private Socket client;

        public SilentServer() throws IOException {
            ss = new ServerSocket(0);
            new Thread(new Runnable() {
                public void run() {
                    try {
                        client = ss.accept();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
        }

        public SocketAddress getLocalSocketAddress() {
            return ss.getLocalSocketAddress();
        }

        public void close() throws IOException {
            client.close();
            ss.close();
        }
    }

    // This thread calls the "close" method on the supplied T after 2s.
static class Killer<T> extends Thread {
private final T s;








