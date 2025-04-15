/*Disable preview after capture

If the preview is allowed to run continuously a
preview frame can be delivered using the previous parameters.

Signed-off-by: Lucifer Liu <yliu@ingenic.cn>
Signed-off-by: Chris Dearman <chris@mips.com>*/
//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/CameraTest.java b/tests/tests/hardware/src/android/hardware/cts/CameraTest.java
//Synthetic comment -- index c6c4a44..3abba4a 100644

//Synthetic comment -- @@ -1679,6 +1679,7 @@
mCamera.startPreview();
waitForPreviewDone();
assertEquals(PREVIEW_CALLBACK_RECEIVED, mPreviewCallbackResult);

// Check if the picture size is the same as requested.
mCamera.takePicture(mShutterCallback, mRawPictureCallback, mJpegPictureCallback);







