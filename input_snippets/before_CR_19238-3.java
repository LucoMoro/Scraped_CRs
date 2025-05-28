
//<Beginning of snippet n. 0>


theme, isProjectTheme,
configuredProjectRes, frameworkResources, mProjectCallback,
mLogger);
if (transparentBackground) {
// It doesn't matter what the background color is as long as the alpha
// is 0 (fully transparent). We're using red to make it more obvious if
// for some reason the background is painted when it shouldn't be.
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
private CanvasTransform mHScale;

/**
* Constructs an {@link ImageOverlay} tied to the given canvas.
*
* @param canvas The {@link LayoutCanvas} to paint the overlay over.
* @return The corresponding SWT image, or null.
*/
public Image setImage(BufferedImage awtImage) {
        if (mImage != null) {
            mImage.dispose();
        }
        if (awtImage == null) {
            mImage = null;

} else {
            mImage = SwtUtils.convertToSwt(mCanvas.getDisplay(), awtImage, false, -1);
}

return mImage;
}
}

}

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


*/
public void drawImage() {
// get last image
                                    BufferedImage image;
synchronized (this) {
                                        image = mImage;
mImage = null;
mPendingDrawing = false;
}

                                    mImageOverlay.setImage(image);
redraw();
}
});

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


* {@link MoveGesture}.
*/
public abstract class Overlay {
/**
* Construct the overlay, using the given graphics context for painting.
*/
*            to {@link #paint} will correspond to this device.
*/
public void create(Device device) {
}

/**
throw new IllegalArgumentException("paint() not implemented, probably done "
+ "with specialized paint signature");
}
}

//<End of snippet n. 3>










//<Beginning of snippet n. 4>

new file mode 100644


//<End of snippet n. 4>










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
mTimeout = timeout;
}

public IXmlPullParser getLayoutDescription() {
return mLayoutDescription;
}
public long getTimeout() {
return mTimeout;
}
}

//<End of snippet n. 5>








