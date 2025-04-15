/*ADT GLE2: minor unit tests

Change-Id:I231b272363bf1d9652093c747fd2db865f1658c7*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/editors/layout/gscripts/Rect.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/editors/layout/gscripts/Rect.java
//Synthetic comment -- index b09f8f9..1f5948a 100755

//Synthetic comment -- @@ -50,21 +50,24 @@
}

/** Initialize rectangle to the given values. They can be invalid. */
    public Rect set(int x, int y, int w, int h) {
this.x = x;
this.y = y;
this.w = w;
this.h = h;
        return this;
}

/** Initialize rectangle to match the given one. */
    public Rect set(Rect r) {
set(r.x, r.y, r.w, r.h);
        return this;
}

/** Initialize rectangle to match the given one. */
    public Rect set(Rectangle swtRect) {
set(swtRect.x, swtRect.y, swtRect.width, swtRect.height);
        return this;
}

/** Returns a new instance of a rectangle with the same values. */
//Synthetic comment -- @@ -88,7 +91,7 @@

/** Returns true if the rectangle contains the x,y coordinates, borders included. */
public boolean contains(Point p) {
        return p != null && contains(p.x, p.y);
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/editors/layout/gscripts/PointTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/editors/layout/gscripts/PointTest.java
new file mode 100755
//Synthetic comment -- index 0000000..b37995d

//Synthetic comment -- @@ -0,0 +1,121 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.editors.layout.gscripts;

import junit.framework.TestCase;

public class PointTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public final void testPointIntInt() {
        Point p = new Point(1, 2);
        assertEquals(1, p.x);
        assertEquals(2, p.y);

        p = new Point(-3, -4);
        assertEquals(-3, p.x);
        assertEquals(-4, p.y);
    }

    public final void testSet() {
        Point p = new Point(1, 2);
        assertEquals(1, p.x);
        assertEquals(2, p.y);

        p.set(-3, -4);
        assertEquals(-3, p.x);
        assertEquals(-4, p.y);
    }

    public final void testPointPoint() {
        Point p = new Point(1, 2);
        Point p2 = new Point(p);

        assertNotSame(p, p2);
        assertEquals(p, p2);
    }

    public final void testPointPoint_Null() {
        // Constructing a point with null throws an NPE
        try {
            new Point(null);
        } catch (NullPointerException ignore) {
            return; // success
        }

        fail("new Point(null) failed to throew NullPointerException");
    }

    public final void testCopy() {
        Point p = new Point(1, 2);
        Point p2 = p.copy();

        assertNotSame(p, p2);
        assertEquals(p, p2);
    }

    public final void testOffsetBy() {
        Point p = new Point(1, 2);
        Point p2 = p.offsetBy(3, 4);

        assertSame(p, p2);
        assertEquals(1+3, p.x);
        assertEquals(2+4, p.y);
    }

    public final void testEquals_Null() {
        Point p = new Point(1, 2);
        assertFalse(p.equals(null));
    }

    public final void testEquals_UnknownObject() {
        Point p = new Point(1, 2);
        assertFalse(p.equals(new Object()));
    }

    public final void testEquals_Point() {
        Point p = new Point(1, 2);
        Point p1 = new Point(1, 2);
        Point p2 = new Point(-3, -4);

        assertNotSame(p1, p);
        assertTrue(p.equals(p1));

        assertFalse(p.equals(p2));
    }

    public final void testHashCode() {
        Point p = new Point(1, 2);
        Point p1 = new Point(1, 2);
        Point p2 = new Point(-3, -4);

        assertNotSame(p1, p);
        assertEquals(p1.hashCode(), p.hashCode());

        assertFalse(p2.hashCode() == p.hashCode());
    }

    public final void testToString() {
        Point p = new Point(1, 2);
        assertEquals("Point [1x2]", p.toString());
    }

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/editors/layout/gscripts/RectTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/editors/layout/gscripts/RectTest.java
new file mode 100755
//Synthetic comment -- index 0000000..b8d78dd

//Synthetic comment -- @@ -0,0 +1,283 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.editors.layout.gscripts;

import org.eclipse.swt.graphics.Rectangle;

import junit.framework.TestCase;

public class RectTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public final void testRect() {
        Rect r = new Rect();
        assertEquals(0, r.x);
        assertEquals(0, r.y);
        assertEquals(0, r.w);
        assertEquals(0, r.h);
    }

    public final void testRectIntIntIntInt() {
        Rect r = new Rect(1, 2, 3, 4);
        assertEquals(1, r.x);
        assertEquals(2, r.y);
        assertEquals(3, r.w);
        assertEquals(4, r.h);
    }

    public final void testRectRect() {
        Rect r = new Rect(1, 2, 3, 4);
        Rect r2 = new Rect(r);

        assertNotSame(r2, r);
        assertEquals(r2, r);
    }

    public final void testRectRectangle() {
        Rectangle r = new Rectangle(3, 4, 20, 30);
        Rect r2 = new Rect(r);

        assertEquals(3, r2.x);
        assertEquals(4, r2.y);
        assertEquals(20, r2.w);
        assertEquals(30, r2.h);
    }

    public final void testSetIntIntIntInt() {
        Rect r = new Rect(1, 2, 3, 4);
        Rect r2 = r.set(3, 4, 20, 30);

        assertSame(r2, r);
        assertEquals(3, r2.x);
        assertEquals(4, r2.y);
        assertEquals(20, r2.w);
        assertEquals(30, r2.h);
    }

    public final void testSetRect() {
        Rect r = new Rect(1, 2, 3, 4);
        Rect r2 = new Rect(3, 4, 20, 30);
        Rect r3 = r.set(r2);

        assertSame(r3, r);
        assertNotSame(r3, r2);
        assertEquals(3, r.x);
        assertEquals(4, r.y);
        assertEquals(20, r.w);
        assertEquals(30, r.h);
    }

    public final void testSetRectangle() {
        Rect r = new Rect(1, 2, 3, 4);
        Rectangle r2 = new Rectangle(3, 4, 20, 30);
        Rect r3 = r.set(r2);

        assertSame(r3, r);
        assertNotSame(r3, r2);
        assertEquals(3, r.x);
        assertEquals(4, r.y);
        assertEquals(20, r.w);
        assertEquals(30, r.h);
    }

    public final void testCopy() {
        Rect r = new Rect(1, 2, 3, 4);
        Rect r2 = r.copy();

        assertNotSame(r2, r);
        assertEquals(r2, r);
    }

    public final void testIsValid() {
        Rect r = new Rect();
        assertFalse(r.isValid());

        r = new Rect(1, 2, 3, 4);
        assertTrue(r.isValid());

        // Rectangles must have a width > 0 to be valid
        r = new Rect(1, 2, 0, 4);
        assertFalse(r.isValid());
        r = new Rect(1, 2, -5, 4);
        assertFalse(r.isValid());

        // Rectangles must have a height > 0 to be valid
        r = new Rect(1, 2, 3, 0);
        assertFalse(r.isValid());
        r = new Rect(1, 2, 3, -5);
        assertFalse(r.isValid());

        r = new Rect(1, 2, 0, 0);
        assertFalse(r.isValid());
        r = new Rect(1, 2, -20, -5);
        assertFalse(r.isValid());
    }

    public final void testContainsIntInt() {
        Rect r = new Rect(3, 4, 20, 30);

        assertTrue(r.contains(3,    4));
        assertTrue(r.contains(3+19, 4));
        assertTrue(r.contains(3+19, 4+29));
        assertTrue(r.contains(3,    4+29));

        assertFalse(r.contains(3-1, 4));
        assertFalse(r.contains(3,   4-1));
        assertFalse(r.contains(3-1, 4-1));

        assertFalse(r.contains(3+20, 4));
        assertFalse(r.contains(3+20, 4+30));
        assertFalse(r.contains(3,    4+30));
    }

    public final void testContainsIntInt_Invalid() {
        // Invalid rects always return false
        Rect r = new Rect(3, 4, -20, -30);
        assertFalse(r.contains(3,    4));
    }

    public final void testContainsPoint_Null() {
        // contains(null) returns false rather than an NPE
        Rect r = new Rect(3, 4, -20, -30);
        assertFalse(r.contains(null));
    }

    public final void testContainsPoint() {
        Rect r = new Rect(3, 4, 20, 30);

        assertTrue(r.contains(new Point(3,    4)));
        assertTrue(r.contains(new Point(3+19, 4)));
        assertTrue(r.contains(new Point(3+19, 4+29)));
        assertTrue(r.contains(new Point(3,    4+29)));

        assertFalse(r.contains(new Point(3-1, 4)));
        assertFalse(r.contains(new Point(3,   4-1)));
        assertFalse(r.contains(new Point(3-1, 4-1)));

        assertFalse(r.contains(new Point(3+20, 4)));
        assertFalse(r.contains(new Point(3+20, 4+30)));
        assertFalse(r.contains(new Point(3,    4+30)));
    }

    public final void testMoveTo() {
        Rect r = new Rect(3, 4, 20, 30);
        Rect r2 = r.moveTo(100, 200);

        assertSame(r2, r);
        assertEquals(100, r.x);
        assertEquals(200, r.y);
        assertEquals(20, r.w);
        assertEquals(30, r.h);
    }

    public final void testOffsetBy() {
        Rect r = new Rect(3, 4, 20, 30);
        Rect r2 = r.offsetBy(100, 200);

        assertSame(r2, r);
        assertEquals(103, r.x);
        assertEquals(204, r.y);
        assertEquals(20, r.w);
        assertEquals(30, r.h);
    }

    public final void testGetCenter() {
        Rect r = new Rect(3, 4, 20, 30);
        Point p = r.getCenter();

        assertEquals(3+20/2, p.x);
        assertEquals(4+30/2, p.y);
    }

    public final void testGetTopLeft() {
        Rect r = new Rect(3, 4, 20, 30);
        Point p = r.getTopLeft();

        assertEquals(3, p.x);
        assertEquals(4, p.y);
    }

    public final void testGetBottomLeft() {
        Rect r = new Rect(3, 4, 20, 30);
        Point p = r.getBottomLeft();

        assertEquals(3, p.x);
        assertEquals(4+30, p.y);
    }

    public final void testGetTopRight() {
        Rect r = new Rect(3, 4, 20, 30);
        Point p = r.getTopRight();

        assertEquals(3+20, p.x);
        assertEquals(4, p.y);
    }

    public final void testGetBottomRight() {
        Rect r = new Rect(3, 4, 20, 30);
        Point p = r.getBottomRight();

        assertEquals(3+20, p.x);
        assertEquals(4+30, p.y);
    }

    public final void testToString() {
        Rect r = new Rect(3, 4, 20, 30);

        assertEquals("Rect [3x4 - 20x30]", r.toString());
    }

    public final void testEqualsObject() {
        Rect r = new Rect(3, 4, 20, 30);

        assertFalse(r.equals(null));
        assertFalse(r.equals(new Object()));
        assertTrue(r.equals(new Rect(3, 4, 20, 30)));
    }

    public final void testEqualsObject_Invalid() {
        Rect r = new Rect(3, 4, 20, 30);
        assertTrue(r.isValid());

        Rect i1 = new Rect(3, 4, 0, 0);
        assertFalse(i1.isValid());
        Rect i2 = new Rect(10, 20, 0, 0);
        assertFalse(i2.isValid());

        // valid rects can't be equal to invalid rects
        assertFalse(r.equals(i1));
        assertFalse(r.equals(i2));

        // invalid rects are equal to each other whatever their content is
        assertEquals(i2, i1);
    }

    public final void testHashCode() {
        Rect r = new Rect(1, 2, 3, 4);
        Rect r1 = new Rect(3, 4, 20, 30);
        Rect r2 = new Rect(3, 4, 20, 30);

        assertFalse(r1.hashCode() == r.hashCode());
        assertEquals(r2.hashCode(), r1.hashCode());
    }


}







