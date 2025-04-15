/*Implementing more robust equality check.

Because of rounding in floating point operations, direct equality
check is not reliable. This implements robust method evaluating
distance of two numbers and comparing it to small enough constant.

Change-Id:I2d71bd0ad6524c015676f0c57c6070eede968709*/




//Synthetic comment -- diff --git a/core/java/android/gesture/GestureUtils.java b/core/java/android/gesture/GestureUtils.java
//Synthetic comment -- index dd221fc..ab73d40 100755

//Synthetic comment -- @@ -38,6 +38,8 @@
*/
public final class GestureUtils {

    /** Delta used for floating point equality checks. */
    private static final float FP_EQUALITY_DELTA = 1e-8f;
private static final float SCALING_THRESHOLD = 0.26f;
private static final float NONUNIFORM_SCALE = (float) Math.sqrt(2);

//Synthetic comment -- @@ -548,7 +550,7 @@
float rightside = (float) Math.sqrt(Math.pow(value, 2) - b);
float lambda1 = -value + rightside;
float lambda2 = -value - rightside;
        if (Math.abs(lambda1 - lambda2) < FP_EQUALITY_DELTA) {
targetVector[0] = 0;
targetVector[1] = 0;
} else {







