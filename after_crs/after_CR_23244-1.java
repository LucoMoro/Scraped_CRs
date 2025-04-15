/*Watchdog: Improvement of debuggability

If the watchdog detects a problem the system server process
is killed, that is followed by a crash. Because the crash is
done after the system server process is killed, the crash
don't contain info about the system server.
This improvement will make sure that the system is crashed
before the system server process is killed.
Behavior is only changed for eng and userdebug builds.

Change-Id:I9f1c8fd8b03d0114032ed44fb582705ad0b49733*/




//Synthetic comment -- diff --git a/services/java/com/android/server/Watchdog.java b/services/java/com/android/server/Watchdog.java
//Synthetic comment -- index 2a25c2a..06fc853 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
//Synthetic comment -- @@ -39,6 +40,8 @@
import android.util.Slog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

//Synthetic comment -- @@ -422,11 +425,11 @@
}

// If we got here, that means that the system is most likely hung.

final String name = (mCurrentMonitor != null) ?
mCurrentMonitor.getClass().getName() : "null";

            Slog.w(TAG, "WATCHDOG PROBLEM IN SYSTEM SERVER: " + name);
EventLog.writeEvent(EventLogTags.WATCHDOG, name);

ArrayList<Integer> pids = new ArrayList<Integer>();
//Synthetic comment -- @@ -460,11 +463,21 @@
dropboxThread.join(2000);  // wait up to 2 seconds for it to return.
} catch (InterruptedException ignored) {}

            // Only kill/crash if the debugger is not attached.
if (!Debug.isDebuggerConnected()) {
                if (Build.TYPE.equals("eng") || Build.TYPE.equals("userdebug")) {
                    Slog.w(TAG, "*** WATCHDOG KILLING THE SYSTEM: " + name);

                    // Give some extra time to make sure CrashMonitorService reacts to
                    // the dropbox entry before the crash
                    SystemClock.sleep(2000);

                    forceCrashDump();
                } else {
                    Slog.w(TAG, "*** WATCHDOG KILLING SYSTEM PROCESS: " + name);
                    Process.killProcess(Process.myPid());
                    System.exit(10);
                }
} else {
Slog.w(TAG, "Debugger connected: Watchdog is *not* killing the system process");
}
//Synthetic comment -- @@ -473,6 +486,24 @@
}
}

    private void forceCrashDump() {
        FileWriter out = null;
        try {
            out = new FileWriter("/proc/sysrq-trigger");
            out.write("c");
        } catch (IOException e) {
            Slog.e(TAG, "Trigger crash dump: " + e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    Slog.e(TAG, "Trigger crash dump: " + e);
                }
            }
        }
    }

private File dumpKernelStackTraces() {
String tracesPath = SystemProperties.get("dalvik.vm.stack-trace-file", null);
if (tracesPath == null || tracesPath.length() == 0) {







