/*Fix crop tests on Windows (RGB endianess issue).

Since endianess can change between an input and output
image in SwtUtils, it's important to not test pixels as
pure int values but instead use the correct RGB palette
mapping.

Change-Id:Icd3aad499a5cc069e8cc804af7511c95849a78a6*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtilsTest.java
//Synthetic comment -- index 5561e52..de7999c 100644

//Synthetic comment -- @@ -240,18 +240,27 @@
scale, alpha);

assertNotNull(result);
        ImageData outData = result.getImageData();
        assertEquals(20, outData.width);
        assertEquals(20, outData.height);

        PaletteData outPalette = outData.palette;
        assertNotNull(outPalette);

        byte[] outAlphaData = outData.alphaData;
        assertNotNull(outAlphaData);

for (int y = 0; y < 20; y++) {
for (int x = 0; x < 20; x++) {
int r = y + 60;
int g = x + 30;

                RGB expected = new RGB(r, g, 0);
                RGB actual = outPalette.getRGB(outData.getPixel(x, y));
                assertEquals(expected, actual);

                byte actualAlpha = outAlphaData[y*20+x];
                assertEquals(alpha, actualAlpha);
}
}
}
//Synthetic comment -- @@ -270,30 +279,41 @@
scale, alpha);

assertNotNull(result);
        ImageData outData = result.getImageData();
        assertEquals(120, outData.width);
        assertEquals(90, outData.height);

        PaletteData outPalette = outData.palette;
        assertNotNull(outPalette);

        byte[] outAlphaData = outData.alphaData;
        assertNotNull(outAlphaData);

for (int y = 0; y < 20; y++) {
for (int x = 0; x < 20; x++) {
int r = y + 10;
int g = x + 10;

                RGB expected = new RGB(r, g, 0);
                RGB actual = outPalette.getRGB(outData.getPixel(x, y));
                assertEquals(expected, actual);

                assertEquals(alpha, outAlphaData[y*120+x]);
}
}
for (int y = 70; y < 90; y++) {
for (int x = 100; x < 120; x++) {
int r = y + 10;
int g = x + 10;

                RGB expected = new RGB(r, g, 0);
                RGB actual = outPalette.getRGB(outData.getPixel(x, y));
                assertEquals(expected, actual);

                assertEquals(alpha, outAlphaData[y*120+x]);
}
}
        assertEquals(0, outAlphaData[40]);
}

/**







