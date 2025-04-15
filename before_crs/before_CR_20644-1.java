/*frameworks/base: Watchdog to generate trace of ActivityController

Change-Id:Iaaede499d1cc07edd85e73d02283db55c0f45d41*/
//Synthetic comment -- diff --git a/services/java/com/android/server/Watchdog.java b/services/java/com/android/server/Watchdog.java
//Synthetic comment -- index 1b885f5..2dd21c6 100644

//Synthetic comment -- @@ -83,6 +83,7 @@
Monitor mCurrentMonitor;

int mPhonePid;

final Calendar mCalendar = Calendar.getInstance();
int mMinScreenOff = MEMCHECK_DEFAULT_MIN_SCREEN_OFF;
//Synthetic comment -- @@ -206,6 +207,9 @@
if ("com.android.phone".equals(name)) {
mPhonePid = pid;
}
}
}

//Synthetic comment -- @@ -432,6 +436,7 @@
ArrayList<Integer> pids = new ArrayList<Integer>();
pids.add(Process.myPid());
if (mPhonePid > 0) pids.add(mPhonePid);
// Pass !waitedHalf so that just in case we somehow wind up here without having
// dumped the halfway stacks, we properly re-initialize the trace file.
File stack = ActivityManagerService.dumpStackTraces(!waitedHalf, pids, null, null);








//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 2322ee1..5b75945 100644

//Synthetic comment -- @@ -5853,6 +5853,10 @@
public void setActivityController(IActivityController controller) {
enforceCallingPermission(android.Manifest.permission.SET_ACTIVITY_WATCHER,
"setActivityController()");
synchronized (this) {
mController = controller;
}







