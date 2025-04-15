/*Work around ConcurrentCloseTest flakiness on >= 3.1 kernels.

It looks like something's changed that means we can't reliably
consume all the listen(2) backlog any more. For the tests we've
seen fail because they sometimes connect rather than time out,
switch to an unroutable address.

Bug: 6971145
Change-Id:I259d31b1a15123bcd78c36849d5ed863d392ac20*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/net/ConcurrentCloseTest.java b/luni/src/test/java/libcore/java/net/ConcurrentCloseTest.java
//Synthetic comment -- index 6988f6f..22723bf 100644

//Synthetic comment -- @@ -50,7 +50,7 @@
}

public void test_connect() throws Exception {
        StuckServer ss = new StuckServer(false);
Socket s = new Socket();
new Killer(s).start();
try {
//Synthetic comment -- @@ -65,7 +65,7 @@
}

public void test_connect_timeout() throws Exception {
        StuckServer ss = new StuckServer(false);
Socket s = new Socket();
new Killer(s).start();
try {
//Synthetic comment -- @@ -80,7 +80,7 @@
}

public void test_connect_nonBlocking() throws Exception {
        StuckServer ss = new StuckServer(false);
SocketChannel s = SocketChannel.open();
new Killer(s.socket()).start();
try {








//Synthetic comment -- diff --git a/support/src/test/java/tests/net/StuckServer.java b/support/src/test/java/tests/net/StuckServer.java
//Synthetic comment -- index eababce..dd7f56a 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package tests.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
//Synthetic comment -- @@ -26,17 +27,43 @@
* A test ServerSocket that you can't connect to --- connects will time out.
*/
public final class StuckServer {
    private static final boolean DEBUG = false;

private ServerSocket serverSocket;
    private InetSocketAddress address;
private ArrayList<Socket> clients = new ArrayList<Socket>();

public StuckServer() throws IOException {
        this(true);
    }

    public StuckServer(boolean useBacklog) throws IOException {
// Set a backlog and use it up so that we can expect the
        // connection to time out. According to Stevens
// 4.5 "listen function", Linux adds 3 to the specified
// backlog, so we need to connect 4 times before it will hang.
        if (useBacklog) {
            this.serverSocket = new ServerSocket(0, 1);
            this.address = (InetSocketAddress) serverSocket.getLocalSocketAddress();
            if (DEBUG) {
                System.err.println("StuckServer: " + serverSocket);
            }
            for (int i = 0; i < 4; ++i) {
                Socket client = new Socket(serverSocket.getInetAddress(), serverSocket.getLocalPort());
                clients.add(client);
                if (DEBUG) {
                    System.err.println("StuckServer client " + i + " - " + client);
                }
            }
        } else {
            // Note that we've had some trouble with backlog on >= 3.1 kernels: http://b/6971145.
            // The root cause is unknown, but this works around the ConcurrentCloseTest flakiness.
            // RFC 5737 implies this network will be unreachable. (There are two other networks
            // to try if we have trouble with this one.)
            // We've had trouble with 10.* in the past (because test labs running CTS often use
            // net 10!) but hopefully this network will be better.
            InetAddress testNet1 = InetAddress.getByAddress(new byte[] { (byte) 192, 0, 2, 0});
            this.address = new InetSocketAddress(testNet1, 80);
}
}

//Synthetic comment -- @@ -59,15 +86,17 @@
}

public InetSocketAddress getLocalSocketAddress() {
        return address;
}

public int getLocalPort() {
        return address.getPort();
}

public void close() throws IOException {
        if (serverSocket != null) {
            serverSocket.close();
        }
for (Socket client : clients) {
client.close();
}







