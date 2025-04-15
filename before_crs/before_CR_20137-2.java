/*Fixing a couple of crashes when mContentUri is null.

There are some corner cases in the gallery where it is
trying to display new images that has not been fully
prepared yet.

Change-Id:I47e87faeee3edac6afe0ead46f4773e01e52f59e*/
//Synthetic comment -- diff --git a/src/com/cooliris/media/HudLayer.java b/src/com/cooliris/media/HudLayer.java
//Synthetic comment -- index a449603..4b09b27 100644

//Synthetic comment -- @@ -324,7 +324,7 @@
public void run() {
ArrayList<MediaBucket> buckets = mGridLayer.getSelectedBuckets();
MediaItem item = MediaBucketList.getFirstItemSelection(buckets);
                            if (item == null) {
return;
}
mGridLayer.deselectAll();
//Synthetic comment -- @@ -345,7 +345,7 @@
public void run() {
ArrayList<MediaBucket> buckets = mGridLayer.getSelectedBuckets();
MediaItem item = MediaBucketList.getFirstItemSelection(buckets);
                            if (item == null) {
return;
}
mGridLayer.deselectAll();
//Synthetic comment -- @@ -368,7 +368,7 @@
public void run() {
ArrayList<MediaBucket> buckets = mGridLayer.getSelectedBuckets();
MediaItem item = MediaBucketList.getFirstItemSelection(buckets);
                            if (item == null) {
return;
}
mGridLayer.deselectAll();







