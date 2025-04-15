/*Watchdog: Improvement of debuggability

If the watchdog detects a problem the system server process
is killed, that is followed by a crash. Because the crash is
done after the system server process is killed, the crash
don't contain info about the system server.
This improvement will make sure that the system is crashed
before the system server process is killed.
Behavior is only changed for eng and userdebug builds.

Change-Id:I945849575e2db6e9eb4718a5715075709606008a*/
//Synthetic comment -- diff --git a/services/java/com/android/server/Watchdog.java b/services/java/com/android/server/Watchdog.java
//Synthetic comment -- index c239382..c14e2f6 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
//Synthetic comment -- @@ -38,6 +39,8 @@
import android.util.Slog;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

//Synthetic comment -- @@ -428,11 +431,10 @@
}

// If we got here, that means that the system is most likely hung.
            // First collect stack traces from all threads of the system process.
            // Then kill this process so that the system will restart.

final String name = (mCurrentMonitor != null) ?
mCurrentMonitor.getClass().getName() : "null";
EventLog.writeEvent(EventLogTags.WATCHDOG, name);

ArrayList<Integer> pids = new ArrayList<Integer>();
//Synthetic comment -- @@ -467,11 +469,15 @@
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
//Synthetic comment -- @@ -480,6 +486,50 @@
}
}

private File dumpKernelStackTraces() {
String tracesPath = SystemProperties.get("dalvik.vm.stack-trace-file", null);
if (tracesPath == null || tracesPath.length() == 0) {







