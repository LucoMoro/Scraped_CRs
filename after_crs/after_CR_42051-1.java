/*Fix the issue about SingleShot

1. If it calls startNavigating(), it is always set by tracking mode.
2. Cause : The positioning mode is set as the following
   native_set_position_mode(mPositionMode, GPS_POSITION_RECURRENCE_PERIODIC, interval, 0, 0)
3. Modify : If mSingleShotGps is true, positioning mode is set as the following
   native_set_position_mode(mPositionMode, GPS_POSITION_RECURRENCE_SINGLE, interval, 0, 0)
Signed-off-by : sungeun lim <sungeun.lim@lge.com>

Change-Id:Ia9d3bddd17f95166ebaa12be45e025bfe684b62b*/




//Synthetic comment -- diff --git a/services/java/com/android/server/location/GpsLocationProvider.java b/services/java/com/android/server/location/GpsLocationProvider.java
old mode 100755
new mode 100644
//Synthetic comment -- index 4ad6140..3ea644ac

//Synthetic comment -- @@ -839,11 +839,21 @@
mFixInterval = (int)minTime;

if (mStarted && hasCapability(GPS_CAPABILITY_SCHEDULING)) {
            if(mSingleShot)
            {
                  if (!native_set_position_mode(mPositionMode, GPS_POSITION_RECURRENCE_SINGLE,
                        mFixInterval, 0, 0)) {
                    Log.e(TAG, "set_position_mode failed in setMinTime()");
                  }
            }
            else
            {
if (!native_set_position_mode(mPositionMode, GPS_POSITION_RECURRENCE_PERIODIC,
mFixInterval, 0, 0)) {
Log.e(TAG, "set_position_mode failed in setMinTime()");
}
}
            }
}
}

//Synthetic comment -- @@ -1009,12 +1019,24 @@
}

int interval = (hasCapability(GPS_CAPABILITY_SCHEDULING) ? mFixInterval : 1000);
            if(mSingleShot)
            {
            if (!native_set_position_mode(mPositionMode, GPS_POSITION_RECURRENCE_SINGLE,
                    interval, 0, 0)) {
                mStarted = false;
                Log.e(TAG, "set_position_mode failed in startNavigating()");
                return;
            }
            }
           else
            {
if (!native_set_position_mode(mPositionMode, GPS_POSITION_RECURRENCE_PERIODIC,
interval, 0, 0)) {
mStarted = false;
Log.e(TAG, "set_position_mode failed in startNavigating()");
return;
}
            }
if (!native_start()) {
mStarted = false;
Log.e(TAG, "native_start failed in startNavigating()");







