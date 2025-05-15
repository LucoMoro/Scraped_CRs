//<Beginning of snippet n. 0>


import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.os.Bundle;
import android.os.Environment;

public abstract class FileList extends ListActivity {
    protected String mPath;

    protected void setupPath() {
        mPath = Environment.getExternalStorageDirectory().getPath() + "/android/layout_tests";
    }

    public void focusList() {
        getListView().setSelection(mFocusIndex);
    }
}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>



import com.android.dumprendertree.forwarder.ForwardService;
import android.util.Log;
import java.io.BufferedOutputStream;

public class FsUtils {

    private static final String LOGTAG = "FsUtils";
    static final String HTTP_TESTS_PREFIX = Environment.getExternalStorageDirectory().getPath() + "/android/layout_tests/http/tests/";
    static final String HTTPS_TESTS_PREFIX = Environment.getExternalStorageDirectory().getPath() + "/android/layout_tests/http/tests/ssl/";
    static final String HTTP_LOCAL_TESTS_PREFIX = Environment.getExternalStorageDirectory().getPath() + "/android/layout_tests/http/tests/local/";
    static final String HTTP_MEDIA_TESTS_PREFIX = Environment.getExternalStorageDirectory().getPath() + "/android/layout_tests/http/tests/media/";
    static final String HTTP_WML_TESTS_PREFIX = Environment.getExternalStorageDirectory().getPath() + "/android/layout_tests/http/tests/wml/";

    private FsUtils() {
        // no creation of instances
    }

//<End of snippet n. 1>










//<Beginning of snippet n. 2>



import com.android.dumprendertree.TestShellActivity.DumpDataType;
import com.android.dumprendertree.forwarder.AdbUtils;
import com.android.dumprendertree.forwarder.ForwardServer;
import com.android.dumprendertree.forwarder.ForwardService;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;

public class MyTestRecorder {
    private static final String LOGTAG = "LayoutTests";
    static final int DEFAULT_TIMEOUT_IN_MILLIS = 5000;
    static final String LAYOUT_TESTS_ROOT = Environment.getExternalStorageDirectory().getPath() + "/android/layout_tests/";
    static final String LAYOUT_TESTS_RESULT_DIR = Environment.getExternalStorageDirectory().getPath() + "/android/layout_tests_results/";
    static final String ANDROID_EXPECTED_RESULT_DIR = Environment.getExternalStorageDirectory().getPath() + "/android/expected_results/";
    static final String LAYOUT_TESTS_LIST_FILE = Environment.getExternalStorageDirectory().getPath() + "/android/layout_tests_list.txt";
    static final String TEST_STATUS_FILE = Environment.getExternalStorageDirectory().getPath() + "/android/running_test.txt";

    private BufferedOutputStream mBufferedOutputPassedStream;

    public MyTestRecorder(boolean resume) {
        try {
            File resultsPassedFile = new File(Environment.getExternalStorageDirectory().getPath() + "/layout_tests_passed.txt");
            File resultsFailedFile = new File(Environment.getExternalStorageDirectory().getPath() + "/layout_tests_failed.txt");
            File resultsIgnoreResultFile = new File(Environment.getExternalStorageDirectory().getPath() + "/layout_tests_ignored.txt");
            File noExpectedResultFile = new File(Environment.getExternalStorageDirectory().getPath() + "/layout_tests_nontext.txt");

            mBufferedOutputPassedStream = new BufferedOutputStream(new FileOutputStream(resultsPassedFile, resume));
        } catch (Exception e) {
            Log.e(LOGTAG, "Error initializing MyTestRecorder", e);
        }
    }

    static final String LAYOUT_TESTS_RESULTS_REFERENCE_FILES[] = {
        "results/layout_tests_passed.txt",
        "results/layout_tests_failed.txt",
    };

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Process;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

public class LoadTestsAutoTest extends ActivityInstrumentationTestCase2<TestShellActivity> {

    private final static String LOGTAG = "LoadTest";
    private final static String LOAD_TEST_RESULT = Environment.getExternalStorageDirectory().getPath() + "/load_test_result.txt";
    private boolean mFinished;
    static final String LOAD_TEST_RUNNER_FILES[] = {
        "run_page_cycler.py"
    };

//<End of snippet n. 3>










//<Beginning of snippet n. 4>



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedOutputStream;

private static final int MENU_START = 0x01;
private static String LOGTAG = "MenuActivity";
static final String LAYOUT_TESTS_LIST_FILE = Environment.getExternalStorageDirectory().getPath() + "/android/layout_tests_list.txt";

public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
}

//<End of snippet n. 4>










//<Beginning of snippet n. 5>



import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.test.ActivityInstrumentationTestCase2;

private static final String LOGTAG = "ReliabilityTest";
private static final String PKG_NAME = "com.android.dumprendertree";
private static final String TEST_LIST_FILE = Environment.getExternalStorageDirectory().getPath() + "/android/reliability_tests_list.txt";
private static final String TEST_STATUS_FILE = Environment.getExternalStorageDirectory().getPath() + "/android/reliability_running_test.txt";
private static final String TEST_TIMEOUT_FILE = Environment.getExternalStorageDirectory().getPath() + "/android/reliability_timeout_test.txt";
private static final String TEST_LOAD_TIME_FILE = Environment.getExternalStorageDirectory().getPath() + "/android/reliability_load_time.txt";
private static final String TEST_DONE = "#DONE";
static final String RELIABILITY_TEST_RUNNER_FILES[] = {
    "run_reliability_tests.py"
};

//<End of snippet n. 5>










//<Beginning of snippet n. 6>


import android.graphics.Bitmap.Config;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

static final String SAVE_IMAGE = "SaveImage";
static final int DRAW_RUNS = 5;
static final String DRAW_TIME_LOG = Environment.getExternalStorageDirectory().getPath() + "/android/page_draw_time.txt";

private boolean mGeolocationPermissionSet;
private boolean mGeolocationPermission;

//<End of snippet n. 6>










//<Beginning of snippet n. 7>


import java.io.FileReader;
import java.io.IOException;
import android.util.Log;

public class ForwardService {

    private static final String DEFAULT_TEST_HOST = "android-browser-test.mtv.corp.google.com";
    private static final String FORWARD_HOST_CONF = Environment.getExternalStorageDirectory().getPath() + "/drt_forward_host.txt";

    private ForwardService() {
        int addr = getForwardHostAddr();
    }

//<End of snippet n. 7>










//<Beginning of snippet n. 8>


import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

private String getUniqueFileName(String ext) {
    File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/locationtracker");
    if (!dir.exists()) {
        dir.mkdir();
    }
    return Environment.getExternalStorageDirectory().getPath() + "/locationtracker/tracking-" +
        DateUtils.getCurrentTimestamp() + "." + ext;
}

private void launchSettings() {

//<End of snippet n. 8>










//<Beginning of snippet n. 9>



package com.android.statusbartest;

import android.app.ListActivity;
import android.app.PendingIntent;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.ListView;
import android.content.Context;
import android.content.ContentResolver;
import android.content.Intent;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Vibrator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.net.Uri;
import android.os.SystemClock;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.os.PowerManager;

public class NotificationTestList extends TestActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        pm.goToSleep(SystemClock.uptimeMillis());

        Notification n = new Notification();
        n.sound = Uri.parse("file://" + Environment.getExternalStorageDirectory().getPath() + "/virtual-void.mp3");
        Log.d(LOGTAG, "n.sound=" + n.sound);

        NotificationManager mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNM.notify(1, n);
    }
//<End of snippet n. 9>