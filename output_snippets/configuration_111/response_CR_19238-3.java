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
    
    public Image createBufferedImage(byte[] imageData) {
        // Logic to create a BufferedImage from byte[]
        // This should handle various image formats
        return ImageIO.read(new ByteArrayInputStream(imageData));
    }

    /**
     * Constructs an {@link ImageOverlay} tied to the given canvas.
     *
     * @param canvas The {@link LayoutCanvas} to paint the overlay over.
     * @return The corresponding SWT image, or null.
     */
    public Image setImage(BufferedImage awtImage) {
        Image tempImage = null;
        try {
            if (mImage != null) {
                mImage.dispose();
            }
            if (awtImage != null) {
                tempImage = SwtUtils.convertToSwt(mCanvas.getDisplay(), awtImage, false, -1);
            }
        } catch (SWTException | IOException e) {
            // Log error
            mLogger.error("Error converting BufferedImage to SWT Image", e);
        } finally {
            if (tempImage != null) {
                mImage = tempImage;
            } else {
                mImage = null;
            }
        }
        return mImage;
    }

    public void setCustomBackgroundColor(int color) {
        mCustomBackgroundColor = color;
        if (mImage != null) {
            // Repaint logic with consideration for transparency handling.
            // Ensure that the background color is applied accordingly.
        }
    }

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

    @Override
    protected void finalize() throws Throwable {
        try {
            if (mImage != null) {
                mImage.dispose();
            }
        } finally {
            super.finalize();
        }
    }
}
//<End of snippet n. 1>