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
     */
    public ImageOverlay(LayoutCanvas canvas) {
        mCanvas = canvas;
    }

    /**
     * Instantiates a BufferedImage specifically for SWT from byte array data.
     */
    private BufferedImage instantiateBitmap(byte[] data, int width, int height) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        // Logic to convert byte[] to BufferedImage would go here
        return bufferedImage;
    }

    /**
     * Set the image from a BufferedImage.
     *
     * @param awtImage the BufferedImage to convert and set as overlay image
     * @return The corresponding SWT image, or null.
     */
    public Image setImage(BufferedImage awtImage) {
        int width = (awtImage != null) ? awtImage.getWidth() : 0;
        int height = (awtImage != null) ? awtImage.getHeight() : 0;

        if (mImage != null && (mImage.getBounds().width != width || mImage.getBounds().height != height)) {
            mImage.dispose();
            mImage = null;
        }
        
        if (awtImage == null) {
            return null;
        }

        mImage = SwtUtils.convertToSwt(mCanvas.getDisplay(), awtImage, false, -1);
        return mImage;
    }
}

//<End of snippet n. 1>