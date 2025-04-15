/*Tilt code now handles displays with a default landscape orientation.

Based onId7baafa7465aba938a4fec1f165ef6fe9d8af8e1Bug: 2937359
Change-Id:I6ceb5763edd837f08239b88ad947e24801fbb537*/




//Synthetic comment -- diff --git a/src/com/cooliris/media/GridInputProcessor.java b/src/com/cooliris/media/GridInputProcessor.java
//Synthetic comment -- index ec7e6f9..91dfd47 100644

//Synthetic comment -- @@ -20,9 +20,12 @@
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

//Synthetic comment -- @@ -57,6 +60,7 @@
private boolean mTouchFeedbackDelivered;
private GestureDetector mGestureDetector;
private ScaleGestureDetector mScaleGestureDetector;
    private Display mDisplay;
private boolean mZoomGesture;
private int mCurrentScaleSlot;
private float mScale;
//Synthetic comment -- @@ -76,6 +80,10 @@
mGestureDetector.setIsLongpressEnabled(true);
mZoomGesture = false;
mScale = 1.0f;
        {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            mDisplay = windowManager.getDefaultDisplay();
        }
}

public int getCurrentFocusSlot() {
//Synthetic comment -- @@ -109,7 +117,23 @@
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







