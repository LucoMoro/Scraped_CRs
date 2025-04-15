/*Increase javax.obex performance

When a file is transferred over OBEX the javax.obex classes allocate
the same amount of memory multiple times simply to copy the same data
around internally, only to throw those allocations away when the next
data block is processed. In the Bluetooth OPP case that meant that
64Kb was allocated around 15 times for every 64Kb transferred. Since
the transfer speed is around 1.3Mbps that means almost 2.5Mb of
memory is garbage collected every second, increasing the time needed
for each block transfer.
Fixed by keeping data in reusable byte buffer objects instead.
The time to transfer a 39MB file is reduced by about 10-20%.

Change-Id:I182c0374c2915d1b37ca12200fb36de57dabc67e*/
//Synthetic comment -- diff --git a/obex/javax/obex/ClientOperation.java b/obex/javax/obex/ClientOperation.java
//Synthetic comment -- index 05b498c..9e0700f 100644

//Synthetic comment -- @@ -37,7 +37,6 @@
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;

/**
* This class implements the <code>Operation</code> interface. It will read and
//Synthetic comment -- @@ -72,6 +71,8 @@

private boolean mEndOfBodySent;

/**
* Creates new OperationImpl to read and write data to a server
* @param maxSize the maximum packet size
//Synthetic comment -- @@ -100,6 +101,8 @@

mRequestHeader = new HeaderSet();

int[] headerList = header.getHeaderList();

if (headerList != null) {
//Synthetic comment -- @@ -396,7 +399,7 @@
*/
private boolean sendRequest(int opCode) throws IOException {
boolean returnValue = false;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
int bodyLength = -1;
byte[] headerArray = ObexHelper.createHeader(mRequestHeader, true);
if (mPrivateOutput != null) {
//Synthetic comment -- @@ -437,9 +440,9 @@
throw new IOException("OBEX Packet exceeds max packet size");
}

                byte[] sendHeader = new byte[end - start];
                System.arraycopy(headerArray, start, sendHeader, 0, sendHeader.length);
                if (!mParent.sendRequest(opCode, sendHeader, mReplyHeader, mPrivateInput)) {
return false;
}

//Synthetic comment -- @@ -456,7 +459,8 @@
return false;
}
} else {
            out.write(headerArray);
}

if (bodyLength > 0) {
//Synthetic comment -- @@ -471,8 +475,6 @@
bodyLength = mMaxPacketSize - headerArray.length - 6;
}

            byte[] body = mPrivateOutput.readBytes(bodyLength);

/*
* Since this is a put request if the final bit is set or
* the output stream is closed we need to send the 0x49
//Synthetic comment -- @@ -480,44 +482,40 @@
*/
if ((mPrivateOutput.isClosed()) && (!returnValue) && (!mEndOfBodySent)
&& ((opCode & 0x80) != 0)) {
                out.write(0x49);
mEndOfBodySent = true;
} else {
                out.write(0x48);
}

bodyLength += 3;
            out.write((byte)(bodyLength >> 8));
            out.write((byte)bodyLength);

            if (body != null) {
                out.write(body);
            }
}

if (mPrivateOutputOpen && bodyLength <= 0 && !mEndOfBodySent) {
// only 0x82 or 0x83 can send 0x49
if ((opCode & 0x80) == 0) {
                out.write(0x48);
} else {
                out.write(0x49);
mEndOfBodySent = true;

}

bodyLength = 3;
            out.write((byte)(bodyLength >> 8));
            out.write((byte)bodyLength);
}

        if (out.size() == 0) {
if (!mParent.sendRequest(opCode, null, mReplyHeader, mPrivateInput)) {
return false;
}
return returnValue;
}
        if ((out.size() > 0)
                && (!mParent.sendRequest(opCode, out.toByteArray(), mReplyHeader, mPrivateInput))) {
return false;
}









//Synthetic comment -- diff --git a/obex/javax/obex/ClientSession.java b/obex/javax/obex/ClientSession.java
//Synthetic comment -- index 0935383..597c17c 100644

//Synthetic comment -- @@ -32,7 +32,6 @@

package javax.obex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
//Synthetic comment -- @@ -62,11 +61,23 @@

