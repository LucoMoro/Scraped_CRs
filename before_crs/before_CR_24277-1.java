/*Incorporated reviewer's comment on the Unit tests.

Incorporated comments from reviewer (pascal.massimino@gmail.com), mostly
nits.

Change-Id:I8d28177d2f08ec162606069465da0d8cca6fa8c7*/
//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/cts/BitmapFactoryTest.java b/tests/tests/graphics/src/android/graphics/cts/BitmapFactoryTest.java
//Synthetic comment -- index eb73f39..fe78477 100644

//Synthetic comment -- @@ -197,8 +197,8 @@
Bitmap b = BitmapFactory.decodeStream(is);
assertNotNull(b);
// Test the bitmap size
            assertEquals(HEIGHTS[i], b.getHeight());
assertEquals(WIDTHS[i], b.getWidth());
}
}

//Synthetic comment -- @@ -210,6 +210,7 @@
public void testDecodeStream4() throws IOException {
BitmapFactory.Options options = new BitmapFactory.Options();
options.inPreferredConfig = Config.ARGB_8888;

// Decode the PNG & WebP test images. The WebP test image has been encoded from PNG test
// image and should have same similar (within some error-tolerance) Bitmap data.
//Synthetic comment -- @@ -218,19 +219,19 @@
assertNotNull(bPng);
assertEquals(bPng.getConfig(), Config.ARGB_8888);

        InputStream iStreamWebp = obtainInputStream(R.drawable.webp_test);
        Bitmap bWebp = BitmapFactory.decodeStream(iStreamWebp, null, options);
        assertNotNull(bWebp);
        compareBitmaps(bPng, bWebp, 16, true);

// Compress the PNG image to WebP format (Quality=90) and decode it back.
// This will test end-to-end WebP encoding and decoding.
ByteArrayOutputStream oStreamWebp = new ByteArrayOutputStream();
assertTrue(bPng.compress(CompressFormat.WEBP, 90, oStreamWebp));
        iStreamWebp = new ByteArrayInputStream(oStreamWebp.toByteArray());
        bWebp = BitmapFactory.decodeStream(iStreamWebp, null, options);
        assertNotNull(bWebp);
        compareBitmaps(bPng, bWebp, 16, true);
}

@TestTargetNew(
//Synthetic comment -- @@ -338,20 +339,19 @@
}

// Compare expected to actual to see if their diff is less then mseMargin.
    // lessThanMargin is to indicate whether we expect the diff to be
    // "less than" or "no less than".
private void compareBitmaps(Bitmap expected, Bitmap actual,
int mseMargin, boolean lessThanMargin) {
        assertEquals("mismatching widths", expected.getWidth(),
                actual.getWidth());
        assertEquals("mismatching heights", expected.getHeight(),
                actual.getHeight());
assertEquals("mismatching configs", expected.getConfig(),
actual.getConfig());

double mse = 0;
        int width = expected.getWidth();
        int height = expected.getHeight();
int[] expectedColors = new int [width * height];
int[] actualColors = new int [width * height];

//Synthetic comment -- @@ -375,10 +375,10 @@
}
}

    private double distance(int exp, int actual) {
        int r = Color.red(actual) - Color.red(exp);
        int g = Color.green(actual) - Color.green(exp);
        int b = Color.blue(actual) - Color.blue(exp);
return r * r + g * g + b * b;
}
}







