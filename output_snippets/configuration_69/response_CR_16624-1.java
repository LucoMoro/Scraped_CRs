//<Beginning of snippet n. 0>

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.SystemClock;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.cooliris.app.App;

private boolean mTouchFeedbackDelivered;
private GestureDetector mGestureDetector;
private ScaleGestureDetector mScaleGestureDetector;
private boolean mZoomGesture;
private int mCurrentScaleSlot;
private float mScale;
private boolean isLandscape;

mGestureDetector.setIsLongpressEnabled(true);
mZoomGesture = false;
mScale = 1.0f;

public void onSensorChanged(SensorEvent event) {
    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
        float[] values = event.values;
        if (mCamera.mWidth > mCamera.mHeight) {
            isLandscape = true;
        } else {
            isLandscape = false;
        }

        float valueToUse = isLandscape ? values[1] : values[0];
        float tiltValue = 0.8f * mPrevTiltValueLowPass + 0.2f * valueToUse;
        if (Math.abs(tiltValue) < 0.5f)
            tiltValue = 0.0f;
    }
}

public int getCurrentFocusSlot() {
    // additional logic to handle the slot selection based on the device orientation
    float[] values = getAccelerometerValues();
    float valueToUse = isLandscape ? values[1] : values[0];
    float tiltValue = 0.8f * mPrevTiltValueLowPass + 0.2f * valueToUse;
    if (Math.abs(tiltValue) < 0.5f)
        tiltValue = 0.0f;

    // implementation for determining the current focus slot based on tiltValue
    // ...
}

//<End of snippet n. 0>