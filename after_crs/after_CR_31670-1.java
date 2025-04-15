/*Missing an explicit termination before return

If a device doesn't support the video snapshot function, the testcase
'testVideoSnapshotByCamera()' will not release a camera H/W resource.
It causes a cameara open-error for the next test case.

Change-Id:Ia0f52d3a288fcbee351f8524d981bfeadd109e24*/




//Synthetic comment -- diff --git a/tests/tests/hardware/src/android/hardware/cts/CameraTest.java b/tests/tests/hardware/src/android/hardware/cts/CameraTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index c6c4a44..3b29f6a

//Synthetic comment -- @@ -2840,7 +2840,10 @@
private void testVideoSnapshotByCamera(int cameraId) throws Exception {
initializeMessageLooper(cameraId);
Camera.Parameters parameters = mCamera.getParameters();
        if (!parameters.isVideoSnapshotSupported()) {
            terminateMessageLooper();
            return;
        }

SurfaceHolder holder = getActivity().getSurfaceView().getHolder();








