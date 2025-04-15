/*Replaced /sdcard with Environment.getExternalStorageDirectory()

Change-Id:Ic3dd12b874b5ea218b71827744fc5c05e86180e3*/
//Synthetic comment -- diff --git a/tests/AndroidTests/src/com/android/unit_tests/NewDatabasePerformanceTests.java b/tests/AndroidTests/src/com/android/unit_tests/NewDatabasePerformanceTests.java
//Synthetic comment -- index 8644fbb..be22c9c 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.test.PerformanceTestCase;

import junit.framework.Assert;
//Synthetic comment -- @@ -28,14 +29,14 @@

/**
* Database Performance Tests
 * 
*/

public class NewDatabasePerformanceTests {

// Edit this to change the test run times.  The original is 100.
final static int kMultiplier = 1;
  
public static class PerformanceBase extends TestCase
implements PerformanceTestCase {
protected static final int CURRENT_DATABASE_VERSION = 42;
//Synthetic comment -- @@ -43,7 +44,8 @@
protected File mDatabaseFile;

public void setUp() {
      mDatabaseFile = new File("/sdcard", "perf_database_test.db");
if (mDatabaseFile.exists()) {
mDatabaseFile.delete();
}
//Synthetic comment -- @@ -486,7 +488,7 @@
}

/**
   *  100 SELECTs on integer 
*/

public static class SelectInteger100 extends PerformanceBase {
//Synthetic comment -- @@ -589,7 +591,7 @@

public static class SelectIndexString100 extends PerformanceBase {
private static final int SIZE = 1 * kMultiplier;
    private static final String[] COLUMNS = {"c"};      

@Override
public void setUp() {
//Synthetic comment -- @@ -693,7 +695,7 @@

public static class Delete1000 extends PerformanceBase {
private static final int SIZE = 10 * kMultiplier;
    private static final String[] COLUMNS = {"c"};       

@Override
public void setUp() {
//Synthetic comment -- @@ -719,7 +721,7 @@
}

/**
   *  1000 DELETE's without an index with where clause 
*/

public static class DeleteWhere1000 extends PerformanceBase {
//Synthetic comment -- @@ -755,7 +757,7 @@
}

/**
   *  1000 DELETE's with an index with where clause 
*/

public static class DeleteIndexWhere1000 extends PerformanceBase {
//Synthetic comment -- @@ -792,7 +794,7 @@
}

/**
   *  1000 update's with an index with where clause 
*/

public static class UpdateIndexWhere1000 extends PerformanceBase {
//Synthetic comment -- @@ -835,11 +837,11 @@
}

/**
   *  1000 update's without an index with where clause 
*/

public static class UpdateWhere1000 extends PerformanceBase {
    private static final int SIZE = 10 * kMultiplier;       
private String[] where = new String[SIZE];
ContentValues[] mValues = new ContentValues[SIZE];

//Synthetic comment -- @@ -876,7 +878,7 @@
}

/**
   *  10000 inserts for an integer 
*/

public static class InsertInteger10000 extends PerformanceBase {
//Synthetic comment -- @@ -897,7 +899,7 @@
b.put("a", r);
mValues[i] = b;
}
    }        

public void testRun() {
for (int i = 0; i < SIZE; i++) {
//Synthetic comment -- @@ -929,7 +931,7 @@
b.put("a", r);
mValues[i] = b;
}
    }        

public void testRun() {
for (int i = 0; i < SIZE; i++) {
//Synthetic comment -- @@ -939,7 +941,7 @@
}

/**
   *  10000 inserts for a String 
*/

public static class InsertString10000 extends PerformanceBase {
//Synthetic comment -- @@ -960,7 +962,7 @@
b.put("a", numberName(r));
mValues[i] = b;
}
    }        

public void testRun() {
for (int i = 0; i < SIZE; i++) {
//Synthetic comment -- @@ -970,11 +972,11 @@
}

/**
   *  10000 inserts for a String - indexed table 
*/

public static class InsertStringIndexed10000 extends PerformanceBase {
    private static final int SIZE = 100 * kMultiplier;       
ContentValues[] mValues = new ContentValues[SIZE];

@Override
//Synthetic comment -- @@ -990,7 +992,7 @@
int r = random.nextInt(100000);
ContentValues b = new ContentValues(1);
b.put("a", numberName(r));
        mValues[i] = b; 
}
}

//Synthetic comment -- @@ -1030,7 +1032,7 @@
where[i] = "a LIKE '" + numberName(r).substring(0, 1) + "*'";

}
    }        

public void testRun() {
for (int i = 0; i < SIZE; i++) {
//Synthetic comment -- @@ -1068,8 +1070,8 @@
int r = random.nextInt(100000);
where[i] = "a LIKE '" + numberName(r).substring(0, 1) + "*'";

      }                              
    }        

public void testRun() {
for (int i = 0; i < SIZE; i++) {
//Synthetic comment -- @@ -1102,7 +1104,7 @@
int upper = (i + 10) * 100;
where[i] = "a >= " + lower + " AND a < " + upper;
}
    }        

public void testRun() {
for (int i = 0; i < SIZE; i++) {
//Synthetic comment -- @@ -1138,7 +1140,7 @@
where[i] = "a >= " + lower + " AND a < " + upper;
}

    }        

public void testRun() {
for (int i = 0; i < SIZE; i++) {
//Synthetic comment -- @@ -1174,8 +1176,8 @@
for (int i = 0; i < SIZE; i++) {
where[i] = "a LIKE '*e*'";

      }                              
    }        

public void testRun() {
for (int i = 0; i < SIZE; i++) {
//Synthetic comment -- @@ -1212,8 +1214,8 @@
for (int i = 0; i < SIZE; i++) {
where[i] = "a LIKE '*e*'";

      }                              
    }        

public void testRun() {
for (int i = 0; i < SIZE; i++) {








//Synthetic comment -- diff --git a/tests/DumpRenderTree/src/com/android/dumprendertree/FileList.java b/tests/DumpRenderTree/src/com/android/dumprendertree/FileList.java
//Synthetic comment -- index e741177..6338524 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.os.Bundle;


public abstract class FileList extends ListActivity
//Synthetic comment -- @@ -179,17 +180,16 @@
getListView().setSelection(mFocusIndex);
}

    protected void setupPath()
    {
    	mPath = "/sdcard/android/layout_tests";
    	mBaseLength = mPath.length();
}

protected String mPath;
protected int mBaseLength;
protected String mFocusFile;
protected int mFocusIndex;
    
private final static int OPEN_DIRECTORY = 0;
private final static int RUN_TESTS = 1;









//Synthetic comment -- diff --git a/tests/DumpRenderTree/src/com/android/dumprendertree/FsUtils.java b/tests/DumpRenderTree/src/com/android/dumprendertree/FsUtils.java
//Synthetic comment -- index fea366c..f5db8c5 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.dumprendertree.forwarder.ForwardService;

import android.util.Log;

import java.io.BufferedOutputStream;
//Synthetic comment -- @@ -31,11 +32,12 @@
public class FsUtils {

private static final String LOGTAG = "FsUtils";
    static final String HTTP_TESTS_PREFIX = "/sdcard/android/layout_tests/http/tests/";
    static final String HTTPS_TESTS_PREFIX = "/sdcard/android/layout_tests/http/tests/ssl/";
    static final String HTTP_LOCAL_TESTS_PREFIX = "/sdcard/android/layout_tests/http/tests/local/";
    static final String HTTP_MEDIA_TESTS_PREFIX = "/sdcard/android/layout_tests/http/tests/media/";
    static final String HTTP_WML_TESTS_PREFIX = "/sdcard/android/layout_tests/http/tests/wml/";

private FsUtils() {
//no creation of instances








//Synthetic comment -- diff --git a/tests/DumpRenderTree/src/com/android/dumprendertree/LayoutTestsAutoTest.java b/tests/DumpRenderTree/src/com/android/dumprendertree/LayoutTestsAutoTest.java
//Synthetic comment -- index 8983612..84b51e7 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

//Synthetic comment -- @@ -90,10 +91,11 @@

public MyTestRecorder(boolean resume) {
try {
            File resultsPassedFile = new File("/sdcard/layout_tests_passed.txt");
            File resultsFailedFile = new File("/sdcard/layout_tests_failed.txt");
            File noExpectedResultFile = new File("/sdcard/layout_tests_nontext.txt");
            File resultTimedoutFile = new File("/sdcard/layout_tests_timedout.txt");

mBufferedOutputPassedStream =
new BufferedOutputStream(new FileOutputStream(resultsPassedFile, resume));
//Synthetic comment -- @@ -126,11 +128,12 @@
private static final String LOGTAG = "LayoutTests";
static final int DEFAULT_TIMEOUT_IN_MILLIS = 5000;

    static final String LAYOUT_TESTS_ROOT = "/sdcard/android/layout_tests/";
    static final String LAYOUT_TESTS_RESULT_DIR = "/sdcard/android/layout_tests_results/";
    static final String ANDROID_EXPECTED_RESULT_DIR = "/sdcard/android/expected_results/";
    static final String LAYOUT_TESTS_LIST_FILE = "/sdcard/android/layout_tests_list.txt";
    static final String TEST_STATUS_FILE = "/sdcard/android/running_test.txt";
static final String LAYOUT_TESTS_RESULTS_REFERENCE_FILES[] = {
"results/layout_tests_passed.txt",
"results/layout_tests_failed.txt",








//Synthetic comment -- diff --git a/tests/DumpRenderTree/src/com/android/dumprendertree/LoadTestsAutoTest.java b/tests/DumpRenderTree/src/com/android/dumprendertree/LoadTestsAutoTest.java
//Synthetic comment -- index ba46197..510e223 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Process;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
//Synthetic comment -- @@ -35,7 +36,8 @@
public class LoadTestsAutoTest extends ActivityInstrumentationTestCase2<TestShellActivity> {

private final static String LOGTAG = "LoadTest";
    private final static String LOAD_TEST_RESULT = "/sdcard/load_test_result.txt";
private boolean mFinished;
static final String LOAD_TEST_RUNNER_FILES[] = {
"run_page_cycler.py"








//Synthetic comment -- diff --git a/tests/DumpRenderTree/src/com/android/dumprendertree/Menu.java b/tests/DumpRenderTree/src/com/android/dumprendertree/Menu.java
//Synthetic comment -- index e15ab65..8465718 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedOutputStream;
//Synthetic comment -- @@ -28,7 +29,8 @@

private static final int MENU_START = 0x01;
private static String LOGTAG = "MenuActivity";
    static final String LAYOUT_TESTS_LIST_FILE = "/sdcard/android/layout_tests_list.txt";

public void onCreate(Bundle icicle) {
super.onCreate(icicle);








//Synthetic comment -- diff --git a/tests/DumpRenderTree/src/com/android/dumprendertree/ReliabilityTest.java b/tests/DumpRenderTree/src/com/android/dumprendertree/ReliabilityTest.java
//Synthetic comment -- index 9bc0962..92588f9 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.test.ActivityInstrumentationTestCase2;
//Synthetic comment -- @@ -37,10 +38,11 @@

private static final String LOGTAG = "ReliabilityTest";
private static final String PKG_NAME = "com.android.dumprendertree";
    private static final String TEST_LIST_FILE = "/sdcard/android/reliability_tests_list.txt";
    private static final String TEST_STATUS_FILE = "/sdcard/android/reliability_running_test.txt";
    private static final String TEST_TIMEOUT_FILE = "/sdcard/android/reliability_timeout_test.txt";
    private static final String TEST_LOAD_TIME_FILE = "/sdcard/android/reliability_load_time.txt";
private static final String TEST_DONE = "#DONE";
static final String RELIABILITY_TEST_RUNNER_FILES[] = {
"run_reliability_tests.py"








//Synthetic comment -- diff --git a/tests/DumpRenderTree/src/com/android/dumprendertree/forwarder/ForwardService.java b/tests/DumpRenderTree/src/com/android/dumprendertree/forwarder/ForwardService.java
//Synthetic comment -- index 8b7de6e..25dd04fd 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import java.io.FileReader;
import java.io.IOException;

import android.util.Log;

public class ForwardService {
//Synthetic comment -- @@ -33,7 +34,8 @@

private static final String DEFAULT_TEST_HOST = "android-browser-test.mtv.corp.google.com";

    private static final String FORWARD_HOST_CONF = "/sdcard/drt_forward_host.txt";

private ForwardService() {
int addr = getForwardHostAddr();








//Synthetic comment -- diff --git a/tests/LocationTracker/src/com/android/locationtracker/TrackerActivity.java b/tests/LocationTracker/src/com/android/locationtracker/TrackerActivity.java
//Synthetic comment -- index 98d0a50..4cfdf6c 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
//Synthetic comment -- @@ -210,12 +211,11 @@
}

private String getUniqueFileName(String ext) {
        File dir = new File("/sdcard/locationtracker");
if (!dir.exists()) {
dir.mkdir();
}
        return "/sdcard/locationtracker/tracking-" +
            DateUtils.getCurrentTimestamp() + "." + ext;
}

private void launchSettings() {








//Synthetic comment -- diff --git a/tests/StatusBar/src/com/android/statusbartest/NotificationTestList.java b/tests/StatusBar/src/com/android/statusbartest/NotificationTestList.java
//Synthetic comment -- index 65c7dcbd..61f7954 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import android.content.Intent;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Vibrator;
import android.os.Bundle;
import android.os.Handler;
//Synthetic comment -- @@ -54,7 +55,7 @@
@Override
protected Test[] tests() {
mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        
return mTests;
}

//Synthetic comment -- @@ -62,14 +63,15 @@
new Test("Off and sound") {
public void run() {
PowerManager pm = (PowerManager)NotificationTestList.this.getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wl = 
pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "sound");
wl.acquire();

pm.goToSleep(SystemClock.uptimeMillis());

Notification n = new Notification();
                n.sound = Uri.parse("file:///sdcard/virtual-void.mp3");
Log.d(TAG, "n.sound=" + n.sound);

mNM.notify(1, n);
//Synthetic comment -- @@ -154,7 +156,7 @@
n.defaults |= Notification.DEFAULT_SOUND ;
n.vibrate = new long[] {
300, 400, 300, 400, 300, 400, 300, 400, 300, 400, 300, 400,
                        300, 400, 300, 400, 300, 400, 300, 400, 300, 400, 300, 400, 
300, 400, 300, 400, 300, 400, 300, 400, 300, 400, 300, 400 };
mNM.notify(1, n);
}
//Synthetic comment -- @@ -513,7 +515,7 @@
}
}
},
        
new Test("Cancel eight notifications") {
public void run() {
for (int i = 1; i < 9; i++) {
//Synthetic comment -- @@ -521,7 +523,7 @@
}
}
},
        
new Test("Persistent with numbers 1") {
public void run() {
mNM.notify(1, notificationWithNumbers(1));







