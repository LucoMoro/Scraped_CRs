//<Beginning of snippet n. 0>


import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;

private static class Item extends CLabel implements MouseTrackListener, DragSourceListener {
    private boolean mMouseIn;
    private DragSource mSource;
    private final ElementDescriptor mDesc;
    public PaletteControl mPalette;
    private Image mImage;
    private boolean mIsPlaceholder;

    public Item(Composite parent, PaletteControl palette, ElementDescriptor desc) {
        super(parent, SWT.NONE);
        mPalette = palette;
        mDesc = desc;
        mMouseIn = false;

        mSource = new DragSource(this, DND.DROP_COPY);
        mSource.setTransfer(new Transfer[]{SimpleXmlTransfer.getInstance()});
        mSource.addDragListener(this);
        mSource.setDragSourceEffect(this);
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

    public void dragStart(DragSourceEvent e) {
        Rect bounds = null;
        Image previewImage = renderPreview();
        mIsPlaceholder = previewImage == null;
        if (previewImage != null) {
            ImageData data = previewImage.getImageData();
            int width = data.width;
            int height = data.height;
            bounds = new Rect(0, 0, width, height);
            e.doit = true;
        }

        SimpleElement se = new SimpleElement(
                SimpleXmlTransfer.getFqcn(mDesc),
                null, /* parentFqcn */
                bounds, /* bounds */
                null /* parentBounds */
        );
        GlobalCanvasDragInfo.getInstance().stopDrag();
    }

    private Image renderPreview() {
        Control control = getControl();
        GC gc = new GC(control);
        Point size = control.getSize();
        final Image image = new Image(getDisplay(), size.x, size.y);
        gc.copyArea(image, 0, 0);
        gc.dispose();

        Display display = getDisplay();
        BufferedImage awtImage = SwtUtils.convertToAwt(image);
        if (awtImage != null) {
            return SwtUtils.convertToSwt(display, awtImage, true, -1);
        }
        return null;
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
        // Handle setting data on drag
    }

    /* package */ Image getPreviewImage() {
        return mImage;
    }

    /* package */ boolean isPlaceholder() {
        return mIsPlaceholder;
    }
}

//<End of snippet n. 0>