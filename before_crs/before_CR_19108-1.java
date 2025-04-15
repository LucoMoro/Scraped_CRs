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
This will avoid the unnecessary cahce thumbnail re-generation.

Change-Id:I9b07d00103be1759910a6246d46cfcd5fcf7b982Signed-off-by: Solaiyappan Saravanan <saravanan.s@ti.com>*/
//Synthetic comment -- diff --git a/src/com/cooliris/cache/BootReceiver.java b/src/com/cooliris/cache/BootReceiver.java
//Synthetic comment -- index 70fbdc7..fd6b412 100644

//Synthetic comment -- @@ -35,6 +35,8 @@
final String action = intent.getAction();
Log.i(TAG, "Got intent with action " + action);
if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
;
} else if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
;








//Synthetic comment -- diff --git a/src/com/cooliris/cache/CacheService.java b/src/com/cooliris/cache/CacheService.java
//Synthetic comment -- index c8a5fc0..a2c3ab1 100644

//Synthetic comment -- @@ -108,7 +108,8 @@
public static final int THUMBNAIL_DATE_MODIFIED_INDEX = 1;
public static final int THUMBNAIL_DATA_INDEX = 2;
public static final int THUMBNAIL_ORIENTATION_INDEX = 3;
    public static final String[] THUMBNAIL_PROJECTION = new String[] { Images.ImageColumns._ID, Images.ImageColumns.DATE_ADDED,
Images.ImageColumns.DATA, Images.ImageColumns.ORIENTATION };

public static final String[] SENSE_PROJECTION = new String[] { Images.ImageColumns.BUCKET_ID,







