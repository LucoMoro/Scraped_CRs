/*Add Move Up/Down Actions

This changeset adds "Move Up" and "Move Down" context menu items to
the outline, as well as keyboard shortcuts for these: + and -. The
actions apply only when the Outline window has focus. Thus, to reorder
an item you can select it, then press + repeatedly to move it down in
the hierarchy or - to move it back up.

In addition, this changeset fixes a selection syncing issue which also
affected drag & drop: It will now correctly always select the target
dropped tree items after a drag or reorder.

Change-Id:I11ce4fd302e537b86a614a7cbe59f1ac47f61aa5*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlineDropListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlineDropListener.java
//Synthetic comment -- index e777e3b..e70687e 100644

//Synthetic comment -- @@ -27,7 +27,6 @@
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Display;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
//Synthetic comment -- @@ -157,18 +156,7 @@
}
// Select the newly dropped nodes
final SelectionManager selectionManager = canvas.getSelectionManager();
        if (!selectionManager.selectDropped(added)) {
            // In some scenarios we can't find the actual view infos yet; this
            // seems to happen when you drag from one canvas to another (see the
            // related comment next to the setFocus() call below). In that case
            // defer selection briefly until the view hierarchy etc is up to
            // date.
            Display.getDefault().asyncExec(new Runnable() {
                public void run() {
                    selectionManager.selectDropped(added);
                }
            });
        }

canvas.redraw();









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java
//Synthetic comment -- index 33032de..b16f0e5 100755

//Synthetic comment -- @@ -16,13 +16,21 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder.Reference;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.ui.ErrorImageComposite;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
//Synthetic comment -- @@ -48,8 +56,14 @@
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.IPageLayout;
//Synthetic comment -- @@ -61,6 +75,9 @@
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import java.util.ArrayList;

/**
* An outline page for the layout canvas view.
//Synthetic comment -- @@ -83,7 +100,7 @@
private final GraphicalEditorPart mGraphicalEditorPart;

/**
     * RootWrapper is a workaround: we can't set the input of the treeview to its root
* element, so we introduce a fake parent.
*/
private final RootWrapper mRootWrapper = new RootWrapper();
//Synthetic comment -- @@ -108,6 +125,44 @@
}
};

public OutlinePage(GraphicalEditorPart graphicalEditorPart) {
super();
mGraphicalEditorPart = graphicalEditorPart;
//Synthetic comment -- @@ -184,6 +239,25 @@
dispose();
}
});
}

@Override
//Synthetic comment -- @@ -442,6 +516,10 @@
mMenuManager = new MenuManager();
mMenuManager.removeAll();

final String prefix = LayoutCanvas.PREFIX_CANVAS_ACTION;
mMenuManager.add(new DelegateAction(prefix + ActionFactory.CUT.getId()));
mMenuManager.add(new DelegateAction(prefix + ActionFactory.COPY.getId()));
//Synthetic comment -- @@ -472,6 +550,13 @@
mMenuManager);

getControl().setMenu(mMenuManager.createContextMenu(getControl()));
}

/**
//Synthetic comment -- @@ -518,7 +603,11 @@
super.run();
}

        /** Updates this action to delegate to its counterpart in the given editor part */
public void updateFromEditorPart(GraphicalEditorPart editorPart) {
LayoutCanvas canvas = editorPart == null ? null : editorPart.getCanvasControl();
if (canvas == null) {
//Synthetic comment -- @@ -564,4 +653,195 @@
actionBars.setGlobalActionHandler(mTreeSelectAllAction.getId(), mTreeSelectAllAction);
actionBars.updateActionBars();
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java
//Synthetic comment -- index b44deba..cad3301 100644

//Synthetic comment -- @@ -31,6 +31,8 @@
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.w3c.dom.Node;

import java.util.ArrayList;
//Synthetic comment -- @@ -677,6 +679,25 @@
return nodes.size() == newChildren.size();
}

private void updateMenuActions() {
boolean hasSelection = !mSelections.isEmpty();
mCanvas.updateMenuActionState(hasSelection);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java
//Synthetic comment -- index 92ad589..0bf89bc 100644

//Synthetic comment -- @@ -425,6 +425,27 @@
}

/**
* Returns the previous UI sibling of this UI node. If the node does not have a previous
* sibling, returns null.
*








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/manifest/model/UiElementNodeTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/manifest/model/UiElementNodeTest.java
//Synthetic comment -- index 37cc2be..30f709c 100644

//Synthetic comment -- @@ -160,12 +160,14 @@
assertEquals("application", application.getDescriptor().getXmlName());
assertEquals(0, application.getUiChildren().size());
assertEquals(0, application.getUiAttributes().size());

// get /manifest/permission
UiElementNode first_permission = ui_child_it.next();
assertEquals("permission", first_permission.getDescriptor().getXmlName());
assertEquals(0, first_permission.getUiChildren().size());
assertEquals(0, first_permission.getUiAttributes().size());
}

public void testLoadFrom_NewTree_N_Nodes() {







