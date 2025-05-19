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
public void setImage(byte[] imageData) {
    if (imageData == null) {
        disposeImage();
        return;
    }
    
    try {
        BufferedImage awtImage = createBufferedImage(imageData);
        if (awtImage != null) {
            if (mImage != null && mImage.getBounds().width == awtImage.getWidth() && mImage.getBounds().height == awtImage.getHeight()) {
                // Reuse existing image if dimensions match
                SwtUtils.convertToSwt(mCanvas.getDisplay(), awtImage, false, -1);
            } else {
                disposeImage();
                mImage = SwtUtils.convertToSwt(mCanvas.getDisplay(), awtImage, false, -1);
            }
        }
    } catch (SWTException e) {
        mLogger.log(SWT.ERROR, "Error converting byte array to SWT Image", e);
        disposeImage();
    } catch (Exception e) {
        mLogger.log(SWT.ERROR, "Unexpected error converting byte array to SWT Image", e);
        disposeImage();
    }
}

private BufferedImage createBufferedImage(byte[] imageData) {
    try (ByteArrayInputStream bais = new ByteArrayInputStream(imageData)) {
        BufferedImage img = ImageIO.read(bais);
        if (img == null) {
            throw new IOException("ImageIO returned null for the provided byte array.");
        }
        return img;
    } catch (IOException e) {
        mLogger.log(SWT.ERROR, "Failed to create BufferedImage from byte array", e);
        throw new RuntimeException("Failed to create BufferedImage", e); // Re-throw for higher-level handling
    }
}

private void disposeImage() {
    if (mImage != null) {
        mImage.dispose();
        mImage = null;
    }
}
}
//<End of snippet n. 1>