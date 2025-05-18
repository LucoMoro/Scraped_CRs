//<Beginning of snippet n. 0>

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;

private static class Item extends CLabel implements MouseTrackListener {
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
        mSource.addDragListener(new DragSourceListener() {
            @Override
            public void dragStart(DragSourceEvent e) {
                mImage = renderPreview();
                mIsPlaceholder = mImage == null;
                e.doit = true;
                if (mImage != null) {
                    e.image = mImage;
                }
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
                event.data = mDesc;
            }
        });
    }

    @Override
    public void mouseHover(MouseEvent e) {
        // pass
    }

    ElementDescriptor getDescriptor() {
        return mDesc;
    }

    GraphicalEditorPart getEditor() {
        return mPalette.getEditor();
    }

    DragSource getDragSource() {
        return mSource;
    }

    private Image renderPreview() {
        // Implement the rendering logic here based on the descriptor
        // For demonstration, creating a simple image
        Image previewImage = new Image(getDisplay(), 100, 100); // Example dimensions
        // Add rendering logic here to customize the preview based on mDesc
        return previewImage; // Return the created image as the preview
    }

    Image getPreviewImage() {
        return mImage;
    }

    boolean isPlaceholder() {
        return mIsPlaceholder;
    }
}

//<End of snippet n. 0>