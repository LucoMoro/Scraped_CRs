/*"CameraStartUp.java" package name fix

Change-Id:I7ad839ce7881335b166e59d35c4d3316b5adbb3dSigned-off-by: Emilian Peev <epeev@ti.com>*/




//Synthetic comment -- diff --git a/tests/src/com/android/camera/stress/CameraStartUp.java b/tests/src/com/android/camera/stress/CameraStartUp.java
//Synthetic comment -- index 8aec0a9..b7b0b52 100644

//Synthetic comment -- @@ -22,7 +22,7 @@
private String TAG = "CameraStartUp";
private static final String CAMERA_TEST_OUTPUT_FILE =
Environment.getExternalStorageDirectory().toString() + "/mediaStressOut.txt";
    private static final String CAMERA_PACKAGE_NAME = "com.android.camera";
private static final String CAMERA_ACTIVITY_NAME = "com.android.camera.Camera";
private static final String VIDEORECORDER_ACTIVITY_NAME = "com.android.camera.VideoCamera";
private static int WAIT_TIME_FOR_PREVIEW = 1500; //1.5 second
//Synthetic comment -- @@ -138,4 +138,4 @@
writeToOutputFile(totalStartupTime,
individualStartupTime, false, "Camera");
}
\ No newline at end of file
}







