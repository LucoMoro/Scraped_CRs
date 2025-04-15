/*Fixed parallell thumbnail creation failure

The thumbnail creation sometimes failed due to the garbage collector
freeing up a FileInputStream that didn't have any reference but
was used indirectly by a FileDescriptor. This was triggered when
a lot of thumbnails were created in parallell.

Change-Id:I80590700f034d05374539c17ac3902165e0093b6*/
//Synthetic comment -- diff --git a/media/java/android/media/ThumbnailUtils.java b/media/java/android/media/ThumbnailUtils.java
//Synthetic comment -- index 3d85e31..631435f 100644

//Synthetic comment -- @@ -104,8 +104,10 @@
}

if (bitmap == null) {
try {
                FileDescriptor fd = new FileInputStream(filePath).getFD();
BitmapFactory.Options options = new BitmapFactory.Options();
options.inSampleSize = 1;
options.inJustDecodeBounds = true;
//Synthetic comment -- @@ -123,6 +125,14 @@
bitmap = BitmapFactory.decodeFileDescriptor(fd, null, options);
} catch (IOException ex) {
Log.e(TAG, "", ex);
}
}








