/*Disable preview after capture

If the preview is allowed to run continuously a
preview frame can be delivered using the previous parameters.

Change-Id:I4c1ad4c29f416b3c9d7abca1c3fcbb04463765abSigned-off-by: Lucifer Liu <yliu@ingenic.cn>
Signed-off-by: Chris Dearman <chris@mips.com>*/




//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/CameraTest.java b/tests/tests/hardware/src/android/hardware/cts/CameraTest.java
//Synthetic comment -- index 3b29f6a..34dc7ab 100755

//Synthetic comment -- @@ -1679,6 +1679,7 @@
mCamera.startPreview();
waitForPreviewDone();
assertEquals(PREVIEW_CALLBACK_RECEIVED, mPreviewCallbackResult);
		mCamera.stopPreview();

// Check if the picture size is the same as requested.
mCamera.takePicture(mShutterCallback, mRawPictureCallback, mJpegPictureCallback);







