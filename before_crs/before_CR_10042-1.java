/*Supress Audio recording in camcorder.

This is a temporary patch.

I will abandon this in 2 weeks time.

Submitting on behalf of Ricardo Martinez <ricardo.martinez@ti.com>

Signed-off-by: Anu Sundararajan <sanuradha@ti.com>*/
//Synthetic comment -- diff --git a/src/com/android/camera/VideoCamera.java b/src/com/android/camera/VideoCamera.java
//Synthetic comment -- index 186d827..4cbe51a 100644

//Synthetic comment -- @@ -68,7 +68,7 @@
private static final String TAG = "videocamera";

private static final boolean DEBUG = true;
    private static final boolean DEBUG_SUPPRESS_AUDIO_RECORDING = DEBUG && false;

private static final int CLEAR_SCREEN_DELAY = 4;
private static final int UPDATE_RECORD_TIME = 5;







