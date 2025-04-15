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
//Synthetic comment -- index 4e5f56a..f497526 100644

//Synthetic comment -- @@ -26,16 +26,8 @@
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;

/**
//Synthetic comment -- @@ -57,11 +49,6 @@
/** Horizontal scaling & scrollbar information. */
private CanvasTransform mHScale;

    /**
     * A lazily instantiated SWT sample model
     */
    private PixelInterleavedSampleModel mSampleModel;


/**
* Constructs an {@link ImageOverlay} tied to the given canvas.
//Synthetic comment -- @@ -99,8 +86,8 @@
* @param awtImage The AWT image to be rendered as an SWT image.
* @return The corresponding SWT image, or null.
*/
    public Image setImage(BufferedImage awtImage) {
        if (awtImage != mAwtImage) {
mAwtImage = null;

if (mImage != null) {
//Synthetic comment -- @@ -113,21 +100,8 @@
mImage = SwtUtils.convertToSwt(mCanvas.getDisplay(), awtImage, false, -1);
}
} else {
            // The image being passed is the one that was created in #getImage(int,int),
            // we can create an SWT image more efficiently.
            WritableRaster awtRaster = mAwtImage.getRaster();
            DataBufferByte byteBuffer = (DataBufferByte) awtRaster.getDataBuffer();
            byte[] data = byteBuffer.getData();

            ImageData imageData = new ImageData(mAwtImage.getWidth(), mAwtImage.getHeight(), 32,
                        new PaletteData(0x00FF0000, 0x0000FF00, 0x000000FF));

            // normally we'd use ImageData.setPixels() but it only accepts int[] for 32 bits image.
            // However from #getImage(int, int), we know the raster data is the same exact format
            // as the SWT image, so we just do a copy.
            System.arraycopy(data, 0, imageData.data, 0, data.length);

            mImage  = new Image(getDevice(), imageData);
}

return mImage;
//Synthetic comment -- @@ -143,7 +117,7 @@
}

@Override
    public void paint(GC gc) {
if (mImage != null) {
boolean valid = mCanvas.getViewHierarchy().isValid();
if (!valid) {
//Synthetic comment -- @@ -223,47 +197,73 @@
}
}

public BufferedImage getImage(int w, int h) {
if (mAwtImage == null ||
mAwtImage.getWidth() != w ||
mAwtImage.getHeight() != h) {

            ImageData imageData =
                new ImageData(w, h, 32, new PaletteData(0x00FF0000, 0x0000FF00, 0x000000FF));
            Image image = new Image(getDevice(), imageData);

            // get the new imageData in case the host OS forced a different format.
            imageData = image.getImageData();

            // create a writable raster around the image data.
            WritableRaster raster = (WritableRaster) Raster.createRaster(
                    getSampleModel(imageData.palette, w, h),
                    new DataBufferByte(imageData.data, imageData.data.length),
                    new Point(0,0));

            ColorModel colorModel = new ComponentColorModel(
                    ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB),
                    true /*hasAlpha*/, true /*isAlphaPremultiplied*/,
                    ColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE);

            mAwtImage = new BufferedImage(colorModel, raster, false, null);
}

return mAwtImage;
}

    private SampleModel getSampleModel(PaletteData palette, int w, int h) {
        if (mSampleModel == null) {
            return mSampleModel = new PixelInterleavedSampleModel(DataBuffer.TYPE_BYTE, w, h,
                    4 /*pixel stride*/, w * 4 /*scanlineStride*/,
                    getBandOffset(palette));
        }

        return mSampleModel.createCompatibleSampleModel(w, h);
    }

    private int[] getBandOffset(PaletteData palette) {
        // FIXME actually figure out the PixelInterleavedSampleModel's band offset from the image data palette.
        return new int[] {3, 2, 1, 0};
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index e05db27..ed42207 100755

//Synthetic comment -- @@ -996,19 +996,20 @@
scene.animate(viewObject, "testanim", false /*isFrameworkAnimation*/,
new IAnimationListener() {
private int mCount = 0;
                                private BufferedImage mImage;
private boolean mPendingDrawing = false;
                                public synchronized void onNewFrame(final BufferedImage image) {
mCount++;
                                    mImage = image;
                                    if (mPendingDrawing == false) {
                                        getDisplay().asyncExec(new Runnable() {
                                            public void run() {
                                                drawImage();
                                            }
                                        });

                                        mPendingDrawing = true;
}
}

//Synthetic comment -- @@ -1026,8 +1027,6 @@
public void drawImage() {
// get last image
synchronized (this) {
                                        mImageOverlay.setImage(mImage);
                                        mImage = null;
mPendingDrawing = false;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SwtUtils.java
//Synthetic comment -- index 854ebf5..29a6059 100644

//Synthetic comment -- @@ -27,7 +27,7 @@

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.lang.reflect.Field;
import java.util.List;

//Synthetic comment -- @@ -85,7 +85,7 @@
int width = awtImage.getWidth();
int height = awtImage.getHeight();

        Raster raster = awtImage.getData(new java.awt.Rectangle(width, height));
int[] imageDataBuffer = ((DataBufferInt) raster.getDataBuffer()).getData();

ImageData imageData =







