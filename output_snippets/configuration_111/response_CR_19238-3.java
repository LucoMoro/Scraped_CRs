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
public Image setImage(byte[] imageData) {
    if (mImage != null) {
        mImage.dispose();
    }
    if (imageData == null) {
        mImage = null;
    } else {
        BufferedImage awtImage = SwtUtils.convertToBufferedImage(imageData);
        if (awtImage != null) {
            mImage = SwtUtils.convertToSwt(mCanvas.getDisplay(), awtImage, false, -1);
        } else {
            throw new IllegalArgumentException("Invalid image data");
        }
    }
    return mImage;
}

public void setCustomBackgroundColor(int color) {
    this.mCustomBackgroundColor = color;
}

private void disposePreviousImage() {
    if (mImage != null && !mImage.isDisposed()) {
        mImage.dispose();
    }
}
}

//<End of snippet n. 1>

//<Beginning of snippet n. 2>

public void drawImage() {
    byte[] imageData;
    synchronized (this) {
        imageData = mImage; // Presuming mImage is now a byte[] type
        mImage = null;
        mPendingDrawing = false;
    }
    mImageOverlay.setImage(imageData);
    redraw();
}

//<End of snippet n. 2>

//<Beginning of snippet n. 3>

/**
* {@link MoveGesture}.
*/
public abstract class Overlay {
/**
* Construct the overlay, using the given graphics context for painting.
*/
public void create(Device device) {
}

/**
* @throws IllegalArgumentException thrown if paint is not implemented correctly
*/
public void paint(GC gc) {
    throw new IllegalArgumentException("paint() not implemented, probably done "
        + "with specialized paint signature");
}
}

//<End of snippet n. 3>

//<Beginning of snippet n. 5>

private int mCustomBackgroundColor;
private long mTimeout;

/**
*
* @param layoutDescription the {@link IXmlPullParser} letting the LayoutLib Bridge visit the
mCustomBackgroundEnabled = params.mCustomBackgroundEnabled;
mCustomBackgroundColor = params.mCustomBackgroundColor;
mTimeout = params.mTimeout;
}

public void setCustomBackgroundColor(int color) {
    mCustomBackgroundColor = color;
}

public IXmlPullParser getLayoutDescription() {
    return mLayoutDescription;
}

public long getTimeout() {
    return mTimeout;
}

//<End of snippet n. 5>