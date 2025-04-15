/*Catch the NameNotFoundException and return to avoid null pointer when updating package in AMS.

Change-Id:I26cf200cc84639e1261085b6905a567320cbb0a9Author: Bing Deng <bingx.deng@intel.com>
Signed-off-by: Bing Deng <bingx.deng@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 63612*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index db64a9a..5567117 100644

//Synthetic comment -- @@ -2085,6 +2085,7 @@
}
} catch (PackageManager.NameNotFoundException e) {
Slog.w(TAG, "Unable to retrieve gids", e);
}

/*







