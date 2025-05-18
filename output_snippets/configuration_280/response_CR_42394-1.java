//<Beginning of snippet n. 0>

private int mNumActiveUpdates = 0;
private int mNumPassiveUpdates = 0;
private boolean mRunning = false;
private static final long OVERDUE_THRESHOLD = 60000; // 1 minute in milliseconds

private class ActiveListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        if (!mRunning) return;
        if (isLocationOverdue(location)) return;

        mNumActiveUpdates++;
        scheduleTimeout();
        // Process the active location change logic here
    }

    private boolean isLocationOverdue(Location location) {
        return System.currentTimeMillis() - location.getTime() > OVERDUE_THRESHOLD;
    }
}

private class PassiveListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        if (!mRunning) return;
        if (isLocationOverdue(location)) return;
        if (!location.getProvider().equals(mProvider)) return;

        mNumPassiveUpdates++;
        // Process the passive location change logic here
    }
}

//<End of snippet n. 0>