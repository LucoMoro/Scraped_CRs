/*DO NOT MERGE Relax LocationVerifier assertions.

This has merged in a future release already.

Relax assertions:

1 - First 2 updates failures are ignored, but a warning is still
emitted.

2 - Updates can be up to 15% of the request interval ahead of schedule. eg.
if an app requests updates every 10s, then receiving an update after
8.5s is fine.

Change-Id:I98aa26129c570d2f75e38f13d0751709c4adebb5*/
//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/location/LocationVerifier.java b/apps/CtsVerifier/src/com/android/cts/verifier/location/LocationVerifier.java
//Synthetic comment -- index be0ef01..143ee24 100644

//Synthetic comment -- @@ -28,6 +28,9 @@

private static final int MSG_TIMEOUT = 1;

private final LocationManager mLocationManager;
private final PassFailLog mCb;
private final String mProvider;
//Synthetic comment -- @@ -68,10 +71,15 @@
location.getElapsedRealtimeNanos());
}

            if (mNumActiveUpdates != 1 && delta < mMinActiveInterval) {
                fail(mProvider + " location updated too fast: " + delta + "ms < " +
                        mMinActiveInterval + "ms");
                return;
}

mCb.log("active " + mProvider + " update (" + delta + "ms)");
//Synthetic comment -- @@ -127,15 +135,20 @@
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
//Synthetic comment -- @@ -153,15 +166,15 @@
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







