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

/**
* This class implements the <code>Operation</code> interface. It will read and
//Synthetic comment -- @@ -72,6 +71,8 @@

private boolean mEndOfBodySent;

    private ObexByteBuffer mOutBuffer;

/**
* Creates new OperationImpl to read and write data to a server
* @param maxSize the maximum packet size
//Synthetic comment -- @@ -100,6 +101,8 @@

mRequestHeader = new HeaderSet();

        mOutBuffer = new ObexByteBuffer(32);

int[] headerList = header.getHeaderList();

if (headerList != null) {
//Synthetic comment -- @@ -396,7 +399,7 @@
*/
private boolean sendRequest(int opCode) throws IOException {
boolean returnValue = false;

int bodyLength = -1;
byte[] headerArray = ObexHelper.createHeader(mRequestHeader, true);
if (mPrivateOutput != null) {
//Synthetic comment -- @@ -437,9 +440,9 @@
throw new IOException("OBEX Packet exceeds max packet size");
}

                mOutBuffer.reset();
                mOutBuffer.write(headerArray, start, end - start);
                if (!mParent.sendRequest(opCode, mOutBuffer, mReplyHeader, mPrivateInput)) {
return false;
}

//Synthetic comment -- @@ -456,7 +459,8 @@
return false;
}
} else {
            mOutBuffer.reset();
            mOutBuffer.write(headerArray);
}

if (bodyLength > 0) {
//Synthetic comment -- @@ -471,8 +475,6 @@
bodyLength = mMaxPacketSize - headerArray.length - 6;
}

/*
* Since this is a put request if the final bit is set or
* the output stream is closed we need to send the 0x49
//Synthetic comment -- @@ -480,44 +482,40 @@
*/
if ((mPrivateOutput.isClosed()) && (!returnValue) && (!mEndOfBodySent)
&& ((opCode & 0x80) != 0)) {
                mOutBuffer.write((byte)0x49);
mEndOfBodySent = true;
} else {
                mOutBuffer.write((byte)0x48);
}

bodyLength += 3;
            mOutBuffer.write((byte)(bodyLength >> 8));
            mOutBuffer.write((byte)bodyLength);
            mPrivateOutput.writeTo(mOutBuffer, bodyLength - 3);
}

if (mPrivateOutputOpen && bodyLength <= 0 && !mEndOfBodySent) {
// only 0x82 or 0x83 can send 0x49
if ((opCode & 0x80) == 0) {
                mOutBuffer.write((byte)0x48);
} else {
                mOutBuffer.write((byte)0x49);
mEndOfBodySent = true;
}

bodyLength = 3;
            mOutBuffer.write((byte)(bodyLength >> 8));
            mOutBuffer.write((byte)bodyLength);
}

        if (mOutBuffer.getLength() == 0) {
if (!mParent.sendRequest(opCode, null, mReplyHeader, mPrivateInput)) {
return false;
}
return returnValue;
}
        if ((mOutBuffer.getLength() > 0)
                && (!mParent.sendRequest(opCode, mOutBuffer, mReplyHeader, mPrivateInput))) {
return false;
}









//Synthetic comment -- diff --git a/obex/javax/obex/ClientSession.java b/obex/javax/obex/ClientSession.java
//Synthetic comment -- index 0935383..b7fa1a7 100644

//Synthetic comment -- @@ -32,7 +32,6 @@

package javax.obex;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
//Synthetic comment -- @@ -62,11 +61,23 @@

private final OutputStream mOutput;

    private ObexByteBuffer mOutBuffer;

    private ObexByteBuffer mData;

    private ObexByteBuffer mBodyBuffer;

    private ObexByteBuffer mHeaderBuffer;

public ClientSession(final ObexTransport trans) throws IOException {
mInput = trans.openInputStream();
mOutput = trans.openOutputStream();
mOpen = true;
mRequestActive = false;
        mOutBuffer = new ObexByteBuffer(32);
        mData = new ObexByteBuffer(32);
        mBodyBuffer = new ObexByteBuffer(32);
        mHeaderBuffer = new ObexByteBuffer(32);
}

