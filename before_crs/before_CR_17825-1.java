/*MediaProvider: catch OutOfMemoryError when creating thumbnails.

When creating image thumbnails the memory will sometimes run out
resulting in a java.lang.OutOfMemoryError. With this change, this
error will be caught so that a generic thumbnail placeholder
is used instead of the one we failed to create.

The OutOfMemoryError may be caused by unreasonably large and/or
broken images. Rather than crashing the process the code now
logs the error and resumes loading the next thumbnail in queue.

Change-Id:I4ed644c7973060832e6c05b2b920f273c819e3f1*/
//Synthetic comment -- diff --git a/src/com/android/providers/media/MediaProvider.java b/src/com/android/providers/media/MediaProvider.java
//Synthetic comment -- index c13978a..7cdb62a 100644

//Synthetic comment -- @@ -324,6 +324,17 @@
// This could happen if we unplug the sd card during insert/update/delete
// See getDatabaseForUri.
Log.w(TAG, ex);
} finally {
synchronized (mCurrentThumbRequest) {
mCurrentThumbRequest.mState = MediaThumbRequest.State.DONE;







