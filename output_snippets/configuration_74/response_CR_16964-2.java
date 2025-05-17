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
public void testLogPermissions() throws IOException {
    StringBuilder log = new StringBuilder();
    String separator = System.lineSeparator();
    
    try (Process logcatProc = Runtime.getRuntime().exec(new String[] {"logcat", "-d", "AndroidRuntime:E:" + LOGTAG + ":V *:S"});
         BufferedReader reader = new BufferedReader(new InputStreamReader(logcatProc.getInputStream()))) {
        
        Log.d(LOGTAG, "no read logs permission test");
        
        String line;
        while ((line = reader.readLine()) != null) {
            log.append(line);
            log.append(separator);
        }
        
    } catch (IOException e) {
        Log.e(LOGTAG, "Failed to execute logcat command", e);
    }
    
    // no permission get empty log
    assertEquals(0, log.length());
}

//<End of snippet n. 0>