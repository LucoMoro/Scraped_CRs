/*Scale drag previews with the screen zoom

If you have zoomed your layout, which is going to be common when
designing for 10" screens, then the drag preview shown when dragging
from the palette is not sized correctly. It assumes a 100% zoom.

This changeset looks at the current canvas zoom and scales the preview
image such that it matches the canvas zoom, so if you're zoomed in
then the preview will be larger, and vice versa.

Change-Id:I471b4d8ae459aad79d50a2b54ecca6bd7d7cf943*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java
//Synthetic comment -- index 94419b3..6432aa4 100644

//Synthetic comment -- @@ -19,8 +19,11 @@

import org.eclipse.swt.graphics.Rectangle;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Iterator;
//Synthetic comment -- @@ -427,4 +430,31 @@
throw new NumberFormatException();
}

    /**
     * Resize the given image
     *
     * @param source the image to be scaled
     * @param xScale x scale
     * @param yScale y scale
     * @return the scaled image
     */
    public static BufferedImage scale(BufferedImage source, double xScale, double yScale) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();
        int destWidth = (int) (xScale * sourceWidth);
        int destHeight = (int) (yScale * sourceHeight);
        BufferedImage scaled = new BufferedImage(destWidth, destHeight, source.getType());
        Graphics2D g2 = scaled.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setColor(new Color(0, true));
        g2.fillRect(0, 0, destWidth, destHeight);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(source, 0, 0, destWidth, destHeight, 0, 0, sourceWidth, sourceHeight, null);
        g2.dispose();

        return scaled;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index fbf7167..56da519 100755

//Synthetic comment -- @@ -27,9 +27,9 @@
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.common.rendering.api.Params.RenderingMode;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
//Synthetic comment -- @@ -567,10 +567,14 @@
createDragImage(e);
if (mImage != null && !mIsPlaceholder) {
ImageData data = mImage.getImageData();
                LayoutCanvas canvas = mEditor.getCanvasControl();
                double scale = canvas.getScale();
                int x = -data.width / 2;
                int y = -data.height / 2;
                int width = (int) (data.width / scale);
                int height = (int) (data.height / scale);
bounds = new Rect(0, 0, width, height);
                dragBounds = new Rect(x, y, width, height);
}

SimpleElement se = new SimpleElement(
//Synthetic comment -- @@ -828,6 +832,11 @@
!hasTransparency ? 0.6f : needsContrast ? 0.8f : 0.7f/*alpha*/,
0x000000 /* shadowRgb */);

                            double scale = canvas.getScale();
                            if (scale != 1L) {
                                cropped = ImageUtils.scale(cropped, scale, scale);
                            }

Display display = getDisplay();
int alpha = (!hasTransparency || !needsContrast) ? IMG_ALPHA : -1;
Image swtImage = SwtUtils.convertToSwt(display, cropped, true, alpha);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtilsTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtilsTest.java
//Synthetic comment -- index 73129f4..f903e74 100644

//Synthetic comment -- @@ -291,11 +291,11 @@
assertEquals(0xFFFF0000, sub.getRGB(0, 0));
assertEquals(0xFFFF0000, sub.getRGB(9, 9));

        sub = ImageUtils.subImage(image, 23, 23, 23 + 5, 23 + 5);
assertEquals(5, sub.getWidth());
assertEquals(5, sub.getHeight());
assertEquals(0xFF00FF00, sub.getRGB(0, 0));
        assertEquals(0xFFFF0000, sub.getRGB(4, 4));
}

public void testGetColor() throws Exception {
//Synthetic comment -- @@ -303,4 +303,29 @@
assertEquals(0xFF000000, ImageUtils.getColor("#000000"));
assertEquals(0xABCDEF91, ImageUtils.getColor("#ABCDEF91"));
}

    public void testScaleImage() throws Exception {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics g = image.getGraphics();
        g.setColor(new Color(0xFF00FF00, true));
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.setColor(new Color(0xFFFF0000, true));
        g.fillRect(25, 25, 50, 50);
        g.dispose();

        BufferedImage scaled = ImageUtils.scale(image, 0.5, 0.5);
        assertEquals(50, scaled.getWidth());
        assertEquals(50, scaled.getHeight());
        assertEquals(0xFF00FF00, scaled.getRGB(0, 0));
        assertEquals(0xFF00FF00, scaled.getRGB(49, 49));
        assertEquals(0xFFFF0000, scaled.getRGB(25, 25));

        scaled = ImageUtils.scale(image, 2.0, 2.0);
        assertEquals(200, scaled.getWidth());
        assertEquals(200, scaled.getHeight());
        assertEquals(0xFF00FF00, scaled.getRGB(0, 0));
        assertEquals(0xFF00FF00, scaled.getRGB(48, 48));
        assertEquals(0xFFFF0000, scaled.getRGB(100, 100));
        assertEquals(0xFF00FF00, scaled.getRGB(199, 199));
    }
}







