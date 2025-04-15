/*StartPreview only after setting fps range paramters

Starting the preview before setting fps range causes
first supported fps range to be set while preview is
running in a different(default) fps range. This can
cause teh fps range for the first supported fps range
to be inaccurately measured.

Signed-off-by: Akwasi Boateng <akwasi.boateng@ti.com>

Change-Id:Ica6011f04dde0a5c7d801f94595916b004464d24*/
//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/CameraTest.java b/tests/tests/hardware/src/android/hardware/cts/CameraTest.java
//Synthetic comment -- index 2dd09b6..7650560 100644

//Synthetic comment -- @@ -1490,7 +1490,6 @@
private void testPreviewFpsRangeByCamera(int cameraId) throws Exception {
initializeMessageLooper(cameraId);
mCamera.setPreviewDisplay(getActivity().getSurfaceView().getHolder());
        mCamera.startPreview();

// Test if the parameters exists and minimum fps <= maximum fps.
int[] defaultFps = new int[2];







