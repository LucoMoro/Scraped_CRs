/*ADT: The image given to layoutlib now shares data with ImageData

We now create a custom BufferedImage that also includes a reusable
ImageData to generate SWT images. While there is still the AWT to
SWT conversion, at least most buffers aren't re-allocated every
time during animation. The only new allocation is the one done
in the Image constructor to pass the ImageData content to the native
image object.

This also fixes the tearing issue introduced in the previous
commit by calling ImageOverlay.setImage from the animation
listener callback. This should not be done in the UI thread
runnable as each rendering reuse the same bitmap.
When the animation listener callback returns, the image
will be reused for a new rendering so it's content will
change.

Also fixes the windows rendering by computing the proper
band offset based on the SWT palette Data.

Change-Id:Id57a3696bd92441271f6bee9681edefd12140b02*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java
//Synthetic comment -- index 4e5f56a..3177f32 100644

//Synthetic comment -- @@ -26,16 +26,8 @@
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;

/**
//Synthetic comment -- @@ -57,11 +49,6 @@
/** Horizontal scaling & scrollbar information. */
private CanvasTransform mHScale;


/**
* Constructs an {@link ImageOverlay} tied to the given canvas.
//Synthetic comment -- @@ -99,8 +86,8 @@
* @param awtImage The AWT image to be rendered as an SWT image.
* @return The corresponding SWT image, or null.
*/
    public synchronized Image setImage(BufferedImage awtImage) {
        if (awtImage != mAwtImage || awtImage == null) {
mAwtImage = null;

if (mImage != null) {
//Synthetic comment -- @@ -113,21 +100,8 @@
mImage = SwtUtils.convertToSwt(mCanvas.getDisplay(), awtImage, false, -1);
}
} else {
            assert awtImage instanceof SwtReadyBufferedImage;
            mImage = ((SwtReadyBufferedImage)awtImage).getSwtImage();
}

return mImage;
//Synthetic comment -- @@ -143,7 +117,7 @@
}

@Override
    public synchronized void paint(GC gc) {
if (mImage != null) {
boolean valid = mCanvas.getViewHierarchy().isValid();
if (!valid) {
//Synthetic comment -- @@ -223,47 +197,76 @@
}
}

    /**
     * Custom {@link BufferedImage} class able to convert itself into an SWT {@link Image}
     * efficiently.
     *
     * The BufferedImage also contains an instance of {@link ImageData} that's kept around
     * and used to create new SWT {@link Image} objects in {@link #getSwtImage()}.
     *
     */
    private static final class SwtReadyBufferedImage extends BufferedImage {

        private final ImageData mImageData;
        private final Device mDevice;

        /**
         * Creates the image with a given model, raster and SWT {@link ImageData}
         * @param model the color model
         * @param raster the image raster
         * @param imageData the SWT image data.
         * @param device the {@link Device} in which the SWT image will be painted.
         */
        private SwtReadyBufferedImage(int width, int height, ImageData imageData, Device device) {
            super(width, height, BufferedImage.TYPE_INT_ARGB);
            mImageData = imageData;
            mDevice = device;
        }

        /**
         * Returns a new {@link Image} object initialized with the content of the BufferedImage.
         * @return the image object.
         */
        private Image getSwtImage() {
            // transfer the content of the bufferedImage into the image data.
            WritableRaster raster = getRaster();
            int[] imageDataBuffer = ((DataBufferInt) raster.getDataBuffer()).getData();

            mImageData.setPixels(0, 0, imageDataBuffer.length, imageDataBuffer, 0);

            return new Image(mDevice, mImageData);
        }

        /**
         * Creates a new {@link SwtReadyBufferedImage}.
         * @param w the width of the image
         * @param h the height of the image
         * @param device the device in which the SWT image will be painted
         * @return a new {@link SwtReadyBufferedImage} object
         */
        private static SwtReadyBufferedImage createImage(int w, int h, Device device) {
            ImageData imageData = new ImageData(w, h, 32,
                    new PaletteData(0x00FF0000, 0x0000FF00, 0x000000FF));

            SwtReadyBufferedImage swtReadyImage = new SwtReadyBufferedImage(w, h,
                    imageData, device);

            return swtReadyImage;
        }
    }

    /**
     * Implementation of {@link IImageFactory#getImage(int, int)}.
     */
public BufferedImage getImage(int w, int h) {
if (mAwtImage == null ||
mAwtImage.getWidth() != w ||
mAwtImage.getHeight() != h) {

            mAwtImage = SwtReadyBufferedImage.createImage(w, h, getDevice());
}

return mAwtImage;
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index e05db27..ed42207 100755

//Synthetic comment -- @@ -996,19 +996,20 @@
scene.animate(viewObject, "testanim", false /*isFrameworkAnimation*/,
new IAnimationListener() {
private int mCount = 0;
private boolean mPendingDrawing = false;
                                public void onNewFrame(final BufferedImage image) {
mCount++;
                                    mImageOverlay.setImage(image);
                                    synchronized (this) {
                                        if (mPendingDrawing == false) {
                                            getDisplay().asyncExec(new Runnable() {
                                                public void run() {
                                                    drawImage();
                                                }
                                            });

                                            mPendingDrawing = true;
                                        }
}
}

//Synthetic comment -- @@ -1026,8 +1027,6 @@
public void drawImage() {
// get last image
synchronized (this) {
mPendingDrawing = false;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtils.java
//Synthetic comment -- index 854ebf5..29a6059 100644

//Synthetic comment -- @@ -27,7 +27,7 @@

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.lang.reflect.Field;
import java.util.List;

//Synthetic comment -- @@ -85,7 +85,7 @@
int width = awtImage.getWidth();
int height = awtImage.getHeight();

        WritableRaster raster = awtImage.getRaster();
int[] imageDataBuffer = ((DataBufferInt) raster.getDataBuffer()).getData();

ImageData imageData =







