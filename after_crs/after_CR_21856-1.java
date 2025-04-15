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

    public void testBreakText() {
        String text = "HIJKLMN";
        char[] textChars = text.toCharArray();
        SpannedString textSpan = new SpannedString(text);

Paint p = new Paint();
        float[] widths = new float[text.length()];
        assertEquals(text.length(), p.getTextWidths(text, widths));

float totalWidth = 0.0f;
        for (int i = 0; i < text.length(); i++) {
totalWidth += widths[i];
}

float[] measured = new float[1];
        for (int i = 0; i < text.length(); i++) {
            assertBreakText(text, textChars, textSpan, i, i + 1, true, totalWidth, 1, widths[i]);
}

// Measure empty string
        assertBreakText(text, textChars, textSpan, 0, 0, true, totalWidth, 0, 0);

// Measure substring from front: "HIJ"
        assertBreakText(text, textChars, textSpan, 0, 3, true, totalWidth,
                3, widths[0] + widths[1] + widths[2]);

// Reverse measure substring from front: "HIJ"
        assertBreakText(text, textChars, textSpan, 0, 3, false, totalWidth,
                3, widths[0] + widths[1] + widths[2]);

// Measure substring from back: "MN"
        assertBreakText(text, textChars, textSpan, 5, 7, false, totalWidth,
                2, widths[5] + widths[6]);

// Reverse measure substring from back: "MN"
        assertBreakText(text, textChars, textSpan, 5, 7, false, totalWidth,
                2, widths[5] + widths[6]);

// Measure substring in the middle: "JKL"
        assertBreakText(text, textChars, textSpan, 2, 5, true, totalWidth,
                3, widths[2] + widths[3] + widths[4]);

// Reverse measure substring in the middle: "JKL"
        assertBreakText(text, textChars, textSpan, 2, 5, false, totalWidth,
                3, widths[2] + widths[3] + widths[4]);

// Measure substring in the middle and restrict width to the first 2 characters.
        assertBreakText(text, textChars, textSpan, 2, 5, true, widths[2] + widths[3],
                2, widths[2] + widths[3]);

// Reverse measure substring in the middle and restrict width to the last 2 characters.
        assertBreakText(text, textChars, textSpan, 2, 5, false, widths[3] + widths[4],
                2, widths[3] + widths[4]);
}

    private void assertBreakText(String text, char[] textChars, SpannedString textSpan,
            int start, int end, boolean measureForwards, float maxWidth, int expectedCount,
            float expectedWidth) {
Paint p = new Paint();

        int count = end - start;
        if (!measureForwards) {
            count = -count;
}

        float[][] measured = new float[][] {
            new float[1],
            new float[1],
            new float[1]
        };
        String textSlice = text.substring(start, end);
        assertEquals(expectedCount, p.breakText(textSlice, measureForwards, maxWidth, measured[0]));
        assertEquals(expectedCount, p.breakText(textChars, start, count, maxWidth, measured[1]));
        assertEquals(expectedCount, p.breakText(textSpan, start, end, measureForwards, maxWidth,
                measured[2]));

        for (int i = 0; i < measured.length; i++) {
            assertEquals("i: " + i, expectedWidth, measured[i][0]);
}
}

@TestTargetNew(







