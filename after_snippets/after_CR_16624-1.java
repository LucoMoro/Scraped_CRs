
//<Beginning of snippet n. 0>


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.SystemClock;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.WindowManager;

import com.cooliris.app.App;

private boolean mTouchFeedbackDelivered;
private GestureDetector mGestureDetector;
private ScaleGestureDetector mScaleGestureDetector;
    private Display mDisplay;
private boolean mZoomGesture;
private int mCurrentScaleSlot;
private float mScale;
mGestureDetector.setIsLongpressEnabled(true);
mZoomGesture = false;
mScale = 1.0f;
        {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            mDisplay = windowManager.getDefaultDisplay();
        }
}

public int getCurrentFocusSlot() {
switch (event.sensor.getType()) {
case Sensor.TYPE_ACCELEROMETER:
float[] values = event.values;
            float valueToUse;
            switch (mDisplay.getRotation()) {
            case Surface.ROTATION_0:
                valueToUse = values[0];
                break;
            case Surface.ROTATION_90:
                valueToUse = -event.values[1];
                break;
            case Surface.ROTATION_180:
                valueToUse = -event.values[0];
                break;
            case Surface.ROTATION_270:
                valueToUse =  event.values[1];
                break;
            default:
                valueToUse = 0.0f;
            }
float tiltValue = 0.8f * mPrevTiltValueLowPass + 0.2f * valueToUse;
if (Math.abs(tiltValue) < 0.5f)
tiltValue = 0.0f;

//<End of snippet n. 0>








