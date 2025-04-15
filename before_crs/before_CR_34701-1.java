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
XVelocity = 2.0f;
YVelocity = 2.0f;
        assertEquals(XVelocity, vt.getXVelocity(), ERROR_TOLERANCE);
        assertEquals(YVelocity, vt.getYVelocity(), ERROR_TOLERANCE);
vt.computeCurrentVelocity(10);
        XVelocity = 20.0f;
        YVelocity = 20.0f;
assertEquals(XVelocity, vt.getXVelocity(), ERROR_TOLERANCE);
assertEquals(YVelocity, vt.getYVelocity(), ERROR_TOLERANCE);

//Synthetic comment -- @@ -118,26 +122,26 @@
vt.clear();
vt.addMovement(me);
vt.computeCurrentVelocity(1);
        XVelocity = 0.8242369f;
        YVelocity = 0.8242369f;
        assertEquals(XVelocity, vt.getXVelocity(), ERROR_TOLERANCE);
        assertEquals(YVelocity, vt.getYVelocity(), ERROR_TOLERANCE);

vt.clear();
me.addBatch(100L, 100, 100, .0f, .0f, 0);
vt.addMovement(me);
vt.computeCurrentVelocity(1);
        XVelocity = 0.8500008f;
        YVelocity = 0.8500008f;
        assertEquals(XVelocity, vt.getXVelocity(), ERROR_TOLERANCE);
        assertEquals(YVelocity, vt.getYVelocity(), ERROR_TOLERANCE);

me.recycle();
me = MotionEvent.obtain(0L, 110L, MotionEvent.ACTION_UP, 100f, 100f, 0);
vt.addMovement(me);
vt.computeCurrentVelocity(1);
        XVelocity = 0.8500008f;
        YVelocity = 0.8500008f;
assertEquals(XVelocity, vt.getXVelocity(), ERROR_TOLERANCE);
assertEquals(YVelocity, vt.getYVelocity(), ERROR_TOLERANCE);








