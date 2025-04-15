/*CTS: Fixes for android.hardware.cts.CameraTest#testPreviewCallback

- setPreviewDisplay() was omitted during the individual
  preview resolution tests. The camera documentation states
  that a call to either setPreviewDisplay() or setPreviewTexture()
  is necessary in order to start the preview. Apart from
  that setPreviewDisplay() is mandatory on platforms, which utilize
  an overlay for camera preview.

Change-Id:Ifd2ce8d787a8c1d6790701b9d04f13f4158227d0*/




//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/CameraTest.java b/tests/tests/hardware/src/android/hardware/cts/CameraTest.java
//Synthetic comment -- index 45bc87e..ef8490d 100644

//Synthetic comment -- @@ -405,10 +405,8 @@
parameters.setPreviewSize(size.width, size.height);
mCamera.setParameters(parameters);
assertEquals(size, mCamera.getParameters().getPreviewSize());
            checkPreviewCallback();
assertTrue(mPreviewCallbackResult);
try {
// Wait for a while to throw away the remaining preview frames.
Thread.sleep(1000);







