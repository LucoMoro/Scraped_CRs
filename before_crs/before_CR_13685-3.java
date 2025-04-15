/*Fix problem with restarting an application process that recently has died.

There exists a race condition when starting a process that recently has died.
If the ActivityManager receives the death notification for the died process
after the new process has been started but before an application thread has
been attached to the new process will the newly created process be removed
during the cleanup of the died process. If this happens when sending a broadcast
could it result in an ANR.

This is solved by doing the clean up before starting a new process that uses
the same process record.*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 1a22a26a..5f6356d 100644

//Synthetic comment -- @@ -1905,11 +1905,16 @@
+ " app=" + app + " knownToBeDead=" + knownToBeDead
+ " thread=" + (app != null ? app.thread : null)
+ " pid=" + (app != null ? app.pid : -1));
        if (app != null &&
                (!knownToBeDead || app.thread == null) && app.pid > 0) {
            return app;
}
        
String hostingNameStr = hostingName != null
? hostingName.flattenToShortString() : null;

//Synthetic comment -- @@ -4553,7 +4558,9 @@

mProcDeaths[0]++;

        if (app.thread != null && app.thread.asBinder() == thread.asBinder()) {
Log.i(TAG, "Process " + app.processName + " (pid " + pid
+ ") has died.");
EventLog.writeEvent(LOG_AM_PROCESS_DIED, app.pid, app.processName);
//Synthetic comment -- @@ -4603,6 +4610,11 @@
scheduleAppGcsLocked();
}
}
} else if (Config.LOGD) {
Log.d(TAG, "Received spurious death notification for thread "
+ thread.asBinder());
//Synthetic comment -- @@ -5424,6 +5436,8 @@
finishReceiverLocked(br.receiver, br.resultCode, br.resultData,
br.resultExtras, br.resultAbort, true);
scheduleBroadcastsLocked();
}
}








