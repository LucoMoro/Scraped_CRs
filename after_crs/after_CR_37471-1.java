/*Fix an ICS DatagramPacket bug.

(cherry-picked from e50d82455c813210a2d452070f45fd38d9903159.)

Bug:http://code.google.com/p/android/issues/detail?id=24748Change-Id:Id7772c3f27961c99d3e5e3856e79edb84483dd46*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/net/DatagramPacket.java b/luni/src/main/java/java/net/DatagramPacket.java
//Synthetic comment -- index 663585c..3b1bc36 100644

//Synthetic comment -- @@ -26,26 +26,26 @@
*/
public final class DatagramPacket {

    private byte[] data;

/**
* Length of the data to be sent or size of data that was received via
* DatagramSocket#receive() method call.
*/
    private int length;

/**
     * The last user-supplied length (as opposed to a length set by simply receiving a packet).
     * This length (unlike 'length') is sticky, and survives until the user sets another length.
     * It's used to limit the amount of data that will be taken from future packets.
*/
    private int userSuppliedLength;

    private InetAddress address;

    private int port = -1; // The default port number is -1

    private int offset = 0;

/**
* Constructs a new {@code DatagramPacket} object to receive data up to
//Synthetic comment -- @@ -92,8 +92,7 @@
* @param aPort
*            the port of the target host.
*/
    public DatagramPacket(byte[] data, int offset, int length, InetAddress host, int aPort) {
this(data, offset, length);
setPort(aPort);
address = host;
//Synthetic comment -- @@ -185,7 +184,7 @@
this.data = data;
this.offset = offset;
this.length = byteCount;
        this.userSuppliedLength = byteCount;
}

/**
//Synthetic comment -- @@ -197,21 +196,12 @@
*/
public synchronized void setData(byte[] buf) {
length = buf.length; // This will check for null
        userSuppliedLength = length;
data = buf;
offset = 0;
}

/**
* Sets the length of the datagram packet. This length plus the offset must
* be lesser than or equal to the buffer size.
*
//Synthetic comment -- @@ -219,21 +209,27 @@
*            the length of this datagram packet.
*/
public synchronized void setLength(int length) {
        if (length < 0 || offset + length > data.length) {
            throw new IndexOutOfBoundsException("length=" + length + ", offset=" + offset +
                                                ", buffer size=" + data.length);
        }
        this.length = length;
        this.userSuppliedLength = length;
}

/**
     * Resets 'length' to the last user-supplied length, ready to receive another packet.
     * @hide for PlainDatagramSocketImpl
*/
    public void resetLengthForReceive() {
        this.length = userSuppliedLength;
    }

    /**
     * Sets 'length' without changing 'userSuppliedLength', after receiving a packet.
     * @hide for IoBridge
     */
    public void setReceivedLength(int length) {
this.length = length;
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/net/DatagramSocket.java b/luni/src/main/java/java/net/DatagramSocket.java
//Synthetic comment -- index 487a18a..2b468fa 100644

//Synthetic comment -- @@ -249,11 +249,8 @@
if (pendingConnectException != null) {
throw new SocketException("Pending connect failure", pendingConnectException);
}
        pack.resetLengthForReceive();
impl.receive(pack);
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/io/IoBridge.java b/luni/src/main/java/libcore/io/IoBridge.java
//Synthetic comment -- index db8d8ad..f95e8ec 100644

//Synthetic comment -- @@ -525,7 +525,7 @@
return -1;
}
if (packet != null) {
            packet.setReceivedLength(byteCount);
if (!isConnected) {
packet.setAddress(srcAddress.getAddress());
packet.setPort(srcAddress.getPort());








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/net/OldDatagramSocketTest.java b/luni/src/test/java/libcore/java/net/OldDatagramSocketTest.java
//Synthetic comment -- index 666a503..d51d461 100644

//Synthetic comment -- @@ -804,9 +804,11 @@
InetAddress localHost = InetAddress.getLocalHost();
Thread.sleep(1000);
DatagramSocket sds = new DatagramSocket(ports[1]);
                    sds.send(new DatagramPacket("Test".getBytes("UTF-8"), "Test".length(), localHost, portNumber));
                    sds.send(new DatagramPacket("Longer test".getBytes("UTF-8"), "Longer test".length(), localHost, portNumber));
                    sds.send(new DatagramPacket("3 Test".getBytes("UTF-8"), "3 Test".length(), localHost, portNumber));
                    sds.send(new DatagramPacket("4 Test".getBytes("UTF-8"), "4 Test".length(), localHost, portNumber));
                    sds.send(new DatagramPacket("5".getBytes("UTF-8"), "5".length(), localHost, portNumber));
sds.close();
} catch (Exception e) {
throw new RuntimeException(e);
//Synthetic comment -- @@ -815,15 +817,30 @@
}

try {
            new Thread(new TestDGRcv(), "datagram receiver").start();
ds = new java.net.DatagramSocket(portNumber);
ds.setSoTimeout(6000);
            byte[] rbuf = new byte[1000];
DatagramPacket rdp = new DatagramPacket(rbuf, rbuf.length);

            // Receive the first packet.
ds.receive(rdp);
            assertEquals("Test", new String(rbuf, 0, rdp.getLength()));
            // Check that we can still receive a longer packet (http://code.google.com/p/android/issues/detail?id=24748).
            ds.receive(rdp);
            assertEquals("Longer test", new String(rbuf, 0, rdp.getLength()));
            // See what happens if we manually call DatagramPacket.setLength.
            rdp.setLength(4);
            ds.receive(rdp);
            assertEquals("3 Te", new String(rbuf, 0, rdp.getLength()));
            // And then another.
            ds.receive(rdp);
            assertEquals("4 Te", new String(rbuf, 0, rdp.getLength()));
            // And then a packet shorter than the user-supplied length.
            ds.receive(rdp);
            assertEquals("5", new String(rbuf, 0, rdp.getLength()));

ds.close();
} finally {
ds.close();
}







