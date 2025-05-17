//<Beginning of snippet n. 0>
proc = mService.mProcessNames.get(r.processName, r.info.applicationInfo.uid);
if (proc == null || (proc.thread == null && (activityCount == 0 || activityCount == 1))) {
    showStartingIcon = true;
}
activityCount++;
if (DEBUG_TRANSITION) Slog.v(TAG,
"Prepare open transition: starting " + r);
// Implement an activity end mechanism to decrement activityCount as needed
//<End of snippet n. 0>