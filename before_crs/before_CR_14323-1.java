/*Remove SurfaceViewTest Race Condition

Issue 7187

Don't create a separate thread to call the draw method, because the
thread may not be executed by the time the test asserts that the draw
method was called.

Change-Id:I6796d03d798de38a168ae476145012cf88d86fbe*/
//Synthetic comment -- diff --git a/tests/src/android/view/cts/SurfaceViewStubActivity.java b/tests/src/android/view/cts/SurfaceViewStubActivity.java
//Synthetic comment -- index 319af05..75d5ff6 100644

//Synthetic comment -- @@ -54,7 +54,6 @@

private SurfaceHolder mHolder;
private MockCanvas mCanvas;
        private Thread mSurfaceViewThread;

private boolean mIsDraw;
private boolean mIsAttachedToWindow;
//Synthetic comment -- @@ -171,32 +170,23 @@
}

public void surfaceCreated(SurfaceHolder holder) {
            // The Surface has been created, start our drawing thread.
            mSurfaceViewThread = new Thread() {
                @Override
                public void run() {
                    // Use mock canvas listening to the drawColor() calling.
                    mCanvas = new MockCanvas(Bitmap.createBitmap( BITMAP_WIDTH,
                                                                  BITMAP_HEIGHT,
                                                                  Bitmap.Config.ARGB_8888));
                    draw(mCanvas);

                    // Lock the surface, this returns a Canvas that can be used to render into.
                    Canvas canvas = mHolder.lockCanvas();
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint.setColor(Color.BLUE);
                    canvas.drawRect(RECT_LEFT, RECT_TOP, RECT_RIGHT, RECT_BOTTOM, paint);

                    // And finally unlock and post the surface.
                    mHolder.unlockCanvasAndPost(canvas);
                }
            };

            mSurfaceViewThread.start();
}

public void surfaceDestroyed(SurfaceHolder holder) {
            mSurfaceViewThread = null;
}

public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {







