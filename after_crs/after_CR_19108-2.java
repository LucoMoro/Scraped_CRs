/*IMAGE:Gallery3D: Speed up the cache thumbnail generation.

[Comment]
The cache thumbnails(128*96) are generated during the first entry
to the Gallery. So there was a slowness in grid view for the first
time.
This Patch will trigger the cache thumbnails generation after the
media scanner finish its scanning of files.
So the user will the see thumbnails faster even for the first time
entry to Gallery.

This patch also change the timestamp used for the cache thumbnails
from DATE_ADDED to DATE_MODIFIED. This is needed because during
thumbnail loading it checks for the DATE_MODIFIED timestamp in
DiskCache.get() function.
This will avoid the unnecessary cache thumbnail re-generation.

Change-Id:If1ae301a7b8e7225c79aab29ec7c8efef1cacbacSigned-off-by: Solaiyappan Saravanan <saravanan.s@ti.com>*/




//Synthetic comment -- diff --git a/src/com/cooliris/cache/BootReceiver.java b/src/com/cooliris/cache/BootReceiver.java
//Synthetic comment -- index 70fbdc7..f46caf4 100644

//Synthetic comment -- @@ -35,7 +35,8 @@
final String action = intent.getAction();
Log.i(TAG, "Got intent with action " + action);
if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
            CacheService.markDirty();
            CacheService.startCache(context,true);
} else if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
;
} else if (action.equals(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)) {








//Synthetic comment -- diff --git a/src/com/cooliris/cache/CacheService.java b/src/com/cooliris/cache/CacheService.java
//Synthetic comment -- index c8a5fc0..a2c3ab1 100644

//Synthetic comment -- @@ -108,7 +108,8 @@
public static final int THUMBNAIL_DATE_MODIFIED_INDEX = 1;
public static final int THUMBNAIL_DATA_INDEX = 2;
public static final int THUMBNAIL_ORIENTATION_INDEX = 3;
    public static final String[] THUMBNAIL_PROJECTION = new String[] {
            Images.ImageColumns._ID, Images.ImageColumns.DATE_MODIFIED,
Images.ImageColumns.DATA, Images.ImageColumns.ORIENTATION };

public static final String[] SENSE_PROJECTION = new String[] { Images.ImageColumns.BUCKET_ID,







