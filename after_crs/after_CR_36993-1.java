/*Use soft references for the AWT image cache

The layout editor stores both an SWT image (of the most recently
rendered scene), as well as an AWT image which is kept around such
that it can be used over again and over again as the scratch buffer
for layout lib. However, if you're opening a lot of editors, there's a
lot of these AWT buffers sitting around and most of the time not
getting used. In a low memory situation they should be able to be
freed up.

To allow for this, the AWT image buffers are now held only by a soft
(not weak) reference. If the soft reference is cleared by the garbage
collector, then a new AWT image buffer will be allocated when needed
for layoutlib.

There is one wrinkle in all this: On Linux, we prescale images
(because scaling while painting is very slow -- see issue 19447). This
means we need to keep the AWT image around. Thus, there's a second
field which is strong reference to the AWT image, which we set if
prescaling is enabled.

Change-Id:Ifb64e476c29194f0e99fec023612f9dc880dd6f4*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java
//Synthetic comment -- index a5df4bd..c402b2b 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.lang.ref.SoftReference;

/**
* The {@link ImageOverlay} class renders an image as an overlay.
//Synthetic comment -- @@ -56,7 +57,17 @@

/** Current background AWT image. This is created by {@link #getImage()}, which is called
* by the LayoutLib. */
    private SoftReference<BufferedImage> mAwtImage = new SoftReference<BufferedImage>(null);

    /**
     * Strong reference to the image in the above soft reference, to prevent
     * garbage collection when {@link PRESCALE} is set, until the scaled image
     * is created (lazily as part of the next paint call, where this strong
     * reference is nulled out and the above soft reference becomes eligible to
     * be reclaimed when memory is low.)
     */
    @SuppressWarnings("unused") // Used by the garbage collector to keep mAwtImage non-soft
    private BufferedImage mAwtImageStrongRef;

/** The associated {@link LayoutCanvas}. */
private LayoutCanvas mCanvas;
//Synthetic comment -- @@ -109,8 +120,10 @@
* @return The corresponding SWT image, or null.
*/
public synchronized Image setImage(BufferedImage awtImage, boolean isAlphaChannelImage) {
        BufferedImage oldAwtImage = mAwtImage.get();
        if (awtImage != oldAwtImage || awtImage == null) {
            mAwtImage.clear();
            mAwtImageStrongRef = null;

if (mImage != null) {
mImage.dispose();
//Synthetic comment -- @@ -165,7 +178,12 @@
*/
@Nullable
BufferedImage getAwtImage() {
        BufferedImage awtImage = mAwtImage.get();
        if (awtImage == null && mImage != null) {
            awtImage = SwtUtils.convertToAwt(mImage);
        }

        return awtImage;
}

@Override
//Synthetic comment -- @@ -184,11 +202,12 @@
// This is done lazily in paint rather than when the image changes because
// the image must be rescaled each time the zoom level changes, which varies
// independently from when the image changes.
            BufferedImage awtImage = mAwtImage.get();
            if (PRESCALE && awtImage != null) {
if (mPreScaledImage == null ||
mPreScaledImage.getImageData().width != hi.getScalledImgSize()) {
                    double xScale = hi.getScalledImgSize() / (double) awtImage.getWidth();
                    double yScale = vi.getScalledImgSize() / (double) awtImage.getHeight();
BufferedImage scaledAwtImage;

// NOTE: == comparison on floating point numbers is okay
//Synthetic comment -- @@ -198,9 +217,9 @@
// of rounding errors.
if (xScale == 1.0 && yScale == 1.0) {
// Scaling to 100% is easy!
                        scaledAwtImage = awtImage;
} else {
                        scaledAwtImage = ImageUtils.scale(awtImage, xScale, yScale);
}
assert scaledAwtImage.getWidth() == hi.getScalledImgSize();
if (mPreScaledImage != null && !mPreScaledImage.isDisposed()) {
//Synthetic comment -- @@ -208,6 +227,8 @@
}
mPreScaledImage = SwtUtils.convertToSwt(mCanvas.getDisplay(), scaledAwtImage,
true /*transferAlpha*/, -1);
                    // We can't just clear the mAwtImageStrongRef here, because if the
                    // zooming factor changes, we may need to use it again
}

if (mPreScaledImage != null) {
//Synthetic comment -- @@ -349,14 +370,19 @@
*/
@Override
public BufferedImage getImage(int w, int h) {
        BufferedImage awtImage = mAwtImage.get();
        if (awtImage == null ||
                awtImage.getWidth() != w ||
                awtImage.getHeight() != h) {
            mAwtImage.clear();
            awtImage = SwtReadyBufferedImage.createImage(w, h, getDevice());
            mAwtImage = new SoftReference<BufferedImage>(awtImage);
            if (PRESCALE) {
                mAwtImageStrongRef = awtImage;
            }
}

        return awtImage;
}

/**







