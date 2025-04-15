/*Fix PaintTest#testGetTextWidths Methods

Bug 3188260

Instead of testing hard-coded values check that the various
overloads return the same widths for some strings.

Change-Id:I60ed15eacb2c1a5bc39885b8ce1bb8288caab4a6*/
//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/PaintTest.java b/tests/tests/graphics/src/android/graphics/cts/PaintTest.java
//Synthetic comment -- index db6a2d6..c807803 100644

//Synthetic comment -- @@ -823,427 +823,45 @@

}

    @TestTargetNew(
        level = TestLevel.TODO,
        method = "getTextWidths",
        args = {char[].class, int.class, int.class, float[].class}
    )
    @BrokenTest("unknown if hardcoded values being checked are correct")
    public void testGetTextWidths1() throws Exception {
Paint p = new Paint();
        char[] chars = {'H', 'I', 'J', 'K', 'L', 'M', 'N'};
        float[] width = {8.0f, 4.0f, 3.0f, 7.0f, 6.0f, 10.0f, 9.0f};
        float[] f = new float[7];

        assertEquals(7, p.getTextWidths(chars, 0, 7, f));
        for (int i = 0; i < chars.length; i++) {
            assertEquals(width[i], f[i]);
}

        assertEquals(4, p.getTextWidths(chars, 3, 4, f));
        for (int i = 3; i < chars.length; i++) {
            assertEquals(width[i], f[i - 3]);
        }

        assertEquals(1, p.getTextWidths(chars, 6, 1, f));
        assertEquals(width[6], f[0]);
        assertEquals(0, p.getTextWidths(chars, 6, 0, f));

        try {
            p.getTextWidths(chars, -1, 6, f);
            fail("Should throw an ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextWidths(chars, 0, -1, f);
            fail("Should throw an ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextWidths(chars, 1, 8, f);
            fail("Should throw an ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //except here
        }

        float[] f2 = new float[3];
        try {
            p.getTextWidths(chars, 0, 6, f2);
            fail("Should throw an ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //except here
        }
    }

    @TestTargetNew(
        level = TestLevel.TODO,
        method = "getTextWidths",
        args = {java.lang.CharSequence.class, int.class, int.class, float[].class}
    )
    @BrokenTest("unknown if hardcoded values being checked are correct")
    public void testGetTextWidths2() throws Exception {
        Paint p = new Paint();

        // CharSequence of String
        String string = "HIJKLMN";
        float[] width = {8.0f, 4.0f, 3.0f, 7.0f, 6.0f, 10.0f, 9.0f};
        float[] f = new float[7];

        assertEquals(7, p.getTextWidths((CharSequence) string, 0, 7, f));
        for (int i = 0; i < string.length(); i++) {
            assertEquals(width[i], f[i]);
        }

        assertEquals(4, p.getTextWidths((CharSequence) string, 3, 7, f));
        for (int i = 3; i < string.length(); i++) {
            assertEquals(width[i], f[i - 3]);
        }

        assertEquals(1, p.getTextWidths((CharSequence) string, 6, 7, f));
        assertEquals(width[6], f[0]);
        assertEquals(0, p.getTextWidths((CharSequence) string, 7, 7, f));

        try {
            p.getTextWidths((CharSequence) string, -1, 6, f);
            fail("Should throw an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //except here
        }
        try {
            p.getTextWidths((CharSequence) string, 0, -1, f);
            fail("Should throw an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextWidths((CharSequence) string, 4, 3, f);
            fail("Should throw an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextWidths((CharSequence) string, 1, 8, f);
            fail("Should throw an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        float[] f2 = new float[3];
        try {
            p.getTextWidths((CharSequence) string, 0, 6, f2);
            fail("Should throw an ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //except here
        }
        // CharSequence of SpannedString
        SpannedString spannedString = new SpannedString("HIJKLMN");

        assertEquals(7, p.getTextWidths(spannedString, 0, 7, f));
        for (int i = 0; i < spannedString.length(); i++) {
            assertEquals(width[i], f[i]);
        }

        assertEquals(4, p.getTextWidths(spannedString, 3, 7, f));
        for (int i = 3; i < spannedString.length(); i++) {
            assertEquals(width[i], f[i - 3]);
        }

        assertEquals(1, p.getTextWidths(spannedString, 6, 7, f));
        assertEquals(width[6], f[0]);
        assertEquals(0, p.getTextWidths(spannedString, 7, 7, f));

        try {
            p.getTextWidths(spannedString, -1, 6, f);
            fail("Should throw an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextWidths(spannedString, 0, -1, f);
            fail("Should throw an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextWidths(spannedString, 4, 3, f);
            fail("Should throw an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextWidths(spannedString, 1, 8, f);
            fail("Should throw an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextWidths(spannedString, 0, 6, f2);
            fail("Should throw an ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //except here
        }

        // CharSequence of SpannableString
        SpannableString spannableString = new SpannableString("HIJKLMN");

        assertEquals(7, p.getTextWidths(spannableString, 0, 7, f));
        for (int i = 0; i < spannableString.length(); i++) {
            assertEquals(width[i], f[i]);
        }

        assertEquals(4, p.getTextWidths(spannableString, 3, 7, f));
        for (int i = 3; i < spannableString.length(); i++) {
            assertEquals(width[i], f[i - 3]);
        }

        assertEquals(1, p.getTextWidths(spannableString, 6, 7, f));
        assertEquals(width[6], f[0]);
        assertEquals(0, p.getTextWidths(spannableString, 7, 7, f));

        try {
            p.getTextWidths(spannableString, -1, 6, f);
            fail("Should throw an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextWidths(spannableString, 0, -1, f);
            fail("Should throw an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextWidths(spannableString, 4, 3, f);
            fail("Should throw an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextWidths(spannableString, 1, 8, f);
            fail("Should throw an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextWidths(spannableString, 0, 6, f2);
            fail("Should throw an ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //except here
        }

        // CharSequence of SpannableStringBuilder (GraphicsOperations)
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("HIJKLMN");

        assertEquals(7, p.getTextWidths(spannableStringBuilder, 0, 7, f));
        for (int i = 0; i < spannableStringBuilder.length(); i++) {
            assertEquals(width[i], f[i]);
        }

        assertEquals(4, p.getTextWidths(spannableStringBuilder, 3, 7, f));
        for (int i = 3; i < spannableStringBuilder.length(); i++) {
            assertEquals(width[i], f[i - 3]);
        }

        assertEquals(1, p.getTextWidths(spannableStringBuilder, 6, 7, f));
        assertEquals(width[6], f[0]);
        assertEquals(0, p.getTextWidths(spannableStringBuilder, 7, 7, f));

        try {
            p.getTextWidths(spannableStringBuilder, -1, 6, f);
            fail("Should throw an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextWidths(spannableStringBuilder, 0, -1, f);
            fail("Should throw an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextWidths(spannableStringBuilder, 4, 3, f);
            fail("Should throw an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextWidths(spannableStringBuilder, 1, 8, f);
            fail("Should throw an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextWidths(spannableStringBuilder, 0, 6, f2);
            fail("Should throw an ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //except here
        }

        // CharSequence of StringBuilder
        StringBuilder stringBuilder = new StringBuilder("HIJKLMN");

        assertEquals(7, p.getTextWidths(stringBuilder, 0, 7, f));
        for (int i = 0; i < stringBuilder.length(); i++) {
            assertEquals(width[i], f[i]);
        }

        assertEquals(4, p.getTextWidths(stringBuilder, 3, 7, f));
        for (int i = 3; i < stringBuilder.length(); i++) {
            assertEquals(width[i], f[i - 3]);
        }

        assertEquals(1, p.getTextWidths(stringBuilder, 6, 7, f));
        assertEquals(width[6], f[0]);
        assertEquals(0, p.getTextWidths(stringBuilder, 7, 7, f));

        try {
            p.getTextWidths(stringBuilder, -1, 6, f);
            fail("Should throw an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextWidths(stringBuilder, 0, -1, f);
            fail("Should throw an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextWidths(stringBuilder, 4, 3, f);
            fail("Should throw an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextWidths(stringBuilder, 1, 8, f);
            fail("Should throw an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextWidths(stringBuilder, 0, 6, f2);
            fail("Should throw an ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //except here
        }

    }

    @TestTargetNew(
        level = TestLevel.TODO,
        method = "getTextWidths",
        args = {java.lang.String.class, int.class, int.class, float[].class}
    )
    @BrokenTest("unknown if hardcoded values being checked are correct")
    public void testGetTextWidths3() {
        Paint p = new Paint();
        String string = "HIJKLMN";
        float[] width = {8.0f, 4.0f, 3.0f, 7.0f, 6.0f, 10.0f, 9.0f};
        float[] f = new float[7];

        assertEquals(7, p.getTextWidths(string, 0, 7, f));
        for (int i = 0; i < string.length(); i++) {
            assertEquals(width[i], f[i]);
        }

        assertEquals(4, p.getTextWidths(string, 3, 7, f));
        for (int i = 3; i < string.length(); i++) {
            assertEquals(width[i], f[i - 3]);
        }

        assertEquals(1, p.getTextWidths(string, 6, 7, f));
        assertEquals(width[6], f[0]);
        assertEquals(0, p.getTextWidths(string, 7, 7, f));

        try {
            p.getTextWidths(string, -1, 6, f);
            fail("Should throw an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextWidths(string, 0, -1, f);
            fail("Should throw an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextWidths(string, 4, 3, f);
            fail("Should throw an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //except here
        }

        try {
            p.getTextWidths(string, 1, 8, f);
            fail("Should throw an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //except here
        }
        float[] f2 = new float[3];
        try {
            p.getTextWidths(string, 0, 6, f2);
            fail("Should throw an ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //except here
        }
    }

    @TestTargetNew(
        level = TestLevel.TODO,
        method = "getTextWidths",
        args = {java.lang.String.class, float[].class}
    )
    @BrokenTest("unknown if hardcoded values being checked are correct")
    public void testGetTextWidths4() throws Exception {
        Paint p = new Paint();
        String string = "HIJKLMN";
        float[] width = {8.0f, 4.0f, 3.0f, 7.0f, 6.0f, 10.0f, 9.0f};
        float[] f = new float[7];

        assertEquals(7, p.getTextWidths(string, f));
        for (int i = 0; i < string.length(); i++) {
            assertEquals(width[i], f[i]);
        }

        assertEquals(0, p.getTextWidths("", f));

        try {
            p.getTextWidths(null, f);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
        }

        float[] f2 = new float[3];
        try {
            p.getTextWidths(string, f2);
            fail("Should throw an ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //except here
}
}








