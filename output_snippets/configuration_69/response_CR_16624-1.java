//<Beginning of snippet n. 0>

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.pm.PackageManager;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.util.DisplayMetrics;
import android.content.Context;

private boolean mTouchFeedbackDelivered;
private GestureDetector mGestureDetector;
private ScaleGestureDetector mScaleGestureDetector;
private boolean mZoomGesture;
private int mCurrentScaleSlot;
private float mScale;
private float mPrevTiltValueLowPass = 0.0f;
private final SensorManager mSensorManager;

public void initializeGestureDetectors() {
    mGestureDetector.setIsLongpressEnabled(true);
    mZoomGesture = false;
    mScale = 1.0f;
    registerOrientationListener();
}

public int getCurrentFocusSlot(SensorEvent event) {
    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
        float[] values = event.values;
        if (values == null || values.length < 2) {
            return mCurrentScaleSlot;
        }
        float valueToUse = isDeviceInPortraitMode() ? values[0] : -values[1];
        float tiltValue = 0.8f * mPrevTiltValueLowPass + 0.2f * valueToUse;
        if (Math.abs(tiltValue) < 0.5f) {
            tiltValue = 0.0f;
        }
        mPrevTiltValueLowPass = tiltValue;
    }
    return mCurrentScaleSlot;
}

private boolean isDeviceInPortraitMode() {
    DisplayMetrics metrics = new DisplayMetrics();
    ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
    return metrics.widthPixels < metrics.heightPixels;
}

private void registerOrientationListener() {
    Sensor rotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    mSensorManager.registerListener(new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event != null && event.values != null && event.values.length > 0) {
                // Update mCurrentScaleSlot based on the new orientation
                getCurrentFocusSlot(event);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL);
}

//<End of snippet n. 0>