/*ADT GLE2: Link outline's context menu to active canvas' menu.

Change-Id:I29712077a340276f0cde0c9c1ecf75f2e931e515*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java
//Synthetic comment -- index 7d01fd6..a1c1c5c 100644

//Synthetic comment -- @@ -307,7 +307,7 @@
/**
* Returns the custom IContentOutlinePage or IPropertySheetPage when asked for it.
*/
    @SuppressWarnings("unchecked")
@Override
public Object getAdapter(Class adapter) {
// for the outline, force it to come from the Graphical Editor.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 4805e37..b42a11d 100755

//Synthetic comment -- @@ -63,6 +63,7 @@
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
//Synthetic comment -- @@ -665,7 +666,7 @@

public void onSdkLoaded() {
Sdk currentSdk = Sdk.getCurrent();
            if (currentSdk != null) {
IAndroidTarget target = currentSdk.getTarget(mEditedFile.getProject());
if (target != null) {
mConfigComposite.onSdkLoaded(target);
//Synthetic comment -- @@ -830,6 +831,13 @@
return mLayoutEditor;
}

public UiDocumentNode getModel() {
return mLayoutEditor.getUiRootNode();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index d9172c8..dc0b253 100755

//Synthetic comment -- @@ -32,6 +32,9 @@

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
//Synthetic comment -- @@ -119,7 +122,6 @@
* TODO list:
* - gray on error, keep select but disable d'n'd.
* - context menu handling of layout + local props (via IViewRules)
 * - outline should include same context menu + delete/copy/paste ops.
* - outline should include drop support (from canvas or from palette)
* - handle empty root:
*    - Must also be able to copy/paste into an empty document (prolly need to bypass script, and deal with the xmlns)
//Synthetic comment -- @@ -127,6 +129,8 @@
*/
class LayoutCanvas extends Canvas implements ISelectionProvider {

/** The layout editor that uses this layout canvas. */
private final LayoutEditor mLayoutEditor;

//Synthetic comment -- @@ -244,6 +248,8 @@
/** Copy action for the Edit or context menu. */
private Action mCopyAction;


public LayoutCanvas(LayoutEditor layoutEditor,
RulesEngine rulesEngine,
//Synthetic comment -- @@ -607,6 +613,35 @@
mSelectionListeners.remove(listener);
}

//---

private class ScaleInfo implements ICanvasTransform {
//Synthetic comment -- @@ -1503,10 +1538,12 @@

private void createContextMenu() {

        MenuManager mm = new MenuManager();
        createMenuAction(mm);
        setMenu(mm.createContextMenu(this));
/*
Menu menu = new Menu(this);

ISharedImages wbImages = PlatformUI.getWorkbench().getSharedImages();
//Synthetic comment -- @@ -1635,9 +1672,13 @@
*/
private void copyActionAttributes(Action action, ActionFactory factory) {
IWorkbenchAction wa = factory.create(mLayoutEditor.getEditorSite().getWorkbenchWindow());
        action.setEnabled(false);
action.setText(wa.getText());
action.setDescription(wa.getDescription());
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

import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
//Synthetic comment -- @@ -36,8 +42,11 @@
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import java.util.ArrayList;
//Synthetic comment -- @@ -76,6 +85,8 @@
* element, so we introduce a fake parent.
*/
private final RootWrapper mRootWrapper = new RootWrapper();

public OutlinePage2() {
super();
//Synthetic comment -- @@ -125,11 +136,17 @@

// Listen to selection changes from the layout editor
getSite().getPage().addSelectionListener(this);
}

@Override
public void dispose() {
mRootWrapper.setRoot(null);
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

/**
* Content provider for the Outline model.
* Objects are going to be {@link CanvasViewInfo}.
//Synthetic comment -- @@ -346,4 +364,185 @@
// pass
}
}
}







