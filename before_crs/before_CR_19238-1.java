/*ADT/Layoutlib: New API to let the caller instantiate the bitmap.

This allows us to use a bitmap more compatible with SWT.

Change-Id:I0062a4f4442ff6469cf0ad4f501c1fbe8c719400*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 8cb13e6..1e51994 100755

//Synthetic comment -- @@ -1436,12 +1436,17 @@
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








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java
//Synthetic comment -- index b920607..167a6ed 100644

//Synthetic comment -- @@ -16,20 +16,34 @@

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
//Synthetic comment -- @@ -40,6 +54,7 @@
/** Horizontal scaling & scrollbar information. */
private ScaleInfo mHScale;

/**
* Constructs an {@link ImageOverlay} tied to the given canvas.
*
//Synthetic comment -- @@ -77,14 +92,18 @@
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
//Synthetic comment -- @@ -180,4 +199,35 @@
}
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/Overlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/Overlay.java
//Synthetic comment -- index ac96d76..432d074 100644

//Synthetic comment -- @@ -26,6 +26,8 @@
* {@link MoveGesture}.
*/
public abstract class Overlay {
/**
* Construct the overlay, using the given graphics context for painting.
*/
//Synthetic comment -- @@ -41,6 +43,7 @@
*            to {@link #paint} will correspond to this device.
*/
public void create(Device device) {
}

/**
//Synthetic comment -- @@ -59,4 +62,8 @@
throw new IllegalArgumentException("paint() not implemented, probably done "
+ "with specialized paint signature");
}
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/IImageFactory.java b/layoutlib_api/src/com/android/layoutlib/api/IImageFactory.java
new file mode 100644
//Synthetic comment -- index 0000000..25ef05d

//Synthetic comment -- @@ -0,0 +1,25 @@








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/SceneParams.java b/layoutlib_api/src/com/android/layoutlib/api/SceneParams.java
//Synthetic comment -- index 0eb9768..1bbd96a 100644

//Synthetic comment -- @@ -64,6 +64,8 @@
private int mCustomBackgroundColor;
private long mTimeout;

/**
*
* @param layoutDescription the {@link IXmlPullParser} letting the LayoutLib Bridge visit the
//Synthetic comment -- @@ -136,6 +138,7 @@
mCustomBackgroundEnabled = params.mCustomBackgroundEnabled;
mCustomBackgroundColor = params.mCustomBackgroundColor;
mTimeout = params.mTimeout;
}

public void setCustomBackgroundColor(int color) {
//Synthetic comment -- @@ -147,6 +150,10 @@
mTimeout = timeout;
}

public IXmlPullParser getLayoutDescription() {
return mLayoutDescription;
}
//Synthetic comment -- @@ -214,4 +221,8 @@
public long getTimeout() {
return mTimeout;
}
}







