/*Check and fail early if requested wallpaper size exceeds maximum texture size.

This fixes an issue where OpenGL initialization succeeds but buffer allocation fails because the requested wallpaper size
is too large (or otherwise unsupported) by the graphics hardware. This fixes an issue where SystemUI crashes constantly
on the PandaBoard when connected to a full HD display. Tested only on PandaBoard, no access to alternative hardware.
Signed-off-by: Wim Vander Schelden <wim.vander.schelden@philips.com>

Change-Id:I8d2e1ae9fd9772977c4e365f23f2f58bbca3787c*/




//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/ImageWallpaper.java b/packages/SystemUI/src/com/android/systemui/ImageWallpaper.java
//Synthetic comment -- index 9da883a..8d0fe755 100644

//Synthetic comment -- @@ -378,6 +378,7 @@
if (DEBUG) {
Log.d(TAG, "Redrawing wallpaper");
}

if (mIsHwAccelerated) {
if (!drawWallpaperWithOpenGL(sh, availw, availh, xPixels, yPixels)) {
drawWallpaperWithCanvas(sh, availw, availh, xPixels, yPixels);
//Synthetic comment -- @@ -640,13 +641,26 @@
}

mEglContext = createContext(mEgl, mEglDisplay, mEglConfig);

            int[] maxSize = new int[1];
            Rect frame = surfaceHolder.getSurfaceFrame();
            glGetIntegerv(GL_MAX_TEXTURE_SIZE, maxSize, 0);
            if(frame.width() > maxSize[0] || frame.height() > maxSize[0]) {
                mEgl.eglDestroyContext(mEglDisplay, mEglContext);
                mEgl.eglTerminate(mEglDisplay);
                Log.e(GL_LOG_TAG, "requested  texture size " +
                    frame.width() + "x" + frame.height() + " exceeds the support maximum of " +
                    maxSize[0] + "x" + maxSize[0]);
                return false;
            }

mEglSurface = mEgl.eglCreateWindowSurface(mEglDisplay, mEglConfig, surfaceHolder, null);

if (mEglSurface == null || mEglSurface == EGL_NO_SURFACE) {
int error = mEgl.eglGetError();
                if (error == EGL_BAD_NATIVE_WINDOW || error == EGL_BAD_ALLOC) {
                    Log.e(GL_LOG_TAG, "createWindowSurface returned " +
                                         GLUtils.getEGLErrorString(error) + ".");
return false;
}
throw new RuntimeException("createWindowSurface failed " +







