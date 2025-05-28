```
<Beginning of snippet n. 1>
BufferedImage awtImage = mAwtImage.get();
if (PRESCALE && awtImage != null) {
if (mPreScaledImage == null ||
                        mPreScaledImage.getImageData().width != hi.getScaledImgSize()) {
                    double xScale = hi.getScaledImgSize() / (double) awtImage.getWidth();
                    double yScale = vi.getScaledImgSize() / (double) awtImage.getHeight();
BufferedImage scaledAwtImage;

// NOTE: == comparison on floating point numbers is okay
} else {
scaledAwtImage = ImageUtils.scale(awtImage, xScale, yScale);
}
                    assert scaledAwtImage.getWidth() == hi.getScaledImgSize();
if (mPreScaledImage != null && !mPreScaledImage.isDisposed()) {
mPreScaledImage.dispose();
}
vi.getScaledImgSize(),        // srcHeight
hi.translate(0),        // destX
vi.translate(0),        // destY
                    hi.getScaledImgSize(), // destWidth
                    vi.getScaledImgSize());  // destHeight

if (oldAlias != -2) {
gc_setAntialias(gc, oldAlias);
<End of snippet n. 1>