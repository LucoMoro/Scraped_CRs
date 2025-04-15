/*GpsLocationProvider: Do not release wakelock until the GPS engine is fully off

Change-Id:I705b1d33af2d70aa1084cca8f6280fade9461d2cSigned-off-by: Mike Lockwood <lockwood@android.com>*/
//Synthetic comment -- diff --git a/location/java/com/android/internal/location/GpsLocationProvider.java b/location/java/com/android/internal/location/GpsLocationProvider.java
//Synthetic comment -- index cd62ed1..7763549 100755

//Synthetic comment -- @@ -875,53 +875,51 @@
synchronized(mListeners) {
boolean wasNavigating = mNavigating;
mNavigating = (status == GPS_STATUS_SESSION_BEGIN);
    
            if (wasNavigating == mNavigating) {
                return;
            }
            
            if (mNavigating) {
if (DEBUG) Log.d(TAG, "Acquiring wakelock");
mWakeLock.acquire();
}
        
            int size = mListeners.size();
            for (int i = 0; i < size; i++) {
                Listener listener = mListeners.get(i);
try {
                    if (mNavigating) {
                        listener.mListener.onGpsStarted(); 
                    } else {
                        listener.mListener.onGpsStopped(); 
}
} catch (RemoteException e) {
Log.w(TAG, "RemoteException in reportStatus");
                    mListeners.remove(listener);
                    // adjust for size of list changing
                    size--;
}
}

            try {
                // update battery stats
                for (int i=mClientUids.size() - 1; i >= 0; i--) {
                    int uid = mClientUids.keyAt(i);
                    if (mNavigating) {
                        mBatteryStats.noteStartGps(uid);
                    } else {
                        mBatteryStats.noteStopGps(uid);
                    }
                }
            } catch (RemoteException e) {
                Log.w(TAG, "RemoteException in reportStatus");
            }

            // send an intent to notify that the GPS has been enabled or disabled.
            Intent intent = new Intent(GPS_ENABLED_CHANGE_ACTION);
            intent.putExtra(EXTRA_ENABLED, mNavigating);
            mContext.sendBroadcast(intent);

            if (!mNavigating) {
if (DEBUG) Log.d(TAG, "Releasing wakelock");
mWakeLock.release();
}







