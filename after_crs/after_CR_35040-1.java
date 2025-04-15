/*Properly handle quick switch between Home button and Gallery app

This change fixes the following bug:

1. Playing a movie (especially Full HD) by Gallery.
2. Press the Home button.
3. Press the Gallery shortcut on Launcher.
4. Very quickly repeat #2 and #3 several times.

Result: "Sorry, this video cannot be played" alert is shown.

Change-Id:Idf9d222ca1e0bb90bfe06f6dc807dfbc71b6415bSigned-off-by: Yuriy Zabroda <x0167398@ti.com>*/




//Synthetic comment -- diff --git a/core/java/android/view/SurfaceView.java b/core/java/android/view/SurfaceView.java
//Synthetic comment -- index 6726c56e..7a76262 100644

//Synthetic comment -- @@ -519,6 +519,13 @@
}
}

                    if (!mNewSurface.isValid()) {
                        // If the surface is invalid, it makes no sense to do
                        // anything with it (e.g. mark it for redraw, etc).

                        return;
                    }

mSurface.transferFrom(mNewSurface);

if (visible) {







