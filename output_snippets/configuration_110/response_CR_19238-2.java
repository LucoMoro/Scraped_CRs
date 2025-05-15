//<Beginning of snippet n. 0>


theme, isProjectTheme,
configuredProjectRes, frameworkResources, mProjectCallback,
mLogger);
if (transparentBackground) {
    params.setCustomBackgroundColor(0x00FF0000);
}
LayoutScene scene = layoutLib.getBridge().createScene(params);

return scene;

//<End of snippet n. 0>

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

    /**
     * Constructs an {@link ImageOverlay} tied to the given canvas.
     *
     * @return The corresponding SWT image, or null.
     */
    public Image setImage(BufferedImage awtImage) {
        if (mImage != null) {
            mImage.dispose();
        }
        if (awtImage == null) {
            mImage = null;
        } else {
            mImage = createSwtImage(awtImage);
        }
        return mImage;
    }

    private Image createSwtImage(BufferedImage awtImage) {
        return SwtUtils.convertToSwt(mCanvas.getDisplay(), awtImage, false, -1);
    }
}

//<End of snippet n. 1>

//<Beginning of snippet n. 2>

public abstract class Overlay {
    /**
     * Construct the overlay, using the given graphics context for painting.
     */
    public void create(Device device) {
    }

    public void paint(GC gc) {
        throw new IllegalArgumentException("paint() not implemented, probably done "
                + "with specialized paint signature");
    }
}

//<End of snippet n. 2>

//<Beginning of snippet n. 4>

private int mCustomBackgroundColor;
private long mTimeout;

/**
 *
 * @param layoutDescription the {@link IXmlPullParser} letting the LayoutLib Bridge visit the
 */
public void setCustomBackgroundColor(int color) {
    mCustomBackgroundColor = color;
}

public IXmlPullParser getLayoutDescription() {
    return mLayoutDescription;
}

public long getTimeout() {
    return mTimeout;
}

//<End of snippet n. 4>