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
        StuckServer ss = new StuckServer();
Socket s = new Socket();
new Killer(s).start();
try {
//Synthetic comment -- @@ -65,7 +65,7 @@
}

public void test_connect_timeout() throws Exception {
        StuckServer ss = new StuckServer();
Socket s = new Socket();
new Killer(s).start();
try {
//Synthetic comment -- @@ -80,7 +80,7 @@
}

public void test_connect_nonBlocking() throws Exception {
        StuckServer ss = new StuckServer();
SocketChannel s = SocketChannel.open();
new Killer(s.socket()).start();
try {








//Synthetic comment -- diff --git a/support/src/test/java/tests/net/StuckServer.java b/support/src/test/java/tests/net/StuckServer.java
//Synthetic comment -- index eababce..dd7f56a 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package tests.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
//Synthetic comment -- @@ -26,17 +27,43 @@
* A test ServerSocket that you can't connect to --- connects will time out.
*/
public final class StuckServer {
private ServerSocket serverSocket;
private ArrayList<Socket> clients = new ArrayList<Socket>();

public StuckServer() throws IOException {
// Set a backlog and use it up so that we can expect the
        // connection to time out. According to Steven's
// 4.5 "listen function", Linux adds 3 to the specified
// backlog, so we need to connect 4 times before it will hang.
        serverSocket = new ServerSocket(0, 1);
        for (int i = 0; i < 4; i++) {
            clients.add(new Socket(serverSocket.getInetAddress(), serverSocket.getLocalPort()));
}
}

//Synthetic comment -- @@ -59,15 +86,17 @@
}

public InetSocketAddress getLocalSocketAddress() {
        return (InetSocketAddress) serverSocket.getLocalSocketAddress();
}

public int getLocalPort() {
        return serverSocket.getLocalPort();
}

public void close() throws IOException {
        serverSocket.close();
for (Socket client : clients) {
client.close();
}







