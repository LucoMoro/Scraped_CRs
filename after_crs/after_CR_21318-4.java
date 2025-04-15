/*Protecting more views from (bad) MotionEvents

When handling MotionEvents, the method findPointerIndex can return -1
if the current pointer id can't be translated to a pointer index.
Some views are not handling this, which will lead to an out-of-
index crash. In 2585e9b there were checks added, this change adds
some more.

Change-Id:I93ce2420afd83a06b689a1ed35ead7d170cd68f1*/




//Synthetic comment -- diff --git a/core/java/android/widget/HorizontalScrollView.java b/core/java/android/widget/HorizontalScrollView.java
//Synthetic comment -- index 18c4fe6..0fd3422 100644

//Synthetic comment -- @@ -456,6 +456,10 @@
}

final int pointerIndex = ev.findPointerIndex(activePointerId);
                if (pointerIndex == -1) {
                    break;
                }

final int x = (int) ev.getX(pointerIndex);
final int xDiff = (int) Math.abs(x - mLastMotionX);
if (xDiff > mTouchSlop) {
//Synthetic comment -- @@ -557,6 +561,10 @@
}
case MotionEvent.ACTION_MOVE:
final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                if (activePointerIndex == -1) {
                    break;
                }

final int x = (int) ev.getX(activePointerIndex);
int deltaX = mLastMotionX - x;
if (!mIsBeingDragged && Math.abs(deltaX) > mTouchSlop) {








//Synthetic comment -- diff --git a/core/java/android/widget/ScrollView.java b/core/java/android/widget/ScrollView.java
//Synthetic comment -- index ebc54f4..a0cf335 100644

//Synthetic comment -- @@ -478,8 +478,10 @@
}

final int pointerIndex = ev.findPointerIndex(activePointerId);
                if (pointerIndex == -1) {
                    break;
                }

if (yDiff > mTouchSlop) {
mIsBeingDragged = true;
mLastMotionY = y;
//Synthetic comment -- @@ -585,6 +587,10 @@
}
case MotionEvent.ACTION_MOVE:
final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                if (activePointerIndex == -1) {
                    break;
                }

final int y = (int) ev.getY(activePointerIndex);
int deltaY = mLastMotionY - y;
if (!mIsBeingDragged && Math.abs(deltaY) > mTouchSlop) {