public HeaderSet connect(final HeaderSet header) throws IOException {
//Synthetic comment -- @@ -97,19 +108,20 @@
* Byte 5&6: Max OBEX Packet Length (Defined in MAX_PACKET_SIZE)
* Byte 7 to n: headers
*/
        ObexByteBuffer requestPacket = new ObexByteBuffer(totalLength);

// We just need to start at  byte 3 since the sendRequest() method will
// handle the length and 0x80.
        requestPacket.write((byte)0x10);
        requestPacket.write((byte)0x00);
        requestPacket.write((byte)(ObexHelper.MAX_PACKET_SIZE_INT >> 8));
        requestPacket.write((byte)(ObexHelper.MAX_PACKET_SIZE_INT & 0xFF));
if (head != null) {
            requestPacket.write(head);
}

// check with local max packet size
        if ((totalLength + 3) > ObexHelper.MAX_PACKET_SIZE_INT) {
throw new IOException("Packet size exceeds max packet size");
}

//Synthetic comment -- @@ -214,8 +226,14 @@
}
}

        ObexByteBuffer headBuffer = null;
        if (head != null) {
            headBuffer = new ObexByteBuffer(head.length);
            headBuffer.write(head);
        }

HeaderSet returnHeaderSet = new HeaderSet();
        sendRequest(ObexHelper.OBEX_OPCODE_DISCONNECT, headBuffer, returnHeaderSet, null);

/*
* An OBEX DISCONNECT reply from the server:
//Synthetic comment -- @@ -340,11 +358,11 @@
* Byte 5: constants
* Byte 6 & up: headers
*/
        ObexByteBuffer packet = new ObexByteBuffer(totalLength);
        packet.write((byte)flags);
        packet.write((byte)0x00);
if (headset != null) {
            packet.write(head);
}

HeaderSet returnHeaderSet = new HeaderSet();
//Synthetic comment -- @@ -405,31 +423,30 @@
*        <code>false</code> if an authentication response failed to pass
* @throws IOException if an IO error occurs
*/
    public boolean sendRequest(int opCode, ObexByteBuffer head, HeaderSet header,
PrivateInputStream privateInput) throws IOException {
//check header length with local max size
if (head != null) {
            if ((head.getLength() + 3) > ObexHelper.MAX_PACKET_SIZE_INT) {
throw new IOException("header too large ");
}
}

        mOutBuffer.reset();
        mOutBuffer.write((byte)opCode);

// Determine if there are any headers to send
if (head == null) {
            mOutBuffer.write((byte)0x00);
            mOutBuffer.write((byte)0x03);
} else {
            mOutBuffer.write((byte)((head.getLength() + 3) >> 8));
            mOutBuffer.write((byte)(head.getLength() + 3));
            mOutBuffer.write(head, 0);
}

// Write the request to the output stream and flush the stream
        mOutBuffer.peek(mOutput, mOutBuffer.getLength());
mOutput.flush();

header.responseCode = mInput.read();
//Synthetic comment -- @@ -440,7 +457,7 @@
throw new IOException("Packet received exceeds packet size limit");
}
if (length > ObexHelper.BASE_PACKET_LENGTH) {
            mData.reset();
if (opCode == ObexHelper.OBEX_OPCODE_CONNECT) {
@SuppressWarnings("unused")
int version = mInput.read();
//Synthetic comment -- @@ -454,31 +471,21 @@
}

if (length > 7) {
                    mData.write(mInput, length - 7);
} else {
return true;
}
} else {
                mData.write(mInput, length - 3);
if (opCode == ObexHelper.OBEX_OPCODE_ABORT) {
return true;
}
}

            ObexHelper.updateHeaderSet(header, mData, mBodyBuffer, mHeaderBuffer);

            if ((privateInput != null) && (mBodyBuffer.getLength() > 0)) {
                privateInput.writeBytes(mBodyBuffer, 1);
}

if (header.mConnectionID != null) {
//Synthetic comment -- @@ -497,17 +504,26 @@
&& (header.mAuthChall != null)) {

if (handleAuthChall(header)) {

                    // This can't be a member variable and mOutBuffer can't be used here
                    // since this is a recursive call.
                    // That's OK since authentication should not happen very often.

                    // The first three bytes of mOutBuffer is header data which should not be included.
                    // Three bytes are allocated for a new header instead.
                    ObexByteBuffer sendHeadersBuffer = new ObexByteBuffer(
                            (mOutBuffer.getLength() - 3) + header.mAuthResp.length + 3);
                    sendHeadersBuffer.write(mOutBuffer, 3);

                    sendHeadersBuffer.write((byte)HeaderSet.AUTH_RESPONSE);
                    sendHeadersBuffer.write((byte)((header.mAuthResp.length + 3) >> 8));
                    sendHeadersBuffer.write((byte)(header.mAuthResp.length + 3));
                    sendHeadersBuffer.write(header.mAuthResp);

header.mAuthChall = null;
header.mAuthResp = null;

                    return sendRequest(opCode, sendHeadersBuffer, header, privateInput);
}
}
}








//Synthetic comment -- diff --git a/obex/javax/obex/HeaderSet.java b/obex/javax/obex/HeaderSet.java
//Synthetic comment -- index b89b707..8cf15f5c 100644

