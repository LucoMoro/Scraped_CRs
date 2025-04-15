/*Fix SwtUtils conversion for no-alpha case.

Change-Id:I275b526670ca6e60cfc4f8749631dff2ef240b96*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtils.java
//Synthetic comment -- index c2c46f1..88f91a0 100644

//Synthetic comment -- @@ -31,13 +31,41 @@
* Various generic SWT utilities such as image conversion.
*/
public class SwtUtils {

private SwtUtils() {
}

/**
     * Returns the {@link PaletteData} describing the ARGB ordering expected from
     * integers representing pixels for AWT {@link BufferedImage}.
     *
     * @return A new {@link PaletteData} suitable for AWT images.
     */
    public static PaletteData getAwtPaletteData(int imageType) {
        switch (imageType) {
            case BufferedImage.TYPE_INT_RGB:
            case BufferedImage.TYPE_INT_ARGB:
            case BufferedImage.TYPE_INT_ARGB_PRE:
                return new PaletteData(0x00FF0000, 0x0000FF00, 0x000000FF);

            case BufferedImage.TYPE_3BYTE_BGR:
            case BufferedImage.TYPE_4BYTE_ABGR:
            case BufferedImage.TYPE_4BYTE_ABGR_PRE:
                return new PaletteData(0x000000FF, 0x0000FF00, 0x00FF0000);

            default:
                throw new UnsupportedOperationException("RGB type not supported yet.");
        }
    }

    /**
* Converts an AWT {@link BufferedImage} into an equivalent SWT {@link Image}. Whether
* the transparency data is transferred is optional, and this method can also apply an
* alpha adjustment during the conversion.
     * <p/>
     * Implementation details: on Windows, the returned {@link Image} will have an ordering
     * matching the Windows DIB (e.g. RGBA, not ARGB). Callers must make sure to use
     * <code>Image.getImageData().paletteData</code> to get the right pixels out of the image.
*
* @param display The display where the SWT image will be shown
* @param awtImage The AWT {@link BufferedImage}
//Synthetic comment -- @@ -55,8 +83,8 @@
Raster raster = awtImage.getData(new java.awt.Rectangle(width, height));
int[] imageDataBuffer = ((DataBufferInt) raster.getDataBuffer()).getData();

        ImageData imageData =
            new ImageData(width, height, 32, getAwtPaletteData(awtImage.getType()));

imageData.setPixels(0, 0, imageDataBuffer.length, imageDataBuffer, 0);

//Synthetic comment -- @@ -83,8 +111,7 @@
imageData.alpha = globalAlpha;
}

        return new Image(display, imageData);
}

/**
//Synthetic comment -- @@ -96,14 +123,41 @@
* @return an AWT image representing the source SWT image
*/
public static BufferedImage convertToAwt(Image swtImage) {
        ImageData swtData = swtImage.getImageData();
        BufferedImage awtImage =
            new BufferedImage(swtData.width, swtData.height, BufferedImage.TYPE_INT_ARGB);
        PaletteData swtPalette = swtData.palette;
        if (swtPalette.isDirect) {
            PaletteData awtPalette = getAwtPaletteData(awtImage.getType());

            if (swtPalette.equals(awtPalette)) {
                // No color conversion needed.
                for (int y = 0; y < swtData.height; y++) {
                    for (int x = 0; x < swtData.width; x++) {
                      int pixel = swtData.getPixel(x, y);
                      awtImage.setRGB(x, y, 0xFF000000 | pixel);
                    }
                }
            } else {
                // We need to remap the colors
                int sr = -awtPalette.redShift   + swtPalette.redShift;
                int sg = -awtPalette.greenShift + swtPalette.greenShift;
                int sb = -awtPalette.blueShift  + swtPalette.blueShift;

                for (int y = 0; y < swtData.height; y++) {
                    for (int x = 0; x < swtData.width; x++) {
                      int pixel = swtData.getPixel(x, y);

                      int r = pixel & swtPalette.redMask;
                      int g = pixel & swtPalette.greenMask;
                      int b = pixel & swtPalette.blueMask;
                      r = (sr < 0) ? r >>> -sr : r << sr;
                      g = (sg < 0) ? g >>> -sg : g << sg;
                      b = (sb < 0) ? b >>> -sb : b << sb;

                      pixel = 0xFF000000 | r | g | b;
                      awtImage.setRGB(x, y, pixel);
                    }
}
}
} else {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtilsTest.java
//Synthetic comment -- index c575f46..6eca0cf 100644

//Synthetic comment -- @@ -20,6 +20,8 @@

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
//Synthetic comment -- @@ -33,36 +35,48 @@
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

        ImageData outData = outImage.getImageData();
        assertEquals(inImage.getWidth(), outData.width);
        assertEquals(inImage.getHeight(), outData.height);
        assertNull(outData.alphaData);

        PaletteData inPalette  = SwtUtils.getAwtPaletteData(inImage.getType());
        PaletteData outPalette = outData.palette;

        for (int y = 0; y < outData.height; y++) {
            for (int x = 0; x < outData.width; x++) {
                // Note: we can't compare pixel directly as integers since convertToSwt() might
                // have changed the RGBA ordering depending on the platform (e.g. it will on
                // Windows.)
                RGB expected = outPalette.getRGB(outData.getPixel(x, y));
                RGB actual   = inPalette.getRGB( inImage.getRGB(  x, y));
                assertEquals(expected, actual);
}
}

// Convert back to AWT and compare with original AWT image
        BufferedImage awtImage = SwtUtils.convertToAwt(outImage);
assertNotNull(awtImage);
        assertEquals(inImage.getType(), awtImage.getType());
        for (int y = 0; y < outData.height; y++) {
            for (int x = 0; x < outData.width; x++) {
                // Note: we can compare pixels as integers since we just
                // asserted both images have the same color image type.
                assertEquals(inImage.getRGB(x, y), awtImage.getRGB(x, y));
}
}
}

public void testImageConvertGlobalAlpha() throws Exception {







