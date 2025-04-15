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

    protected static final int INVALID_POINTER = -1;
    private int mActivePointerId = INVALID_POINTER;

private class AnimationBundle extends ArrayList<Tweener> {
private static final long serialVersionUID = 0xA84D78726F127468L;
private boolean mSuspended;
//Synthetic comment -- @@ -737,16 +740,27 @@

@Override
public boolean onTouchEvent(MotionEvent event) {
        final int maskedAction = event.getActionMasked();
boolean handled = false;
        switch (maskedAction) {
case MotionEvent.ACTION_DOWN:
if (DEBUG) Log.v(TAG, "*** DOWN ***");
                mActivePointerId = event.getPointerId(0);
handleDown(event);
handleMove(event);
handled = true;
break;

            case MotionEvent.ACTION_POINTER_DOWN:
                if( mActivePointerId == INVALID_POINTER) {
                    if (DEBUG) Log.v(TAG, "*** P-DOWN ***");
                    mActivePointerId = event.getPointerId(event.getActionIndex());
                    handleDown(event);
                    handleMove(event);
                    handled = true;
                }
                break;

case MotionEvent.ACTION_MOVE:
if (DEBUG) Log.v(TAG, "*** MOVE ***");
handleMove(event);
//Synthetic comment -- @@ -760,6 +774,15 @@
handled = true;
break;

            case MotionEvent.ACTION_POINTER_UP:
                if( event.getPointerId(event.getActionIndex()) == mActivePointerId ) {
                    if (DEBUG) Log.v(TAG, "*** P-UP ***");
                    handleMove(event);
                    handleUp(event);
                    handled = true;
                }
                break;

case MotionEvent.ACTION_CANCEL:
if (DEBUG) Log.v(TAG, "*** CANCEL ***");
handleMove(event);
//Synthetic comment -- @@ -777,8 +800,10 @@
}

private void handleDown(MotionEvent event) {
        int pointerIndex = event.findPointerIndex(mActivePointerId);
        if( pointerIndex < 0) pointerIndex = 0;
        float eventX = event.getX(pointerIndex);
        float eventY = event.getY(pointerIndex);
switchToState(STATE_START, eventX, eventY);
if (!trySwitchToFirstTouchState(eventX, eventY)) {
mDragging = false;
//Synthetic comment -- @@ -789,7 +814,10 @@

private void handleUp(MotionEvent event) {
if (DEBUG && mDragging) Log.v(TAG, "** Handle RELEASE");
        int pointerIndex = event.findPointerIndex(mActivePointerId);
        if( pointerIndex < 0) pointerIndex = 0;
        switchToState(STATE_FINISH, event.getX(pointerIndex), event.getY(pointerIndex));
        mActivePointerId = INVALID_POINTER;
}

private void handleCancel(MotionEvent event) {
//Synthetic comment -- @@ -803,9 +831,15 @@
// mActiveTarget = -1; // Drop the active target if canceled.

switchToState(STATE_FINISH, event.getX(), event.getY());
        mActivePointerId = INVALID_POINTER;
}

private void handleMove(MotionEvent event) {
        int pointerIndex = event.findPointerIndex(mActivePointerId);
        if( pointerIndex < 0) {
            // ignore to prevent palm touch click
            return;
        }
int activeTarget = -1;
final int historySize = event.getHistorySize();
ArrayList<TargetDrawable> targets = mTargetDrawables;
//Synthetic comment -- @@ -813,8 +847,8 @@
float x = 0.0f;
float y = 0.0f;
for (int k = 0; k < historySize + 1; k++) {
            float eventX = k < historySize ? event.getHistoricalX(pointerIndex, k) : event.getX(pointerIndex);
            float eventY = k < historySize ? event.getHistoricalY(pointerIndex, k) : event.getY(pointerIndex);
// tx and ty are relative to wave center
float tx = eventX - mWaveCenterX;
float ty = eventY - mWaveCenterY;







