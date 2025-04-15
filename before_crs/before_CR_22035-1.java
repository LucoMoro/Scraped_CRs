/*Fix Paint#measureText Tests

Bug 3188260

Instead of hardcoding values just check that the returned values
of the overloaded methods make some sense.

Change-Id:Ia8d97300d5b12a6528b445e0fbfc0b745665a2af*/
//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/PaintTest.java b/tests/tests/graphics/src/android/graphics/cts/PaintTest.java
//Synthetic comment -- index 61f7e08..20701cb 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package android.graphics.cts;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -28,7 +27,6 @@
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rasterizer;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.Xfermode;
//Synthetic comment -- @@ -37,8 +35,6 @@
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.test.AndroidTestCase;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;

@TestTargetClass(Paint.class)
//Synthetic comment -- @@ -1270,60 +1266,54 @@
assertEquals(-26, fmi.top);
}

    @TestTargetNew(
        level = TestLevel.TODO,
        method = "measureText",
        args = {char[].class, int.class, int.class}
    )
    @BrokenTest("unknown if hardcoded values being checked are correct")
    public void testMeasureText1() {
Paint p = new Paint();

        // The default text size
        assertEquals(12.0f, p.getTextSize());

        char[] c = {};
        char[] c2 = {'H'};
        char[] c3 = {'H', 'I', 'J', 'H', 'I', 'J'};
        assertEquals(0.0f, p.measureText(c, 0, 0));
        assertEquals(8.0f, p.measureText(c2, 0, 1));
        assertEquals(8.0f, p.measureText(c3, 0, 1));
        assertEquals(15.0f, p.measureText(c3, 0, 3));
        assertEquals(15.0f, p.measureText(c3, 3, 3));
        assertEquals(30.0f, p.measureText(c3, 0, 6));

        p.setTextSize(24.0f);

        assertEquals(17.0f, p.measureText(c2, 0, 1));

        p.setTextSize(12.0f);
        p.setTypeface(Typeface.MONOSPACE);

        assertEquals(7.0f, p.measureText(c2, 0, 1));

        try {
            p.measureText(c3, -1, 3);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
}

        try {
            p.measureText(c3, 4, 3);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
}

        try {
            p.measureText(c3, 0, 9);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }

        try {
            p.measureText((char[]) null, 0, 0);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }
}

@TestTargetNew(







