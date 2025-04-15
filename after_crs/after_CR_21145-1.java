/*Increase height and width of the status bar to accommodate higher resolution devices

Change-Id:Iecbfff3fdf9c17da01f6e7cd9e300c1692314887*/




//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/InstrumentationTest.java b/tests/tests/app/src/android/app/cts/InstrumentationTest.java
//Synthetic comment -- index 5ce33a0..fce9972 100644

//Synthetic comment -- @@ -71,8 +71,8 @@
final long eventTime = SystemClock.uptimeMillis();
// use coordinates for MotionEvent that do not include the status bar
// TODO: is there a more deterministic way to get these values
        final long x = 55;
        final long y = 55;
final int metaState = 0;
mMotionEvent = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y,
metaState);







