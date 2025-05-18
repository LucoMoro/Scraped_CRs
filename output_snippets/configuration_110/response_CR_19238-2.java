//<Beginning of snippet n. 1>

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@link ImageOverlay} class renders an image as an overlay.
 */
public class ImageOverlay extends Overlay {
    /** Current background image. Null when there's no image. */
    private Image mImage;

    /** The associated {@link LayoutCanvas}. */
    private LayoutCanvas mCanvas;

    /** Logger for error handling */
    private static final Logger logger = Logger.getLogger(ImageOverlay.class.getName());

    /** 
     * Constructs an {@link ImageOverlay} tied to the given canvas.
     * 
     * @return The corresponding SWT image, or null.
     */
    public Image setImage(BufferedImage awtImage) {
        Image tempImage = null;
        try {
            if (mImage != null) {
                mImage.dispose();
            }
            if (awtImage == null) {
                mImage = null;
                return null;
            }
            tempImage = SwtUtils.convertToSwt(mCanvas.getDisplay(), awtImage, false, -1);
            mImage = tempImage;
        } catch (SWTException e) {
            logger.log(Level.SEVERE, "Error converting BufferedImage to SWT Image", e);
            mImage = null;
        } finally {
            if (tempImage != null && mImage == null) {
                tempImage.dispose();
            }
        }
        return mImage;
    }

    public static Image createBitmap(Device device, BufferedImage awtImage) {
        // Implementation to create a new SWT Image from a BufferedImage
        return SwtUtils.convertToSwt(device, awtImage, false, -1);
    }
}

//<End of snippet n. 1>