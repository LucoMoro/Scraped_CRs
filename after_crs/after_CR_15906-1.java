/*ADT GLE2: Link outline's context menu to active canvas' menu.

Change-Id:I29712077a340276f0cde0c9c1ecf75f2e931e515*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java
//Synthetic comment -- index 7d01fd6..a1c1c5c 100644

//Synthetic comment -- @@ -307,7 +307,7 @@
/**
* Returns the custom IContentOutlinePage or IPropertySheetPage when asked for it.
*/
    @SuppressWarnings("rawtypes")
@Override
public Object getAdapter(Class adapter) {
// for the outline, force it to come from the Graphical Editor.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 4805e37..b42a11d 100755

//Synthetic comment -- @@ -63,6 +63,7 @@
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
//Synthetic comment -- @@ -665,7 +666,7 @@

public void onSdkLoaded() {
Sdk currentSdk = Sdk.getCurrent();
            if (currentSdk != null && mEditedFile != null) {
IAndroidTarget target = currentSdk.getTarget(mEditedFile.getProject());
if (target != null) {
mConfigComposite.onSdkLoaded(target);
//Synthetic comment -- @@ -830,6 +831,13 @@
return mLayoutEditor;
}

    public IAction getCanvasAction(String canvasActionId) {
        if (mCanvasViewer != null) {
            return mCanvasViewer.getCanvas().getAction(canvasActionId);
        }
        return null;
    }

public UiDocumentNode getModel() {
return mLayoutEditor.getUiRootNode();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index d9172c8..dc0b253 100755

//Synthetic comment -- @@ -32,6 +32,9 @@

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
//Synthetic comment -- @@ -119,7 +122,6 @@
* TODO list:
* - gray on error, keep select but disable d'n'd.
* - context menu handling of layout + local props (via IViewRules)
* - outline should include drop support (from canvas or from palette)
* - handle empty root:
*    - Must also be able to copy/paste into an empty document (prolly need to bypass script, and deal with the xmlns)
//Synthetic comment -- @@ -127,6 +129,8 @@
*/
class LayoutCanvas extends Canvas implements ISelectionProvider {

    public static final String PREFIX_CANVAS_ACTION = "canvas_action_";

/** The layout editor that uses this layout canvas. */
private final LayoutEditor mLayoutEditor;

//Synthetic comment -- @@ -244,6 +248,8 @@
/** Copy action for the Edit or context menu. */
private Action mCopyAction;

    private MenuManager mMenuManager;


public LayoutCanvas(LayoutEditor layoutEditor,
RulesEngine rulesEngine,
//Synthetic comment -- @@ -607,6 +613,35 @@
mSelectionListeners.remove(listener);
}

    /**
     * Returns the action for the context menu corresponding to the given action id.
     * <p/>
     * For global actions such as copy or paste, the action id must be composed of
     * the {@link #PREFIX_CANVAS_ACTION} followed by one of {@link ActionFactory}'s
     * action ids.
     * <p/>
     * Returns null if there's no action for the given id.
     */
    public IAction getAction(String actionId) {
        String prefix = PREFIX_CANVAS_ACTION;
        if (mMenuManager == null ||
                actionId == null ||
                !actionId.startsWith(prefix)) {
            return null;
        }

        actionId = actionId.substring(prefix.length());

        for (IContributionItem contrib : mMenuManager.getItems()) {
            if (contrib instanceof ActionContributionItem &&
                    actionId.equals(contrib.getId())) {
                return ((ActionContributionItem) contrib).getAction();
            }
        }

        return null;
    }

//---

private class ScaleInfo implements ICanvasTransform {
//Synthetic comment -- @@ -1503,10 +1538,12 @@

private void createContextMenu() {

        mMenuManager = new MenuManager();
        createMenuAction(mMenuManager);
        setMenu(mMenuManager.createContextMenu(this));
/*
        TODO insert generated menus/actions here.

Menu menu = new Menu(this);

ISharedImages wbImages = PlatformUI.getWorkbench().getSharedImages();
//Synthetic comment -- @@ -1635,9 +1672,13 @@
*/
private void copyActionAttributes(Action action, ActionFactory factory) {
IWorkbenchAction wa = factory.create(mLayoutEditor.getEditorSite().getWorkbenchWindow());
        action.setId(wa.getId());
action.setText(wa.getText());
        action.setEnabled(wa.isEnabled());
action.setDescription(wa.getDescription());
        action.setToolTipText(wa.getToolTipText());
        action.setAccelerator(wa.getAccelerator());
        action.setActionDefinitionId(wa.getActionDefinitionId());
action.setImageDescriptor(wa.getImageDescriptor());
action.setHoverImageDescriptor(wa.getHoverImageDescriptor());
action.setDisabledImageDescriptor(wa.getDisabledImageDescriptor());
//Synthetic comment -- @@ -1819,5 +1860,4 @@
//   Must also be able to copy/paste into an empty document (prolly need to bypass script, and deal with the xmlns)
AdtPlugin.displayWarning("Canvas Paste", "Not implemented yet");
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java
//Synthetic comment -- index b1086c2..c92b53b 100755

//Synthetic comment -- @@ -23,6 +23,12 @@
import com.android.ide.eclipse.adt.internal.editors.ui.ErrorImageComposite;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
//Synthetic comment -- @@ -36,8 +42,11 @@
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import java.util.ArrayList;
//Synthetic comment -- @@ -76,6 +85,8 @@
* element, so we introduce a fake parent.
*/
private final RootWrapper mRootWrapper = new RootWrapper();
	/** Part listener, to update the context menu associated with GraphicalEditorPart. */
    private PartListener mPartListener;

public OutlinePage2() {
super();
//Synthetic comment -- @@ -125,11 +136,17 @@

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
}
//Synthetic comment -- @@ -199,7 +216,6 @@

// ----

/**
* In theory, the root of the model should be the input of the {@link TreeViewer},
* which would be the root {@link CanvasViewInfo}.
//Synthetic comment -- @@ -225,6 +241,8 @@
}
}

    // --- Content and Label Providers ---

/**
* Content provider for the Outline model.
* Objects are going to be {@link CanvasViewInfo}.
//Synthetic comment -- @@ -346,4 +364,185 @@
// pass
}
}

    // --- Context Menu ---

    /**
     * This viewer uses its own actions that delegate to the ones given
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
     * An action that delegates its properties and behavior to a target action.
     * The target action can be null or it can change overtime, typically as the
     * layout canvas' editor part is activated or closed.
     */
    private static class DelegateAction extends Action {
        private IAction mTargetAction;
        private final String mCanvasActionId;

        public DelegateAction(String canvasActionId) {
            super(canvasActionId);
            mCanvasActionId = canvasActionId;
        }

        // --- Methods form IAction ---

        /** Returns the target action's {@link #isEnabled()} if defined, or false. */
        @Override
        public boolean isEnabled() {
            return mTargetAction == null ? false : mTargetAction.isEnabled();
        }

        /** Returns the target action's {@link #isChecked()} if defined, or false. */
        @Override
        public boolean isChecked() {
            return mTargetAction == null ? false : mTargetAction.isChecked();
        }

        /** Returns the target action's {@link #isHandled()} if defined, or false. */
        @Override
        public boolean isHandled() {
            return mTargetAction == null ? false : mTargetAction.isHandled();
        }

        /** Runs the target action if defined. */
        @Override
        public void run() {
            if (mTargetAction != null) {
                mTargetAction.run();
            }
            super.run();
        }

        /** Updates this action to delegate to its counterpart in the given editor part */
        public void updateFromEditorPart(GraphicalEditorPart editorPart) {
            if (editorPart == null) {
                mTargetAction = null;
            } else {
                mTargetAction = editorPart.getCanvasAction(mCanvasActionId);
            }

            if (mTargetAction != null) {
                setText(mTargetAction.getText());
                setId(mTargetAction.getId());
                setDescription(mTargetAction.getDescription());
                setImageDescriptor(mTargetAction.getImageDescriptor());
                setHoverImageDescriptor(mTargetAction.getHoverImageDescriptor());
                setDisabledImageDescriptor(mTargetAction.getDisabledImageDescriptor());
                setToolTipText(mTargetAction.getToolTipText());
                setActionDefinitionId(mTargetAction.getActionDefinitionId());
                setHelpListener(mTargetAction.getHelpListener());
                setAccelerator(mTargetAction.getAccelerator());
                setChecked(mTargetAction.isChecked());
                setEnabled(mTargetAction.isEnabled());
            } else {
                setEnabled(false);
            }
        }
    }
}







