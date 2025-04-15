/*Adjust mBiggerTouchSlopSquare to the suitable value

If the scaling factor is larger than 1.0 (i.e. 1.5),
then mTouchSlopSquare(576) is bigger than mBiggerTouchSlopSquare(400).
The double tap condition should be bigger than a single tap's one.
This causes the fail of the following CTS test cases in the device has
over 240 density.
- android.view.cts.GestureDetectorTest
  * testOnTouchEvent
- android.view.cts.GestureDetector_SimpleOnGestureListenerTest
  * testSimpleOnGestureListener

Change-Id:I0e61c13670e1300be1ccf45a89ef89410496fb48*/
//Synthetic comment -- diff --git a/core/java/android/view/GestureDetector.java b/core/java/android/view/GestureDetector.java
old mode 100644
new mode 100755
//Synthetic comment -- index c1e1049..4f6d1f6

//Synthetic comment -- @@ -193,10 +193,8 @@
}
}

    // TODO: ViewConfiguration
    private int mBiggerTouchSlopSquare = 20 * 20;

private int mTouchSlopSquare;
private int mDoubleTapSlopSquare;
private int mMinimumFlingVelocity;
private int mMaximumFlingVelocity;
//Synthetic comment -- @@ -384,10 +382,11 @@
mIgnoreMultitouch = ignoreMultitouch;

// Fallback to support pre-donuts releases
        int touchSlop, doubleTapSlop;
if (context == null) {
//noinspection deprecation
touchSlop = ViewConfiguration.getTouchSlop();
doubleTapSlop = ViewConfiguration.getDoubleTapSlop();
//noinspection deprecation
mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
//Synthetic comment -- @@ -395,11 +394,13 @@
} else {
final ViewConfiguration configuration = ViewConfiguration.get(context);
touchSlop = configuration.getScaledTouchSlop();
doubleTapSlop = configuration.getScaledDoubleTapSlop();
mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();
}
mTouchSlopSquare = touchSlop * touchSlop;
mDoubleTapSlopSquare = doubleTapSlop * doubleTapSlop;
}









//Synthetic comment -- diff --git a/core/java/android/view/ViewConfiguration.java b/core/java/android/view/ViewConfiguration.java
old mode 100644
new mode 100755
//Synthetic comment -- index 924c9d4..0c8e472

//Synthetic comment -- @@ -102,6 +102,12 @@
private static final int TOUCH_SLOP = 16;

/**
* Distance a touch can wander before we think the user is attempting a paged scroll
* (in dips)
*/
//Synthetic comment -- @@ -156,6 +162,7 @@
private final int mMaximumFlingVelocity;
private final int mScrollbarSize;
private final int mTouchSlop;
private final int mPagingTouchSlop;
private final int mDoubleTapSlop;
private final int mWindowTouchSlop;
//Synthetic comment -- @@ -177,6 +184,7 @@
mMaximumFlingVelocity = MAXIMUM_FLING_VELOCITY;
mScrollbarSize = SCROLL_BAR_SIZE;
mTouchSlop = TOUCH_SLOP;
mPagingTouchSlop = PAGING_TOUCH_SLOP;
mDoubleTapSlop = DOUBLE_TAP_SLOP;
mWindowTouchSlop = WINDOW_TOUCH_SLOP;
//Synthetic comment -- @@ -206,6 +214,7 @@
mMaximumFlingVelocity = (int) (density * MAXIMUM_FLING_VELOCITY + 0.5f);
mScrollbarSize = (int) (density * SCROLL_BAR_SIZE + 0.5f);
mTouchSlop = (int) (density * TOUCH_SLOP + 0.5f);
mPagingTouchSlop = (int) (density * PAGING_TOUCH_SLOP + 0.5f);
mDoubleTapSlop = (int) (density * DOUBLE_TAP_SLOP + 0.5f);
mWindowTouchSlop = (int) (density * WINDOW_TOUCH_SLOP + 0.5f);
//Synthetic comment -- @@ -367,6 +376,25 @@
}

/**
* @return Distance a touch can wander before we think the user is scrolling a full page
*         in dips
*/







