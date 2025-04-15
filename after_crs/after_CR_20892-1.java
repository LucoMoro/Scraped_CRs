/*fix scrolling bug upon pinch-zoom

when the user is using two fingers to pinch-zoom a photo, the screen
will keep scrolling to the upper-left direction even when the two
fingers are stationary.

Change-Id:I883f0b2fbb7f1dee7f59039db8f6b9b8fbd30b7cSigned-off-by: Madan Ankapura <mankapur@sta.samsung.com>*/




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
//Synthetic comment -- index 91dfd47..c4e938d 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import android.view.WindowManager;

import com.cooliris.app.App;
import com.cooliris.media.Vector3f;

public final class GridInputProcessor implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener,
ScaleGestureDetector.OnScaleGestureListener {
//Synthetic comment -- @@ -756,12 +757,29 @@
if (Float.isInfinite(scale) || Float.isNaN(scale))
return true;
mScale = scale * mScale;
        boolean performTranslation = true;
if (layer.getState() == GridLayer.STATE_FULL_SCREEN) {
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

                // For support two-finger panning
                float posPrevX = detector.getPrevFocusX();
                float posPrevY = detector.getPrevFocusY();
                posPrevX -= (mCamera.mWidth / 2);
                posPrevY -= (mCamera.mHeight / 2);
                mCamera.convertToRelativeCameraSpace(posPrevX, posPrevY, 0, retValPrev);
            }
if (performTranslation) {
float posX = detector.getFocusX();
float posY = detector.getFocusY();
//Synthetic comment -- @@ -778,8 +796,11 @@
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
//Synthetic comment -- index 7ebb274..e68ae49 100644

//Synthetic comment -- @@ -131,6 +131,10 @@
private MotionEvent mPrevEvent;
private MotionEvent mCurrEvent;

    // Added for supporting moving pinch zoom center
    private float mPrevFocusX;
    private float mPrevFocusY;

private float mFocusX;
private float mFocusY;
private float mPrevFingerDiffX;
//Synthetic comment -- @@ -300,12 +304,16 @@
mCurrFingerDiffX = cvx;
mCurrFingerDiffY = cvy;

        // Added for supporting moving pinch zoom center
        mPrevFocusX = mFocusX;
        mPrevFocusY = mFocusY;
        
mFocusX = cx0 + cvx * 0.5f;
mFocusY = cy0 + cvy * 0.5f;
mTimeDelta = curr.getEventTime() - prev.getEventTime();
mCurrPressure = curr.getPressure(0) + curr.getPressure(1);
mPrevPressure = prev.getPressure(0) + prev.getPressure(1);

// Update the correct finger.
mBottomFingerCurrX = cx0;
mBottomFingerCurrY = cy0;
//Synthetic comment -- @@ -334,6 +342,15 @@
return mGestureInProgress;
}

    // Added for supporting moving pinch zoom center
    public float getPrevFocusX() {
        return mPrevFocusX;
    }

    public float getPrevFocusY() {
        return mPrevFocusY;
    }

/**
* Get the X coordinate of the current gesture's focal point. If a gesture
* is in progress, the focal point is directly between the two pointers







