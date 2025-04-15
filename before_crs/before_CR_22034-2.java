/*Remove Some Broken Paint Tests

Bug 3188260

Change-Id:I7e6b4fc06f2f771b3a96f764518ee3b3cb624f50*/
//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/PaintTest.java b/tests/tests/graphics/src/android/graphics/cts/PaintTest.java
//Synthetic comment -- index 28da75a..61f7e08 100644

//Synthetic comment -- @@ -889,138 +889,6 @@

@TestTargetNew(
level = TestLevel.COMPLETE,
        method = "getTextBounds",
        args = {java.lang.String.class, int.class, int.class, android.graphics.Rect.class}
    )
    @BrokenTest("Test result will be different when run in batch mode")
    public void testGetTextBounds1() throws Exception {
        Paint p = new Paint();
        Rect r = new Rect();
        String s = "HIJKLMN";

        try {
            p.getTextBounds(s, -1, 2, r);
        } catch (IndexOutOfBoundsException e) {
        } catch (RuntimeException e) {
            fail("Should not throw a RuntimeException");
        }

        try {
            p.getTextBounds(s, 0, -2, r);
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextBounds(s, 4, 3, r);
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextBounds(s, 0, 8, r);
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextBounds(s, 0, 2, null);
        } catch (NullPointerException e) {
            //except here
        }

        p.getTextBounds(s, 0, 0, r);
        assertEquals(0, r.bottom);
        assertEquals(-1, r.left);
        assertEquals(0, r.right);
        assertEquals(-1, r.top);

        p.getTextBounds(s, 0, 1, r);
        assertEquals(0, r.bottom);
        assertEquals(1, r.left);
        assertEquals(8, r.right);
        assertEquals(-9, r.top);

        p.getTextBounds(s, 1, 2, r);
        assertEquals(0, r.bottom);
        assertEquals(0, r.left);
        assertEquals(4, r.right);
        assertEquals(-9, r.top);

        p.getTextBounds(s, 0, 6, r);
        assertEquals(3, r.bottom);
        assertEquals(1, r.left);
        assertEquals(38, r.right);
        assertEquals(-9, r.top);
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "getTextBounds",
        args = {char[].class, int.class, int.class, android.graphics.Rect.class}
    )
    @BrokenTest("Test result will be different when run in batch mode")
    public void testGetTextBounds2() throws Exception {
        Paint p = new Paint();
        Rect r = new Rect();
        char[] chars = {'H', 'I', 'J', 'K', 'L', 'M', 'N'};

        try {
            p.getTextBounds(chars, -1, 2, r);
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextBounds(chars, 0, -2, r);
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextBounds(chars, 4, 3, r);
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextBounds(chars, 0, 8, r);
        } catch (IndexOutOfBoundsException e) {
            //except here
        }
        try {
            p.getTextBounds(chars, 0, 2, null);
        } catch (NullPointerException e) {
            //except here
        }

        p.getTextBounds(chars, 0, 0, r);
        assertEquals(0, r.bottom);
        assertEquals(-1, r.left);
        assertEquals(0, r.right);
        assertEquals(0, r.top);

        p.getTextBounds(chars, 0, 1, r);
        assertEquals(0, r.bottom);
        assertEquals(1, r.left);
        assertEquals(8, r.right);
        assertEquals(-9, r.top);

        p.getTextBounds(chars, 1, 2, r);
        assertEquals(3, r.bottom);
        assertEquals(0, r.left);
        assertEquals(7, r.right);
        assertEquals(-9, r.top);

        p.getTextBounds(chars, 0, 6, r);
        assertEquals(3, r.bottom);
        assertEquals(1, r.left);
        assertEquals(38, r.right);
        assertEquals(-9, r.top);
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
method = "setShadowLayer",
args = {float.class, float.class, float.class, int.class}
)
//Synthetic comment -- @@ -1459,379 +1327,6 @@
}

