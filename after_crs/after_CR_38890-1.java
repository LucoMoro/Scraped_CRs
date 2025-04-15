/*Gallery2: Fix thumb and front image out of sync

In case of changing number of images in current folder ('crop' or 'delete' action)
sometime thumbnail and front image are out of sync (outlined thumb doesnt corresponds
the current front image). Such behaviour especially observed in case a lot of
images in current folder (more then half of MediaSet.MEDIAITEM_BATCH_FETCH_COUNT=500)

As a side effect from issue above 'Loading' bar sometime is never dissappeared
after crop operation.

Such Gallery behavior can be explained by manual changing current front image
index without any notification about it. It is a bad idea to change index
of current front image (mCurrentIndex) without of triggering notification
about such kind of changes.

Change-Id:Ib4d44962e70e34e6aab07922fea5ff6adb87ab5dSigned-off-by: Anatolii Shuba <x0158321@ti.com>*/




//Synthetic comment -- diff --git a/src/com/android/gallery3d/app/PhotoDataAdapter.java b/src/com/android/gallery3d/app/PhotoDataAdapter.java
//Synthetic comment -- index 9b1c8c4..7d314c2 100644

//Synthetic comment -- @@ -388,10 +388,7 @@
public void setCurrentPhoto(Path path, int indexHint) {
if (mItemPath == path) return;
mItemPath = path;
        updateCurrentIndex(indexHint);

// We need to reload content if the path doesn't match.
MediaItem item = getCurrentMediaItem();
//Synthetic comment -- @@ -716,7 +713,7 @@
mItemPath = null;
updateCurrentItem();
} else {
                updateCurrentIndex(info.indexHint);
}

updateSlidingWindow();
//Synthetic comment -- @@ -744,8 +741,7 @@
private void updateCurrentItem() {
if (mSize == 0) return;
if (mCurrentIndex >= mSize) {
                updateCurrentIndex(mSize - 1);
mPhotoView.startSlideInAnimation(PhotoView.TRANS_SLIDE_IN_LEFT);
} else {
mPhotoView.notifyOnNewImage();







