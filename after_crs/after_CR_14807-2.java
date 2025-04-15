/*Cleaned up Samples by removing unsed imports and variables.
Changed deprecated Config.LOGD to Config.DEBUG

Change-Id:I01414dd83eb6f9a41e56762dd7fc00e7f1115039*/




//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/AlphaBitmap.java b/samples/ApiDemos/src/com/example/android/apis/graphics/AlphaBitmap.java
//Synthetic comment -- index 8fff231..8b216cf 100644

//Synthetic comment -- @@ -18,15 +18,12 @@

import com.example.android.apis.R;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;

import java.io.InputStream;

public class AlphaBitmap extends GraphicsActivity {









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/AnimateDrawables.java b/samples/ApiDemos/src/com/example/android/apis/graphics/AnimateDrawables.java
//Synthetic comment -- index 7c9473d..9ecd26b 100644

//Synthetic comment -- @@ -18,14 +18,14 @@

import com.example.android.apis.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class AnimateDrawables extends GraphicsActivity {

//Synthetic comment -- @@ -55,7 +55,8 @@
an.startNow();
}

        @Override
        protected void onDraw(Canvas canvas) {
canvas.drawColor(Color.WHITE);

mDrawable.draw(canvas);








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Arcs.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Arcs.java
//Synthetic comment -- index ff8b38b..2efe851 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
// class is in a sub-package.
//import com.example.android.apis.R;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/BitmapDecode.java b/samples/ApiDemos/src/com/example/android/apis/graphics/BitmapDecode.java
//Synthetic comment -- index 88f0c1d..287a99a 100644

//Synthetic comment -- @@ -18,36 +18,36 @@

import com.example.android.apis.R;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.Bundle;
import android.view.*;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;

public class BitmapDecode extends GraphicsActivity {

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static class SampleView extends View {
private Bitmap mBitmap;
private Bitmap mBitmap2;
private Bitmap mBitmap3;
private Bitmap mBitmap4;
private Drawable mDrawable;

private Movie mMovie;
private long mMovieStart;

        //Set to false to use decodeByteArray
        private static boolean DECODE_STREAM = true;

private static byte[] streamToBytes(InputStream is) {
ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
byte[] buffer = new byte[1024];
//Synthetic comment -- @@ -60,33 +60,33 @@
}
return os.toByteArray();
}

public SampleView(Context context) {
super(context);
setFocusable(true);

java.io.InputStream is;
is = context.getResources().openRawResource(R.drawable.beach);

BitmapFactory.Options opts = new BitmapFactory.Options();
Bitmap bm;

opts.inJustDecodeBounds = true;
bm = BitmapFactory.decodeStream(is, null, opts);

// now opts.outWidth and opts.outHeight are the dimension of the
// bitmap, even though bm is null

opts.inJustDecodeBounds = false;    // this will request the bm
opts.inSampleSize = 4;             // scaled down by 4
bm = BitmapFactory.decodeStream(is, null, opts);

mBitmap = bm;

// decode an image with transparency
is = context.getResources().openRawResource(R.drawable.frog);
mBitmap2 = BitmapFactory.decodeStream(is);

// create a deep copy of it using getPixels() into different configs
int w = mBitmap2.getWidth();
int h = mBitmap2.getHeight();
//Synthetic comment -- @@ -96,32 +96,34 @@
Bitmap.Config.ARGB_8888);
mBitmap4 = Bitmap.createBitmap(pixels, 0, w, w, h,
Bitmap.Config.ARGB_4444);

mDrawable = context.getResources().getDrawable(R.drawable.button);
mDrawable.setBounds(150, 20, 300, 100);

is = context.getResources().openRawResource(R.drawable.animated_gif);

            if (DECODE_STREAM) {
mMovie = Movie.decodeStream(is);
} else {
byte[] array = streamToBytes(is);
mMovie = Movie.decodeByteArray(array, 0, array.length);
}
}

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(0xFFCCCCCC);

Paint p = new Paint();
p.setAntiAlias(true);

canvas.drawBitmap(mBitmap, 10, 10, null);
canvas.drawBitmap(mBitmap2, 10, 170, null);
canvas.drawBitmap(mBitmap3, 110, 170, null);
canvas.drawBitmap(mBitmap4, 210, 170, null);

mDrawable.draw(canvas);

long now = android.os.SystemClock.uptimeMillis();
if (mMovieStart == 0) {   // first time
mMovieStart = now;
//Synthetic comment -- @@ -140,4 +142,3 @@
}
}
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/BitmapPixels.java b/samples/ApiDemos/src/com/example/android/apis/graphics/BitmapPixels.java
//Synthetic comment -- index 88717bc..e608f83 100644

//Synthetic comment -- @@ -16,14 +16,9 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;

import java.nio.IntBuffer;
//Synthetic comment -- @@ -41,7 +36,6 @@
private Bitmap mBitmap1;
private Bitmap mBitmap2;
private Bitmap mBitmap3;

// access the red component from a premultiplied color
private static int getR32(int c) { return (c >>  0) & 0xFF; }
//Synthetic comment -- @@ -165,4 +159,3 @@
}
}
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Clipping.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Clipping.java
//Synthetic comment -- index cf83597..6898e88 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/ColorFilters.java b/samples/ApiDemos/src/com/example/android/apis/graphics/ColorFilters.java
//Synthetic comment -- index 92d18ba..8eb60b5 100644

