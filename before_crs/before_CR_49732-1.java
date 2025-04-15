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
//Synthetic comment -- index 5bd5c49..b6d2a9e 100644

//Synthetic comment -- @@ -23,7 +23,11 @@
import android.os.Handler;
import android.os.Message;

public class LocationVerifier implements Handler.Callback {
public static final String TAG = "CtsVerifierLocation";

private static final int MSG_TIMEOUT = 1;
//Synthetic comment -- @@ -31,18 +35,29 @@
/** Timing failures on first NUM_IGNORED_UPDATES updates are ignored. */
private static final int NUM_IGNORED_UPDATES = 2;

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
//Synthetic comment -- @@ -50,6 +65,9 @@
private boolean mRunning = false;
private boolean mActiveLocationArrive = false;

private class ActiveListener implements LocationListener {
@Override
public void onLocationChanged(Location location) {
//Synthetic comment -- @@ -63,30 +81,17 @@
long delta = timestamp - mLastActiveTimestamp;
mLastActiveTimestamp = timestamp;

            if (delta < mMinActiveInterval) {
                if (mNumActiveUpdates > NUM_IGNORED_UPDATES ) {
                    fail(mProvider + " location updated too fast: " + delta + "ms < " +
                         mMinActiveInterval + "ms");
                    return;
                } else {
                    mCb.log("WARNING: active " + mProvider + " location updated too fast: " +
                         delta + "ms < " + mMinActiveInterval + "ms");
                }
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
//Synthetic comment -- @@ -99,6 +104,45 @@
public void onProviderDisabled(String provider) { }
}

private class PassiveListener implements LocationListener {
@Override
public void onLocationChanged(Location location) {
//Synthetic comment -- @@ -119,22 +163,12 @@
long delta = timestamp - mLastPassiveTimestamp;
mLastPassiveTimestamp = timestamp;

            if (delta < mMinPassiveInterval) {
                if (mNumPassiveUpdates > NUM_IGNORED_UPDATES) {
                    fail("passive " + mProvider + " location updated too fast: " + delta + "ms < " +
                         mMinPassiveInterval + "ms");
                    mCb.log("when passive updates are much much faster than active updates it " +
                            "suggests the location provider implementation is not power efficient");
                    if (LocationManager.GPS_PROVIDER.equals(mProvider)) {
                        mCb.log("check GPS_CAPABILITY_SCHEDULING in GPS driver");
                    }
                    return;
                } else {
                    mCb.log("WARNING: passive " + mProvider + " location updated too fast: " +
                            delta + "ms < " + mMinPassiveInterval + "ms");
                }
}

mCb.log("passive " + mProvider + " update (" + delta + "ms)");
}

//Synthetic comment -- @@ -150,12 +184,6 @@
String provider, long requestedInterval, int numUpdates) {
mProvider = provider;
mInterval = requestedInterval;
        // Updates can be up to 15% of the request interval ahead of schedule
        mMinActiveInterval = Math.max(0, (long) (requestedInterval * 0.85));
        // Allow passive updates to be up to 10x faster than active updates,
        // beyond that it is very likely the implementation is not taking
        // advantage of the interval to be power efficient
        mMinPassiveInterval = mMinActiveInterval / 10;
// timeout at 60 seconds after interval time
mTimeout = requestedInterval + 60 * 1000;
mRequestedUpdates = numUpdates + NUM_IGNORED_UPDATES;
//Synthetic comment -- @@ -189,13 +217,19 @@
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







