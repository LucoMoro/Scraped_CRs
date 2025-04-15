/*Fix an ICS DatagramPacket bug.

(cherry-picked from e50d82455c813210a2d452070f45fd38d9903159.)

Bug:http://code.google.com/p/android/issues/detail?id=24748Change-Id:Id7772c3f27961c99d3e5e3856e79edb84483dd46*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/net/DatagramPacket.java b/luni/src/main/java/java/net/DatagramPacket.java
//Synthetic comment -- index 663585c..3b1bc36 100644

//Synthetic comment -- @@ -26,26 +26,26 @@
*/
public final class DatagramPacket {

    byte[] data;

/**
* Length of the data to be sent or size of data that was received via
* DatagramSocket#receive() method call.
*/
    int length;

/**
     * Size of internal buffer that is used to store received data. Should be
     * greater or equal to "length" field.
*/
    int capacity;

    InetAddress address;

    int port = -1; // The default port number is -1

    int offset = 0;


/**
* Constructs a new {@code DatagramPacket} object to receive data up to
//Synthetic comment -- @@ -92,8 +92,7 @@
* @param aPort
*            the port of the target host.
*/
    public DatagramPacket(byte[] data, int offset, int length,
            InetAddress host, int aPort) {
this(data, offset, length);
setPort(aPort);
address = host;
//Synthetic comment -- @@ -185,7 +184,7 @@
this.data = data;
this.offset = offset;
this.length = byteCount;
        this.capacity = byteCount;
}

/**
//Synthetic comment -- @@ -197,21 +196,12 @@
*/
public synchronized void setData(byte[] buf) {
length = buf.length; // This will check for null
        capacity = buf.length;
data = buf;
offset = 0;
}

/**
     * Gets the current capacity value.
     *
     * @return the current capacity value
     */
    synchronized int getCapacity() {
        return capacity;
    }

    /**
* Sets the length of the datagram packet. This length plus the offset must
* be lesser than or equal to the buffer size.
*
//Synthetic comment -- @@ -219,21 +209,27 @@
*            the length of this datagram packet.
*/
public synchronized void setLength(int length) {
        setLengthOnly(length);
        this.capacity = length;
}

/**
     * An alternative to {@link #setLength(int)}, that doesn't reset the {@link #capacity}
     * field.
     *
     * @param len the length of this datagram packet
*/
    synchronized void setLengthOnly(int length) {
        if (length < 0 || offset + length > data.length) {
            throw new IndexOutOfBoundsException("length=" + length + ", offset=" + offset +
                    ", buffer size=" + data.length);
        }
this.length = length;
}









//Synthetic comment -- diff --git a/luni/src/main/java/java/net/DatagramSocket.java b/luni/src/main/java/java/net/DatagramSocket.java
//Synthetic comment -- index 487a18a..2b468fa 100644

//Synthetic comment -- @@ -249,11 +249,8 @@
if (pendingConnectException != null) {
throw new SocketException("Pending connect failure", pendingConnectException);
}

        pack.setLength(pack.getCapacity());
impl.receive(pack);
        // pack's length field is now updated by native code call;
        // pack's capacity field is unchanged
}

/**








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/io/IoBridge.java b/luni/src/main/java/libcore/io/IoBridge.java
//Synthetic comment -- index db8d8ad..f95e8ec 100644

//Synthetic comment -- @@ -525,7 +525,7 @@
return -1;
}
if (packet != null) {
            packet.setLength(byteCount);
if (!isConnected) {
packet.setAddress(srcAddress.getAddress());
packet.setPort(srcAddress.getPort());








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/net/OldDatagramSocketTest.java b/luni/src/test/java/libcore/java/net/OldDatagramSocketTest.java
//Synthetic comment -- index 666a503..d51d461 100644

//Synthetic comment -- @@ -804,9 +804,11 @@
InetAddress localHost = InetAddress.getLocalHost();
Thread.sleep(1000);
DatagramSocket sds = new DatagramSocket(ports[1]);
                    DatagramPacket rdp = new DatagramPacket("Test String"
                            .getBytes(), 11, localHost, portNumber);
                    sds.send(rdp);
sds.close();
} catch (Exception e) {
throw new RuntimeException(e);
//Synthetic comment -- @@ -815,15 +817,30 @@
}

try {
            new Thread(new TestDGRcv(), "DGSender").start();
ds = new java.net.DatagramSocket(portNumber);
ds.setSoTimeout(6000);
            byte rbuf[] = new byte[1000];
DatagramPacket rdp = new DatagramPacket(rbuf, rbuf.length);
ds.receive(rdp);
ds.close();
            assertEquals("Send/Receive failed to return correct data: "
                    + new String(rbuf, 0, 11), "Test String", new String(rbuf, 0, 11));
} finally {
ds.close();
}







