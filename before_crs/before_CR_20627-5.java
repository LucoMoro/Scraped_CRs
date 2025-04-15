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
//Synthetic comment -- index dfe61a1..0fd2bbd 100755

//Synthetic comment -- @@ -53,7 +53,6 @@
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEffect;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
//Synthetic comment -- @@ -326,7 +325,7 @@
continue;
}

            Item item = new Item(group, this, desc);
toggle.addItem(item);
GridDataBuilder.create(item).hFill().hGrab();
}
//Synthetic comment -- @@ -452,16 +451,14 @@
* An Item widget represents one {@link ElementDescriptor} that can be dropped on the
* GLE2 canvas using drag'n'drop.
*/
    private static class Item extends CLabel implements MouseTrackListener {

private boolean mMouseIn;
private DragSource mSource;
        private final ElementDescriptor mDesc;
        public PaletteControl mPalette;

        public Item(Composite parent, PaletteControl palette, ElementDescriptor desc) {
super(parent, SWT.NONE);
            mPalette = palette;
mDesc = desc;
mMouseIn = false;

//Synthetic comment -- @@ -473,8 +470,7 @@
// DND Reference: http://www.eclipse.org/articles/Article-SWT-DND/DND-in-SWT.html
mSource = new DragSource(this, DND.DROP_COPY);
mSource.setTransfer(new Transfer[] { SimpleXmlTransfer.getInstance() });
            mSource.addDragListener(new DescDragSourceListener(this));
            mSource.setDragSourceEffect(new PreviewDragSourceEffect(this));
}

@Override
//Synthetic comment -- @@ -512,30 +508,18 @@
public void mouseHover(MouseEvent e) {
// pass
}

        /* package */ ElementDescriptor getDescriptor() {
            return mDesc;
        }

        /* package */ GraphicalEditorPart getEditor() {
            return mPalette.getEditor();
        }

        /* package */ DragSource getDragSource() {
            return mSource;
        }
}

