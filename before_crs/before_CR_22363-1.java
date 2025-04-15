/*Adjustment for different physical DPI display values

Adjustment for different physical DPI display values that can differ
from the logical DisplayMetrics.density value.

Change-Id:I781ad916446ccb0a78c273c9fb9d05cd10dc38e8*/
//Synthetic comment -- diff --git a/core/java/android/view/ViewConfiguration.java b/core/java/android/view/ViewConfiguration.java
//Synthetic comment -- index 924c9d4..43bf028 100644

//Synthetic comment -- @@ -200,15 +200,21 @@
final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
final float density = metrics.density;

        mEdgeSlop = (int) (density * EDGE_SLOP + 0.5f);
mFadingEdgeLength = (int) (density * FADING_EDGE_LENGTH + 0.5f);
        mMinimumFlingVelocity = (int) (density * MINIMUM_FLING_VELOCITY + 0.5f);
        mMaximumFlingVelocity = (int) (density * MAXIMUM_FLING_VELOCITY + 0.5f);
mScrollbarSize = (int) (density * SCROLL_BAR_SIZE + 0.5f);
        mTouchSlop = (int) (density * TOUCH_SLOP + 0.5f);
        mPagingTouchSlop = (int) (density * PAGING_TOUCH_SLOP + 0.5f);
        mDoubleTapSlop = (int) (density * DOUBLE_TAP_SLOP + 0.5f);
        mWindowTouchSlop = (int) (density * WINDOW_TOUCH_SLOP + 0.5f);

// Size of the screen in bytes, in ARGB_8888 format
mMaximumDrawingCacheSize = 4 * metrics.widthPixels * metrics.heightPixels;







