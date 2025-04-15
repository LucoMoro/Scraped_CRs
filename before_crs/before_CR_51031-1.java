/*Catch OutOfMemoryError for getBitmap.

Change-Id:I85bf682cc98de0f075b4d8d6ec621d56ae559adbSigned-off-by: Bin Li <libin@marvell.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/util/ThumbnailManager.java b/src/com/android/mms/util/ThumbnailManager.java
//Synthetic comment -- index 3834168..681ba46 100644

//Synthetic comment -- @@ -252,6 +252,8 @@
bitmap = getBitmap(mIsVideo);
} catch (IllegalArgumentException e) {
Log.e(TAG, "Couldn't load bitmap for " + mUri, e);
}
final Bitmap resultBitmap = bitmap;








