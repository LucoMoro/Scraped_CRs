/*Updated Sensor Samples to newest API

Change-Id:I88c0ce08232fed34aa99c97385155220e7d9abff*/




//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Compass.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Compass.java
//Synthetic comment -- index d2a9907..d3b0981 100644

//Synthetic comment -- @@ -16,15 +16,13 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Config;
import android.util.Log;
import android.view.View;
//Synthetic comment -- @@ -33,23 +31,22 @@

private static final String TAG = "Compass";

    private SensorManager mSensorManager;
    private Sensor mSensor;
private SampleView mView;
private float[] mValues;

    private final SensorEventListener mListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            if (Config.LOGD) Log.d(TAG,
                    "sensorChanged (" + event.values[0] + ", " + event.values[1] + ", " + event.values[2] + ")");
            mValues = event.values;
if (mView != null) {
mView.invalidate();
}
}

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
}
};

//Synthetic comment -- @@ -57,6 +54,7 @@
protected void onCreate(Bundle icicle) {
super.onCreate(icicle);
mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
mView = new SampleView(this);
setContentView(mView);
}
//Synthetic comment -- @@ -66,9 +64,9 @@
{
if (Config.LOGD) Log.d(TAG, "onResume");
super.onResume();

        mSensorManager.registerListener(mListener, mSensor,
                SensorManager.SENSOR_DELAY_GAME);
}

@Override








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/SensorTest.java b/samples/ApiDemos/src/com/example/android/apis/graphics/SensorTest.java
//Synthetic comment -- index ed5b5ae..87e0461 100644

//Synthetic comment -- @@ -16,22 +16,21 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Config;
import android.view.View;

public class SensorTest extends GraphicsActivity {
    private final String TAG = "SensorTest";

    private SensorManager mSensorManager;
    private Sensor mSensor;
private SampleView mView;
private float[] mValues;

//Synthetic comment -- @@ -76,29 +75,31 @@
}
};

    private final SensorEventListener mListener = new SensorEventListener() {

private final float[] mScale = new float[] { 2, 2.5f, 0.5f };   // accel
private float[] mPrev = new float[3];
        private long mLastGestureTime;

        public void onSensorChanged(SensorEvent event) {
boolean show = false;
float[] diff = new float[3];

for (int i = 0; i < 3; i++) {
                diff[i] = Math.round(mScale[i] * (event.values[i] - mPrev[i]) * 0.45f);
if (Math.abs(diff[i]) > 0) {
show = true;
}
                mPrev[i] = event.values[i];
}

if (show) {
// only shows if we think the delta is big enough, in an attempt
// to detect "serious" moves left/right or up/down
                android.util.Log.e(TAG, "sensorChanged " + event.sensor.getName() +
                        " (" + event.values[0] + ", " + event.values[1] + ", " +
                        event.values[2] + ")" + " diff(" + diff[0] +
                        " " + diff[1] + " " + diff[2] + ")");
}

long now = android.os.SystemClock.uptimeMillis();
//Synthetic comment -- @@ -128,12 +129,8 @@
}
}
}

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
}
};

//Synthetic comment -- @@ -141,28 +138,24 @@
protected void onCreate(Bundle icicle) {
super.onCreate(icicle);
mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
mView = new SampleView(this);
setContentView(mView);
        if (Config.LOGD) android.util.Log.d(TAG, "create " + mSensorManager);
}

@Override
protected void onResume() {
super.onResume();
        mSensorManager.registerListener(mListener, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
        if (Config.LOGD) android.util.Log.d(TAG, "resume " + mSensorManager);
}

@Override
protected void onStop() {
mSensorManager.unregisterListener(mListener);
super.onStop();
        if (Config.LOGD) android.util.Log.d(TAG, "stop " + mSensorManager);
}

private class SampleView extends View {
//Synthetic comment -- @@ -182,7 +175,8 @@
mPath.close();
}

        @Override
        protected void onDraw(Canvas canvas) {
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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
//Synthetic comment -- @@ -47,13 +48,10 @@
* </table> 
*/
public class Sensors extends Activity {
private SensorManager mSensorManager;
private GraphView mGraphView;

    private class GraphView extends View implements SensorEventListener
{
private Bitmap  mBitmap;
private Paint   mPaint = new Paint();
//Synthetic comment -- @@ -172,29 +170,29 @@
}
}

        public void onSensorChanged(SensorEvent event) {
//Log.d(TAG, "sensor: " + sensor + ", x: " + values[0] + ", y: " + values[1] + ", z: " + values[2]);
synchronized (this) {
if (mBitmap != null) {
final Canvas canvas = mCanvas;
final Paint paint = mPaint;
                    if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
for (int i=0 ; i<3 ; i++) {
                            mOrientationValues[i] = event.values[i];
}
} else {
float deltaX = mSpeed;
float newX = mLastX + deltaX;

                        int j = (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) ? 1 : 0;
for (int i=0 ; i<3 ; i++) {
int k = i+j*3;
                            final float v = mYOffset + event.values[i] * mScale[j];
paint.setColor(mColors[k]);
canvas.drawLine(mLastX, mLastValues[k], newX, v, paint);
mLastValues[k] = v;
}
                        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
mLastX += mSpeed;
}
invalidate();
//Synthetic comment -- @@ -202,9 +200,7 @@
}
}

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
}
}

//Synthetic comment -- @@ -226,10 +222,14 @@
@Override
protected void onResume() {
super.onResume();
        mSensorManager.registerListener(mGraphView,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(mGraphView,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_FASTEST);
mSensorManager.registerListener(mGraphView, 
                mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
SensorManager.SENSOR_DELAY_FASTEST);
}








