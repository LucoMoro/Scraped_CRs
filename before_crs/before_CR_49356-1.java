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
        // As per ETSI 102.220 Sec7.1.2, if the total length is greater
        // than 0x7F, it should be coded in two bytes and the first byte
        // should be 0x81.
        if (length > 0x7F) {
buf.write(0x81);
}
        buf.write(length);
}
}

//Synthetic comment -- @@ -114,7 +123,17 @@
// ETSI TS 102 223 8.15, should use the same format as in SMS messages
// on the network.
if (mIsUcs2) {
                    // ucs2 is by definition big endian.
data = mInData.getBytes("UTF-16BE");
} else if (mIsPacked) {
int size = mInData.length();
//Synthetic comment -- @@ -139,7 +158,12 @@
}

// length - one more for data coding scheme.
        writeLength(buf, data.length + 1);

// data coding scheme
if (mIsUcs2) {







