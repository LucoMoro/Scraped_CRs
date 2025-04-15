/*Increase height and width of the status bar to accommodate higher resolution devices

Change-Id:I3f96a3714feaaef289fba505dbd38ef33a8391fe*/




//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/InstrumentationTest.java b/tests/tests/app/src/android/app/cts/InstrumentationTest.java
//Synthetic comment -- index 1861ae2..e3eb46d 100644

//Synthetic comment -- @@ -73,8 +73,8 @@
final long eventTime = SystemClock.uptimeMillis();
// use coordinates for MotionEvent that do not include the status bar
// TODO: is there a more deterministic way to get these values
        final long x = 55;
        final long y = 55;
final int metaState = 0;
mMotionEvent = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y,
metaState);







