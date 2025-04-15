/*Fix the crash when choosing pick your video in effects recording.

In pick your video case, mEffectType is not EFFECTS_NONE and
mEffectsRecorder is null in onPause.

bug:6826528

Change-Id:Id8c933555b1b8a37c541deb04746468efe1787c0*/
//Synthetic comment -- diff --git a/src/com/android/camera/VideoCamera.java b/src/com/android/camera/VideoCamera.java
//Synthetic comment -- index dc03217..ae5176b 100755

//Synthetic comment -- @@ -851,6 +851,7 @@
stopPreview();
if (effectsActive() && mEffectsRecorder != null) {
mEffectsRecorder.release();
}
}

//Synthetic comment -- @@ -884,13 +885,15 @@
// Closing the effects out. Will shut down the effects graph.
private void closeEffects() {
Log.v(TAG, "Closing effects");
if (mEffectsRecorder == null) {
Log.d(TAG, "Effects are already closed. Nothing to do");
}
// This call can handle the case where the camera is already released
// after the recording has been stopped.
mEffectsRecorder.release();
        mEffectType = EffectsRecorder.EFFECT_NONE;
}

// By default, we want to close the effects as well with the camera.
//Synthetic comment -- @@ -920,10 +923,8 @@
// Disconnect the camera from effects so that camera is ready to
// be released to the outside world.
mEffectsRecorder.disconnectCamera();
            if (closeEffectsAlso) {
                closeEffects();
            }
}
mCameraDevice.setZoomChangeListener(null);
mCameraDevice.setErrorCallback(null);
CameraHolder.instance().release();
//Synthetic comment -- @@ -1287,8 +1288,8 @@
cleanupEmptyFile();
mEffectsRecorder.release();
mEffectsRecorder = null;
            mEffectType = EffectsRecorder.EFFECT_NONE;
}
mVideoFilename = null;
}

//Synthetic comment -- @@ -2148,6 +2149,7 @@
stopPreview();
} else {
mEffectsRecorder.release();
}
resizeForPreviewAspectRatio();
startPreview(); // Parameters will be set in startPreview().







