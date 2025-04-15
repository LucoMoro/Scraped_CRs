/*Read Log Permission Test Fixes

Bug 2964078

Fix a typo in the logcat command used in the test. There was an
extra colon in one of the arguments.

Add a test to check that log files under "/dev/log" are not world
readable.

Change-Id:I6aafe9cfcdee1250f5d693cf83abb826faf63344*/




//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/NoReadLogsPermissionTest.java b/tests/tests/permission/src/android/permission/cts/NoReadLogsPermissionTest.java
//Synthetic comment -- index db57927..10ed4be 100644

//Synthetic comment -- @@ -16,14 +16,17 @@

package android.permission.cts;

import android.os.FileUtils;
import android.os.FileUtils.FileStatus;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
* Verify the read system log require specific permissions.
*/
//Synthetic comment -- @@ -37,12 +40,12 @@
* @throws IOException
*/
@MediumTest
    public void testLogcat() throws IOException {
Process logcatProc = null;
BufferedReader reader = null;
try {
logcatProc = Runtime.getRuntime().exec(new String[]
                    {"logcat", "-d", "AndroidRuntime:E " + LOGTAG + ":V *:S" });
Log.d(LOGTAG, "no read logs permission test");

reader = new BufferedReader(new InputStreamReader(logcatProc.getInputStream()));
//Synthetic comment -- @@ -54,6 +57,7 @@
log.append(line);
log.append(separator);
}

// no permission get empty log
assertEquals(0, log.length());

//Synthetic comment -- @@ -63,4 +67,18 @@
}
}
}

    public void testLogFilePermissions() {
        File logDir = new File("/dev/log");
        File[] logFiles = logDir.listFiles();
        assertTrue("Where are the log files? Please check that they are not world readable.", logFiles.length > 0);

        FileStatus status = new FileStatus();
        for (File log : logFiles) {
            if (FileUtils.getFileStatus(log.getAbsolutePath(), status)) {
                assertFalse("Log file "  + log.getAbsolutePath() + " should not be world readable.",
                        (status.mode & FileUtils.S_IROTH) == FileUtils.S_IROTH);
            }
        }
    }
}







