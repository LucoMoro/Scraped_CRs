//<Beginning of snippet n. 0>

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
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
        mSource.setTransfer(new Transfer[]{SimpleXmlTransfer.getInstance()});
        mSource.addDragListener(new CombinedDragSourceHandler(this));
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

private static class CombinedDragSourceHandler implements DragSourceListener {
    private final Item mItem;
    private Image mPreviewImage;

    public CombinedDragSourceHandler(Item item) {
        mItem = item;
    }

    public void dragStart(DragSourceEvent e) {
        try {
            mPreviewImage = renderPreview();
            if (mPreviewImage != null) {
                ImageData data = mPreviewImage.getImageData();
                int width = data.width;
                int height = data.height;
                SimpleElement se = new SimpleElement(
                    SimpleXmlTransfer.getFqcn(mItem.getDescriptor()),
                    null, 
                    new Rect(0, 0, width, height), 
                    null);
                GlobalCanvasDragInfo.getInstance().stopDrag();
            } else {
                e.doit = false;
            }
        } catch (Exception ex) {
            System.err.println("Error during dragStart: " + ex.getMessage());
            e.doit = false;
        }
    }

    public void dragFinished(DragSourceEvent e) {
        if (mPreviewImage != null) {
            mPreviewImage.dispose();
            mPreviewImage = null;
        }
    }

    public void dragSetData(DragSourceEvent e) {
        // Set data for the drag here
    }
    
    private Image renderPreview() {
        Point size = mItem.getSize();
        Image previewImage = new Image(mItem.getDisplay(), size.x, size.y);
        GC gc = new GC(previewImage);
        // Implement logic to draw item specifics into the context
        gc.dispose();
        return previewImage;
    }
}

//<End of snippet n. 0>