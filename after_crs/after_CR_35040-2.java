/*Properly handle quick switch between Home button and Gallery app

This change fixes the following bug:

1. Playing a movie (especially Full HD) by Gallery.
2. Press the Home button.
3. Press the Gallery shortcut on Launcher.
4. Very quickly repeat #2 and #3 several times.

Result: "Sorry, this video cannot be played" alert is shown.

Change-Id:Idf9d222ca1e0bb90bfe06f6dc807dfbc71b6415bSigned-off-by: Yuriy Zabroda <yuriy.zabroda@ti.com>*/




//Synthetic comment -- diff --git a/core/java/android/view/SurfaceView.java b/core/java/android/view/SurfaceView.java
//Synthetic comment -- index 6726c56e..129471d 100644

//Synthetic comment -- @@ -530,7 +530,9 @@
callbacks = getSurfaceCallbacks();
}
for (SurfaceHolder.Callback c : callbacks) {
                                if (mSurface.isValid()) {
                                    c.surfaceCreated(mSurfaceHolder);
                                }
}
}
if (creating || formatChanged || sizeChanged
//Synthetic comment -- @@ -541,7 +543,9 @@
callbacks = getSurfaceCallbacks();
}
for (SurfaceHolder.Callback c : callbacks) {
                                if (mSurface.isValid()) {
                                    c.surfaceChanged(mSurfaceHolder, mFormat, myWidth, myHeight);
                                }
}
}
if (redrawNeeded) {
//Synthetic comment -- @@ -551,8 +555,10 @@
}
for (SurfaceHolder.Callback c : callbacks) {
if (c instanceof SurfaceHolder.Callback2) {
                                    if (mSurface.isValid()) {
                                        ((SurfaceHolder.Callback2)c).surfaceRedrawNeeded(
                                                mSurfaceHolder);
                                    }
}
}
}







