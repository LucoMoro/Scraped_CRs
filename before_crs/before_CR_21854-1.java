/*Fix bounds computation for drags from palette to canvas

When you drag over the canvas, the potential drop location is shown
along with an outline of the component as it would appear at that
location. This relies on knowing the size of the dragged component.

When dragging from the palette we use the drag preview image. However,
the bounds were a bit too big because we were using the raw size of
the drag image, and it now includes a drop shadow!  This changeset
adjusts this such that we use the original ViewInfo bounds from the
render rather than the usually larger image bounds.

Change-Id:Ic4b870995e17284fbd8a7ed2f4b4a7ec02f4e2c3*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index 78e906c..352fbc8 100755

//Synthetic comment -- @@ -641,13 +641,16 @@

createDragImage(e);
if (mImage != null && !mIsPlaceholder) {
                ImageData data = mImage.getImageData();
LayoutCanvas canvas = mEditor.getCanvasControl();
double scale = canvas.getScale();
                int x = -data.width / 2;
                int y = -data.height / 2;
                int width = (int) (data.width / scale);
                int height = (int) (data.height / scale);
bounds = new Rect(0, 0, width, height);
dragBounds = new Rect(x, y, width, height);
}
//Synthetic comment -- @@ -713,8 +716,10 @@
/** Amount of alpha to multiply into the image (divided by 256) */
private static final int IMG_ALPHA = 216;

        /** The image shown by the drag source effect */
private Image mImage;

/**
* If true, the image is a preview of the view, and if not it is a "fallback"
//Synthetic comment -- @@ -723,7 +728,14 @@
private boolean mIsPlaceholder;

private void createDragImage(DragSourceEvent event) {
            mImage = renderPreview();

mIsPlaceholder = mImage == null;
if (mIsPlaceholder) {
//Synthetic comment -- @@ -769,8 +781,11 @@
}
}

        /** Performs the actual rendering of the descriptor into an image */
        private Image renderPreview() {
ViewMetadataRepository repository = ViewMetadataRepository.get();
RenderMode renderMode = repository.getRenderMode(mDesc.getFullClassName());
if (renderMode == RenderMode.SKIP) {
//Synthetic comment -- @@ -922,6 +937,8 @@
}

if (cropped != null) {
boolean needsContrast = hasTransparency
&& !ImageUtils.containsDarkPixels(cropped);
cropped = ImageUtils.createDropShadow(cropped,
//Synthetic comment -- @@ -937,7 +954,8 @@
Display display = getDisplay();
int alpha = (!hasTransparency || !needsContrast) ? IMG_ALPHA : -1;
Image swtImage = SwtUtils.convertToSwt(display, cropped, true, alpha);
                            return swtImage;
}
}
}







