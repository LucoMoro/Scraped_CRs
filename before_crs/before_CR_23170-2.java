/*Android 3.0 is fixed, but Android 2.3.4 ,lastest public release, has this bug.

If you call setDrawingCacheEnabled(true), updateCursorControllerPosition is never called.
I've added PreDrawListener() in InsertionPointCursorController, SelectionModifierCursorController

Change-Id:Iad32e40ca65994b679e0461a7369dd9caa4d4084*/
//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index 68600cf..48b1623 100644

//Synthetic comment -- @@ -8023,6 +8023,14 @@
}
};

InsertionPointCursorController() {
mHandle = new HandleView(this, HandleView.CENTER);
}
//Synthetic comment -- @@ -8031,11 +8039,15 @@
updatePosition();
mHandle.show();
hideDelayed(DELAY_BEFORE_FADE_OUT);
}

public void hide() {
mHandle.hide();
removeCallbacks(mHider);
}

private void hideDelayed(int msec) {
//Synthetic comment -- @@ -8098,7 +8110,13 @@
private long mPreviousTapUpTime = 0;
private int mPreviousTapPositionX;
private int mPreviousTapPositionY;

SelectionModifierCursorController() {
mStartHandle = new HandleView(this, HandleView.LEFT);
mEndHandle = new HandleView(this, HandleView.RIGHT);
//Synthetic comment -- @@ -8115,12 +8133,16 @@
mStartHandle.show();
mEndHandle.show();
hideInsertionPointCursorController();
}

public void hide() {
mStartHandle.hide();
mEndHandle.hide();
mIsShowing = false;
}

public boolean isShowing() {







