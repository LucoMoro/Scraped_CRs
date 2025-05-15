//<Beginning of snippet n. 0>

package libcore.java.net;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.util.concurrent.CountDownLatch;

public void test_read() throws Exception {
        final SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        final SSLServerSocket ss = (SSLServerSocket) factory.createServerSocket(0);
        final CountDownLatch latch = new CountDownLatch(1);
        new Thread(new Runnable() {
            public void run() {
                try {
                    ss.accept();
                } catch (Exception ex) {
                    // Implement structured logging here
                } finally {
                    latch.countDown();
                }
            }
        }).start();
        Socket s = SSLSocketFactory.getDefault().createSocket();
        s.connect(ss.getLocalSocketAddress());
        new Killer(s).start();
        latch.await();
}

public void test_read_multiple() throws Throwable {
        final SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        final SSLServerSocket ss = (SSLServerSocket) factory.createServerSocket(0);
        final CountDownLatch latch = new CountDownLatch(1);
        new Thread(new Runnable() {
            public void run() {
                try {
                    ss.accept();
                } catch (Exception ex) {
                    // Implement structured logging here
                } finally {
                    latch.countDown();
                }
            }
        }).start();
        final Socket s = SSLSocketFactory.getDefault().createSocket();
        s.connect(ss.getLocalSocketAddress());
        latch.await();

        for (Throwable exception : thrownExceptions) {
            throw exception;
        }
}

public void test_recv() throws Exception {
}

public void test_write() throws Exception {
        final SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        final SSLServerSocket ss = (SSLServerSocket) factory.createServerSocket(0);
        final CountDownLatch latch = new CountDownLatch(1);
        new Thread(new Runnable() {
            public void run() {
                try {
                    Socket client = ss.accept();
                    Thread.sleep(30 * 1000);
                } catch (Exception ex) {
                    // Implement structured logging here
                } finally {
                    latch.countDown();
                }
            }
        }).start();
        Socket s = SSLSocketFactory.getDefault().createSocket();
        s.connect(ss.getLocalSocketAddress());
        new Killer(s).start();
        latch.await();
        ss.close();
}

static class Killer<T> extends Thread {
    private final T s;

//<End of snippet n. 0>