//<Beginning of snippet n. 0>

package android.permission.cts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;

/**
* Verify the read system log require specific permissions.
* @throws IOException
*/
@MediumTest
public void testSetMicrophoneMute() throws IOException {
    Process logcatProc = null;
    StringBuilder log = new StringBuilder();
    String line;
    String separator = "\n";
    
    try {
        File logFile = new File("/dev/log");
        if (logFile.canRead() && !logFile.canExecute() && !logFile.canWrite()) {
            logcatProc = Runtime.getRuntime().exec(new String[]{"logcat", "-d", "AndroidRuntime:E " + LOGTAG + ":V *:S"});
            Log.d(LOGTAG, "no read logs permission test");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(logcatProc.getInputStream()))) {
                while ((line = reader.readLine()) != null) {
                    log.append(line);
                    log.append(separator);
                }
            }
        }
    } catch (IOException e) {
        Log.e(LOGTAG, "Error executing logcat command", e);
        throw e;
    }
    
    // no permission get empty log
    assertEquals("Log length should be 0 when no permission", 0, log.length());
}

//<End of snippet n. 0>