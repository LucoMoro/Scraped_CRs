/*Bugs when connecting a SocketChannel registered in a Selector

Seehttp://code.google.com/p/android/issues/detail?id=6309Bug fixed in org.apache.harmony.nio.internal.SelectorImpl by verifying
that there is a connection pending when adding a writable FD to the
internal list of readyKeys.

A unit test is also provided in java.nio.SelectorTest.

Change-Id:Icf47e81b3bcc299608d0672365daf797e44cdfdd*/




//Synthetic comment -- diff --git a/luni/src/test/java/java/nio/SelectorTest.java b/luni/src/test/java/java/nio/SelectorTest.java
new file mode 100644
//Synthetic comment -- index 0000000..b860620

//Synthetic comment -- @@ -0,0 +1,99 @@
package java.nio;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.NoConnectionPendingException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import junit.framework.Test;
import junit.framework.TestSuite;
import tests.support.Support_PortManager;

public class SelectorTest extends junit.framework.TestCase {
    private static final int WAIT_TIME = 100;
    private static final int PORT = Support_PortManager.getNextPort();
    private static final InetSocketAddress LOCAL_ADDRESS = new InetSocketAddress(
            "127.0.0.1", PORT);

    Selector selector;
    ServerSocketChannel ssc;

    protected void setUp() throws Exception {
        super.setUp();
        ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ServerSocket ss = ssc.socket();
        InetSocketAddress address = new InetSocketAddress(PORT);
        ss.bind(address);
        selector = Selector.open();
    }

    protected void tearDown() throws Exception {
        try {
            ssc.close();
        } catch (Exception e) {
            // do nothing
        }
        try {
            selector.close();
        } catch (Exception e) {
            // do nothing
        }
        super.tearDown();
    }

    // http://code.google.com/p/android/issues/detail?id=4237
    public void test_connectFinish_fails()
            throws Exception {

        final SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_CONNECT);
        final Boolean[] fail = new Boolean[1];
        new Thread() {
            public void run() {
                try {
                    while (selector.isOpen()) {
                        if (selector.select() != 0) {
                            for (SelectionKey key : selector.selectedKeys()) {
                                if (key.isValid() && key.isConnectable()) {
                                    try {
                                        channel.finishConnect();
                                        synchronized (fail) {
                                            fail[0] = Boolean.FALSE;
                                            fail.notify();
                                        }
                                    }
                                    catch (NoConnectionPendingException _) {
                                        synchronized (fail) {
                                            fail[0] = Boolean.TRUE;
                                            fail.notify();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                catch (Exception _) {}
            }
        }.start();
        Thread.sleep(WAIT_TIME);
        channel.connect(LOCAL_ADDRESS);
        long time = System.currentTimeMillis();
        synchronized (fail) {
            while (System.currentTimeMillis() - time < WAIT_TIME || fail[0] == null) {
                fail.wait(WAIT_TIME);
            }
        }
        if (fail[0] == null) {
            System.out.println("test does not work");
        }
        else if (fail[0].booleanValue()) {
            fail();
        }
    }
}









//Synthetic comment -- diff --git a/nio/src/main/java/org/apache/harmony/nio/internal/SelectorImpl.java b/nio/src/main/java/org/apache/harmony/nio/internal/SelectorImpl.java
//Synthetic comment -- index 9d96454..9a735b6 100644

//Synthetic comment -- @@ -281,6 +281,14 @@
}
}

    private boolean isConnectionPending(SelectionKeyImpl key) {
        java.nio.channels.SelectableChannel channel = key.channel();
        if (channel instanceof java.nio.channels.SocketChannel) {
            return ((java.nio.channels.SocketChannel)channel).isConnectionPending();
        }
        return true;
    }

/**
* Prepare the readableFDs, writableFDs, readyKeys and flags arrays in
* preparation for a call to {@code INetworkSystem#select()}. After they're
//Synthetic comment -- @@ -315,7 +323,7 @@
readyKeys[r] = key;
r++;
}
            if ((((isConnectionPending(key) ? OP_CONNECT : 0) | OP_WRITE) & interestOps) != 0) {
writableFDs[w] = ((FileDescriptorHandler) key.channel()).getFD();
readyKeys[w + numReadable] = key;
w++;







