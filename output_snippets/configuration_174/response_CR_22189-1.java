<<Beginning of snippet n. 0>>
proc = mService.mProcessNames.get(r.processName, r.info.applicationInfo.uid);
if (proc == null || proc.thread == null) {
    if (!isActivityRunningForTask(r.taskId)) {
        showStartingIcon = true;
    }
}
if (DEBUG_TRANSITION) Slog.v(TAG, "Prepare open transition: starting " + r);
<<End of snippet n. 0>>