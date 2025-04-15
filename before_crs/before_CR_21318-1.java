/*Protecting views from (bad) MotionEvents

When handling MotionEvents, the method findPointerIndex can return -1
if the current pointer id can't be translated to a pointer index.
Several views are not handling this, which will lead to an out-of-
index crash.

Change-Id:I93ce2420afd83a06b689a1ed35ead7d170cd68f1*/
//Synthetic comment -- diff --git a/core/java/android/widget/AbsListView.java b/core/java/android/widget/AbsListView.java
//Synthetic comment -- index 9a38acc..db4152a 100644

//Synthetic comment -- @@ -2169,6 +2169,10 @@

case MotionEvent.ACTION_MOVE: {
final int pointerIndex = ev.findPointerIndex(mActivePointerId);
final int y = (int) ev.getY(pointerIndex);
deltaY = y - mMotionY;
switch (mTouchMode) {
//Synthetic comment -- @@ -2659,6 +2663,10 @@
switch (mTouchMode) {
case TOUCH_MODE_DOWN:
final int pointerIndex = ev.findPointerIndex(mActivePointerId);
final int y = (int) ev.getY(pointerIndex);
if (startScrollIfNeeded(y - mMotionY)) {
return true;








//Synthetic comment -- diff --git a/core/java/android/widget/HorizontalScrollView.java b/core/java/android/widget/HorizontalScrollView.java
//Synthetic comment -- index fee3455..abf42b0 100644

//Synthetic comment -- @@ -430,6 +430,10 @@
}

final int pointerIndex = ev.findPointerIndex(activePointerId);
final float x = ev.getX(pointerIndex);
final int xDiff = (int) Math.abs(x - mLastMotionX);
if (xDiff > mTouchSlop) {
//Synthetic comment -- @@ -522,6 +526,10 @@
if (mIsBeingDragged) {
// Scroll to follow the motion event
final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
final float x = ev.getX(activePointerIndex);
final int deltaX = (int) (mLastMotionX - x);
mLastMotionX = x;








//Synthetic comment -- diff --git a/core/java/android/widget/ScrollView.java b/core/java/android/widget/ScrollView.java
//Synthetic comment -- index 2ad67ba..2de0903 100644

//Synthetic comment -- @@ -426,6 +426,10 @@
}

final int pointerIndex = ev.findPointerIndex(activePointerId);
final float y = ev.getY(pointerIndex);
final int yDiff = (int) Math.abs(y - mLastMotionY);
if (yDiff > mTouchSlop) {
//Synthetic comment -- @@ -517,6 +521,10 @@
if (mIsBeingDragged) {
// Scroll to follow the motion event
final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
final float y = ev.getY(activePointerIndex);
final int deltaY = (int) (mLastMotionY - y);
mLastMotionY = y;








//Synthetic comment -- diff --git a/core/tests/coretests/src/android/widget/scroll/ScrollViewButtonsAndLabelsTest.java b/core/tests/coretests/src/android/widget/scroll/ScrollViewButtonsAndLabelsTest.java
//Synthetic comment -- index 7efb9aa..39ea91c 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.widget.scroll.ScrollViewButtonsAndLabels;

import android.test.ActivityInstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
//Synthetic comment -- @@ -25,7 +26,8 @@
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.view.KeyEvent;


public class ScrollViewButtonsAndLabelsTest
extends ActivityInstrumentationTestCase<ScrollViewButtonsAndLabels> {
//Synthetic comment -- @@ -161,6 +163,32 @@
mScreenTop, buttonLoc[1]);
}

private int goToBottomButton() {
int numButtons = getActivity().getNumButtons();
Button lastButton = getActivity().getButton(numButtons - 1);







