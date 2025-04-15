/*GpsLocationProvider: Store new Location before onGpsStatusChanged is sent

In reportLocation() the new location is now stored before the callback
onGpsStatusChanged(GPS_EVENT_FIRST_FIX) is sent. This will prevent users
from getting a faulty location from LocationManager.getLastKnownLocation()
after receiving onGpsStatusChanged(GPS_EVENT_FIRST_FIX).*/




//Synthetic comment -- diff --git a/location/java/com/android/internal/location/GpsLocationProvider.java b/location/java/com/android/internal/location/GpsLocationProvider.java
//Synthetic comment -- index 1fe3d05..f085e43 100755

//Synthetic comment -- @@ -800,29 +800,6 @@
if (VERBOSE) Log.v(TAG, "reportLocation lat: " + latitude + " long: " + longitude +
" timestamp: " + timestamp);

synchronized (mLocation) {
mLocationFlags = flags;
if ((flags & LOCATION_HAS_LAT_LONG) == LOCATION_HAS_LAT_LONG) {
//Synthetic comment -- @@ -858,6 +835,29 @@
}
}

        mLastFixTime = System.currentTimeMillis();
        // report time to first fix
        if (mTTFF == 0 && (flags & LOCATION_HAS_LAT_LONG) == LOCATION_HAS_LAT_LONG) {
            mTTFF = (int)(mLastFixTime - mFixRequestTime);
            if (Config.LOGD) Log.d(TAG, "TTFF: " + mTTFF);

            // notify status listeners
            synchronized(mListeners) {
                int size = mListeners.size();
                for (int i = 0; i < size; i++) {
                    Listener listener = mListeners.get(i);
                    try {
                        listener.mListener.onFirstFix(mTTFF);
                    } catch (RemoteException e) {
                        Log.w(TAG, "RemoteException in stopNavigating");
                        mListeners.remove(listener);
                        // adjust for size of list changing
                        size--;
                    }
                }
            }
        }

if (mStarted && mStatus != LocationProvider.AVAILABLE) {
mAlarmManager.cancel(mTimeoutIntent);
// send an intent to notify that the GPS is receiving fixes.







