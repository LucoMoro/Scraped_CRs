/*Protecting more views from (bad) MotionEvents

When handling MotionEvents, the method findPointerIndex can return -1
if the current pointer id can't be translated to a pointer index.
Some views are not handling this, which will lead to an out-of-
index crash. In 2585e9b there were checks added, this change adds
some more.

Change-Id:I93ce2420afd83a06b689a1ed35ead7d170cd68f1*/




//Synthetic comment -- diff --git a/core/java/android/widget/HorizontalScrollView.java b/core/java/android/widget/HorizontalScrollView.java
//Synthetic comment -- index 18c4fe6..bc72618 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.InputDevice;
import android.view.KeyEvent;
//Synthetic comment -- @@ -62,6 +63,7 @@

private static final float MAX_SCROLL_FACTOR = ScrollView.MAX_SCROLL_FACTOR;

    private static final String TAG = "HorizontalScrollView";

private long mLastScroll;

//Synthetic comment -- @@ -456,6 +458,11 @@
}

final int pointerIndex = ev.findPointerIndex(activePointerId);
                if (pointerIndex == -1) {
                    Log.e(TAG, "Invalid pointer index");
                    break;
                }

final int x = (int) ev.getX(pointerIndex);
final int xDiff = (int) Math.abs(x - mLastMotionX);
if (xDiff > mTouchSlop) {
//Synthetic comment -- @@ -557,6 +564,11 @@
}
case MotionEvent.ACTION_MOVE:
final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                if (activePointerIndex == -1) {
                    Log.e(TAG, "Invalid active pointer index");
                    break;
                }

final int x = (int) ev.getX(activePointerIndex);
int deltaX = mLastMotionX - x;
if (!mIsBeingDragged && Math.abs(deltaX) > mTouchSlop) {








//Synthetic comment -- diff --git a/core/java/android/widget/ScrollView.java b/core/java/android/widget/ScrollView.java
//Synthetic comment -- index ebc54f4..06c03a6 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import android.os.Bundle;
import android.os.StrictMode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.InputDevice;
import android.view.KeyEvent;
//Synthetic comment -- @@ -69,6 +70,8 @@

static final float MAX_SCROLL_FACTOR = 0.5f;

    private static final String TAG = "ScrollView";

private long mLastScroll;

private final Rect mTempRect = new Rect();
//Synthetic comment -- @@ -478,6 +481,11 @@
}

final int pointerIndex = ev.findPointerIndex(activePointerId);
                if (pointerIndex == -1) {
                    Log.e(TAG, "Invalid pointer index");
                    break;
                }

final int y = (int) ev.getY(pointerIndex);
final int yDiff = Math.abs(y - mLastMotionY);
if (yDiff > mTouchSlop) {
//Synthetic comment -- @@ -585,6 +593,11 @@
}
case MotionEvent.ACTION_MOVE:
final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                if (activePointerIndex == -1) {
                    Log.e(TAG, "Invalid active pointer index");
                    break;
                }

final int y = (int) ev.getY(activePointerIndex);
int deltaY = mLastMotionY - y;
if (!mIsBeingDragged && Math.abs(deltaY) > mTouchSlop) {







