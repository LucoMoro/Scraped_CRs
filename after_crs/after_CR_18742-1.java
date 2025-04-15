/*Fix the incorrect documents.

The metohd splitSize() may return null in some cases.

Change-Id:Ie0b590ab9ffa5c3d2a134f8464851ec6040d7293*/




//Synthetic comment -- diff --git a/core/java/android/hardware/Camera.java b/core/java/android/hardware/Camera.java
//Synthetic comment -- index 8687a89..53f9851 100644

//Synthetic comment -- @@ -1006,8 +1006,8 @@
/**
* Gets the supported preview sizes.
*
         * @return a list of Size object. This method will return null or
         *         a list with at least one element.
*/
public List<Size> getSupportedPreviewSizes() {
String str = get(KEY_PREVIEW_SIZE + SUPPORTED_VALUES_SUFFIX);
//Synthetic comment -- @@ -1041,9 +1041,8 @@
/**
* Gets the supported jpeg thumbnail sizes.
*
         * @return a list of Size object. This method will return null or
         *         a list with at least one element.
*/
public List<Size> getSupportedJpegThumbnailSizes() {
String str = get(KEY_JPEG_THUMBNAIL_SIZE + SUPPORTED_VALUES_SUFFIX);
//Synthetic comment -- @@ -1197,8 +1196,8 @@
/**
* Gets the supported picture sizes.
*
         * @return a list of supported picture sizes. This method will return
         *         null or a list with at least one element.
*/
public List<Size> getSupportedPictureSizes() {
String str = get(KEY_PICTURE_SIZE + SUPPORTED_VALUES_SUFFIX);







