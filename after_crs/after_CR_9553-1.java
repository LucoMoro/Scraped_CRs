/*Disabling generation of thumbnail after video capture

This is a temporary fix. Please do not merge this patch.

Signed-off-by: Anu Sundararajan <sanurdha@ti.com>*/




//Synthetic comment -- diff --git a/src/com/android/camera/VideoCamera.java b/src/com/android/camera/VideoCamera.java
//Synthetic comment -- index 4828b71..4e581aa 100644

//Synthetic comment -- @@ -840,7 +840,7 @@
Log.v(TAG, "stopVideoRecordingAndDisplayDialog");
if (mMediaRecorderRecording) {
stopVideoRecording();
            //acquireAndShowVideoFrame();
showPostRecordingAlert();
}
}







