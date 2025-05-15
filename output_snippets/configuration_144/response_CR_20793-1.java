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

private float currentZoomLevel; // Assuming this is set elsewhere based on canvas zoom

private void createDragImage(Event e) {
    if (mImage != null && !mIsPlaceholder) {
        ImageData data = mImage.getImageData();
        int width = (int) (data.width * currentZoomLevel);
        int height = (int) (data.height * currentZoomLevel);
        bounds = new Rect(0, 0, width, height);
        dragBounds = new Rect(-width / 2, -height / 2, width, height);
    }

    SimpleElement se = new SimpleElement(
        !hasTransparency ? 0.6f : needsContrast ? 0.8f : 0.7f,        
        0x000000 // shadowRgb
    );

    Display display = getDisplay();
    int alpha = (!hasTransparency || !needsContrast) ? IMG_ALPHA : -1;
    Image swtImage = SwtUtils.convertToSwt(display, cropped, true, alpha);
}

//<End of snippet n. 1>