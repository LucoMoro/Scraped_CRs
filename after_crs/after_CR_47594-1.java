/*[Subject]: Live wall paper Nexus crashed after rotating it during loading

Live wall paper Nexus crashed after rotating it during loading

Change-Id:I509355e0271f27fb8968af9be2a3fef7147ed712Author: Wei Wang <wei.wang1@borqs.com>
Signed-off-by: Bing Deng <bingx.deng@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 31252*/




//Synthetic comment -- diff --git a/src/com/android/wallpaper/livepicker/LiveWallpaperPreview.java b/src/com/android/wallpaper/livepicker/LiveWallpaperPreview.java
//Synthetic comment -- index 55e0d24..994b968 100644

//Synthetic comment -- @@ -247,7 +247,6 @@

public void disconnect() {
synchronized (this) {
if (mEngine != null) {
try {
mEngine.destroy();
//Synthetic comment -- @@ -256,7 +255,10 @@
}
mEngine = null;
}
                if (mConnected == true) {
                    unbindService(this);
                }
                mConnected = false;
mService = null;
}
}







