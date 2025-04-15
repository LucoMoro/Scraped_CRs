/*Checking hardware feature when test rear camera

To support the device which do not have rear camera

Change-Id:I81bcaa1f4fbdf0b1fb94f5f7c82e2b0e179c629e*/




//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/Camera_SizeTest.java b/tests/tests/hardware/src/android/hardware/cts/Camera_SizeTest.java
//Synthetic comment -- index 028b819..0b7b69f 100644

//Synthetic comment -- @@ -43,12 +43,18 @@
args = {int.class, int.class}
)
public void testConstructor() {
        if (Camera.getNumberOfCameras() < 1) {
            return;
        }

        Camera camera = Camera.open(0);
Parameters parameters = camera.getParameters();

checkSize(parameters, WIDTH1, HEIGHT1);
checkSize(parameters, WIDTH2, HEIGHT2);
checkSize(parameters, WIDTH3, HEIGHT3);

        camera.release();
}

private void checkSize(Parameters parameters, int width, int height) {








//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/MediaRecorderTest.java b/tests/tests/media/src/android/media/cts/MediaRecorderTest.java
//Synthetic comment -- index ead3d62..70b108e 100644

//Synthetic comment -- @@ -170,22 +170,27 @@
)
@UiThreadTest
public void testSetCamera() throws Exception {
        int nCamera = Camera.getNumberOfCameras();
        for (int cameraId = 0; cameraId < nCamera; cameraId++) {
            mCamera = Camera.open(cameraId);
            mCamera.unlock();
            mMediaRecorder.setCamera(mCamera);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
            mMediaRecorder.setVideoFrameRate(FRAME_RATE);
            mMediaRecorder.setVideoSize(VIDEO_WIDTH, VIDEO_HEIGHT);
            mMediaRecorder.setPreviewDisplay(getActivity().getSurfaceHolder().getSurface());
            mMediaRecorder.setOutputFile(OUTPUT_PATH);

            mMediaRecorder.prepare();
            mMediaRecorder.start();
            Thread.sleep(1000);
            mMediaRecorder.stop();
            assertTrue(mOutFile.exists());

            mCamera.release();
        }
}

private void checkOutputExist() {







