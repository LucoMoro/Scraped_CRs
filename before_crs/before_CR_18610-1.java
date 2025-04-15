/*Bluetooth OBEX timeout problem.

Long time to show notification of file transfer failed after
canceling transfer via remote part.

Device A is in contact with our device, B. When device A Cancel
a transfer operation, it sends OBEX_OPCODE_DISCONNECT HeaderID
and the length of package to B. B use the length of package to
read remainder bytes from A. If the bytes B read do not meet the
received length it will block and wait for remainder bytes from A.
But when B compute the remainder bytes it forgets to subtract
the three bytes it has already read through HeaderID and the length
of the package. So the operation was blocked until the operation
timeout.

Change-Id:I0f8bf62d3119e081b5c01af9fc05fe586fd4fabc*/
//Synthetic comment -- diff --git a/obex/javax/obex/ServerOperation.java b/obex/javax/obex/ServerOperation.java
//Synthetic comment -- index 07a3a53..d1476d2 100644

//Synthetic comment -- @@ -397,11 +397,13 @@
&& (headerID != ObexHelper.OBEX_OPCODE_GET_FINAL)) {

if (length > 3) {
                    byte[] temp = new byte[length];
bytesReceived = mInput.read(temp);

                    while (bytesReceived != length) {
                        bytesReceived += mInput.read(temp, bytesReceived, length - bytesReceived);
}
}








