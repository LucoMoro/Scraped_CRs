/*Fix the other SwtUtilsTest methods under Windows.

Fixes the following issues:
- All methods must use the palette mask to compare RGB values.
- Use a non-trivial default color for testing. The previous
  choice of 0xFF00FF00 would not allow detecting RGB <-> BGR
  mixups and the 0xFF alpha mask prevented from noticing the
  fill operation was pre-multiplying the colors.
- Assert the converted image as the expected transparency type.

Change-Id:I1dc7518a7d7000bce75e1c4166a9921ba40a43cb*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtilsTest.java
//Synthetic comment -- index 8b11887..49d782a 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.ide.common.api.Rect;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
//Synthetic comment -- @@ -35,15 +36,18 @@
public class SwtUtilsTest extends TestCase {

public void testImageConvertNoAlpha() throws Exception {
        // Note: We need an TYPE_INT_ARGB SWT image here (instead of TYPE_INT_ARGB_PRE) to
        // prevent the alpha from being pre-multiplied into the RGB when drawing the image.
        BufferedImage inImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
Graphics g = inImage.getGraphics();
        g.setColor(new Color(0xAA112233, true));
g.fillRect(0, 0, inImage.getWidth(), inImage.getHeight());
g.dispose();

Shell shell = new Shell();
Display display = shell.getDisplay();

        // Convert the RGB image, effectively discarding the alpha channel entirely.
Image outImage = SwtUtils.convertToSwt(display, inImage, false, -1);
assertNotNull(outImage);

//Synthetic comment -- @@ -51,6 +55,7 @@
assertEquals(inImage.getWidth(), outData.width);
assertEquals(inImage.getHeight(), outData.height);
assertNull(outData.alphaData);
        assertEquals(SWT.TRANSPARENCY_NONE, outData.getTransparencyType());

PaletteData inPalette  = SwtUtils.getAwtPaletteData(inImage.getType());
PaletteData outPalette = outData.palette;
//Synthetic comment -- @@ -60,8 +65,8 @@
// Note: we can't compare pixel directly as integers since convertToSwt() might
// have changed the RGBA ordering depending on the platform (e.g. it will on
// Windows.)
                RGB expected = inPalette.getRGB( inImage.getRGB(  x, y));
                RGB actual   = outPalette.getRGB(outData.getPixel(x, y));
assertEquals(expected, actual);
}
}
//Synthetic comment -- @@ -70,87 +75,123 @@
BufferedImage awtImage = SwtUtils.convertToAwt(outImage);
assertNotNull(awtImage);

        // Both image have the same RGBA ordering
        assertEquals(BufferedImage.TYPE_INT_ARGB, inImage.getType());
        assertEquals(BufferedImage.TYPE_INT_ARGB, awtImage.getType());

        int awtAlphaMask = 0xFF000000;

for (int y = 0; y < outData.height; y++) {
for (int x = 0; x < outData.width; x++) {
// Note: we can compare pixels as integers since we just
                // asserted both images have the same color image type except
                // for the content of the alpha channel.
                int actual = awtImage.getRGB(x, y);
                assertEquals(awtAlphaMask, actual & awtAlphaMask);
                assertEquals(awtAlphaMask | inImage.getRGB(x, y), awtImage.getRGB(x, y));
}
}
}

public void testImageConvertGlobalAlpha() throws Exception {
        BufferedImage inImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = inImage.getGraphics();
        g.setColor(new Color(0xAA112233, true));
        g.fillRect(0, 0, inImage.getWidth(), inImage.getHeight());
g.dispose();

Shell shell = new Shell();
Display display = shell.getDisplay();

        Image outImage = SwtUtils.convertToSwt(display, inImage, false, 128);
        assertNotNull(outImage);

        ImageData outData = outImage.getImageData();
        assertEquals(inImage.getWidth(), outData.width);
        assertEquals(inImage.getHeight(), outData.height);
        assertEquals(128, outData.alpha);
        assertEquals(SWT.TRANSPARENCY_NONE, outData.getTransparencyType());
        assertNull(outData.alphaData);

        PaletteData inPalette  = SwtUtils.getAwtPaletteData(inImage.getType());
        PaletteData outPalette = outData.palette;

        for (int y = 0; y < outData.height; y++) {
            for (int x = 0; x < outData.width; x++) {

                RGB expected = inPalette.getRGB( inImage.getRGB(  x, y));
                RGB actual   = outPalette.getRGB(outData.getPixel(x, y));
                assertEquals(expected, actual);
}
}
}

public void testImageConvertAlpha() throws Exception {
        BufferedImage inImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = inImage.getGraphics();
        g.setColor(new Color(0xAA112233, true));
        g.fillRect(0, 0, inImage.getWidth(), inImage.getHeight());
g.dispose();

Shell shell = new Shell();
Display display = shell.getDisplay();

        Image outImage = SwtUtils.convertToSwt(display, inImage, true, -1);
        assertNotNull(outImage);

        ImageData outData = outImage.getImageData();
        assertEquals(inImage.getWidth(), outData.width);
        assertEquals(inImage.getHeight(), outData.height);
        assertEquals(SWT.TRANSPARENCY_ALPHA, outData.getTransparencyType());

        PaletteData inPalette  = SwtUtils.getAwtPaletteData(inImage.getType());
        PaletteData outPalette = outData.palette;

        for (int y = 0; y < outData.height; y++) {
            for (int x = 0; x < outData.width; x++) {
                RGB expected = inPalette.getRGB( inImage.getRGB(  x, y));
                RGB actual   = outPalette.getRGB(outData.getPixel(x, y));
                assertEquals(expected, actual);

                // Note: >> instead of >>> since we will compare with byte (a signed number)
                int expectedAlpha = inImage.getRGB(x, y) >> 24;
                int actualAlpha = outData.alphaData[y * outData.width + x];
                assertEquals(expectedAlpha, actualAlpha);
}
}
}

public void testImageConvertAlphaMultiplied() throws Exception {
        BufferedImage inImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = inImage.getGraphics();
        g.setColor(new Color(0xAA112233, true));
        g.fillRect(0, 0, inImage.getWidth(), inImage.getHeight());
g.dispose();

Shell shell = new Shell();
Display display = shell.getDisplay();
        Image outImage = SwtUtils.convertToSwt(display, inImage, true, 32);
        assertNotNull(outImage);

        // Expected alpha is 0xAA from the AWT input image pre-multiplied by 32 in convertToSwt.
        int expectedAlpha = (0xAA * 32) >> 8;

        ImageData outData = outImage.getImageData();
        assertEquals(inImage.getWidth(), outData.width);
        assertEquals(inImage.getHeight(), outData.height);
        assertEquals(SWT.TRANSPARENCY_ALPHA, outData.getTransparencyType());

        PaletteData inPalette  = SwtUtils.getAwtPaletteData(inImage.getType());
        PaletteData outPalette = outData.palette;

        for (int y = 0; y < outData.height; y++) {
            for (int x = 0; x < outData.width; x++) {
                RGB expected = inPalette.getRGB( inImage.getRGB(  x, y));
                RGB actual   = outPalette.getRGB(outData.getPixel(x, y));
assertEquals(expected, actual);

                byte actualAlpha = outData.alphaData[y * outData.width + x];
                assertEquals(expectedAlpha, actualAlpha);
}
}
}







