/*ADT: The image given to layoutlib now shares data with ImageData

We now create a custom BufferedImage that shares a byte[] with an
instance of SWT's ImageData to prevent allocating both.

Change-Id:Id57a3696bd92441271f6bee9681edefd12140b02*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java
//Synthetic comment -- index 4e5f56a..2e6212f 100644

//Synthetic comment -- @@ -57,11 +57,6 @@
/** Horizontal scaling & scrollbar information. */
private CanvasTransform mHScale;

    /**
     * A lazily instantiated SWT sample model
     */
    private PixelInterleavedSampleModel mSampleModel;


/**
* Constructs an {@link ImageOverlay} tied to the given canvas.
//Synthetic comment -- @@ -99,7 +94,7 @@
* @param awtImage The AWT image to be rendered as an SWT image.
* @return The corresponding SWT image, or null.
*/
    public Image setImage(BufferedImage awtImage) {
if (awtImage != mAwtImage) {
mAwtImage = null;

//Synthetic comment -- @@ -113,21 +108,8 @@
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
//Synthetic comment -- @@ -143,7 +125,7 @@
}

@Override
    public void paint(GC gc) {
if (mImage != null) {
boolean valid = mCanvas.getViewHierarchy().isValid();
if (!valid) {
//Synthetic comment -- @@ -223,14 +205,57 @@
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
//Synthetic comment -- @@ -243,27 +268,63 @@

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
//Synthetic comment -- index e05db27..3776fd7 100755

//Synthetic comment -- @@ -996,11 +996,10 @@
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
//Synthetic comment -- @@ -1026,8 +1025,6 @@
public void drawImage() {
// get last image
synchronized (this) {
                                        mImageOverlay.setImage(mImage);
                                        mImage = null;
mPendingDrawing = false;
}








