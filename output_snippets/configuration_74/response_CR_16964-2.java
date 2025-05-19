//<Beginning of snippet n. 0>

package android.permission.cts;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;

/**
* Verify the read system log require specific permissions.
* @throws IOException
*/
@MediumTest
public void testLogFilePermissions() throws IOException {
    StringBuilder log = new StringBuilder();
    try (Process logcatProc = Runtime.getRuntime().exec(new String[]{
            "logcat", "-d", "AndroidRuntime:E:" + LOGTAG + ":V", "*:S"});
         BufferedReader reader = new BufferedReader(new InputStreamReader(logcatProc.getInputStream()))) {
        Log.d(LOGTAG, "no read logs permission test");
        String line;
        while ((line = reader.readLine()) != null) {
            log.append(line);
            log.append("\n");
        }
    }
    assertTrue(log.length() == 0);
}

public void testLogFileWorldReadable() {
    File logDir = new File("/dev/log");
    File[] logFiles = logDir.listFiles();
    assertNotNull(logFiles);
    for (File logFile : logFiles) {
        assertFalse("Log file " + logFile.getName() + " should not be world-readable", logFile.canRead());
    }
}

//<End of snippet n. 0>