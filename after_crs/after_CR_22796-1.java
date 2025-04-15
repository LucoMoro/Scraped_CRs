/*CTS: Set camera default framerate to video recorder for android.hardware.cts.CameraTest#testLockUnlock

- Video recorder is started without setting framerate first. Thus, hardcoded default framerete value is set in StagefrightRecorder, and then StagefrightRecorder trying to configure Camera hardware with this hardcoded value (20 fps). But there isn't guarantee, that Camera hardware can support 20 fps. If not, CTS will fail.

- As a solution, default Camera hardware framerate can be used to configure Video recorder.*/




//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/CameraTest.java b/tests/tests/hardware/src/android/hardware/cts/CameraTest.java
//Synthetic comment -- index 60b8459..5df08f1 100644

//Synthetic comment -- @@ -878,12 +878,13 @@
SurfaceHolder surfaceHolder;
surfaceHolder = getActivity().getSurfaceView().getHolder();
Size size = parameters.getPreviewSize();
        int frame_rate = parameters.getPreviewFrameRate();
mCamera.setParameters(parameters);
mCamera.setPreviewDisplay(surfaceHolder);
mCamera.startPreview();
mCamera.lock();  // Locking again from the same process has no effect.
try {
            recordVideo(size, frame_rate, surfaceHolder);
fail("Recording should not succeed because camera is locked.");
} catch (Exception e) {
// expected
//Synthetic comment -- @@ -898,7 +899,7 @@
}

try {
            recordVideo(size, frame_rate, surfaceHolder);
} catch (Exception e) {
fail("Should not throw exception");
}
//Synthetic comment -- @@ -907,7 +908,7 @@
terminateMessageLooper();
}

    private void recordVideo(Size size, int framerate, SurfaceHolder surfaceHolder) throws Exception {
MediaRecorder recorder = new MediaRecorder();
try {
recorder.setCamera(mCamera);
//Synthetic comment -- @@ -915,6 +916,7 @@
recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
recorder.setOutputFile("/dev/null");
recorder.setVideoSize(size.width, size.height);
            recorder.setVideoFrameRate(framerate);
recorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
recorder.setPreviewDisplay(surfaceHolder.getSurface());
recorder.prepare();







