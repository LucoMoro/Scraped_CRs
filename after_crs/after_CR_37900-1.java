/*Fix sluggish to launch an app

The new trace feature/code in 4.0.4, which will dump the app stack
trace every 500ms if an app can't be launched within 500ms, is
holding the global ActivityManagerService lock unnecessarily. It
slows down the app's startup.

Change-Id:Id3a87fc939d9a2b4ce23a14a24956c045f36cb7dSolution: move the trace file write operations out of sync block.*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 6190a63..b6a2f42 100644

//Synthetic comment -- @@ -2990,7 +2990,7 @@
}
}

    final void logAppTooSlow(int pid, long startTime, String msg) {
if (IS_USER_BUILD) {
return;
}
//Synthetic comment -- @@ -3023,7 +3023,7 @@
sb.append(msg);
FileOutputStream fos = new FileOutputStream(tracesFile);
fos.write(sb.toString().getBytes());
                if (pid <= 0) {
fos.write("\n*** No application process!".getBytes());
}
fos.close();
//Synthetic comment -- @@ -3033,9 +3033,9 @@
return;
}

            if (pid > 0) {
ArrayList<Integer> firstPids = new ArrayList<Integer>();
                firstPids.add(pid);
dumpStackTraces(tracesPath, firstPids, null, null);
}









//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityStack.java b/services/java/com/android/server/am/ActivityStack.java
//Synthetic comment -- index 86d3a1a..d581d33 100755

//Synthetic comment -- @@ -307,11 +307,18 @@
// We don't at this point know if the activity is fullscreen,
// so we need to be conservative and assume it isn't.
Slog.w(TAG, "Activity pause timeout for " + r);
                    int pid = -1;
                    long pauseTime = 0;
                    String m = null;
synchronized (mService) {
if (r.app != null) {
                            pid = r.app.pid;
}
                        pauseTime = r.pauseTime;
                        m = "pausing " + r;
                    }
                    if (pid > 0) {
                        mService.logAppTooSlow(pid, pauseTime, m);
}

activityPaused(r != null ? r.appToken : null, true);
//Synthetic comment -- @@ -332,12 +339,21 @@
} break;
case LAUNCH_TICK_MSG: {
ActivityRecord r = (ActivityRecord)msg.obj;
                    int pid = -1;
                    long launchTickTime = 0;
                    String m = null;
synchronized (mService) {
if (r.continueLaunchTickingLocked()) {
                            if (r.app != null) {
                                pid = r.app.pid;
                            }
                            launchTickTime = r.launchTickTime;
                            m = "launching " + r;
}
}
                    if (pid > 0) {
                        mService.logAppTooSlow(pid, launchTickTime, m);
                    }
} break;
case DESTROY_TIMEOUT_MSG: {
ActivityRecord r = (ActivityRecord)msg.obj;







