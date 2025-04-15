/*Handle OutOfMemoryError when decoding images in ThumbnailUtils

Added handling of OutOfMemoryError handling to createImageThumbnail
method in ThumbnailUtils.java. During mediascanner run it would run
out of memory when trying to decode very large images. Now it handles
this error and returns null which is handled by the media scanner.

Change-Id:Ie68722dfa1cedd3c0847bf483baa40c4827ad5a8*/
//Synthetic comment -- diff --git a/media/java/android/media/ThumbnailUtils.java b/media/java/android/media/ThumbnailUtils.java
//Synthetic comment -- index 494b4cb..db77ad5 100644

//Synthetic comment -- @@ -123,6 +123,8 @@
bitmap = BitmapFactory.decodeFileDescriptor(fd, null, options);
} catch (IOException ex) {
Log.e(TAG, "", ex);
}
}








