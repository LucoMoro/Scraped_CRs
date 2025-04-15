/*Check if physical cameras exist on the device.

Added handlings to pass testStressCamera,
testStressCameraSwitchRecorder and testStressRecordVideoAndPlayback
with no camera devices.

Change-Id:I250aad99fd29e6a30459b22c28ba1fff8afed693*/
//Synthetic comment -- diff --git a/tests/tests/mediastress/src/android/mediastress/cts/MediaRecorderStressTest.java b/tests/tests/mediastress/src/android/mediastress/cts/MediaRecorderStressTest.java
//Synthetic comment -- index fd4e3b3..e7eba87 100644

//Synthetic comment -- @@ -155,6 +155,10 @@
//Test case for stressing the camera preview.
@LargeTest
public void testStressCamera() throws Exception {
SurfaceHolder mSurfaceHolder;
mSurfaceHolder = MediaFrameworkTest.getSurfaceView().getHolder();
File stressOutFile = new File(WorkDir.getTopDir(), MEDIA_STRESS_OUTPUT);
//Synthetic comment -- @@ -237,6 +241,10 @@
//Stress test case for switching camera and video recorder preview.
@LargeTest
public void testStressCameraSwitchRecorder() throws Exception {
String filename;
SurfaceHolder mSurfaceHolder;
mSurfaceHolder = MediaFrameworkTest.getSurfaceView().getHolder();
//Synthetic comment -- @@ -315,6 +323,10 @@
//Stress test case for record a video and play right away.
@LargeTest
public void testStressRecordVideoAndPlayback() throws Exception {
String filename;
SurfaceHolder mSurfaceHolder;
mSurfaceHolder = MediaFrameworkTest.getSurfaceView().getHolder();







