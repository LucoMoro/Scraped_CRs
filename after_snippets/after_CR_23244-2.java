
//<Beginning of snippet n. 0>


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.util.Slog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

}

// If we got here, that means that the system is most likely hung.

final String name = (mCurrentMonitor != null) ?
mCurrentMonitor.getClass().getName() : "null";
            Slog.w(TAG, "WATCHDOG PROBLEM IN SYSTEM SERVER: " + name);
EventLog.writeEvent(EventLogTags.WATCHDOG, name);

ArrayList<Integer> pids = new ArrayList<Integer>();
dropboxThread.join(2000);  // wait up to 2 seconds for it to return.
} catch (InterruptedException ignored) {}

            // Only kill/crash the process if the debugger is not attached.
if (!Debug.isDebuggerConnected()) {
Slog.w(TAG, "*** WATCHDOG KILLING SYSTEM PROCESS: " + name);
                if (Build.TYPE.equals("eng") || Build.TYPE.equals("userdebug")) {
                    forceCrashDump();
                } else {
                    Process.killProcess(Process.myPid());
                    System.exit(10);
                }
} else {
Slog.w(TAG, "Debugger connected: Watchdog is *not* killing the system process");
}
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
                // 'sync' command always success, and this function returns 0.
                p.waitFor();
            } else {
                Slog.e(TAG, "Failed to execute 'sync' command.");
            }
        } catch (Exception e) {
            // This code is an emergency path to crash MUT. The system already
            // caused fatal error, and then calls this function to create a
            // crash dump. This function must run below code to crash, even if
            // failed to execute sync command.
            // Therefore, ignore all exception, here.
            Slog.e(TAG, "Failed to execute 'sync' command : " + e);
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

//<End of snippet n. 0>








