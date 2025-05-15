
//<Beginning of snippet n. 0>



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
log.append(line);
log.append(separator);
}

// no permission get empty log
assertEquals(0, log.length());

}
}
}

    public void testLogFilePermissions() {
        File logDir = new File("/dev/log");
        File[] logFiles = logDir.listFiles();
        assertTrue("Where are the log files? Please check that they are not world readable.",
                logFiles.length > 0);

        FileStatus status = new FileStatus();
        for (File log : logFiles) {
            if (FileUtils.getFileStatus(log.getAbsolutePath(), status)) {
                assertFalse("Log file "  + log.getAbsolutePath() + " should not be world readable.",
                        (status.mode & FileUtils.S_IROTH) == FileUtils.S_IROTH);
            }
        }
    }
}

//<End of snippet n. 0>








