/*Fix x coordinate of the ACTION_UP event in TouchUtils.drag() method

The ACTION_UP event was fired at the "fromX" position instead of being
fired at the "toX" position which is the current value of local var "x".

This bug had no real impact as the VelocityTracker always ignores the last
MotionEvent when it received more than 3 events...*/




//Synthetic comment -- diff --git a/test-runner/android/test/TouchUtils.java b/test-runner/android/test/TouchUtils.java
//Synthetic comment -- index 962b2f9..69c6d2d 100644

//Synthetic comment -- @@ -773,7 +773,7 @@
float xStep = (toX - fromX) / stepCount;

MotionEvent event = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_DOWN, x, y, 0);
inst.sendPointerSync(event);
inst.waitForIdleSync();

//Synthetic comment -- @@ -787,7 +787,7 @@
}

eventTime = SystemClock.uptimeMillis();
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0);
inst.sendPointerSync(event);
inst.waitForIdleSync();
}







