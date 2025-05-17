//<Beginning of snippet n. 1>

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
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
    private CanvasTransform mHScale;
    private static final Logger LOGGER = Logger.getLogger(ImageOverlay.class.getName());

    /**
     * Constructs an {@link ImageOverlay} tied to the given canvas.
     *
     * @param canvas The {@link LayoutCanvas} to paint the overlay over.
     * @return The corresponding SWT image, or null.
     */
    public Image setImage(BufferedImage awtImage) {
        if (awtImage == null || awtImage.getWidth() <= 0 || awtImage.getHeight() <= 0) {
            LOGGER.warning("Invalid BufferedImage provided: " + (awtImage == null ? "null" : "width=" + awtImage.getWidth() + ", height=" + awtImage.getHeight()));
            mImage = null;
            return null;
        }

        synchronized (this) {
            if (mImage != null && mImage.getWidth() == awtImage.getWidth() && mImage.getHeight() == awtImage.getHeight()) {
                return mImage;
            }
            if (mImage != null) {
                mImage.dispose();
            }
            try {
                mImage = SwtUtils.convertToSwt(mCanvas.getDisplay(), awtImage, false, -1);
            } catch (SWTException e) {
                LOGGER.log(Level.SEVERE, "Failed to set image", e);
                mImage = null;
            }
        }

        return mImage;
    }
    
    public static BufferedImage instantiateBufferedImage(byte[] imageData) throws IllegalArgumentException {
        if (imageData == null || imageData.length == 0) {
            throw new IllegalArgumentException("Invalid image data provided.");
        }
        try {
            return ImageIO.read(new ByteArrayInputStream(imageData));
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to convert byte array to BufferedImage", e);
        }
    }
}

//<End of snippet n. 1>

//<Beginning of snippet n. 2>

public void drawImage() {
    BufferedImage image;
    synchronized (this) {
        image = mImage;
        mImage = null;
        mPendingDrawing = false;
    }

    if (image != null) {
        mImageOverlay.setImage(image);
    }
    redraw();
}

//<End of snippet n. 2>