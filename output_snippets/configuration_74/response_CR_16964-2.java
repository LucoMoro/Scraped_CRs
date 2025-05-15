//<Beginning of snippet n. 0>

package android.permission.cts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;

/**
* Verify the read system log require specific permissions.
*/
@MediumTest
public void testLogFilePermissions() throws IOException {
    Process logcatProc = null;
    BufferedReader reader = null;
    StringBuilder log = new StringBuilder();
    String line;
    String LOGTAG = "LogFilePermissionsTest";
    String separator = "\n";

    try {
        logcatProc = Runtime.getRuntime().exec(new String[]{"logcat", "-d", "AndroidRuntime:E " + LOGTAG + ":V *:S" });
        Log.d(LOGTAG, "no read logs permission test");

        reader = new BufferedReader(new InputStreamReader(logcatProc.getInputStream()));
        while ((line = reader.readLine()) != null) {
            log.append(line);
            log.append(separator);
        }
    } catch (IOException e) {
        Log.e(LOGTAG, "Error executing logcat command", e);
        return;
    } finally {
        if (reader != null) {
            reader.close();
        }
        if (logcatProc != null) {
            logcatProc.destroy();
        }
    }

    // no permission get empty log
    assertEquals(0, log.length());
}

//<End of snippet n. 0>