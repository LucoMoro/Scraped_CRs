//<Beginning of snippet n. 0>
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Environment;

public abstract class FileList extends ListActivity {
    protected void setupPath() {
        mPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/layout_tests";
        mBaseLength = mPath.length();
    }

    protected String mPath;
    private int mFocusIndex;

    public void setListViewSelection() {
        getListView().setSelection(mFocusIndex);
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import android.util.Log;
import java.io.BufferedOutputStream;

public class FsUtils {
    private static final String LOGTAG = "FsUtils";
    static final String HTTP_TESTS_PREFIX = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/layout_tests/http/tests/";
    static final String HTTPS_TESTS_PREFIX = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/layout_tests/http/tests/ssl/";
    static final String HTTP_LOCAL_TESTS_PREFIX = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/layout_tests/http/tests/local/";
    static final String HTTP_MEDIA_TESTS_PREFIX = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/layout_tests/http/tests/media/";
    static final String HTTP_WML_TESTS_PREFIX = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/layout_tests/http/tests/wml/";

    private FsUtils() {
        // no creation of instances
    }
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import android.os.Environment;

public class MyTestRecorder {
    private static final String LOGTAG = "LayoutTests";
    static final int DEFAULT_TIMEOUT_IN_MILLIS = 5000;

    static final String LAYOUT_TESTS_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/layout_tests/";
    static final String LAYOUT_TESTS_RESULT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/layout_tests_results/";
    static final String ANDROID_EXPECTED_RESULT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/expected_results/";
    static final String LAYOUT_TESTS_LIST_FILE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/layout_tests_list.txt";
    static final String TEST_STATUS_FILE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/running_test.txt";
    private BufferedOutputStream mBufferedOutputPassedStream;

    public MyTestRecorder(boolean resume) {
        try {
            File resultsPassedFile = new File(LAYOUT_TESTS_ROOT + "layout_tests_passed.txt");
            File resultsFailedFile = new File(LAYOUT_TESTS_ROOT + "layout_tests_failed.txt");
            File resultsIgnoreResultFile = new File(LAYOUT_TESTS_ROOT + "layout_tests_ignored.txt");
            File noExpectedResultFile = new File(LAYOUT_TESTS_ROOT + "layout_tests_nontext.txt");

            mBufferedOutputPassedStream = new BufferedOutputStream(new FileOutputStream(resultsPassedFile, resume));
        } catch (Exception e) {
            Log.e(LOGTAG, "Error initializing test recorder", e);
        }
    }
    // Assuming other methods exist
}
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

public class LoadTestsAutoTest extends ActivityInstrumentationTestCase2<TestShellActivity> {
    private final static String LOGTAG = "LoadTest";
    private final static String LOAD_TEST_RESULT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/load_test_result.txt";

    private boolean mFinished;
    static final String LOAD_TEST_RUNNER_FILES[] = {
        "run_page_cycler.py"
    };
    // Assuming other methods exist
}
//<End of snippet n. 3>

//<Beginning of snippet n. 4>
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MenuActivity extends Activity {
    private static final int MENU_START = 0x01;
    private static String LOGTAG = "MenuActivity";
    static final String LAYOUT_TESTS_LIST_FILE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/layout_tests_list.txt";

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // Assuming other methods exist
    }
}
//<End of snippet n. 4>

//<Beginning of snippet n. 5>
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.test.ActivityInstrumentationTestCase2;

public class ReliabilityTest extends ActivityInstrumentationTestCase2 {
    private static final String LOGTAG = "ReliabilityTest";
    private static final String PKG_NAME = "com.android.dumprendertree";
    private static final String TEST_LIST_FILE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/reliability_tests_list.txt";
    private static final String TEST_STATUS_FILE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/reliability_running_test.txt";
    private static final String TEST_TIMEOUT_FILE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/reliability_timeout_test.txt";
    private static final String TEST_LOAD_TIME_FILE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/reliability_load_time.txt";
    private static final String TEST_DONE = "#DONE";
    static final String RELIABILITY_TEST_RUNNER_FILES[] = {
        "run_reliability_tests.py"
    };
    // Assuming other methods exist
}
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
static final String DRAW_TIME_LOG = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/page_draw_time.txt";

private boolean mGeolocationPermissionSet;
private boolean mGeolocationPermission;
// Assuming other methods exist
//<End of snippet n. 6>

//<Beginning of snippet n. 7>
import android.util.Log;

public class ForwardService {
    private static final String DEFAULT_TEST_HOST = "android-browser-test.mtv.corp.google.com";
    private static final String FORWARD_HOST_CONF = Environment.getExternalStorageDirectory().getAbsolutePath() + "/drt_forward_host.txt";

    private ForwardService() {
        int addr = getForwardHostAddr();
        // Assuming other methods exist
    }
}
//<End of snippet n. 7>

//<Beginning of snippet n. 8>
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.os.Environment;
import java.io.File;

private String getUniqueFileName(String ext) {
    File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/locationtracker");
    if (!dir.exists()) {
        dir.mkdir();
    }
    return Environment.getExternalStorageDirectory().getAbsolutePath() + "/locationtracker/tracking-" + DateUtils.getCurrentTimestamp() + "." + ext;
}

private void launchSettings() {
    // Assuming other methods exist
}
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
import android.os.Environment;

public class NotificationTestList extends TestActivity {
    // Assuming other methods exist
    public void notifyWithSound() {
        Notification n = new Notification();
        n.sound = Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/virtual-void.mp3");
        Log.d(TAG, "n.sound=" + n.sound);
        mNM.notify(1, n);
    }
}
//<End of snippet n. 9>