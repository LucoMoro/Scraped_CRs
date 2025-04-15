/*ADT GLE2: delegate drop events from Outline to Canvas.

Change-Id:I19c108ec3f242a88e750522cf2acfdd017318937*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 8d2279d..811d896 100755

//Synthetic comment -- @@ -55,6 +55,7 @@
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
//Synthetic comment -- @@ -118,14 +119,14 @@
* selection changes.
*
* @since GLE2
 */
/*
* TODO list:
* - gray on error, keep select but disable d'n'd.
* - context menu handling of layout + local props (via IViewRules)
* - handle empty root:
*    - Must also be able to copy/paste into an empty document (prolly need to bypass script, and deal with the xmlns)
 *    - We must be able to move/copy/cut the top root element (mostly between documents).
*/
class LayoutCanvas extends Canvas implements ISelectionProvider {

//Synthetic comment -- @@ -313,10 +314,8 @@
// --- setup drag'n'drop ---
// DND Reference: http://www.eclipse.org/articles/Article-SWT-DND/DND-in-SWT.html

mDropListener = new CanvasDropListener(this);
        mDropTarget = createDropTarget(this, mDropListener);

mDragSourceListener = new CanvasDragSourceListener();
mDragSource = createDragSource(this, mDragSourceListener);
//Synthetic comment -- @@ -403,11 +402,19 @@
* Returns our {@link DragSourceListener}.
* This is used by {@link OutlinePage2} to delegate drag source events.
*/
    /* package */ DragSourceListener getDragListener() {
return mDragSourceListener;
}

/**
     * Returns our {@link DropTargetListener}.
     * This is used by {@link OutlinePage2} to delegate drop target events.
     */
    /* package */ DropTargetListener getDropListener() {
        return mDropListener;
    }

    /**
* Sets the result of the layout rendering. The result object indicates if the layout
* rendering succeeded. If it did, it contains a bitmap and the objects rectangles.
*
//Synthetic comment -- @@ -1284,10 +1291,10 @@
//---------------

/**
     * Helper to create the drag source for the given control.
* <p/>
* This is static with package-access so that {@link OutlinePage2} can also
     * create an exact copy of the source with the same attributes.
*/
/* package */ static DragSource createDragSource(
Control control,
//Synthetic comment -- @@ -1301,6 +1308,21 @@
return source;
}

    /**
     * Helper to create the drop target for the given control.
     * <p/>
     * This is static with package-access so that {@link OutlinePage2} can also
     * create an exact copy of the drop target with the same attributes.
     */
    /* package */ static DropTarget createDropTarget(
            Control control,
            DropTargetListener dropListener) {
        DropTarget dropTarget = new DropTarget(
                control, DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_DEFAULT);
        dropTarget.setTransfer(new Transfer[] { SimpleXmlTransfer.getInstance() } );
        dropTarget.addDropListener(dropListener);
        return dropTarget;
    }

/**
* Our canvas {@link DragSourceListener}. Handles drag being started and finished








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java
//Synthetic comment -- index 88c891c..ec945b6 100755

//Synthetic comment -- @@ -45,6 +45,9 @@
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
//Synthetic comment -- @@ -57,20 +60,6 @@

import java.util.ArrayList;

/**
* An outline page for the GLE2 canvas view.
* <p/>
//Synthetic comment -- @@ -109,7 +98,13 @@
* Drag source, created with the same attributes as the one from {@link LayoutCanvas}.
* The drag source listener delegates to the current GraphicalEditorPart.
*/
    private DragSource mDragSource;

    /**
     * Drop target, created with the same attributes as the one from {@link LayoutCanvas}.
     * The drop target listener delegates to the current GraphicalEditorPart.
     */
    private DropTarget mDropTarget;

public OutlinePage2(GraphicalEditorPart graphicalEditorPart) {
super();
//Synthetic comment -- @@ -158,8 +153,8 @@
}
});

        mDragSource = LayoutCanvas.createDragSource(getControl(), new DelegateDragListener());
        mDropTarget = LayoutCanvas.createDropTarget(getControl(), new DelegateDropListener());

setupContextMenu();

//Synthetic comment -- @@ -169,9 +164,14 @@

