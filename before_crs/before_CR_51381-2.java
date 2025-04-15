/*Fall back to software rendering of the wallpaper in case OpenGL rendering fails

This fixes an issue where OpenGL initialization succeeds but buffer allocation fails because the requested wallpaper size
is too large (or otherwise unsupported) by the graphics hardware. This fixes an issue where SystemUI crashes constantly
on the PandaBoard when connected to a full HD display. Tested only on PandaBoard, no access to alternative hardware.
Signed-off-by: Wim Vander Schelden <wim.vander.schelden@philips.com>

Change-Id:I8d2e1ae9fd9772977c4e365f23f2f58bbca3787c*/
//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/ImageWallpaper.java b/packages/SystemUI/src/com/android/systemui/ImageWallpaper.java
//Synthetic comment -- index 9da883a..6cfa44f 100644

//Synthetic comment -- @@ -378,7 +378,9 @@
if (DEBUG) {
Log.d(TAG, "Redrawing wallpaper");
}
            if (mIsHwAccelerated) {
if (!drawWallpaperWithOpenGL(sh, availw, availh, xPixels, yPixels)) {
drawWallpaperWithCanvas(sh, availw, availh, xPixels, yPixels);
}
//Synthetic comment -- @@ -645,8 +647,8 @@

if (mEglSurface == null || mEglSurface == EGL_NO_SURFACE) {
int error = mEgl.eglGetError();
                if (error == EGL_BAD_NATIVE_WINDOW) {
                    Log.e(GL_LOG_TAG, "createWindowSurface returned EGL_BAD_NATIVE_WINDOW.");
return false;
}
throw new RuntimeException("createWindowSurface failed " +







