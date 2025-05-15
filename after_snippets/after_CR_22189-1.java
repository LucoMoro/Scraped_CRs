
//<Beginning of snippet n. 0>


proc = mService.mProcessNames.get(r.processName, r.info.applicationInfo.uid);
}
if (proc == null || proc.thread == null) {
                if (!newTask && mResumedActivity == null) {
                    // This is a special case when stressing the framework
                    // by starting many activities fast in the same task.
                    // Since mResumedActivity is null, we can assume that we are
                    // in transition and we already have set showStartingIcon to true.
                    showStartingIcon = false;
                } else {
                    showStartingIcon = true;
                }
}
if (DEBUG_TRANSITION) Slog.v(TAG,
"Prepare open transition: starting " + r);

//<End of snippet n. 0>