private final OutputStream mOutput;

public ClientSession(final ObexTransport trans) throws IOException {
mInput = trans.openInputStream();
mOutput = trans.openOutputStream();
mOpen = true;
mRequestActive = false;
}

public HeaderSet connect(final HeaderSet header) throws IOException {
//Synthetic comment -- @@ -97,19 +108,20 @@
* Byte 5&6: Max OBEX Packet Length (Defined in MAX_PACKET_SIZE)
* Byte 7 to n: headers
*/
        byte[] requestPacket = new byte[totalLength];
// We just need to start at  byte 3 since the sendRequest() method will
// handle the length and 0x80.
        requestPacket[0] = (byte)0x10;
        requestPacket[1] = (byte)0x00;
        requestPacket[2] = (byte)(ObexHelper.MAX_PACKET_SIZE_INT >> 8);
        requestPacket[3] = (byte)(ObexHelper.MAX_PACKET_SIZE_INT & 0xFF);
if (head != null) {
            System.arraycopy(head, 0, requestPacket, 4, head.length);
}

// check with local max packet size
        if ((requestPacket.length + 3) > ObexHelper.MAX_PACKET_SIZE_INT) {
throw new IOException("Packet size exceeds max packet size");
}

//Synthetic comment -- @@ -214,8 +226,14 @@
}
}

HeaderSet returnHeaderSet = new HeaderSet();
        sendRequest(ObexHelper.OBEX_OPCODE_DISCONNECT, head, returnHeaderSet, null);

/*
* An OBEX DISCONNECT reply from the server:
//Synthetic comment -- @@ -340,11 +358,11 @@
* Byte 5: constants
* Byte 6 & up: headers
*/
        byte[] packet = new byte[totalLength];
        packet[0] = (byte)flags;
        packet[1] = (byte)0x00;
if (headset != null) {
            System.arraycopy(head, 0, packet, 2, head.length);
}

HeaderSet returnHeaderSet = new HeaderSet();
//Synthetic comment -- @@ -405,31 +423,30 @@
*        <code>false</code> if an authentication response failed to pass
* @throws IOException if an IO error occurs
*/
    public boolean sendRequest(int opCode, byte[] head, HeaderSet header,
PrivateInputStream privateInput) throws IOException {
//check header length with local max size
if (head != null) {
            if ((head.length + 3) > ObexHelper.MAX_PACKET_SIZE_INT) {
throw new IOException("header too large ");
}
}

        int bytesReceived;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write((byte)opCode);

// Determine if there are any headers to send
if (head == null) {
            out.write(0x00);
            out.write(0x03);
} else {
            out.write((byte)((head.length + 3) >> 8));
            out.write((byte)(head.length + 3));
            out.write(head);
}

// Write the request to the output stream and flush the stream
        mOutput.write(out.toByteArray());
mOutput.flush();

header.responseCode = mInput.read();
//Synthetic comment -- @@ -440,7 +457,7 @@
throw new IOException("Packet received exceeds packet size limit");
}
if (length > ObexHelper.BASE_PACKET_LENGTH) {
            byte[] data = null;
if (opCode == ObexHelper.OBEX_OPCODE_CONNECT) {
@SuppressWarnings("unused")
int version = mInput.read();
//Synthetic comment -- @@ -454,31 +471,21 @@
}

if (length > 7) {
                    data = new byte[length - 7];

                    bytesReceived = mInput.read(data);
                    while (bytesReceived != (length - 7)) {
                        bytesReceived += mInput.read(data, bytesReceived, data.length
                                - bytesReceived);
                    }
} else {
return true;
}
} else {
                data = new byte[length - 3];
                bytesReceived = mInput.read(data);

                while (bytesReceived != (length - 3)) {
                    bytesReceived += mInput.read(data, bytesReceived, data.length - bytesReceived);
                }
if (opCode == ObexHelper.OBEX_OPCODE_ABORT) {
return true;
}
}

            byte[] body = ObexHelper.updateHeaderSet(header, data);
            if ((privateInput != null) && (body != null)) {
                privateInput.writeBytes(body, 1);
}

