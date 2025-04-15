/*Fix 32-bit DDMS screen capture colors.

The assumption that 32 bits bpp data is little endian is wrong.
Removing the reversing bytes logic to produce correct colors.

Change-Id:I9e42918ff522d28b839ca58d1cdda268f5c139ae*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/RawImage.java b/ddms/libs/ddmlib/src/com/android/ddmlib/RawImage.java
//Synthetic comment -- index adb0cc9..a743608 100644

//Synthetic comment -- @@ -201,14 +201,7 @@
* <p/>This value is compatible with org.eclipse.swt.graphics.PaletteData
*/
private int getMask(int length, int offset) {
        int res = getMask(length) << offset;

        // if the bpp is 32 bits then we need to invert it because the buffer is in little endian
        if (bpp == 32) {
            return Integer.reverseBytes(res);
        }

        return res;
}

/**







