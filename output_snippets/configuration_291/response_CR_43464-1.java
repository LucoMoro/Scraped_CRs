//<Beginning of snippet n. 0>

package libcore.java.net;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public void test_read() throws Exception {
    try (final ServerSocket ss = new ServerSocket(0)) {
        final CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            try {
                ss.accept();
            } catch (Exception ex) {
                logError("Exception in accept: " + ex.getMessage());
            } finally {
                latch.countDown();
            }
        }).start();
        try (Socket s = new SSLSocketFactory().createSocket()) {
            s.connect(ss.getLocalSocketAddress());
            new Killer(s).start();
            latch.await();
        }
    }
}

public void test_read_multiple() throws Throwable {
    try (final ServerSocket ss = new ServerSocket(0)) {
        final CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            try {
                ss.accept();
            } catch (Exception ex) {
                logError("Exception in accept: " + ex.getMessage());
            } finally {
                latch.countDown();
            }
        }).start();
        try (final Socket s = new SSLSocketFactory().createSocket()) {
            s.connect(ss.getLocalSocketAddress());
            latch.await();
        }
    }
}

public void test_recv() throws Exception {
}

public void test_write() throws Exception {
    try (final ServerSocket ss = new ServerSocket(0)) {
        final CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            try {
                Socket client = ss.accept();
                Thread.sleep(30 * 1000);
            } catch (Exception ex) {
                logError("Exception in accept: " + ex.getMessage());
            } finally {
                latch.countDown();
            }
        }).start();
        try (Socket s = new SSLSocketFactory().createSocket()) {
            s.connect(ss.getLocalSocketAddress());
            new Killer(s).start();
            latch.await();
        }
    }
}

static class Killer<T> extends Thread {
    private final T s;

//<End of snippet n. 0>