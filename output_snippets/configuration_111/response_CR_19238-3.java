//<Beginning of snippet n. 1>
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * The {@link ImageOverlay} class renders an image as an overlay.
 */
public class ImageOverlay extends Overlay {
    /** Current background image. Null when there's no image. */
    private Image mImage;

    /** The associated {@link LayoutCanvas}. */
    private LayoutCanvas mCanvas;
    private CanvasTransform mHScale;

    public BufferedImage createBufferedImage(byte[] imageData) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(imageData));
    }

    /**
     * Constructs an {@link ImageOverlay} tied to the given canvas.
     *
     * @param canvas The {@link LayoutCanvas} to paint the overlay over.
     * @return The corresponding SWT image, or null.
     */
    public Image setImage(byte[] imageData) {
        BufferedImage awtImage = null;
        try {
            awtImage = createBufferedImage(imageData);
            if (mImage != null) {
                mImage.dispose();
            }
            if (awtImage != null) {
                mImage = SwtUtils.convertToSwt(mCanvas.getDisplay(), awtImage, false, -1);
            } else {
                mImage = null;
            }
        } catch (SWTException | IOException e) {
            mLogger.error("Error processing image data", e);
            mImage = null; // Ensure mImage is set to null on error
        } finally {
            if (mImage == null && awtImage != null) {
                mImage = null; // Ensure to consistently dispose
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