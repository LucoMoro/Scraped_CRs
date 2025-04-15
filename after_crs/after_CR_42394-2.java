/*Filter out the overdue location fail case in GPS test

Occasionally, passive listeners receive a overdue location.
The overdue location comes from previous test round, and it will
confuse calculation of time interval. Finally, the confused interval
is mistaken for a FAIL even though GPS works normally. For this reason,
we have to filter out the overdue location at beginning of test round.

Change-Id:I72bfc08b458e99805f60e8caac3c2594c77ad93f*/




//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/location/LocationVerifier.java b/apps/CtsVerifier/src/com/android/cts/verifier/location/LocationVerifier.java
//Synthetic comment -- index fd226a6..4bc0355 100644

//Synthetic comment -- @@ -45,12 +45,14 @@
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

//Synthetic comment -- @@ -93,6 +95,11 @@
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
//Synthetic comment -- @@ -186,4 +193,4 @@
mProvider + " location change");
return true;
}
\ No newline at end of file
}







