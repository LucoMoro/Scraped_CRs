/*Decreased the value for ACTION_MOVE in a simulated touch.

If a device is customized with a high touch slop value (~20) in
order to get a better touch experience a cts test will fail
(android.view.cts.GestureDetectorTest#testOnTouchEvent).

The test will fail due to that the the movement in a simulated
touch is based on the slop value rather than a fixed value of a
short move. In this situation cts will recognize a doubletap as
a fling resulting in a test failure.

By using a fixed value in the simulated touch, cts will be able
to give an accurate result also when testing GestureDetector
tests on devices with customized slop values.

Change-Id:If6341b78a1541472f2930f03ed05e2ce3de98ceb*/




//Synthetic comment -- diff --git a/test-runner/src/android/test/TouchUtils.java b/test-runner/src/android/test/TouchUtils.java
//Synthetic comment -- index acbde0b..993ce1c 100644

//Synthetic comment -- @@ -270,9 +270,8 @@
inst.waitForIdleSync();

eventTime = SystemClock.uptimeMillis();
event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE,
                x + 1, y + 1, 0);
inst.sendPointerSync(event);
inst.waitForIdleSync();








