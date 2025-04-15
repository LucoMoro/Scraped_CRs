/*Adding modifications for zero gravity hack (allow phone screen to rotate to all sides)*/
//Synthetic comment -- diff --git a/core/java/android/view/WindowOrientationListener.java b/core/java/android/view/WindowOrientationListener.java
//Synthetic comment -- index 13606e7..48e17f2c 100755

//Synthetic comment -- @@ -21,6 +21,7 @@
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Config;
import android.util.Log;

//Synthetic comment -- @@ -40,7 +41,8 @@
private Sensor mSensor;
private SensorEventListener mSensorEventListener;
private int mSensorRotation = -1;

/**
* Creates a new WindowOrientationListener.
* 
//Synthetic comment -- @@ -60,6 +62,7 @@
* SENSOR_DELAY_NORMAL} for simple screen orientation change detection.
*/
public WindowOrientationListener(Context context, int rate) {
mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
mRate = rate;
mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//Synthetic comment -- @@ -133,7 +136,7 @@
private static final int LANDSCAPE_LOWER = 235;
// Minimum angle which is considered portrait
private static final int PORTRAIT_LOWER = 60;
        
// Internal value used for calculating linear variant
private static final float PL_LF_UPPER =
((float)(PL_UPPER-PL_LOWER))/((float)(PIVOT_UPPER-PIVOT));
//Synthetic comment -- @@ -144,6 +147,60 @@
((float)(LP_UPPER - LP_LOWER))/((float)(PIVOT_UPPER-PIVOT));
private static final float LP_LF_LOWER =
((float)(LP_UPPER - LP_LOWER))/((float)(PIVOT-PIVOT_LOWER)); 

public void onSensorChanged(SensorEvent event) {
float[] values = event.values;
//Synthetic comment -- @@ -151,22 +208,24 @@
float Y = values[_DATA_Y];
float Z = values[_DATA_Z];
float OneEightyOverPi = 57.29577957855f;
            float gravity = (float) Math.sqrt(X*X+Y*Y+Z*Z);
            float zyangle = (float)Math.asin(Z/gravity)*OneEightyOverPi;
int rotation = -1;
if ((zyangle <= PIVOT_UPPER) && (zyangle >= PIVOT_LOWER)) {
// Check orientation only if the phone is flat enough
                // Don't trust the angle if the magnitude is small compared to the y value
                float angle = (float)Math.atan2(Y, -X) * OneEightyOverPi;
                int orientation = 90 - (int)Math.round(angle);
// normalize to 0 - 359 range
while (orientation >= 360) {
orientation -= 360;
                } 
while (orientation < 0) {
orientation += 360;
}
                // Orientation values between  LANDSCAPE_LOWER and PL_LOWER
// are considered landscape.
// Ignore orientation values between 0 and LANDSCAPE_LOWER
// For orientation values between LP_UPPER and PL_LOWER,
//Synthetic comment -- @@ -181,24 +240,97 @@
} else {
threshold = LP_LOWER + (LP_LF_UPPER * delta);
}
                        rotation = (orientation >= threshold) ? Surface.ROTATION_0 : Surface.ROTATION_90;
} else {
if (delta < 0) {
// Delta is negative
                            threshold = PL_UPPER+(PL_LF_LOWER * delta);
} else {
                            threshold = PL_UPPER-(PL_LF_UPPER * delta);
}
                        rotation = (orientation <= threshold) ? Surface.ROTATION_90: Surface.ROTATION_0;
}
} else if ((orientation >= LANDSCAPE_LOWER) && (orientation < LP_LOWER)) {
rotation = Surface.ROTATION_90;
} else if ((orientation >= PL_UPPER) || (orientation <= PORTRAIT_LOWER)) {
rotation = Surface.ROTATION_0;
}
if ((rotation != -1) && (rotation != mSensorRotation)) {
                    mSensorRotation = rotation;
                    onOrientationChanged(mSensorRotation);
}
}
}







