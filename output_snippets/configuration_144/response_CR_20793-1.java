//<Beginning of snippet n. 0>
import org.eclipse.swt.graphics.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class DragImageHandler {
    private float currentZoomLevel = 1.0f;

    public void updateZoomLevel(float zoomLevel) {
        this.currentZoomLevel = zoomLevel;
    }

    protected float getCurrentZoomLevel() {
        return currentZoomLevel;
    }

    public void createDragImage(Event e) {
        if (mImage != null && !mIsPlaceholder) {
            ImageData data = mImage.getImageData();
            if (data != null) {
                int width = (int) (data.width * getCurrentZoomLevel());
                int height = (int) (data.height * getCurrentZoomLevel());
                bounds = new Rect(0, 0, width, height);
                dragBounds = new Rect(-width / 2, -height / 2, width, height);
            }
        }
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
    private DragImageHandler dragImageHandler;

    public DragImageCreator(DragImageHandler dragImageHandler) {
        this.dragImageHandler = dragImageHandler;
    }

    public void createDragImage(Event e) {
        if (mImage != null && !mIsPlaceholder) {
            ImageData data = mImage.getImageData();
            if (data != null) {
                int width = (int) (data.width * dragImageHandler.getCurrentZoomLevel());
                int height = (int) (data.height * dragImageHandler.getCurrentZoomLevel());
                bounds = new Rect(0, 0, width, height);
                dragBounds = new Rect(-width / 2, -height / 2, width, height);
            }
        }

        SimpleElement se = new SimpleElement(
                !hasTransparency ? 0.6f : needsContrast ? 0.8f : 0.7f,
                0x000000);

        Display display = getDisplay();
        int alpha = (!hasTransparency || !needsContrast) ? IMG_ALPHA : -1;
        Image swtImage = SwtUtils.convertToSwt(display, cropped, true, alpha);
    }
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
import static org.junit.Assert.assertEquals;

public class ImageUtilsTest {
    public void testSubImage() throws Exception {
        // Test assertions
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
}
//<End of snippet n. 2>