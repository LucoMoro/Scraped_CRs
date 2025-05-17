//<Beginning of snippet n. 0>
private int mNumActiveUpdates = 0;
private int mNumPassiveUpdates = 0;
private boolean mRunning = false;
private static final long OVERDUE_THRESHOLD = 60000; // Example threshold of 60 seconds

private boolean isLocationOverdue(Location location) {
    return location.getTime() < System.currentTimeMillis() - OVERDUE_THRESHOLD;
}

private class ActiveListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        if (!mRunning) return;
        if (isLocationOverdue(location)) return;

        mNumActiveUpdates++;
        scheduleTimeout();
    }
}

private class PassiveListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        if (!mRunning) return;
        if (!location.getProvider().equals(mProvider)) return;
        if (isLocationOverdue(location)) return;

        mNumPassiveUpdates++;
        log(mProvider + " location change");
    }
}
//<End of snippet n. 0>