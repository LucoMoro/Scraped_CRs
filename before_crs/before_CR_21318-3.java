/*Protecting more views from (bad) MotionEvents

When handling MotionEvents, the method findPointerIndex can return -1
if the current pointer id can't be translated to a pointer index.
Some views are not handling this, which will lead to an out-of-
index crash. In 2585e9b there were checks added, this change adds
some more.

Change-Id:I93ce2420afd83a06b689a1ed35ead7d170cd68f1*/
//Synthetic comment -- diff --git a/core/java/android/widget/HorizontalScrollView.java b/core/java/android/widget/HorizontalScrollView.java
//Synthetic comment -- index 1683d20..5e75366 100644

//Synthetic comment -- @@ -460,6 +460,10 @@
}

final int pointerIndex = ev.findPointerIndex(activePointerId);
final float x = ev.getX(pointerIndex);
final int xDiff = (int) Math.abs(x - mLastMotionX);
if (xDiff > mTouchSlop) {
//Synthetic comment -- @@ -558,6 +562,10 @@
if (mIsBeingDragged) {
// Scroll to follow the motion event
final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
final float x = ev.getX(activePointerIndex);
final int deltaX = (int) (mLastMotionX - x);
mLastMotionX = x;








//Synthetic comment -- diff --git a/core/java/android/widget/ScrollView.java b/core/java/android/widget/ScrollView.java
//Synthetic comment -- index 767eaee..04ccadc 100644

//Synthetic comment -- @@ -472,6 +472,10 @@
}

final int pointerIndex = ev.findPointerIndex(activePointerId);
final float y = ev.getY(pointerIndex);
final int yDiff = (int) Math.abs(y - mLastMotionY);
if (yDiff > mTouchSlop) {
//Synthetic comment -- @@ -572,6 +576,10 @@
if (mIsBeingDragged) {
// Scroll to follow the motion event
final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
final float y = ev.getY(activePointerIndex);
final int deltaY = (int) (mLastMotionY - y);
mLastMotionY = y;







