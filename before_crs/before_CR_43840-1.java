/*GlowPadView multiple pointer support.

Event triggered on active pointer up not last pointer up to prevent
palm touch click during one hand gesture. This happens while unlocking
on keyguard on normal size screen.

Change-Id:I09d5028dbe0dfcd84a7cb772a0e2ff2431bf2af1*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/widget/multiwaveview/GlowPadView.java b/core/java/com/android/internal/widget/multiwaveview/GlowPadView.java
//Synthetic comment -- index 4e60b75..5239325 100644

//Synthetic comment -- @@ -118,6 +118,9 @@
private boolean mDragging;
private int mNewTargetResources;

private class AnimationBundle extends ArrayList<Tweener> {
private static final long serialVersionUID = 0xA84D78726F127468L;
private boolean mSuspended;
//Synthetic comment -- @@ -737,16 +740,27 @@

@Override
public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
boolean handled = false;
        switch (action) {
case MotionEvent.ACTION_DOWN:
if (DEBUG) Log.v(TAG, "*** DOWN ***");
handleDown(event);
handleMove(event);
handled = true;
break;

case MotionEvent.ACTION_MOVE:
if (DEBUG) Log.v(TAG, "*** MOVE ***");
handleMove(event);
//Synthetic comment -- @@ -760,6 +774,15 @@
handled = true;
break;

case MotionEvent.ACTION_CANCEL:
if (DEBUG) Log.v(TAG, "*** CANCEL ***");
handleMove(event);
//Synthetic comment -- @@ -777,8 +800,10 @@
}

private void handleDown(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
switchToState(STATE_START, eventX, eventY);
if (!trySwitchToFirstTouchState(eventX, eventY)) {
mDragging = false;
//Synthetic comment -- @@ -789,7 +814,10 @@

private void handleUp(MotionEvent event) {
if (DEBUG && mDragging) Log.v(TAG, "** Handle RELEASE");
        switchToState(STATE_FINISH, event.getX(), event.getY());
}

private void handleCancel(MotionEvent event) {
//Synthetic comment -- @@ -803,9 +831,15 @@
// mActiveTarget = -1; // Drop the active target if canceled.

switchToState(STATE_FINISH, event.getX(), event.getY());
}

private void handleMove(MotionEvent event) {
int activeTarget = -1;
final int historySize = event.getHistorySize();
ArrayList<TargetDrawable> targets = mTargetDrawables;
//Synthetic comment -- @@ -813,8 +847,8 @@
float x = 0.0f;
float y = 0.0f;
for (int k = 0; k < historySize + 1; k++) {
            float eventX = k < historySize ? event.getHistoricalX(k) : event.getX();
            float eventY = k < historySize ? event.getHistoricalY(k) : event.getY();
// tx and ty are relative to wave center
float tx = eventX - mWaveCenterX;
float ty = eventY - mWaveCenterY;







