//<Beginning of snippet n. 0>

private int mNumActiveUpdates = 0;
private int mNumPassiveUpdates = 0;
private boolean mRunning = false;

private class ActiveListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        if (!mRunning) return;
        if (isLocationOverdue(location)) return;

        mNumActiveUpdates++;
        scheduleTimeout();
    }

    private boolean isLocationOverdue(Location location) {
        long currentTime = System.currentTimeMillis();
        long threshold = 60000; // e.g., 60 seconds threshold
        return (currentTime - location.getTime()) > threshold;
    }
}

private class PassiveListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        if (!mRunning) return;
        if (!location.getProvider().equals(mProvider)) return;
        if (isLocationOverdue(location)) return;

        mNumPassiveUpdates++;
        Log.d(mProvider, "location change");
    }

    private boolean isLocationOverdue(Location location) {
        long currentTime = System.currentTimeMillis();
        long threshold = 60000; // e.g., 60 seconds threshold
        return (currentTime - location.getTime()) > threshold;
    }
}

//<End of snippet n. 0>