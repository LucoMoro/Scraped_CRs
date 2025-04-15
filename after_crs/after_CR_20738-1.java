/*Properly convert the rendered layout images if alpha is needed.

The layoutlib can specify if the image is rendered as a floating
window, and needs alpha.
If it doesn't needed it we do a faster convert discarding the
alpha which will be better when playing animations in the normal
case.

Change-Id:I2dbd2d1ae9190207b51978e4a8d77cdff25f3e45*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java
//Synthetic comment -- index 7833570..35d56b6 100644

//Synthetic comment -- @@ -26,6 +26,8 @@
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
//Synthetic comment -- @@ -86,7 +88,7 @@
* @param awtImage The AWT image to be rendered as an SWT image.
* @return The corresponding SWT image, or null.
*/
    public synchronized Image setImage(BufferedImage awtImage, boolean isFloatingWindow) {
if (awtImage != mAwtImage || awtImage == null) {
mAwtImage = null;

//Synthetic comment -- @@ -97,11 +99,16 @@
if (awtImage == null) {
mImage = null;
} else {
                mImage = SwtUtils.convertToSwt(mCanvas.getDisplay(), awtImage, true, -1);
}
} else {
assert awtImage instanceof SwtReadyBufferedImage;

            if (isFloatingWindow) {
                mImage = SwtUtils.convertToSwt(mCanvas.getDisplay(), awtImage, true, -1);
            } else {
                mImage = ((SwtReadyBufferedImage)awtImage).getSwtImage();
            }
}

return mImage;
//Synthetic comment -- @@ -264,6 +271,12 @@
mAwtImage.getHeight() != h) {

mAwtImage = SwtReadyBufferedImage.createImage(w, h, getDevice());

            Graphics2D gc = mAwtImage.createGraphics();
            gc.setColor(new Color(0x00000000, true));
            gc.fillRect(0, 0, w-1, h-1);
            gc.dispose();

}

return mAwtImage;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 8a5e198..7fbeb89 100755

//Synthetic comment -- @@ -520,7 +520,7 @@

mViewHierarchy.setSession(session, explodedNodes);
if (mViewHierarchy.isValid() && session != null) {
            Image image = mImageOverlay.setImage(session.getImage(), session.isFloatingWindow());

mOutlinePage.setModel(mViewHierarchy.getRoot());









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PlayAnimationMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PlayAnimationMenu.java
//Synthetic comment -- index 7d32f78..5ccbb66 100644

//Synthetic comment -- @@ -164,7 +164,7 @@
}

ImageOverlay imageOverlay = mCanvas.getImageOverlay();
                                imageOverlay.setImage(s.getImage(), s.isFloatingWindow());
synchronized (this) {
if (mPendingDrawing == false) {
mCanvas.getDisplay().asyncExec(new Runnable() {








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/RenderSession.java b/layoutlib_api/src/com/android/ide/common/rendering/api/RenderSession.java
//Synthetic comment -- index 0adf6f5..5a00ebc 100644

//Synthetic comment -- @@ -73,6 +73,16 @@
return null;
}

    /**
     * Returns true if the current session is rendered as a floating window.
     * <p/>
     * If true this means the alpha channel of {@link #getImage()} should be respected. If false,
     * if can be dropped if it's more convenient/faster.
     * @return whether the current session is rendered as a floating window.
     */
    public boolean isFloatingWindow() {
        return true;
    }

/**
* Returns a map of (XML attribute name, attribute value) containing only default attribute







