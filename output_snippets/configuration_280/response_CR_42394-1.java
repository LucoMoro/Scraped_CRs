//<Beginning of snippet n. 0>


private int mNumActiveUpdates = 0;
private int mNumPassiveUpdates = 0;
private boolean mRunning = false;
private long overdueThreshold = 30000; // example threshold in milliseconds

private class ActiveListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        if (!mRunning) return;
        if (isLocationOverdue(location)) return;

        mNumActiveUpdates++;
        scheduleTimeout();
    }

    private boolean isLocationOverdue(Location location) {
        return (System.currentTimeMillis() - location.getTime()) > overdueThreshold;
    }
}

private class PassiveListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        if (!mRunning) return;
        if (!location.getProvider().equals(mProvider)) return;
        if (isLocationOverdue(location)) return;

        mNumPassiveUpdates++;
        // Assuming there should be some log here
        System.out.println(mProvider + " location change");
    }
}

//<End of snippet n. 0>