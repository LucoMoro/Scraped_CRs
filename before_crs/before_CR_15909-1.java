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
                mOutline = new OutlinePage2();
}

return mOutline;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index b42a11d..ea3171f 100755

//Synthetic comment -- @@ -63,7 +63,6 @@
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
//Synthetic comment -- @@ -831,9 +830,9 @@
return mLayoutEditor;
}

    public IAction getCanvasAction(String canvasActionId) {
if (mCanvasViewer != null) {
            return mCanvasViewer.getCanvas().getAction(canvasActionId);
}
return null;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ICanvasTransform.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ICanvasTransform.java
//Synthetic comment -- index 57069f5..03d86fb 100755

//Synthetic comment -- @@ -16,7 +16,12 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

public interface ICanvasTransform {
/**
* Margin around the rendered image.
* Should be enough space to display the layout width and height pseudo widgets.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index dc0b253..8d2279d 100755

//Synthetic comment -- @@ -129,7 +129,7 @@
*/
class LayoutCanvas extends Canvas implements ISelectionProvider {

    public static final String PREFIX_CANVAS_ACTION = "canvas_action_";

/** The layout editor that uses this layout canvas. */
private final LayoutEditor mLayoutEditor;
//Synthetic comment -- @@ -221,7 +221,7 @@
private ScaleInfo mHScale;

/** Drag source associated with this canvas. */
    private DragSource mSource;

/** List of clients listening to selection changes. */
private final ListenerList mSelectionListeners = new ListenerList();
//Synthetic comment -- @@ -250,6 +250,8 @@

private MenuManager mMenuManager;


public LayoutCanvas(LayoutEditor layoutEditor,
RulesEngine rulesEngine,
//Synthetic comment -- @@ -316,12 +318,8 @@
mDropListener = new CanvasDropListener(this);
mDropTarget.addDropListener(mDropListener);

        mSource = new DragSource(this, DND.DROP_COPY | DND.DROP_MOVE);
        mSource.setTransfer(new Transfer[] {
                TextTransfer.getInstance(),
                SimpleXmlTransfer.getInstance()
            } );
        mSource.addDragListener(new CanvasDragSourceListener());

// --- setup context menu ---
setupGlobalActionHandlers();
//Synthetic comment -- @@ -359,6 +357,11 @@
mRulesEngine = null;
}

if (mClipboard != null) {
mClipboard.dispose();
mClipboard = null;
//Synthetic comment -- @@ -392,13 +395,16 @@
* Returns the factory to use to convert from {@link CanvasViewInfo} or from
* {@link UiViewElementNode} to {@link INode} proxies.
*/
    public NodeFactory getNodeFactory() {
return mNodeFactory;
}

    /** Returns the shared SWT keyboard. */
    public Clipboard getClipboard() {
        return mClipboard;
}

/**
//Synthetic comment -- @@ -411,7 +417,7 @@
*
* @param result The new rendering result, either valid or not.
*/
    public void setResult(ILayoutResult result) {
// disable any hover
mHoverRect = null;

//Synthetic comment -- @@ -459,16 +465,16 @@
redraw();
}

    public void setShowOutline(boolean newState) {
mShowOutline = newState;
redraw();
}

    public double getScale() {
return mHScale.getScale();
}

    public void setScale(double scale) {
mHScale.setScale(scale);
mVScale.setScale(scale);
redraw();
//Synthetic comment -- @@ -483,7 +489,7 @@
* @param displayY Y in SWT display coordinates
* @return A new {@link Point} in canvas coordinates
*/
    public Point displayToCanvasPoint(int displayX, int displayY) {
// convert screen coordinates to local SWT control coordinates
org.eclipse.swt.graphics.Point p = this.toControl(displayX, displayY);

//Synthetic comment -- @@ -492,6 +498,19 @@
return new Point(x, y);
}


//----
// Implementation of ISelectionProvider
//Synthetic comment -- @@ -622,7 +641,7 @@
* <p/>
* Returns null if there's no action for the given id.
*/
    public IAction getAction(String actionId) {
String prefix = PREFIX_CANVAS_ACTION;
if (mMenuManager == null ||
actionId == null ||
//Synthetic comment -- @@ -644,6 +663,10 @@

//---

private class ScaleInfo implements ICanvasTransform {
/** Canvas image size (original, before zoom), in pixels */
private int mImgSize;
//Synthetic comment -- @@ -1260,6 +1283,29 @@

//---------------

private class CanvasDragSourceListener implements DragSourceListener {

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java
//Synthetic comment -- index c92b53b..88c891c 100755

//Synthetic comment -- @@ -27,6 +27,8 @@
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IElementComparer;
//Synthetic comment -- @@ -39,13 +41,17 @@
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

//Synthetic comment -- @@ -68,7 +74,9 @@
/**
* An outline page for the GLE2 canvas view.
* <p/>
 * The page is created by {@link LayoutEditor#getAdapter(Class)}.
* It sets itself as a listener on the site's selection service in order to be
* notified of the canvas' selection changes.
* The underlying page is also a selection provider (via IContentOutlinePage)
//Synthetic comment -- @@ -81,15 +89,31 @@
implements ISelectionListener, INullSelectionListener {

/**
* RootWrapper is a workaround: we can't set the input of the treeview to its root
* element, so we introduce a fake parent.
*/
private final RootWrapper mRootWrapper = new RootWrapper();
	/** Part listener, to update the context menu associated with GraphicalEditorPart. */
    private PartListener mPartListener;

    public OutlinePage2() {
super();
}

@Override
//Synthetic comment -- @@ -134,18 +158,23 @@
}
});

        // Listen to selection changes from the layout editor
        getSite().getPage().addSelectionListener(this);

setupContextMenu();
}

@Override
public void dispose() {
        mRootWrapper.setRoot(null);

        IWorkbenchWindow win = getSite().getWorkbenchWindow();
        win.getPartService().removePartListener(mPartListener);

getSite().getPage().removeSelectionListener(this);
super.dispose();
//Synthetic comment -- @@ -372,108 +401,37 @@
* by the {@link LayoutCanvas}. All the processing is actually handled
* directly by the canvas and this viewer only gets refreshed as a
* consequence of the canvas changing the XML model.
     * <p/>
     * To do that, we create actions that currently listen to the active
     * part and only defer to an active layout canvas.
*/
private void setupContextMenu() {

        MenuManager mm = new MenuManager();
        mm.removeAll();

final String prefix = LayoutCanvas.PREFIX_CANVAS_ACTION;
        mm.add(new DelegateAction(prefix + ActionFactory.CUT.getId()));
        mm.add(new DelegateAction(prefix + ActionFactory.COPY.getId()));
        mm.add(new DelegateAction(prefix + ActionFactory.PASTE.getId()));

        mm.add(new Separator());

        mm.add(new DelegateAction(prefix + ActionFactory.DELETE.getId()));
        mm.add(new DelegateAction(prefix + ActionFactory.SELECT_ALL.getId()));

        getControl().setMenu(mm.createContextMenu(getControl()));

        mPartListener = new PartListener(mm);
        IWorkbenchWindow win = getSite().getWorkbenchWindow();
        win.getPartService().addPartListener(mPartListener);
    }

    /**
     * Listen to part changes.
     * <p/>
     * This listener only cares specifically about GLE2's {@link GraphicalEditorPart} changing.
     * When the part changes, the delegate menu actions are refreshed to make sure that the
     * ouline's context menu always points to the current active layout canvas.
     */
    private static class PartListener implements IPartListener {
        private final MenuManager mMenuManager;

        public PartListener(MenuManager menuManager) {
            mMenuManager = menuManager;
        }

        public void partOpened(IWorkbenchPart part) {
            // pass
        }

        public void partActivated(IWorkbenchPart part) {
            GraphicalEditorPart gep = getGraphicalEditorPart(part);
            if (gep != null) {
                updateMenuActions(gep);
            }
        }

        public void partBroughtToTop(IWorkbenchPart part) {
            GraphicalEditorPart gep = getGraphicalEditorPart(part);
            if (gep != null) {
                updateMenuActions(gep);
            }
        }

        public void partDeactivated(IWorkbenchPart part) {
            GraphicalEditorPart gep = getGraphicalEditorPart(part);
            if (gep != null) {
                updateMenuActions(gep);
            }
        }

        public void partClosed(IWorkbenchPart part) {
            GraphicalEditorPart gep = getGraphicalEditorPart(part);
            if (gep != null) {
                updateMenuActions(gep);
            }
        }

        /**
         * Returns a non-null reference on the {@link GraphicalEditorPart} if this
         * is the part that changed, or null.
         */
        private GraphicalEditorPart getGraphicalEditorPart(IWorkbenchPart part) {
            if (part instanceof LayoutEditor) {
                part = ((LayoutEditor) part).getActiveEditor();
            }
            if (part instanceof GraphicalEditorPart) {
                return (GraphicalEditorPart) part;
            }
            return null;
        }

        /**
         * For every action contributed to our menu manager, ask the action to
         * update itself. The action will refresh its target action to match the
         * one from the given {@link GraphicalEditorPart}. If the editor part is
         * null, the delegate action will unlink from its target.
         */
        private void updateMenuActions(GraphicalEditorPart editorPart) {
            for (IContributionItem contrib : mMenuManager.getItems()) {
                if (contrib instanceof ActionContributionItem) {
                    IAction action = ((ActionContributionItem) contrib).getAction();
                    if (action instanceof DelegateAction) {
                        ((DelegateAction) action).updateFromEditorPart(editorPart);
}
}
}
        }
}

/**
//Synthetic comment -- @@ -521,10 +479,11 @@

/** Updates this action to delegate to its counterpart in the given editor part */
public void updateFromEditorPart(GraphicalEditorPart editorPart) {
            if (editorPart == null) {
mTargetAction = null;
} else {
                mTargetAction = editorPart.getCanvasAction(mCanvasActionId);
}

if (mTargetAction != null) {
//Synthetic comment -- @@ -545,4 +504,76 @@
}
}
}
}







