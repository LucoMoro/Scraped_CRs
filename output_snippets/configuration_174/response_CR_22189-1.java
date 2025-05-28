<<Beginning of snippet n. 0>>
proc = mService.mProcessNames.get(r.processName, r.info.applicationInfo.uid);
if (proc == null || proc.thread == null) {
    if (!isAnyActivityOngoing() && canShowStartingIcon()) {
        showStartingIcon = true;
    }
}
if (DEBUG_TRANSITION) Slog.v(TAG,
"Prepare open transition: starting " + r);

// Utility method to check for ongoing activities
private boolean isAnyActivityOngoing() {
    // Assuming ongoingActivities is a List or Set that tracks active activities
    return ongoingActivities.size() > 0; // Actual check for ongoing activities 
}

// Throttle mechanism to control starting icon display
private long lastIconShownTime = 0;

private boolean canShowStartingIcon() {
    long currentTime = System.currentTimeMillis();
    if (currentTime - lastIconShownTime > 2000) { // Show every 2 seconds
        lastIconShownTime = currentTime;
        return true;
    }
    return false;
}
<<End of snippet n. 0>>