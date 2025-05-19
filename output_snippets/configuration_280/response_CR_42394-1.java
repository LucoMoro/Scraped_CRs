//<Beginning of snippet n. 0>


private int mNumActiveUpdates = 0;
private int mNumPassiveUpdates = 0;
private boolean mRunning = false;

private class LocationListenerImpl implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        if (isOverdue(location)) return;
        if (!mRunning) return;

        if (location.getProvider().equals(mProvider)) {
            mNumPassiveUpdates++;
        } else {
            mNumActiveUpdates++;
            scheduleTimeout();
        }
    }

    private boolean isOverdue(Location location) {
        long currentTime = System.currentTimeMillis();
        return (currentTime - location.getTime()) > TIMEOUT_THRESHOLD;
    }
}
//<End of snippet n. 0>