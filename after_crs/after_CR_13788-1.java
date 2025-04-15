/*Adding modifications for zero gravity hack (allow phone screen to rotate to all sides)*/




//Synthetic comment -- diff --git a/core/java/android/view/WindowOrientationListener.java b/core/java/android/view/WindowOrientationListener.java
//Synthetic comment -- index 13606e7..48e17f2c 100755

//Synthetic comment -- @@ -21,6 +21,7 @@
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.Settings;
import android.util.Config;
import android.util.Log;

//Synthetic comment -- @@ -40,7 +41,8 @@
private Sensor mSensor;
private SensorEventListener mSensorEventListener;
private int mSensorRotation = -1;
    private Context mContext;
    
/**
* Creates a new WindowOrientationListener.
* 
//Synthetic comment -- @@ -60,6 +62,7 @@
* SENSOR_DELAY_NORMAL} for simple screen orientation change detection.
*/
public WindowOrientationListener(Context context, int rate) {
        mContext = context;
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


        // new orientation h4x start here
        // -- optedoblivion
        private static final int LANDSCAPE_LOWER_2 = 125;
        private static final int LP_LOWER_2 = 40;
        private static final int PL_UPPER_2 = 65;
        private static final int PL_LOWER_2 = 90;
        private static final int LP_UPPER_2 = 1;
        private static final int PORTRAIT_LOWER_2 = 300;
        private static final int LP_LOWER_3 = 220;
        private static final int PL_UPPER_3 = 245;
        private static final int LP_UPPER_3 = 181;
        private static final int PL_UPPER_4 = 115;
        private static final int LP_LOWER_4 = 140;
        private static final int LP_UPPER_4 = 179;

        // Internal value used for calculating linear variant
        private static final float PL_LF_UPPER_2 =
            ((float)(PL_UPPER_2-PL_LOWER_2))/((float)(PIVOT_UPPER-PIVOT));
        private static final float PL_LF_LOWER_2 =
            ((float)(PL_UPPER_2-PL_LOWER_2))/((float)(PIVOT-PIVOT_LOWER));
        //  Internal value used for calculating linear variant
        private static final float LP_LF_UPPER_2 =
            ((float)(LP_UPPER_2 - LP_LOWER_2))/((float)(PIVOT_UPPER-PIVOT));
        private static final float LP_LF_LOWER_2 =
            ((float)(LP_UPPER_2 - LP_LOWER_2))/((float)(PIVOT-PIVOT_LOWER)); 

        // Internal value used for calculating linear variant
        private static final float PL_LF_UPPER_3 =
            ((float)(PL_UPPER_3-PL_LOWER))/((float)(PIVOT_UPPER-PIVOT));
        private static final float PL_LF_LOWER_3 =
            ((float)(PL_UPPER_3-PL_LOWER))/((float)(PIVOT-PIVOT_LOWER));
        //  Internal value used for calculating linear variant
        private static final float LP_LF_UPPER_3 =
            ((float)(LP_UPPER_3 - LP_LOWER_3))/((float)(PIVOT_UPPER-PIVOT));
        private static final float LP_LF_LOWER_3 =
            ((float)(LP_UPPER_3 - LP_LOWER_3))/((float)(PIVOT-PIVOT_LOWER)); 

        // Internal value used for calculating linear variant
        private static final float PL_LF_UPPER_4 =
            ((float)(PL_UPPER_4-PL_LOWER_2))/((float)(PIVOT_UPPER-PIVOT));
        private static final float PL_LF_LOWER_4 =
            ((float)(PL_UPPER_4-PL_LOWER_2))/((float)(PIVOT-PIVOT_LOWER));
        //  Internal value used for calculating linear variant
        private static final float LP_LF_UPPER_4 =
            ((float)(LP_UPPER_4 - LP_LOWER_4))/((float)(PIVOT_UPPER-PIVOT));
        private static final float LP_LF_LOWER_4 =
            ((float)(LP_UPPER_4 - LP_LOWER_4))/((float)(PIVOT-PIVOT_LOWER)); 

	// End h4x vars        




public void onSensorChanged(SensorEvent event) {
float[] values = event.values;
//Synthetic comment -- @@ -151,22 +208,24 @@
float Y = values[_DATA_Y];
float Z = values[_DATA_Z];
float OneEightyOverPi = 57.29577957855f;
            float gravity = (float) Math.sqrt(X * X + Y * Y + Z * Z);
            float zyangle = (float) Math.asin(Z / gravity) * OneEightyOverPi;
int rotation = -1;
if ((zyangle <= PIVOT_UPPER) && (zyangle >= PIVOT_LOWER)) {
                               
// Check orientation only if the phone is flat enough
                // Don't trust the angle if the magnitude is small compared to
                // the y value
                float angle = (float) Math.atan2(Y, -X) * OneEightyOverPi;
                int orientation = 90 - (int) Math.round(angle);
// normalize to 0 - 359 range
while (orientation >= 360) {
orientation -= 360;
                }
while (orientation < 0) {
orientation += 360;
}
                // Orientation values between LANDSCAPE_LOWER and PL_LOWER
// are considered landscape.
// Ignore orientation values between 0 and LANDSCAPE_LOWER
// For orientation values between LP_UPPER and PL_LOWER,
//Synthetic comment -- @@ -181,24 +240,97 @@
} else {
threshold = LP_LOWER + (LP_LF_UPPER * delta);
}
                        rotation = (orientation >= threshold) ? Surface.ROTATION_0
                                : Surface.ROTATION_90;
} else {
if (delta < 0) {
// Delta is negative
                            threshold = PL_UPPER + (PL_LF_LOWER * delta);
} else {
                            threshold = PL_UPPER - (PL_LF_UPPER * delta);
}
                        rotation = (orientation <= threshold) ? Surface.ROTATION_90
                                : Surface.ROTATION_0;
}
} else if ((orientation >= LANDSCAPE_LOWER) && (orientation < LP_LOWER)) {
rotation = Surface.ROTATION_90;
} else if ((orientation >= PL_UPPER) || (orientation <= PORTRAIT_LOWER)) {
rotation = Surface.ROTATION_0;
                    // Start orientation h4x
                    // --optedoblivion
                } else if ((orientation <= PL_LOWER_2) && (orientation >= LP_UPPER_2)) {
                    float threshold;
                    float delta = zyangle - PIVOT;
                    if (mSensorRotation == Surface.ROTATION_270) {
                        if (delta < 0) {
                            threshold = LP_LOWER_2 - (LP_LF_LOWER_2 * delta);
                        } else {
                            threshold = LP_LOWER_2 + (LP_LF_UPPER_2 * delta);
                        }
                        rotation = (orientation <= threshold) ? Surface.ROTATION_0
                                : Surface.ROTATION_270;
                    } else {
                        if (delta < 0) {
                            threshold = PL_UPPER_2 + (PL_LF_LOWER_2 * delta);
                        } else {
                            threshold = PL_UPPER_2 - (PL_LF_UPPER_2 * delta);
                        }
                        rotation = (orientation >= threshold) ? Surface.ROTATION_270
                                : Surface.ROTATION_0;
                    }
                } else if ((orientation <= LANDSCAPE_LOWER_2) && (orientation > LP_LOWER_2)) {
                    rotation = Surface.ROTATION_270;
                } else if ((orientation <= PL_LOWER) && (orientation >= LP_UPPER_3)) {
                    float threshold;
                    float delta = zyangle - PIVOT;
                    if (mSensorRotation == Surface.ROTATION_90) {
                        if (delta < 0) {
                            threshold = LP_LOWER_3 - (LP_LF_LOWER_3 * delta);
                        } else {
                            threshold = LP_LOWER_3 + (LP_LF_UPPER_3 * delta);
                        }
                        rotation = (orientation <= threshold) ? Surface.ROTATION_180
                                : Surface.ROTATION_90;
                    } else {
                        if (delta < 0) {
                            threshold = PL_UPPER_3 + (PL_LF_LOWER_3 * delta);
                        } else {
                            threshold = PL_UPPER_3 - (PL_LF_UPPER_3 * delta);
                        }
                        rotation = (orientation >= threshold) ? Surface.ROTATION_90
                                : Surface.ROTATION_180;
                    }
                } else if ((orientation <= LP_LOWER_3) && (orientation >= LP_LOWER_4)) {
                    rotation = Surface.ROTATION_180;
                } else if ((orientation >= PL_LOWER_2) && (orientation <= LP_UPPER_4)) {
                    float threshold;
                    float delta = zyangle - PIVOT;
                    if (mSensorRotation == Surface.ROTATION_270) {
                        if (delta < 0) {
                            threshold = LP_LOWER_4 - (LP_LF_LOWER_4 * delta);
                        } else {
                            threshold = LP_LOWER_4 + (LP_LF_UPPER_4 * delta);
                        }
                        rotation = (orientation >= threshold) ? Surface.ROTATION_180
                                : Surface.ROTATION_270;
                    } else {
                        if (delta < 0) {
                            threshold = PL_UPPER_4 + (PL_LF_UPPER_4 * delta);
                        } else {
                            threshold = PL_UPPER_4 - (PL_LF_UPPER_4 * delta);
                        }
                        rotation = (orientation <= threshold) ? Surface.ROTATION_270
                                : Surface.ROTATION_180;
                    }
}
                // End orientation h4x
if ((rotation != -1) && (rotation != mSensorRotation)) {
                    if (Surface.ROTATION_180 != rotation || (Surface.ROTATION_180 == rotation && 
                            (Settings.System.getInt(mContext.getContentResolver(), 
                                    Settings.System.USE_180_ORIENTATION, 0) > 0))) {            
                        mSensorRotation = rotation;
                        onOrientationChanged(mSensorRotation);
                    }
}
}
}







