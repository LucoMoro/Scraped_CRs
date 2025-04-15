/*Fix wrong a static variable for output path name.

Fix issuehttp://code.google.com/p/android/issues/detail?id=25537Signed-off-by: fujita <fujita@brilliantservice.co.jp>*/
//Synthetic comment -- diff --git a/tests/tests/mediastress/src/android/mediastress/cts/MediaRecorderStressTest.java b/tests/tests/mediastress/src/android/mediastress/cts/MediaRecorderStressTest.java
//Synthetic comment -- index fd4e3b3..4897476 100644

//Synthetic comment -- @@ -43,7 +43,7 @@
private static final int NUMBER_OF_SWTICHING_LOOPS_BW_CAMERA_AND_RECORDER = 50;
private static final long WAIT_TIME_CAMERA_TEST = 3000;  // in ms
private static final long WAIT_TIME_RECORDER_TEST = 5000;  // in ms
    private static final String OUTPUT_FILE = WorkDir.getTopDirString() + "temp";
private static final String OUTPUT_FILE_EXT = ".3gp";
private static final String MEDIA_STRESS_OUTPUT ="mediaStressOutput.txt";
private final CameraErrorCallback mCameraErrorCallback = new CameraErrorCallback();







