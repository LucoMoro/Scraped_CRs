/*Fix More Paint#breakText Tests

Bug 3188260

Accidentally uncommented 2 broken tests earlier although they do pass.
Remove those tests and consolidate the testing of the different
breakText functions into a single test.

Change-Id:Iec1b3d9f40d63a647d752b7341b29d06fd3793e9*/
//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/PaintTest.java b/tests/tests/graphics/src/android/graphics/cts/PaintTest.java
//Synthetic comment -- index 5b9d853..28da75a 100644

//Synthetic comment -- @@ -70,216 +70,85 @@
new Paint(p);
}

    @TestTargetNew(
        level = TestLevel.TODO,
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
        level = TestLevel.TODO,
        method = "breakText",
        args = {java.lang.CharSequence.class, int.class, int.class, boolean.class, float.class,
                float[].class}
    )
    public void testBreakText2() {
Paint p = new Paint();
        String string = "HIJKLMN";
        float[] width = {8.0f, 4.0f, 3.0f, 7.0f, 6.0f, 10.0f, 9.0f};
        float[] f = new float[1];

        assertEquals(7, p.breakText(string, 0, 7, true, 50.0f, f));
        assertEquals(47.0f, f[0]);
        assertEquals(6, p.breakText(string, 0, 7, true, 40.0f, f));
        assertEquals(38.0f, f[0]);
        assertEquals(7, p.breakText(string, 0, 7, false, 50.0f, f));
        assertEquals(47.0f, f[0]);

        for (int i = 0; i < string.length(); i++) {
            assertEquals(1, p.breakText(string, i, i + 1, true, 20.0f, f));
            assertEquals(width[i], f[0]);
}

        assertEquals(4, p.breakText(string, 0, 4, true, 30.0f, f));
        assertEquals(22.0f, f[0]);
        assertEquals(3, p.breakText(string, 0, 3, true, 30.0f, f));
        assertEquals(15.0f, f[0]);
        assertEquals(2, p.breakText(string, 0, 2, true, 30.0f, f));
        assertEquals(12.0f, f[0]);
        assertEquals(1, p.breakText(string, 0, 1, true, 30.0f, f));
        assertEquals(8.0f, f[0]);
        assertEquals(0, p.breakText(string, 0, 0, true, 30.0f, f));
        assertEquals(0.0f, f[0]);

        assertEquals(1, p.breakText(string, 2, 3, true, 30.0f, f));
        assertEquals(3.0f, f[0]);
        assertEquals(1, p.breakText(string, 2, 3, false, 30.0f, f));
        assertEquals(3.0f, f[0]);

        assertEquals(1, p.breakText(string, 0, 1, true, 30.0f, f));
        assertEquals(8.0f, f[0]);
        assertEquals(2, p.breakText(string, 0, 2, true, 30.0f, f));
        assertEquals(12.0f, f[0]);
        assertEquals(3, p.breakText(string, 0, 3, true, 30.0f, f));
        assertEquals(15.0f, f[0]);
        assertEquals(4, p.breakText(string, 0, 4, true, 30.0f, f));
        assertEquals(22.0f, f[0]);

        assertEquals(7, p.breakText(string, 0, 7, true, 50.0f, f));
        assertEquals(47.0f, f[0]);
        assertEquals(6, p.breakText(string, 0, 7, true, 40.0f, f));
        assertEquals(38.0f, f[0]);

        assertEquals(7, p.breakText(string, 0, 7, false, 50.0f, null));
        assertEquals(7, p.breakText(string, 0, 7, true, 50.0f, null));

        try {
            p.breakText(string, 0, 8, true, 60.0f, null);
            fail("Should throw an StringIndexOutOfboundsException");
        } catch (StringIndexOutOfBoundsException e) {
            //except here
}
        try {
            p.breakText(string, -1, 7, true, 50.0f, null);
            fail("Should throw an StringIndexOutOfboundsException");
        } catch (StringIndexOutOfBoundsException e) {
            //except here
        }
        try {
            p.breakText(string, 1, -7, true, 50.0f, null);
            fail("Should throw an StringIndexOutOfboundsException");
        } catch (StringIndexOutOfBoundsException e) {
            //except here
        }
        try {
            p.breakText(string, 7, 1, true, 50.0f, null);
            fail("Should throw an StringIndexOutOfboundsException");
        } catch (StringIndexOutOfBoundsException e) {
            //except here
        }

    }

    @TestTargetNew(
        level = TestLevel.TODO,
        method = "breakText",
        args = {java.lang.String.class, boolean.class, float.class, float[].class}
    )
    public void testBreakText3() {
        Paint p = new Paint();
        String string = "HIJKLMN";
        float[] width = {8.0f, 4.0f, 3.0f, 7.0f, 6.0f, 10.0f, 9.0f};
        float[] f = new float[1];

        for (int i = 0; i < string.length(); i++) {
            assertEquals(1, p.breakText(string.substring(i, i+1), true, 20.0f, f));
            assertEquals(width[i], f[0]);
            assertEquals(1, p.breakText(string.substring(i, i+1), false, 20.0f, f));
            assertEquals(width[i], f[0]);
        }

        assertEquals(0, p.breakText("", false, 20.0f, f));
        assertEquals(0.0f, f[0]);

        assertEquals(7, p.breakText(string, true, 50.0f, f));
        assertEquals(47.0f, f[0]);
        assertEquals(7, p.breakText(string, false, 50.0f, f));
        assertEquals(47.0f, f[0]);
        assertEquals(6, p.breakText(string, false, 40.0f, f));
        assertEquals(39.0f, f[0]);
        assertEquals(5, p.breakText(string, false, 35.0f, f));
        assertEquals(35.0f, f[0]);
        assertEquals(4, p.breakText(string, false, 33.0f, f));
        assertEquals(32.0f, f[0]);
        assertEquals(3, p.breakText(string, false, 25.0f, f));
        assertEquals(25.0f, f[0]);
        assertEquals(2, p.breakText(string, false, 20.0f, f));
        assertEquals(19.0f, f[0]);
        assertEquals(1, p.breakText(string, false, 13.0f, f));
        assertEquals(9.0f, f[0]);
        assertEquals(0, p.breakText(string, false, 3.0f, f));
        assertEquals(0.0f, f[0]);

        assertEquals(7, p.breakText(string, true, 50.0f, f));
        assertEquals(47.0f, f[0]);
        assertEquals(6, p.breakText(string, true, 40.0f, f));
        assertEquals(38.0f, f[0]);
        assertEquals(5, p.breakText(string, true, 35.0f, f));
        assertEquals(28.0f, f[0]);
        assertEquals(4, p.breakText(string, true, 25.0f, f));
        assertEquals(22.0f, f[0]);
        assertEquals(3, p.breakText(string, true, 20.0f, f));
        assertEquals(15.0f, f[0]);
        assertEquals(2, p.breakText(string, true, 12.0f, f));
        assertEquals(12.0f, f[0]);
        assertEquals(1, p.breakText(string, true, 10.0f, f));
        assertEquals(8.0f, f[0]);
        assertEquals(0, p.breakText(string, true, 3.0f, f));
        assertEquals(0.0f, f[0]);

        assertEquals(7, p.breakText(string, true, 50.0f, null));
        assertEquals(6, p.breakText(string, true, 40.0f, null));
        assertEquals(5, p.breakText(string, true, 35.0f, null));
        assertEquals(4, p.breakText(string, true, 25.0f, null));
        assertEquals(3, p.breakText(string, true, 20.0f, null));
        assertEquals(2, p.breakText(string, true, 12.0f, null));
        assertEquals(1, p.breakText(string, true, 10.0f, null));
        assertEquals(0, p.breakText(string, true, 3.0f, null));
}

@TestTargetNew(







