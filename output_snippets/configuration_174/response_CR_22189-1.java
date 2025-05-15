//<Beginning of snippet n. 0>

int rapidStartCounter = 0;
final int rapidStartThreshold = 5; // Example threshold
Handler handler = new Handler(Looper.getMainLooper());
Runnable resetCounter = () -> rapidStartCounter = 0;

proc = mService.mProcessNames.get(r.processName, r.info.applicationInfo.uid);

if (proc == null || proc.thread == null) {
    rapidStartCounter++;
    if (rapidStartCounter <= rapidStartThreshold) {
        showStartingIcon = true;
        handler.postDelayed(resetCounter, 2000); // Reset after a delay
    } else {
        showStartingIcon = false; // Don't show if rapid starts exceed threshold
    }
}

if (DEBUG_TRANSITION) Slog.v(TAG, "Prepare open transition: starting " + r);

//<End of snippet n. 0>