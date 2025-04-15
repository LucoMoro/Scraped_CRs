/*Fix PaintTest#testGetTextWidths Methods

Bug 3188260

Instead of testing hard-coded values check that the various
overloads return the same widths.

Change-Id:I60ed15eacb2c1a5bc39885b8ce1bb8288caab4a6*/




//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/PaintTest.java b/tests/tests/graphics/src/android/graphics/cts/PaintTest.java
//Synthetic comment -- index db6a2d6..5b9d853 100644

//Synthetic comment -- @@ -823,427 +823,48 @@

}

    public void testGetTextWidths() throws Exception {
        String text = "HIJKLMN";
        char[] textChars = text.toCharArray();
        SpannedString textSpan = new SpannedString(text);

        // Test measuring the widths of the entire text
        assertGetTextWidths(text, textChars, textSpan, 0, 7);

        // Test measuring a substring of the text
        assertGetTextWidths(text, textChars, textSpan, 1, 3);

        // Test measuring a substring of zero length.
        assertGetTextWidths(text, textChars, textSpan, 3, 3);

        // Test measuring substrings from the front and back
        assertGetTextWidths(text, textChars, textSpan, 0, 2);
        assertGetTextWidths(text, textChars, textSpan, 4, 7);
}

    /** Tests all four overloads of getTextWidths are the same. */
    private void assertGetTextWidths(String text, char[] textChars, SpannedString textSpan,
            int start, int end) {
Paint p = new Paint();
        int count = end - start;
        float[][] widths = new float[][] {
            new float[count],
            new float[count],
            new float[count],
            new float[count]
        };

        String textSlice = text.substring(start, end);
        assertEquals(count, p.getTextWidths(textSlice, widths[0]));
        assertEquals(count, p.getTextWidths(textChars, start, count, widths[1]));
        assertEquals(count, p.getTextWidths(textSpan, start, end, widths[2]));
        assertEquals(count, p.getTextWidths(text, start, end, widths[3]));

        // Check that the widths returned by the overloads are the same.
        for (int i = 0; i < count; i++) {
            assertEquals(widths[0][i], widths[1][i]);
            assertEquals(widths[1][i], widths[2][i]);
            assertEquals(widths[2][i], widths[3][i]);
}
}








