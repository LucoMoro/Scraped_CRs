/*Fixing the wrong link in YuvImage JavaDoc.

Change-Id:Ie6334e16424e59b75274ef265f10d26ba484316a*/




//Synthetic comment -- diff --git a/graphics/java/android/graphics/YuvImage.java b/graphics/java/android/graphics/YuvImage.java
//Synthetic comment -- index 9368da6..af3f276 100644

//Synthetic comment -- @@ -36,7 +36,7 @@
private final static int WORKING_COMPRESS_STORAGE = 4096;

/**
     * The YUV format as defined in {@link ImageFormat}.
*/
private int mFormat;

//Synthetic comment -- @@ -67,7 +67,7 @@
*
* @param yuv     The YUV data. In the case of more than one image plane, all the planes must be
*                concatenated into a single byte array.
     * @param format  The YUV data format as defined in {@link ImageFormat}.
* @param width   The width of the YuvImage.
* @param height  The height of the YuvImage.
* @param strides (Optional) Row bytes of each image plane. If yuv contains padding, the stride
//Synthetic comment -- @@ -152,7 +152,7 @@
}

/**
     * @return the YUV format as defined in {@link ImageFormat}.
*/
public int getYuvFormat() {
return mFormat;







