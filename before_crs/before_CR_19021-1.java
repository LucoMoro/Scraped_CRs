/*Fix SwtUtils conversion for no-alpha case.

Change-Id:I275b526670ca6e60cfc4f8749631dff2ef240b96*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtils.java
//Synthetic comment -- index c2c46f1..656046d 100644

//Synthetic comment -- @@ -31,13 +31,41 @@
* Various generic SWT utilities such as image conversion.
*/
public class SwtUtils {
private SwtUtils() {
}

/**
* Converts an AWT {@link BufferedImage} into an equivalent SWT {@link Image}. Whether
* the transparency data is transferred is optional, and this method can also apply an
* alpha adjustment during the conversion.
*
* @param display The display where the SWT image will be shown
* @param awtImage The AWT {@link BufferedImage}
//Synthetic comment -- @@ -55,8 +83,8 @@
Raster raster = awtImage.getData(new java.awt.Rectangle(width, height));
int[] imageDataBuffer = ((DataBufferInt) raster.getDataBuffer()).getData();

        ImageData imageData = new ImageData(width, height, 32, new PaletteData(0x00FF0000,
                0x0000FF00, 0x000000FF));

imageData.setPixels(0, 0, imageDataBuffer.length, imageDataBuffer, 0);

//Synthetic comment -- @@ -83,8 +111,7 @@
imageData.alpha = globalAlpha;
}

        Image image = new Image(display, imageData);
        return image;
}

/**
//Synthetic comment -- @@ -96,14 +123,41 @@
* @return an AWT image representing the source SWT image
*/
public static BufferedImage convertToAwt(Image swtImage) {
        ImageData data = swtImage.getImageData();
        BufferedImage awtImage = new BufferedImage(data.width, data.height, BufferedImage.TYPE_INT_ARGB);
        PaletteData palette = data.palette;
        if (palette.isDirect) {
            for (int y = 0; y < data.height; y++) {
                for (int x = 0; x < data.width; x++) {
                  int pixel = data.getPixel(x, y);
                  awtImage.setRGB(x, y, 0xFF000000 | pixel);
}
}
} else {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtilsTest.java
//Synthetic comment -- index c575f46..c7c4fe0 100644

//Synthetic comment -- @@ -20,6 +20,8 @@

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
//Synthetic comment -- @@ -33,32 +35,39 @@
public class SwtUtilsTest extends TestCase {

public void testImageConvertNoAlpha() throws Exception {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics g = image.getGraphics();
        g.setColor(new Color(0xFF00FF00, true));
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
g.dispose();

Shell shell = new Shell();
Display display = shell.getDisplay();
        Image swtImage = SwtUtils.convertToSwt(display, image, false, -1);
        assertNotNull(swtImage);
        ImageData data = swtImage.getImageData();
        assertEquals(image.getWidth(), data.width);
        assertEquals(image.getHeight(), data.height);
        assertNull(data.alphaData);
        for (int y = 0; y < data.height; y++) {
            for (int x = 0; x < data.width; x++) {
                assertEquals(image.getRGB(x, y) & 0xFFFFFF, data.getPixel(x, y));
}
}

// Convert back to AWT and compare with original AWT image
        BufferedImage awtImage = SwtUtils.convertToAwt(swtImage);
assertNotNull(awtImage);
        for (int y = 0; y < data.height; y++) {
            for (int x = 0; x < data.width; x++) {
                assertEquals(image.getRGB(x, y), awtImage.getRGB(x, y));
}
}








