/*Be tolerant of GPS session and engine on status messages arriving out of order.

BUG: 2366194

Change-Id:Iac152cc46f3647e12c9077f1bfa4183f19ac94b6Signed-off-by: Mike Lockwood <lockwood@android.com>*/
//Synthetic comment -- diff --git a/location/java/com/android/internal/location/GpsLocationProvider.java b/location/java/com/android/internal/location/GpsLocationProvider.java
//Synthetic comment -- index 7763549..134756e 100755

//Synthetic comment -- @@ -182,6 +182,9 @@

// true if GPS is navigating
private boolean mNavigating;

// requested frequency of fixes, in seconds
private int mFixInterval = 1;
//Synthetic comment -- @@ -556,13 +559,17 @@
mNetworkThread = null;
}

// The GpsEventThread does not wait for the GPS to shutdown
// so we need to report the GPS_STATUS_ENGINE_OFF event here
if (mNavigating) {
reportStatus(GPS_STATUS_ENGINE_OFF);
}

        native_cleanup();
}

public boolean isEnabled() {
//Synthetic comment -- @@ -874,9 +881,24 @@

synchronized(mListeners) {
boolean wasNavigating = mNavigating;
            mNavigating = (status == GPS_STATUS_SESSION_BEGIN);

            if (mNavigating && !mWakeLock.isHeld()) {
if (DEBUG) Log.d(TAG, "Acquiring wakelock");
mWakeLock.acquire();
}
//Synthetic comment -- @@ -919,7 +941,8 @@
mContext.sendBroadcast(intent);
}

            if (status == GPS_STATUS_ENGINE_OFF && mWakeLock.isHeld()) {
if (DEBUG) Log.d(TAG, "Releasing wakelock");
mWakeLock.release();
}







