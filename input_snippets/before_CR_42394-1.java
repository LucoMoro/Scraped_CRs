
//<Beginning of snippet n. 0>


private int mNumActiveUpdates = 0;
private int mNumPassiveUpdates = 0;
private boolean mRunning = false;

private class ActiveListener implements LocationListener {
@Override
public void onLocationChanged(Location location) {
if (!mRunning) return;

mNumActiveUpdates++;
scheduleTimeout();

@Override
public void onLocationChanged(Location location) {
if (!mRunning) return;
if (!location.getProvider().equals(mProvider)) return;

mNumPassiveUpdates++;
mProvider + " location change");
return true;
}
}
\ No newline at end of file

//<End of snippet n. 0>








