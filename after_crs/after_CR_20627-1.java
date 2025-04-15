/*Combine DescDragSourceListener and PreviewDragSourceEffect

It turns out that a DragSource listener can also set a drag preview
effect, so we don't need to have two separate classes for this; this
was slightly tricky because the drag source needs to get the image
from the preview drag source effect in order to record the right
offsets for the drag.

This changeset rolls the two classes into one, the original drag
source.

Change-Id:I249736c13e49f06886318ea64dc8de40e832d216*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index cdd064a..709ce5b 100755

//Synthetic comment -- @@ -53,7 +53,6 @@
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
//Synthetic comment -- @@ -326,7 +325,7 @@
continue;
}

            Item item = new Item(group, desc);
toggle.addItem(item);
GridDataBuilder.create(item).hFill().hGrab();
}
//Synthetic comment -- @@ -452,16 +451,14 @@
* An Item widget represents one {@link ElementDescriptor} that can be dropped on the
* GLE2 canvas using drag'n'drop.
*/
    private class Item extends CLabel implements MouseTrackListener {

private boolean mMouseIn;
private DragSource mSource;
private final ElementDescriptor mDesc;

        public Item(Composite parent, ElementDescriptor desc) {
super(parent, SWT.NONE);
mDesc = desc;
mMouseIn = false;

//Synthetic comment -- @@ -474,7 +471,6 @@
mSource = new DragSource(this, DND.DROP_COPY);
mSource.setTransfer(new Transfer[] { SimpleXmlTransfer.getInstance() });
mSource.addDragListener(new DescDragSourceListener(this));
}

@Override
//Synthetic comment -- @@ -516,21 +512,13 @@
/* package */ ElementDescriptor getDescriptor() {
return mDesc;
}
}

/**
* A {@link DragSourceListener} that deals with drag'n'drop of
* {@link ElementDescriptor}s.
*/
    private class DescDragSourceListener implements DragSourceListener {
private final Item mItem;
private SimpleElement[] mElements;

//Synthetic comment -- @@ -543,19 +531,15 @@
// Preview images are created before the drag source listener is notified
// of the started drag.
Rect bounds = null;
Rect dragBounds = null;

            createDragImage(e);
            if (mImage != null && !mIsPlaceholder) {
                ImageData data = mImage.getImageData();
                int width = data.width;
                int height = data.height;
                bounds = new Rect(0, 0, width, height);
                dragBounds = new Rect(-width / 2, -height / 2, width, height);
}

SimpleElement se = new SimpleElement(
//Synthetic comment -- @@ -588,14 +572,12 @@
// Unregister the dragged data.
GlobalCanvasDragInfo.getInstance().stopDrag();
mElements = null;
            if (mImage != null) {
                mImage.dispose();
                mImage = null;
            }
}

// TODO: Figure out the right dimensions to use for rendering.
// We WILL crop this after rendering, but for performance reasons it would be good
// not to make it much larger than necessary since to crop this we rely on
//Synthetic comment -- @@ -617,9 +599,6 @@
/** Amount of alpha to multiply into the image (divided by 256) */
private static final int IMG_ALPHA = 216;

/** The image shown by the drag source effect */
private Image mImage;

//Synthetic comment -- @@ -629,13 +608,7 @@
*/
private boolean mIsPlaceholder;

        private void createDragImage(DragSourceEvent event) {
mImage = renderPreview();

mIsPlaceholder = mImage == null;
//Synthetic comment -- @@ -644,7 +617,7 @@
// example the preview of an empty layout), so instead create a placeholder
// image
// Render the palette item itself as an image
                Control control = PaletteControl.this;
GC gc = new GC(control);
Point size = control.getSize();
final Image image = new Image(mItem.getDisplay(), size.x, size.y);
//Synthetic comment -- @@ -680,23 +653,6 @@
event.offsetY = imageBounds.height / 2;

}
}

/** Performs the actual rendering of the descriptor into an image */
//Synthetic comment -- @@ -715,7 +671,7 @@

// Insert our target view's XML into it as a node
ElementDescriptor desc = mItem.getDescriptor();
            GraphicalEditorPart editor = getEditor();
LayoutEditor layoutEditor = editor.getLayoutEditor();

String viewName = desc.getXmlLocalName();
//Synthetic comment -- @@ -835,7 +791,7 @@
!hasTransparency ? 0.6f : needsContrast ? 0.8f : 0.7f/*alpha*/,
0x000000 /* shadowRgb */);

                            Display display = getDisplay();
int alpha = (!hasTransparency || !needsContrast) ? IMG_ALPHA : -1;
Image swtImage = SwtUtils.convertToSwt(display, cropped, true, alpha);
return swtImage;
//Synthetic comment -- @@ -857,7 +813,7 @@
* as commented out code the code won't be accidentally broken.
*/
@SuppressWarnings("all")
        private void dumpDocument(Document document) {
// Diagnostics: print out the XML that we're about to render
if (false) { // Will be omitted by the compiler
org.apache.xml.serialize.OutputFormat outputFormat =
//Synthetic comment -- @@ -883,26 +839,5 @@
}
}
}
}
}







