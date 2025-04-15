/*stk: fix text length coding according to 3GPP spec

When the text string of get inkey/input command is greater than
127 bytes, the length is not correctly coded in the terminal
response. See ETSI 101.220 chapter 7.1.2.

This patch review the coding of the length according to the 3GPP
spec.

Change-Id:Icfb4fa8974fb896699f855431a9385034594084bAuthor: Guillaume Lucas <guillaume.lucas@intel.com>
Signed-off-by: Guillaume Lucas <guillaumex.lucas@intel.com>
Signed-off-by: Nizar Haouati <nizar.haouati@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 2902 13417*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/ResponseData.java b/src/java/com/android/internal/telephony/cat/ResponseData.java
//Synthetic comment -- index 814ec0d..7ce0c59 100644

//Synthetic comment -- @@ -36,13 +36,22 @@
public abstract void format(ByteArrayOutputStream buf);

public static void writeLength(ByteArrayOutputStream buf, int length) {
        // Refer ETSI 101.220 Sec 7.1.2 for info on the length encoding
        if (length < 128) {
            buf.write(length);
        } else if (length < 256) {
buf.write(0x81);
            buf.write(length & 0xff);
        } else if (length < 65536) {
            buf.write(0x82);
            buf.write((length >> 8) & 0xff);
            buf.write(length & 0xff);
        } else {
            buf.write(0x83);
            buf.write((length >> 16) & 0xff);
            buf.write((length >> 8) & 0xff);
            buf.write(length & 0xff);
}
}
}

//Synthetic comment -- @@ -114,7 +123,17 @@
// ETSI TS 102 223 8.15, should use the same format as in SMS messages
// on the network.
if (mIsUcs2) {
                    int mHeaderLen =
                         5 // Command details tag full len
                         + 4 // Device identities tag full len
                         + 3 // Result tag full len
                         + 4; // String header len.
                    // the max length of an APDU message is 0xFF
                    // if string is too long, need truncate it.
                    int mRemainLen = 0xFF - mHeaderLen;
                    if (mInData.length() * 2 > mRemainLen) {
                        mInData = mInData.substring(0, mRemainLen/2 - 1);
                    }
data = mInData.getBytes("UTF-16BE");
} else if (mIsPacked) {
int size = mInData.length();
//Synthetic comment -- @@ -139,7 +158,12 @@
}

// length - one more for data coding scheme.
        int length = data.length + 1;

        if (length > 0x7F) {
            buf.write(0x81);
        }
        buf.write(length);

// data coding scheme
if (mIsUcs2) {







