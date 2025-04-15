/*Fix Broken PaintTest#testBreakText1

Bug 3188260

Use getTextWidths to get the widths of the individual
characters of the strings rather than hard coding them.

Change-Id:I17c58f991fa2901e072c69fbcf0885bf85728872*/




//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/PaintTest.java b/tests/tests/graphics/src/android/graphics/cts/PaintTest.java
//Synthetic comment -- index 42e557c..db6a2d6 100644

//Synthetic comment -- @@ -16,6 +16,12 @@

package android.graphics.cts;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

import android.graphics.ColorFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
//Synthetic comment -- @@ -34,11 +40,6 @@
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;

@TestTargetClass(Paint.class)
public class PaintTest extends AndroidTestCase {
//Synthetic comment -- @@ -74,67 +75,60 @@
method = "breakText",
args = {char[].class, int.class, int.class, float.class, float[].class}
)
    public void testBreakText_charArray() {
Paint p = new Paint();

char[] chars = {'H', 'I', 'J', 'K', 'L', 'M', 'N'};

        float[] widths = new float[chars.length];
        assertEquals(chars.length, p.getTextWidths(chars, 0, chars.length, widths));

        float totalWidth = 0.0f;
for (int i = 0; i < chars.length; i++) {
            totalWidth += widths[i];
}

        float[] measured = new float[1];
        for (int i = 0; i < chars.length; i++) {
            assertEquals(1, p.breakText(chars, i, 1, totalWidth, measured));
            assertEquals(widths[i], measured[0]);
}

        // Measure empty string
        assertEquals(0, p.breakText(chars, 0, 0, totalWidth, measured));
        assertEquals(0.0f, measured[0]);

        // Measure substring from front: "HIJ"
        assertEquals(3, p.breakText(chars, 0, 3, totalWidth, measured));
        assertEquals(widths[0] + widths[1] + widths[2], measured[0]);

        // Reverse measure substring from front: "HIJ"
        assertEquals(3, p.breakText(chars, 0, -3, totalWidth, measured));
        assertEquals(widths[0] + widths[1] + widths[2], measured[0]);

        // Measure substring from back: "MN"
        assertEquals(2, p.breakText(chars, 5, 2, totalWidth, measured));
        assertEquals(widths[5] + widths[6], measured[0]);

        // Reverse measure substring from back: "MN"
        assertEquals(2, p.breakText(chars, 5, -2, totalWidth, measured));
        assertEquals(widths[5] + widths[6], measured[0]);

        // Measure substring in the middle: "JKL"
        assertEquals(3, p.breakText(chars, 2, 3, totalWidth, measured));
        assertEquals(widths[2] + widths[3] + widths[4], measured[0]);

        // Reverse measure substring in the middle: "JKL"
        assertEquals(3, p.breakText(chars, 2, -3, totalWidth, measured));
        assertEquals(widths[2] + widths[3] + widths[4], measured[0]);

        // Measure substring in the middle and restrict width to the first 2 characters.
        assertEquals(2, p.breakText(chars, 2, 3, widths[2] + widths[3], measured));
        assertEquals(widths[2] + widths[3], measured[0]);

        // Reverse measure substring in the middle and restrict width to the last 2 characters.
        assertEquals(2, p.breakText(chars, 2, -3, widths[3] + widths[4], measured));
        assertEquals(widths[3] + widths[4], measured[0]);
}

@TestTargetNew(
//Synthetic comment -- @@ -143,7 +137,6 @@
args = {java.lang.CharSequence.class, int.class, int.class, boolean.class, float.class,
float[].class}
)
public void testBreakText2() {
Paint p = new Paint();
String string = "HIJKLMN";
//Synthetic comment -- @@ -227,7 +220,6 @@
method = "breakText",
args = {java.lang.String.class, boolean.class, float.class, float[].class}
)
public void testBreakText3() {
Paint p = new Paint();
String string = "HIJKLMN";







