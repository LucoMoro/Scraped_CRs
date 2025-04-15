/*Replaced /sdcard/ with Environment.getExternalStorageDirectory()

Change-Id:I09792354504c8bd808898b1e3ed97721ff002eb8*/
//Synthetic comment -- diff --git a/tests/src/com/android/camera/stress/CameraLatency.java b/tests/src/com/android/camera/stress/CameraLatency.java
//Synthetic comment -- index 5c6d308..3a0a26c 100755

//Synthetic comment -- @@ -22,6 +22,7 @@
import java.io.FileWriter;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
//Synthetic comment -- @@ -36,7 +37,8 @@
private String TAG = "CameraLatency";
private static final int TOTAL_NUMBER_OF_IMAGECAPTURE = 20;
private static final long WAIT_FOR_IMAGE_CAPTURE_TO_BE_TAKEN = 4000;
    private static final String CAMERA_TEST_OUTPUT_FILE = "/sdcard/mediaStressOut.txt";

private long mTotalAutoFocusTime;
private long mTotalShutterLag;








//Synthetic comment -- diff --git a/tests/src/com/android/camera/stress/CameraStartUp.java b/tests/src/com/android/camera/stress/CameraStartUp.java
//Synthetic comment -- index a07b6ba..77bbebf 100644

//Synthetic comment -- @@ -2,7 +2,7 @@

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Debug;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.app.Activity;
//Synthetic comment -- @@ -18,7 +18,8 @@
private static final int TOTAL_NUMBER_OF_STARTUP = 20;

private String TAG = "CameraStartUp";
    private static final String CAMERA_TEST_OUTPUT_FILE = "/sdcard/mediaStressOut.txt";
private static final String CAMERA_PACKAGE_NAME = "com.google.android.camera";
private static final String CAMERA_ACTIVITY_NAME = "com.android.camera.Camera";
private static final String VIDEORECORDER_ACTIVITY_NAME = "com.android.camera.VideoCamera";








//Synthetic comment -- diff --git a/tests/src/com/android/camera/stress/ImageCapture.java b/tests/src/com/android/camera/stress/ImageCapture.java
//Synthetic comment -- index 0eb5586..0be758c 100755

//Synthetic comment -- @@ -19,15 +19,16 @@
import com.android.camera.Camera;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.KeyEvent;
import java.io.FileWriter;
import java.io.Writer;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import android.content.Intent;
//Synthetic comment -- @@ -51,7 +52,8 @@
private static final long WAIT_FOR_PREVIEW = 1500; //1.5 seconds
private static final long WAIT_FOR_STABLE_STATE = 2000; //2 seconds
private static final int NO_OF_LOOPS_TAKE_MEMORY_SNAPSHOT = 10;
    private static final String CAMERA_MEM_OUTPUTFILE = "/sdcard/ImageCaptureMemOut.txt";

//the tolerant memory leak
private static final int MAX_ACCEPTED_MEMORY_LEAK_KB = 150;
//Synthetic comment -- @@ -61,7 +63,8 @@
private static int mStartPid = 0;
private static int mEndPid = 0;

    private static final String CAMERA_TEST_OUTPUT_FILE = "/sdcard/mediaStressOut.txt";
private BufferedWriter mOut;
private FileWriter mfstream;









//Synthetic comment -- diff --git a/tests/src/com/android/camera/stress/SwitchPreview.java b/tests/src/com/android/camera/stress/SwitchPreview.java
//Synthetic comment -- index 4575043..5be0021 100755

//Synthetic comment -- @@ -22,6 +22,7 @@

import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
//Synthetic comment -- @@ -41,7 +42,8 @@
private static final int TOTAL_NUMBER_OF_SWITCHING = 200;
private static final long WAIT_FOR_PREVIEW = 4000;

    private static final String CAMERA_TEST_OUTPUT_FILE = "/sdcard/mediaStressOut.txt";
private BufferedWriter mOut;
private FileWriter mfstream;