if (header.mConnectionID != null) {
//Synthetic comment -- @@ -497,17 +504,23 @@
&& (header.mAuthChall != null)) {

if (handleAuthChall(header)) {
                    out.write((byte)HeaderSet.AUTH_RESPONSE);
                    out.write((byte)((header.mAuthResp.length + 3) >> 8));
                    out.write((byte)(header.mAuthResp.length + 3));
                    out.write(header.mAuthResp);
header.mAuthChall = null;
header.mAuthResp = null;

                    byte[] sendHeaders = new byte[out.size() - 3];
                    System.arraycopy(out.toByteArray(), 3, sendHeaders, 0, sendHeaders.length);

                    return sendRequest(opCode, sendHeaders, header, privateInput);
}
}
}








//Synthetic comment -- diff --git a/obex/javax/obex/HeaderSet.java b/obex/javax/obex/HeaderSet.java
//Synthetic comment -- index b89b707..8cf15f5c 100644

//Synthetic comment -- @@ -126,7 +126,7 @@
/**
* Represents the OBEX End of BODY header.
* <P>
     * The value of <code>BODY</code> is 0x49 (73).
*/
public static final int END_OF_BODY = 0x49;









//Synthetic comment -- diff --git a/obex/javax/obex/ObexByteBuffer.java b/obex/javax/obex/ObexByteBuffer.java
new file mode 100644
//Synthetic comment -- index 0000000..18dafaf

//Synthetic comment -- @@ -0,0 +1,326 @@








//Synthetic comment -- diff --git a/obex/javax/obex/ObexHelper.java b/obex/javax/obex/ObexHelper.java
//Synthetic comment -- index 1b66662..7e02fd5 100644

//Synthetic comment -- @@ -151,21 +151,22 @@
* exception to be thrown. When it is thrown, it is ignored.
* @param header the HeaderSet to update
* @param headerArray the byte array containing headers
     * @return the result of the last start body or end body header provided;
     *         the first byte in the result will specify if a body or end of
     *         body is received
