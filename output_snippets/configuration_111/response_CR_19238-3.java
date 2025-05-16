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
    private CanvasTransform mHScale;

    /**
     * Constructs an {@link ImageOverlay} tied to the given canvas.
     *
     * @param canvas The {@link LayoutCanvas} to paint the overlay over.
     * @return The corresponding SWT image, or null.
     */
    public Image setImage(BufferedImage awtImage) {
        if (mImage != null) {
            mImage.dispose();
        }
        if (awtImage == null) {
            mImage = null;
            return null;
        }

        try {
            mImage = SwtUtils.convertToSwt(mCanvas.getDisplay(), awtImage, false, -1);
        } catch (SWTException e) {
            mLogger.error("Failed to convert BufferedImage to SWT Image: " + e.getMessage());
            mImage = null;
        }
        
        return mImage;
    }

    @Override
    public void paint(GC gc) {
        if (mImage != null) {
            gc.drawImage(mImage, 0, 0);
        } else {
            throw new IllegalArgumentException("paint() not implemented, probably done "
                    + "with specialized paint signature");
        }
    }

    // Other methods and constructors
}
//<End of snippet n. 1>