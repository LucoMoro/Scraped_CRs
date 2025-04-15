/*GlobalTime: Compute the Sun's position when the application is launched.

Fix to calculate the sun's position when the application is started.
This ensures that the day and night difference is displayed if the
application is started within a minute after the system has been up.

Change-Id:I5f7ec1b1c34cbe629e792aec5c232b37981d2eb6*/




//Synthetic comment -- diff --git a/samples/GlobalTime/src/com/android/globaltime/GlobalTime.java b/samples/GlobalTime/src/com/android/globaltime/GlobalTime.java
//Synthetic comment -- index d96b644..bda5086 100644

//Synthetic comment -- @@ -1414,7 +1414,8 @@

// Use the message's time, it's good enough and
// allows us to avoid a system call.
                if ((mLastSunPositionTime == 0) ||
		    ((msg.getWhen() - mLastSunPositionTime) >= ONE_MINUTE)) {
// Recompute the sun's position once per minute
// Place the light at the Sun's direction
computeSunDirection();







