/*ArgbEvaluator can't evaluate alpha value properly.
This issue was reported and status is now FutureReleasehttp://code.google.com/p/android/issues/detail?id=36158but not yet fixed in master branch.
Signed-off-by: NoraBora <noranbora@gmail.com>

Change-Id:Ia540c6676a0ab8249c34c9646eda495e894f70ed*/




//Synthetic comment -- diff --git a/core/java/android/animation/ArgbEvaluator.java b/core/java/android/animation/ArgbEvaluator.java
//Synthetic comment -- index c3875be..7d078e1 100644

//Synthetic comment -- @@ -40,13 +40,13 @@
*/
public Object evaluate(float fraction, Object startValue, Object endValue) {
int startInt = (Integer) startValue;
        int startA = (startInt >>> 24);
int startR = (startInt >> 16) & 0xff;
int startG = (startInt >> 8) & 0xff;
int startB = startInt & 0xff;

int endInt = (Integer) endValue;
        int endA = (endInt >>> 24);
int endR = (endInt >> 16) & 0xff;
int endG = (endInt >> 8) & 0xff;
int endB = endInt & 0xff;
//Synthetic comment -- @@ -56,4 +56,4 @@
(int)((startG + (int)(fraction * (endG - startG))) << 8) |
(int)((startB + (int)(fraction * (endB - startB))));
}
\ No newline at end of file
}







