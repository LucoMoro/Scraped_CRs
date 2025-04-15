/*Replaced /sdcard with Environment.getExternalStorageDirectory()

Change-Id:I6c6296f881e5629d86122bf731206eabea180641*/




//Synthetic comment -- diff --git a/tests/src/com/android/camera/stress/CameraLatency.java b/tests/src/com/android/camera/stress/CameraLatency.java
//Synthetic comment -- index 221e7b1..032b271 100755

//Synthetic comment -- @@ -22,6 +22,7 @@
import java.io.FileWriter;

import android.app.Instrumentation;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
//Synthetic comment -- @@ -36,7 +37,8 @@
private String TAG = "CameraLatency";
private static final int TOTAL_NUMBER_OF_IMAGECAPTURE = 20;
private static final long WAIT_FOR_IMAGE_CAPTURE_TO_BE_TAKEN = 3000;
    private static final String CAMERA_TEST_OUTPUT_FILE =
        Environment.getExternalStorageDirectory() + "/mediaStressOut.txt";

private long mTotalAutoFocusTime;
private long mTotalShutterLag;








//Synthetic comment -- diff --git a/tests/src/com/android/camera/stress/CameraStartUp.java b/tests/src/com/android/camera/stress/CameraStartUp.java
//Synthetic comment -- index 71325d2..936411e 100644

//Synthetic comment -- @@ -3,6 +3,7 @@
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Debug;
import android.os.Environment;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.app.Activity;
//Synthetic comment -- @@ -18,7 +19,8 @@
private static final int TOTAL_NUMBER_OF_STARTUP = 20;

private String TAG = "CameraStartUp";
    private static final String CAMERA_TEST_OUTPUT_FILE =
        Environment.getExternalStorageDirectory() + "/mediaStressOut.txt";
private static final String CAMERA_PACKAGE_NAME = "com.android.camera";
private static final String CAMERA_ACTIVITY_NAME = "com.android.camera.Camera";
private static final String VIDEORECORDER_ACTIVITY_NAME = "com.android.camera.VideoCamera";








//Synthetic comment -- diff --git a/tests/src/com/android/camera/stress/ImageCapture.java b/tests/src/com/android/camera/stress/ImageCapture.java
//Synthetic comment -- index 49e8d41..26b47a8 100755

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.camera.Camera;

import android.app.Instrumentation;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
//Synthetic comment -- @@ -51,7 +52,9 @@
private static final long WAIT_FOR_PREVIEW = 1500; //1.5 seconds
private static final long WAIT_FOR_STABLE_STATE = 2000; //2 seconds
private static final int NO_OF_LOOPS_TAKE_MEMORY_SNAPSHOT = 10;
    private static final String EXTERNAL_DIR = Environment.getExternalStorageDirectory().toString();
    private static final String CAMERA_MEM_OUTPUTFILE =
        EXTERNAL_DIR + "/ImageCaptureMemOut.txt";

//the tolerant memory leak
private static final int MAX_ACCEPTED_MEMORY_LEAK_KB = 150;
//Synthetic comment -- @@ -61,7 +64,8 @@
private static int mStartPid = 0;
private static int mEndPid = 0;

    private static final String CAMERA_TEST_OUTPUT_FILE =
        EXTERNAL_DIR + "/mediaStressOut.txt";
private BufferedWriter mOut;
private FileWriter mfstream;









//Synthetic comment -- diff --git a/tests/src/com/android/camera/stress/SwitchPreview.java b/tests/src/com/android/camera/stress/SwitchPreview.java
//Synthetic comment -- index e03a563..cf2ca59 100755

//Synthetic comment -- @@ -22,6 +22,7 @@

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
//Synthetic comment -- @@ -41,7 +42,8 @@
private static final int TOTAL_NUMBER_OF_SWITCHING = 200;
private static final long WAIT_FOR_PREVIEW = 4000;

    private static final String CAMERA_TEST_OUTPUT_FILE =
        Environment.getExternalStorageDirectory() + "/mediaStressOut.txt";
private BufferedWriter mOut;
private FileWriter mfstream;








