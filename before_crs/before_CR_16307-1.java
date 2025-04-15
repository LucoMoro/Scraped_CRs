/*frameworks/base: Add commands to change visibility of wallpaper

In the current architecture the wallpaper remains alive even when its not
visible. Provide commands to change visibility of wallpaper. This change
improves performance.

Change-Id:Ia9bc56e24f4b3379a2ad0ea304bb687775cc16b5*/
//Synthetic comment -- diff --git a/services/java/com/android/server/WindowManagerService.java b/services/java/com/android/server/WindowManagerService.java
//Synthetic comment -- index 68787cd..847f824 100644

//Synthetic comment -- @@ -2335,8 +2335,21 @@
curWallpaperIndex--;
WindowState wallpaper = token.windows.get(curWallpaperIndex);
try {
                        wallpaper.mClient.dispatchWallpaperCommand(action,
                                x, y, z, extras, sync);
// We only want to be synchronous with one wallpaper.
sync = false;
} catch (RemoteException e) {