@Override
public void dispose() {
        if (mDragSource != null) {
            mDragSource.dispose();
            mDragSource = null;
        }

        if (mDropTarget != null) {
            mDropTarget.dispose();
            mDropTarget = null;
}

mRootWrapper.setRoot(null);
//Synthetic comment -- @@ -508,7 +508,15 @@

// --- Drag Source ---

    /**
     * Delegates the drag events to the {@link LayoutCanvas}.
     * <p/>
     * We convert the drag coordinates from the bounding box {@link TreeViewer}'s
     * cell to the ones of the bounding of the corresponding canvas element.
     */
    private class DelegateDragListener implements DragSourceListener {

        private final Point mTempPoint = new Point(0, 0);

public void dragStart(DragSourceEvent event) {
if (!adjustEventCoordinates(event)) {
//Synthetic comment -- @@ -517,21 +525,23 @@
}
LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
if (canvas != null) {
                canvas.getDragListener().dragStart(event);
}
}

public void dragSetData(DragSourceEvent event) {
            adjustEventCoordinates(event);
LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
if (canvas != null) {
                canvas.getDragListener().dragSetData(event);
}
}

public void dragFinished(DragSourceEvent event) {
            adjustEventCoordinates(event);
LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
if (canvas != null) {
                canvas.getDragListener().dragFinished(event);
}
}

//Synthetic comment -- @@ -540,40 +550,168 @@
* its event coordinates to match the canvas *control* coordinates.
* <p/>
* Returns false if no element was found at the given position,
         * which is used to cancel the drag start.
*/
private boolean adjustEventCoordinates(DragSourceEvent event) {
            if (event.x == mTempPoint.x && event.y == mTempPoint.y) {
                // Seems like the event is reusing the coordinates we last
                // converted. Avoid converting them twice. We only modified
                // the event struct in case of a successful conversion so
                // we can return true here.
                return true;
}

            mTempPoint.x = event.x;
            mTempPoint.y = event.y;

            boolean result = viewerToCanvasControlCoordinates(mTempPoint);
            if (result) {
                event.x = mTempPoint.x;
                event.y = mTempPoint.y;
            }

            return result;
}
}

    // --- Drop Target ---

    /**
     * Delegates drop events to the {@link LayoutCanvas}.
     * <p/>
     * We convert the drag/drop coordinates from the bounding box {@link TreeViewer}'s
     * cell to the ones of the bounding of the corresponding canvas element.
     */
    private class DelegateDropListener implements DropTargetListener {

        private final Point mTempPoint = new Point(0, 0);

        public void dragEnter(DropTargetEvent event) {
            LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
            if (canvas != null) {
                adjustEventCoordinates(canvas, event);
                canvas.getDropListener().dragEnter(event);
            }
        }

        public void dragLeave(DropTargetEvent event) {
            LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
            if (canvas != null) {
                adjustEventCoordinates(canvas, event);
                canvas.getDropListener().dragLeave(event);
            }
        }

        public void dragOperationChanged(DropTargetEvent event) {
            LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
            if (canvas != null) {
                adjustEventCoordinates(canvas, event);
                canvas.getDropListener().dragOperationChanged(event);
            }
        }

        public void dragOver(DropTargetEvent event) {
            LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
            if (canvas != null) {
                adjustEventCoordinates(canvas, event);
                canvas.getDropListener().dragOver(event);
            }
        }

        public void dropAccept(DropTargetEvent event) {
            LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
            if (canvas != null) {
                adjustEventCoordinates(canvas, event);
                canvas.getDropListener().dropAccept(event);
            }
        }

        public void drop(DropTargetEvent event) {
            LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
            if (canvas != null) {
                adjustEventCoordinates(canvas, event);
                canvas.getDropListener().drop(event);
            }
        }

        /**
         * Finds the element under which the drag started and adjusts
         * its event coordinates to match the canvas *control* coordinates.
         * <p/>
         * Returns false if no element was found at the given position,
         * which is used to cancel the drag start.
         */
        private boolean adjustEventCoordinates(LayoutCanvas canvas, DropTargetEvent event) {
            if (event.x == mTempPoint.x && event.y == mTempPoint.y) {
                // Seems like the event is reusing the coordinates we last
                // converted. Avoid converting them twice. We only modified
                // the event struct in case of a successful conversion so
                // we can return true here.
                return true;
            }

            // Note that whereas DragSource coordinates are relative to the
            // control, DropTarget coordinates are relative to the display.
            // So we need to convert from display to control (treeview) coordinates.
            Point p = getControl().toControl(event.x, event.y);

            mTempPoint.x = p.x;
            mTempPoint.y = p.y;

            boolean result = viewerToCanvasControlCoordinates(mTempPoint);
            if (result) {
                // We now convert the canvas control coordinates to display coordinates.
                p = canvas.toDisplay(mTempPoint);

                event.x = p.x;
                event.y = p.y;
            }

            return result;
        }
    }

    /**
     * Finds the element under which the drag started and adjusts
     * its event coordinates to match the canvas *control* coordinates.
     * <p/>
     * I need to repeat this to make it extra clear: this returns canvas *control*
     * coordinates, not just "canvas coordinates".
     * <p/>
     * @param inOutXY The event x/y coordinates in input (in control coordinates).
     *   If the method returns true, it places the canvas *control* coordinates here.
     * @return false if no element was found at the given position, or true
     *   if the tree viewer cell was found and the coordinates were correctly converted.
     */
    private boolean viewerToCanvasControlCoordinates(Point inOutXY) {
        ViewerCell cell = getTreeViewer().getCell(inOutXY);
        if (cell != null) {
            Rectangle cr = cell.getBounds();
            Object item = cell.getElement();

            if (cr != null && !cr.isEmpty() && item instanceof CanvasViewInfo) {
                CanvasViewInfo vi = (CanvasViewInfo) item;
                Rectangle vir = vi.getAbsRect();

                // interpolate from the "cr" bounding box to the "vir" bounding box
                double ratio = (double) vir.width / (double) cr.width;
                int x = (int) (vir.x + ratio * (inOutXY.x - cr.x));
                ratio = (double) vir.height / (double) cr.height;
                int y = (int) (vir.y + ratio * (inOutXY.y - cr.y));

                LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
                if (canvas != null) {
                    com.android.ide.eclipse.adt.editors.layout.gscripts.Point p =
                        canvas.canvasToControlPoint(x, y);

                    inOutXY.x = p.x;
                    inOutXY.y = p.y;
                    return true;
                }
            }
        }

        return false;
    }

}







