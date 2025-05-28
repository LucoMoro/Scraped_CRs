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
        Image previousImage = mImage;
        try {
            if (awtImage == null) {
                mImage = null;
            } else {
                mImage = createBitmap(awtImage.getWidth(), awtImage.getHeight(), SWT.IMAGE_RGB);
                if (mImage == null) {
                    throw new RuntimeException("Failed to convert AWT image to SWT image.");
                }
                SwtUtils.convertToSwt(mCanvas.getDisplay(), awtImage, false, -1);
            }
        } catch (Exception e) {
            // Enhanced error handling
            String errorMessage = "Error setting image: " + e.getMessage();
            System.err.println(errorMessage);
            throw new RuntimeException(errorMessage, e);
        } finally {
            if (previousImage != null) {
                previousImage.dispose();
            }
        }
        return mImage;
    }

    public Image createBitmap(int width, int height, int pixelFormat) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive.");
        }

        try {
            return new Image(mCanvas.getDisplay(), width, height, pixelFormat);
        } catch (SWTException e) {
            // Enhanced error handling
            String errorMessage = "Failed to create bitmap: " + e.getMessage();
            System.err.println(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }
}

//<End of snippet n. 1>