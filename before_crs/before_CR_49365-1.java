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
                        LocationManager.GPS_PROVIDER, 0, 10);
mLocationVerifier.start();
break;
case 2:
// Test GPS with minTime = 1s
mLocationVerifier = new LocationVerifier(this, mLocationManager,
                        LocationManager.GPS_PROVIDER, 1000, 10);
mLocationVerifier.start();
break;
case 3:
// Test GPS with minTime = 5s
mLocationVerifier = new LocationVerifier(this, mLocationManager,
                        LocationManager.GPS_PROVIDER, 5 * 1000, 4);
mLocationVerifier.start();
break;
case 4:
// Test GPS with minTime = 15s
mLocationVerifier = new LocationVerifier(this, mLocationManager,
                        LocationManager.GPS_PROVIDER, 15 * 1000, 4);
mLocationVerifier.start();
break;
case 5:








//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/location/LocationVerifier.java b/apps/CtsVerifier/src/com/android/cts/verifier/location/LocationVerifier.java
//Synthetic comment -- index be0ef01..c9504d2 100644

//Synthetic comment -- @@ -23,23 +23,41 @@
import android.os.Handler;
import android.os.Message;

public class LocationVerifier implements Handler.Callback {
public static final String TAG = "CtsVerifierLocation";

private static final int MSG_TIMEOUT = 1;

private final LocationManager mLocationManager;
private final PassFailLog mCb;
private final String mProvider;
private final long mInterval;
    private final long mMinActiveInterval;
    private final long mMinPassiveInterval;
private final long mTimeout;
private final Handler mHandler;
private final int mRequestedUpdates;
private final ActiveListener mActiveListener;
private final PassiveListener mPassiveListener;

private long mLastActiveTimestamp = -1;
private long mLastPassiveTimestamp = -1;
private int mNumActiveUpdates = 0;
//Synthetic comment -- @@ -47,6 +65,9 @@
private boolean mRunning = false;
private boolean mActiveLocationArrive = false;

private class ActiveListener implements LocationListener {
@Override
public void onLocationChanged(Location location) {
//Synthetic comment -- @@ -60,33 +81,17 @@
long delta = timestamp - mLastActiveTimestamp;
mLastActiveTimestamp = timestamp;

            if (location.getAccuracy() <= 0.0) {
                fail(mProvider + " location has invalid accuracy: " + location.getAccuracy());
            }
            if (location.getElapsedRealtimeNanos() <= 0) {
                fail(mProvider + " location has invalid elapsed realtime: " +
                        location.getElapsedRealtimeNanos());
            }

            if (mNumActiveUpdates != 1 && delta < mMinActiveInterval) {
                fail(mProvider + " location updated too fast: " + delta + "ms < " +
                        mMinActiveInterval + "ms");
return;
}

mCb.log("active " + mProvider + " update (" + delta + "ms)");

            if (!mProvider.equals(location.getProvider())) {
                fail("wrong provider in callback, actual: " + location.getProvider() +
                        " expected: " + mProvider);
                return;
            }

if (mNumActiveUpdates >= mRequestedUpdates) {
                if (mNumPassiveUpdates < mRequestedUpdates - 1) {
                    fail("passive location updates not working (expected: " + mRequestedUpdates +
                            " received: " + mNumPassiveUpdates + ")");
                }
pass();
}
}
//Synthetic comment -- @@ -99,6 +104,44 @@
public void onProviderDisabled(String provider) { }
}

private class PassiveListener implements LocationListener {
@Override
public void onLocationChanged(Location location) {
//Synthetic comment -- @@ -119,25 +162,12 @@
long delta = timestamp - mLastPassiveTimestamp;
mLastPassiveTimestamp = timestamp;

            if (location.getAccuracy() <= 0.0) {
                fail(mProvider + " location has invalid accuracy: " + location.getAccuracy());
            }
            if (location.getElapsedRealtimeNanos() <= 0) {
                fail(mProvider + " location has invalid elapsed realtime: " +
                        location.getElapsedRealtimeNanos());
            }

            if (mNumPassiveUpdates != 1 && delta < mMinPassiveInterval) {
                fail("passive " + mProvider + " location updated too fast: " + delta + "ms < " +
                        mMinPassiveInterval + "ms");
                mCb.log("when passive updates are much much faster than active updates it " +
                        "suggests the location provider implementation is not power efficient");
                if (LocationManager.GPS_PROVIDER.equals(mProvider)) {
                    mCb.log("check GPS_CAPABILITY_SCHEDULING in GPS driver");
                }
return;
}

mCb.log("passive " + mProvider + " update (" + delta + "ms)");
}

//Synthetic comment -- @@ -153,15 +183,9 @@
String provider, long requestedInterval, int numUpdates) {
mProvider = provider;
mInterval = requestedInterval;
        // Updates can be up to 100ms ahead of schedule
        mMinActiveInterval = Math.max(0, requestedInterval - 100);
        // Allow passive updates to be up to 10x faster than active updates,
        // beyond that it is very likely the implementation is not taking
        // advantage of the interval to be power efficient
        mMinPassiveInterval = mMinActiveInterval / 10;
// timeout at 60 seconds after interval time
mTimeout = requestedInterval + 60 * 1000;
        mRequestedUpdates = numUpdates;
mLocationManager = locationManager;
mCb = cb;
mHandler = new Handler(this);
//Synthetic comment -- @@ -192,13 +216,19 @@
}

private void pass() {
        stop();
        mCb.pass();
}

private void fail(String s) {
        stop();
        mCb.fail(s);
}

private void scheduleTimeout() {







