/*Be tolerant of GPS session and engine on status messages arriving out of order.

BUG: 2366194

Change-Id:I642dacd397202f671adba5a863394438ca62988dSigned-off-by: Mike Lockwood <lockwood@android.com>*/




//Synthetic comment -- diff --git a/location/java/com/android/internal/location/GpsLocationProvider.java b/location/java/com/android/internal/location/GpsLocationProvider.java
//Synthetic comment -- index 7763549..134756e 100755

//Synthetic comment -- @@ -182,6 +182,9 @@

// true if GPS is navigating
private boolean mNavigating;

    // true if GPS engine is on
    private boolean mEngineOn;

// requested frequency of fixes, in seconds
private int mFixInterval = 1;
//Synthetic comment -- @@ -556,13 +559,17 @@
mNetworkThread = null;
}

        // do this before releasing wakelock
        native_cleanup();

// The GpsEventThread does not wait for the GPS to shutdown
// so we need to report the GPS_STATUS_ENGINE_OFF event here
if (mNavigating) {
            reportStatus(GPS_STATUS_SESSION_END);
        }
        if (mEngineOn) {
reportStatus(GPS_STATUS_ENGINE_OFF);
}
}

public boolean isEnabled() {
//Synthetic comment -- @@ -874,9 +881,24 @@

synchronized(mListeners) {
boolean wasNavigating = mNavigating;

            switch (status) {
                case GPS_STATUS_SESSION_BEGIN:
                    mNavigating = true;
                    break;
                case GPS_STATUS_SESSION_END:
                    mNavigating = false;
                    break;
                case GPS_STATUS_ENGINE_ON:
                    mEngineOn = true;
                    break;
                case GPS_STATUS_ENGINE_OFF:
                    mEngineOn = false;
                    break;
            }

            // beware, the events can come out of order
            if ((mNavigating || mEngineOn) && !mWakeLock.isHeld()) {
if (DEBUG) Log.d(TAG, "Acquiring wakelock");
mWakeLock.acquire();
}
//Synthetic comment -- @@ -919,7 +941,8 @@
mContext.sendBroadcast(intent);
}

            // beware, the events can come out of order
            if (!mNavigating && !mEngineOn && mWakeLock.isHeld()) {
if (DEBUG) Log.d(TAG, "Releasing wakelock");
mWakeLock.release();
}







