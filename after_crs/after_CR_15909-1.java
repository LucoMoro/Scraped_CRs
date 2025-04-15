/*ADT GLE2: drag from Outline view.

The drag source listener delegates the handling to the canvas.

Changed the OutlinePage2 to no longer listen to parts activations.
Instead there's one instance of OutlinePage2 per instance of
the GraphicalEditorPart and the link is provided in the constructor
directly.

Change-Id:I8bee65b2a7f75bd1436082c9a9753c561d8a6cab*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java
//Synthetic comment -- index a1c1c5c..e245558 100644

//Synthetic comment -- @@ -325,7 +325,7 @@
mOutline = mOutlineForGle1;

} else if (mOutline == null && mGraphicalEditor instanceof GraphicalEditorPart) {
                mOutline = new OutlinePage2((GraphicalEditorPart) mGraphicalEditor);
}

return mOutline;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index b42a11d..ea3171f 100755

//Synthetic comment -- @@ -63,7 +63,6 @@
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
//Synthetic comment -- @@ -831,9 +830,9 @@
return mLayoutEditor;
}

    /* package */ LayoutCanvas getCanvasControl() {
if (mCanvasViewer != null) {
            return mCanvasViewer.getCanvas();
}
return null;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ICanvasTransform.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ICanvasTransform.java
//Synthetic comment -- index 57069f5..03d86fb 100755

//Synthetic comment -- @@ -16,7 +16,12 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

/**
 * Interface for a class that can convert between client pixel's coordinates
 * and canvas coordinates. Each instance of such a transform deals with only
 * one axis, so clients need to use 2 instances for X and Y.
 */
/* package */ interface ICanvasTransform {
/**
* Margin around the rendered image.
* Should be enough space to display the layout width and height pseudo widgets.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index dc0b253..8d2279d 100755

//Synthetic comment -- @@ -129,7 +129,7 @@
*/
class LayoutCanvas extends Canvas implements ISelectionProvider {

    /* package */ static final String PREFIX_CANVAS_ACTION = "canvas_action_";

/** The layout editor that uses this layout canvas. */
private final LayoutEditor mLayoutEditor;
//Synthetic comment -- @@ -221,7 +221,7 @@
private ScaleInfo mHScale;

/** Drag source associated with this canvas. */
    private DragSource mDragSource;

/** List of clients listening to selection changes. */
private final ListenerList mSelectionListeners = new ListenerList();
//Synthetic comment -- @@ -250,6 +250,8 @@

private MenuManager mMenuManager;

    private CanvasDragSourceListener mDragSourceListener;


public LayoutCanvas(LayoutEditor layoutEditor,
RulesEngine rulesEngine,
//Synthetic comment -- @@ -316,12 +318,8 @@
mDropListener = new CanvasDropListener(this);
mDropTarget.addDropListener(mDropListener);

        mDragSourceListener = new CanvasDragSourceListener();
        mDragSource = createDragSource(this, mDragSourceListener);

// --- setup context menu ---
setupGlobalActionHandlers();
//Synthetic comment -- @@ -359,6 +357,11 @@
mRulesEngine = null;
}

        if (mDragSource != null) {
            mDragSource.dispose();
            mDragSource = null;
        }

if (mClipboard != null) {
mClipboard.dispose();
mClipboard = null;
//Synthetic comment -- @@ -392,13 +395,16 @@
* Returns the factory to use to convert from {@link CanvasViewInfo} or from
* {@link UiViewElementNode} to {@link INode} proxies.
*/
    /* package */ NodeFactory getNodeFactory() {
return mNodeFactory;
}

    /**
     * Returns our {@link DragSourceListener}.
     * This is used by {@link OutlinePage2} to delegate drag source events.
     */
    /* package */ DragSourceListener getDragSourceListener() {
        return mDragSourceListener;
}

/**
//Synthetic comment -- @@ -411,7 +417,7 @@
*
* @param result The new rendering result, either valid or not.
*/
    /* package */ void setResult(ILayoutResult result) {
// disable any hover
mHoverRect = null;

//Synthetic comment -- @@ -459,16 +465,16 @@
redraw();
}

    /* package */ void setShowOutline(boolean newState) {
mShowOutline = newState;
redraw();
}

    /* package */ double getScale() {
return mHScale.getScale();
}

    /* package */ void setScale(double scale) {
mHScale.setScale(scale);
mVScale.setScale(scale);
redraw();
//Synthetic comment -- @@ -483,7 +489,7 @@
* @param displayY Y in SWT display coordinates
* @return A new {@link Point} in canvas coordinates
*/
    /* package */ Point displayToCanvasPoint(int displayX, int displayY) {
// convert screen coordinates to local SWT control coordinates
org.eclipse.swt.graphics.Point p = this.toControl(displayX, displayY);

//Synthetic comment -- @@ -492,6 +498,19 @@
return new Point(x, y);
}

    /**
     * Transforms a point, expressed in canvas coordinates, into "client" coordinates
     * relative to the control (and not relative to the display.)
     *
     * @param canvasX X in the canvas coordinates
     * @param canvasY Y in the canvas coordinates
     * @return A new {@link Point} in control client coordinates (not display coordinates)
     */
    /* package */ Point canvasToControlPoint(int canvasX, int canvasY) {
        int x = mHScale.translate(canvasX);
        int y = mVScale.translate(canvasY);
        return new Point(x, y);
    }

//----
// Implementation of ISelectionProvider
//Synthetic comment -- @@ -622,7 +641,7 @@
* <p/>
* Returns null if there's no action for the given id.
*/
    /* package */ IAction getAction(String actionId) {
String prefix = PREFIX_CANVAS_ACTION;
if (mMenuManager == null ||
actionId == null ||
//Synthetic comment -- @@ -644,6 +663,10 @@

//---

    /**
     * Helper class to convert between control pixel coordinates and canvas coordinates.
     * Takes care of the zooming and offset of the canvas.
     */
private class ScaleInfo implements ICanvasTransform {
/** Canvas image size (original, before zoom), in pixels */
private int mImgSize;
//Synthetic comment -- @@ -1260,6 +1283,29 @@

//---------------

    /**
     * Helper to create our drag source for the given control.
     * <p/>
     * This is static with package-access so that {@link OutlinePage2} can also
     * create an exact copy of the source, with the same attributes.
     */
    /* package */ static DragSource createDragSource(
            Control control,
            DragSourceListener dragSourceListener) {
        DragSource source = new DragSource(control, DND.DROP_COPY | DND.DROP_MOVE);
        source.setTransfer(new Transfer[] {
                TextTransfer.getInstance(),
                SimpleXmlTransfer.getInstance()
            } );
        source.addDragListener(dragSourceListener);
        return source;
    }


    /**
     * Our canvas {@link DragSourceListener}. Handles drag being started and finished
     * and generating the drag data.
     */
private class CanvasDragSourceListener implements DragSourceListener {

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java
//Synthetic comment -- index c92b53b..88c891c 100755

//Synthetic comment -- @@ -27,6 +27,8 @@
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IElementComparer;
//Synthetic comment -- @@ -39,13 +41,17 @@
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

//Synthetic comment -- @@ -68,7 +74,9 @@
/**
* An outline page for the GLE2 canvas view.
* <p/>
 * The page is created by {@link LayoutEditor#getAdapter(Class)}. This means
 * we have *one* instance of the outline page per open canvas editor.
 * <p/>
* It sets itself as a listener on the site's selection service in order to be
* notified of the canvas' selection changes.
* The underlying page is also a selection provider (via IContentOutlinePage)
//Synthetic comment -- @@ -81,15 +89,31 @@
implements ISelectionListener, INullSelectionListener {

/**
     * The graphical editor that created this outline.
     */
    private final GraphicalEditorPart mGraphicalEditorPart;

    /**
* RootWrapper is a workaround: we can't set the input of the treeview to its root
* element, so we introduce a fake parent.
*/
private final RootWrapper mRootWrapper = new RootWrapper();

    /**
     * Menu manager for the context menu actions.
     * The actions delegate to the current GraphicalEditorPart.
     */
    private MenuManager mMenuManager;

    /**
     * Drag source, created with the same attributes as the one from {@link LayoutCanvas}.
     * The drag source listener delegates to the current GraphicalEditorPart.
     */
    private DragSource mSource;

    public OutlinePage2(GraphicalEditorPart graphicalEditorPart) {
super();
        mGraphicalEditorPart = graphicalEditorPart;
}

@Override
//Synthetic comment -- @@ -134,18 +158,23 @@
}
});


        mSource = LayoutCanvas.createDragSource(getControl(), new DelegateDragSourceListener());

setupContextMenu();

        // Listen to selection changes from the layout editor
        getSite().getPage().addSelectionListener(this);
}

@Override
public void dispose() {
        if (mSource != null) {
            mSource.dispose();
            mSource = null;
        }

        mRootWrapper.setRoot(null);

getSite().getPage().removeSelectionListener(this);
super.dispose();
//Synthetic comment -- @@ -372,108 +401,37 @@
* by the {@link LayoutCanvas}. All the processing is actually handled
* directly by the canvas and this viewer only gets refreshed as a
* consequence of the canvas changing the XML model.
*/
private void setupContextMenu() {

        mMenuManager = new MenuManager();
        mMenuManager.removeAll();

final String prefix = LayoutCanvas.PREFIX_CANVAS_ACTION;
        mMenuManager.add(new DelegateAction(prefix + ActionFactory.CUT.getId()));
        mMenuManager.add(new DelegateAction(prefix + ActionFactory.COPY.getId()));
        mMenuManager.add(new DelegateAction(prefix + ActionFactory.PASTE.getId()));

        mMenuManager.add(new Separator());

        mMenuManager.add(new DelegateAction(prefix + ActionFactory.DELETE.getId()));
        mMenuManager.add(new DelegateAction(prefix + ActionFactory.SELECT_ALL.getId()));

        getControl().setMenu(mMenuManager.createContextMenu(getControl()));

        mMenuManager.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                // Update all actions to match their LayoutCanvas counterparts
                for (IContributionItem contrib : mMenuManager.getItems()) {
                    if (contrib instanceof ActionContributionItem) {
                        IAction action = ((ActionContributionItem) contrib).getAction();
                        if (action instanceof DelegateAction) {
                            ((DelegateAction) action).updateFromEditorPart(mGraphicalEditorPart);
                        }
}
}
}
        });
}

/**
//Synthetic comment -- @@ -521,10 +479,11 @@

/** Updates this action to delegate to its counterpart in the given editor part */
public void updateFromEditorPart(GraphicalEditorPart editorPart) {
            LayoutCanvas canvas = editorPart == null ? null : editorPart.getCanvasControl();
            if (canvas == null) {
mTargetAction = null;
} else {
                mTargetAction = canvas.getAction(mCanvasActionId);
}

if (mTargetAction != null) {
//Synthetic comment -- @@ -545,4 +504,76 @@
}
}
}


    // --- Drag Source ---

    private class DelegateDragSourceListener implements DragSourceListener {

        public void dragStart(DragSourceEvent event) {
            if (!adjustEventCoordinates(event)) {
                event.doit = false;
                return;
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

        /**
         * Finds the element under which the drag started and adjusts
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







