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
    private static final Logger LOGGER = Logger.getLogger(ImageOverlay.class.getName());
    
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
    public synchronized Image setImage(BufferedImage awtImage) {
        if (awtImage != null && awtImage.getWidth() <= 0 || awtImage.getHeight() <= 0) {
            throw new IllegalArgumentException("BufferedImage must have positive width and height.");
        }
        try {
            if (mImage != null) {
                mImage.dispose();
            }
            mImage = (awtImage == null) ? null : SwtUtils.convertToSwt(mCanvas.getDisplay(), awtImage, false, -1);
        } catch (SWTException e) {
            LOGGER.log(Level.SEVERE, "Error setting image", e);
            mImage = null;
        }
        return mImage;
    }

    public Image createBitmap(int width, int height) {
        return new Image(mCanvas.getDisplay(), width, height);
    }
}

//<End of snippet n. 1>


//<Beginning of snippet n. 4>

private int mCustomBackgroundColor;
private long mTimeout;

/**
 *
 * @param layoutDescription the {@link IXmlPullParser} letting the LayoutLib Bridge visit the
 */
mCustomBackgroundEnabled = params.mCustomBackgroundEnabled;
mCustomBackgroundColor = params.mCustomBackgroundColor;
mTimeout = params.mTimeout;
}

public void setCustomBackgroundColor(int color) {
    if ((color & 0xFF000000) == 0) {
        throw new IllegalArgumentException("Custom background color should not be fully transparent.");
    }
    mCustomBackgroundColor = color;
}

public IXmlPullParser getLayoutDescription() {
    return mLayoutDescription;
}

public long getTimeout() {
    return mTimeout;
}

//<End of snippet n. 4>