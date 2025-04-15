/*DO NOT MERGE Filter out the overdue location cases in GPS test

Bug 7109051

This was merged into jb-mr1 already.

Occasionally, passive listeners receive a overdue location.
The overdue location comes from previous test round, and it will
confuse calculation of time interval. Finally, the confused interval
is mistaken for a FAIL even though GPS works normally. For this reason,
we have to filter out the overdue location at beginning of test round.
We log that the overdue location was ignored.

Change-Id:I72bfc08b458e99805f60e8caac3c2594c77ad93f*/
//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/location/LocationVerifier.java b/apps/CtsVerifier/src/com/android/cts/verifier/location/LocationVerifier.java
//Synthetic comment -- index fd226a6..6226cf6 100644

//Synthetic comment -- @@ -45,12 +45,14 @@
private int mNumActiveUpdates = 0;
private int mNumPassiveUpdates = 0;
private boolean mRunning = false;

private class ActiveListener implements LocationListener {
@Override
public void onLocationChanged(Location location) {
if (!mRunning) return;

mNumActiveUpdates++;
scheduleTimeout();

//Synthetic comment -- @@ -95,6 +97,15 @@
if (!mRunning) return;
if (!location.getProvider().equals(mProvider)) return;

mNumPassiveUpdates++;
long timestamp = location.getTime();
long delta = timestamp - mLastPassiveTimestamp;
//Synthetic comment -- @@ -186,4 +197,4 @@
mProvider + " location change");
return true;
}
}
\ No newline at end of file







