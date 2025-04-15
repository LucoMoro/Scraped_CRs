/*Only allow drag images for canvas drags

The outline view "reuses" the drag source listener for the canvas,
doing some tricks with mouse events to make it look like the drag is
originating within the canvas. However, the image previews of drags do
not work well in this case. Therefore, stash data on the drag souce
such that the drag source handler can only do image previews when the
drag source is really the canvas instead of the outline page.

Change-Id:I919264f001fa232053a0b9831ebc32ee22beb877*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java
//Synthetic comment -- index d3a7820..3e06bb1 100644

//Synthetic comment -- @@ -47,6 +47,9 @@
* gestures and in order to update the gestures along the way.
*/
public class GestureManager {
    /** Drag source data key */
    private static String KEY_DRAG_PREVIEW = "dragpreview"; //NON-NLS-1$

/** The canvas which owns this GestureManager. */
private final LayoutCanvas mCanvas;

//Synthetic comment -- @@ -466,6 +469,28 @@
}

/**
     * Sets whether the given drag source is enabled for drag previews.
     * The default for a drag source is false.
     *
     * @param source the drag source in question
     * @param enable true to enable drag previews
     */
    public static void setDragPreviewEnabled(DragSource source, boolean enable) {
        source.setData(KEY_DRAG_PREVIEW, enable ? KEY_DRAG_PREVIEW : null);
    }

    /**
     * Returns true if the given drag source is enabled for drag previews.
     * The default for a drag source is false.
     *
     * @param source the drag source in question
     * @return true if the drag source allows drag previews
     */
    public static boolean isDragPreviewEnabled(DragSource source) {
        return source.getData(KEY_DRAG_PREVIEW) != null;
    }

    /**
* Our canvas {@link DragSourceListener}. Handles drag being started and
* finished and generating the drag data.
*/
//Synthetic comment -- @@ -581,7 +606,12 @@

// Render drag-images: Copy portions of the full screen render.
Image image = mCanvas.getImageOverlay().getImage();
                boolean enabled = false;
                if (e.widget instanceof DragSource) {
                    DragSource ds = (DragSource) e.widget;
                    enabled = isDragPreviewEnabled(ds);
                }
                if (enabled && image != null) {
/**
* Transparency of the dragged image ([0-255]). We're using 30%
* translucency to make the image faint and not obscure the drag








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 443171e..70e3e56 100755

//Synthetic comment -- @@ -280,6 +280,7 @@

mDropTarget = createDropTarget(this);
mDragSource = createDragSource(this);
        GestureManager.setDragPreviewEnabled(mDragSource, true);
mGestureManager.registerListeners(mDragSource, mDropTarget);

if (mLayoutEditor == null) {