/**
* A {@link DragSourceListener} that deals with drag'n'drop of
* {@link ElementDescriptor}s.
*/
    private static class DescDragSourceListener implements DragSourceListener {
        private final Item mItem;
private SimpleElement[] mElements;

        public DescDragSourceListener(Item item) {
            mItem = item;
}

public void dragStart(DragSourceEvent e) {
//Synthetic comment -- @@ -543,23 +527,19 @@
// Preview images are created before the drag source listener is notified
// of the started drag.
Rect bounds = null;
            DragSource dragSource = mItem.getDragSource();
            DragSourceEffect dragSourceEffect = dragSource.getDragSourceEffect();
Rect dragBounds = null;
            if (dragSourceEffect instanceof PreviewDragSourceEffect) {
                PreviewDragSourceEffect preview = (PreviewDragSourceEffect) dragSourceEffect;
                Image previewImage = preview.getPreviewImage();
                if (previewImage != null && !preview.isPlaceholder()) {
                    ImageData data = previewImage.getImageData();
                    int width = data.width;
                    int height = data.height;
                    bounds = new Rect(0, 0, width, height);
                    dragBounds = new Rect(-width / 2, -height / 2, width, height);
                }
}

SimpleElement se = new SimpleElement(
                    SimpleXmlTransfer.getFqcn(mItem.getDescriptor()),
null   /* parentFqcn */,
bounds /* bounds */,
null   /* parentBounds */);
//Synthetic comment -- @@ -588,14 +568,12 @@
// Unregister the dragged data.
GlobalCanvasDragInfo.getInstance().stopDrag();
mElements = null;
}
    }

    /**
     * A {@link DragSourceEffect} (an image shown under the drag cursor) which renders a
     * preview of the given item.
     */
    private static class PreviewDragSourceEffect extends DragSourceEffect {
// TODO: Figure out the right dimensions to use for rendering.
// We WILL crop this after rendering, but for performance reasons it would be good
// not to make it much larger than necessary since to crop this we rely on
//Synthetic comment -- @@ -617,9 +595,6 @@
/** Amount of alpha to multiply into the image (divided by 256) */
private static final int IMG_ALPHA = 216;

        /** The item this preview is rendering a preview for */
        private final Item mItem;

/** The image shown by the drag source effect */
private Image mImage;

//Synthetic comment -- @@ -629,13 +604,7 @@
*/
private boolean mIsPlaceholder;

        private PreviewDragSourceEffect(Item item) {
            super(item);
            mItem = item;
        }

        @Override
        public void dragStart(DragSourceEvent event) {
mImage = renderPreview();

mIsPlaceholder = mImage == null;
//Synthetic comment -- @@ -644,14 +613,14 @@
// example the preview of an empty layout), so instead create a placeholder
// image
// Render the palette item itself as an image
                Control control = getControl();
GC gc = new GC(control);
Point size = control.getSize();
                final Image image = new Image(mItem.getDisplay(), size.x, size.y);
gc.copyArea(image, 0, 0);
gc.dispose();

                Display display = mItem.getDisplay();
BufferedImage awtImage = SwtUtils.convertToAwt(image);
if (awtImage != null) {
awtImage = ImageUtils.createDropShadow(awtImage, 3 /* shadowSize */,
//Synthetic comment -- @@ -680,23 +649,6 @@
event.offsetY = imageBounds.height / 2;

}

            event.doit = true;
        }

        @Override
        public void dragFinished(DragSourceEvent event) {
            super.dragFinished(event);

            if (mImage != null) {
                mImage.dispose();
                mImage = null;
            }
        }

        @Override
        public void dragSetData(DragSourceEvent event) {
            super.dragSetData(event);
}

/** Performs the actual rendering of the descriptor into an image */
//Synthetic comment -- @@ -714,11 +666,10 @@
}

// Insert our target view's XML into it as a node
            ElementDescriptor desc = mItem.getDescriptor();
            GraphicalEditorPart editor = mItem.getEditor();
LayoutEditor layoutEditor = editor.getLayoutEditor();

            String viewName = desc.getXmlLocalName();
Element element = document.createElement(viewName);

// Set up a proper name space
//Synthetic comment -- @@ -734,7 +685,7 @@

// This doesn't apply to all, but doesn't seem to cause harm and makes for a
// better experience with text-oriented views like buttons and texts
            element.setAttributeNS(SdkConstants.NS_RESOURCES, ATTR_TEXT, desc.getUiName());

document.appendChild(element);

//Synthetic comment -- @@ -842,7 +793,7 @@
!hasTransparency ? 0.6f : needsContrast ? 0.8f : 0.7f/*alpha*/,
0x000000 /* shadowRgb */);

                            Display display = getControl().getDisplay();
int alpha = (!hasTransparency || !needsContrast) ? IMG_ALPHA : -1;
Image swtImage = SwtUtils.convertToSwt(display, cropped, true, alpha);
return swtImage;
//Synthetic comment -- @@ -864,7 +815,7 @@
* as commented out code the code won't be accidentally broken.
*/
@SuppressWarnings("all")
        private static void dumpDocument(Document document) {
// Diagnostics: print out the XML that we're about to render
if (false) { // Will be omitted by the compiler
org.apache.xml.serialize.OutputFormat outputFormat =
//Synthetic comment -- @@ -890,26 +841,5 @@
}
}
}

        /**
         * Returns the image shown as the drag source effect. The image may be a preview
         * of the palette item, or just a placeholder image; {@link #isPlaceholder()} can
         * tell the difference.
         *
         * @return the image shown as preview. May be null (between drags).
         */
        /* package */ Image getPreviewImage() {
            return mImage;
        }

        /**
         * Returns true if the image returned by {@link #getPreviewImage()} is just a
         * placeholder for a real preview, and false if the image is an actual preview.
         *
         * @return true if the preview image is just a placeholder
         */
        /* package */ boolean isPlaceholder() {
            return mIsPlaceholder;
        }
}
}







