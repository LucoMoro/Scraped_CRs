/*Fix typo

Change-Id:I92386ed8f6e8c2c17c3dc2803f4ac937cf3616c6*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasTransform.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasTransform.java
//Synthetic comment -- index 7cbb8f2..e367f9e 100644

//Synthetic comment -- @@ -104,7 +104,7 @@
*
* @return The scaled image size in pixels.
*/
    public int getScalledImgSize() {
return (int) (mImgSize * mScale);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java
//Synthetic comment -- index c402b2b..d079ff4 100644

//Synthetic comment -- @@ -205,9 +205,9 @@
BufferedImage awtImage = mAwtImage.get();
if (PRESCALE && awtImage != null) {
if (mPreScaledImage == null ||
                        mPreScaledImage.getImageData().width != hi.getScalledImgSize()) {
                    double xScale = hi.getScalledImgSize() / (double) awtImage.getWidth();
                    double yScale = vi.getScalledImgSize() / (double) awtImage.getHeight();
BufferedImage scaledAwtImage;

// NOTE: == comparison on floating point numbers is okay
//Synthetic comment -- @@ -221,7 +221,7 @@
} else {
scaledAwtImage = ImageUtils.scale(awtImage, xScale, yScale);
}
                    assert scaledAwtImage.getWidth() == hi.getScalledImgSize();
if (mPreScaledImage != null && !mPreScaledImage.isDisposed()) {
mPreScaledImage.dispose();
}
//Synthetic comment -- @@ -251,8 +251,8 @@
vi.getImgSize(),        // srcHeight
hi.translate(0),        // destX
vi.translate(0),        // destY
                    hi.getScalledImgSize(), // destWidth
                    vi.getScalledImgSize());  // destHeight

if (oldAlias != -2) {
gc_setAntialias(gc, oldAlias);







