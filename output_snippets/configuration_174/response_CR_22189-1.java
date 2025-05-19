//<Beginning of snippet n. 0>
long lastActivityTimestamp = 0;
final long ACTIVITY_THRESHOLD = 500;
int activityCounter = 0;
final Object lock = new Object();

proc = mService.mProcessNames.get(r.processName, r.info.applicationInfo.uid);
long currentTime = System.currentTimeMillis();

synchronized (lock) {
    if (proc == null || proc.thread == null) {
        if (activityCounter == 0 && currentTime - lastActivityTimestamp > ACTIVITY_THRESHOLD) {
            showStartingIcon = true;
        }
        if (activityCounter == 0) {
            lastActivityTimestamp = currentTime;
        }
        activityCounter++;
    }
}

if (DEBUG_TRANSITION) Slog.v(TAG, "Prepare open transition: starting " + r);

// Reset the counter when the activity has completed
synchronized (lock) {
    if (activityCounter > 0) {
        activityCounter--;
        if (activityCounter == 0) {
            showStartingIcon = false; // Reset showStartingIcon when no active activities
        }
    }
}
//<End of snippet n. 0>