* @throws IOException if an invalid header was found
*/
    public static byte[] updateHeaderSet(HeaderSet header, byte[] headerArray) throws IOException {
int index = 0;
int length = 0;
int headerID;
byte[] value = null;
        byte[] body = null;
HeaderSet headerImpl = header;
try {
            while (index < headerArray.length) {
                headerID = 0xFF & headerArray[index];
switch (headerID & (0xC0)) {

/*
//Synthetic comment -- @@ -181,14 +182,15 @@
case 0x40:
boolean trimTail = true;
index++;
                        length = 0xFF & headerArray[index];
length = length << 8;
index++;
                        length += 0xFF & headerArray[index];
length -= 3;
index++;
                        value = new byte[length];
                        System.arraycopy(headerArray, index, value, 0, length);
if (length == 0 || (length > 0 && (value[length - 1] != 0))) {
trimTail = false;
}
//Synthetic comment -- @@ -198,10 +200,10 @@
// Remove trailing null
if (trimTail == false) {
headerImpl.setHeader(headerID, new String(value, 0,
                                                value.length, "ISO8859_1"));
} else {
headerImpl.setHeader(headerID, new String(value, 0,
                                                value.length - 1, "ISO8859_1"));
}
} catch (UnsupportedEncodingException e) {
throw e;
//Synthetic comment -- @@ -210,27 +212,27 @@

case HeaderSet.AUTH_CHALLENGE:
headerImpl.mAuthChall = new byte[length];
                                System.arraycopy(headerArray, index, headerImpl.mAuthChall, 0,
                                        length);
break;

case HeaderSet.AUTH_RESPONSE:
headerImpl.mAuthResp = new byte[length];
                                System.arraycopy(headerArray, index, headerImpl.mAuthResp, 0,
                                        length);
break;

case HeaderSet.BODY:
/* Fall Through */
case HeaderSet.END_OF_BODY:
                                body = new byte[length + 1];
                                body[0] = (byte)headerID;
                                System.arraycopy(headerArray, index, body, 1, length);
break;

case HeaderSet.TIME_ISO_8601:
try {
                                    String dateString = new String(value, "ISO8859_1");
Calendar temp = Calendar.getInstance();
if ((dateString.length() == 16)
&& (dateString.charAt(15) == 'Z')) {
//Synthetic comment -- @@ -257,9 +259,13 @@
default:
if ((headerID & 0xC0) == 0x00) {
headerImpl.setHeader(headerID, ObexHelper.convertToUnicode(
                                            value, true));
} else {
                                    headerImpl.setHeader(headerID, value);
}
}

//Synthetic comment -- @@ -273,7 +279,7 @@
case 0x80:
index++;
try {
                            headerImpl.setHeader(headerID, Byte.valueOf(headerArray[index]));
} catch (Exception e) {
// Not a valid header so ignore
}
//Synthetic comment -- @@ -288,7 +294,7 @@
case 0xC0:
index++;
value = new byte[4];
                        System.arraycopy(headerArray, index, value, 0, 4);
try {
if (headerID != HeaderSet.TIME_4_BYTE) {
// Determine if it is a connection ID.  These
//Synthetic comment -- @@ -317,8 +323,6 @@
} catch (IOException e) {
throw new IOException("Header was not formatted properly");
}

        return body;
}

/**
//Synthetic comment -- @@ -874,11 +878,11 @@
* @return a Unicode string
* @throws IllegalArgumentException if the byte array has an odd length
*/
    public static String convertToUnicode(byte[] b, boolean includesNull) {
        if (b == null || b.length == 0) {
return null;
}
        int arrayLength = b.length;
if (!((arrayLength % 2) == 0)) {
throw new IllegalArgumentException("Byte array not of a valid form");
}








//Synthetic comment -- diff --git a/obex/javax/obex/ObexSession.java b/obex/javax/obex/ObexSession.java
//Synthetic comment -- index a7daeb5..6c4ba43 100644

//Synthetic comment -- @@ -98,7 +98,7 @@

case ObexHelper.OBEX_AUTH_REALM_CHARSET_UNICODE:
// UNICODE Encoding
                    realm = ObexHelper.convertToUnicode(realmString, false);
break;

default:








//Synthetic comment -- diff --git a/obex/javax/obex/PrivateInputStream.java b/obex/javax/obex/PrivateInputStream.java
//Synthetic comment -- index 5daee72..2bcf541 100644

//Synthetic comment -- @@ -44,9 +44,7 @@

private BaseStream mParent;

    private byte[] mData;

    private int mIndex;

private boolean mOpen;

//Synthetic comment -- @@ -56,8 +54,7 @@
*/
public PrivateInputStream(BaseStream p) {
mParent = p;
        mData = new byte[0];
        mIndex = 0;
mOpen = true;
}

//Synthetic comment -- @@ -73,7 +70,7 @@
@Override
public synchronized int available() throws IOException {
ensureOpen();
        return mData.length - mIndex;
}

/**
//Synthetic comment -- @@ -89,12 +86,12 @@
@Override
public synchronized int read() throws IOException {
ensureOpen();
        while (mData.length == mIndex) {
if (!mParent.continueOperation(true, true)) {
return -1;
}
}
        return (mData[mIndex++] & 0xFF);
}

@Override
//Synthetic comment -- @@ -113,26 +110,23 @@
}
ensureOpen();

        int currentDataLength = mData.length - mIndex;
int remainReadLength = length;
int offset1 = offset;
int result = 0;

        while (currentDataLength <= remainReadLength) {
            System.arraycopy(mData, mIndex, b, offset1, currentDataLength);
            mIndex += currentDataLength;
            offset1 += currentDataLength;
            result += currentDataLength;
            remainReadLength -= currentDataLength;

if (!mParent.continueOperation(true, true)) {
return result == 0 ? -1 : result;
}
            currentDataLength = mData.length - mIndex;
}
if (remainReadLength > 0) {
            System.arraycopy(mData, mIndex, b, offset1, remainReadLength);
            mIndex += remainReadLength;
result += remainReadLength;
}
return result;
//Synthetic comment -- @@ -144,16 +138,8 @@
* @param body the data to add to the stream
* @param start the start of the body to array to copy
*/
    public synchronized void writeBytes(byte[] body, int start) {

        int length = (body.length - start) + (mData.length - mIndex);
        byte[] temp = new byte[length];

        System.arraycopy(mData, mIndex, temp, 0, mData.length - mIndex);
        System.arraycopy(body, start, temp, mData.length - mIndex, body.length - start);

        mData = temp;
        mIndex = 0;
notifyAll();
}









//Synthetic comment -- diff --git a/obex/javax/obex/PrivateOutputStream.java b/obex/javax/obex/PrivateOutputStream.java
//Synthetic comment -- index ca420af..06dd55e 100644

//Synthetic comment -- @@ -34,7 +34,6 @@

import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

/**
* This object provides an output stream to the Operation objects used in this
//Synthetic comment -- @@ -45,7 +44,7 @@

private BaseStream mParent;

    private ByteArrayOutputStream mArray;

private boolean mOpen;

//Synthetic comment -- @@ -57,7 +56,7 @@
*/
public PrivateOutputStream(BaseStream p, int maxSize) {
mParent = p;
        mArray = new ByteArrayOutputStream();
mMaxPacketSize = maxSize;
mOpen = true;
}
//Synthetic comment -- @@ -67,7 +66,7 @@
* @return the number of bytes written to the output stream
*/
public int size() {
        return mArray.size();
}

/**
//Synthetic comment -- @@ -82,8 +81,8 @@
public synchronized void write(int b) throws IOException {
ensureOpen();
mParent.ensureNotDone();
        mArray.write(b);
        if (mArray.size() == mMaxPacketSize) {
mParent.continueOperation(true, false);
}
}
//Synthetic comment -- @@ -108,38 +107,30 @@
ensureOpen();
mParent.ensureNotDone();
if (count < mMaxPacketSize) {
            mArray.write(buffer, offset, count);
} else {
while (remainLength >= mMaxPacketSize) {
                mArray.write(buffer, offset1, mMaxPacketSize);
offset1 += mMaxPacketSize;
remainLength = count - offset1;
mParent.continueOperation(true, false);
}
if (remainLength > 0) {
                mArray.write(buffer, offset1, remainLength);
}
}
}

/**
     * Reads the bytes that have been written to this stream.
     * @param size the size of the array to return
     * @return the byte array that is written
*/
    public synchronized byte[] readBytes(int size) {
        if (mArray.size() > 0) {
            byte[] temp = mArray.toByteArray();
            mArray.reset();
            byte[] result = new byte[size];
            System.arraycopy(temp, 0, result, 0, size);
            if (temp.length != size) {
                mArray.write(temp, size, temp.length - size);
            }
            return result;
        } else {
            return null;
        }
}

/**








//Synthetic comment -- diff --git a/obex/javax/obex/ServerOperation.java b/obex/javax/obex/ServerOperation.java
//Synthetic comment -- index 07a3a53..0e4f47f 100644

//Synthetic comment -- @@ -37,7 +37,6 @@
import java.io.DataInputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;

/**
* This class implements the Operation interface for server side connections.
//Synthetic comment -- @@ -88,6 +87,12 @@

private boolean mHasBody;

/**
* Creates new ServerOperation
* @param p the parent that created this object
//Synthetic comment -- @@ -114,7 +119,10 @@
mRequestFinished = false;
mPrivateOutputOpen = false;
mHasBody = false;
        int bytesReceived;

/*
* Determine if this is a PUT request
//Synthetic comment -- @@ -165,16 +173,12 @@
* Determine if any headers were sent in the initial request
*/
if (length > 3) {
            byte[] data = new byte[length - 3];
            bytesReceived = in.read(data);

            while (bytesReceived != data.length) {
                bytesReceived += in.read(data, bytesReceived, data.length - bytesReceived);
            }

            byte[] body = ObexHelper.updateHeaderSet(requestHeader, data);

            if (body != null) {
mHasBody = true;
}

//Synthetic comment -- @@ -205,8 +209,8 @@

}

            if (body != null) {
                mPrivateInput.writeBytes(body, 1);
} else {
while ((!mGetOperation) && (!finalBitSet)) {
sendReply(ResponseCodes.OBEX_HTTP_CONTINUE);
//Synthetic comment -- @@ -281,8 +285,7 @@
* @throws IOException if an IO error occurs
*/
public synchronized boolean sendReply(int type) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int bytesReceived;

long id = mListener.getConnectionId();
if (id == -1) {
//Synthetic comment -- @@ -322,10 +325,10 @@
mParent.sendResponse(ResponseCodes.OBEX_HTTP_INTERNAL_ERROR, null);
throw new IOException("OBEX Packet exceeds max packet size");
}
                byte[] sendHeader = new byte[end - start];
                System.arraycopy(headerArray, start, sendHeader, 0, sendHeader.length);

                mParent.sendResponse(type, sendHeader);
start = end;
}

//Synthetic comment -- @@ -336,7 +339,7 @@
}

} else {
            out.write(headerArray);
}

// For Get operation: if response code is OBEX_HTTP_OK, then this is the
//Synthetic comment -- @@ -356,36 +359,34 @@
bodyLength = mMaxPacketLength - headerArray.length - 6;
}

                byte[] body = mPrivateOutput.readBytes(bodyLength);

/*
* Since this is a put request if the final bit is set or
* the output stream is closed we need to send the 0x49
* (End of Body) otherwise, we need to send 0x48 (Body)
*/
if ((finalBitSet) || (mPrivateOutput.isClosed())) {
                    out.write(0x49);
} else {
                    out.write(0x48);
}

bodyLength += 3;
                out.write((byte)(bodyLength >> 8));
                out.write((byte)bodyLength);
                out.write(body);
}
}

if ((finalBitSet) && (type == ResponseCodes.OBEX_HTTP_OK) && (orginalBodyLength <= 0)) {
            out.write(0x49);
orginalBodyLength = 3;
            out.write((byte)(orginalBodyLength >> 8));
            out.write((byte)orginalBodyLength);

}

mResponseSize = 3;
        mParent.sendResponse(type, out.toByteArray());

if (type == ResponseCodes.OBEX_HTTP_CONTINUE) {
int headerID = mInput.read();
//Synthetic comment -- @@ -397,12 +398,9 @@
&& (headerID != ObexHelper.OBEX_OPCODE_GET_FINAL)) {

if (length > 3) {
                    byte[] temp = new byte[length];
                    bytesReceived = mInput.read(temp);

                    while (bytesReceived != length) {
                        bytesReceived += mInput.read(temp, bytesReceived, length - bytesReceived);
                    }
}

/*
//Synthetic comment -- @@ -440,17 +438,14 @@
* Determine if any headers were sent in the initial request
*/
if (length > 3) {
                    byte[] data = new byte[length - 3];
                    bytesReceived = mInput.read(data);

                    while (bytesReceived != data.length) {
                        bytesReceived += mInput.read(data, bytesReceived, data.length
                                - bytesReceived);
                    }
                    byte[] body = ObexHelper.updateHeaderSet(requestHeader, data);
                    if (body != null) {
mHasBody = true;
}
if (mListener.getConnectionId() != -1 && requestHeader.mConnectionID != null) {
mListener.setConnectionId(ObexHelper
.convertToLong(requestHeader.mConnectionID));
//Synthetic comment -- @@ -479,8 +474,8 @@
requestHeader.mAuthChall = null;
}

                    if (body != null) {
                        mPrivateInput.writeBytes(body, 1);
}
}
}








//Synthetic comment -- diff --git a/obex/javax/obex/ServerSession.java b/obex/javax/obex/ServerSession.java
//Synthetic comment -- index a4b9759..694c8a91 100644

//Synthetic comment -- @@ -60,6 +60,10 @@

private boolean mClosed;

/**
* Creates new ServerSession.
* @param trans the connection to the client
//Synthetic comment -- @@ -80,6 +84,9 @@
mClosed = false;
mProcessThread = new Thread(this);
mProcessThread.start();
}

/**
//Synthetic comment -- @@ -253,24 +260,22 @@
* @param header the headers to include in the response
* @throws IOException if an IO error occurs
*/
    public void sendResponse(int code, byte[] header) throws IOException {
int totalLength = 3;
        byte[] data = null;

if (header != null) {
            totalLength += header.length;
            data = new byte[totalLength];
            data[0] = (byte)code;
            data[1] = (byte)(totalLength >> 8);
            data[2] = (byte)totalLength;
            System.arraycopy(header, 0, data, 3, header.length);
} else {
            data = new byte[totalLength];
            data[0] = (byte)code;
            data[1] = (byte)0x00;
            data[2] = (byte)totalLength;
}
        mOutput.write(data);
mOutput.flush();
}

//Synthetic comment -- @@ -291,7 +296,6 @@
int totalLength = 3;
byte[] head = null;
int code = -1;
        int bytesReceived;
HeaderSet request = new HeaderSet();
HeaderSet reply = new HeaderSet();

//Synthetic comment -- @@ -305,15 +309,10 @@
totalLength = 3;
} else {
if (length > 5) {
                byte[] headers = new byte[length - 5];
                bytesReceived = mInput.read(headers);

                while (bytesReceived != headers.length) {
                    bytesReceived += mInput.read(headers, bytesReceived, headers.length
                            - bytesReceived);
                }

                ObexHelper.updateHeaderSet(request, headers);

if (mListener.getConnectionId() != -1 && request.mConnectionID != null) {
mListener.setConnectionId(ObexHelper.convertToLong(request.mConnectionID));
//Synthetic comment -- @@ -386,18 +385,18 @@
}

// Compute Length of OBEX SETPATH packet
        byte[] replyData = new byte[totalLength];
        replyData[0] = (byte)code;
        replyData[1] = (byte)(totalLength >> 8);
        replyData[2] = (byte)totalLength;
if (head != null) {
            System.arraycopy(head, 0, replyData, 3, head.length);
}
/*
* Write the OBEX SETPATH packet to the server. Byte 0: response code
* Byte 1&2: Connect Packet Length Byte 3 to n: headers
*/
        mOutput.write(replyData);
mOutput.flush();
}

//Synthetic comment -- @@ -414,7 +413,6 @@
int code = ResponseCodes.OBEX_HTTP_OK;
int totalLength = 3;
byte[] head = null;
        int bytesReceived;
HeaderSet request = new HeaderSet();
HeaderSet reply = new HeaderSet();

//Synthetic comment -- @@ -426,15 +424,10 @@
totalLength = 3;
} else {
if (length > 3) {
                byte[] headers = new byte[length - 3];
                bytesReceived = mInput.read(headers);

                while (bytesReceived != headers.length) {
                    bytesReceived += mInput.read(headers, bytesReceived, headers.length
                            - bytesReceived);
                }

                ObexHelper.updateHeaderSet(request, headers);
}

if (mListener.getConnectionId() != -1 && request.mConnectionID != null) {
//Synthetic comment -- @@ -485,23 +478,18 @@
}

// Compute Length of OBEX CONNECT packet
        byte[] replyData;
if (head != null) {
            replyData = new byte[3 + head.length];
        } else {
            replyData = new byte[3];
        }
        replyData[0] = (byte)code;
        replyData[1] = (byte)(totalLength >> 8);
        replyData[2] = (byte)totalLength;
        if (head != null) {
            System.arraycopy(head, 0, replyData, 3, head.length);
}
/*
* Write the OBEX DISCONNECT packet to the server. Byte 0: response code
* Byte 1&2: Connect Packet Length Byte 3 to n: headers
*/
        mOutput.write(replyData);
mOutput.flush();
}

//Synthetic comment -- @@ -525,7 +513,6 @@
int code = -1;
HeaderSet request = new HeaderSet();
HeaderSet reply = new HeaderSet();
        int bytesReceived;

/*
* Read in the length of the OBEX packet, OBEX version, flags, and max
//Synthetic comment -- @@ -548,15 +535,10 @@
totalLength = 7;
} else {
if (packetLength > 7) {
                byte[] headers = new byte[packetLength - 7];
                bytesReceived = mInput.read(headers);

                while (bytesReceived != headers.length) {
                    bytesReceived += mInput.read(headers, bytesReceived, headers.length
                            - bytesReceived);
                }

                ObexHelper.updateHeaderSet(request, headers);
}

if (mListener.getConnectionId() != -1 && request.mConnectionID != null) {







