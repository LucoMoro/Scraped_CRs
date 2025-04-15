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
//Synthetic comment -- index 40283a8..67ceeb5 100644

//Synthetic comment -- @@ -1897,11 +1897,16 @@
+ " app=" + app + " knownToBeDead=" + knownToBeDead
+ " thread=" + (app != null ? app.thread : null)
+ " pid=" + (app != null ? app.pid : -1));
        if (app != null && app.pid > 0) {
            if (!knownToBeDead || app.thread == null) {
                return app;
            } else {
	        // An application record is attached to a previous process,
	        // clean it up now.
	        handleAppDiedLocked(app, true);
            }
}

String hostingNameStr = hostingName != null
? hostingName.flattenToShortString() : null;

//Synthetic comment -- @@ -4545,7 +4550,9 @@

mProcDeaths[0]++;

        // Clean up already done if the process has been re-started.
        if (app.pid == pid && app.thread != null &&
                app.thread.asBinder() == thread.asBinder()) {
Log.i(TAG, "Process " + app.processName + " (pid " + pid
+ ") has died.");
EventLog.writeEvent(LOG_AM_PROCESS_DIED, app.pid, app.processName);
//Synthetic comment -- @@ -4595,6 +4602,11 @@
scheduleAppGcsLocked();
}
}
        } else if (app.pid != pid) {
            // A new process has already been started.
            Log.i(TAG, "Process " + app.processName + " (pid " + pid
                    + ") has died and restarted (pid " + app.pid + ").");
            EventLog.writeEvent(LOG_AM_PROCESS_DIED, pid, app.processName);
} else if (Config.LOGD) {
Log.d(TAG, "Received spurious death notification for thread "
+ thread.asBinder());
//Synthetic comment -- @@ -5401,6 +5413,8 @@
finishReceiverLocked(br.receiver, br.resultCode, br.resultData,
br.resultExtras, br.resultAbort, true);
scheduleBroadcastsLocked();
                // We need to reset the state if we fails to start the receiver.
                br.state = BroadcastRecord.IDLE;
}
}








