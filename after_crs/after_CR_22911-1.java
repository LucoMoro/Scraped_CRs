/*Fixed the "Select All" issue in Gallery3D

Two issues:
    1) "Select All" doesn't select all videos and photos in one album
    2) "Select All" deselects some items previously selected

The problem is caused by the same IDs in the image table and video table
in database. The ID generation for each item is fine. But we cannot use
ID as the only criteria for the uniqueness of items. If one image has the
id "100" in the image table and one video has the id "100" in the video
table, and both of them happen to be in the same bucket, then only one
will be selected when we do "Select All".

Change-Id:I6431827779a43dfaaf57338627ae3572f1b7a58bSigned-off-by: madan ankapura <mankapur@sta.samsung.com>*/




//Synthetic comment -- diff --git a/src/com/cooliris/media/MediaBucketList.java b/src/com/cooliris/media/MediaBucketList.java
//Synthetic comment -- index a1a30e9..9301221 100644

//Synthetic comment -- @@ -130,7 +130,8 @@
// Search for the bucket for this media set
for (int i = 0; i < numSelectedBuckets; ++i) {
final MediaBucket bucketCompare = selectedBuckets.get(i);
            if (bucketCompare != null && bucketCompare.mediaSet != null
                    && mediaSetToAdd != null && bucketCompare.mediaSet == mediaSetToAdd) {
// We found the MediaSet.
if (!hasExpandedMediaSet) {
// Remove this bucket from the list since this bucket was
//Synthetic comment -- @@ -171,7 +172,7 @@
boolean foundIndex = false;
for (int j = 0; j < numPresentItems; ++j) {
final MediaItem selectedItem = selectedItems.get(j);
                            if (selectedItem != null && item != null && selectedItem == item) {
// This index was already present, we need to
// remove it.
foundIndex = true;







