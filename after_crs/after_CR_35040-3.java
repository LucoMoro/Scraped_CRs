/*Properly handle quick switch between Home button and Gallery app

This change fixes the following bug:

1. Playing a movie (especially Full HD) by Gallery.
2. Press the Home button.
3. Press the Gallery shortcut on Launcher.
4. Very quickly repeat #2 and #3 several times.

Result: "Sorry, this video cannot be played" alert is shown.

Change-Id:Idf9d222ca1e0bb90bfe06f6dc807dfbc71b6415bSigned-off-by: Yuriy Zabroda <yuriy.zabroda@ti.com>*/




//Synthetic comment -- diff --git a/core/java/android/view/SurfaceView.java b/core/java/android/view/SurfaceView.java
//Synthetic comment -- index fd302dc..ed4c75c 100644

//Synthetic comment -- @@ -531,7 +531,7 @@

mSurface.transferFrom(mNewSurface);

                    if (visible && mSurface.isValid()) {
if (!mSurfaceCreated && (surfaceChanged || visibleChanged)) {
mSurfaceCreated = true;
mIsCreating = true;