//Synthetic comment -- @@ -126,7 +126,7 @@
/**
* Represents the OBEX End of BODY header.
* <P>
     * The value of <code>END_OF_BODY</code> is 0x49 (73).
*/
public static final int END_OF_BODY = 0x49;









//Synthetic comment -- diff --git a/obex/javax/obex/ObexByteBuffer.java b/obex/javax/obex/ObexByteBuffer.java
new file mode 100644
//Synthetic comment -- index 0000000..4e7e115

//Synthetic comment -- @@ -0,0 +1,326 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package javax.obex;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class ObexByteBuffer {
    private static final int REALLOC_EXTRA_SPACE = 24;

    private byte[] mBuffer;

    private int mIndex;

    private int mLength;

    public ObexByteBuffer(int initialSize) {
        mBuffer = new byte[initialSize];
        mIndex = 0;
        mLength = 0;
    }

    /**
     * Mark bytes at beginning or valid data as invalid.
     * @param numBytes Number of bytes to consume.
     */
    private void consume(int numBytes) {
        mLength -= numBytes;
        if (mLength > 0) {
            mIndex += numBytes;
        } else {
            mIndex = 0;
        }
    }

    /**
     * Make room in for new data (if needed).
     * @param numBytes Number of bytes to make room for.
     */
    private void acquire(int numBytes) {
        int remainingSpace = mBuffer.length - (mIndex + mLength);

        // Do we need to grow or shuffle?
        if (remainingSpace < numBytes) {
            int availableSpace = mBuffer.length - mLength;
            if (availableSpace < numBytes) {
                // Need to grow. Add some extra space to avoid small growth.
                byte[] newbuf = new byte[mLength + numBytes + REALLOC_EXTRA_SPACE];
                System.arraycopy(mBuffer, mIndex, newbuf, 0, mLength);
                mBuffer = newbuf;
            } else {
                // Need to shuffle
                System.arraycopy(mBuffer, mIndex, mBuffer, 0, mLength);
            }
            mIndex = 0;
        }
    }

    /**
     * Get the internal byte array. Use with care.
     * @return the internal byte array
     */
    public byte[] getBytes() {
        return mBuffer;
    }

    /**
     * Get number of written but not consumed bytes.
     * @return number of bytes
     */
    public int getLength() {
        return mLength;
    }

    /**
     * Discard all unconsumed bytes.
     */
    public void reset() {
        mIndex = 0;
        mLength = 0;
    }

    /**
     * Read and consume one byte.
     * @return Next unconsumed byte.
     */
    public byte read() {
        if (mLength == 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        mLength--;
        return mBuffer[mIndex++];
    }

    /**
     * Read and consume bytes, and write them into a byte array.
     * Will read (dest.length - destOffset) bytes.
     * @param dest Array to copy data into.
     * @param destOffset Where to start writing in dest.
     * @return number of read bytes.
     */
    public int read(byte[] dest, int destOffset) {
        return read(dest, destOffset, mLength);
    }

    /**
     * Read and consume bytes, and write them into a byte array.
     * Will read length bytes.
     * @param dest Array to copy data into.
     * @param destOffset Where to start writing in dest.
     * @param length Number of bytes to read.
     * @return number of read bytes.
     */
    public int read(byte[] dest, int destOffset, int length) {
        peek(0, dest, destOffset, length);
        consume(length);
        return length;
    }

    /**
     * Read and consume bytes, and write them into another ObexByteBuffer.
     * @param dest ObexByteBuffer to copy data into.
     * @param length Number of bytes to read.
     * @return number of read bytes.
     */
    public int read(ObexByteBuffer dest, int length) {
        if (length > mLength) {
            throw new ArrayIndexOutOfBoundsException();
        }
        dest.write(mBuffer, mIndex, length);
        consume(length);

        return length;
    }

    /**
     * Read and consume all unconsumed bytes, and write them into an OutputStream.
     * @param dest OutputStream to copy data into.
     * @return number of read bytes.
     */
    public int read(OutputStream stream) throws IOException {
        return read(stream, mLength);
    }

    /**
     * Read and consume bytes, and write them into an OutputStream.
     * @param dest OutputStream to copy data into.
     * @param length Number of bytes to read.
     * @return number of read bytes.
     */
    public int read(OutputStream destStream, int length) throws IOException {
        peek(destStream, length);
        consume(length);
        return length;
    }

    /**
     * Read (but don't consume) one byte.
     * @param offset Offset into unconsumed bytes.
     * @return Requested unconsumed byte.
     */
    public byte peek(int offset) {
        if (offset >= mLength) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return mBuffer[mIndex + offset];
    }

    /**
     * Read (but don't consume) bytes and write them into a byte array.
     * Will read dest.length bytes.
     * @param offset Offset into unconsumed bytes.
     * @param dest Array to copy data into.
     */
    public void peek(int offset, byte[] dest) {
        peek(offset, dest, 0, dest.length);
    }

    /**
     * Read (but don't consume) bytes and write them into a byte array.
     * Will read length bytes.
     * @param offset Offset into unconsumed bytes.
     * @param dest Array to copy data into.
     * @param destOffset Where to start writing in dest.
     * @param length Number of bytes to read.
     */
    public void peek(int offset, byte[] dest, int destOffset, int length) {
        if (offset + length > mLength) {
            throw new ArrayIndexOutOfBoundsException();
        }
        System.arraycopy(mBuffer, mIndex + offset, dest, destOffset, length);
    }

    /**
     * Read (but don't consume) bytes, and write them into an OutputStream.
     * @param dest OutputStream to copy data into.
     * @param length Number of bytes to read.
     */
    public void peek(OutputStream stream, int length) throws IOException {
        if (length > mLength) {
            throw new ArrayIndexOutOfBoundsException();
        }
        stream.write(mBuffer, mIndex, length);
    }

    /**
     * Write a new byte.
     * @param src Byte to write.
     */
    public void write(byte src) {
        acquire(1);
        mBuffer[mIndex + mLength] = src;
        mLength++;
    }

    /**
     * Read bytes from a byte array and add to unconsumed bytes.
     * Will copy src.length bytes.
     * @param src Array to read from.
     */
    public void write(byte[] src) {
        write(src, 0, src.length);
    }

    /**
     * Read bytes from a byte array and add to unconsumed bytes.
     * Will copy (src.length - srcOffset) bytes.
     * @param src Array to read from.
     * @param srcOffset Offset into source array.
     */
    public void write(byte[] src, int srcOffset) {
        write(src, srcOffset, src.length - srcOffset);
    }

    /**
     * Read bytes from a byte array and add to unconsumed bytes.
     * Will copy srcLength bytes.
     * @param src Array to read from.
     * @param srcOffset Offset into source array.
     * @param srcLength Number of bytes to read/write.
     */
    public void write(byte[] src, int srcOffset, int srcLength) {
        // Make sure we have space.
        acquire(srcLength);

        // Add the new data at the end
        System.arraycopy(src, srcOffset, mBuffer, mIndex + mLength, srcLength);
        mLength += srcLength;
    }

    /**
     * Read bytes from another ObexByteBuffer and add to unconsumed bytes.
     * Will copy src.getLength() bytes. The bytes in src will not be consumed.
     * @param src ObexByteBuffer to read from.
     * @param srcOffset Offset into source array.
     */
    public void write(ObexByteBuffer src) {
        write(src.mBuffer, 0, src.getLength());
    }

    /**
     * Read bytes from another ObexByteBuffer and add to unconsumed bytes.
     * Will copy (src.getLength() - srcOffset) bytes. The bytes in src will not
     * be consumed.
     * @param src ObexByteBuffer to read from.
     * @param srcOffset Offset into source array.
     */
    public void write(ObexByteBuffer src, int srcOffset) {
        if (srcOffset > src.mLength) {
            throw new ArrayIndexOutOfBoundsException();
        }
        write(src.mBuffer, src.mIndex + srcOffset, src.mLength - src.mIndex - srcOffset);
    }

    /**
     * Read bytes from another ObexByteBuffer and add to unconsumed bytes.
     * Will copy srcLength bytes. The bytes in src will not be
     * consumed.
     * @param src ObexByteBuffer to read from.
     * @param srcOffset Offset into source array.
     * @param srcLength Number of bytes to read/write.
     */
    public void write(ObexByteBuffer src, int srcOffset, int srcLength) {
        if (srcOffset + srcLength > src.mLength) {
            throw new ArrayIndexOutOfBoundsException();
        }
        write(src.mBuffer, src.mIndex + srcOffset, srcLength);
    }

    /**
     * Read bytes from an InputStream and add to unconsumed bytes.
     * @param src InputStream to read from
     * @param srcLength Number of bytes to read
     * @throws IOException
     */
    public void write(InputStream src, int srcLength) throws IOException {
        // First make sure we have space.
        acquire(srcLength);

        // Read data until the requested number of bytes have been read.
        int numBytes = 0;
        do {
            int readBytes = src.read(mBuffer, mIndex + mLength + numBytes, srcLength - numBytes);
            if (readBytes == -1) {
                throw new IOException();
            }
            numBytes += readBytes;
        } while (numBytes != srcLength);
        mLength += numBytes;
    }
}








