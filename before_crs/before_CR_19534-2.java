/*frameworks/base: Fix to release references to previous live wallpaper

The service connection to the previous live wallpaper is unbound when
a new wallpaper is effective. Although the service connection is
unbound it is not disconnected and its reference to wallpaper's
service and engine is still effective. This adds up to the total
JNI references and causes dalvik (hosting system_server) to abort.
Fix is to release the references in clearWallpaperComponentLocked.

Change-Id:Idd2bab83a56d2e6c6dd7ab9be08d5e14887aa384*/
//Synthetic comment -- diff --git a/services/java/com/android/server/WallpaperManagerService.java b/services/java/com/android/server/WallpaperManagerService.java
//Synthetic comment -- index 859c85c..997e750 100644

//Synthetic comment -- @@ -587,6 +587,8 @@
mIWindowManager.removeWindowToken(mWallpaperConnection.mToken);
} catch (RemoteException e) {
}
mWallpaperConnection = null;
}
}







