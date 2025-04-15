/*Fixed parallell thumbnail creation failure

The thumbnail creation sometimes failed due to the garbage collector
freeing up a FileInputStream that didn't have any reference but
was used indirectly by a FileDescriptor. This was triggered when
a lot of thumbnails were created in parallell.

Change-Id:I80590700f034d05374539c17ac3902165e0093b6*/




//Synthetic comment -- diff --git a/media/java/android/media/ThumbnailUtils.java b/media/java/android/media/ThumbnailUtils.java
//Synthetic comment -- index b276e33..9349211 100644

//Synthetic comment -- @@ -104,8 +104,10 @@
}

if (bitmap == null) {
            FileInputStream is = null;
try {
                is = new FileInputStream(filePath);
                FileDescriptor fd = is.getFD();
BitmapFactory.Options options = new BitmapFactory.Options();
options.inSampleSize = 1;
options.inJustDecodeBounds = true;
//Synthetic comment -- @@ -125,6 +127,14 @@
Log.e(TAG, "", ex);
} catch (OutOfMemoryError oom) {
Log.e(TAG, "Unable to decode file " + filePath + ". OutOfMemoryError.", oom);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        Log.e(TAG, "Failed to close FileInputStream");
                    }
                }
}
}








