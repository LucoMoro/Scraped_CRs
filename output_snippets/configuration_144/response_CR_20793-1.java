//<Beginning of snippet n. 0>
import org.eclipse.swt.graphics.Rectangle;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Iterator;

public class CanvasHandler {

    private float currentZoomLevel = 1.0f; // Default zoom level

    public float getCurrentZoomLevel() {
        // Logic to retrieve the current zoom level of the canvas
        return currentZoomLevel;
    }

    public void setCurrentZoomLevel(float zoomLevel) {
        if (zoomLevel <= 0) {
            throw new IllegalArgumentException("Zoom level must be positive.");
        }
        this.currentZoomLevel = zoomLevel;
    }
    
    public void createDragImage(Event e) {
        // Implementation of drag image creation
    }
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

public class DragImageCreator {

    private Image mImage;
    private boolean mIsPlaceholder;

    public void createDragImage(Event e) {
        if (mImage != null && !mIsPlaceholder) {
            ImageData data = mImage.getImageData();
            int width = (int) (data.width * getCurrentZoomLevel());
            int height = (int) (data.height * getCurrentZoomLevel());
            Rect bounds = new Rect(0, 0, width, height);
            Rect dragBounds = new Rect(-width / 2, -height / 2, width, height);
        }
        
        // Additional logic for setting up the drag image...

        SimpleElement se = new SimpleElement(
            !hasTransparency ? 0.6f : needsContrast ? 0.8f : 0.7f/*alpha*/,
            0x000000 /* shadowRgb */
        );

        Display display = getDisplay();
        int alpha = (!hasTransparency || !needsContrast) ? IMG_ALPHA : -1;
        Image swtImage = SwtUtils.convertToSwt(display, cropped, true, alpha);
    }

    // Method to be implemented for getting the zoom level
    private float getCurrentZoomLevel() {
        // Assuming a method to fetch current zoom level from the canvas
        return new CanvasHandler().getCurrentZoomLevel();
    }
}

//<End of snippet n. 1>

//<Beginning of snippet n. 2>
import static org.junit.Assert.assertEquals;

public class ImageUtilsTest {
    
    public void testSubImageAndColor() throws Exception {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        // ... Assume image is populated with colors
        
        BufferedImage sub = ImageUtils.subImage(image, 23, 23, 5, 5);
        assertEquals(5, sub.getWidth());
        assertEquals(5, sub.getHeight());
        assertEquals(0xFF00FF00, sub.getRGB(0, 0));
        assertEquals(0xFFFF0000, sub.getRGB(9, 9));
    }

    public void testGetColor() throws Exception {
        assertEquals(0xFF000000, ImageUtils.getColor("#000000"));
        assertEquals(0xABCDEF91, ImageUtils.getColor("#ABCDEF91"));
    }
}
//<End of snippet n. 2>