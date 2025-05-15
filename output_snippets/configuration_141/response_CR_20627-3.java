//<Beginning of snippet n. 0>


import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

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

        mSource = new DragSource(this, DND.DROP_COPY);
        mSource.setTransfer(new Transfer[] { SimpleXmlTransfer.getInstance() });
        mSource.addDragListener(new UnifiedDragSourceListener(this));
    }

    @Override
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
 * A {@link DragSourceListener} that handles both drag'n'drop and preview functionalities.
 */
private static class UnifiedDragSourceListener implements DragSourceListener {
    private final Item mItem;
    private SimpleElement[] mElements;
    private Image mImage;
    private boolean mIsPlaceholder;

    public UnifiedDragSourceListener(Item item) {
        mItem = item;
    }

    public void dragStart(DragSourceEvent e) {
        Rect bounds = null;
        DragSource dragSource = mItem.getDragSource();
        Image previewImage = renderPreview();
        
        mIsPlaceholder = previewImage == null;
        if (previewImage != null && !isPlaceholder()) {
            ImageData data = previewImage.getImageData();
            int width = data.width;
            int height = data.height;
            bounds = new Rect(0, 0, width, height);
        }

        SimpleElement se = new SimpleElement(
                SimpleXmlTransfer.getFqcn(mItem.getDescriptor()),
                null, /* parentFqcn */
                bounds, /* bounds */
                null /* parentBounds */
        );
        GlobalCanvasDragInfo.getInstance().stopDrag();
        mElements = null;
    }

    public void dragFinished(DragSourceEvent event) {
        if (mImage != null) {
            mImage.dispose();
            mImage = null;
        }
    }

    public void dragSetData(DragSourceEvent event) {
        // Logic to set data goes here
    }

    private Image renderPreview() {
        Control control = mItem;
        GC gc = new GC(control);
        Point size = control.getSize();
        mImage = new Image(mItem.getDisplay(), size.x, size.y);
        gc.copyArea(mImage, 0, 0);
        gc.dispose();
        return mImage;
    }

    /* package */ Image getPreviewImage() {
        return mImage;
    }

    /* package */ boolean isPlaceholder() {
        return mIsPlaceholder;
    }
}

//<End of snippet n. 0>