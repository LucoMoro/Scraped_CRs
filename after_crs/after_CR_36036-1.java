/*Fix image resource leak

Change-Id:I5aae62f308b16bef6027cc57d880b6a0670b57da*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageOverlay.java
//Synthetic comment -- index 4704791..a5df4bd 100644

//Synthetic comment -- @@ -126,9 +126,17 @@
assert awtImage instanceof SwtReadyBufferedImage;

if (isAlphaChannelImage) {
                if (mImage != null) {
                    mImage.dispose();
                }

mImage = SwtUtils.convertToSwt(mCanvas.getDisplay(), awtImage, true, -1);
} else {
                Image prev = mImage;
mImage = ((SwtReadyBufferedImage)awtImage).getSwtImage();
                if (prev != mImage && prev != null) {
                    prev.dispose();
                }
}
}

//Synthetic comment -- @@ -195,6 +203,9 @@
scaledAwtImage = ImageUtils.scale(mAwtImage, xScale, yScale);
}
assert scaledAwtImage.getWidth() == hi.getScalledImgSize();
                    if (mPreScaledImage != null && !mPreScaledImage.isDisposed()) {
                        mPreScaledImage.dispose();
                    }
mPreScaledImage = SwtUtils.convertToSwt(mCanvas.getDisplay(), scaledAwtImage,
true /*transferAlpha*/, -1);
}







