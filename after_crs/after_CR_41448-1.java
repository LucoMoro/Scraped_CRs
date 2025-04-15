/*Proximity Sensor: Delay 500ms to process the near event

If a near event is got, delay 500ms to avoid that screen going off by an
accidental swipe.

Change-Id:I792f21c433f5bd60a1c7d9dd41a049fd3cce3faaAuthor: Jian Li <jian.d.li@intel.com>
Signed-off-by: Jian Li <jian.d.li@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 33317*/




//Synthetic comment -- diff --git a/services/java/com/android/server/PowerManagerService.java b/services/java/com/android/server/PowerManagerService.java
//Synthetic comment -- index 888ec69..a4ec72c 100644

//Synthetic comment -- @@ -127,6 +127,7 @@

// For debouncing the proximity sensor in milliseconds
private static final int PROXIMITY_SENSOR_DELAY = 1000;
    private static final int PROXIMITY_SENSOR_ACTIVE_DELAY = 500;

// trigger proximity if distance is less than 5 cm
private static final float PROXIMITY_THRESHOLD = 5.0f;
//Synthetic comment -- @@ -3320,10 +3321,18 @@
if (mDebugProximitySensor) {
Slog.d(TAG, "mProximityListener.onSensorChanged active: " + active);
}
                // if a near event is got, delay PROXIMITY_SENSOR_ACTIVE_DELAY here to avoid
                // screen going off by an accidental swipe
                int delay = active ? PROXIMITY_SENSOR_ACTIVE_DELAY : 0;

                // enforce delaying atleast PROXIMITY_SENSOR_DELAY before processing to debounce
                // the sensor
                if (timeSinceLastEvent < PROXIMITY_SENSOR_DELAY)
                    delay = Math.max(delay,
                            PROXIMITY_SENSOR_DELAY - (int)timeSinceLastEvent);
                if (delay > 0) {
mProximityPendingValue = (active ? 1 : 0);
                    mHandler.postDelayed(mProximityTask, delay);
proximityTaskQueued = true;
} else {
// process the value immediately







