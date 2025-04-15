/*Revert "Watchdog: Improvement of debuggability"

This reverts commit 9211b13c3268035b0da0c51ed2d6d5a578d45ff3.*/




//Synthetic comment -- diff --git a/services/java/com/android/server/Watchdog.java b/services/java/com/android/server/Watchdog.java
//Synthetic comment -- index c14e2f6..c239382 100644

//Synthetic comment -- @@ -25,7 +25,6 @@
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
//Synthetic comment -- @@ -39,8 +38,6 @@
import android.util.Slog;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

//Synthetic comment -- @@ -431,10 +428,11 @@
}

// If we got here, that means that the system is most likely hung.
            // First collect stack traces from all threads of the system process.
            // Then kill this process so that the system will restart.

final String name = (mCurrentMonitor != null) ?
mCurrentMonitor.getClass().getName() : "null";
EventLog.writeEvent(EventLogTags.WATCHDOG, name);

ArrayList<Integer> pids = new ArrayList<Integer>();
//Synthetic comment -- @@ -469,15 +467,11 @@
dropboxThread.join(2000);  // wait up to 2 seconds for it to return.
} catch (InterruptedException ignored) {}

            // Only kill the process if the debugger is not attached.
if (!Debug.isDebuggerConnected()) {
Slog.w(TAG, "*** WATCHDOG KILLING SYSTEM PROCESS: " + name);
                Process.killProcess(Process.myPid());
                System.exit(10);
} else {
Slog.w(TAG, "Debugger connected: Watchdog is *not* killing the system process");
}
//Synthetic comment -- @@ -486,50 +480,6 @@
}
}

private File dumpKernelStackTraces() {
String tracesPath = SystemProperties.get("dalvik.vm.stack-trace-file", null);
if (tracesPath == null || tracesPath.length() == 0) {







