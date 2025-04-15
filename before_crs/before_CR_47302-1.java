/*Avoid multiple extraction of thumbnails

When inserting a new memory card containing images, or taking a camera
picture, several requests for a thumbnail for the same picture are made.
At the time the requests are made, no thumbnail exists, so each of the
requests result in the full image being decoded and encoded to a
thumbnail, an operation taking about 100ms per thumbnail. Fixing this
cuts times for image thumbnails in half. Also improves Camera
shot-to-shot times.

When a thumbnail request is completed, scan the queue for identical
requests, and update those requests to avoid having to decode the same
image several times.

Change-Id:I53d720779925de882b0e2f080b0b87ec318e08a9*/
//Synthetic comment -- diff --git a/src/com/android/providers/media/MediaProvider.java b/src/com/android/providers/media/MediaProvider.java
//Synthetic comment -- index c73871d..036e600 100644

//Synthetic comment -- @@ -596,6 +596,17 @@
File origFile = new File(mCurrentThumbRequest.mPath);
if (origFile.exists() && origFile.length() > 0) {
mCurrentThumbRequest.execute();
} else {
// original file hasn't been stored yet
synchronized (mMediaThumbQueue) {








//Synthetic comment -- diff --git a/src/com/android/providers/media/MediaThumbRequest.java b/src/com/android/providers/media/MediaThumbRequest.java
//Synthetic comment -- index 1f30a3e..7695312 100644

//Synthetic comment -- @@ -224,7 +224,12 @@
ContentValues values = new ContentValues();
// both video/images table use the same column name "mini_thumb_magic"
values.put(ImageColumns.MINI_THUMB_MAGIC, magic);
                mCr.update(mUri, values, null, null);
}
} else {
Log.w(TAG, "can't create bitmap for thumbnail.");







