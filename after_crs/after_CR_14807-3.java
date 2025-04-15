/*Cleaned up Samples by removing unsed imports and variables.
Changed deprecated Config.LOGD to Config.DEBUG
Removed unnecessary whitespaces

Change-Id:I01414dd83eb6f9a41e56762dd7fc00e7f1115039*/




//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/AlphaBitmap.java b/samples/ApiDemos/src/com/example/android/apis/graphics/AlphaBitmap.java
//Synthetic comment -- index 8fff231..90d3450 100644

//Synthetic comment -- @@ -18,15 +18,12 @@

import com.example.android.apis.R;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;

import java.io.InputStream;

public class AlphaBitmap extends GraphicsActivity {

//Synthetic comment -- @@ -35,23 +32,23 @@
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static class SampleView extends View {
private Bitmap mBitmap;
private Bitmap mBitmap2;
private Bitmap mBitmap3;
private Shader mShader;

private static void drawIntoBitmap(Bitmap bm) {
float x = bm.getWidth();
float y = bm.getHeight();
Canvas c = new Canvas(bm);
Paint p = new Paint();
p.setAntiAlias(true);

p.setAlpha(0x80);
c.drawCircle(x/2, y/2, x/2, p);

p.setAlpha(0x30);
p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
p.setTextSize(60);
//Synthetic comment -- @@ -59,28 +56,28 @@
Paint.FontMetrics fm = p.getFontMetrics();
c.drawText("Alpha", x/2, (y-fm.ascent)/2, p);
}

public SampleView(Context context) {
super(context);
setFocusable(true);

InputStream is = context.getResources().openRawResource(R.drawable.app_sample_code);
mBitmap = BitmapFactory.decodeStream(is);
mBitmap2 = mBitmap.extractAlpha();
mBitmap3 = Bitmap.createBitmap(200, 200, Bitmap.Config.ALPHA_8);
drawIntoBitmap(mBitmap3);

mShader = new LinearGradient(0, 0, 100, 70, new int[] {
Color.RED, Color.GREEN, Color.BLUE },
null, Shader.TileMode.MIRROR);
}

@Override protected void onDraw(Canvas canvas) {
canvas.drawColor(Color.WHITE);

Paint p = new Paint();
float y = 10;

p.setColor(Color.RED);
canvas.drawBitmap(mBitmap, 10, y, p);
y += mBitmap.getHeight() + 10;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/AnimateDrawable.java b/samples/ApiDemos/src/com/example/android/apis/graphics/AnimateDrawable.java
//Synthetic comment -- index 279b588..330924e 100644

//Synthetic comment -- @@ -23,23 +23,23 @@
import android.view.animation.Transformation;

public class AnimateDrawable extends ProxyDrawable {

private Animation mAnimation;
private Transformation mTransformation = new Transformation();

public AnimateDrawable(Drawable target) {
super(target);
}

public AnimateDrawable(Drawable target, Animation animation) {
super(target);
mAnimation = animation;
}

public Animation getAnimation() {
return mAnimation;
}

public void setAnimation(Animation anim) {
mAnimation = anim;
}
//Synthetic comment -- @@ -47,11 +47,11 @@
public boolean hasStarted() {
return mAnimation != null && mAnimation.hasStarted();
}

public boolean hasEnded() {
return mAnimation == null || mAnimation.hasEnded();
}

@Override
public void draw(Canvas canvas) {
Drawable dr = getProxy();
//Synthetic comment -- @@ -69,4 +69,4 @@
}
}
}









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/AnimateDrawables.java b/samples/ApiDemos/src/com/example/android/apis/graphics/AnimateDrawables.java
//Synthetic comment -- index 7c9473d..0398fbf 100644

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

//Synthetic comment -- @@ -34,7 +34,7 @@
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static class SampleView extends View {
private AnimateDrawable mDrawable;

//Synthetic comment -- @@ -45,17 +45,18 @@

Drawable dr = context.getResources().getDrawable(R.drawable.beach);
dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());

Animation an = new TranslateAnimation(0, 100, 0, 200);
an.setDuration(2000);
an.setRepeatCount(-1);
an.initialize(10, 10, 10, 10);

mDrawable = new AnimateDrawable(dr, an);
an.startNow();
}

