/*Fix android application platform will crash problem.

If slide on password lock panel and repeatedly swith this
panel to lock panel with BACK button and POWER/HOME button,
the android application platform will crash.*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/widget/LockPatternView.java b/core/java/com/android/internal/widget/LockPatternView.java
//Synthetic comment -- index 53be891..bad7292 100644

//Synthetic comment -- @@ -582,6 +582,8 @@
mInProgressY = y;

if (mPatternInProgress) {
final ArrayList<Cell> pattern = mPattern;
final float radius = mSquareWidth * mDiameterFactor * 0.5f;








