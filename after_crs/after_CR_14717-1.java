/*Use integer for loop counter instead of float

Change-Id:Iafaccbb3a3a7cbe0d67ed6827906d713c37ce89b*/




//Synthetic comment -- diff --git a/core/java/android/view/VelocityTracker.java b/core/java/android/view/VelocityTracker.java
//Synthetic comment -- index 9a8ee02..b976378 100644

//Synthetic comment -- @@ -190,7 +190,7 @@
final long oldestTime = pastTime[oldestTouch];
float accumX = 0;
float accumY = 0;
            int N = (lastTouch - oldestTouch + NUM_PAST) % NUM_PAST + 1;
// Skip the last received event, since it is probably pretty noisy.
if (N > 3) N--;








