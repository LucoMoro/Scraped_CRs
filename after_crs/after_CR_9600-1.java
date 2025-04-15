/*Revert "Disabling generation of thumbnail after video capture"

This reverts commit 3e79aa11e13d595847a252457f11029b655e726f.*/




//Synthetic comment -- diff --git a/src/com/android/camera/VideoCamera.java b/src/com/android/camera/VideoCamera.java
//Synthetic comment -- index 4e581aa..4828b71 100644

//Synthetic comment -- @@ -840,7 +840,7 @@
Log.v(TAG, "stopVideoRecordingAndDisplayDialog");
if (mMediaRecorderRecording) {
stopVideoRecording();
            acquireAndShowVideoFrame();
showPostRecordingAlert();
}
}







