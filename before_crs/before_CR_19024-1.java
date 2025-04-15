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

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
//Synthetic comment -- @@ -35,15 +36,18 @@
public class SwtUtilsTest extends TestCase {

public void testImageConvertNoAlpha() throws Exception {
        BufferedImage inImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB_PRE);
Graphics g = inImage.getGraphics();
        g.setColor(new Color(0xFF00FF00, true));  // green
g.fillRect(0, 0, inImage.getWidth(), inImage.getHeight());
g.dispose();

Shell shell = new Shell();
Display display = shell.getDisplay();

Image outImage = SwtUtils.convertToSwt(display, inImage, false, -1);
assertNotNull(outImage);

//Synthetic comment -- @@ -51,6 +55,7 @@
assertEquals(inImage.getWidth(), outData.width);
assertEquals(inImage.getHeight(), outData.height);
assertNull(outData.alphaData);

PaletteData inPalette  = SwtUtils.getAwtPaletteData(inImage.getType());
PaletteData outPalette = outData.palette;
//Synthetic comment -- @@ -60,8 +65,8 @@
// Note: we can't compare pixel directly as integers since convertToSwt() might
// have changed the RGBA ordering depending on the platform (e.g. it will on
// Windows.)
                RGB expected = outPalette.getRGB(outData.getPixel(x, y));
                RGB actual   = inPalette.getRGB( inImage.getRGB(  x, y));
assertEquals(expected, actual);
}
}
//Synthetic comment -- @@ -70,87 +75,123 @@
BufferedImage awtImage = SwtUtils.convertToAwt(outImage);
assertNotNull(awtImage);

        // Both image have compatible RGB orderings
        assertEquals(BufferedImage.TYPE_INT_ARGB_PRE, inImage.getType());
        assertEquals(BufferedImage.TYPE_INT_ARGB,     awtImage.getType());

for (int y = 0; y < outData.height; y++) {
for (int x = 0; x < outData.width; x++) {
// Note: we can compare pixels as integers since we just
                // asserted both images have the same color image type.
                assertEquals(inImage.getRGB(x, y), awtImage.getRGB(x, y));
}
}
}

public void testImageConvertGlobalAlpha() throws Exception {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics g = image.getGraphics();
        g.setColor(new Color(0xFF00FF00, true));
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
g.dispose();

Shell shell = new Shell();
Display display = shell.getDisplay();
        Image swtImage = SwtUtils.convertToSwt(display, image, false, 128);
        assertNotNull(swtImage);
        ImageData data = swtImage.getImageData();
        assertEquals(image.getWidth(), data.width);
        assertEquals(image.getHeight(), data.height);
        assertNull(data.alphaData);
        assertEquals(128, data.alpha);
        for (int y = 0; y < data.height; y++) {
            for (int x = 0; x < data.width; x++) {
                assertEquals(image.getRGB(x, y) & 0xFFFFFF, data.getPixel(x, y));
}
}
}

public void testImageConvertAlpha() throws Exception {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics g = image.getGraphics();
        g.setColor(new Color(0xFF00FF00, true));
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
g.dispose();

Shell shell = new Shell();
Display display = shell.getDisplay();
        Image swtImage = SwtUtils.convertToSwt(display, image, true, -1);
        assertNotNull(swtImage);
        ImageData data = swtImage.getImageData();
        assertEquals(image.getWidth(), data.width);
        assertEquals(image.getHeight(), data.height);
        for (int y = 0; y < data.height; y++) {
            for (int x = 0; x < data.width; x++) {
                assertEquals(image.getRGB(x, y) & 0xFFFFFF, data.getPixel(x, y));
                // Note: >> instead of >>> since we will compare with byte (a signed
                // number)
                assertEquals(image.getRGB(x, y) >> 24, data.alphaData[y * data.width + x]);
}
}
}

public void testImageConvertAlphaMultiplied() throws Exception {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics g = image.getGraphics();
        g.setColor(new Color(0xFF00FF00, true));
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
g.dispose();

Shell shell = new Shell();
Display display = shell.getDisplay();
        Image swtImage = SwtUtils.convertToSwt(display, image, true, 32);
        assertNotNull(swtImage);
        ImageData data = swtImage.getImageData();
        assertEquals(image.getWidth(), data.width);
        assertEquals(image.getHeight(), data.height);
        for (int y = 0; y < data.height; y++) {
            for (int x = 0; x < data.width; x++) {
                assertEquals(image.getRGB(x, y) & 0xFFFFFF, data.getPixel(x, y));
                int expectedAlpha = (image.getRGB(x,y) >>> 24);
                byte expected = (byte)(expectedAlpha / 8);
                byte actual = data.alphaData[y * data.width + x];
assertEquals(expected, actual);
}
}
}







