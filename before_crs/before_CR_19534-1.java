/*frameworks/base: Fix to release references to previous live wallpaper

The service connection to the previous live wallpaper is unbound when
a new wallpaper is effective. Although the service connection is
unbound it is not disconnected and its reference to wallpaper's
service and engine is still effective. This adds up to the total
JNI references and causes dalvik (hosting system_server) to abort.
Fix is to release the references in clearWallpaperComponentLocked.

Change-Id:Ie2029f73737bd886d97d009dc1dcfd65e7d65efe*/
//Synthetic comment -- diff --git a/services/java/com/android/server/WallpaperManagerService.java b/services/java/com/android/server/WallpaperManagerService.java
//Synthetic comment -- index 124da4e..cc59174 100644

//Synthetic comment -- @@ -586,6 +586,8 @@
mIWindowManager.removeWindowToken(mWallpaperConnection.mToken);
} catch (RemoteException e) {
}
mWallpaperConnection = null;
}
}







