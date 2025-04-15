/*Avoid a crash in LiveWallpaper Preview

After selected a live wallpaper from Settings for preview, then
quick change wallpaper orientation, the device crash. The problem
is due to the rapid changes of an orientation, the wallpaper
connection was trying to unbind from the service to which it was
not binding to, and result in the occurrence of an Exception.

To fix it, before unbinding, it checks whether the
wallpaperconnection is connected to service or not.

Change-Id:I9bdaa4aea269921f0ca51c1b1e1fa0710d7174e2*/
//Synthetic comment -- diff --git a/src/com/android/wallpaper/livepicker/LiveWallpaperPreview.java b/src/com/android/wallpaper/livepicker/LiveWallpaperPreview.java
//Synthetic comment -- index d4f7ef7..3751d3b 100644

//Synthetic comment -- @@ -247,7 +247,6 @@

public void disconnect() {
synchronized (this) {
                mConnected = false;
if (mEngine != null) {
try {
mEngine.destroy();
//Synthetic comment -- @@ -256,8 +255,11 @@
}
mEngine = null;
}
                unbindService(this);
                mService = null;
}
}








