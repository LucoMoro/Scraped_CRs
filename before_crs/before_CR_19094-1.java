/*Fix for better performance when swiping through different Home Screen panels.

Change-Id:I8ec3283bd2f7dc52585517b39f6674ae534b7fc3*/
//Synthetic comment -- diff --git a/src/com/android/launcher2/Workspace.java b/src/com/android/launcher2/Workspace.java
//Synthetic comment -- index cba1a3b..82586fe 100644

//Synthetic comment -- @@ -918,13 +918,13 @@
if (velocityX > SNAP_VELOCITY && mCurrentScreen > 0) {
// Fling hard enough to move left.
// Don't fling across more than one screen at a time.
                    final int bound = scrolledPos < whichScreen ?
mCurrentScreen - 1 : mCurrentScreen;
snapToScreen(Math.min(whichScreen, bound), velocityX, true);
} else if (velocityX < -SNAP_VELOCITY && mCurrentScreen < getChildCount() - 1) {
// Fling hard enough to move right
// Don't fling across more than one screen at a time.
                    final int bound = scrolledPos > whichScreen ?
mCurrentScreen + 1 : mCurrentScreen;
snapToScreen(Math.max(whichScreen, bound), velocityX, true);
} else {







