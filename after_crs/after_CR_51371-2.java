/*Applying ChannelScrolling to remove calculateDragAngle

To avoid angle calculation changed the logic to use virtual areas.
When try to drag or pan, check where the current touch point locates
and determine how to drag a page.

Change-Id:Id9bd08a69c4ac93ff385c8088e6c16bed5dc5f88Signed-off-by: kiwon <kiwon98.lee@samsung.com>*/




//Synthetic comment -- diff --git a/core/java/android/webkit/WebViewClassic.java b/core/java/android/webkit/WebViewClassic.java
//Synthetic comment -- index ae56e6b..a1f64c2 100644

//Synthetic comment -- @@ -1258,6 +1258,42 @@

mAutoFillData = new WebViewCore.AutoFillData();
mEditTextScroller = new Scroller(context);

        // Calculate channel distance
        calculateChannelDistance(context);
    }

    /**
     * Calculate CHANNEL_DISTANCE based on the screen information.
     * @param context A Context object used to access application assets.
     */
    private void calculateChannelDistance(Context context) {
        // Basic concepts
        //  1. The channel distance should be far(?) if the screen size is big.
        //  2. If the density is big(?), the channel distance should be big(?)
        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        final double screenSize = Math.hypot((double)(metrics.widthPixels/metrics.densityDpi),
                (double)(metrics.heightPixels/metrics.densityDpi));
        if (screenSize < 3.0) {
            CHANNEL_DISTANCE = 16;
        } else if (screenSize < 5.0) {
            CHANNEL_DISTANCE = 22;
        } else if (screenSize < 7.0) {
            CHANNEL_DISTANCE = 28;
        } else {
            CHANNEL_DISTANCE = 34;
        }
        CHANNEL_DISTANCE = (int)(CHANNEL_DISTANCE * metrics.density);
        if (CHANNEL_DISTANCE < 16) CHANNEL_DISTANCE = 16;

        if (DebugFlags.WEB_VIEW) {
            Log.v(LOGTAG, "CHANNEL_DISTANCE : " + CHANNEL_DISTANCE
                    + ", density : " + metrics.density
                    + ", screenSize : " + screenSize
                    + ", metrics.heightPixels : " + metrics.heightPixels
                    + ", metrics.widthPixels : " + metrics.widthPixels
                    + ", metrics.densityDpi : " + metrics.densityDpi);
        }
}

// WebViewProvider bindings
//Synthetic comment -- @@ -5717,18 +5753,6 @@
}

/*
*  These values are used to influence the average angle when entering
*  snap mode. If is is the first movement entering snap, we set the average
*  to the appropriate ideal. If the user is entering into snap after the
//Synthetic comment -- @@ -5742,6 +5766,13 @@
*/
private static final float MMA_WEIGHT_N = 5;

    private static final int SNAP_BOUND = 16;
    private static int CHANNEL_DISTANCE = 16;
    private int mFirstTouchX = -1; // the first touched point
    private int mFirstTouchY = -1;
    private int mDistanceX = 0;
    private int mDistanceY = 0;

private boolean inFullScreenMode() {
return mFullScreenHolder != null;
}
//Synthetic comment -- @@ -5830,12 +5861,6 @@
}
}

/*
* Common code for single touch and multi-touch.
* (x, y) denotes current focus point, which is the touch point for single touch
//Synthetic comment -- @@ -5861,6 +5886,11 @@
switch (action) {
case MotionEvent.ACTION_DOWN: {
mConfirmMove = false;

                // Channel Scrolling
                mFirstTouchX = x;
                mFirstTouchY = y;

if (!mEditTextScroller.isFinished()) {
mEditTextScroller.abortAnimation();
}
//Synthetic comment -- @@ -5998,20 +6028,16 @@
break;
}

                    if ((detector == null || !detector.isInProgress())
                            && SNAP_NONE == mSnapScrollMode) {
                        int ax = Math.abs(x - mFirstTouchX);
                        int ay = Math.abs(y - mFirstTouchY);
                        if (ax < SNAP_BOUND && ay < SNAP_BOUND) {
                            break;
                        } else if (ax < SNAP_BOUND) {
mSnapScrollMode = SNAP_Y;
                        } else if (ay < SNAP_BOUND) {
                            mSnapScrollMode = SNAP_X;
}
}

//Synthetic comment -- @@ -6030,31 +6056,21 @@
if (deltaX == 0 && deltaY == 0) {
keepScrollBarsVisible = true;
} else {
                    if (mSnapScrollMode == SNAP_X || mSnapScrollMode == SNAP_Y) {
                        mDistanceX += Math.abs(deltaX);
                        mDistanceY += Math.abs(deltaY);
if (mSnapScrollMode == SNAP_X) {
                            if (mDistanceY > CHANNEL_DISTANCE) {
mSnapScrollMode = SNAP_NONE;
                            } else if (mDistanceX > CHANNEL_DISTANCE) {
                                mDistanceX = mDistanceY = 0;
}
} else {
                            if (mDistanceX > CHANNEL_DISTANCE) {
                                mSnapScrollMode = SNAP_NONE;
                            } else if (mDistanceY > CHANNEL_DISTANCE) {
                                mDistanceX = mDistanceY = 0;
                            }
}
}
if (mSnapScrollMode != SNAP_NONE) {
//Synthetic comment -- @@ -6089,6 +6105,8 @@
break;
}
case MotionEvent.ACTION_UP: {
                mFirstTouchX  = mFirstTouchY = -1;
                mDistanceX = mDistanceY = 0;
if (mIsEditingText && mSelectionStarted) {
endScrollEdit();
mPrivateHandler.sendEmptyMessageDelayed(SCROLL_HANDLE_INTO_VIEW,