@TestTargetNew(
        level = TestLevel.TODO,
        method = "measureText",
        args = {java.lang.String.class, int.class, int.class}
    )
    @BrokenTest("unknown if hardcoded values being checked are correct")
    public void testMeasureText2() {
        Paint p = new Paint();
        String string = "HIJHIJ";

        // The default text size
        assertEquals(12.0f, p.getTextSize());

        assertEquals(0.0f, p.measureText("", 0, 0));
        assertEquals(8.0f, p.measureText("H", 0, 1));
        assertEquals(4.0f, p.measureText("I", 0, 1));
        assertEquals(3.0f, p.measureText("J", 0, 1));
        assertEquals(8.0f, p.measureText(string, 0, 1));
        assertEquals(15.0f, p.measureText(string, 0, 3));
        assertEquals(15.0f, p.measureText(string, 3, 6));
        assertEquals(30.0f, p.measureText(string, 0, 6));

        p.setTextSize(24.0f);

        assertEquals(17.0f, p.measureText("H", 0, 1));
        assertEquals(8.0f, p.measureText("I", 0, 1));
        assertEquals(7.0f, p.measureText("J", 0, 1));

        p.setTextSize(12.0f);
        p.setTypeface(Typeface.MONOSPACE);

        assertEquals(7.0f, p.measureText("H", 0, 1));
        assertEquals(7.0f, p.measureText("I", 0, 1));
        assertEquals(7.0f, p.measureText("J", 0, 1));

        try {
            p.measureText(string, -1, 3);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }

        try {
            p.measureText(string, 4, 3);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }

        try {
            p.measureText(string, 0, 9);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }

        try {
            p.measureText((String) null, 0, 0);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "measureText",
        args = {java.lang.String.class}
    )
    @BrokenTest("unknown if hardcoded values being checked are correct")
    public void testMeasureText3() {
        Paint p = new Paint();

        // The default text size
        p.setTextSize(12.0f);
        assertEquals(12.0f, p.getTextSize());

        assertEquals(0.0f, p.measureText(""));
        assertEquals(8.0f, p.measureText("H"));
        assertEquals(4.0f, p.measureText("I"));
        assertEquals(3.0f, p.measureText("J"));
        assertEquals(7.0f, p.measureText("K"));
        assertEquals(6.0f, p.measureText("L"));
        assertEquals(10.0f, p.measureText("M"));
        assertEquals(9.0f, p.measureText("N"));
        assertEquals(12.0f, p.measureText("HI"));
        p.setTextSize(24.0f);

        assertEquals(17.0f, p.measureText("H"));
        assertEquals(8.0f, p.measureText("I"));
        assertEquals(7.0f, p.measureText("J"));
        assertEquals(14.0f, p.measureText("K"));
        assertEquals(12.0f, p.measureText("L"));
        assertEquals(21.0f, p.measureText("M"));
        assertEquals(18.0f, p.measureText("N"));
        assertEquals(25.0f, p.measureText("HI"));

        p.setTextSize(12.0f);
        p.setTypeface(Typeface.MONOSPACE);

        assertEquals(7.0f, p.measureText("H"));
        assertEquals(7.0f, p.measureText("I"));
        assertEquals(7.0f, p.measureText("J"));
        assertEquals(7.0f, p.measureText("K"));
        assertEquals(7.0f, p.measureText("L"));
        assertEquals(7.0f, p.measureText("M"));
        assertEquals(7.0f, p.measureText("N"));
        assertEquals(14.0f, p.measureText("HI"));

        try {
            p.measureText(null);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }
    }

    @TestTargetNew(
        level = TestLevel.TODO,
        method = "measureText",
        args = {java.lang.CharSequence.class, int.class, int.class}
    )
    @BrokenTest("unknown if hardcoded values being tested are correct")
    public void testMeasureText4() {

        Paint p = new Paint();
        // CharSequence of String
        String string = "HIJHIJ";
        // The default text size
        p.setTextSize(12.0f);
        assertEquals(12.0f, p.getTextSize());

        assertEquals(8.0f, p.measureText((CharSequence) string, 0, 1));
        assertEquals(15.0f, p.measureText((CharSequence) string, 0, 3));
        assertEquals(15.0f, p.measureText((CharSequence) string, 3, 6));
        assertEquals(30.0f, p.measureText((CharSequence) string, 0, 6));

        p.setTextSize(24.0f);

        assertEquals(17.0f, p.measureText((CharSequence) string, 0, 1));
        assertEquals(32.0f, p.measureText((CharSequence) string, 0, 3));
        assertEquals(32.0f, p.measureText((CharSequence) string, 3, 6));
        assertEquals(64.0f, p.measureText((CharSequence) string, 0, 6));

        p.setTextSize(12.0f);
        p.setTypeface(Typeface.MONOSPACE);

        assertEquals(7.0f, p.measureText((CharSequence) string, 0, 1));
        assertEquals(21.0f, p.measureText((CharSequence) string, 0, 3));
        assertEquals(21.0f, p.measureText((CharSequence) string, 3, 6));
        assertEquals(42.0f, p.measureText((CharSequence) string, 0, 6));

        try {
            p.measureText((CharSequence) "HIJHIJ", -1, 3);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }

        try {
            p.measureText((CharSequence) "HIJHIJ", 4, 3);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }

        try {
            p.measureText((CharSequence) "HIJHIJ", 0, 9);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }

        try {
            p.measureText((CharSequence) null, 0, 0);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }

        // CharSequence of SpannedString
        SpannedString spannedString = new SpannedString("HIJHIJ");
        // The default text size and typeface
        p.setTextSize(12.0f);
        p.setTypeface(Typeface.DEFAULT);

        assertEquals(8.0f, p.measureText(spannedString, 0, 1));
        assertEquals(15.0f, p.measureText(spannedString, 0, 3));
        assertEquals(15.0f, p.measureText(spannedString, 3, 6));
        assertEquals(30.0f, p.measureText(spannedString, 0, 6));

        p.setTextSize(24.0f);

        assertEquals(17.0f, p.measureText(spannedString, 0, 1));
        assertEquals(32.0f, p.measureText(spannedString, 0, 3));
        assertEquals(32.0f, p.measureText(spannedString, 3, 6));
        assertEquals(64.0f, p.measureText(spannedString, 0, 6));

        p.setTextSize(12.0f);
        p.setTypeface(Typeface.MONOSPACE);

        assertEquals(7.0f, p.measureText(spannedString, 0, 1));
        assertEquals(21.0f, p.measureText(spannedString, 0, 3));
        assertEquals(21.0f, p.measureText(spannedString, 3, 6));
        assertEquals(42.0f, p.measureText(spannedString, 0, 6));

        try {
            p.measureText(spannedString, -1, 3);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }

        try {
            p.measureText(spannedString, 4, 3);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }

        try {
            p.measureText(spannedString, 0, 9);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }

        try {
            p.measureText((SpannedString) null, 0, 0);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }

        // CharSequence of SpannableString
        SpannableString spannableString = new SpannableString("HIJHIJ");
        // The default text size and typeface
        p.setTextSize(12.0f);
        p.setTypeface(Typeface.DEFAULT);

        assertEquals(8.0f, p.measureText(spannableString, 0, 1));
        assertEquals(15.0f, p.measureText(spannableString, 0, 3));
        assertEquals(15.0f, p.measureText(spannableString, 3, 6));
        assertEquals(30.0f, p.measureText(spannableString, 0, 6));

        p.setTextSize(24.0f);

        assertEquals(17.0f, p.measureText(spannableString, 0, 1));
        assertEquals(32.0f, p.measureText(spannableString, 0, 3));
        assertEquals(32.0f, p.measureText(spannableString, 3, 6));
        assertEquals(64.0f, p.measureText(spannableString, 0, 6));

        p.setTextSize(12.0f);
        p.setTypeface(Typeface.MONOSPACE);

        assertEquals(7.0f, p.measureText(spannableString, 0, 1));
        assertEquals(21.0f, p.measureText(spannableString, 0, 3));
        assertEquals(21.0f, p.measureText(spannableString, 3, 6));
        assertEquals(42.0f, p.measureText(spannableString, 0, 6));

        try {
            p.measureText(spannableString, -1, 3);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }

        try {
            p.measureText(spannableString, 4, 3);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }

        try {
            p.measureText(spannableString, 0, 9);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }

        try {
            p.measureText((SpannableString) null, 0, 0);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }

        // CharSequence of SpannableStringBuilder (GraphicsOperations)
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("HIJHIJ");
        // The default text size
        p.setTextSize(12.0f);
        p.setTypeface(Typeface.DEFAULT);

        assertEquals(8.0f, p.measureText(spannableStringBuilder, 0, 1));
        assertEquals(15.0f, p.measureText(spannableStringBuilder, 0, 3));
        assertEquals(15.0f, p.measureText(spannableStringBuilder, 3, 6));
        assertEquals(30.0f, p.measureText(spannableStringBuilder, 0, 6));

        p.setTextSize(24.0f);

        assertEquals(17.0f, p.measureText(spannableStringBuilder, 0, 1));
        assertEquals(32.0f, p.measureText(spannableStringBuilder, 0, 3));
        assertEquals(32.0f, p.measureText(spannableStringBuilder, 3, 6));
        assertEquals(64.0f, p.measureText(spannableStringBuilder, 0, 6));

        p.setTextSize(12.0f);
        p.setTypeface(Typeface.MONOSPACE);

        assertEquals(7.0f, p.measureText(spannableStringBuilder, 0, 1));
        assertEquals(21.0f, p.measureText(spannableStringBuilder, 0, 3));
        assertEquals(21.0f, p.measureText(spannableStringBuilder, 3, 6));
        assertEquals(42.0f, p.measureText(spannableStringBuilder, 0, 6));

        try {
            p.measureText(spannableStringBuilder, -1, 3);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }

        try {
            p.measureText(spannableStringBuilder, 4, 3);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }

        try {
            p.measureText(spannableStringBuilder, 0, 9);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }

        try {
            p.measureText((SpannableStringBuilder) null, 0, 0);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }

        // CharSequence of StringBuilder
        StringBuilder stringBuilder = new StringBuilder("HIJHIJ");
        // The default text size and typeface
        p.setTextSize(12.0f);
        p.setTypeface(Typeface.DEFAULT);

        assertEquals(8.0f, p.measureText(stringBuilder, 0, 1));
        assertEquals(15.0f, p.measureText(stringBuilder, 0, 3));
        assertEquals(15.0f, p.measureText(stringBuilder, 3, 6));
        assertEquals(30.0f, p.measureText(stringBuilder, 0, 6));

        p.setTextSize(24.0f);

        assertEquals(17.0f, p.measureText(stringBuilder, 0, 1));
        assertEquals(32.0f, p.measureText(stringBuilder, 0, 3));
        assertEquals(32.0f, p.measureText(stringBuilder, 3, 6));
        assertEquals(64.0f, p.measureText(stringBuilder, 0, 6));

        p.setTextSize(12.0f);
        p.setTypeface(Typeface.MONOSPACE);

        assertEquals(7.0f, p.measureText(stringBuilder, 0, 1));
        assertEquals(21.0f, p.measureText(stringBuilder, 0, 3));
        assertEquals(21.0f, p.measureText(stringBuilder, 3, 6));
        assertEquals(42.0f, p.measureText(stringBuilder, 0, 6));

        try {
            p.measureText(stringBuilder, -1, 3);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }

        try {
            p.measureText(stringBuilder, 4, 3);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }

        try {
            p.measureText(stringBuilder, 0, 9);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }

        try {
            p.measureText((StringBuilder) null, 0, 0);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }

    }

    @TestTargetNew(
level = TestLevel.COMPLETE,
method = "getTextPath",
args = {char[].class, int.class, int.class, float.class, float.class,







