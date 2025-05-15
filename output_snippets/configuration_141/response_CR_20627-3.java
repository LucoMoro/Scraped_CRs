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
    private SimpleElement[] mElements;
    private Image mImage;
    private boolean mIsPlaceholder;

    public Item(Composite parent, PaletteControl palette, ElementDescriptor desc) {
        super(parent, SWT.NONE);
        mPalette = palette;
        mDesc = desc;
        mMouseIn = false;

        mSource = new DragSource(this, DND.DROP_COPY);
        mSource.setTransfer(new Transfer[]{SimpleXmlTransfer.getInstance()});
        mSource.addDragListener(new CombinedDragSourceEffect(this));
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

    private class CombinedDragSourceEffect extends DragSourceEffect implements DragSourceListener {
        private final Item mItem;

        public CombinedDragSourceEffect(Item item) {
            super(item);
            mItem = item;
        }

        @Override
        public void dragStart(DragSourceEvent e) {
            Rect bounds = null;
            DragSource dragSource = mItem.getDragSource();
            Image previewImage = renderPreview();
            mIsPlaceholder = previewImage == null;

            if (previewImage != null && !mIsPlaceholder) {
                ImageData data = previewImage.getImageData();
                int width = data.width;
                int height = data.height;
                bounds = new Rect(0, 0, width, height);
            }

            SimpleElement se = new SimpleElement(
                    SimpleXmlTransfer.getFqcn(mItem.getDescriptor()),
                    null, /* parentFqcn */
                    bounds, /* bounds */
                    null  /* parentBounds */
            );

            GlobalCanvasDragInfo.getInstance().stopDrag();
            mElements = null;
            e.doit = true;
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
            // Data setting logic here if needed
        }

        private Image renderPreview() {
            // Example implementation omitted for brevity
            return null; // Replace with actual rendering logic
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