//Synthetic comment -- @@ -23,18 +23,17 @@
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.Bundle;
import android.view.*;

public class ColorFilters extends GraphicsActivity {

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));

}

private static class SampleView extends View {
private Activity mActivity;
private Drawable mDrawable;
//Synthetic comment -- @@ -52,7 +51,7 @@
int center = (r.top + r.bottom) >> 1;
int h = curr.getIntrinsicHeight();
int y = center - (h >> 1);

curr.setBounds(x, y, x + curr.getIntrinsicWidth(), y + h);
}

//Synthetic comment -- @@ -61,7 +60,7 @@
mActivity = activity;
Context context = activity;
setFocusable(true);

mDrawable = context.getResources().getDrawable(R.drawable.btn_default_normal);
mDrawable.setBounds(0, 0, 150, 48);
mDrawable.setDither(true);
//Synthetic comment -- @@ -84,13 +83,13 @@
mPaint.setAntiAlias(true);
mPaint.setTextSize(16);
mPaint.setTextAlign(Paint.Align.CENTER);

mPaint2 = new Paint(mPaint);
mPaint2.setAlpha(64);

Paint.FontMetrics fm = mPaint.getFontMetrics();
mPaintTextOffset = (fm.descent + fm.ascent) * 0.5f;

mColors = new int[] {
0,
0xCC0000FF,
//Synthetic comment -- @@ -106,10 +105,10 @@
PorterDuff.Mode.MULTIPLY,
};
mModeIndex = 0;

updateTitle();
}

private void swapPaintColors() {
if (mPaint.getColor() == 0xFF000000) {
mPaint.setColor(0xFFFFFFFF);
//Synthetic comment -- @@ -120,11 +119,11 @@
}
mPaint2.setAlpha(64);
}

private void updateTitle() {
mActivity.setTitle(mModes[mModeIndex].toString());
}

private void drawSample(Canvas canvas, ColorFilter filter) {
Rect r = mDrawable.getBounds();
float x = (r.left + r.right) * 0.5f;
//Synthetic comment -- @@ -134,15 +133,15 @@
mDrawable.draw(canvas);
canvas.drawText("Label", x+1, y+1, mPaint2);
canvas.drawText("Label", x, y, mPaint);

for (Drawable dr : mDrawables) {
dr.setColorFilter(filter);
dr.draw(canvas);
}
}

