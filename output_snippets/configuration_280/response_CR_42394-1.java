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
        return (currentTime - location.getTime()) > MAX_LOCATION_AGE; // Define MAX_LOCATION_AGE as per your requirement
    }
}

private class PassiveListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        if (!mRunning) return;
        if (isLocationOverdue(location)) return;
        if (!location.getProvider().equals(mProvider)) return;

        mNumPassiveUpdates++;
        // Handle passive location change
        System.out.println(mProvider + " location change");
    }

    private boolean isLocationOverdue(Location location) {
        long currentTime = System.currentTimeMillis();
        return (currentTime - location.getTime()) > MAX_LOCATION_AGE; // Define MAX_LOCATION_AGE as per your requirement
    }
}
//<End of snippet n. 0>