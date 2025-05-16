//<Beginning of snippet n. 0>


import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEffect;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.events.MouseTrackListener;

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
        mSource.addDragListener(new CombinedDragSourceListener(this));
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

private static class CombinedDragSourceListener implements DragSourceListener {
    private final Item mItem;
    private Image mImage;
    private boolean mIsPlaceholder;

    public CombinedDragSourceListener(Item item) {
        mItem = item;
    }

    public void dragStart(DragSourceEvent e) {
        mImage = renderPreview();

        mIsPlaceholder = mImage == null;

        if (mImage != null) {
            e.doit = true;
        } else {
            e.doit = false; // Prevent drag if there is no image
        }

        // GlobalCanvasDragInfo.getInstance().stopDrag(); // Ensure to handle the internal state if necessary
    }

    private Image renderPreview() {
        Control control = mItem.getControl();
        GC gc = new GC(control);
        Point size = control.getSize();
        Image image = new Image(mItem.getDisplay(), size.x, size.y);
        gc.copyArea(image, 0, 0);
        gc.dispose();

        return image; // Handle rendering specifics based on your application needs
    }

    @Override
    public void dragFinished(DragSourceEvent event) {
        if (mImage != null) {
            mImage.dispose();
            mImage = null;
        }
    }

    @Override
    public void dragSetData(DragSourceEvent event) {
        // Set the data for the drag operation
    }

    public Image getPreviewImage() {
        return mImage;
    }

    public boolean isPlaceholder() {
        return mIsPlaceholder;
    }
}

//<End of snippet n. 0>