
//<Beginning of snippet n. 0>


import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
continue;
}

            Item item = new Item(group, desc);
toggle.addItem(item);
GridDataBuilder.create(item).hFill().hGrab();
}
* An Item widget represents one {@link ElementDescriptor} that can be dropped on the
* GLE2 canvas using drag'n'drop.
*/
    private class Item extends CLabel implements MouseTrackListener {

private boolean mMouseIn;
private DragSource mSource;
        private final ViewElementDescriptor mDesc;

        public Item(Composite parent, ViewElementDescriptor desc) {
super(parent, SWT.NONE);
mDesc = desc;
mMouseIn = false;

// DND Reference: http://www.eclipse.org/articles/Article-SWT-DND/DND-in-SWT.html
mSource = new DragSource(this, DND.DROP_COPY);
mSource.setTransfer(new Transfer[] { SimpleXmlTransfer.getInstance() });
            mSource.addDragListener(new DescDragSourceListener(mDesc));
}

@Override
public void mouseHover(MouseEvent e) {
// pass
}
}

/**
* A {@link DragSourceListener} that deals with drag'n'drop of
* {@link ElementDescriptor}s.
*/
    private class DescDragSourceListener implements DragSourceListener {
        private final ViewElementDescriptor mDesc;
private SimpleElement[] mElements;

        public DescDragSourceListener(ViewElementDescriptor desc) {
            mDesc = desc;
}

public void dragStart(DragSourceEvent e) {
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
                    SimpleXmlTransfer.getFqcn(mDesc),
null   /* parentFqcn */,
bounds /* bounds */,
null   /* parentBounds */);
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
/** Amount of alpha to multiply into the image (divided by 256) */
private static final int IMG_ALPHA = 216;

/** The image shown by the drag source effect */
private Image mImage;

*/
private boolean mIsPlaceholder;

        private void createDragImage(DragSourceEvent event) {
mImage = renderPreview();

mIsPlaceholder = mImage == null;
// example the preview of an empty layout), so instead create a placeholder
// image
// Render the palette item itself as an image
                Control control = PaletteControl.this;
GC gc = new GC(control);
Point size = control.getSize();
                Display display = getDisplay();
                final Image image = new Image(display, size.x, size.y);
gc.copyArea(image, 0, 0);
gc.dispose();

BufferedImage awtImage = SwtUtils.convertToAwt(image);
if (awtImage != null) {
awtImage = ImageUtils.createDropShadow(awtImage, 3 /* shadowSize */,
event.offsetY = imageBounds.height / 2;

}
}

/** Performs the actual rendering of the descriptor into an image */
}

// Insert our target view's XML into it as a node
            GraphicalEditorPart editor = getEditor();
LayoutEditor layoutEditor = editor.getLayoutEditor();

            String viewName = mDesc.getXmlLocalName();
Element element = document.createElement(viewName);

// Set up a proper name space

// This doesn't apply to all, but doesn't seem to cause harm and makes for a
// better experience with text-oriented views like buttons and texts
            element.setAttributeNS(SdkConstants.NS_RESOURCES, ATTR_TEXT, mDesc.getUiName());

document.appendChild(element);

!hasTransparency ? 0.6f : needsContrast ? 0.8f : 0.7f/*alpha*/,
0x000000 /* shadowRgb */);

                            Display display = getDisplay();
int alpha = (!hasTransparency || !needsContrast) ? IMG_ALPHA : -1;
Image swtImage = SwtUtils.convertToSwt(display, cropped, true, alpha);
return swtImage;
* as commented out code the code won't be accidentally broken.
*/
@SuppressWarnings("all")
        private void dumpDocument(Document document) {
// Diagnostics: print out the XML that we're about to render
if (false) { // Will be omitted by the compiler
org.apache.xml.serialize.OutputFormat outputFormat =
}
}
}
}
}

//<End of snippet n. 0>








