//<Beginning of snippet n. 1>

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

import java.awt.image.BufferedImage;

/**
* The {@link ImageOverlay} class renders an image as an overlay.
*/
public class ImageOverlay extends Overlay {
/** Current background image. Null when there's no image. */
private Image mImage;

/** The associated {@link LayoutCanvas}. */
private LayoutCanvas mCanvas;
/** Horizontal scaling & scrollbar information. */
private CanvasTransform mHScale;

/**
* Constructs an {@link ImageOverlay} tied to the given canvas.
*
* @return The corresponding SWT image, or null.
*/
public Image setImage(BufferedImage awtImage) {
    try {
        if (mImage != null) {
            mImage.dispose();
        }
        if (awtImage == null) {
            mImage = null;
        } else {
            mImage = SwtUtils.convertToSwt(mCanvas.getDisplay(), awtImage, false, -1);
        }
    } catch (SWTException e) {
        // Handle error appropriately to maintain application stability
        mLogger.log(e.getMessage());
        mImage = null;
    }
    return mImage;
}

/**
 * Public method to create a bitmap compatible with SWT.
 *
 * @param width the width of the bitmap
 * @param height the height of the bitmap
 * @return a new SWT-compatible Image
 */
public Image createCompatibleBitmap(int width, int height) {
    return new Image(mCanvas.getDisplay(), width, height);
}

// Additional methods to enhance parameter validation
public void setCustomBackgroundColor(int color) {
    if ((color & 0xFF000000) == 0) {
        throw new IllegalArgumentException("Color must not be fully transparent");
    }
    mCustomBackgroundColor = color;
}

}

//<End of snippet n. 1>