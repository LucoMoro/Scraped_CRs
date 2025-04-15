/*Camcorder HUD no longer disappears when opening slider

The camcorder head up display was incorrectly detached when opening
the slider. Changed behavior to conform with the camera head up
display.

Change-Id:Ie52ab641c0702cbae5e04530a40f992ded8923bc*/
//Synthetic comment -- diff --git a/src/com/android/camera/VideoCamera.java b/src/com/android/camera/VideoCamera.java
//Synthetic comment -- index ee05f9e..f082070 100644

//Synthetic comment -- @@ -400,8 +400,8 @@
// becomes landscape.
Configuration config = getResources().getConfiguration();
if (config.orientation == Configuration.ORIENTATION_LANDSCAPE
                && !mPausing && mGLRootView == null) {
            attachHeadUpDisplay();
} else if (mGLRootView != null) {
detachHeadUpDisplay();
}







