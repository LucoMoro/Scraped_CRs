//<Beginning of snippet n. 0>

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.util.Slog;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

final String name = (mCurrentMonitor != null) ? mCurrentMonitor.getClass().getName() : "null";
EventLog.writeEvent(EventLogTags.WATCHDOG, name);

ArrayList<Integer> pids = new ArrayList<Integer>();
dropboxThread.join(2000);  // wait up to 2 seconds for it to return.
} catch (InterruptedException ignored) {}

// Collect additional logging information before crashing.
Slog.w(TAG, "Current thread state: " + Thread.currentThread().getState());
Slog.w(TAG, "Current system time: " + Calendar.getInstance().getTime());

boolean isEngOrUserDebug = Build.TYPE.equals("eng") || Build.TYPE.equals("userdebug");

// If we have detected that the system is likely hung, force a crash for debugging.
if (isEngOrUserDebug) {
    Slog.w(TAG, "*** FORCING CRASH FOR DEBUGGING in build: " + Build.TYPE);
    throw new RuntimeException("Forced crash");
}

// Only kill the process if the debugger is not attached.
if (!Debug.isDebuggerConnected()) {
    Slog.w(TAG, "*** WATCHDOG KILLING SYSTEM PROCESS: " + name);
    Process.killProcess(Process.myPid());
    System.exit(10);
} else {
    Slog.w(TAG, "Debugger connected: Watchdog is *not* killing the system process");
}
}

private File dumpKernelStackTraces() {
    String tracesPath = SystemProperties.get("dalvik.vm.stack-trace-file", null);
    if (tracesPath == null || tracesPath.length() == 0) {
//<End of snippet n. 0>