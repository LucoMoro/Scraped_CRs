/*Fix PaintTest#testGetTextWidths Methods

Bug 3188260

Instead of testing hard-coded values check that the various
overloads return the same widths for some strings.

Change-Id:I60ed15eacb2c1a5bc39885b8ce1bb8288caab4a6*/




//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/PaintTest.java b/tests/tests/graphics/src/android/graphics/cts/PaintTest.java
//Synthetic comment -- index db6a2d6..c807803 100644

//Synthetic comment -- @@ -823,427 +823,45 @@

}

    public void testGetTextWidths() throws Exception {
        String text = "HIJKLMN";
        char[] textChars = text.toCharArray();
        SpannedString textSpan = new SpannedString(text);

Paint p = new Paint();
        float[][] widths = new float[][] {
            new float[text.length()],
            new float[text.length()],
            new float[text.length()],
            new float[text.length()]
        };

        // Test measuring the widths of the entire text
        assertEquals(text.length(), p.getTextWidths(text, widths[0]));
        assertEquals(text.length(), p.getTextWidths(textChars, 0, text.length(), widths[1]));
        assertEquals(text.length(), p.getTextWidths(textSpan, 0, text.length(), widths[2]));
        assertEquals(text.length(), p.getTextWidths(text, 0, text.length(), widths[3]));

        for (int i = 0; i < text.length(); i++) {
            assertEquals(widths[0][i], widths[1][i]);
            assertEquals(widths[1][i], widths[2][i]);
            assertEquals(widths[2][i], widths[3][i]);
}

        // Test measuring a substring of the text
        int start = 1;
        int end = 3;
        int count = end - start;
        String textSlice = text.substring(1, 3);
        assertEquals(count, p.getTextWidths(textSlice, widths[0]));
        assertEquals(count, p.getTextWidths(textChars, start, count, widths[1]));
        assertEquals(count, p.getTextWidths(textSpan, start, end, widths[2]));
        assertEquals(count, p.getTextWidths(text, start, end, widths[3]));

        for (int i = 0; i < count; i++) {
            assertEquals(widths[0][i], widths[1][i]);
            assertEquals(widths[1][i], widths[2][i]);
            assertEquals(widths[2][i], widths[3][i]);
}
}








