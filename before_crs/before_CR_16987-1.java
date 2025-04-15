/*Allow two SurfaceViews inbetween the app and the wallpaper.

When two SurfaceViews are used by an application, which is
transparent and uses the wallpaper, only one of the SurfaceViews
will be visible.

The problem is that when trying to find the correct location
for the wallpaper, the window manager locates the activity
window and skips every window below it which is related.
For the first SurfaceView this will be ok, since the
SurfaceViews's attached window is the previous window.
However for the second SurfaceView this condition will fail
(the 2nd SurfaceViews attached window is not the previous
window (which is the 1st SurfaceView)).

The fix adds an extra check which detects this usecase
(the windows are related if the attached window is the same
as the attached window of the previous window).

Change-Id:I368e9dd53455dc310f48d6d8c0be68350ff2b79e*/
//Synthetic comment -- diff --git a/services/java/com/android/server/WindowManagerService.java b/services/java/com/android/server/WindowManagerService.java
//Synthetic comment -- index 68787cd..11b6310 100644

//Synthetic comment -- @@ -1501,6 +1501,7 @@
WindowState wb = (WindowState)localmWindows.get(foundI-1);
if (wb.mBaseLayer < maxLayer &&
wb.mAttachedWindow != foundW &&
(wb.mAttrs.type != TYPE_APPLICATION_STARTING ||
wb.mToken != foundW.mToken)) {
// This window is not related to the previous one in any







