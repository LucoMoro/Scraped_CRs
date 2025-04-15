/*Updated Sensor Samples to newest API

Change-Id:I88c0ce08232fed34aa99c97385155220e7d9abff*/
//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Compass.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Compass.java
//Synthetic comment -- index d2a9907..d3b0981 100644

//Synthetic comment -- @@ -16,15 +16,13 @@

package com.example.android.apis.graphics;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Config;
import android.util.Log;
import android.view.View;
//Synthetic comment -- @@ -33,23 +31,22 @@

private static final String TAG = "Compass";

	private SensorManager mSensorManager;
private SampleView mView;
private float[] mValues;

    private final SensorListener mListener = new SensorListener() {
    
        public void onSensorChanged(int sensor, float[] values) {
            if (Config.LOGD) Log.d(TAG, "sensorChanged (" + values[0] + ", " + values[1] + ", " + values[2] + ")");
            mValues = values;
if (mView != null) {
mView.invalidate();
}
}

        public void onAccuracyChanged(int sensor, int accuracy) {
            // TODO Auto-generated method stub
            
}
};

//Synthetic comment -- @@ -57,6 +54,7 @@
protected void onCreate(Bundle icicle) {
super.onCreate(icicle);
mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
mView = new SampleView(this);
setContentView(mView);
}
//Synthetic comment -- @@ -66,9 +64,9 @@
{
if (Config.LOGD) Log.d(TAG, "onResume");
super.onResume();
        mSensorManager.registerListener(mListener, 
        		SensorManager.SENSOR_ORIENTATION,
        		SensorManager.SENSOR_DELAY_GAME);
}

@Override








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/SensorTest.java b/samples/ApiDemos/src/com/example/android/apis/graphics/SensorTest.java
//Synthetic comment -- index ed5b5ae..87e0461 100644

//Synthetic comment -- @@ -16,22 +16,21 @@

package com.example.android.apis.graphics;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Config;
import android.util.Log;
import android.view.View;

public class SensorTest extends GraphicsActivity {

	private SensorManager mSensorManager;
private SampleView mView;
private float[] mValues;

//Synthetic comment -- @@ -76,29 +75,31 @@
}
};

    private final SensorListener mListener = new SensorListener() {

private final float[] mScale = new float[] { 2, 2.5f, 0.5f };   // accel

private float[] mPrev = new float[3];
            
        public void onSensorChanged(int sensor, float[] values) {
boolean show = false;
float[] diff = new float[3];

for (int i = 0; i < 3; i++) {
                diff[i] = Math.round(mScale[i] * (values[i] - mPrev[i]) * 0.45f);
if (Math.abs(diff[i]) > 0) {
show = true;
}
                mPrev[i] = values[i];
}

if (show) {
// only shows if we think the delta is big enough, in an attempt
// to detect "serious" moves left/right or up/down
                android.util.Log.e("test", "sensorChanged " + sensor + " (" + values[0] + ", " + values[1] + ", " + values[2] + ")"
                                   + " diff(" + diff[0] + " " + diff[1] + " " + diff[2] + ")");
}

long now = android.os.SystemClock.uptimeMillis();
//Synthetic comment -- @@ -128,12 +129,8 @@
}
}
}
        
        private long mLastGestureTime;

        public void onAccuracyChanged(int sensor, int accuracy) {
            // TODO Auto-generated method stub
            
}
};

//Synthetic comment -- @@ -141,28 +138,24 @@
protected void onCreate(Bundle icicle) {
super.onCreate(icicle);
mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
mView = new SampleView(this);
setContentView(mView);
//        android.util.Log.d("skia", "create " + mSensorManager);
}

@Override
protected void onResume() {
super.onResume();
        
        int mask = 0;
//        mask |= SensorManager.SENSOR_ORIENTATION;
        mask |= SensorManager.SENSOR_ACCELEROMETER;
        
        mSensorManager.registerListener(mListener, mask, SensorManager.SENSOR_DELAY_FASTEST);
//        android.util.Log.d("skia", "resume " + mSensorManager);
}

@Override
protected void onStop() {
mSensorManager.unregisterListener(mListener);
super.onStop();
//        android.util.Log.d("skia", "stop " + mSensorManager);
}

private class SampleView extends View {
//Synthetic comment -- @@ -182,7 +175,8 @@
mPath.close();
}

        @Override protected void onDraw(Canvas canvas) {
Paint paint = mPaint;

canvas.drawColor(Color.WHITE);
//Synthetic comment -- @@ -216,4 +210,3 @@
}
}
}









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/os/Sensors.java b/samples/ApiDemos/src/com/example/android/apis/os/Sensors.java
//Synthetic comment -- index 910961d..9863222 100644

//Synthetic comment -- @@ -20,9 +20,10 @@
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.hardware.SensorManager;
import android.hardware.SensorListener;
import android.util.Log;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
//Synthetic comment -- @@ -47,13 +48,10 @@
* </table> 
*/
public class Sensors extends Activity {
    /** Tag string for our debug logs */
    private static final String TAG = "Sensors";

private SensorManager mSensorManager;
private GraphView mGraphView;

    private class GraphView extends View implements SensorListener
{
private Bitmap  mBitmap;
private Paint   mPaint = new Paint();
//Synthetic comment -- @@ -172,29 +170,29 @@
}
}

        public void onSensorChanged(int sensor, float[] values) {
//Log.d(TAG, "sensor: " + sensor + ", x: " + values[0] + ", y: " + values[1] + ", z: " + values[2]);
synchronized (this) {
if (mBitmap != null) {
final Canvas canvas = mCanvas;
final Paint paint = mPaint;
                    if (sensor == SensorManager.SENSOR_ORIENTATION) {
for (int i=0 ; i<3 ; i++) {
                            mOrientationValues[i] = values[i];
}
} else {
float deltaX = mSpeed;
float newX = mLastX + deltaX;

                        int j = (sensor == SensorManager.SENSOR_MAGNETIC_FIELD) ? 1 : 0;
for (int i=0 ; i<3 ; i++) {
int k = i+j*3;
                            final float v = mYOffset + values[i] * mScale[j];
paint.setColor(mColors[k]);
canvas.drawLine(mLastX, mLastValues[k], newX, v, paint);
mLastValues[k] = v;
}
                        if (sensor == SensorManager.SENSOR_MAGNETIC_FIELD)
mLastX += mSpeed;
}
invalidate();
//Synthetic comment -- @@ -202,9 +200,7 @@
}
}

        public void onAccuracyChanged(int sensor, int accuracy) {
            // TODO Auto-generated method stub
            
}
}

//Synthetic comment -- @@ -226,10 +222,14 @@
@Override
protected void onResume() {
super.onResume();
mSensorManager.registerListener(mGraphView, 
                SensorManager.SENSOR_ACCELEROMETER | 
                SensorManager.SENSOR_MAGNETIC_FIELD | 
                SensorManager.SENSOR_ORIENTATION,
SensorManager.SENSOR_DELAY_FASTEST);
}








