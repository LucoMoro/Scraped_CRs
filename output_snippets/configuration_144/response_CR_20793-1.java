//<Beginning of snippet n. 0>
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class DragPreview {

    private float zoomLevel = 1.0f;

    public void createDragImage(Event e) {
        zoomLevel = getCurrentZoomLevel();

        BufferedImage draggedImage = // Your logic to create the image
        if (draggedImage != null) {
            int width = (int) (draggedImage.getWidth() * zoomLevel);
            int height = (int) (draggedImage.getHeight() * zoomLevel);
            Rectangle bounds = new Rectangle(0, 0, width, height);
            Rectangle dragBounds = new Rectangle(-width / 2, -height / 2, width, height);

            // Logic to draw the drag image with dragBounds
        }
    }

    private float getCurrentZoomLevel() {
        // Implement retrieval of the current zoom level
        return 1.0f; // Replace with actual implementation
    }
}

//<End of snippet n. 0>
//<Beginning of snippet n. 1>
import com.android.ide.common.rendering.api.LayoutLog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

public void createDragImage(Event e) {
    if (mImage != null && !mIsPlaceholder) {
        ImageData data = mImage.getImageData();
        int width = (int) (data.width * zoomLevel);
        int height = (int) (data.height * zoomLevel);

        Rectangle bounds = new Rectangle(0, 0, width, height);
        Rectangle dragBounds = new Rectangle(-width / 2, -height / 2, width, height);

        SimpleElement se = new SimpleElement(
            !hasTransparency ? 0.6f : needsContrast ? 0.8f : 0.7f,
            0x000000
        );

        Display display = getDisplay();
        int alpha = (!hasTransparency || !needsContrast) ? IMG_ALPHA : -1;
        Image swtImage = SwtUtils.convertToSwt(display, cropped, true, alpha);

        // Logic to handle drag image creation and visualization
    }
}

//<End of snippet n. 1>
//<Beginning of snippet n. 2>
public void testSubImage() {
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