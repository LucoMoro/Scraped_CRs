/*Handle OutOfMemoryError when decoding images in ThumbnailUtils

Added handling of OutOfMemoryError handling to createImageThumbnail
method in ThumbnailUtils.java. During mediascanner run it would run
out of memory when trying to decode very large images. Now it handles
this error and returns null which is handled by the media scanner.

Change-Id:Ie68722dfa1cedd3c0847bf483baa40c4827ad5a8*/




//Synthetic comment -- diff --git a/media/java/android/media/ThumbnailUtils.java b/media/java/android/media/ThumbnailUtils.java
//Synthetic comment -- index 494b4cb..b276e33 100644

//Synthetic comment -- @@ -83,7 +83,7 @@
*
* @param filePath the path of image file
* @param kind could be MINI_KIND or MICRO_KIND
     * @return Bitmap, or null on failures
*
* @hide This method is only used by media framework and media provider internally.
*/
//Synthetic comment -- @@ -123,6 +123,8 @@
bitmap = BitmapFactory.decodeFileDescriptor(fd, null, options);
} catch (IOException ex) {
Log.e(TAG, "", ex);
            } catch (OutOfMemoryError oom) {
                Log.e(TAG, "Unable to decode file " + filePath + ". OutOfMemoryError.", oom);
}
}








