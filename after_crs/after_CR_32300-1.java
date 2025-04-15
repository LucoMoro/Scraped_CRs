/*ImageWallpaper : get bitmap width before calc screen offset

Inside drawFrameLocked, updateWallpaperLocked() is called after
calculae drawing offset using mBackgroundWidth/Height. Because
mBackgroundWidth/Height is modified inside updateWallpaperLocked(),
it sould be called before Mbackgroundwidth/Height is used.

Many market launcher and some phone vendor launcher call
suggestDesiredDimenstions with argument (w,h) instead aosp's
original (w*2,h) for single page wallpaper. In that case, when
launcher orientation changed, ImageWallpaper does not properly
display wallpaper image and mostly you can see Black Edge at
left.

Change-Id:I93ebc4337b01f73d3154346943afa4afd6b1988a*/




//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/ImageWallpaper.java b/packages/SystemUI/src/com/android/systemui/ImageWallpaper.java
//Synthetic comment -- index 724679f..f9debce 100644

//Synthetic comment -- @@ -282,6 +282,13 @@
updateWallpaperLocked();
}

            if (mBackground == null) {
                // If we somehow got to this point after we have last flushed
                // the wallpaper, well we really need it to draw again.  So
                // seems like we need to reload it.  Ouch.
                updateWallpaperLocked();
            }

SurfaceHolder sh = getSurfaceHolder();
final Rect frame = sh.getSurfaceFrame();
final int dw = frame.width();
//Synthetic comment -- @@ -303,13 +310,6 @@
mLastXTranslation = xPixels;
mLastYTranslation = yPixels;

if (mIsHwAccelerated) {
if (!drawWallpaperWithOpenGL(sh, availw, availh, xPixels, yPixels)) {
drawWallpaperWithCanvas(sh, availw, availh, xPixels, yPixels);







