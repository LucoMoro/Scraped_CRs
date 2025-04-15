/*GlobalTime: Perform Zoom In and Out only after initialization

Invoking zoom on mGLView before initialization causes Null pointer
exception. The usecase is to launch global time and immediately
press key 2 or 8 (just before the globe is seen on display).

Change-Id:I10e7736527a934029364c9c83f147f94574f5488*/




//Synthetic comment -- diff --git a/samples/GlobalTime/src/com/android/globaltime/GlobalTime.java b/samples/GlobalTime/src/com/android/globaltime/GlobalTime.java
//Synthetic comment -- index d96b644..e27ee56 100644

//Synthetic comment -- @@ -898,7 +898,7 @@

// The '2' key zooms out
case KeyEvent.KEYCODE_2:
            if (!mAlphaKeySet && !mDisplayWorldFlat && mInitialized) {
mGLView.zoom(-2);
handled = true;
}
//Synthetic comment -- @@ -906,7 +906,7 @@

// The '8' key zooms in
case KeyEvent.KEYCODE_8:
            if (!mAlphaKeySet && !mDisplayWorldFlat && mInitialized) {
mGLView.zoom(2);
handled = true;
}







