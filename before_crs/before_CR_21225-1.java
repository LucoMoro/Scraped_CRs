/*Fix Broken PaintTest#testBreakText1

Bug 3188260

Use getTextWidths to get the widths of the individual
characters of the strings rather than hard coding them.

Change-Id:I17c58f991fa2901e072c69fbcf0885bf85728872*/
//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/PaintTest.java b/tests/tests/graphics/src/android/graphics/cts/PaintTest.java
//Synthetic comment -- index 42e557c..db6a2d6 100644

//Synthetic comment -- @@ -16,6 +16,12 @@

package android.graphics.cts;

import android.graphics.ColorFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
//Synthetic comment -- @@ -34,11 +40,6 @@
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;
import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

@TestTargetClass(Paint.class)
public class PaintTest extends AndroidTestCase {
//Synthetic comment -- @@ -74,67 +75,60 @@
method = "breakText",
args = {char[].class, int.class, int.class, float.class, float[].class}
)
    @BrokenTest("unknown if hardcoded values being checked are correct")
    public void testBreakText1() {
Paint p = new Paint();

char[] chars = {'H', 'I', 'J', 'K', 'L', 'M', 'N'};
        float[] width = {8.0f, 4.0f, 3.0f, 7.0f, 6.0f, 10.0f, 9.0f};
        float[] f = new float[1];

for (int i = 0; i < chars.length; i++) {
            assertEquals(1, p.breakText(chars, i, 1, 20.0f, f));
            assertEquals(width[i], f[0]);
}

        // start from 'H'
        int indexH = 0;
        assertEquals(4, p.breakText(chars, indexH, 4, 30.0f, f));
        assertEquals(22.0f, f[0]);
        assertEquals(3, p.breakText(chars, indexH, 3, 30.0f, f));
        assertEquals(15.0f, f[0]);
        assertEquals(2, p.breakText(chars, indexH, 2, 30.0f, f));
        assertEquals(12.0f, f[0]);
        assertEquals(1, p.breakText(chars, indexH, 1, 30.0f, f));
        assertEquals(8.0f, f[0]);
        assertEquals(0, p.breakText(chars, indexH, 0, 30.0f, f));
        assertEquals(0.0f, f[0]);

        assertEquals(1, p.breakText(chars, indexH + 2, 1, 30.0f, f));
        assertEquals(3.0f, f[0]);
        assertEquals(1, p.breakText(chars, indexH + 2, -1, 30.0f, f));
        assertEquals(3.0f, f[0]);

        assertEquals(1, p.breakText(chars, indexH, -1, 30.0f, f));
        assertEquals(8.0f, f[0]);
        assertEquals(2, p.breakText(chars, indexH, -2, 30.0f, f));
        assertEquals(12.0f, f[0]);
        assertEquals(3, p.breakText(chars, indexH, -3, 30.0f, f));
        assertEquals(15.0f, f[0]);
        assertEquals(4, p.breakText(chars, indexH, -4, 30.0f, f));
        assertEquals(22.0f, f[0]);

        assertEquals(7, p.breakText(chars, indexH, 7, 50.0f, f));
        assertEquals(47.0f, f[0]);
        assertEquals(6, p.breakText(chars, indexH, 7, 40.0f, f));
        assertEquals(38.0f, f[0]);

        assertEquals(7, p.breakText(chars, indexH, -7, 50.0f, null));
        assertEquals(7, p.breakText(chars, indexH, 7, 50.0f, null));

        try {
            p.breakText(chars, 0, 8, 60.0f, null);
            fail("Should throw an ArrayIndexOutOfboundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //except here
        }
        try {
            p.breakText(chars, -1, 7, 50.0f, null);
            fail("Should throw an ArrayIndexOutOfboundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //except here
}

}

@TestTargetNew(
//Synthetic comment -- @@ -143,7 +137,6 @@
args = {java.lang.CharSequence.class, int.class, int.class, boolean.class, float.class,
float[].class}
)
    @BrokenTest("unknown if hardcoded values being checked are correct")
public void testBreakText2() {
Paint p = new Paint();
String string = "HIJKLMN";
//Synthetic comment -- @@ -227,7 +220,6 @@
method = "breakText",
args = {java.lang.String.class, boolean.class, float.class, float[].class}
)
    @BrokenTest("unknown if hardcoded values being checked are correct")
public void testBreakText3() {
Paint p = new Paint();
String string = "HIJKLMN";







