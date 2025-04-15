/*Catch all the stack info as soon as possible when ANR&UIWDT happen

curently, when UIWDT and ANR happend, only catch JAVA stack.
it's hard to debug such issue. Actually, ANR and UIWDT issue always
means issue happended in user space or kernel space,so we catch all
stack info when ANR&UIWDT happend, it's useful for us to debug ANR&UIWDT
issue

Change-Id:I761d3a6a861f57bea6a977064d1103d5e8b57679Author: Xiaobing Tu <xiaobing.tu@intel.com>
Signed-off-by: Xiaobing Tu <xiaobing.tu@intel.com>
Signed-off-by: Xuemin Su <xuemin.su@intel.com>
Signed-off-by: Xindong Ma <xindong.ma@intel.com>
Signed-off-by: Dongxing Zhang <dongxing.zhang@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ:37815*/
//Synthetic comment -- diff --git a/services/java/com/android/server/Watchdog.java b/services/java/com/android/server/Watchdog.java
//Synthetic comment -- index c239382..c8d2321 100644

//Synthetic comment -- @@ -41,6 +41,8 @@
import java.util.ArrayList;
import java.util.Calendar;

/** This class calls its monitor every minute. Killing this process if they don't return **/
public class Watchdog extends Thread {
static final String TAG = "Watchdog";
//Synthetic comment -- @@ -106,7 +108,7 @@
int mReqMinScreenOff = -1;    // >= 0 if a specific screen off time has been requested
int mReqMinNextAlarm = -1;    // >= 0 if specific time to next alarm has been requested
int mReqRecheckInterval= -1;  // >= 0 if a specific recheck interval has been requested

/**
* Used for scheduling monitor callbacks and checking memory usage.
*/
//Synthetic comment -- @@ -430,6 +432,16 @@
// If we got here, that means that the system is most likely hung.
// First collect stack traces from all threads of the system process.
// Then kill this process so that the system will restart.

final String name = (mCurrentMonitor != null) ?
mCurrentMonitor.getClass().getName() : "null";
//Synthetic comment -- @@ -456,12 +468,12 @@
// itself may be deadlocked.  (which has happened, causing this statement to
// deadlock and the watchdog as a whole to be ineffective)
Thread dropboxThread = new Thread("watchdogWriteToDropbox") {
                    public void run() {
                        mActivity.addErrorToDropBox(
                                "watchdog", null, "system_server", null, null,
                                name, null, stack, null);
                    }
                };
dropboxThread.start();
try {
dropboxThread.join(2000);  // wait up to 2 seconds for it to return.








//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 60085f4..127a3ea7 100644

//Synthetic comment -- @@ -157,6 +157,8 @@
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public final class ActivityManagerService extends ActivityManagerNative
implements Watchdog.Monitor, BatteryStatsImpl.BatteryCallback {
private static final String USER_DATA_DIR = "/data/user/";
//Synthetic comment -- @@ -1289,7 +1291,7 @@
}
dropBuilder.append(catSw.toString());
addErrorToDropBox("lowmem", null, "system_server", null,
                                null, tag.toString(), dropBuilder.toString(), null, null);
Slog.i(TAG, logBuilder.toString());
synchronized (ActivityManagerService.this) {
long now = SystemClock.uptimeMillis();
//Synthetic comment -- @@ -3043,6 +3045,9 @@
return null;
}

dumpStackTraces(tracesPath, firstPids, processStats, lastPids, nativeProcs);
return tracesFile;
}
//Synthetic comment -- @@ -3256,7 +3261,17 @@
}
}
}

// Log the ANR to the main log.
StringBuilder info = new StringBuilder();
info.setLength(0);
//Synthetic comment -- @@ -3293,9 +3308,8 @@
// There is no trace file, so dump (only) the alleged culprit's threads to the log
Process.sendSignal(app.pid, Process.SIGNAL_QUIT);
}

addErrorToDropBox("anr", app, app.processName, activity, parent, annotation,
                cpuInfo, tracesFile, null);

if (mController != null) {
try {
//Synthetic comment -- @@ -7877,7 +7891,7 @@
crashInfo.throwFileName,
crashInfo.throwLineNumber);

        addErrorToDropBox("crash", r, processName, null, null, null, null, null, crashInfo);

crashApplication(r, crashInfo);
}
//Synthetic comment -- @@ -8073,7 +8087,7 @@
r == null ? -1 : r.info.flags,
tag, crashInfo.exceptionMessage);

        addErrorToDropBox("wtf", r, processName, null, null, tag, null, null, crashInfo);

if (r != null && r.pid != Process.myPid() &&
Settings.Secure.getInt(mContext.getContentResolver(),
//Synthetic comment -- @@ -8179,7 +8193,7 @@
ProcessRecord process, String processName, ActivityRecord activity,
ActivityRecord parent, String subject,
final String report, final File logFile,
            final ApplicationErrorReport.CrashInfo crashInfo) {
// NOTE -- this must never acquire the ActivityManagerService lock,
// otherwise the watchdog may be prevented from resetting the system.

//Synthetic comment -- @@ -8191,6 +8205,9 @@
if (dbox == null || !dbox.isTagEnabled(dropboxTag)) return;

final StringBuilder sb = new StringBuilder(1024);
appendDropBoxProcessHeaders(process, processName, sb);
if (activity != null) {
sb.append("Activity: ").append(activity.shortComponentName).append("\n");








//Synthetic comment -- diff --git a/services/java/com/android/server/am/DebugAnr.java b/services/java/com/android/server/am/DebugAnr.java
new file mode 100644
//Synthetic comment -- index 0000000..d167b93

//Synthetic comment -- @@ -0,0 +1,17 @@







