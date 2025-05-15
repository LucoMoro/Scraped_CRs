
//<Beginning of snippet n. 0>


private int mNumActiveUpdates = 0;
private int mNumPassiveUpdates = 0;
private boolean mRunning = false;
    private boolean mActiveLocationArrive = false;

private class ActiveListener implements LocationListener {
@Override
public void onLocationChanged(Location location) {
if (!mRunning) return;

            mActiveLocationArrive = true;
mNumActiveUpdates++;
scheduleTimeout();

@Override
public void onLocationChanged(Location location) {
if (!mRunning) return;
            // When a test round start, passive listener shouldn't recevice location before active listener.
            // If this situation occurs, we treat this location as overdue location.
            // (The overdue location comes from previous test round, it occurs occasionally)
            // We have to skip it to prevent wrong calculation of time interval.
            if (!mActiveLocationArrive) return;
if (!location.getProvider().equals(mProvider)) return;

mNumPassiveUpdates++;
mProvider + " location change");
return true;
}
\ No newline at end of file
}

//<End of snippet n. 0>








