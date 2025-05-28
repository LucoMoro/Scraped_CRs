//<Beginning of snippet n. 0>

package libcore.java.net;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLContext;
import java.util.logging.Logger;
import java.util.concurrent.CountDownLatch;

public class NetworkTest {
    private static final Logger logger = Logger.getLogger(NetworkTest.class.getName());
    private final CountDownLatch latch = new CountDownLatch(1);
    private volatile boolean serverRunning = true;

    public void test_read() throws Exception {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, null, null);
        try (final SSLServerSocket ss = (SSLServerSocket) sslContext.getServerSocketFactory().createServerSocket(0)) {
            new Thread(() -> {
                try {
                    ss.accept();
                } catch (Exception ex) {
                    logger.warning("Exception in accept: " + ex.getMessage());
                } finally {
                    serverRunning = false;
                    latch.countDown();
                }
            }).start();

            SSLSocket s = (SSLSocket) sslContext.getSocketFactory().createSocket(ss.getLocalSocketAddress(), 0);
            new Killer(s).start();

            latch.await();
        }
    }

    public void test_read_multiple() throws Throwable {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, null, null);
        try (final SSLServerSocket ss = (SSLServerSocket) sslContext.getServerSocketFactory().createServerSocket(0)) {
            new Thread(() -> {
                try {
                    ss.accept();
                } catch (Exception ex) {
                    logger.warning("Exception in accept: " + ex.getMessage());
                }
            }).start();

            SSLSocket s = (SSLSocket) sslContext.getSocketFactory().createSocket(ss.getLocalSocketAddress(), 0);
            
            for (Throwable exception : thrownExceptions) {
                throw exception; 
            }
        }
    }

    public void test_recv() throws Exception {
    }

    public void test_write() throws Exception {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, null, null);
        try (final SSLServerSocket ss = (SSLServerSocket) sslContext.getServerSocketFactory().createServerSocket(0)) {
            new Thread(() -> {
                try {
                    logger.info("accepting...");
                    SSLSocket client = (SSLSocket) ss.accept();
                    logger.info("accepted...");
                    Thread.sleep(30 * 1000);
                    logger.info("server exiting...");
                } catch (Exception ex) {
                    logger.warning("Exception in accept: " + ex.getMessage());
                }
            }).start();

            SSLSocket s = (SSLSocket) sslContext.getSocketFactory().createSocket(ss.getLocalSocketAddress(), 0);
            new Killer(s).start();

            latch.await();
        }
    }

    static class Killer<T> extends Thread {
        private final T s;

        public Killer(T s) {
            this.s = s;
        }

        @Override
        public void run() {
            try {
                if (s instanceof SSLSocket) {
                    ((SSLSocket) s).close();
                }
            } catch (Exception ex) {
                logger.warning("Exception during Killer execution: " + ex.getMessage());
            }
        }
    }
}

//<End of snippet n. 0>