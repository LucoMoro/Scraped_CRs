
//<Beginning of snippet n. 0>



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
}

public void test_read() throws Exception {
        SilentServer ss = new SilentServer();
Socket s = new Socket();
s.connect(ss.getLocalSocketAddress());
new Killer(s).start();
}

public void test_read_multiple() throws Throwable {
        SilentServer ss = new SilentServer();
final Socket s = new Socket();
s.connect(ss.getLocalSocketAddress());

for (Throwable exception : thrownExceptions) {
throw exception;
}

        ss.close();
}

public void test_recv() throws Exception {
}

public void test_write() throws Exception {
        final SilentServer ss = new SilentServer();
Socket s = new Socket();
s.connect(ss.getLocalSocketAddress());
new Killer(s).start();
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


//<End of snippet n. 0>








