/*Tilt code now handles displays with a default landscape orientation.

Based onId7baafa7465aba938a4fec1f165ef6fe9d8af8e1Bug: 2937359
Change-Id:I6ceb5763edd837f08239b88ad947e24801fbb537*/
//Synthetic comment -- diff --git a/src/com/cooliris/media/GridInputProcessor.java b/src/com/cooliris/media/GridInputProcessor.java
//Synthetic comment -- index ec7e6f9..91dfd47 100644

//Synthetic comment -- @@ -20,9 +20,12 @@
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.SystemClock;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.cooliris.app.App;

//Synthetic comment -- @@ -57,6 +60,7 @@
private boolean mTouchFeedbackDelivered;
private GestureDetector mGestureDetector;
private ScaleGestureDetector mScaleGestureDetector;
private boolean mZoomGesture;
private int mCurrentScaleSlot;
private float mScale;
//Synthetic comment -- @@ -76,6 +80,10 @@
mGestureDetector.setIsLongpressEnabled(true);
mZoomGesture = false;
mScale = 1.0f;
}

public int getCurrentFocusSlot() {
//Synthetic comment -- @@ -109,7 +117,23 @@
switch (event.sensor.getType()) {
case Sensor.TYPE_ACCELEROMETER:
float[] values = event.values;
            float valueToUse = (mCamera.mWidth < mCamera.mHeight) ? values[0] : -values[1];
float tiltValue = 0.8f * mPrevTiltValueLowPass + 0.2f * valueToUse;
if (Math.abs(tiltValue) < 0.5f)
tiltValue = 0.0f;







