/*fix to remove invalid media item within media database

Add sanity check when loading media item from media database to
avoid invalid media item shown within gallery

Change-Id:Ic643c4f9bfe4598fd95e8080276e290de6a31465Author: Tianmi Chen <tianmi.chen@intel.com>
Signed-off-by: Tianmi Chen <tianmi.chen@intel.com>
Signed-off-by: Dan Liang <dan.liang@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 57488*/
//Synthetic comment -- diff --git a/src/com/android/gallery3d/data/LocalAlbum.java b/src/com/android/gallery3d/data/LocalAlbum.java
//Synthetic comment -- index e05aac0..1f8b5f0 100644

//Synthetic comment -- @@ -127,6 +127,11 @@
try {
while (cursor.moveToNext()) {
int id = cursor.getInt(0);  // _id must be in the first column
Path childPath = mItemPath.getChild(id);
MediaItem item = loadOrUpdateItem(childPath, cursor,
dataManager, mApplication, mIsImage);







