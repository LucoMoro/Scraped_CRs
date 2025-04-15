/*ADT: The image given to layoutlib now shares data with ImageData

We now create a custom BufferedImage that shares a byte[] with an
instance of SWT's ImageData to prevent allocating both.

Change-Id:Id57a3696bd92441271f6bee9681edefd12140b02*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java
//Synthetic comment -- index 4e5f56a..bcf9307 100644

//Synthetic comment -- @@ -57,11 +57,6 @@
/** Horizontal scaling & scrollbar information. */
private CanvasTransform mHScale;


/**
* Constructs an {@link ImageOverlay} tied to the given canvas.
//Synthetic comment -- @@ -99,7 +94,7 @@
* @param awtImage The AWT image to be rendered as an SWT image.
* @return The corresponding SWT image, or null.
*/
    public synchronized Image setImage(BufferedImage awtImage) {
if (awtImage != mAwtImage) {
mAwtImage = null;

//Synthetic comment -- @@ -113,21 +108,8 @@
mImage = SwtUtils.convertToSwt(mCanvas.getDisplay(), awtImage, false, -1);
}
} else {
            assert awtImage instanceof SwtReadyBufferedImage;
            mImage = ((SwtReadyBufferedImage)awtImage).getSwtImage();
}

return mImage;
//Synthetic comment -- @@ -143,7 +125,7 @@
}

@Override
    public synchronized void paint(GC gc) {
if (mImage != null) {
boolean valid = mCanvas.getViewHierarchy().isValid();
if (!valid) {
//Synthetic comment -- @@ -223,14 +205,57 @@
}
}

    /**
     * Custom {@link BufferedImage} class able to convert itself into an SWT {@link Image}
     * efficiently.
     *
     * The type of the {@link BufferedImage} matches the type of the SWT {@link Image} so that
     * the BufferedImage directly writes into an {@link ImageData} buffer.
     *
     */
    private static final class SwtReadyBufferedImage extends BufferedImage {

        /**
         * A lazily instantiated SWT sample model
         */
        private static PixelInterleavedSampleModel sSampleModel;

        private final ImageData mImageData;
        private final Device mDevice;

        /**
         * Creates the image with a given model, raster and SWT {@link ImageData}
         * @param model the color model
         * @param raster the image raster
         * @param imageData the SWT image data.
         * @param device the {@link Device} in which the SWT image will be painted.
         */
        private SwtReadyBufferedImage(ColorModel model, WritableRaster raster,
                ImageData imageData, Device device) {
            super(model, raster, false, null);
            mImageData = imageData;
            mDevice = device;
        }

        /**
         * Returns a new {@link Image} object initialized with the content of the BufferedImage.
         * @return the image object.
         */
        private Image getSwtImage() {
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
ImageData imageData =
new ImageData(w, h, 32, new PaletteData(0x00FF0000, 0x0000FF00, 0x000000FF));
            Image image = new Image(device, imageData);

// get the new imageData in case the host OS forced a different format.
imageData = image.getImageData();
//Synthetic comment -- @@ -246,24 +271,39 @@
true /*hasAlpha*/, true /*isAlphaPremultiplied*/,
ColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE);

            SwtReadyBufferedImage swtReadyImage = new SwtReadyBufferedImage(colorModel, raster,
                    imageData, device);

            return swtReadyImage;
        }

        private static SampleModel getSampleModel(PaletteData palette, int w, int h) {
            if (sSampleModel == null) {
                return sSampleModel = new PixelInterleavedSampleModel(DataBuffer.TYPE_BYTE, w, h,
                        4 /*pixel stride*/, w * 4 /*scanlineStride*/,
                        getBandOffset(palette));
            }

            return sSampleModel.createCompatibleSampleModel(w, h);
        }


        private static int[] getBandOffset(PaletteData palette) {
            // FIXME actually figure out the PixelInterleavedSampleModel's band offset from the image data palette.
            return new int[] {3, 2, 1, 0};
        }

    }

    public BufferedImage getImage(int w, int h) {
        if (mAwtImage == null ||
                mAwtImage.getWidth() != w ||
                mAwtImage.getHeight() != h) {

            mAwtImage = SwtReadyBufferedImage.createImage(w, h, getDevice());
}

return mAwtImage;
}

}







