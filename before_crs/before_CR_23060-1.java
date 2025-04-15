/*OBEX: Fix PrivateOutputStream small write problem

When data less than max packet size in length is sent into the write
method the data will only be added to the internal buffer. If several
calls to write is performed by the application continueOperation will
not be called at all. The solution to the problem is to always check
the internal buffer size and to call continueOperation every time
maxPacketSize bytes is in the internal buffer.

Change-Id:I5ebfa3c26db2c1aefe1a115d7782d8ceaa760937*/
//Synthetic comment -- diff --git a/obex/javax/obex/PrivateOutputStream.java b/obex/javax/obex/PrivateOutputStream.java
//Synthetic comment -- index ca420af..713f4ae 100644

//Synthetic comment -- @@ -107,18 +107,15 @@

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








