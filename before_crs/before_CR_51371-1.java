/*Applying ChannelScrolling to remove calculateDragAngle

To avoid angle calculation changed the logic to use virtual areas.
When try to drag or pan, check where the current touch point locates
and determine how to drag a page.

Change-Id:Id9bd08a69c4ac93ff385c8088e6c16bed5dc5f88Signed-off-by: kiwon <kiwon98.lee@samsung.com>*/
//Synthetic comment -- diff --git a/core/java/android/webkit/WebViewClassic.java b/core/java/android/webkit/WebViewClassic.java
//Synthetic comment -- index ae56e6b..8550882 100644

//Synthetic comment -- @@ -1258,6 +1258,42 @@

mAutoFillData = new WebViewCore.AutoFillData();
mEditTextScroller = new Scroller(context);
}

// WebViewProvider bindings
//Synthetic comment -- @@ -5717,18 +5753,6 @@
}

/*
     * Here is the snap align logic:
     * 1. If it starts nearly horizontally or vertically, snap align;
     * 2. If there is a dramitic direction change, let it go;
     *
     * Adjustable parameters. Angle is the radians on a unit circle, limited
     * to quadrant 1. Values range from 0f (horizontal) to PI/2 (vertical)
     */
    private static final float HSLOPE_TO_START_SNAP = .25f;
    private static final float HSLOPE_TO_BREAK_SNAP = .4f;
    private static final float VSLOPE_TO_START_SNAP = 1.25f;
    private static final float VSLOPE_TO_BREAK_SNAP = .95f;
    /*
*  These values are used to influence the average angle when entering
*  snap mode. If is is the first movement entering snap, we set the average
*  to the appropriate ideal. If the user is entering into snap after the
//Synthetic comment -- @@ -5742,6 +5766,13 @@
*/
private static final float MMA_WEIGHT_N = 5;

private boolean inFullScreenMode() {
return mFullScreenHolder != null;
}
//Synthetic comment -- @@ -5830,12 +5861,6 @@
}
}

    private float calculateDragAngle(int dx, int dy) {
        dx = Math.abs(dx);
        dy = Math.abs(dy);
        return (float) Math.atan2(dy, dx);
    }

/*
* Common code for single touch and multi-touch.
* (x, y) denotes current focus point, which is the touch point for single touch
//Synthetic comment -- @@ -5861,6 +5886,11 @@
switch (action) {
case MotionEvent.ACTION_DOWN: {
mConfirmMove = false;
if (!mEditTextScroller.isFinished()) {
mEditTextScroller.abortAnimation();
}
//Synthetic comment -- @@ -5998,20 +6028,16 @@
break;
}

                    // Only lock dragging to one axis if we don't have a scale in progress.
                    // Scaling implies free-roaming movement. Note this is only ever a question
                    // if mZoomManager.supportsPanDuringZoom() is true.
                    mAverageAngle = calculateDragAngle(deltaX, deltaY);
                    if (detector == null || !detector.isInProgress()) {
                        // if it starts nearly horizontal or vertical, enforce it
                        if (mAverageAngle < HSLOPE_TO_START_SNAP) {
                            mSnapScrollMode = SNAP_X;
                            mSnapPositive = deltaX > 0;
                            mAverageAngle = ANGLE_HORIZ;
                        } else if (mAverageAngle > VSLOPE_TO_START_SNAP) {
mSnapScrollMode = SNAP_Y;
                            mSnapPositive = deltaY > 0;
                            mAverageAngle = ANGLE_VERT;
}
}

//Synthetic comment -- @@ -6030,31 +6056,21 @@
if (deltaX == 0 && deltaY == 0) {
keepScrollBarsVisible = true;
} else {
                    mAverageAngle +=
                        (calculateDragAngle(deltaX, deltaY) - mAverageAngle)
                        / MMA_WEIGHT_N;
                    if (mSnapScrollMode != SNAP_NONE) {
                        if (mSnapScrollMode == SNAP_Y) {
                            // radical change means getting out of snap mode
                            if (mAverageAngle < VSLOPE_TO_BREAK_SNAP) {
                                mSnapScrollMode = SNAP_NONE;
                            }
                        }
if (mSnapScrollMode == SNAP_X) {
                            // radical change means getting out of snap mode
                            if (mAverageAngle > HSLOPE_TO_BREAK_SNAP) {
mSnapScrollMode = SNAP_NONE;
                            }
}
} else {
                        if (mAverageAngle < HSLOPE_TO_START_SNAP) {
                            mSnapScrollMode = SNAP_X;
                            mSnapPositive = deltaX > 0;
                            mAverageAngle = (mAverageAngle + ANGLE_HORIZ) / 2;
                        } else if (mAverageAngle > VSLOPE_TO_START_SNAP) {
                            mSnapScrollMode = SNAP_Y;
                            mSnapPositive = deltaY > 0;
                            mAverageAngle = (mAverageAngle + ANGLE_VERT) / 2;
}
}
if (mSnapScrollMode != SNAP_NONE) {
//Synthetic comment -- @@ -6089,6 +6105,8 @@
break;
}
case MotionEvent.ACTION_UP: {
if (mIsEditingText && mSelectionStarted) {
endScrollEdit();
mPrivateHandler.sendEmptyMessageDelayed(SCROLL_HANDLE_INTO_VIEW,