        @Override
        protected void onDraw(Canvas canvas) {
canvas.drawColor(Color.WHITE);

mDrawable.draw(canvas);








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Arcs.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Arcs.java
//Synthetic comment -- index ff8b38b..1bd07f9 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
// class is in a sub-package.
//import com.example.android.apis.R;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
//Synthetic comment -- @@ -33,7 +32,7 @@
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static class SampleView extends View {
private Paint[] mPaints;
private Paint mFramePaint;
//Synthetic comment -- @@ -43,27 +42,27 @@
private float mStart;
private float mSweep;
private int mBigIndex;

private static final float SWEEP_INC = 2;
private static final float START_INC = 15;

public SampleView(Context context) {
super(context);

mPaints = new Paint[4];
mUseCenters = new boolean[4];
mOvals = new RectF[4];

mPaints[0] = new Paint();
mPaints[0].setAntiAlias(true);
mPaints[0].setStyle(Paint.Style.FILL);
mPaints[0].setColor(0x88FF0000);
mUseCenters[0] = false;

mPaints[1] = new Paint(mPaints[0]);
mPaints[1].setColor(0x8800FF00);
mUseCenters[1] = true;

mPaints[2] = new Paint(mPaints[0]);
mPaints[2].setStyle(Paint.Style.STROKE);
mPaints[2].setStrokeWidth(4);
//Synthetic comment -- @@ -73,36 +72,36 @@
mPaints[3] = new Paint(mPaints[2]);
mPaints[3].setColor(0x88888888);
mUseCenters[3] = true;

mBigOval = new RectF(40, 10, 280, 250);

mOvals[0] = new RectF( 10, 270,  70, 330);
mOvals[1] = new RectF( 90, 270, 150, 330);
mOvals[2] = new RectF(170, 270, 230, 330);
mOvals[3] = new RectF(250, 270, 310, 330);

mFramePaint = new Paint();
mFramePaint.setAntiAlias(true);
mFramePaint.setStyle(Paint.Style.STROKE);
mFramePaint.setStrokeWidth(0);
}

private void drawArcs(Canvas canvas, RectF oval, boolean useCenter,
Paint paint) {
canvas.drawRect(oval, mFramePaint);
canvas.drawArc(oval, mStart, mSweep, useCenter, paint);
}

@Override protected void onDraw(Canvas canvas) {
canvas.drawColor(Color.WHITE);

drawArcs(canvas, mBigOval, mUseCenters[mBigIndex],
mPaints[mBigIndex]);

for (int i = 0; i < 4; i++) {
drawArcs(canvas, mOvals[i], mUseCenters[i], mPaints[i]);
}

mSweep += SWEEP_INC;
if (mSweep > 360) {
mSweep -= 360;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/BitmapDecode.java b/samples/ApiDemos/src/com/example/android/apis/graphics/BitmapDecode.java
//Synthetic comment -- index 88f0c1d..6a8b542 100644

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
        private static final boolean DECODE_STREAM = true;

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








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/BitmapMesh.java b/samples/ApiDemos/src/com/example/android/apis/graphics/BitmapMesh.java
//Synthetic comment -- index 4d48a1e..12c79ca 100644

//Synthetic comment -- @@ -31,16 +31,16 @@
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static class SampleView extends View {
private static final int WIDTH = 20;
private static final int HEIGHT = 20;
private static final int COUNT = (WIDTH + 1) * (HEIGHT + 1);

private final Bitmap mBitmap;
private final float[] mVerts = new float[COUNT*2];
private final float[] mOrig = new float[COUNT*2];

private final Matrix mMatrix = new Matrix();
private final Matrix mInverse = new Matrix();

//Synthetic comment -- @@ -55,7 +55,7 @@

mBitmap = BitmapFactory.decodeResource(getResources(),
R.drawable.beach);

float w = mBitmap.getWidth();
float h = mBitmap.getHeight();
// construct our mesh
//Synthetic comment -- @@ -63,17 +63,17 @@
for (int y = 0; y <= HEIGHT; y++) {
float fy = h * y / HEIGHT;
for (int x = 0; x <= WIDTH; x++) {
                    float fx = w * x / WIDTH;
setXY(mVerts, index, fx, fy);
setXY(mOrig, index, fx, fy);
index += 1;
}
}

mMatrix.setTranslate(10, 10);
mMatrix.invert(mInverse);
}

@Override protected void onDraw(Canvas canvas) {
canvas.drawColor(0xFFCCCCCC);

//Synthetic comment -- @@ -81,7 +81,7 @@
canvas.drawBitmapMesh(mBitmap, WIDTH, HEIGHT, mVerts, 0,
null, 0, null);
}

private void warp(float cx, float cy) {
final float K = 10000;
float[] src = mOrig;
//Synthetic comment -- @@ -94,7 +94,7 @@
float dd = dx*dx + dy*dy;
float d = FloatMath.sqrt(dd);
float pull = K / (dd + 0.000001f);

pull /= (d + 0.000001f);
//   android.util.Log.d("skia", "index " + i + " dist=" + d + " pull=" + pull);

//Synthetic comment -- @@ -114,7 +114,7 @@
@Override public boolean onTouchEvent(MotionEvent event) {
float[] pt = { event.getX(), event.getY() };
mInverse.mapPoints(pt);

int x = (int)pt[0];
int y = (int)pt[1];
if (mLastWarpX != x || mLastWarpY != y) {








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/BitmapPixels.java b/samples/ApiDemos/src/com/example/android/apis/graphics/BitmapPixels.java
//Synthetic comment -- index 88717bc..e9b2167 100644

//Synthetic comment -- @@ -16,32 +16,26 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class BitmapPixels extends GraphicsActivity {

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static class SampleView extends View {
private Bitmap mBitmap1;
private Bitmap mBitmap2;
private Bitmap mBitmap3;

// access the red component from a premultiplied color
private static int getR32(int c) { return (c >>  0) & 0xFF; }
//Synthetic comment -- @@ -67,7 +61,7 @@
private static short pack4444(int r, int g, int b, int a) {
return (short)((a << 0) | ( b << 4) | (g << 8) | (r << 12));
}

private static int mul255(int c, int a) {
int prod = c * a + 128;
return (prod + (prod >> 8)) >> 8;
//Synthetic comment -- @@ -88,7 +82,7 @@
// now pack it in the correct order
return pack8888(r, g, b, a);
}

private static void makeRamp(int from, int to, int n,
int[] ramp8888, short[] ramp565,
short[] ramp4444) {
//Synthetic comment -- @@ -113,7 +107,7 @@
a += da;
}
}

private static IntBuffer makeBuffer(int[] src, int n) {
IntBuffer dst = IntBuffer.allocate(n*n);
for (int i = 0; i < n; i++) {
//Synthetic comment -- @@ -122,7 +116,7 @@
dst.rewind();
return dst;
}

private static ShortBuffer makeBuffer(short[] src, int n) {
ShortBuffer dst = ShortBuffer.allocate(n*n);
for (int i = 0; i < n; i++) {
//Synthetic comment -- @@ -131,31 +125,31 @@
dst.rewind();
return dst;
}

public SampleView(Context context) {
super(context);
setFocusable(true);

final int N = 100;
int[] data8888 = new int[N];
short[] data565 = new short[N];
short[] data4444 = new short[N];

makeRamp(premultiplyColor(Color.RED), premultiplyColor(Color.GREEN),
N, data8888, data565, data4444);

mBitmap1 = Bitmap.createBitmap(N, N, Bitmap.Config.ARGB_8888);
mBitmap2 = Bitmap.createBitmap(N, N, Bitmap.Config.RGB_565);
mBitmap3 = Bitmap.createBitmap(N, N, Bitmap.Config.ARGB_4444);

mBitmap1.copyPixelsFromBuffer(makeBuffer(data8888, N));
mBitmap2.copyPixelsFromBuffer(makeBuffer(data565, N));
mBitmap3.copyPixelsFromBuffer(makeBuffer(data4444, N));
}

@Override protected void onDraw(Canvas canvas) {
            canvas.drawColor(0xFFCCCCCC);

int y = 10;
canvas.drawBitmap(mBitmap1, 10, y, null);
y += mBitmap1.getHeight() + 10;
//Synthetic comment -- @@ -165,4 +159,3 @@
}
}
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/CameraPreview.java b/samples/ApiDemos/src/com/example/android/apis/graphics/CameraPreview.java
//Synthetic comment -- index e3cf976..f28690f 100644

//Synthetic comment -- @@ -27,16 +27,16 @@

// ----------------------------------------------------------------------

public class CameraPreview extends Activity {
private Preview mPreview;

@Override
	protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);

// Hide the window title.
requestWindowFeature(Window.FEATURE_NO_TITLE);

// Create our Preview view and set it as the content of our activity.
mPreview = new Preview(this);
setContentView(mPreview);
//Synthetic comment -- @@ -49,10 +49,10 @@
class Preview extends SurfaceView implements SurfaceHolder.Callback {
SurfaceHolder mHolder;
Camera mCamera;

Preview(Context context) {
super(context);

// Install a SurfaceHolder.Callback so we get notified when the
// underlying surface is created and destroyed.
mHolder = getHolder();








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Clipping.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Clipping.java
//Synthetic comment -- index cf83597..42f8be3 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
//Synthetic comment -- @@ -29,7 +28,7 @@
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static class SampleView extends View {
private Paint mPaint;
private Path mPath;
//Synthetic comment -- @@ -37,46 +36,46 @@
public SampleView(Context context) {
super(context);
setFocusable(true);

mPaint = new Paint();
mPaint.setAntiAlias(true);
mPaint.setStrokeWidth(6);
mPaint.setTextSize(16);
mPaint.setTextAlign(Paint.Align.RIGHT);

mPath = new Path();
}

private void drawScene(Canvas canvas) {
canvas.clipRect(0, 0, 100, 100);

canvas.drawColor(Color.WHITE);

mPaint.setColor(Color.RED);
canvas.drawLine(0, 0, 100, 100, mPaint);

mPaint.setColor(Color.GREEN);
canvas.drawCircle(30, 70, 30, mPaint);

mPaint.setColor(Color.BLUE);
canvas.drawText("Clipping", 100, 30, mPaint);
}

@Override protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.GRAY);

canvas.save();
canvas.translate(10, 10);
drawScene(canvas);
canvas.restore();

canvas.save();
canvas.translate(160, 10);
canvas.clipRect(10, 10, 90, 90);
canvas.clipRect(30, 30, 70, 70, Region.Op.DIFFERENCE);
drawScene(canvas);
canvas.restore();

canvas.save();
canvas.translate(10, 160);
mPath.reset();
//Synthetic comment -- @@ -85,21 +84,21 @@
canvas.clipPath(mPath, Region.Op.REPLACE);
drawScene(canvas);
canvas.restore();

canvas.save();
canvas.translate(160, 160);
canvas.clipRect(0, 0, 60, 60);
canvas.clipRect(40, 40, 100, 100, Region.Op.UNION);
drawScene(canvas);
canvas.restore();

canvas.save();
canvas.translate(10, 310);
canvas.clipRect(0, 0, 60, 60);
canvas.clipRect(40, 40, 100, 100, Region.Op.XOR);
drawScene(canvas);
canvas.restore();

canvas.save();
canvas.translate(160, 310);
canvas.clipRect(0, 0, 60, 60);








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
//Synthetic comment -- index 19a0f7f..f438e73 100644

//Synthetic comment -- @@ -18,35 +18,31 @@

import com.example.android.apis.R;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.View;

public class ColorMatrixSample extends GraphicsActivity {

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static class SampleView extends View {
private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
private Bitmap mBitmap;
private float mAngle;

public SampleView(Context context) {
super(context);

mBitmap = BitmapFactory.decodeResource(context.getResources(),
R.drawable.balloons);
}

private static void setTranslate(ColorMatrix cm, float dr, float dg,
float db, float da) {
cm.set(new float[] {
//Synthetic comment -- @@ -55,7 +51,7 @@
0, 0, 2, 0, db,
0, 0, 0, 1, da });
}

private static void setContrast(ColorMatrix cm, float contrast) {
float scale = contrast + 1.f;
float translate = (-.5f * scale + .5f) * 255.f;
//Synthetic comment -- @@ -65,7 +61,7 @@
0, 0, scale, 0, translate,
0, 0, 0, 1, 0 });
}

private static void setContrastTranslateOnly(ColorMatrix cm, float contrast) {
float scale = contrast + 1.f;
float translate = (-.5f * scale + .5f) * 255.f;
//Synthetic comment -- @@ -75,7 +71,7 @@
0, 0, 1, 0, translate,
0, 0, 0, 1, 0 });
}

private static void setContrastScaleOnly(ColorMatrix cm, float contrast) {
float scale = contrast + 1.f;
float translate = (-.5f * scale + .5f) * 255.f;
//Synthetic comment -- @@ -85,40 +81,40 @@
0, 0, scale, 0, 0,
0, 0, 0, 1, 0 });
}

@Override protected void onDraw(Canvas canvas) {
Paint paint = mPaint;
float x = 20;
float y = 20;

canvas.drawColor(Color.WHITE);

paint.setColorFilter(null);
canvas.drawBitmap(mBitmap, x, y, paint);

ColorMatrix cm = new ColorMatrix();

mAngle += 2;
if (mAngle > 180) {
mAngle = 0;
}

//convert our animated angle [-180...180] to a contrast value of [-1..1]
float contrast = mAngle / 180.f;

setContrast(cm, contrast);
paint.setColorFilter(new ColorMatrixColorFilter(cm));
canvas.drawBitmap(mBitmap, x + mBitmap.getWidth() + 10, y, paint);

setContrastScaleOnly(cm, contrast);
paint.setColorFilter(new ColorMatrixColorFilter(cm));
canvas.drawBitmap(mBitmap, x, y + mBitmap.getHeight() + 10, paint);

setContrastTranslateOnly(cm, contrast);
paint.setColorFilter(new ColorMatrixColorFilter(cm));
canvas.drawBitmap(mBitmap, x, y + 2*(mBitmap.getHeight() + 10),
paint);

invalidate();
}
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/ColorPickerDialog.java b/samples/ApiDemos/src/com/example/android/apis/graphics/ColorPickerDialog.java
//Synthetic comment -- index 7588180..1c7125f 100644

//Synthetic comment -- @@ -37,7 +37,7 @@
private Paint mCenterPaint;
private final int[] mColors;
private OnColorChangedListener mListener;

ColorPickerView(Context c, OnColorChangedListener l, int color) {
super(c);
mListener = l;
//Synthetic comment -- @@ -46,33 +46,33 @@
0xFFFFFF00, 0xFFFF0000
};
Shader s = new SweepGradient(0, 0, mColors, null);

mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
mPaint.setShader(s);
mPaint.setStyle(Paint.Style.STROKE);
mPaint.setStrokeWidth(32);

mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
mCenterPaint.setColor(color);
mCenterPaint.setStrokeWidth(5);
}

private boolean mTrackingCenter;
private boolean mHighlightCenter;

        @Override
protected void onDraw(Canvas canvas) {
float r = CENTER_X - mPaint.getStrokeWidth()*0.5f;

canvas.translate(CENTER_X, CENTER_X);

            canvas.drawOval(new RectF(-r, -r, r, r), mPaint);
canvas.drawCircle(0, 0, CENTER_RADIUS, mCenterPaint);

if (mTrackingCenter) {
int c = mCenterPaint.getColor();
mCenterPaint.setStyle(Paint.Style.STROKE);

if (mHighlightCenter) {
mCenterPaint.setAlpha(0xFF);
} else {
//Synthetic comment -- @@ -81,17 +81,17 @@
canvas.drawCircle(0, 0,
CENTER_RADIUS + mCenterPaint.getStrokeWidth(),
mCenterPaint);

mCenterPaint.setStyle(Paint.Style.FILL);
mCenterPaint.setColor(c);
}
}

@Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
setMeasuredDimension(CENTER_X*2, CENTER_Y*2);
}

private static final int CENTER_X = 100;
private static final int CENTER_Y = 100;
private static final int CENTER_RADIUS = 32;
//Synthetic comment -- @@ -108,11 +108,11 @@
}
return n;
}

private int ave(int s, int d, float p) {
return s + java.lang.Math.round(p * (d - s));
}

private int interpColor(int colors[], float unit) {
if (unit <= 0) {
return colors[0];
//Synthetic comment -- @@ -120,7 +120,7 @@
if (unit >= 1) {
return colors[colors.length - 1];
}

float p = unit * (colors.length - 1);
int i = (int)p;
p -= i;
//Synthetic comment -- @@ -132,16 +132,16 @@
int r = ave(Color.red(c0), Color.red(c1), p);
int g = ave(Color.green(c0), Color.green(c1), p);
int b = ave(Color.blue(c0), Color.blue(c1), p);

return Color.argb(a, r, g, b);
}

private int rotateColor(int color, float rad) {
float deg = rad * 180 / 3.1415927f;
int r = Color.red(color);
int g = Color.green(color);
int b = Color.blue(color);

ColorMatrix cm = new ColorMatrix();
ColorMatrix tmp = new ColorMatrix();

//Synthetic comment -- @@ -150,17 +150,17 @@
cm.postConcat(tmp);
tmp.setYUV2RGB();
cm.postConcat(tmp);

final float[] a = cm.getArray();

int ir = floatToByte(a[0] * r +  a[1] * g +  a[2] * b);
int ig = floatToByte(a[5] * r +  a[6] * g +  a[7] * b);
int ib = floatToByte(a[10] * r + a[11] * g + a[12] * b);

return Color.argb(Color.alpha(color), pinToByte(ir),
pinToByte(ig), pinToByte(ib));
}

private static final float PI = 3.1415926f;

@Override
//Synthetic comment -- @@ -168,7 +168,7 @@
float x = event.getX() - CENTER_X;
float y = event.getY() - CENTER_Y;
boolean inCenter = java.lang.Math.sqrt(x*x + y*y) <= CENTER_RADIUS;

switch (event.getAction()) {
case MotionEvent.ACTION_DOWN:
mTrackingCenter = inCenter;
//Synthetic comment -- @@ -212,7 +212,7 @@
OnColorChangedListener listener,
int initialColor) {
super(context);

mListener = listener;
mInitialColor = initialColor;
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Compass.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Compass.java
//Synthetic comment -- index d3b0981..85471d9 100644

//Synthetic comment -- @@ -35,10 +35,10 @@
private Sensor mSensor;
private SampleView mView;
private float[] mValues;

private final SensorEventListener mListener = new SensorEventListener() {
public void onSensorChanged(SensorEvent event) {
            if (Config.DEBUG) Log.d(TAG,
"sensorChanged (" + event.values[0] + ", " + event.values[1] + ", " + event.values[2] + ")");
mValues = event.values;
if (mView != null) {
//Synthetic comment -- @@ -62,17 +62,17 @@
@Override
protected void onResume()
{
        if (Config.DEBUG) Log.d(TAG, "onResume");
super.onResume();

mSensorManager.registerListener(mListener, mSensor,
SensorManager.SENSOR_DELAY_GAME);
}

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
//Synthetic comment -- @@ -93,12 +92,12 @@
mPath.lineTo(20, 60);
mPath.close();
}

@Override protected void onDraw(Canvas canvas) {
Paint paint = mPaint;

canvas.drawColor(Color.WHITE);

paint.setAntiAlias(true);
paint.setColor(Color.BLACK);
paint.setStyle(Paint.Style.FILL);
//Synthetic comment -- @@ -109,23 +108,24 @@
int cy = h / 2;

canvas.translate(cx, cy);
            if (mValues != null) {
canvas.rotate(-mValues[0]);
}
canvas.drawPath(mPath, mPaint);
}

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
//Synthetic comment -- index e3e5d9a..61fe9d5 100644

//Synthetic comment -- @@ -16,13 +16,9 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;

import java.io.ByteArrayOutputStream;
//Synthetic comment -- @@ -34,11 +30,11 @@
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static final int WIDTH = 50;
private static final int HEIGHT = 50;
private static final int STRIDE = 64;   // must be >= WIDTH

private static int[] createColors() {
int[] colors = new int[STRIDE * HEIGHT];
for (int y = 0; y < HEIGHT; y++) {
//Synthetic comment -- @@ -52,18 +48,18 @@
}
return colors;
}

private static class SampleView extends View {
private Bitmap[] mBitmaps;
private Bitmap[] mJPEG;
private Bitmap[] mPNG;
private int[]    mColors;
private Paint    mPaint;

private static Bitmap codec(Bitmap src, Bitmap.CompressFormat format,
int quality) {
ByteArrayOutputStream os = new ByteArrayOutputStream();
            src.compress(format, quality, os);

byte[] array = os.toByteArray();
return BitmapFactory.decodeByteArray(array, 0, array.length);
//Synthetic comment -- @@ -72,7 +68,7 @@
public SampleView(Context context) {
super(context);
setFocusable(true);

mColors = createColors();
int[] colors = mColors;

//Synthetic comment -- @@ -84,7 +80,7 @@
Bitmap.Config.RGB_565);
mBitmaps[2] = Bitmap.createBitmap(colors, 0, STRIDE, WIDTH, HEIGHT,
Bitmap.Config.ARGB_4444);

// these three will have their colors set later
mBitmaps[3] = Bitmap.createBitmap(WIDTH, HEIGHT,
Bitmap.Config.ARGB_8888);
//Synthetic comment -- @@ -95,10 +91,10 @@
for (int i = 3; i <= 5; i++) {
mBitmaps[i].setPixels(colors, 0, STRIDE, 0, 0, WIDTH, HEIGHT);
}

mPaint = new Paint();
mPaint.setDither(true);

// now encode/decode using JPEG and PNG
mJPEG = new Bitmap[mBitmaps.length];
mPNG = new Bitmap[mBitmaps.length];
//Synthetic comment -- @@ -107,7 +103,7 @@
mPNG[i] = codec(mBitmaps[i], Bitmap.CompressFormat.PNG, 0);
}
}

@Override protected void onDraw(Canvas canvas) {
canvas.drawColor(Color.WHITE);

//Synthetic comment -- @@ -117,7 +113,7 @@
canvas.drawBitmap(mPNG[i], 160, 0, null);
canvas.translate(0, mBitmaps[i].getHeight());
}

// draw the color array directly, w/o craeting a bitmap object
canvas.drawBitmap(mColors, 0, STRIDE, 0, 0, WIDTH, HEIGHT,
true, null);








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
//Synthetic comment -- index 10c42a7..4d2ce2c 100644

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

//Synthetic comment -- @@ -50,7 +47,7 @@

final LayoutInflater li = (LayoutInflater)getSystemService(
LAYOUT_INFLATER_SERVICE);

this.setTitle(R.string.density_title);
LinearLayout root = new LinearLayout(this);
root.setOrientation(LinearLayout.VERTICAL);
//Synthetic comment -- @@ -79,11 +76,11 @@
layout = (LinearLayout)li.inflate(R.layout.density_image_views, null);
addLabelToRoot(root, "Inflated layout");
addChildToRoot(root, layout);

layout = (LinearLayout)li.inflate(R.layout.density_styled_image_views, null);
addLabelToRoot(root, "Inflated styled layout");
addChildToRoot(root, layout);

layout = new LinearLayout(this);
addCanvasBitmap(layout, R.drawable.logo120dpi, true);
addCanvasBitmap(layout, R.drawable.logo160dpi, true);








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/DrawPoints.java b/samples/ApiDemos/src/com/example/android/apis/graphics/DrawPoints.java
//Synthetic comment -- index cbe6373..21eba0d 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
//Synthetic comment -- @@ -29,7 +28,7 @@
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static class SampleView extends View {
private Paint   mPaint = new Paint();
private float[] mPts;
//Synthetic comment -- @@ -38,11 +37,11 @@
private static final int SEGS = 32;
private static final int X = 0;
private static final int Y = 1;

private void buildPoints() {
final int ptCount = (SEGS + 1) * 2;
mPts = new float[ptCount * 2];

float value = 0;
final float delta = SIZE / SEGS;
for (int i = 0; i <= SEGS; i++) {
//Synthetic comment -- @@ -53,16 +52,16 @@
value += delta;
}
}

public SampleView(Context context) {
super(context);

buildPoints();
}

@Override protected void onDraw(Canvas canvas) {
Paint paint = mPaint;

canvas.translate(10, 10);

canvas.drawColor(Color.WHITE);
//Synthetic comment -- @@ -70,7 +69,7 @@
paint.setColor(Color.RED);
paint.setStrokeWidth(0);
canvas.drawLines(mPts, paint);

paint.setColor(Color.BLUE);
paint.setStrokeWidth(3);
canvas.drawPoints(mPts, paint);








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/FingerPaint.java b/samples/ApiDemos/src/com/example/android/apis/graphics/FingerPaint.java
//Synthetic comment -- index 867da4c..fcfd28f 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
//Synthetic comment -- @@ -26,7 +25,7 @@
import android.view.View;

public class FingerPaint extends GraphicsActivity
        implements ColorPickerDialog.OnColorChangedListener {

@Override
protected void onCreate(Bundle savedInstanceState) {
//Synthetic comment -- @@ -41,34 +40,34 @@
mPaint.setStrokeJoin(Paint.Join.ROUND);
mPaint.setStrokeCap(Paint.Cap.ROUND);
mPaint.setStrokeWidth(12);

mEmboss = new EmbossMaskFilter(new float[] { 1, 1, 1 },
0.4f, 6, 3.5f);

mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
}

private Paint       mPaint;
private MaskFilter  mEmboss;
private MaskFilter  mBlur;

public void colorChanged(int color) {
mPaint.setColor(color);
}

public class MyView extends View {

private static final float MINP = 0.25f;
private static final float MAXP = 0.75f;

private Bitmap  mBitmap;
private Canvas  mCanvas;
private Path    mPath;
private Paint   mBitmapPaint;

public MyView(Context c) {
super(c);

mBitmap = Bitmap.createBitmap(320, 480, Bitmap.Config.ARGB_8888);
mCanvas = new Canvas(mBitmap);
mPath = new Path();
//Synthetic comment -- @@ -79,19 +78,19 @@
protected void onSizeChanged(int w, int h, int oldw, int oldh) {
super.onSizeChanged(w, h, oldw, oldh);
}

@Override
protected void onDraw(Canvas canvas) {
canvas.drawColor(0xFFAAAAAA);

canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

canvas.drawPath(mPath, mPaint);
}

private float mX, mY;
private static final float TOUCH_TOLERANCE = 4;

private void touch_start(float x, float y) {
mPath.reset();
mPath.moveTo(x, y);
//Synthetic comment -- @@ -114,12 +113,12 @@
// kill this so we don't double draw
mPath.reset();
}

@Override
public boolean onTouchEvent(MotionEvent event) {
float x = event.getX();
float y = event.getY();

switch (event.getAction()) {
case MotionEvent.ACTION_DOWN:
touch_start(x, y);
//Synthetic comment -- @@ -137,7 +136,7 @@
return true;
}
}

private static final int COLOR_MENU_ID = Menu.FIRST;
private static final int EMBOSS_MENU_ID = Menu.FIRST + 1;
private static final int BLUR_MENU_ID = Menu.FIRST + 2;
//Synthetic comment -- @@ -147,7 +146,7 @@
@Override
public boolean onCreateOptionsMenu(Menu menu) {
super.onCreateOptionsMenu(menu);

menu.add(0, COLOR_MENU_ID, 0, "Color").setShortcut('3', 'c');
menu.add(0, EMBOSS_MENU_ID, 0, "Emboss").setShortcut('4', 's');
menu.add(0, BLUR_MENU_ID, 0, "Blur").setShortcut('5', 'z');
//Synthetic comment -- @@ -164,13 +163,13 @@
*****/
return true;
}

@Override
public boolean onPrepareOptionsMenu(Menu menu) {
super.onPrepareOptionsMenu(menu);
return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
mPaint.setXfermode(null);








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
//Synthetic comment -- index d9f5db0..7e2a694 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
//Synthetic comment -- @@ -29,7 +28,7 @@
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static class SampleView extends View {
private static final int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG |
Canvas.CLIP_SAVE_FLAG |
//Synthetic comment -- @@ -42,23 +41,23 @@
public SampleView(Context context) {
super(context);
setFocusable(true);

mPaint = new Paint();
mPaint.setAntiAlias(true);
}

@Override protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);

canvas.translate(10, 10);

canvas.saveLayerAlpha(0, 0, 200, 200, 0x88, LAYER_FLAGS);

mPaint.setColor(Color.RED);
canvas.drawCircle(75, 75, 75, mPaint);
mPaint.setColor(Color.BLUE);
canvas.drawCircle(125, 125, 75, mPaint);

canvas.restore();
}
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/MeasureText.java b/samples/ApiDemos/src/com/example/android/apis/graphics/MeasureText.java
//Synthetic comment -- index e159efe..c2d433e 100644

//Synthetic comment -- @@ -16,13 +16,9 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;

public class MeasureText extends GraphicsActivity {
//Synthetic comment -- @@ -32,11 +28,11 @@
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static final int WIDTH = 50;
private static final int HEIGHT = 50;
private static final int STRIDE = 64;   // must be >= WIDTH

private static int[] createColors() {
int[] colors = new int[STRIDE * HEIGHT];
for (int y = 0; y < HEIGHT; y++) {
//Synthetic comment -- @@ -50,16 +46,16 @@
}
return colors;
}

private static class SampleView extends View {
private Paint   mPaint;
private float   mOriginX = 10;
private float   mOriginY = 80;

public SampleView(Context context) {
super(context);
setFocusable(true);

mPaint = new Paint();
mPaint.setAntiAlias(true);
mPaint.setStrokeWidth(5);
//Synthetic comment -- @@ -68,22 +64,22 @@
mPaint.setTypeface(Typeface.create(Typeface.SERIF,
Typeface.ITALIC));
}

private void showText(Canvas canvas, String text, Paint.Align align) {
//   mPaint.setTextAlign(align);

Rect    bounds = new Rect();
float[] widths = new float[text.length()];

int count = mPaint.getTextWidths(text, 0, text.length(), widths);
float w = mPaint.measureText(text, 0, text.length());
mPaint.getTextBounds(text, 0, text.length(), bounds);

mPaint.setColor(0xFF88FF88);
canvas.drawRect(bounds, mPaint);
mPaint.setColor(Color.BLACK);
canvas.drawText(text, 0, 0, mPaint);

float[] pts = new float[2 + count*2];
float x = 0;
float y = 0;
//Synthetic comment -- @@ -100,12 +96,12 @@
mPaint.setStrokeWidth(5);
canvas.drawPoints(pts, 0, (count + 1) << 1, mPaint);
}

@Override protected void onDraw(Canvas canvas) {
canvas.drawColor(Color.WHITE);

canvas.translate(mOriginX, mOriginY);

showText(canvas, "Measure", Paint.Align.LEFT);
canvas.translate(0, 80);
showText(canvas, "wiggy!", Paint.Align.CENTER);
//Synthetic comment -- @@ -114,4 +110,3 @@
}
}
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/PathEffects.java b/samples/ApiDemos/src/com/example/android/apis/graphics/PathEffects.java
//Synthetic comment -- index 80ddf38..2894fa9 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
//Synthetic comment -- @@ -30,7 +29,7 @@
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static class SampleView extends View {
private Paint mPaint;
private Path mPath;
//Synthetic comment -- @@ -41,7 +40,7 @@
private static PathEffect makeDash(float phase) {
return new DashPathEffect(new float[] { 15, 5, 8, 5 }, phase);
}

private static void makeEffects(PathEffect[] e, float phase) {
e[0] = null;     // no effect
e[1] = new CornerPathEffect(10);
//Synthetic comment -- @@ -51,7 +50,7 @@
e[4] = new ComposePathEffect(e[2], e[1]);
e[5] = new ComposePathEffect(e[3], e[1]);
}

public SampleView(Context context) {
super(context);
setFocusable(true);
//Synthetic comment -- @@ -60,23 +59,23 @@
mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
mPaint.setStyle(Paint.Style.STROKE);
mPaint.setStrokeWidth(6);

mPath = makeFollowPath();

mEffects = new PathEffect[6];

mColors = new int[] { Color.BLACK, Color.RED, Color.BLUE,
Color.GREEN, Color.MAGENTA, Color.BLACK
};
}

@Override protected void onDraw(Canvas canvas) {
canvas.drawColor(Color.WHITE);

RectF bounds = new RectF();
mPath.computeBounds(bounds, false);
canvas.translate(10 - bounds.left, 10 - bounds.top);

makeEffects(mEffects, mPhase);
mPhase += 1;
invalidate();
//Synthetic comment -- @@ -88,7 +87,7 @@
canvas.translate(0, 28);
}
}

@Override public boolean onKeyDown(int keyCode, KeyEvent event) {
switch (keyCode) {
case KeyEvent.KEYCODE_DPAD_CENTER:
//Synthetic comment -- @@ -106,7 +105,7 @@
}
return p;
}

private static Path makePathDash() {
Path p = new Path();
p.moveTo(4, 0);








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/PathFillTypes.java b/samples/ApiDemos/src/com/example/android/apis/graphics/PathFillTypes.java
//Synthetic comment -- index 78dba26..10cfc49 100644

//Synthetic comment -- @@ -20,11 +20,9 @@
// class is in a sub-package.
//import com.example.android.apis.R;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.View;

public class PathFillTypes extends GraphicsActivity {
//Synthetic comment -- @@ -34,7 +32,7 @@
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static class SampleView extends View {
private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
private Path mPath;
//Synthetic comment -- @@ -48,7 +46,7 @@
mPath.addCircle(40, 40, 45, Path.Direction.CCW);
mPath.addCircle(80, 80, 45, Path.Direction.CCW);
}

private void showPath(Canvas canvas, int x, int y, Path.FillType ft,
Paint paint) {
canvas.save();
//Synthetic comment -- @@ -59,14 +57,14 @@
canvas.drawPath(mPath, paint);
canvas.restore();
}

@Override protected void onDraw(Canvas canvas) {
Paint paint = mPaint;

canvas.drawColor(0xFFCCCCCC);

canvas.translate(20, 20);

paint.setAntiAlias(true);

showPath(canvas, 0, 0, Path.FillType.WINDING, paint);








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Patterns.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Patterns.java
//Synthetic comment -- index d2a51ff..6b6d8e1 100644

//Synthetic comment -- @@ -16,11 +16,9 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.*;

//Synthetic comment -- @@ -31,7 +29,7 @@
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static Bitmap makeBitmap1() {
Bitmap bm = Bitmap.createBitmap(40, 40, Bitmap.Config.RGB_565);
Canvas c = new Canvas(bm);
//Synthetic comment -- @@ -41,7 +39,7 @@
c.drawRect(5, 5, 35, 35, p);
return bm;
}

private static Bitmap makeBitmap2() {
Bitmap bm = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888);
Canvas c = new Canvas(bm);
//Synthetic comment -- @@ -51,13 +49,13 @@
c.drawCircle(32, 32, 27, p);
return bm;
}

private static class SampleView extends View {
private final Shader mShader1;
private final Shader mShader2;
private final Paint mPaint;
private final DrawFilter mFastDF;

private float mTouchStartX;
private float mTouchStartY;
private float mTouchCurrX;
//Synthetic comment -- @@ -72,25 +70,25 @@
mFastDF = new PaintFlagsDrawFilter(Paint.FILTER_BITMAP_FLAG |
Paint.DITHER_FLAG,
0);

mShader1 = new BitmapShader(makeBitmap1(), Shader.TileMode.REPEAT,
Shader.TileMode.REPEAT);
mShader2 = new BitmapShader(makeBitmap2(), Shader.TileMode.REPEAT,
Shader.TileMode.REPEAT);

Matrix m = new Matrix();
m.setRotate(30);
mShader2.setLocalMatrix(m);

mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
}

@Override protected void onDraw(Canvas canvas) {
canvas.setDrawFilter(mDF);

mPaint.setShader(mShader1);
canvas.drawPaint(mPaint);

canvas.translate(mTouchCurrX - mTouchStartX,
mTouchCurrY - mTouchStartY);

//Synthetic comment -- @@ -102,7 +100,7 @@
public boolean onTouchEvent(MotionEvent event) {
float x = event.getX();
float y = event.getY();

switch (event.getAction()) {
case MotionEvent.ACTION_DOWN:
mTouchStartX = mTouchCurrX = x;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/PictureLayout.java b/samples/ApiDemos/src/com/example/android/apis/graphics/PictureLayout.java
//Synthetic comment -- index 9bdb49a..df5fc92 100644

//Synthetic comment -- @@ -26,7 +26,6 @@
import android.view.ViewGroup;
import android.view.ViewParent;

public class PictureLayout extends ViewGroup {
private final Picture mPicture = new Picture();

//Synthetic comment -- @@ -36,7 +35,7 @@

public PictureLayout(Context context, AttributeSet attrs) {
super(context, attrs);
    }

@Override
public void addView(View child) {
//Synthetic comment -- @@ -105,7 +104,7 @@
setMeasuredDimension(resolveSize(maxWidth, widthMeasureSpec),
resolveSize(maxHeight, heightMeasureSpec));
}

private void drawPict(Canvas canvas, int x, int y, int w, int h,
float sx, float sy) {
canvas.save();
//Synthetic comment -- @@ -121,10 +120,10 @@
protected void dispatchDraw(Canvas canvas) {
super.dispatchDraw(mPicture.beginRecording(getWidth(), getHeight()));
mPicture.endRecording();

int x = getWidth()/2;
int y = getHeight()/2;

if (false) {
canvas.drawPicture(mPicture);
} else {








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Pictures.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Pictures.java
//Synthetic comment -- index 1bd0a8c..61842dd 100644

//Synthetic comment -- @@ -16,13 +16,11 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.view.View;

import java.io.*;
//Synthetic comment -- @@ -34,17 +32,17 @@
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static class SampleView extends View {
private Picture mPicture;
private Drawable mDrawable;

static void drawSomething(Canvas canvas) {
Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

p.setColor(0x88FF0000);
canvas.drawCircle(50, 50, 40, p);

p.setColor(Color.GREEN);
p.setTextSize(30);
canvas.drawText("Pictures", 60, 60, p);
//Synthetic comment -- @@ -58,20 +56,20 @@
mPicture = new Picture();
drawSomething(mPicture.beginRecording(200, 100));
mPicture.endRecording();

mDrawable = new PictureDrawable(mPicture);
}

@Override protected void onDraw(Canvas canvas) {
canvas.drawColor(Color.WHITE);

canvas.drawPicture(mPicture);

canvas.drawPicture(mPicture, new RectF(0, 100, getWidth(), 200));

mDrawable.setBounds(0, 200, getWidth(), 300);
mDrawable.draw(canvas);

ByteArrayOutputStream os = new ByteArrayOutputStream();
mPicture.writeToStream(os);
InputStream is = new ByteArrayInputStream(os.toByteArray());








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/PolyToPoly.java b/samples/ApiDemos/src/com/example/android/apis/graphics/PolyToPoly.java
//Synthetic comment -- index 15d92de..a1f1ed4 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
// class is in a sub-package.
//import com.example.android.apis.R;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
//Synthetic comment -- @@ -33,7 +32,7 @@
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static class SampleView extends View {
private Paint   mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
private Matrix  mMatrix = new Matrix();
//Synthetic comment -- @@ -43,13 +42,13 @@
canvas.save();
mMatrix.setPolyToPoly(src, 0, dst, 0, src.length >> 1);
canvas.concat(mMatrix);

mPaint.setColor(Color.GRAY);
mPaint.setStyle(Paint.Style.STROKE);
canvas.drawRect(0, 0, 64, 64, mPaint);
canvas.drawLine(0, 0, 64, 64, mPaint);
canvas.drawLine(0, 64, 64, 0, mPaint);

mPaint.setColor(Color.RED);
mPaint.setStyle(Paint.Style.FILL);
// how to draw the text center on our square
//Synthetic comment -- @@ -58,7 +57,7 @@
// centering in Y, we need to measure ascent/descent first
float y = 64/2 - (mFontMetrics.ascent + mFontMetrics.descent)/2;
canvas.drawText(src.length/2 + "", x, y, mPaint);

canvas.restore();
}

//Synthetic comment -- @@ -72,10 +71,9 @@
mPaint.setTextAlign(Paint.Align.CENTER);
mFontMetrics = mPaint.getFontMetrics();
}

        @Override
        protected void onDraw(Canvas canvas) {
canvas.drawColor(Color.WHITE);

canvas.save();
//Synthetic comment -- @@ -83,7 +81,7 @@
// translate (1 point)
doDraw(canvas, new float[] { 0, 0 }, new float[] { 5, 5 });
canvas.restore();

canvas.save();
canvas.translate(160, 10);
// rotate/uniform-scale (2 points)
//Synthetic comment -- @@ -107,4 +105,3 @@
}
}
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/ProxyDrawable.java b/samples/ApiDemos/src/com/example/android/apis/graphics/ProxyDrawable.java
//Synthetic comment -- index d264134..635132e 100644

//Synthetic comment -- @@ -22,18 +22,18 @@
import android.graphics.drawable.Drawable;

public class ProxyDrawable extends Drawable {

private Drawable mProxy;
private boolean mMutated;

public ProxyDrawable(Drawable target) {
mProxy = target;
}

public Drawable getProxy() {
return mProxy;
}

public void setProxy(Drawable proxy) {
if (proxy != this) {
mProxy = proxy;
//Synthetic comment -- @@ -46,43 +46,43 @@
mProxy.draw(canvas);
}
}

@Override
public int getIntrinsicWidth() {
return mProxy != null ? mProxy.getIntrinsicWidth() : -1;
}

@Override
public int getIntrinsicHeight() {
return mProxy != null ? mProxy.getIntrinsicHeight() : -1;
}

@Override
public int getOpacity() {
return mProxy != null ? mProxy.getOpacity() : PixelFormat.TRANSPARENT;
}

@Override
public void setFilterBitmap(boolean filter) {
if (mProxy != null) {
mProxy.setFilterBitmap(filter);
}
}

@Override
public void setDither(boolean dither) {
if (mProxy != null) {
mProxy.setDither(dither);
}
}

@Override
public void setColorFilter(ColorFilter colorFilter) {
if (mProxy != null) {
mProxy.setColorFilter(colorFilter);
}
}

@Override
public void setAlpha(int alpha) {
if (mProxy != null) {
//Synthetic comment -- @@ -99,4 +99,4 @@
return this;
}
}









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Regions.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Regions.java
//Synthetic comment -- index 833274b..fc0aa08 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
//Synthetic comment -- @@ -29,7 +28,7 @@
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static class SampleView extends View {
private final Paint mPaint = new Paint();
private final Rect  mRect1 = new Rect();
//Synthetic comment -- @@ -38,11 +37,11 @@
public SampleView(Context context) {
super(context);
setFocusable(true);

mPaint.setAntiAlias(true);
mPaint.setTextSize(16);
mPaint.setTextAlign(Paint.Align.CENTER);

mRect1.set(10, 10, 100, 80);
mRect2.set(50, 50, 130, 110);
}
//Synthetic comment -- @@ -55,25 +54,25 @@
mPaint.setColor(Color.BLUE);
mPaint.setAlpha(alpha);
drawCentered(canvas, mRect2, mPaint);

// restore style
mPaint.setStyle(Paint.Style.FILL);
}

private void drawRgn(Canvas canvas, int color, String str, Region.Op op) {
if (str != null) {
mPaint.setColor(Color.BLACK);
canvas.drawText(str, 80, 24, mPaint);
}

Region rgn = new Region();
rgn.set(mRect1);
rgn.op(mRect2, op);

mPaint.setColor(color);
RegionIterator iter = new RegionIterator(rgn);
Rect r = new Rect();

canvas.translate(0, 30);
mPaint.setColor(color);
while (iter.next(r)) {
//Synthetic comment -- @@ -81,7 +80,7 @@
}
drawOriginalRects(canvas, 0x80);
}

private static void drawCentered(Canvas c, Rect r, Paint p) {
float inset = p.getStrokeWidth() * 0.5f;
if (inset == 0) {   // catch hairlines
//Synthetic comment -- @@ -90,32 +89,33 @@
c.drawRect(r.left + inset, r.top + inset,
r.right - inset, r.bottom - inset, p);
}

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.GRAY);

canvas.save();
canvas.translate(80, 5);
drawOriginalRects(canvas, 0xFF);
canvas.restore();

mPaint.setStyle(Paint.Style.FILL);

canvas.save();
canvas.translate(0, 140);
drawRgn(canvas, Color.RED, "Union", Region.Op.UNION);
canvas.restore();

canvas.save();
canvas.translate(0, 280);
drawRgn(canvas, Color.BLUE, "Xor", Region.Op.XOR);
canvas.restore();

canvas.save();
canvas.translate(160, 140);
drawRgn(canvas, Color.GREEN, "Difference", Region.Op.DIFFERENCE);
canvas.restore();

canvas.save();
canvas.translate(160, 280);
drawRgn(canvas, Color.WHITE, "Intersect", Region.Op.INTERSECT);








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/RoundRects.java b/samples/ApiDemos/src/com/example/android/apis/graphics/RoundRects.java
//Synthetic comment -- index b0ff0359..74c2406 100644

//Synthetic comment -- @@ -16,14 +16,10 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.Bundle;
import android.view.*;

public class RoundRects extends GraphicsActivity {
//Synthetic comment -- @@ -33,7 +29,7 @@
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static class SampleView extends View {
private Path    mPath;
private Paint   mPaint;
//Synthetic comment -- @@ -47,73 +43,71 @@
mPath = new Path();
mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
mRect = new Rect(0, 0, 120, 120);

mDrawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
new int[] { 0xFFFF0000, 0xFF00FF00,
0xFF0000FF });
mDrawable.setShape(GradientDrawable.RECTANGLE);
mDrawable.setGradientRadius((float)(Math.sqrt(2) * 60));
}

static void setCornerRadii(GradientDrawable drawable, float r0,
float r1, float r2, float r3) {
drawable.setCornerRadii(new float[] { r0, r0, r1, r1,
r2, r2, r3, r3 });
}

@Override protected void onDraw(Canvas canvas) {

mDrawable.setBounds(mRect);

float r = 16;

canvas.save();
canvas.translate(10, 10);
mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
setCornerRadii(mDrawable, r, r, 0, 0);
mDrawable.draw(canvas);
canvas.restore();

canvas.save();
canvas.translate(10 + mRect.width() + 10, 10);
mDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
setCornerRadii(mDrawable, 0, 0, r, r);
mDrawable.draw(canvas);
canvas.restore();

canvas.translate(0, mRect.height() + 10);

canvas.save();
canvas.translate(10, 10);
mDrawable.setGradientType(GradientDrawable.SWEEP_GRADIENT);
setCornerRadii(mDrawable, 0, r, r, 0);
mDrawable.draw(canvas);
canvas.restore();

canvas.save();
canvas.translate(10 + mRect.width() + 10, 10);
mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
setCornerRadii(mDrawable, r, 0, 0, r);
mDrawable.draw(canvas);
canvas.restore();

canvas.translate(0, mRect.height() + 10);

canvas.save();
canvas.translate(10, 10);
mDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
setCornerRadii(mDrawable, r, 0, r, 0);
mDrawable.draw(canvas);
canvas.restore();

canvas.save();
canvas.translate(10 + mRect.width() + 10, 10);
mDrawable.setGradientType(GradientDrawable.SWEEP_GRADIENT);
setCornerRadii(mDrawable, 0, r, 0, r);
mDrawable.draw(canvas);
canvas.restore();
}
}
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/ScaleToFit.java b/samples/ApiDemos/src/com/example/android/apis/graphics/ScaleToFit.java
//Synthetic comment -- index f55e55b..6ffdb5b 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
//Synthetic comment -- @@ -29,14 +28,14 @@
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static class SampleView extends View {
private final Paint   mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
private final Paint   mHairPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
private final Paint   mLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
private final Matrix  mMatrix = new Matrix();
private final RectF   mSrcR = new RectF();

private static final Matrix.ScaleToFit[] sFits =
new Matrix.ScaleToFit[] {
Matrix.ScaleToFit.FILL,
//Synthetic comment -- @@ -44,11 +43,11 @@
Matrix.ScaleToFit.CENTER,
Matrix.ScaleToFit.END
};

private static final String[] sFitLabels = new String[] {
"FILL", "START", "CENTER", "END"
};

private static final int[] sSrcData = new int[] {
80, 40, Color.RED,
40, 80, Color.GREEN,
//Synthetic comment -- @@ -56,7 +55,7 @@
80, 80, Color.BLACK
};
private static final int N = 4;

private static final int WIDTH = 52;
private static final int HEIGHT = 52;
private final RectF mDstR = new RectF(0, 0, WIDTH, HEIGHT);
//Synthetic comment -- @@ -67,34 +66,33 @@
mHairPaint.setStyle(Paint.Style.STROKE);
mLabelPaint.setTextSize(16);
}

private void setSrcR(int index) {
int w = sSrcData[index*3 + 0];
int h = sSrcData[index*3 + 1];
mSrcR.set(0, 0, w, h);
}

private void drawSrcR(Canvas canvas, int index) {
mPaint.setColor(sSrcData[index*3 + 2]);
canvas.drawOval(mSrcR, mPaint);
}

private void drawFit(Canvas canvas, int index, Matrix.ScaleToFit stf) {
canvas.save();

setSrcR(index);
mMatrix.setRectToRect(mSrcR, mDstR, stf);
canvas.concat(mMatrix);
drawSrcR(canvas, index);

canvas.restore();

canvas.drawRect(mDstR, mHairPaint);
}

        @Override
        protected void onDraw(Canvas canvas) {
canvas.drawColor(Color.WHITE);

canvas.translate(10, 10);
//Synthetic comment -- @@ -106,7 +104,7 @@
canvas.translate(mSrcR.width() + 15, 0);
}
canvas.restore();

canvas.translate(0, 100);
for (int j = 0; j < sFits.length; j++) {
canvas.save();
//Synthetic comment -- @@ -121,4 +119,3 @@
}
}
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/SensorTest.java b/samples/ApiDemos/src/com/example/android/apis/graphics/SensorTest.java
//Synthetic comment -- index 87e0461..dc07a27 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Config;
import android.util.Log;
import android.view.View;

public class SensorTest extends GraphicsActivity {
//Synthetic comment -- @@ -33,7 +34,7 @@
private Sensor mSensor;
private SampleView mView;
private float[] mValues;

private static class RunAve {
private final float[] mWeights;
private final float mWeightScale;
//Synthetic comment -- @@ -43,7 +44,7 @@

public RunAve(float[] weights) {
mWeights = weights;

float sum = 0;
for (int i = 0; i < weights.length; i++) {
sum += weights[i];
//Synthetic comment -- @@ -54,12 +55,12 @@
mSamples = new float[mDepth];
mCurr = 0;
}

public void addSample(float value) {
mSamples[mCurr] = value;
mCurr = (mCurr + 1) % mDepth;
}

public float computeAve() {
final int depth = mDepth;
int index = mCurr;
//Synthetic comment -- @@ -92,20 +93,20 @@
}
mPrev[i] = event.values[i];
}

if (show) {
// only shows if we think the delta is big enough, in an attempt
// to detect "serious" moves left/right or up/down
                Log.e(TAG, "sensorChanged " + event.sensor.getName() +
" (" + event.values[0] + ", " + event.values[1] + ", " +
event.values[2] + ")" + " diff(" + diff[0] +
" " + diff[1] + " " + diff[2] + ")");
}

long now = android.os.SystemClock.uptimeMillis();
if (now - mLastGestureTime > 1000) {
mLastGestureTime = 0;

float x = diff[0];
float y = diff[1];
boolean gestX = Math.abs(x) > 3;
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
//Synthetic comment -- @@ -174,13 +174,13 @@
mPath.lineTo(20, 60);
mPath.close();
}

@Override
protected void onDraw(Canvas canvas) {
Paint paint = mPaint;

canvas.drawColor(Color.WHITE);

paint.setAntiAlias(true);
paint.setColor(Color.BLACK);
paint.setStyle(Paint.Style.FILL);
//Synthetic comment -- @@ -191,21 +191,23 @@
int cy = h / 2;

canvas.translate(cx, cy);
            if (mValues != null) {
canvas.rotate(-mValues[0]);
}
canvas.drawPath(mPath, mPaint);
}

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
//Synthetic comment -- index 6d450bb..236f4fc 100644

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
//Synthetic comment -- @@ -35,49 +31,49 @@
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static class SampleView extends View {
private ShapeDrawable[] mDrawables;

private static Shader makeSweep() {
return new SweepGradient(150, 25,
new int[] { 0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFFFF0000 },
null);
}

private static Shader makeLinear() {
return new LinearGradient(0, 0, 50, 50,
new int[] { 0xFFFF0000, 0xFF00FF00, 0xFF0000FF },
null, Shader.TileMode.MIRROR);
}

private static Shader makeTiling() {
int[] pixels = new int[] { 0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0};
Bitmap bm = Bitmap.createBitmap(pixels, 2, 2,
Bitmap.Config.ARGB_8888);

return new BitmapShader(bm, Shader.TileMode.REPEAT,
Shader.TileMode.REPEAT);
}

private static class MyShapeDrawable extends ShapeDrawable {
private Paint mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

public MyShapeDrawable(Shape s) {
super(s);
mStrokePaint.setStyle(Paint.Style.STROKE);
}

public Paint getStrokePaint() {
return mStrokePaint;
}

@Override protected void onDraw(Shape s, Canvas c, Paint p) {
s.draw(c, p);
s.draw(c, mStrokePaint);
}
}

public SampleView(Context context) {
super(context);
setFocusable(true);
//Synthetic comment -- @@ -85,14 +81,14 @@
float[] outerR = new float[] { 12, 12, 12, 12, 0, 0, 0, 0 };
RectF   inset = new RectF(6, 6, 6, 6);
float[] innerR = new float[] { 12, 12, 0, 0, 12, 12, 0, 0 };

Path path = new Path();
path.moveTo(50, 0);
path.lineTo(0, 50);
path.lineTo(50, 100);
path.lineTo(100, 50);
path.close();

mDrawables = new ShapeDrawable[7];
mDrawables[0] = new ShapeDrawable(new RectShape());
mDrawables[1] = new ShapeDrawable(new OvalShape());
//Synthetic comment -- @@ -104,7 +100,7 @@
innerR));
mDrawables[5] = new ShapeDrawable(new PathShape(path, 100, 100));
mDrawables[6] = new MyShapeDrawable(new ArcShape(45, -270));

mDrawables[0].getPaint().setColor(0xFFFF0000);
mDrawables[1].getPaint().setColor(0xFF00FF00);
mDrawables[2].getPaint().setColor(0xFF0000FF);
//Synthetic comment -- @@ -112,26 +108,26 @@
mDrawables[4].getPaint().setShader(makeLinear());
mDrawables[5].getPaint().setShader(makeTiling());
mDrawables[6].getPaint().setColor(0x88FF8844);

PathEffect pe = new DiscretePathEffect(10, 4);
PathEffect pe2 = new CornerPathEffect(4);
mDrawables[3].getPaint().setPathEffect(
new ComposePathEffect(pe2, pe));

MyShapeDrawable msd = (MyShapeDrawable)mDrawables[6];
msd.getStrokePaint().setStrokeWidth(4);
}

@Override protected void onDraw(Canvas canvas) {

int x = 10;
int y = 10;
int width = 300;
int height = 50;

for (Drawable dr : mDrawables) {
dr.setBounds(x, y, x + width, y + height);
                dr.draw(canvas);
y += height + 5;
}
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Sweep.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Sweep.java
//Synthetic comment -- index dc127fd..5da10cf 100644

//Synthetic comment -- @@ -16,11 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
//Synthetic comment -- @@ -34,7 +29,7 @@
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static class SampleView extends View {
private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
private float mRotate;
//Synthetic comment -- @@ -55,7 +50,7 @@
Color.GREEN }, null);
mPaint.setShader(mShader);
}

@Override protected void onDraw(Canvas canvas) {
Paint paint = mPaint;
float x = 160;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/TextAlign.java b/samples/ApiDemos/src/com/example/android/apis/graphics/TextAlign.java
//Synthetic comment -- index 0576a7c..0ecba16 100644

//Synthetic comment -- @@ -16,13 +16,9 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;

public class TextAlign extends GraphicsActivity {
//Synthetic comment -- @@ -32,27 +28,27 @@
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static class SampleView extends View {
private Paint   mPaint;
private float   mX;
private float[] mPos;

private Path    mPath;
private Paint   mPathPaint;

private static final int DY = 30;
private static final String TEXT_L = "Left";
private static final String TEXT_C = "Center";
private static final String TEXT_R = "Right";
private static final String POSTEXT = "Positioned";
private static final String TEXTONPATH = "Along a path";

private static void makePath(Path p) {
p.moveTo(10, 0);
p.cubicTo(100, -50, 200, 50, 300, 0);
}

private float[] buildTextPositions(String text, float y, Paint paint) {
float[] widths = new float[text.length()];
// initially get the widths for each char
//Synthetic comment -- @@ -67,18 +63,18 @@
}
return pos;
}

public SampleView(Context context) {
super(context);
setFocusable(true);

mPaint = new Paint();
mPaint.setAntiAlias(true);
mPaint.setTextSize(30);
mPaint.setTypeface(Typeface.SERIF);

mPos = buildTextPositions(POSTEXT, 0, mPaint);

mPath = new Path();
makePath(mPath);

//Synthetic comment -- @@ -87,7 +83,7 @@
mPathPaint.setColor(0x800000FF);
mPathPaint.setStyle(Paint.Style.STROKE);
}

@Override protected void onDraw(Canvas canvas) {
canvas.drawColor(Color.WHITE);

//Synthetic comment -- @@ -95,13 +91,13 @@
float x = mX;
float y = 0;
float[] pos = mPos;

// draw the normal strings

p.setColor(0x80FF0000);
canvas.drawLine(x, y, x, y+DY*3, p);
p.setColor(Color.BLACK);

canvas.translate(0, DY);
p.setTextAlign(Paint.Align.LEFT);
canvas.drawText(TEXT_L, x, y, p);
//Synthetic comment -- @@ -113,11 +109,11 @@
canvas.translate(0, DY);
p.setTextAlign(Paint.Align.RIGHT);
canvas.drawText(TEXT_R, x, y, p);

canvas.translate(100, DY*2);

// now draw the positioned strings

p.setColor(0xBB00FF00);
for (int i = 0; i < pos.length/2; i++) {
canvas.drawLine(pos[i*2+0], pos[i*2+1]-DY,
//Synthetic comment -- @@ -127,17 +123,17 @@

p.setTextAlign(Paint.Align.LEFT);
canvas.drawPosText(POSTEXT, pos, p);

canvas.translate(0, DY);
p.setTextAlign(Paint.Align.CENTER);
canvas.drawPosText(POSTEXT, pos, p);

canvas.translate(0, DY);
p.setTextAlign(Paint.Align.RIGHT);
canvas.drawPosText(POSTEXT, pos, p);

// now draw the text on path

canvas.translate(-100, DY*2);

canvas.drawPath(mPath, mPathPaint);
//Synthetic comment -- @@ -148,7 +144,7 @@
canvas.drawPath(mPath, mPathPaint);
p.setTextAlign(Paint.Align.CENTER);
canvas.drawTextOnPath(TEXTONPATH, mPath, 0, 0, p);

canvas.translate(0, DY*1.5f);
canvas.drawPath(mPath, mPathPaint);
p.setTextAlign(Paint.Align.RIGHT);








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/TouchPaint.java b/samples/ApiDemos/src/com/example/android/apis/graphics/TouchPaint.java
//Synthetic comment -- index 0942852..ba48da0 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
//Synthetic comment -- @@ -41,34 +40,34 @@
public class TouchPaint extends GraphicsActivity {
/** Used as a pulse to gradually fade the contents of the window. */
private static final int FADE_MSG = 1;

/** Menu ID for the command to clear the window. */
private static final int CLEAR_ID = Menu.FIRST;
/** Menu ID for the command to toggle fading. */
private static final int FADE_ID = Menu.FIRST+1;

/** How often to fade the contents of the window (in ms). */
private static final int FADE_DELAY = 100;

/** The view responsible for drawing the window. */
MyView mView;
/** Is fading mode enabled? */
boolean mFading;

@Override protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);

// Create and attach the view that is responsible for painting.
mView = new MyView(this);
setContentView(mView);
mView.requestFocus();

// Restore the fading option if we are being thawed from a
// previously saved state.  Note that we are not currently remembering
// the contents of the bitmap.
mFading = savedInstanceState != null ? savedInstanceState.getBoolean("fading", true) : true;
}

@Override public boolean onCreateOptionsMenu(Menu menu) {
menu.add(0, CLEAR_ID, 0, "Clear");
menu.add(0, FADE_ID, 0, "Fade").setCheckable(true);
//Synthetic comment -- @@ -130,14 +129,14 @@
mHandler.sendMessageDelayed(
mHandler.obtainMessage(FADE_MSG), FADE_DELAY);
}

/**
* Stop the pulse to fade the screen.
*/
void stopFading() {
mHandler.removeMessages(FADE_MSG);
}

private Handler mHandler = new Handler() {
@Override public void handleMessage(Message msg) {
switch (msg.what) {
//Synthetic comment -- @@ -155,7 +154,7 @@
}
}
};

public class MyView extends View {
private static final int FADE_ALPHA = 0x06;
private static final int MAX_FADE_STEPS = 256/FADE_ALPHA + 4;
//Synthetic comment -- @@ -171,7 +170,7 @@
private float mCurSize;
private int mCurWidth;
private int mFadeSteps = MAX_FADE_STEPS;

public MyView(Context c) {
super(c);
mPaint = new Paint();
//Synthetic comment -- @@ -190,7 +189,7 @@
mFadeSteps = MAX_FADE_STEPS;
}
}

public void fade() {
if (mCanvas != null && mFadeSteps < MAX_FADE_STEPS) {
mCanvas.drawPaint(mFadePaint);
//Synthetic comment -- @@ -198,7 +197,7 @@
mFadeSteps++;
}
}

@Override protected void onSizeChanged(int w, int h, int oldw,
int oldh) {
int curW = mBitmap != null ? mBitmap.getWidth() : 0;
//Synthetic comment -- @@ -206,10 +205,10 @@
if (curW >= w && curH >= h) {
return;
}

if (curW < w) curW = w;
if (curH < h) curH = h;

Bitmap newBitmap = Bitmap.createBitmap(curW, curH,
Bitmap.Config.RGB_565);
Canvas newCanvas = new Canvas();
//Synthetic comment -- @@ -221,7 +220,7 @@
mCanvas = newCanvas;
mFadeSteps = MAX_FADE_STEPS;
}

@Override protected void onDraw(Canvas canvas) {
if (mBitmap != null) {
canvas.drawBitmap(mBitmap, 0, 0, null);
//Synthetic comment -- @@ -252,7 +251,7 @@
mCurDown = oldDown;
return true;
}

@Override public boolean onTouchEvent(MotionEvent event) {
int action = event.getAction();
mCurDown = action == MotionEvent.ACTION_DOWN
//Synthetic comment -- @@ -268,7 +267,7 @@
event.getSize());
return true;
}

private void drawPoint(float x, float y, float pressure, float size) {
//Log.i("TouchPaint", "Drawing: " + x + "x" + y + " p="
//        + pressure + " s=" + size);








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
//Synthetic comment -- index aefc311..08facaa 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
//Synthetic comment -- @@ -29,20 +28,20 @@
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static class SampleView extends View {
private Paint    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
private Typeface mFace;

public SampleView(Context context) {
super(context);

mFace = Typeface.createFromAsset(getContext().getAssets(),
"fonts/samplefont.ttf");

mPaint.setTextSize(64);
}

@Override protected void onDraw(Canvas canvas) {
canvas.drawColor(Color.WHITE);









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/UnicodeChart.java b/samples/ApiDemos/src/com/example/android/apis/graphics/UnicodeChart.java
//Synthetic comment -- index 7ee99d0..0a2f630 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
//Synthetic comment -- @@ -29,20 +28,20 @@
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);

requestWindowFeature(Window.FEATURE_NO_TITLE);

setContentView(new SampleView(this));
}

private static class SampleView extends View {
private Paint mBigCharPaint;
private Paint mLabelPaint;
private final char[] mChars = new char[256];
private final float[] mPos = new float[512];

private int mBase;

private static final int XMUL = 20;
private static final int YMUL = 28;
private static final int YBASE = 18;
//Synthetic comment -- @@ -51,49 +50,49 @@
super(context);
setFocusable(true);
setFocusableInTouchMode(true);

mBigCharPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
mBigCharPaint.setTextSize(15);
mBigCharPaint.setTextAlign(Paint.Align.CENTER);

mLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
mLabelPaint.setTextSize(8);
mLabelPaint.setTextAlign(Paint.Align.CENTER);

// the position array is the same for all charts
float[] pos = mPos;
int index = 0;
for (int col = 0; col < 16; col++) {
                final float x = col * XMUL + 10;
for (int row = 0; row < 16; row++) {
pos[index++] = x;
pos[index++] = row * YMUL + YBASE;
}
}
}

private float computeX(int index) {
            return (index >> 4) * XMUL + 10;
}

private float computeY(int index) {
return (index & 0xF) * YMUL + YMUL;
}

private void drawChart(Canvas canvas, int base) {
char[] chars = mChars;
for (int i = 0; i < 256; i++) {
int unichar = base + i;
chars[i] = (char)unichar;

canvas.drawText(Integer.toHexString(unichar),
computeX(i), computeY(i), mLabelPaint);
}
canvas.drawPosText(chars, 0, 256, mPos, mBigCharPaint);
}

@Override protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);

canvas.translate(0, 1);
drawChart(canvas, mBase * 256);
//Synthetic comment -- @@ -118,4 +117,3 @@
}
}
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Vertices.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Vertices.java
//Synthetic comment -- index 1e61906..ac1ab8a 100644

//Synthetic comment -- @@ -18,17 +18,11 @@

import com.example.android.apis.R;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;

public class Vertices extends GraphicsActivity {

@Override
//Synthetic comment -- @@ -36,14 +30,13 @@
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static class SampleView extends View {
private final Paint mPaint = new Paint();
private final float[] mVerts = new float[10];
private final float[] mTexs = new float[10];
private final short[] mIndices = { 0, 1, 2, 3, 4, 1 };

private final Matrix mMatrix = new Matrix();
private final Matrix mInverse = new Matrix();

//Synthetic comment -- @@ -61,7 +54,7 @@
Shader s = new BitmapShader(bm, Shader.TileMode.CLAMP,
Shader.TileMode.CLAMP);
mPaint.setShader(s);

float w = bm.getWidth();
float h = bm.getHeight();
// construct our mesh
//Synthetic comment -- @@ -70,18 +63,18 @@
setXY(mTexs, 2, w, 0);
setXY(mTexs, 3, w, h);
setXY(mTexs, 4, 0, h);

setXY(mVerts, 0, w/2, h/2);
setXY(mVerts, 1, 0, 0);
setXY(mVerts, 2, w, 0);
setXY(mVerts, 3, w, h);
setXY(mVerts, 4, 0, h);

mMatrix.setScale(0.8f, 0.8f);
mMatrix.preTranslate(20, 20);
mMatrix.invert(mInverse);
}

@Override protected void onDraw(Canvas canvas) {
canvas.drawColor(0xFFCCCCCC);
canvas.save();
//Synthetic comment -- @@ -104,7 +97,7 @@
invalidate();
return true;
}

}
}









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/graphics/Xfermodes.java b/samples/ApiDemos/src/com/example/android/apis/graphics/Xfermodes.java
//Synthetic comment -- index b9f8424..54e15e3 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
//Synthetic comment -- @@ -33,35 +32,35 @@
import android.view.View;

public class Xfermodes extends GraphicsActivity {

// create a bitmap with a circle, used for the "dst" image
static Bitmap makeDst(int w, int h) {
Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
Canvas c = new Canvas(bm);
Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(0xFFFFCC44);
c.drawOval(new RectF(0, 0, w*3/4, h*3/4), p);
return bm;
}

// create a bitmap with a rect, used for the "src" image
static Bitmap makeSrc(int w, int h) {
Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
Canvas c = new Canvas(bm);
Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

p.setColor(0xFF66AAFF);
c.drawRect(w/3, h/3, w*19/20, h*19/20, p);
return bm;
}

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(new SampleView(this));
}

private static class SampleView extends View {
private static final int W = 64;
private static final int H = 64;
//Synthetic comment -- @@ -70,7 +69,7 @@
private Bitmap mSrcB;
private Bitmap mDstB;
private Shader mBG;     // background checker-board pattern

private static final Xfermode[] sModes = {
new PorterDuffXfermode(PorterDuff.Mode.CLEAR),
new PorterDuffXfermode(PorterDuff.Mode.SRC),
//Synthetic comment -- @@ -89,20 +88,20 @@
new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),
new PorterDuffXfermode(PorterDuff.Mode.SCREEN)
};

private static final String[] sLabels = {
"Clear", "Src", "Dst", "SrcOver",
"DstOver", "SrcIn", "DstIn", "SrcOut",
"DstOut", "SrcATop", "DstATop", "Xor",
"Darken", "Lighten", "Multiply", "Screen"
};

public SampleView(Context context) {
super(context);

mSrcB = makeSrc(W, H);
mDstB = makeDst(W, H);

// make a ckeckerboard pattern
Bitmap bm = Bitmap.createBitmap(new int[] { 0xFFFFFFFF, 0xFFCCCCCC,
0xFFCCCCCC, 0xFFFFFFFF }, 2, 2,
//Synthetic comment -- @@ -114,18 +113,18 @@
m.setScale(6, 6);
mBG.setLocalMatrix(m);
}

@Override protected void onDraw(Canvas canvas) {
canvas.drawColor(Color.WHITE);

Paint labelP = new Paint(Paint.ANTI_ALIAS_FLAG);
labelP.setTextAlign(Paint.Align.CENTER);

Paint paint = new Paint();
paint.setFilterBitmap(false);

canvas.translate(15, 35);

int x = 0;
int y = 0;
for (int i = 0; i < sModes.length; i++) {
//Synthetic comment -- @@ -134,12 +133,12 @@
paint.setShader(null);
canvas.drawRect(x - 0.5f, y - 0.5f,
x + W + 0.5f, y + H + 0.5f, paint);

// draw the checker-board pattern
paint.setStyle(Paint.Style.FILL);
paint.setShader(mBG);
canvas.drawRect(x, y, x + W, y + H, paint);

// draw the src/dst example into our offscreen bitmap
int sc = canvas.saveLayer(x, y, x + W, y + H, null,
Canvas.MATRIX_SAVE_FLAG |
//Synthetic comment -- @@ -153,13 +152,13 @@
canvas.drawBitmap(mSrcB, 0, 0, paint);
paint.setXfermode(null);
canvas.restoreToCount(sc);

// draw the label
canvas.drawText(sLabels[i],
x + W/2, y - labelP.getTextSize()/2, labelP);

x += W + 10;

// wrap around when we've drawn enough for one row
if ((i % ROW_MAX) == ROW_MAX - 1) {
x = 0;







