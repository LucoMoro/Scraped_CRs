//<Beginning of snippet n. 0>

private int mNumActiveUpdates = 0;
private int mNumPassiveUpdates = 0;
private boolean mRunning = false;
private long OVERDUE_THRESHOLD = 60000; // Example threshold: 1 minute in milliseconds

private class ActiveListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        if (!mRunning) return;

        // Check if location is overdue
        if (System.currentTimeMillis() - location.getTime() > OVERDUE_THRESHOLD) {
            return;
        }

        mNumActiveUpdates++;
        scheduleTimeout();
    }
}

private class PassiveListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        if (!mRunning) return;
        if (!location.getProvider().equals(mProvider)) return;

        // Check if location is overdue
        if (System.currentTimeMillis() - location.getTime() > OVERDUE_THRESHOLD) {
            return;
        }

        mNumPassiveUpdates++;
        Log.d(mProvider, " location change");
    }
}

//<End of snippet n. 0>