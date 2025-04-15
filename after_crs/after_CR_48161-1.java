/*Some changes added to compile and run with Java 6 and Java 7.

  - characters no UTF-8

Change-Id:Id365f85cae4d3c39d32a81fec200cdefb0a97f43*/




//Synthetic comment -- diff --git a/samples/AccelerometerPlay/src/com/example/android/accelerometerplay/AccelerometerPlayActivity.java b/samples/AccelerometerPlay/src/com/example/android/accelerometerplay/AccelerometerPlayActivity.java
//Synthetic comment -- index c9e840f..298019d 100644

//Synthetic comment -- @@ -175,12 +175,12 @@

/*
* Time-corrected Verlet integration The position Verlet
+                 * integrator is defined as x(t+$t) = x(t) + x(t) - x(t-$t) +
+                 * a(t)$t$2 However, the above equation doesn't handle variable
+                 * $t very well, a time-corrected version is needed: x(t+$t) =
+                 * x(t) + (x(t) - x(t-$t)) * ($t/$t_prev) + a(t)$t$2 We also add
+                 * a simple friction term (f) to the equation: x(t+$t) = x(t) +
+                 * (1-f) * (x(t) - x(t-$t)) * ($t/$t_prev) + a(t)$t$2
*/
final float dTdT = dT * dT;
final float x = mPosX + mOneMinusFriction * dTC * (mPosX - mLastPosX) + mAccelX







