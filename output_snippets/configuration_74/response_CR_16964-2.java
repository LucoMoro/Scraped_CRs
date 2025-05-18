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
* @throws IOException
*/
@MediumTest
public void testSetMicrophoneMute() throws IOException {
    StringBuilder log = new StringBuilder();
    String line;
    String separator = System.lineSeparator();
    
    try (Process logcatProc = Runtime.getRuntime().exec(new String[]
            {"logcat", "-d", "AndroidRuntime:E:" + LOGTAG + ":V *:S"});
         BufferedReader reader = new BufferedReader(new InputStreamReader(logcatProc.getInputStream()))) {
         
        Log.d(LOGTAG, "no read logs permission test");
        while ((line = reader.readLine()) != null) {
            log.append(line);
            log.append(separator);
        }
    }

    assertEquals(0, log.length());

    // World Readability Test (pseudo code)
    assertFalse("Log files should not be world-readable", areLogFilesWorldReadable());
}

//<End of snippet n. 0>