/*Fix Broken PathMeasureTest#testIsClosed

Bug 3188260

It wasn't broken, but it said it was flakey. I didn't see anything
really flakey except the test was somewhat confusing so replace it
with some easy examples like testing a line segment and a circle.

Change-Id:I483cb363e495f97664a55e2d8c05f435a15aba8f*/




//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/PathMeasureTest.java b/tests/tests/graphics/src/android/graphics/cts/PathMeasureTest.java
//Synthetic comment -- index c738d40..614a44e 100644

//Synthetic comment -- @@ -16,17 +16,17 @@

package android.graphics.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Path.Direction;
import android.test.AndroidTestCase;

@TestTargetClass(PathMeasure.class)
public class PathMeasureTest extends AndroidTestCase {
private PathMeasure mPathMeasure;
//Synthetic comment -- @@ -119,13 +119,22 @@
method = "isClosed",
args = {}
)
public void testIsClosed() {
        Path circle = new Path();
        circle.addCircle(0, 0, 1, Direction.CW);

        PathMeasure measure = new PathMeasure(circle, false);
        assertTrue(measure.isClosed());
        measure.setPath(circle, true);
        assertTrue(measure.isClosed());

        Path line = new Path();
        line.lineTo(5, 5);

        measure.setPath(line, false);
        assertFalse(measure.isClosed());
        measure.setPath(line, true);
        assertTrue(measure.isClosed());
}

@TestTargetNew(







