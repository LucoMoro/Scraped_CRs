/*Protecting views from (bad) MotionEvents

When handling MotionEvents, the method findPointerIndex can return -1
if the current pointer id can't be translated to a pointer index.
Several views are not handling this, which will lead to an out-of-
index crash.

Change-Id:I93ce2420afd83a06b689a1ed35ead7d170cd68f1*/




//Synthetic comment -- diff --git a/core/java/android/widget/AbsListView.java b/core/java/android/widget/AbsListView.java
//Synthetic comment -- index 35ef4cb..181300d 100644

//Synthetic comment -- @@ -2181,6 +2181,10 @@

case MotionEvent.ACTION_MOVE: {
final int pointerIndex = ev.findPointerIndex(mActivePointerId);
            if (pointerIndex == -1) {
                break;
            }

final int y = (int) ev.getY(pointerIndex);
deltaY = y - mMotionY;
switch (mTouchMode) {
//Synthetic comment -- @@ -2671,6 +2675,10 @@
switch (mTouchMode) {
case TOUCH_MODE_DOWN:
final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex == -1) {
                    break;
                }

final int y = (int) ev.getY(pointerIndex);
if (startScrollIfNeeded(y - mMotionY)) {
return true;








//Synthetic comment -- diff --git a/core/java/android/widget/HorizontalScrollView.java b/core/java/android/widget/HorizontalScrollView.java
//Synthetic comment -- index db54a0d..9ff3cb1 100644

//Synthetic comment -- @@ -430,6 +430,10 @@
}

final int pointerIndex = ev.findPointerIndex(activePointerId);
                if (pointerIndex == -1) {
                    break;
                }

final float x = ev.getX(pointerIndex);
final int xDiff = (int) Math.abs(x - mLastMotionX);
if (xDiff > mTouchSlop) {
//Synthetic comment -- @@ -524,6 +528,10 @@
if (mIsBeingDragged) {
// Scroll to follow the motion event
final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                    if (activePointerIndex == -1) {
                        break;
                    }

final float x = ev.getX(activePointerIndex);
final int deltaX = (int) (mLastMotionX - x);
mLastMotionX = x;








//Synthetic comment -- diff --git a/core/java/android/widget/ScrollView.java b/core/java/android/widget/ScrollView.java
//Synthetic comment -- index b1e1fbc..362d413 100644

//Synthetic comment -- @@ -426,6 +426,10 @@
}

final int pointerIndex = ev.findPointerIndex(activePointerId);
                if (pointerIndex == -1) {
                    break;
                }

final float y = ev.getY(pointerIndex);
final int yDiff = (int) Math.abs(y - mLastMotionY);
if (yDiff > mTouchSlop) {
//Synthetic comment -- @@ -519,6 +523,10 @@
if (mIsBeingDragged) {
// Scroll to follow the motion event
final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                    if (activePointerIndex == -1) {
                        break;
                    }

final float y = ev.getY(activePointerIndex);
final int deltaY = (int) (mLastMotionY - y);
mLastMotionY = y;







