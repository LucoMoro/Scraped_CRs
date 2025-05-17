//<Beginning of snippet n. 0>


import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEffect;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;

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
        mSource.setTransfer(new Transfer[]{SimpleXmlTransfer.getInstance()});
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

    private static class CombinedDragSourceListener implements DragSourceListener {
        private final Item mItem;
        private Image mImage;
        private boolean mIsPlaceholder;

        public CombinedDragSourceListener(Item item) {
            mItem = item;
        }

        @Override
        public void dragStart(DragSourceEvent e) {
            mImage = renderPreview();
            mIsPlaceholder = mImage == null;

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
                    null,
                    bounds,
                    null);
            GlobalCanvasDragInfo.getInstance().stopDrag();
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
            if (event.data == null) {
                String data = SimpleXmlTransfer.getFqcn(mItem.getDescriptor());
                event.data = data;
            }
        }

        private Image renderPreview() {
            Control control = mItem.getControl();
            GC gc = new GC(control);
            Point size = control.getSize();
            Image image = new Image(mItem.getDisplay(), size.x, size.y);
            gc.copyArea(image, 0, 0);
            gc.dispose();

            Display display = mItem.getDisplay();
            BufferedImage awtImage = SwtUtils.convertToAwt(image);
            if (awtImage != null) {
                awtImage = ImageUtils.createDropShadow(awtImage, 3, 0x000000);
            }

            int alpha = !mIsPlaceholder ? IMG_ALPHA : -1;
            return SwtUtils.convertToSwt(display, awtImage, true, alpha);
        }

        /* package */ Image getPreviewImage() {
            return mImage;
        }

        /* package */ boolean isPlaceholder() {
            return mIsPlaceholder;
        }
    }
}

//<End of snippet n. 0>