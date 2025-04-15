/*DO NOT SUBMIT - Assert mean and stdv of update deltas

Experiment: assert mean and standard deviation of location update deltas
in GPS CTS test.

Change-Id:If50b90d0b5575a83454ad74dd4d9961367a01177*/




//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/location/GpsTestActivity.java b/apps/CtsVerifier/src/com/android/cts/verifier/location/GpsTestActivity.java
//Synthetic comment -- index 31b5854..c3a1073 100644

//Synthetic comment -- @@ -95,25 +95,25 @@
case 1:
// Test GPS with minTime = 0
mLocationVerifier = new LocationVerifier(this, mLocationManager,
                        LocationManager.GPS_PROVIDER, 0, 8);
mLocationVerifier.start();
break;
case 2:
// Test GPS with minTime = 1s
mLocationVerifier = new LocationVerifier(this, mLocationManager,
                        LocationManager.GPS_PROVIDER, 1000, 8);
mLocationVerifier.start();
break;
case 3:
// Test GPS with minTime = 5s
mLocationVerifier = new LocationVerifier(this, mLocationManager,
                        LocationManager.GPS_PROVIDER, 5 * 1000, 8);
mLocationVerifier.start();
break;
case 4:
// Test GPS with minTime = 15s
mLocationVerifier = new LocationVerifier(this, mLocationManager,
                        LocationManager.GPS_PROVIDER, 15 * 1000, 8);
mLocationVerifier.start();
break;
case 5:








//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/location/LocationVerifier.java b/apps/CtsVerifier/src/com/android/cts/verifier/location/LocationVerifier.java
//Synthetic comment -- index be0ef01..c9504d2 100644

//Synthetic comment -- @@ -23,23 +23,41 @@
import android.os.Handler;
import android.os.Message;

import java.util.List;
import java.util.ArrayList;

public class LocationVerifier implements Handler.Callback {

public static final String TAG = "CtsVerifierLocation";

private static final int MSG_TIMEOUT = 1;

    /** Timing failures on first NUM_IGNORED_UPDATES updates are ignored. */
    private static final int NUM_IGNORED_UPDATES = 2;

    /* The mean computed for the deltas should not be smaller
     * than mInterval * ALLOWED_MEAN_ERROR_RATIO */
    private static final double ALLOWED_MEAN_ERROR_RATIO = 0.25;

    /**
     * The standard deviation computed for the deltas should not be bigger
     * than mInterval * ALLOWED_STD_ERROR_RATIO
     * or MIN_STD_MS, whichever is higher.
     */
    private static final double ALLOWED_STD_ERROR_RATIO = 0.50;
    private static final long MIN_STD_MS = 1000;

private final LocationManager mLocationManager;
private final PassFailLog mCb;
private final String mProvider;
private final long mInterval;
private final long mTimeout;
private final Handler mHandler;
private final int mRequestedUpdates;
private final ActiveListener mActiveListener;
private final PassiveListener mPassiveListener;

    private boolean isTestOutcomeSet = false;
private long mLastActiveTimestamp = -1;
private long mLastPassiveTimestamp = -1;
private int mNumActiveUpdates = 0;
//Synthetic comment -- @@ -47,6 +65,9 @@
private boolean mRunning = false;
private boolean mActiveLocationArrive = false;

    private List<Long> mActiveDeltas = new ArrayList();
    private List<Long> mPassiveDeltas = new ArrayList();

private class ActiveListener implements LocationListener {
@Override
public void onLocationChanged(Location location) {
//Synthetic comment -- @@ -60,33 +81,17 @@
long delta = timestamp - mLastActiveTimestamp;
mLastActiveTimestamp = timestamp;

            if (mNumActiveUpdates <= NUM_IGNORED_UPDATES ) {
                mCb.log("(ignored) active " + mProvider + " update (" + delta + "ms)");
return;
}

            mActiveDeltas.add(delta);
mCb.log("active " + mProvider + " update (" + delta + "ms)");

if (mNumActiveUpdates >= mRequestedUpdates) {
                assertMeanAndStd(mProvider, mActiveDeltas);
                assertMeanAndStd(LocationManager.PASSIVE_PROVIDER, mPassiveDeltas);
pass();
}
}
//Synthetic comment -- @@ -99,6 +104,44 @@
public void onProviderDisabled(String provider) { }
}

    private void assertMeanAndStd(String provider, List<Long> deltas) {
        double mean = computeMean(deltas);
        double std = computeStd(mean, deltas);

        double minMean = mInterval * (1 - ALLOWED_MEAN_ERROR_RATIO);
        if (mean < minMean) {
            fail(provider + " provider mean too small: " + mean
                 + " (min: " + minMean + ")");
            return;
        }

        double maxStd = Math.max(MIN_STD_MS, mInterval * ALLOWED_STD_ERROR_RATIO);
        if (std > maxStd) {
            fail (provider + " provider stdv too big: "
                  + std + " (max: " + maxStd + ")");
            return;
        }

        mCb.log(provider + " provider mean: " + mean);
        mCb.log(provider + " provider stdv: " + std);
    }

    private double computeMean(List<Long> deltas) {
        long accumulator = 0;
        for (Long d : deltas) {
            accumulator += d;
        }
        return accumulator / deltas.size();
    }

    private double computeStd(double mean, List<Long> deltas) {
        long accumulator = 0;
        for (Long d : deltas) {
            accumulator += (mean - d) * (mean -d);
        }
        return Math.sqrt(accumulator / (deltas.size() - 1));
    }

private class PassiveListener implements LocationListener {
@Override
public void onLocationChanged(Location location) {
//Synthetic comment -- @@ -119,25 +162,12 @@
long delta = timestamp - mLastPassiveTimestamp;
mLastPassiveTimestamp = timestamp;

            if (mNumPassiveUpdates <= NUM_IGNORED_UPDATES) {
                mCb.log("(ignored) passive " + mProvider + " update (" + delta + "ms)");
return;
}

            mPassiveDeltas.add(delta);
mCb.log("passive " + mProvider + " update (" + delta + "ms)");
}

//Synthetic comment -- @@ -153,15 +183,9 @@
String provider, long requestedInterval, int numUpdates) {
mProvider = provider;
mInterval = requestedInterval;
// timeout at 60 seconds after interval time
mTimeout = requestedInterval + 60 * 1000;
        mRequestedUpdates = numUpdates + NUM_IGNORED_UPDATES;
mLocationManager = locationManager;
mCb = cb;
mHandler = new Handler(this);
//Synthetic comment -- @@ -192,13 +216,19 @@
}

private void pass() {
        if (!isTestOutcomeSet) {
            stop();
            mCb.pass();
            isTestOutcomeSet = true;
        }
}

private void fail(String s) {
        if (!isTestOutcomeSet) {
            stop();
            mCb.fail(s);
            isTestOutcomeSet = true;
        }
}

private void scheduleTimeout() {







