
//<Beginning of snippet n. 0>



package libcore.java.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedChannelException;
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

for (Throwable exception : thrownExceptions) {
throw exception;
}
}

public void test_recv() throws Exception {
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
ss.close();
}

static class Killer<T> extends Thread {
private final T s;


//<End of snippet n. 0>