//Synthetic comment -- diff --git a/obex/javax/obex/ObexHelper.java b/obex/javax/obex/ObexHelper.java
//Synthetic comment -- index 1b66662..7e02fd5 100644

//Synthetic comment -- @@ -151,21 +151,22 @@
* exception to be thrown. When it is thrown, it is ignored.
* @param header the HeaderSet to update
* @param headerArray the byte array containing headers
     * @param bodyBuffer Buffer to return result in. Will be reset(). Can be null.
     *   Will contain the result of the last start body or end body header provided;
     *   the first byte in the result will specify if a body or end of body is received
     * @param headerBuffer Buffer to use to avoid allocations. Will be reset().
* @throws IOException if an invalid header was found
*/
    static void updateHeaderSet(HeaderSet header, ObexByteBuffer headerArray,
            ObexByteBuffer bodyBuffer, ObexByteBuffer headerBuffer) throws IOException {
int index = 0;
int length = 0;
int headerID;
byte[] value = null;
HeaderSet headerImpl = header;
try {
            while (index < headerArray.getLength()) {
                headerID = 0xFF & headerArray.peek(index);
switch (headerID & (0xC0)) {

/*
//Synthetic comment -- @@ -181,14 +182,15 @@
case 0x40:
boolean trimTail = true;
index++;
                        length = 0xFF & headerArray.peek(index);
length = length << 8;
index++;
                        length += 0xFF & headerArray.peek(index);
length -= 3;
index++;
                        headerBuffer.reset();
                        headerBuffer.write(headerArray, index, length);
                        value = headerBuffer.getBytes();
if (length == 0 || (length > 0 && (value[length - 1] != 0))) {
trimTail = false;
}
//Synthetic comment -- @@ -198,10 +200,10 @@
// Remove trailing null
if (trimTail == false) {
headerImpl.setHeader(headerID, new String(value, 0,
                                                length, "ISO8859_1"));
} else {
headerImpl.setHeader(headerID, new String(value, 0,
                                                length - 1, "ISO8859_1"));
}
} catch (UnsupportedEncodingException e) {
throw e;
//Synthetic comment -- @@ -210,27 +212,27 @@

case HeaderSet.AUTH_CHALLENGE:
headerImpl.mAuthChall = new byte[length];
                                headerArray.peek(index, headerImpl.mAuthChall);
break;

case HeaderSet.AUTH_RESPONSE:
headerImpl.mAuthResp = new byte[length];
                                headerArray.peek(index, headerImpl.mAuthResp);
break;

case HeaderSet.BODY:
/* Fall Through */
case HeaderSet.END_OF_BODY:
                                if (bodyBuffer != null) {
                                    bodyBuffer.reset();
                                    bodyBuffer.write((byte)headerID);
                                    bodyBuffer.write(headerArray, index, length);
                                }
break;

case HeaderSet.TIME_ISO_8601:
try {
                                    String dateString = new String(value, 0, length, "ISO8859_1");
Calendar temp = Calendar.getInstance();
if ((dateString.length() == 16)
&& (dateString.charAt(15) == 'Z')) {
//Synthetic comment -- @@ -257,9 +259,13 @@
default:
if ((headerID & 0xC0) == 0x00) {
headerImpl.setHeader(headerID, ObexHelper.convertToUnicode(
                                            value, length, true));
} else {
                                    // <value> is reused and can contain garbage after [length-1].
                                    // Copy wanted data to temporary array.
                                    byte[] otherValue = new byte[length];
                                    System.arraycopy(value, 0, otherValue, 0, length);
                                    headerImpl.setHeader(headerID, otherValue);
}
}

//Synthetic comment -- @@ -273,7 +279,7 @@
case 0x80:
index++;
try {
                            headerImpl.setHeader(headerID, Byte.valueOf(headerArray.peek(index)));
} catch (Exception e) {
// Not a valid header so ignore
}
//Synthetic comment -- @@ -288,7 +294,7 @@
case 0xC0:
index++;
value = new byte[4];
                        headerArray.peek(index, value);
try {
if (headerID != HeaderSet.TIME_4_BYTE) {
// Determine if it is a connection ID.  These
//Synthetic comment -- @@ -317,8 +323,6 @@
} catch (IOException e) {
throw new IOException("Header was not formatted properly");
}
}

/**
//Synthetic comment -- @@ -874,11 +878,11 @@
* @return a Unicode string
* @throws IllegalArgumentException if the byte array has an odd length
*/
    public static String convertToUnicode(byte[] b, int len, boolean includesNull) {
        if (b == null || len == 0) {
return null;
}
        int arrayLength = len;
if (!((arrayLength % 2) == 0)) {
throw new IllegalArgumentException("Byte array not of a valid form");
}








//Synthetic comment -- diff --git a/obex/javax/obex/ObexSession.java b/obex/javax/obex/ObexSession.java
//Synthetic comment -- index a7daeb5..6c4ba43 100644

//Synthetic comment -- @@ -98,7 +98,7 @@

case ObexHelper.OBEX_AUTH_REALM_CHARSET_UNICODE:
// UNICODE Encoding
                    realm = ObexHelper.convertToUnicode(realmString, realmString.length, false);
break;

default:








//Synthetic comment -- diff --git a/obex/javax/obex/PrivateInputStream.java b/obex/javax/obex/PrivateInputStream.java
//Synthetic comment -- index 5daee72..2bcf541 100644

//Synthetic comment -- @@ -44,9 +44,7 @@

private BaseStream mParent;

    private ObexByteBuffer mBuffer;

private boolean mOpen;

//Synthetic comment -- @@ -56,8 +54,7 @@
*/
public PrivateInputStream(BaseStream p) {
mParent = p;
        mBuffer = new ObexByteBuffer(0);
mOpen = true;
}

//Synthetic comment -- @@ -73,7 +70,7 @@
@Override
public synchronized int available() throws IOException {
ensureOpen();
        return mBuffer.getLength();
}

/**
//Synthetic comment -- @@ -89,12 +86,12 @@
@Override
public synchronized int read() throws IOException {
ensureOpen();
        while (mBuffer.getLength() == 0) {
if (!mParent.continueOperation(true, true)) {
return -1;
}
}
        return (mBuffer.read() & 0xFF);
}

@Override
//Synthetic comment -- @@ -113,26 +110,23 @@
}
ensureOpen();

int remainReadLength = length;
int offset1 = offset;
int result = 0;

        while (mBuffer.getLength() <= remainReadLength) {
            int readBytes = mBuffer.read(b, offset1);

            offset1 += readBytes;
            result += readBytes;
            remainReadLength -= readBytes;

if (!mParent.continueOperation(true, true)) {
return result == 0 ? -1 : result;
}
}
if (remainReadLength > 0) {
            mBuffer.read(b, offset1, remainReadLength);
result += remainReadLength;
}
return result;
//Synthetic comment -- @@ -144,16 +138,8 @@
* @param body the data to add to the stream
* @param start the start of the body to array to copy
*/
    public synchronized void writeBytes(ObexByteBuffer body, int start) {
        mBuffer.write(body, start);
notifyAll();
}









//Synthetic comment -- diff --git a/obex/javax/obex/PrivateOutputStream.java b/obex/javax/obex/PrivateOutputStream.java
//Synthetic comment -- index ca420af..06dd55e 100644

//Synthetic comment -- @@ -34,7 +34,6 @@

import java.io.IOException;
import java.io.OutputStream;

/**
* This object provides an output stream to the Operation objects used in this
//Synthetic comment -- @@ -45,7 +44,7 @@

private BaseStream mParent;

    private ObexByteBuffer mBuffer;

private boolean mOpen;

//Synthetic comment -- @@ -57,7 +56,7 @@
*/
public PrivateOutputStream(BaseStream p, int maxSize) {
mParent = p;
        mBuffer = new ObexByteBuffer(32);
mMaxPacketSize = maxSize;
mOpen = true;
}
//Synthetic comment -- @@ -67,7 +66,7 @@
* @return the number of bytes written to the output stream
*/
public int size() {
        return mBuffer.getLength();
}

/**
//Synthetic comment -- @@ -82,8 +81,8 @@
public synchronized void write(int b) throws IOException {
ensureOpen();
mParent.ensureNotDone();
        mBuffer.write((byte)b);
        if (mBuffer.getLength() == mMaxPacketSize) {
mParent.continueOperation(true, false);
}
}
//Synthetic comment -- @@ -108,38 +107,30 @@
ensureOpen();
mParent.ensureNotDone();
if (count < mMaxPacketSize) {
            mBuffer.write(buffer, offset, count);
} else {
while (remainLength >= mMaxPacketSize) {
                mBuffer.write(buffer, offset1, mMaxPacketSize);
offset1 += mMaxPacketSize;
remainLength = count - offset1;
mParent.continueOperation(true, false);
}
if (remainLength > 0) {
                mBuffer.write(buffer, offset1, remainLength);
}
}
}

/**
     * Write some of the bytes that have been written to this stream to
     * an ObexByteBuffer.
     *
     * @param dest the stream to write to
     * @param start where to write in the byte array
     * @param size the number of bytes to write to the byte array
*/
    public synchronized void writeTo(ObexByteBuffer dest, int size) throws IOException {
        mBuffer.read(dest, size);
}

/**








//Synthetic comment -- diff --git a/obex/javax/obex/ServerOperation.java b/obex/javax/obex/ServerOperation.java
//Synthetic comment -- index d1476d2..89c0779 100644

//Synthetic comment -- @@ -37,7 +37,6 @@
import java.io.DataInputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;

/**
* This class implements the Operation interface for server side connections.
//Synthetic comment -- @@ -88,6 +87,12 @@

private boolean mHasBody;

    private ObexByteBuffer mData;

    private ObexByteBuffer mBodyBuffer;

    private ObexByteBuffer mHeaderBuffer;

/**
* Creates new ServerOperation
* @param p the parent that created this object
//Synthetic comment -- @@ -114,7 +119,10 @@
mRequestFinished = false;
mPrivateOutputOpen = false;
mHasBody = false;

        mData = new ObexByteBuffer(32);
        mBodyBuffer = new ObexByteBuffer(32);
        mHeaderBuffer = new ObexByteBuffer(32);

/*
* Determine if this is a PUT request
//Synthetic comment -- @@ -165,16 +173,12 @@
* Determine if any headers were sent in the initial request
*/
if (length > 3) {
            mData.reset();
            mData.write(in, length - 3);

            ObexHelper.updateHeaderSet(requestHeader, mData, mBodyBuffer, mHeaderBuffer);

            if (mBodyBuffer.getLength() > 0) {
mHasBody = true;
}

//Synthetic comment -- @@ -205,8 +209,8 @@

}

            if (mBodyBuffer.getLength() > 0) {
                mPrivateInput.writeBytes(mBodyBuffer, 1);
} else {
while ((!mGetOperation) && (!finalBitSet)) {
sendReply(ResponseCodes.OBEX_HTTP_CONTINUE);
//Synthetic comment -- @@ -281,8 +285,7 @@
* @throws IOException if an IO error occurs
*/
public synchronized boolean sendReply(int type) throws IOException {
        mBodyBuffer.reset();

long id = mListener.getConnectionId();
if (id == -1) {
//Synthetic comment -- @@ -322,10 +325,10 @@
mParent.sendResponse(ResponseCodes.OBEX_HTTP_INTERNAL_ERROR, null);
throw new IOException("OBEX Packet exceeds max packet size");
}
                mHeaderBuffer.reset();
                mHeaderBuffer.write(headerArray, start, end - start);

                mParent.sendResponse(type, mHeaderBuffer);
start = end;
}

//Synthetic comment -- @@ -336,7 +339,7 @@
}

} else {
            mBodyBuffer.write(headerArray);
}

// For Get operation: if response code is OBEX_HTTP_OK, then this is the
//Synthetic comment -- @@ -356,36 +359,34 @@
bodyLength = mMaxPacketLength - headerArray.length - 6;
}

/*
* Since this is a put request if the final bit is set or
* the output stream is closed we need to send the 0x49
* (End of Body) otherwise, we need to send 0x48 (Body)
*/
if ((finalBitSet) || (mPrivateOutput.isClosed())) {
                    mBodyBuffer.write((byte)0x49);
} else {
                    mBodyBuffer.write((byte)0x48);
}

bodyLength += 3;
                mBodyBuffer.write((byte)(bodyLength >> 8));
                mBodyBuffer.write((byte)bodyLength);
                mPrivateOutput.writeTo(mBodyBuffer, bodyLength - 3);
}
}

if ((finalBitSet) && (type == ResponseCodes.OBEX_HTTP_OK) && (orginalBodyLength <= 0)) {
            mBodyBuffer.write((byte)0x49);
orginalBodyLength = 3;
            mBodyBuffer.write((byte)(orginalBodyLength >> 8));
            mBodyBuffer.write((byte)orginalBodyLength);

}

mResponseSize = 3;
        mParent.sendResponse(type, mBodyBuffer);

if (type == ResponseCodes.OBEX_HTTP_CONTINUE) {
int headerID = mInput.read();
//Synthetic comment -- @@ -397,14 +398,9 @@
&& (headerID != ObexHelper.OBEX_OPCODE_GET_FINAL)) {

if (length > 3) {
                   mData.reset();
// First three bytes already read, compensating for this
                    mData.write(mInput, length - 3);
}

/*
//Synthetic comment -- @@ -442,17 +438,14 @@
* Determine if any headers were sent in the initial request
*/
if (length > 3) {
                    mData.reset();
                    mData.write(mInput, length - 3);

                    ObexHelper.updateHeaderSet(requestHeader, mData, mBodyBuffer, mHeaderBuffer);
                    if (mBodyBuffer.getLength() > 0) {
mHasBody = true;
}

if (mListener.getConnectionId() != -1 && requestHeader.mConnectionID != null) {
mListener.setConnectionId(ObexHelper
.convertToLong(requestHeader.mConnectionID));
//Synthetic comment -- @@ -481,8 +474,8 @@
requestHeader.mAuthChall = null;
}

                    if (mBodyBuffer.getLength() > 0) {
                        mPrivateInput.writeBytes(mBodyBuffer, 1);
}
}
}








//Synthetic comment -- diff --git a/obex/javax/obex/ServerSession.java b/obex/javax/obex/ServerSession.java
//Synthetic comment -- index a4b9759..694c8a91 100644

//Synthetic comment -- @@ -60,6 +60,10 @@

private boolean mClosed;

    private ObexByteBuffer mData;

    private ObexByteBuffer mHeaderBuffer;

/**
* Creates new ServerSession.
* @param trans the connection to the client
//Synthetic comment -- @@ -80,6 +84,9 @@
mClosed = false;
mProcessThread = new Thread(this);
mProcessThread.start();

        mData = new ObexByteBuffer(32);
        mHeaderBuffer = new ObexByteBuffer(32);
}

/**
//Synthetic comment -- @@ -253,24 +260,22 @@
* @param header the headers to include in the response
* @throws IOException if an IO error occurs
*/
    public void sendResponse(int code, ObexByteBuffer header) throws IOException {
int totalLength = 3;
        mData.reset();

if (header != null) {
            totalLength += header.getLength();
            mData.write((byte)code);
            mData.write((byte)(totalLength >> 8));
            mData.write((byte)totalLength);
            mData.write(header);
} else {
            mData.write((byte)code);
            mData.write((byte)0x00);
            mData.write((byte)totalLength);
}
        mData.read(mOutput);
mOutput.flush();
}

//Synthetic comment -- @@ -291,7 +296,6 @@
int totalLength = 3;
byte[] head = null;
int code = -1;
HeaderSet request = new HeaderSet();
HeaderSet reply = new HeaderSet();

//Synthetic comment -- @@ -305,15 +309,10 @@
totalLength = 3;
} else {
if (length > 5) {
                mData.reset();
                mData.write(mInput, length - 5);

                ObexHelper.updateHeaderSet(request, mData, null, mHeaderBuffer);

if (mListener.getConnectionId() != -1 && request.mConnectionID != null) {
mListener.setConnectionId(ObexHelper.convertToLong(request.mConnectionID));
//Synthetic comment -- @@ -386,18 +385,18 @@
}

// Compute Length of OBEX SETPATH packet
        mData.reset();
        mData.write((byte)code);
        mData.write((byte)(totalLength >> 8));
        mData.write((byte)totalLength);
if (head != null) {
            mData.write(head);
}
/*
* Write the OBEX SETPATH packet to the server. Byte 0: response code
* Byte 1&2: Connect Packet Length Byte 3 to n: headers
*/
        mData.read(mOutput);
mOutput.flush();
}

//Synthetic comment -- @@ -414,7 +413,6 @@
int code = ResponseCodes.OBEX_HTTP_OK;
int totalLength = 3;
byte[] head = null;
HeaderSet request = new HeaderSet();
HeaderSet reply = new HeaderSet();

//Synthetic comment -- @@ -426,15 +424,10 @@
totalLength = 3;
} else {
if (length > 3) {
                mData.reset();
                mData.write(mInput, length - 3);

                ObexHelper.updateHeaderSet(request, mData, null, mHeaderBuffer);
}

if (mListener.getConnectionId() != -1 && request.mConnectionID != null) {
//Synthetic comment -- @@ -485,23 +478,18 @@
}

// Compute Length of OBEX CONNECT packet
        mData.reset();
        mData.write((byte)code);
        mData.write((byte)(totalLength >> 8));
        mData.write((byte)totalLength);
if (head != null) {
            mData.write(head);
}
/*
* Write the OBEX DISCONNECT packet to the server. Byte 0: response code
* Byte 1&2: Connect Packet Length Byte 3 to n: headers
*/
        mData.read(mOutput);
mOutput.flush();
}

//Synthetic comment -- @@ -525,7 +513,6 @@
int code = -1;
HeaderSet request = new HeaderSet();
HeaderSet reply = new HeaderSet();

/*
* Read in the length of the OBEX packet, OBEX version, flags, and max
//Synthetic comment -- @@ -548,15 +535,10 @@
totalLength = 7;
} else {
if (packetLength > 7) {
                mData.reset();
                mData.write(mInput, packetLength - 7);

                ObexHelper.updateHeaderSet(request, mData, null, mHeaderBuffer);
}

if (mListener.getConnectionId() != -1 && request.mConnectionID != null) {







