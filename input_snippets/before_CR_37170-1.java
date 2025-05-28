
//<Beginning of snippet n. 0>


*
* @return The scaled image size in pixels.
*/
    public int getScalledImgSize() {
return (int) (mImgSize * mScale);
}


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


BufferedImage awtImage = mAwtImage.get();
if (PRESCALE && awtImage != null) {
if (mPreScaledImage == null ||
                        mPreScaledImage.getImageData().width != hi.getScalledImgSize()) {
                    double xScale = hi.getScalledImgSize() / (double) awtImage.getWidth();
                    double yScale = vi.getScalledImgSize() / (double) awtImage.getHeight();
BufferedImage scaledAwtImage;

// NOTE: == comparison on floating point numbers is okay
} else {
scaledAwtImage = ImageUtils.scale(awtImage, xScale, yScale);
}
                    assert scaledAwtImage.getWidth() == hi.getScalledImgSize();
if (mPreScaledImage != null && !mPreScaledImage.isDisposed()) {
mPreScaledImage.dispose();
}
vi.getImgSize(),        // srcHeight
hi.translate(0),        // destX
vi.translate(0),        // destY
                    hi.getScalledImgSize(), // destWidth
                    vi.getScalledImgSize());  // destHeight

if (oldAlias != -2) {
gc_setAntialias(gc, oldAlias);

//<End of snippet n. 1>








