/*DO NOT MERGE Relax LocationVerifier assertions.

Relax assertions:

1 - First 2 updates failures are ignored, but a warning is still
emitted.

2 - Updates can be up to 15% of the request interval ahead of schedule. eg.
if an app requests updates every 10s, then receiving an update after
8.5s is fine.

Change-Id:I651351958a6984c1696f62efb5bf0f212828f55b*/
//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/location/LocationVerifier.java b/apps/CtsVerifier/src/com/android/cts/verifier/location/LocationVerifier.java
//Synthetic comment -- index 6226cf6..5bd5c49 100644

//Synthetic comment -- @@ -28,6 +28,9 @@

private static final int MSG_TIMEOUT = 1;

private final LocationManager mLocationManager;
private final PassFailLog mCb;
private final String mProvider;
//Synthetic comment -- @@ -60,10 +63,15 @@
long delta = timestamp - mLastActiveTimestamp;
mLastActiveTimestamp = timestamp;

            if (mNumActiveUpdates != 1 && delta < mMinActiveInterval) {
                fail(mProvider + " location updated too fast: " + delta + "ms < " +
                        mMinActiveInterval + "ms");
                return;
}

mCb.log("active " + mProvider + " update (" + delta + "ms)");
//Synthetic comment -- @@ -111,15 +119,20 @@
long delta = timestamp - mLastPassiveTimestamp;
mLastPassiveTimestamp = timestamp;

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
//Synthetic comment -- @@ -137,15 +150,15 @@
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







