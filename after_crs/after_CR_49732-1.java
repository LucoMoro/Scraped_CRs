/*Assert mean and stdev of update deltas

Assert mean and standard deviation of location update deltas in GPS CTS
test, instead of individual values of deltas. This gives more
flexibility to implementations that cannot respect an exect schedule.
This still prevents implementations from computing too many fixes and
using too much battery.

Change-Id:Ic889c567bc7262073da87eba493ffee0f9e68b4d*/




//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/location/GpsTestActivity.java b/apps/CtsVerifier/src/com/android/cts/verifier/location/GpsTestActivity.java
//Synthetic comment -- index 31b5854..bb12b2b 100644

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
                        LocationManager.GPS_PROVIDER, 1 * 1000, 8);
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
//Synthetic comment -- index 5bd5c49..b6d2a9e 100644

//Synthetic comment -- @@ -23,7 +23,11 @@
import android.os.Handler;
import android.os.Message;

import java.util.List;
import java.util.ArrayList;

public class LocationVerifier implements Handler.Callback {

public static final String TAG = "CtsVerifierLocation";

private static final int MSG_TIMEOUT = 1;
//Synthetic comment -- @@ -31,18 +35,29 @@
/** Timing failures on first NUM_IGNORED_UPDATES updates are ignored. */
private static final int NUM_IGNORED_UPDATES = 2;

    /* The mean computed for the deltas should not be smaller
     * than mInterval * MIN_MEAN_RATIO */
    private static final double MIN_MEAN_RATIO = 0.75;

    /**
     * The standard deviation computed for the deltas should not be bigger
     * than mInterval * ALLOWED_STDEV_ERROR_RATIO
     * or MIN_STDEV_MS, whichever is higher.
     */
    private static final double ALLOWED_STDEV_ERROR_RATIO = 0.50;
    private static final long MIN_STDEV_MS = 1000;

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
//Synthetic comment -- @@ -50,6 +65,9 @@
private boolean mRunning = false;
private boolean mActiveLocationArrive = false;

    private List<Long> mActiveDeltas = new ArrayList();
    private List<Long> mPassiveDeltas = new ArrayList();

private class ActiveListener implements LocationListener {
@Override
public void onLocationChanged(Location location) {
//Synthetic comment -- @@ -63,30 +81,17 @@
long delta = timestamp - mLastActiveTimestamp;
mLastActiveTimestamp = timestamp;

            if (mNumActiveUpdates <= NUM_IGNORED_UPDATES ) {
                mCb.log("(ignored) active " + mProvider + " update (" + delta + "ms)");
return;
}

            mActiveDeltas.add(delta);
            mCb.log("active " + mProvider + " update (" + delta + "ms)");

if (mNumActiveUpdates >= mRequestedUpdates) {
                assertMeanAndStdev(mProvider, mActiveDeltas);
                assertMeanAndStdev(LocationManager.PASSIVE_PROVIDER, mPassiveDeltas);
pass();
}
}
//Synthetic comment -- @@ -99,6 +104,45 @@
public void onProviderDisabled(String provider) { }
}

    private void assertMeanAndStdev(String provider, List<Long> deltas) {
        double mean = computeMean(deltas);
        double stdev = computeStdev(mean, deltas);

        double minMean = mInterval * MIN_MEAN_RATIO;
        if (mean < minMean) {
            fail(provider + " provider mean too small: " + mean
                 + " (min: " + minMean + ")");
            return;
        }

        double maxStdev = Math.max(MIN_STDEV_MS, mInterval * ALLOWED_STDEV_ERROR_RATIO);
        if (stdev > maxStdev) {
            fail (provider + " provider stdev too big: "
                  + stdev + " (max: " + maxStdev + ")");
            return;
        }

        mCb.log(provider + " provider mean: " + mean);
        mCb.log(provider + " provider stdev: " + stdev);
    }

    private double computeMean(List<Long> deltas) {
        long accumulator = 0;
        for (long d : deltas) {
            accumulator += d;
        }
        return accumulator / deltas.size();
    }

    private double computeStdev(double mean, List<Long> deltas) {
        double accumulator = 0;
        for (long d : deltas) {
            double diff = d - mean;
            accumulator += diff * diff;
        }
        return Math.sqrt(accumulator / (deltas.size() - 1));
    }

private class PassiveListener implements LocationListener {
@Override
public void onLocationChanged(Location location) {
//Synthetic comment -- @@ -119,22 +163,12 @@
long delta = timestamp - mLastPassiveTimestamp;
mLastPassiveTimestamp = timestamp;

            if (mNumPassiveUpdates <= NUM_IGNORED_UPDATES) {
                mCb.log("(ignored) passive " + mProvider + " update (" + delta + "ms)");
                return;
}

            mPassiveDeltas.add(delta);
mCb.log("passive " + mProvider + " update (" + delta + "ms)");
}

//Synthetic comment -- @@ -150,12 +184,6 @@
String provider, long requestedInterval, int numUpdates) {
mProvider = provider;
mInterval = requestedInterval;
// timeout at 60 seconds after interval time
mTimeout = requestedInterval + 60 * 1000;
mRequestedUpdates = numUpdates + NUM_IGNORED_UPDATES;
//Synthetic comment -- @@ -189,13 +217,19 @@
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







