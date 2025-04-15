/*Test that computed velocity is within reasonable bounds,
rather than testing for specific values.  This allows for
some variation of the velocity computation algorithm.

Change-Id:I06e04676e615ef63a5d3f0bc89da050dc5890f98*/




//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/VelocityTrackerTest.java b/tests/tests/view/src/android/view/cts/VelocityTrackerTest.java
//Synthetic comment -- index 3a68233..db830d3 100644

//Synthetic comment -- @@ -102,13 +102,17 @@
me.addBatch(20L, 20, 20, .0f, .0f, 0);
vt.addMovement(me);
vt.computeCurrentVelocity(1);
        /**
         * Computed velocity should be within reasonable bounds. It should be positive and
         * no more than 1 order of magnitude larger than the simple point-to-point velocity.
         */
XVelocity = 2.0f;
YVelocity = 2.0f;
        assertTrue((0.0f < vt.getXVelocity()) && (vt.getXVelocity() <= (10.0f * XVelocity)));
        assertTrue((0.0f < vt.getYVelocity()) && (vt.getYVelocity() <= (10.0f * YVelocity)));
        XVelocity = 10.0f * vt.getXVelocity();
        YVelocity = 10.0f * vt.getYVelocity();
vt.computeCurrentVelocity(10);
assertEquals(XVelocity, vt.getXVelocity(), ERROR_TOLERANCE);
assertEquals(YVelocity, vt.getYVelocity(), ERROR_TOLERANCE);

//Synthetic comment -- @@ -118,26 +122,26 @@
vt.clear();
vt.addMovement(me);
vt.computeCurrentVelocity(1);
        XVelocity = 1.0f;
        YVelocity = 1.0f;
        assertTrue((0.0f < vt.getXVelocity()) && (vt.getXVelocity() <= (10.0f * XVelocity)));
        assertTrue((0.0f < vt.getYVelocity()) && (vt.getYVelocity() <= (10.0f * YVelocity)));

vt.clear();
me.addBatch(100L, 100, 100, .0f, .0f, 0);
vt.addMovement(me);
vt.computeCurrentVelocity(1);
        XVelocity = 1.0f;
        YVelocity = 1.0f;
        assertTrue((0.0f < vt.getXVelocity()) && (vt.getXVelocity() <= (10.0f * XVelocity)));
        assertTrue((0.0f < vt.getYVelocity()) && (vt.getYVelocity() <= (10.0f * YVelocity)));

me.recycle();
me = MotionEvent.obtain(0L, 110L, MotionEvent.ACTION_UP, 100f, 100f, 0);
vt.addMovement(me);
        XVelocity = vt.getXVelocity();
        YVelocity = vt.getYVelocity();
vt.computeCurrentVelocity(1);
assertEquals(XVelocity, vt.getXVelocity(), ERROR_TOLERANCE);
assertEquals(YVelocity, vt.getYVelocity(), ERROR_TOLERANCE);