@Override protected void onDraw(Canvas canvas) {
            canvas.drawColor(0xFFCCCCCC);

canvas.translate(8, 12);
for (int color : mColors) {
//Synthetic comment -- @@ -160,8 +159,6 @@

@Override
public boolean onTouchEvent(MotionEvent event) {
switch (event.getAction()) {
case MotionEvent.ACTION_DOWN:
break;
//Synthetic comment -- @@ -181,4 +178,3 @@
}
}
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/ColorMatrixSample.java b/samples/ApiDemos/src/com/example/android/apis/graphics/ColorMatrixSample.java
//Synthetic comment -- index 19a0f7f..a9049e9 100644

//Synthetic comment -- @@ -18,11 +18,9 @@

import com.example.android.apis.R;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.View;

public class ColorMatrixSample extends GraphicsActivity {
//Synthetic comment -- @@ -35,9 +33,7 @@

private static class SampleView extends View {
private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
private Bitmap mBitmap;
private float mAngle;

public SampleView(Context context) {








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Compass.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Compass.java
//Synthetic comment -- index d3b0981..2a5705a 100644

//Synthetic comment -- @@ -38,7 +38,7 @@

private final SensorEventListener mListener = new SensorEventListener() {
public void onSensorChanged(SensorEvent event) {
            if (Config.DEBUG) Log.d(TAG,
"sensorChanged (" + event.values[0] + ", " + event.values[1] + ", " + event.values[2] + ")");
mValues = event.values;
if (mView != null) {
//Synthetic comment -- @@ -62,7 +62,7 @@
@Override
protected void onResume()
{
        if (Config.DEBUG) Log.d(TAG, "onResume");
super.onResume();

mSensorManager.registerListener(mListener, mSensor,
//Synthetic comment -- @@ -72,7 +72,7 @@
@Override
protected void onStop()
{
        if (Config.DEBUG) Log.d(TAG, "onStop");
mSensorManager.unregisterListener(mListener);
super.onStop();
}
//Synthetic comment -- @@ -81,7 +81,6 @@
private Paint   mPaint = new Paint();
private Path    mPath = new Path();
private boolean mAnimate;

public SampleView(Context context) {
super(context);
//Synthetic comment -- @@ -118,14 +117,15 @@
@Override
protected void onAttachedToWindow() {
mAnimate = true;
            if (Config.DEBUG) Log.d(TAG, "onAttachedToWindow. mAnimate=" + mAnimate);
super.onAttachedToWindow();
}

@Override
protected void onDetachedFromWindow() {
mAnimate = false;
            if (Config.DEBUG) Log.d(TAG, "onDetachedFromWindow. mAnimate=" + mAnimate);
super.onDetachedFromWindow();
}
}
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/CreateBitmap.java b/samples/ApiDemos/src/com/example/android/apis/graphics/CreateBitmap.java
//Synthetic comment -- index e3e5d9a..d6188d9 100644

//Synthetic comment -- @@ -16,13 +16,9 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;

import java.io.ByteArrayOutputStream;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Cube.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Cube.java
//Synthetic comment -- index bb154eb..715e175 100644

//Synthetic comment -- @@ -88,10 +88,10 @@

public void draw(GL10 gl)
{
        gl.glFrontFace(GL10.GL_CW);
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
        gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
}

private IntBuffer   mVertexBuffer;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/CubeRenderer.java b/samples/ApiDemos/src/com/example/android/apis/graphics/CubeRenderer.java
//Synthetic comment -- index 0f15f91..ac0ae27 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/DensityActivity.java b/samples/ApiDemos/src/com/example/android/apis/graphics/DensityActivity.java
//Synthetic comment -- index 10c42a7..58c3b37 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import com.example.android.apis.R;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
//Synthetic comment -- @@ -34,8 +33,6 @@
import android.view.LayoutInflater;
import android.view.View;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/DrawPoints.java b/samples/ApiDemos/src/com/example/android/apis/graphics/DrawPoints.java
//Synthetic comment -- index cbe6373..c5cf77a 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/FingerPaint.java b/samples/ApiDemos/src/com/example/android/apis/graphics/FingerPaint.java
//Synthetic comment -- index 867da4c..5160e35 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/GradientDrawable1.java b/samples/ApiDemos/src/com/example/android/apis/graphics/GradientDrawable1.java
//Synthetic comment -- index eb6d47d..fa7a6b7 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
// class is in a sub-package.
import com.example.android.apis.R;

import android.os.Bundle;

public class GradientDrawable1 extends GraphicsActivity {








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/GraphicsActivity.java b/samples/ApiDemos/src/com/example/android/apis/graphics/GraphicsActivity.java
//Synthetic comment -- index 023c0d7..69682d4 100644

//Synthetic comment -- @@ -20,9 +20,11 @@
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

class GraphicsActivity extends Activity {
    // set to true to test Picture
    private static final boolean TEST_PICTURE = false;

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
//Synthetic comment -- @@ -30,13 +32,12 @@

@Override
public void setContentView(View view) {
        if (TEST_PICTURE) {
ViewGroup vg = new PictureLayout(this);
vg.addView(view);
view = vg;
}

super.setContentView(view);
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Layers.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Layers.java
//Synthetic comment -- index d9f5db0..1ea23ae 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/MeasureText.java b/samples/ApiDemos/src/com/example/android/apis/graphics/MeasureText.java
//Synthetic comment -- index e159efe..60f50c0 100644

//Synthetic comment -- @@ -16,13 +16,9 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;

public class MeasureText extends GraphicsActivity {
//Synthetic comment -- @@ -114,4 +110,3 @@
}
}
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/PathEffects.java b/samples/ApiDemos/src/com/example/android/apis/graphics/PathEffects.java
//Synthetic comment -- index 80ddf38..73ee111 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/PathFillTypes.java b/samples/ApiDemos/src/com/example/android/apis/graphics/PathFillTypes.java
//Synthetic comment -- index 78dba26..5decd3e 100644

//Synthetic comment -- @@ -20,11 +20,9 @@
// class is in a sub-package.
//import com.example.android.apis.R;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.View;

public class PathFillTypes extends GraphicsActivity {








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Patterns.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Patterns.java
//Synthetic comment -- index d2a51ff..5d37192 100644

//Synthetic comment -- @@ -16,11 +16,9 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.*;









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/PictureLayout.java b/samples/ApiDemos/src/com/example/android/apis/graphics/PictureLayout.java
//Synthetic comment -- index 9bdb49a..8ea6805 100644

//Synthetic comment -- @@ -26,7 +26,6 @@
import android.view.ViewGroup;
import android.view.ViewParent;

public class PictureLayout extends ViewGroup {
private final Picture mPicture = new Picture();









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Pictures.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Pictures.java
//Synthetic comment -- index 1bd0a8c..d8e2df3 100644

//Synthetic comment -- @@ -16,13 +16,11 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.view.View;

import java.io.*;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/PolyToPoly.java b/samples/ApiDemos/src/com/example/android/apis/graphics/PolyToPoly.java
//Synthetic comment -- index 15d92de..42328ab 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
// class is in a sub-package.
//import com.example.android.apis.R;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
//Synthetic comment -- @@ -73,9 +72,8 @@
mFontMetrics = mPaint.getFontMetrics();
}

        @Override
        protected void onDraw(Canvas canvas) {
canvas.drawColor(Color.WHITE);

canvas.save();
//Synthetic comment -- @@ -107,4 +105,3 @@
}
}
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Regions.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Regions.java
//Synthetic comment -- index 833274b..1ab91e2 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
//Synthetic comment -- @@ -91,7 +90,8 @@
r.right - inset, r.bottom - inset, p);
}

        @Override
        protected void onDraw(Canvas canvas) {
canvas.drawColor(Color.GRAY);            

canvas.save();








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/RoundRects.java b/samples/ApiDemos/src/com/example/android/apis/graphics/RoundRects.java
//Synthetic comment -- index b0ff0359..cf94e1d 100644

//Synthetic comment -- @@ -16,14 +16,10 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.Bundle;
import android.view.*;

public class RoundRects extends GraphicsActivity {
//Synthetic comment -- @@ -112,8 +108,6 @@
setCornerRadii(mDrawable, 0, r, 0, r);
mDrawable.draw(canvas);
canvas.restore();
}
}
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/ScaleToFit.java b/samples/ApiDemos/src/com/example/android/apis/graphics/ScaleToFit.java
//Synthetic comment -- index f55e55b..43db751 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
//Synthetic comment -- @@ -92,9 +91,8 @@
canvas.drawRect(mDstR, mHairPaint);
}

        @Override
        protected void onDraw(Canvas canvas) {
canvas.drawColor(Color.WHITE);

canvas.translate(10, 10);
//Synthetic comment -- @@ -121,4 +119,3 @@
}
}
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/SensorTest.java b/samples/ApiDemos/src/com/example/android/apis/graphics/SensorTest.java
//Synthetic comment -- index 87e0461..1c016ba 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Config;
import android.util.Log;
import android.view.View;

public class SensorTest extends GraphicsActivity {
//Synthetic comment -- @@ -96,7 +97,7 @@
if (show) {
// only shows if we think the delta is big enough, in an attempt
// to detect "serious" moves left/right or up/down
                Log.e(TAG, "sensorChanged " + event.sensor.getName() +
" (" + event.values[0] + ", " + event.values[1] + ", " +
event.values[2] + ")" + " diff(" + diff[0] +
" " + diff[1] + " " + diff[2] + ")");
//Synthetic comment -- @@ -114,15 +115,15 @@
if ((gestX || gestY) && !(gestX && gestY)) {
if (gestX) {
if (x < 0) {
                            Log.e("test", "<<<<<<<< LEFT <<<<<<<<<<<<");
} else {
                            Log.e("test", ">>>>>>>>> RITE >>>>>>>>>>>");
}
} else {
if (y < -2) {
                            Log.e("test", "<<<<<<<< UP <<<<<<<<<<<<");
} else {
                            Log.e("test", ">>>>>>>>> DOWN >>>>>>>>>>>");
}
}
mLastGestureTime = now;
//Synthetic comment -- @@ -141,28 +142,27 @@
mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
mView = new SampleView(this);
setContentView(mView);
        if (Config.DEBUG) Log.d(TAG, "create " + mSensorManager);
}

@Override
protected void onResume() {
super.onResume();
mSensorManager.registerListener(mListener, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
        if (Config.DEBUG) Log.d(TAG, "resume " + mSensorManager);
}

@Override
protected void onStop() {
mSensorManager.unregisterListener(mListener);
super.onStop();
        if (Config.DEBUG) Log.d(TAG, "stop " + mSensorManager);
}

private class SampleView extends View {
private Paint   mPaint = new Paint();
private Path    mPath = new Path();
private boolean mAnimate;

public SampleView(Context context) {
super(context);
//Synthetic comment -- @@ -200,12 +200,14 @@
@Override
protected void onAttachedToWindow() {
mAnimate = true;
            if (Config.DEBUG) Log.d(TAG, "onAttachedToWindow. mAnimate="+mAnimate);
super.onAttachedToWindow();
}

@Override
protected void onDetachedFromWindow() {
mAnimate = false;
            if (Config.DEBUG) Log.d(TAG, "onAttachedToWindow. mAnimate="+mAnimate);
super.onDetachedFromWindow();
}
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/ShapeDrawable1.java b/samples/ApiDemos/src/com/example/android/apis/graphics/ShapeDrawable1.java
//Synthetic comment -- index 6d450bb..acde814 100644

//Synthetic comment -- @@ -16,16 +16,12 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.*;
import android.os.Bundle;
import android.view.*;

public class ShapeDrawable1 extends GraphicsActivity {








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Sweep.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Sweep.java
//Synthetic comment -- index dc127fd..1ad2ee6 100644

//Synthetic comment -- @@ -16,11 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/TextAlign.java b/samples/ApiDemos/src/com/example/android/apis/graphics/TextAlign.java
//Synthetic comment -- index 0576a7c..2cc72b1 100644

//Synthetic comment -- @@ -16,13 +16,9 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;

public class TextAlign extends GraphicsActivity {








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/TouchPaint.java b/samples/ApiDemos/src/com/example/android/apis/graphics/TouchPaint.java
//Synthetic comment -- index 0942852..75a189c 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/TouchRotateActivity.java b/samples/ApiDemos/src/com/example/android/apis/graphics/TouchRotateActivity.java
//Synthetic comment -- index c0f32a7..4133c04 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/TriangleRenderer.java b/samples/ApiDemos/src/com/example/android/apis/graphics/TriangleRenderer.java
//Synthetic comment -- index e5299b3..0433af1 100644

//Synthetic comment -- @@ -23,7 +23,6 @@
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Typefaces.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Typefaces.java
//Synthetic comment -- index aefc311..e36162c 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/UnicodeChart.java b/samples/ApiDemos/src/com/example/android/apis/graphics/UnicodeChart.java
//Synthetic comment -- index 7ee99d0..709ffbd 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
//Synthetic comment -- @@ -64,7 +63,7 @@
float[] pos = mPos;
int index = 0;
for (int col = 0; col < 16; col++) {
                final float x = col * XMUL + 10;
for (int row = 0; row < 16; row++) {
pos[index++] = x;
pos[index++] = row * YMUL + YBASE;
//Synthetic comment -- @@ -73,7 +72,7 @@
}

private float computeX(int index) {
            return (index >> 4) * XMUL + 10;
}

private float computeY(int index) {
//Synthetic comment -- @@ -118,4 +117,3 @@
}
}
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Vertices.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Vertices.java
//Synthetic comment -- index 1e61906..2d82d23 100644

//Synthetic comment -- @@ -18,17 +18,11 @@

import com.example.android.apis.R;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;

public class Vertices extends GraphicsActivity {

@Override
//Synthetic comment -- @@ -41,7 +35,6 @@
private final Paint mPaint = new Paint();
private final float[] mVerts = new float[10];
private final float[] mTexs = new float[10];
private final short[] mIndices = { 0, 1, 2, 3, 4, 1 };

private final Matrix mMatrix = new Matrix();








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Xfermodes.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Xfermodes.java
//Synthetic comment -- index b9f8424..791013a 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;







