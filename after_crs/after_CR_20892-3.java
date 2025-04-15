/*fix scrolling bug upon pinch-zoom

when the user is using two fingers to pinch-zoom a photo, the screen
will keep scrolling to the upper-left direction even when the two
fingers are stationary.

Change-Id:I5db1af75ed9fd1e031bee92b1c0d8ac91fa6e0ffSigned-off-by: Madan Ankapura <mankapur@sta.samsung.com>*/




//Synthetic comment -- diff --git a/src/com/cooliris/media/GridCameraManager.java b/src/com/cooliris/media/GridCameraManager.java
//Synthetic comment -- index 7b6e05f..5b75b65 100644

//Synthetic comment -- @@ -107,25 +107,37 @@
imgBottomRight.set(position.x + width, position.y + height, 0);
camera.convertToCameraSpace(0, 0, 0, topLeft);
camera.convertToCameraSpace(camera.mWidth, camera.mHeight, 0, bottomRight);
camera.mConvergenceSpeed = 2.0f;
camera.mFriction = 0.0f;
                    if ((bottomRight.x - topLeft.x) > (imgBottomRight.x - imgTopLeft.x)) {
                        final float hCenterExtent= (bottomRight.x + topLeft.x)/2 -
                            (imgBottomRight.x + imgTopLeft.x)/2;
                        camera.moveBy(-hCenterExtent, 0, 0);
                    } else {
                        float leftExtent = topLeft.x - imgTopLeft.x;
                        float rightExtent = bottomRight.x - imgBottomRight.x;
                        if (leftExtent < 0) {
                            retVal = true;
                            camera.moveBy(-leftExtent, 0, 0);
                        }
                        if (rightExtent > 0) {
                            retVal = true;
                            camera.moveBy(-rightExtent, 0, 0);
                        }
}
                    if ((bottomRight.y - topLeft.y) > (imgBottomRight.y - imgTopLeft.y)) {
                        final float vCenterExtent= (bottomRight.y + topLeft.y)/2 -
                            (imgBottomRight.y + imgTopLeft.y)/2;
                        camera.moveBy(0, -vCenterExtent, 0);
                    } else {
                        float topExtent = topLeft.y - imgTopLeft.y;
                        float bottomExtent = bottomRight.y - imgBottomRight.y;
                        if (topExtent < 0) {
                            camera.moveBy(0, -topExtent, 0);
                        }
                        if (bottomExtent > 0) {
                            camera.moveBy(0, -bottomExtent, 0);
                        }
}
}
} finally {








//Synthetic comment -- diff --git a/src/com/cooliris/media/GridInputProcessor.java b/src/com/cooliris/media/GridInputProcessor.java
//Synthetic comment -- index 91dfd47..13c21a8 100644

//Synthetic comment -- @@ -26,11 +26,14 @@
import android.view.MotionEvent;
import android.view.Surface;
import android.view.WindowManager;
import android.content.pm.PackageManager;

import com.cooliris.app.App;
import com.cooliris.media.Vector3f;

public final class GridInputProcessor implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener,
ScaleGestureDetector.OnScaleGestureListener {
    private MotionEvent mPrevEvent;
private int mCurrentFocusSlot;
private boolean mCurrentFocusIsPressed;
private int mCurrentSelectedSlot;
//Synthetic comment -- @@ -64,6 +67,12 @@
private boolean mZoomGesture;
private int mCurrentScaleSlot;
private float mScale;
    // Added for supporting moving pinch zoom center
    private float mPrevFocusX;
    private float mPrevFocusY;
    private float mFocusX;
    private float mFocusY;
    private boolean mSupportPanAndZoom;

public GridInputProcessor(Context context, GridCamera camera, GridLayer layer, RenderView view, Pool<Vector3f> pool,
DisplayItem[] displayItems) {
//Synthetic comment -- @@ -80,6 +89,8 @@
mGestureDetector.setIsLongpressEnabled(true);
mZoomGesture = false;
mScale = 1.0f;
        mSupportPanAndZoom = context.getPackageManager().hasSystemFeature(
            PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH_DISTINCT);
{
WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
mDisplay = windowManager.getDefaultDisplay();
//Synthetic comment -- @@ -166,6 +177,18 @@
mPrevTouchTime = timestamp;
float timeElapsed = (float) delta;
timeElapsed = timeElapsed * 0.001f; // division by 1000 for seconds

        if (!mScaleGestureDetector.isInProgress()
                    && (mActionCode == MotionEvent.ACTION_POINTER_1_DOWN ||
        		mActionCode == MotionEvent.ACTION_POINTER_2_DOWN)
                    && event.getPointerCount() >= 2) {
                if (mPrevEvent != null) {
                    mPrevEvent.recycle();
                }
                mPrevEvent = MotionEvent.obtain(event);
                setPrevFocus(event);
        }

switch (mActionCode) {
case MotionEvent.ACTION_UP:
touchEnded(mTouchPosX, mTouchPosY, timeElapsed);
//Synthetic comment -- @@ -176,6 +199,7 @@
break;
case MotionEvent.ACTION_MOVE:
touchMoved(mTouchPosX, mTouchPosY, timeElapsed);
            setPrevFocus(event);
break;
}
if (!mZoomGesture)
//Synthetic comment -- @@ -299,6 +323,43 @@
}
return false;
}
    private void setPrevFocus(MotionEvent event){

        if(mPrevEvent == null) {
            mPrevEvent  = MotionEvent.obtain(event);
        }
        final MotionEvent prev = mPrevEvent;

        final float px0 = prev.getX(0);
        final float py0 = prev.getY(0);
        final float px1 = prev.getX(1);
        final float py1 = prev.getY(1);
        final float cx0 = event.getX(0);
        final float cy0 = event.getY(0);
        final float cx1 = event.getX(1);
        final float cy1 = event.getY(1);

        final float pvx = px1 - px0;
        final float pvy = py1 - py0;
        final float cvx = cx1 - cx0;
        final float cvy = cy1 - cy0;

        // Added for supporting moving pinch zoom center
        mPrevFocusX = mFocusX;
        mPrevFocusY = mFocusY;

        mFocusX = cx0 + cvx * 0.5f;
        mFocusY = cy0 + cvy * 0.5f;

    }
    // Added for supporting moving pinch zoom center
    public float getPrevFocusX() {
        return mPrevFocusX;
    }

    public float getPrevFocusY() {
        return mPrevFocusY;
    }

private void touchBegan(int posX, int posY) {
mPrevTouchPosX = posX;
//Synthetic comment -- @@ -756,12 +817,29 @@
if (Float.isInfinite(scale) || Float.isNaN(scale))
return true;
mScale = scale * mScale;
        boolean performTranslation = true;
        if (mSupportPanAndZoom && layer.getState() == GridLayer.STATE_FULL_SCREEN) {
float currentScale = layer.getZoomValue();
if (currentScale <= 1.0f)
performTranslation = false;
final Vector3f retVal = new Vector3f();
            final Vector3f retValCenter = new Vector3f();
            final Vector3f retValPrev = new Vector3f();
            if (performTranslation) {
                float posX = detector.getFocusX();
                float posY = detector.getFocusY();
                posX -= (mCamera.mWidth / 2);
                posY -= (mCamera.mHeight / 2);
                mCamera.convertToRelativeCameraSpace(posX, posY, 0, retVal);
                mCamera.convertToRelativeCameraSpace(0, 0, 0, retValCenter);

                float posPrevX = getPrevFocusX();
                float posPrevY = getPrevFocusY();

                posPrevX -= (mCamera.mWidth / 2);
                posPrevY -= (mCamera.mHeight / 2);
                mCamera.convertToRelativeCameraSpace(posPrevX, posPrevY, 0, retValPrev);
            }
if (performTranslation) {
float posX = detector.getFocusX();
float posY = detector.getFocusY();
//Synthetic comment -- @@ -778,8 +856,11 @@
layer.setZoomValue(currentScale * scale);
if (performTranslation) {
mCamera.update(0.001f);
                // Calculate amount of translation for moving zoom center
                retVal.x= (retVal.x - retValCenter.x)*(1.0f-1.0f/scale) + (retValPrev.x-retVal.x);
                retVal.y= (retVal.y - retValCenter.y)*(1.0f-1.0f/scale) + (retValPrev.y-retVal.y);
mCamera.moveBy(retVal.x, retVal.y, 0);
                //layer.constrainCameraForSlot(mCurrentSelectedSlot);
}
}
if (mLayer.getState() == GridLayer.STATE_GRID_VIEW) {








//Synthetic comment -- diff --git a/src/com/cooliris/media/GridLayer.java b/src/com/cooliris/media/GridLayer.java
//Synthetic comment -- index 74aff5d..d15c266 100644

//Synthetic comment -- @@ -1431,7 +1431,7 @@

public void setZoomValue(float f) {
mZoomValue = f;
        centerCameraForSlot(mInputProcessor.getCurrentSelectedSlot(), 10.0f);
}

public void setPickIntent(boolean b) {








//Synthetic comment -- diff --git a/src/com/cooliris/media/ScaleGestureDetector.java b/src/com/cooliris/media/ScaleGestureDetector.java
//Synthetic comment -- index 7ebb274..326fbbd 100644

//Synthetic comment -- @@ -305,7 +305,7 @@
mTimeDelta = curr.getEventTime() - prev.getEventTime();
mCurrPressure = curr.getPressure(0) + curr.getPressure(1);
mPrevPressure = prev.getPressure(0) + prev.getPressure(1);

// Update the correct finger.
mBottomFingerCurrX = cx0;
mBottomFingerCurrY = cy0;







