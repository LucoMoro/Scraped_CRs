/*Replaced /sdcard with Environment.getExternalStorageDirectory()

Change-Id:I9a7413f81090b69c82ca6b1e585f5e379b19e60c*/




//Synthetic comment -- diff --git a/tests/DumpRenderTree/src/com/android/dumprendertree/FileList.java b/tests/DumpRenderTree/src/com/android/dumprendertree/FileList.java
//Synthetic comment -- index e741177..73d7363 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.os.Bundle;
import android.os.Environment;


public abstract class FileList extends ListActivity
//Synthetic comment -- @@ -179,10 +180,9 @@
getListView().setSelection(mFocusIndex);
}

    protected void setupPath() {
        mPath = Environment.getExternalStorageDirectory() + "/android/layout_tests";
        mBaseLength = mPath.length();
}

protected String mPath;








//Synthetic comment -- diff --git a/tests/DumpRenderTree/src/com/android/dumprendertree/FsUtils.java b/tests/DumpRenderTree/src/com/android/dumprendertree/FsUtils.java
//Synthetic comment -- index 322b0d2..6cfce41 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.dumprendertree.forwarder.ForwardService;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
//Synthetic comment -- @@ -32,11 +33,17 @@
public class FsUtils {

private static final String LOGTAG = "FsUtils";
    static final String EXTERNAL_DIR = Environment.getExternalStorageDirectory().toString();
    static final String HTTP_TESTS_PREFIX =
        EXTERNAL_DIR + "/android/layout_tests/http/tests/";
    static final String HTTPS_TESTS_PREFIX =
        EXTERNAL_DIR + "/android/layout_tests/http/tests/ssl/";
    static final String HTTP_LOCAL_TESTS_PREFIX =
        EXTERNAL_DIR + "/android/layout_tests/http/tests/local/";
    static final String HTTP_MEDIA_TESTS_PREFIX =
        EXTERNAL_DIR + "/android/layout_tests/http/tests/media/";
    static final String HTTP_WML_TESTS_PREFIX =
        EXTERNAL_DIR + "/android/layout_tests/http/tests/wml/";

private FsUtils() {
//no creation of instances








//Synthetic comment -- diff --git a/tests/DumpRenderTree/src/com/android/dumprendertree/LayoutTestsAutoTest.java b/tests/DumpRenderTree/src/com/android/dumprendertree/LayoutTestsAutoTest.java
//Synthetic comment -- index 042158a..9ccf549 100644

//Synthetic comment -- @@ -18,12 +18,12 @@

import com.android.dumprendertree.TestShellActivity.DumpDataType;
import com.android.dumprendertree.forwarder.AdbUtils;
import com.android.dumprendertree.forwarder.ForwardService;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

//Synthetic comment -- @@ -92,10 +92,11 @@

public MyTestRecorder(boolean resume) {
try {
            File externalDir = Environment.getExternalStorageDirectory();
            File resultsPassedFile = new File(externalDir, "layout_tests_passed.txt");
            File resultsFailedFile = new File(externalDir, "layout_tests_failed.txt");
            File resultsIgnoreResultFile = new File(externalDir, "layout_tests_ignored.txt");
            File noExpectedResultFile = new File(externalDir, "layout_tests_nontext.txt");

mBufferedOutputPassedStream =
new BufferedOutputStream(new FileOutputStream(resultsPassedFile, resume));
//Synthetic comment -- @@ -128,11 +129,12 @@
private static final String LOGTAG = "LayoutTests";
static final int DEFAULT_TIMEOUT_IN_MILLIS = 5000;

    static final String EXTERNAL_DIR = Environment.getExternalStorageDirectory().toString();
    static final String LAYOUT_TESTS_ROOT = EXTERNAL_DIR + "/android/layout_tests/";
    static final String LAYOUT_TESTS_RESULT_DIR = EXTERNAL_DIR + "/android/layout_tests_results/";
    static final String ANDROID_EXPECTED_RESULT_DIR = EXTERNAL_DIR + "/android/expected_results/";
    static final String LAYOUT_TESTS_LIST_FILE = EXTERNAL_DIR + "/android/layout_tests_list.txt";
    static final String TEST_STATUS_FILE = EXTERNAL_DIR + "/android/running_test.txt";
static final String LAYOUT_TESTS_RESULTS_REFERENCE_FILES[] = {
"results/layout_tests_passed.txt",
"results/layout_tests_failed.txt",








//Synthetic comment -- diff --git a/tests/DumpRenderTree/src/com/android/dumprendertree/LoadTestsAutoTest.java b/tests/DumpRenderTree/src/com/android/dumprendertree/LoadTestsAutoTest.java
//Synthetic comment -- index 2ef342f..9352f39 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.Process;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
//Synthetic comment -- @@ -35,7 +36,8 @@
public class LoadTestsAutoTest extends ActivityInstrumentationTestCase2<TestShellActivity> {

private final static String LOGTAG = "LoadTest";
    private final static String LOAD_TEST_RESULT =
        Environment.getExternalStorageDirectory() + "/load_test_result.txt";
private boolean mFinished;
static final String LOAD_TEST_RUNNER_FILES[] = {
"run_page_cycler.py"








//Synthetic comment -- diff --git a/tests/DumpRenderTree/src/com/android/dumprendertree/Menu.java b/tests/DumpRenderTree/src/com/android/dumprendertree/Menu.java
//Synthetic comment -- index 82671eb..9c4b57256 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
//Synthetic comment -- @@ -28,7 +29,8 @@

private static final int MENU_START = 0x01;
private static String LOGTAG = "MenuActivity";
    static final String LAYOUT_TESTS_LIST_FILE =
        Environment.getExternalStorageDirectory() + "/android/layout_tests_list.txt";

public void onCreate(Bundle icicle) {
super.onCreate(icicle);








//Synthetic comment -- diff --git a/tests/DumpRenderTree/src/com/android/dumprendertree/ReliabilityTest.java b/tests/DumpRenderTree/src/com/android/dumprendertree/ReliabilityTest.java
//Synthetic comment -- index 9bc0962..d146fc7 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.test.ActivityInstrumentationTestCase2;
//Synthetic comment -- @@ -37,10 +38,16 @@

private static final String LOGTAG = "ReliabilityTest";
private static final String PKG_NAME = "com.android.dumprendertree";
    private static final String EXTERNAL_DIR =
        Environment.getExternalStorageDirectory().toString();
    private static final String TEST_LIST_FILE = EXTERNAL_DIR +
        "/android/reliability_tests_list.txt";
    private static final String TEST_STATUS_FILE = EXTERNAL_DIR +
        "/android/reliability_running_test.txt";
    private static final String TEST_TIMEOUT_FILE = EXTERNAL_DIR +
        "/android/reliability_timeout_test.txt";
    private static final String TEST_LOAD_TIME_FILE = EXTERNAL_DIR +
        "/android/reliability_load_time.txt";
private static final String TEST_DONE = "#DONE";
static final String RELIABILITY_TEST_RUNNER_FILES[] = {
"run_reliability_tests.py"








//Synthetic comment -- diff --git a/tests/DumpRenderTree/src/com/android/dumprendertree/TestShellActivity.java b/tests/DumpRenderTree/src/com/android/dumprendertree/TestShellActivity.java
//Synthetic comment -- index 81d5b08..7475719 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
import android.graphics.Bitmap.Config;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
//Synthetic comment -- @@ -862,7 +863,8 @@
static final String SAVE_IMAGE = "SaveImage";

static final int DRAW_RUNS = 5;
    static final String DRAW_TIME_LOG = Environment.getExternalStorageDirectory() +
        "/android/page_draw_time.txt";

private boolean mGeolocationPermissionSet;
private boolean mGeolocationPermission;








//Synthetic comment -- diff --git a/tests/DumpRenderTree/src/com/android/dumprendertree/forwarder/ForwardService.java b/tests/DumpRenderTree/src/com/android/dumprendertree/forwarder/ForwardService.java
//Synthetic comment -- index 8b7de6e..25dd04fd 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import java.io.FileReader;
import java.io.IOException;

import android.os.Environment;
import android.util.Log;

public class ForwardService {
//Synthetic comment -- @@ -33,7 +34,8 @@

private static final String DEFAULT_TEST_HOST = "android-browser-test.mtv.corp.google.com";

    private static final String FORWARD_HOST_CONF =
        Environment.getExternalStorageDirectory() + "/drt_forward_host.txt";

private ForwardService() {
int addr = getForwardHostAddr();








//Synthetic comment -- diff --git a/tests/LocationTracker/src/com/android/locationtracker/TrackerActivity.java b/tests/LocationTracker/src/com/android/locationtracker/TrackerActivity.java
//Synthetic comment -- index 98d0a50..4cfdf6c 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
//Synthetic comment -- @@ -210,12 +211,11 @@
}

private String getUniqueFileName(String ext) {
        File dir = new File(Environment.getExternalStorageDirectory() + "/locationtracker");
if (!dir.exists()) {
dir.mkdir();
}
        return dir + "/tracking-" + DateUtils.getCurrentTimestamp() + "." + ext;
}

private void launchSettings() {








//Synthetic comment -- diff --git a/tests/StatusBar/src/com/android/statusbartest/NotificationTestList.java b/tests/StatusBar/src/com/android/statusbartest/NotificationTestList.java
//Synthetic comment -- index a6124bf..9ed5156 100644

//Synthetic comment -- @@ -16,24 +16,19 @@

package com.android.statusbartest;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContentResolver;
import android.content.Intent;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Environment;
import android.os.Vibrator;
import android.os.Handler;
import android.util.Log;
import android.net.Uri;
import android.os.SystemClock;
import android.widget.RemoteViews;
import android.os.PowerManager;

public class NotificationTestList extends TestActivity
//Synthetic comment -- @@ -69,7 +64,8 @@
pm.goToSleep(SystemClock.uptimeMillis());

Notification n = new Notification();
                n.sound = Uri.parse("file://" + Environment.getExternalStorageDirectory() +
                        "/virtual-void.mp3");
Log.d(TAG, "n.sound=" + n.sound);

mNM.notify(1, n);







