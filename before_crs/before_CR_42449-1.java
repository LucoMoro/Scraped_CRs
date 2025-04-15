/*Revert "Watchdog: Improvement of debuggability"

This reverts commit 9211b13c3268035b0da0c51ed2d6d5a578d45ff3.*/
//Synthetic comment -- diff --git a/services/java/com/android/server/Watchdog.java b/services/java/com/android/server/Watchdog.java
//Synthetic comment -- index c14e2f6..c239382 100644

//Synthetic comment -- @@ -25,7 +25,6 @@
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
//Synthetic comment -- @@ -39,8 +38,6 @@
import android.util.Slog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

//Synthetic comment -- @@ -431,10 +428,11 @@
}

// If we got here, that means that the system is most likely hung.

final String name = (mCurrentMonitor != null) ?
mCurrentMonitor.getClass().getName() : "null";
            Slog.w(TAG, "WATCHDOG PROBLEM IN SYSTEM SERVER: " + name);
EventLog.writeEvent(EventLogTags.WATCHDOG, name);

ArrayList<Integer> pids = new ArrayList<Integer>();
//Synthetic comment -- @@ -469,15 +467,11 @@
dropboxThread.join(2000);  // wait up to 2 seconds for it to return.
} catch (InterruptedException ignored) {}

            // Only kill/crash the process if the debugger is not attached.
if (!Debug.isDebuggerConnected()) {
Slog.w(TAG, "*** WATCHDOG KILLING SYSTEM PROCESS: " + name);
                if (!Build.TYPE.equals("user")) {
                    forceCrashDump();
                } else {
                    Process.killProcess(Process.myPid());
                    System.exit(10);
                }
} else {
Slog.w(TAG, "Debugger connected: Watchdog is *not* killing the system process");
}
//Synthetic comment -- @@ -486,50 +480,6 @@
}
}

    private void forceCrashDump() {
        /* Sync file system to flash the data which is written just before the
         * crash.
         */
        java.lang.Process p = null;
        try {
            p = Runtime.getRuntime().exec("sync");
            if (p != null) {
                // It is not necessary to check the exit code, here.
                // 'sync' command always succeeds, and this function returns 0.
                p.waitFor();
            } else {
                Slog.e(TAG, "Failed to execute 'sync' command. (no process handle)");
            }
        } catch (Exception e) {
            // This code is an emergency path to crash MUT. The system already
            // caused fatal error, and then calls this function to create a
            // crash dump. This function must run the code below to force a
            // crash, even if the sync command failed.
            // Therefore, ignore all exceptions, here.
            Slog.e(TAG, "Failed to execute 'sync' command prior to forcing crash: " + e);
        } finally {
            if (p != null) {
                p.destroy();
            }
        }

        FileWriter out = null;
        try {
            out = new FileWriter("/proc/sysrq-trigger");
            out.write("c");
        } catch (IOException e) {
            Slog.e(TAG, "Failed to write to sysrq-trigger while triggering crash: " + e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    Slog.e(TAG, "Failed to close sysrq-trigger while triggering crash: " + e);
                }
            }
        }
    }

private File dumpKernelStackTraces() {
String tracesPath = SystemProperties.get("dalvik.vm.stack-trace-file", null);
if (tracesPath == null || tracesPath.length() == 0) {







