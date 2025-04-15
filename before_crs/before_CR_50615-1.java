/*Restore CtsVerifier rate for passive updates

CtsVerifier used to allow update rates up to 10 times as small as the
requested rate for passive location. Restore the rate for this case.

Change-Id:I10515511cb0dc3b892948c122d7b37a6c8f91235*/
//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/location/LocationVerifier.java b/apps/CtsVerifier/src/com/android/cts/verifier/location/LocationVerifier.java
//Synthetic comment -- index b6d2a9e..bcd49ea 100644

//Synthetic comment -- @@ -36,8 +36,12 @@
private static final int NUM_IGNORED_UPDATES = 2;

/* The mean computed for the deltas should not be smaller
     * than mInterval * MIN_MEAN_RATIO */
    private static final double MIN_MEAN_RATIO = 0.75;

/**
* The standard deviation computed for the deltas should not be bigger
//Synthetic comment -- @@ -90,8 +94,8 @@
mCb.log("active " + mProvider + " update (" + delta + "ms)");

if (mNumActiveUpdates >= mRequestedUpdates) {
                assertMeanAndStdev(mProvider, mActiveDeltas);
                assertMeanAndStdev(LocationManager.PASSIVE_PROVIDER, mPassiveDeltas);
pass();
}
}
//Synthetic comment -- @@ -104,11 +108,11 @@
public void onProviderDisabled(String provider) { }
}

    private void assertMeanAndStdev(String provider, List<Long> deltas) {
double mean = computeMean(deltas);
double stdev = computeStdev(mean, deltas);

        double minMean = mInterval * MIN_MEAN_RATIO;
if (mean < minMean) {
fail(provider + " provider mean too small: " + mean
+ " (min: " + minMean + ")");







