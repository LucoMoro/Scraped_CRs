
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
mGestureDetector.setIsLongpressEnabled(true);
mZoomGesture = false;
mScale = 1.0f;
}

public int getCurrentFocusSlot() {
switch (event.sensor.getType()) {
case Sensor.TYPE_ACCELEROMETER:
float[] values = event.values;
            float valueToUse = (mCamera.mWidth < mCamera.mHeight) ? values[0] : -values[1];
float tiltValue = 0.8f * mPrevTiltValueLowPass + 0.2f * valueToUse;
if (Math.abs(tiltValue) < 0.5f)
tiltValue = 0.0f;

//<End of snippet n. 0>








