/*Unable to push files to Windows 7

Windows 7 says it can handle the maximum OBEX packet size. However
if the maximum packet size is used the transfer will fail. This
should be fixed by Microsoft but until they do the only way to solve
this is to reduce the maximum packet size for the OBEX client.

Change-Id:I51975eec972395ae3f1f21cca236b223d0812ef3*/




//Synthetic comment -- diff --git a/obex/javax/obex/ClientSession.java b/obex/javax/obex/ClientSession.java
//Synthetic comment -- index 0935383..27d8976 100644

//Synthetic comment -- @@ -449,8 +449,8 @@
maxPacketSize = (mInput.read() << 8) + mInput.read();

//check with local max size
                if (maxPacketSize > ObexHelper.MAX_CLIENT_PACKET_SIZE) {
                    maxPacketSize = ObexHelper.MAX_CLIENT_PACKET_SIZE;
}

if (length > 7) {








//Synthetic comment -- diff --git a/obex/javax/obex/ObexHelper.java b/obex/javax/obex/ObexHelper.java
//Synthetic comment -- index 1b66662..7852fe9 100644

//Synthetic comment -- @@ -70,6 +70,12 @@
*/
public static final int MAX_PACKET_SIZE_INT = 0xFFFE;

    /**
     * Temporary workaround to be able to push files to Windows 7.
     * TODO: Should be removed as soon as Microsoft updates their driver.
     */
    public static final int MAX_CLIENT_PACKET_SIZE = 0xFC00;

public static final int OBEX_OPCODE_CONNECT = 0x80;

public static final int OBEX_OPCODE_DISCONNECT = 0x81;







