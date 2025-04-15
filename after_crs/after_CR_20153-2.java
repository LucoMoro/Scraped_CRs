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
} catch (OutOfMemoryError err) {
/*
* Note: Catching Errors is in most cases considered
//Synthetic comment -- @@ -349,6 +343,16 @@
* these problems than by catching OutOfMemoryError.
*/
Log.w(TAG, err);
                        } catch (Exception ex) {
                            /*
                             * Failing to generate a thumbnail is not considered
                             * a failure that should generate error messages or
                             * stop the operation.
                             * Catch all exceptions here since there are many
                             * things that can go wrong here, e.g. unplugging or
                             * mounting the SD card during insert/update/delete.
                             */
                            Log.w(TAG, ex);
} finally {
synchronized (mCurrentThumbRequest) {
mCurrentThumbRequest.mState = MediaThumbRequest.State.DONE;
//Synthetic comment -- @@ -361,8 +365,16 @@
synchronized (mThumbRequestStack) {
d = (ThumbData)mThumbRequestStack.pop();
}
                    try {
                        makeThumbInternal(d);
                    } catch (Exception ex) {
                        /*
                         * Same thing here, failing to generate a thumbnail is
                         * not considered a failure that should generate error
                         * messages or stop the user operation.
                         */
                        Log.w(TAG, ex);
                    }
synchronized (mPendingThumbs) {
mPendingThumbs.remove(d.path);
}







