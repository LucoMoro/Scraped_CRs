/*Added check to make orientation calculations more robust

Added check avoid a division by zero resulting in NaN which in turn
makes checkFullyTilted to ignore high tilt angles from then on.
If (x, y, z) == (0, 0, 0) then there is no tilt or rotation and
this vector must be ignored. This check is extended to ignore all
small acceleration values where noise can be of big influence.

Low or zero readings can happen when space travelling free falling,
but more commonly when shaking or getting bad readings from the sensor.
The accelerometer is turned off when not used and polling it too soon
after it is turned on may result in (0, 0, 0).

Change-Id:I19aec653abb8ab6f7126778035c8c96449f1326f*/




//Synthetic comment -- diff --git a/core/java/android/view/WindowOrientationListener.java b/core/java/android/view/WindowOrientationListener.java
//Synthetic comment -- index 2a76e33..6095a64 100755

//Synthetic comment -- @@ -234,6 +234,14 @@
// high time constant.
private static final float MAX_DEVIATION_FROM_GRAVITY = 1.5f;

        // Minimum acceleration considered, in m/s^2. Below this threshold sensor noise will have
        // significant impact on the calculations and in case of the vector (0, 0, 0) there is no
        // defined rotation or tilt at all. Low or zero readings can happen when space travelling
        // or free falling, but more commonly when shaking or getting bad readings from the sensor.
        // The accelerometer is turned off when not used and polling it too soon after it is
        // turned on may result in (0, 0, 0).
        private static final float MIN_ABS_ACCELERATION = 1.5f;

// Actual sampling period corresponding to SensorManager.SENSOR_DELAY_NORMAL.  There's no
// way to get this information from SensorManager.
// Note the actual period is generally 3-30ms larger than this depending on the device, but
//Synthetic comment -- @@ -347,6 +355,9 @@
float deviation = Math.abs(magnitude - SensorManager.STANDARD_GRAVITY);

handleAccelerationDistrust(deviation);
            if (magnitude < MIN_ABS_ACCELERATION) {
                return; // Ignore tilt and orientation when (0, 0, 0) or low reading
            }

// only filter tilt when we're accelerating
float alpha = 1;







