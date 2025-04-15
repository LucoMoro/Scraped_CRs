/*[Download]The re-downloaded picture can’t be stored in Social Gallery.

The re-downloaded picture can’t be showed in Gallery, so add the attribute of COLUMN_MEDIA_SCANNED
into download parameter. When download is successful, the image will be scanned by media library.

Change-Id:I9fe4f6f2a1c7fcb5e0928f9ba09c9f9f84be0837Author: Yuhan Xu <yuhanx.xu@intel.com>
Signed-off-by: Yuhan Xu <yuhanx.xu@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 41211*/




//Synthetic comment -- diff --git a/core/java/android/app/DownloadManager.java b/core/java/android/app/DownloadManager.java
//Synthetic comment -- index 6cf4dd0..4eab97f 100644

//Synthetic comment -- @@ -1085,6 +1085,7 @@
values.put(Downloads.Impl.COLUMN_TOTAL_BYTES, -1);
values.putNull(Downloads.Impl._DATA);
values.put(Downloads.Impl.COLUMN_STATUS, Downloads.Impl.STATUS_PENDING);
        values.put(Downloads.Impl.COLUMN_MEDIA_SCANNED, 0);
mResolver.update(mBaseUri, values, getWhereClauseForIds(ids), getWhereArgsForIds(ids));
}








