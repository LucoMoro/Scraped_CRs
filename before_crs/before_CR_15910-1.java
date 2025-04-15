/*ADT GLE2: delegate drop events from Outline to Canvas.

Change-Id:I19c108ec3f242a88e750522cf2acfdd017318937*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 8d2279d..811d896 100755

//Synthetic comment -- @@ -55,6 +55,7 @@
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
//Synthetic comment -- @@ -118,14 +119,14 @@
* selection changes.
*
* @since GLE2
 *
* TODO list:
* - gray on error, keep select but disable d'n'd.
* - context menu handling of layout + local props (via IViewRules)
 * - outline should include drop support (from canvas or from palette)
* - handle empty root:
*    - Must also be able to copy/paste into an empty document (prolly need to bypass script, and deal with the xmlns)
      - We must be able to move/copy/cut the top root element (mostly between documents).
*/
class LayoutCanvas extends Canvas implements ISelectionProvider {

//Synthetic comment -- @@ -313,10 +314,8 @@
// --- setup drag'n'drop ---
// DND Reference: http://www.eclipse.org/articles/Article-SWT-DND/DND-in-SWT.html

        mDropTarget = new DropTarget(this, DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_DEFAULT);
        mDropTarget.setTransfer(new Transfer[] { SimpleXmlTransfer.getInstance() } );
mDropListener = new CanvasDropListener(this);
        mDropTarget.addDropListener(mDropListener);

mDragSourceListener = new CanvasDragSourceListener();
mDragSource = createDragSource(this, mDragSourceListener);
//Synthetic comment -- @@ -403,11 +402,19 @@
* Returns our {@link DragSourceListener}.
* This is used by {@link OutlinePage2} to delegate drag source events.
*/
    /* package */ DragSourceListener getDragSourceListener() {
return mDragSourceListener;
}

/**
* Sets the result of the layout rendering. The result object indicates if the layout
* rendering succeeded. If it did, it contains a bitmap and the objects rectangles.
*
//Synthetic comment -- @@ -1284,10 +1291,10 @@
//---------------

/**
     * Helper to create our drag source for the given control.
* <p/>
* This is static with package-access so that {@link OutlinePage2} can also
     * create an exact copy of the source, with the same attributes.
*/
/* package */ static DragSource createDragSource(
Control control,
//Synthetic comment -- @@ -1301,6 +1308,21 @@
return source;
}


/**
* Our canvas {@link DragSourceListener}. Handles drag being started and finished








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java
//Synthetic comment -- index 88c891c..ec945b6 100755

//Synthetic comment -- @@ -45,6 +45,9 @@
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
//Synthetic comment -- @@ -57,20 +60,6 @@

import java.util.ArrayList;

/*
 * TODO -- missing features:
 * - right-click context menu *shared* with the one from canvas (simply delegate action)
 * - drag'n'drop initiated from Palette to Outline
 * - drag'n'drop from Outline to Outline
 * - drag'n'drop from Canvas to Outline
 * - drag'n'drop from Outline to Canvas
 * - => Check if we can handle all the d'n'd cases a simply delegating the action to the canvas.
 *      There's a *lot* of logic in the CanvasDropListener we don't want to replicate.
 *      That should be fairly trivial, except that we can't provide X/Y coordinates in the drop
 *      move. We could just fake them by using the topleft/middle point of the tree item's bounds
 *      or something like that.
 */

/**
* An outline page for the GLE2 canvas view.
* <p/>
//Synthetic comment -- @@ -109,7 +98,13 @@
* Drag source, created with the same attributes as the one from {@link LayoutCanvas}.
* The drag source listener delegates to the current GraphicalEditorPart.
*/
    private DragSource mSource;

public OutlinePage2(GraphicalEditorPart graphicalEditorPart) {
super();
//Synthetic comment -- @@ -158,8 +153,8 @@
}
});


        mSource = LayoutCanvas.createDragSource(getControl(), new DelegateDragSourceListener());

setupContextMenu();

//Synthetic comment -- @@ -169,9 +164,14 @@

@Override
public void dispose() {
        if (mSource != null) {
            mSource.dispose();
            mSource = null;
}

mRootWrapper.setRoot(null);
//Synthetic comment -- @@ -508,7 +508,15 @@

// --- Drag Source ---

    private class DelegateDragSourceListener implements DragSourceListener {

public void dragStart(DragSourceEvent event) {
if (!adjustEventCoordinates(event)) {
//Synthetic comment -- @@ -517,21 +525,23 @@
}
LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
if (canvas != null) {
                canvas.getDragSourceListener().dragStart(event);
}
}

public void dragSetData(DragSourceEvent event) {
LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
if (canvas != null) {
                canvas.getDragSourceListener().dragSetData(event);
}
}

public void dragFinished(DragSourceEvent event) {
LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
if (canvas != null) {
                canvas.getDragSourceListener().dragFinished(event);
}
}

//Synthetic comment -- @@ -540,40 +550,168 @@
* its event coordinates to match the canvas *control* coordinates.
* <p/>
* Returns false if no element was found at the given position,
         * which will cancel the drag start.
*/
private boolean adjustEventCoordinates(DragSourceEvent event) {
            int x = event.x;
            int y = event.y;
            ViewerCell cell = getTreeViewer().getCell(new Point(x, y));
            if (cell != null) {
                Rectangle cr = cell.getBounds();
                Object item = cell.getElement();

                if (cr != null && !cr.isEmpty() && item instanceof CanvasViewInfo) {
                    CanvasViewInfo vi = (CanvasViewInfo) item;
                    Rectangle vir = vi.getAbsRect();

                    // interpolate from the "cr" bounding box to the "vir" bounding box
                    double ratio = (double) vir.width / (double) cr.width;
                    x = (int) (vir.x + ratio * (x - cr.x));
                    ratio = (double) vir.height / (double) cr.height;
                    y = (int) (vir.y + ratio * (y - cr.y));

                    LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
                    if (canvas != null) {
                        com.android.ide.eclipse.adt.editors.layout.gscripts.Point p =
                            canvas.canvasToControlPoint(x, y);

                        event.x = p.x;
                        event.y = p.y;
                        return true;
                    }
                }
}

            return false;
}
}

}







