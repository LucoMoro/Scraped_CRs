/*Fix Palette preview image scaling on Windows

When using palette previews with a scaling factor (e.g. the small or
tiny palette modes), the preview images look bad on Windows.  This
appears to be because SWT on Windows has a different set of defaults
for antialiasing and interpolation.

This checkin turns on antialiased high interpolation for image
painting in the palette (when the palette is scaled) to address the
first issue listed in 15071: Layout editor rendering issues in ADT 10

Change-Id:Ie3c619d9105d9d30b1bbe5fb91c195c0f65fee97*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageControl.java
//Synthetic comment -- index d3349e4..c33c4fe 100644

//Synthetic comment -- @@ -147,14 +147,21 @@
int destWidth = imageWidth;
int destHeight = imageHeight;

if (mScale != 1.0f) {
destWidth = (int) (mScale * destWidth);
destHeight = (int) (mScale * destHeight);
}

gc.drawImage(mImage, 0, 0, imageWidth, imageHeight, rect.x + mLeftMargin, rect.y
+ mTopMargin, destWidth, destHeight);

if (mHoverColor != null && mMouseIn) {
gc.setAlpha(60);
gc.setBackground(mHoverColor);







