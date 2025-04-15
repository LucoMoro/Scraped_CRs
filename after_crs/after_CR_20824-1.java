/*Fix display error when re-entering to gallery after deleting image files.

The problem was that deleted image files were showed again on the gallery.
Repro. step)
	1. Delete image files from gallery
	2. go back to idle
	3. re-enter to gallery

Change-Id:I2ab1cdc896b30cc27d03e9f2fcd0e63c4c5096aeSigned-off-by: Sang-Jun Park <sj2202.park@samsung.com>*/




//Synthetic comment -- diff --git a/src/com/cooliris/media/LocalDataSource.java b/src/com/cooliris/media/LocalDataSource.java
//Synthetic comment -- index 7006ba3..0f52ac2 100644

//Synthetic comment -- @@ -297,7 +297,7 @@
final String whereVideos = Video.VideoColumns.BUCKET_ID + "=" + Long.toString(set.mId);
cr.delete(uriImages, whereImages, null);
cr.delete(uriVideos, whereVideos, null);
                    CacheService.markDirty();
}
if (set != null && items != null) {
// We need to remove these items from the set.
//Synthetic comment -- @@ -313,7 +313,7 @@
}
set.updateNumExpectedItems();
set.generateTitle(true);
                    CacheService.markDirty(set.mId);
}
}
break;







