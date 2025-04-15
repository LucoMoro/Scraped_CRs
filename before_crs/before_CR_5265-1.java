/*Fix android application platform will crash problem.

If slide on password lock panel and repeatedly swith this
panel to lock panel with BACK button and POWER/HOME button,
the android application platform will crash.*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/widget/LockPatternView.java b/core/java/com/android/internal/widget/LockPatternView.java
//Synthetic comment -- index bf00eff..5cfc1ee 100644

//Synthetic comment -- @@ -566,6 +566,9 @@
final ArrayList<Cell> pattern = mPattern;
final float radius = mSquareWidth * mDiameterFactor * 0.5f;

Cell cell = pattern.get(patternSize - 1);

float startX = getCenterXForColumn(cell.column);







