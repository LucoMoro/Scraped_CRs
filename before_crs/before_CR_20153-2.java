/*Catch exception when making thumbnails

We have seen a number of differnt exceptoins thrown while
creating thumbnails and plugging out the SD card or mounting
the USB disk, e.g. UnsupportedOperationException,
FileNotFoundException, SQLiteMisuseException.
Rather than making an uber long catch lists it makes sense
to just catch Exception in this case.

Change-Id:I3d83954d2ebe14be162cb78d3207fe70ff8c3d9c*/
//Synthetic comment -- diff --git a/src/com/android/providers/media/MediaProvider.java b/src/com/android/providers/media/MediaProvider.java
//Synthetic comment -- index 7a72e23..e1b9751 100644

//Synthetic comment -- @@ -332,12 +332,6 @@
Log.w(TAG, "original file hasn't been stored yet: " + mCurrentThumbRequest.mPath);
}
}
                        } catch (IOException ex) {
                            Log.w(TAG, ex);
                        } catch (UnsupportedOperationException ex) {
                            // This could happen if we unplug the sd card during insert/update/delete
                            // See getDatabaseForUri.
                            Log.w(TAG, ex);
} catch (OutOfMemoryError err) {
/*
* Note: Catching Errors is in most cases considered
//Synthetic comment -- @@ -349,6 +343,16 @@
* these problems than by catching OutOfMemoryError.
*/
Log.w(TAG, err);
} finally {
synchronized (mCurrentThumbRequest) {
mCurrentThumbRequest.mState = MediaThumbRequest.State.DONE;
//Synthetic comment -- @@ -361,8 +365,16 @@
synchronized (mThumbRequestStack) {
d = (ThumbData)mThumbRequestStack.pop();
}

                    makeThumbInternal(d);
synchronized (mPendingThumbs) {
mPendingThumbs.remove(d.path);
}







