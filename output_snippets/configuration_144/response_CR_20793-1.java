//<Beginning of snippet n. 0>

import org.eclipse.swt.graphics.Rectangle;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Iterator;
throw new NumberFormatException();
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.Params.RenderingMode;
import com.android.ide.common.rendering.api.RenderSession;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;

public void createDragImage(MouseEvent e) {
    float zoomLevel = getCanvasZoomLevel(); // Retrieve current zoom level
    createDragImage(e);
    if (mImage != null && !mIsPlaceholder) {
        ImageData data = mImage.getImageData();
        int width = (int) (data.width * zoomLevel); // Scale width based on zoom
        int height = (int) (data.height * zoomLevel); // Scale height based on zoom
        bounds = new Rect(0, 0, width, height);
        dragBounds = new Rect(-width / 2, -height / 2, width, height);
    }

    SimpleElement se = new SimpleElement(
        !hasTransparency ? 0.6f : needsContrast ? 0.8f : 0.7f/*alpha*/,
        0x000000 /* shadowRgb */
    );

    Display display = getDisplay();
    int alpha = (!hasTransparency || !needsContrast) ? IMG_ALPHA : -1;
    Image swtImage = SwtUtils.convertToSwt(display, cropped, true, alpha);
}

//<End of snippet n. 1>

//<Beginning of snippet n. 2>

assertEquals(0xFFFF0000, sub.getRGB(0, 0));
assertEquals(0xFFFF0000, sub.getRGB(9, 9));

sub = ImageUtils.subImage(image, 23, 23, 5, 5);
assertEquals(5, sub.getWidth());
assertEquals(5, sub.getHeight());
assertEquals(0xFF00FF00, sub.getRGB(0, 0));
assertEquals(0xFFFF0000, sub.getRGB(9, 9));
}

public void testGetColor() throws Exception {
assertEquals(0xFF000000, ImageUtils.getColor("#000000"));
assertEquals(0xABCDEF91, ImageUtils.getColor("#ABCDEF91"));
}
//<End of snippet n. 2>