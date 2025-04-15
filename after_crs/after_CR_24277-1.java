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
assertEquals(WIDTHS[i], b.getWidth());
            assertEquals(HEIGHTS[i], b.getHeight());
}
}

//Synthetic comment -- @@ -210,6 +210,7 @@
public void testDecodeStream4() throws IOException {
BitmapFactory.Options options = new BitmapFactory.Options();
options.inPreferredConfig = Config.ARGB_8888;
        final int kErrorTol = 16;

// Decode the PNG & WebP test images. The WebP test image has been encoded from PNG test
// image and should have same similar (within some error-tolerance) Bitmap data.
//Synthetic comment -- @@ -218,19 +219,19 @@
assertNotNull(bPng);
assertEquals(bPng.getConfig(), Config.ARGB_8888);

        InputStream iStreamWebp1 = obtainInputStream(R.drawable.webp_test);
        Bitmap bWebp1 = BitmapFactory.decodeStream(iStreamWebp1, null, options);
        assertNotNull(bWebp1);
        compareBitmaps(bPng, bWebp1, kErrorTol, true);

// Compress the PNG image to WebP format (Quality=90) and decode it back.
// This will test end-to-end WebP encoding and decoding.
ByteArrayOutputStream oStreamWebp = new ByteArrayOutputStream();
assertTrue(bPng.compress(CompressFormat.WEBP, 90, oStreamWebp));
        InputStream iStreamWebp2 = new ByteArrayInputStream(oStreamWebp.toByteArray());
        Bitmap bWebp2 = BitmapFactory.decodeStream(iStreamWebp2, null, options);
        assertNotNull(bWebp2);
        compareBitmaps(bPng, bWebp2, kErrorTol, true);
}

@TestTargetNew(
//Synthetic comment -- @@ -338,20 +339,19 @@
}

// Compare expected to actual to see if their diff is less then mseMargin.
    // lessThanMargin is to indicate whether we expect the mean square error
    // to be "less than" or "no less than".
private void compareBitmaps(Bitmap expected, Bitmap actual,
int mseMargin, boolean lessThanMargin) {
        final int width = expected.getWidth();
        final int height = expected.getHeight();

        assertEquals("mismatching widths", width, actual.getWidth());
        assertEquals("mismatching heights", height, actual.getHeight());
assertEquals("mismatching configs", expected.getConfig(),
actual.getConfig());

double mse = 0;
int[] expectedColors = new int [width * height];
int[] actualColors = new int [width * height];

//Synthetic comment -- @@ -375,10 +375,10 @@
}
}

    private double distance(int expect, int actual) {
        final int r = Color.red(actual) - Color.red(expect);
        final int g = Color.green(actual) - Color.green(expect);
        final int b = Color.blue(actual) - Color.blue(expect);
return r * r + g * g + b * b;
}
}







