/*Disable Thumbnail creation after video capture

Signed-off-by: Anu Sundararajan <sanuradha@ti.com>*/




//Synthetic comment -- diff --git a/src/com/android/camera/VideoCamera.java b/src/com/android/camera/VideoCamera.java
//Synthetic comment -- index b25a5e2..6446c81 100644

//Synthetic comment -- @@ -1003,7 +1003,7 @@

private void stopVideoRecordingAndGetThumbnail() {
stopVideoRecording();
        //acquireVideoThumb();
}

private void stopVideoRecordingAndShowAlert() {







