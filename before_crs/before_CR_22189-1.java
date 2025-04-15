/*Display not updated when starting new activities in the same task

The problem occurs when the activity process has not yet been
started and we keep starting new activities in an existing
task. Make sure to not ask for showStartingIcon to be shown
when many activities are started fast in the same task.

Change-Id:I14a4c1394cab23cf9a7687d486cfc3089efbd5e7*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityStack.java b/services/java/com/android/server/am/ActivityStack.java
//Synthetic comment -- index 463493b..8e5f1a9 100644

//Synthetic comment -- @@ -1413,7 +1413,15 @@
proc = mService.mProcessNames.get(r.processName, r.info.applicationInfo.uid);
}
if (proc == null || proc.thread == null) {
                showStartingIcon = true;
}
if (DEBUG_TRANSITION) Slog.v(TAG,
"Prepare open transition: starting " + r);







