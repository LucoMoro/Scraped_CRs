/*Fix Paint#measureText Tests

Bug 3188260

Instead of hardcoding values just check that the returned values
of the overloaded methods make some sense.

Change-Id:Ia8d97300d5b12a6528b445e0fbfc0b745665a2af*/




//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/PaintTest.java b/tests/tests/graphics/src/android/graphics/cts/PaintTest.java
//Synthetic comment -- index 61f7e08..20701cb 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package android.graphics.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -28,7 +27,6 @@
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rasterizer;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.Xfermode;
//Synthetic comment -- @@ -37,8 +35,6 @@
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.test.AndroidTestCase;
import android.text.SpannedString;

@TestTargetClass(Paint.class)
//Synthetic comment -- @@ -1270,60 +1266,54 @@
assertEquals(-26, fmi.top);
}

    public void testMeasureText() {
        String text = "HIJKLMN";
        char[] textChars = text.toCharArray();
        SpannedString textSpan = new SpannedString(text);

Paint p = new Paint();
        float[] widths = new float[text.length()];
        for (int i = 0; i < widths.length; i++) {
            widths[i] = p.measureText(text, i, i + 1);
}

        float totalWidth = 0;
        for (int i = 0; i < widths.length; i++) {
            totalWidth += widths[i];
}

        // Test measuring the widths of the entire text
        assertMeasureText(text, textChars, textSpan, 0, 7, totalWidth);

        // Test measuring a substring of the text
        assertMeasureText(text, textChars, textSpan, 1, 3, widths[1] + widths[2]);

        // Test measuring a substring of zero length.
        assertMeasureText(text, textChars, textSpan, 3, 3, 0);

        // Test measuring substrings from the front and back
        assertMeasureText(text, textChars, textSpan, 0, 2, widths[0] + widths[1]);
        assertMeasureText(text, textChars, textSpan, 4, 7, widths[4] + widths[5] + widths[6]);
    }

    /** Tests that all four overloads of measureText are the same and match some value. */
    private void assertMeasureText(String text, char[] textChars, SpannedString textSpan,
            int start, int end, float expectedWidth) {
        Paint p = new Paint();
        int count = end - start;
        float[] widths = new float[] {-1, -1, -1, -1};

        String textSlice = text.substring(start, end);
        widths[0] = p.measureText(textSlice);
        widths[1] = p.measureText(textChars, start, count);
        widths[2] = p.measureText(textSpan, start, end);
        widths[3] = p.measureText(text, start, end);

        // Check that the widths returned by the overloads are the same.
        assertEquals(widths[0], widths[1]);
        assertEquals(widths[1], widths[2]);
        assertEquals(widths[2], widths[3]);
        assertEquals(widths[3], expectedWidth);
}

@TestTargetNew(







