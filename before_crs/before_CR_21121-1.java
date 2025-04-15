/*Improvements to Selection - New Actions & Sync Fix

This changeset adds a new "Select" context menu. In addition to Select
All and Select None, there are new actions to select the parent of the
currently selected item, an action to select all its siblings, and an
action to select all widgets in the layout of the same type. For
example, invoking this on a button will select all buttons in the
layout. Select Parent is bound to Escape and is particularly useful
when you want to target a layout widget that has children and no free
space, since any mouse click will target one of its children. With
Select Parent you click on the child and hit Escape to reach the
container.

In addition, this changeset fixes selection synchronization for
context menus. Until now, you had to FIRST select an item, THEN right
click on it to see its context menu items. The root cause for this is
an SWT bug (eclipse issue 26605), but we can work around it with a
MenuDetectListener, which is run when the menu is posted and gives us
a chance to sync the selection.

Change-Id:If3e15c335c372a6ee8a3c8c357b48bb80fbbb40c*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ControlPoint.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ControlPoint.java
//Synthetic comment -- index e90371f..ba09220 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;

//Synthetic comment -- @@ -57,6 +58,20 @@
}

/**
* Constructs a new {@link ControlPoint} from the given event. The event
* must be from a {@link DragSourceListener} associated with the
* {@link LayoutCanvas} such that the {@link DragSourceEvent#x} and








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 289831f..8e53831 100755

//Synthetic comment -- @@ -64,6 +64,8 @@
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
//Synthetic comment -- @@ -286,6 +288,8 @@
// handle backspace for other platforms as well.
if (e.keyCode == SWT.BS) {
mDeleteAction.run();
} else {
// Zooming actions
char c = e.character;
//Synthetic comment -- @@ -519,6 +523,11 @@
return mClipboardSupport;
}

/**
* Sets the result of the layout rendering. The result object indicates if the layout
* rendering succeeded. If it did, it contains a bitmap and the objects rectangles.
//Synthetic comment -- @@ -1158,6 +1167,16 @@
new DynamicContextMenu(mLayoutEditor, this, mMenuManager);
Menu menu = mMenuManager.createContextMenu(this);
setMenu(menu);
}

/**
//Synthetic comment -- @@ -1173,15 +1192,13 @@
private void setupStaticMenuActions(IMenuManager manager) {
manager.removeAll();

manager.add(mCutAction);
manager.add(mCopyAction);
manager.add(mPasteAction);

manager.add(new Separator());

manager.add(mDeleteAction);
        manager.add(mSelectAllAction);

manager.add(new Separator());
manager.add(new PlayAnimationMenu(this));
manager.add(new Separator());








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java
//Synthetic comment -- index 02f98b1..21dff30 100755

//Synthetic comment -- @@ -21,7 +21,6 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;
import static com.android.ide.common.layout.LayoutConstants.DRAWABLE_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.LAYOUT_PREFIX;

import static org.eclipse.jface.viewers.StyledString.QUALIFIER_STYLER;

import com.android.annotations.VisibleForTesting;
//Synthetic comment -- @@ -589,6 +588,8 @@
mMenuManager.add(mMoveDownAction);
mMenuManager.add(new Separator());

final String prefix = LayoutCanvas.PREFIX_CANVAS_ACTION;
mMenuManager.add(new DelegateAction(prefix + ActionFactory.CUT.getId()));
mMenuManager.add(new DelegateAction(prefix + ActionFactory.COPY.getId()));
//Synthetic comment -- @@ -597,7 +598,6 @@
mMenuManager.add(new Separator());

mMenuManager.add(new DelegateAction(prefix + ActionFactory.DELETE.getId()));
        mMenuManager.add(new DelegateAction(prefix + ActionFactory.SELECT_ALL.getId()));

mMenuManager.addMenuListener(new IMenuListener() {
public void menuAboutToShow(IMenuManager manager) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java
//Synthetic comment -- index 1aad0f2..4f9a277 100644

//Synthetic comment -- @@ -16,11 +16,17 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.api.INode;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.sdklib.SdkConstants;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
//Synthetic comment -- @@ -30,8 +36,10 @@
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPartSite;
import org.w3c.dom.Node;

//Synthetic comment -- @@ -240,6 +248,34 @@
}

/**
* Performs selection for a mouse event.
* <p/>
* Shift key (or Command on the Mac) is used to toggle in multi-selection.
//Synthetic comment -- @@ -504,11 +540,71 @@
mSelections.add(createSelection(vi));
}


fireSelectionChanged();
redraw();
}

/**
* Returns true if and only if there is currently more than one selected
* item.
//Synthetic comment -- @@ -601,11 +697,15 @@
}
});

            // Update menu actions that depend on the selection
            updateMenuActions();

            // Update the layout actions bar
            mCanvas.getLayoutEditor().getGraphicalEditor().getLayoutActionBar().updateSelection();
} finally {
mInsideUpdateSelection = false;
}
//Synthetic comment -- @@ -713,4 +813,111 @@
return new SelectionItem(vi, mCanvas.getRulesEngine(),
mCanvas.getNodeFactory());
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfoTest.java
//Synthetic comment -- index a240f90..494346a 100644

//Synthetic comment -- @@ -40,7 +40,7 @@

public class CanvasViewInfoTest extends TestCase {

    private static ViewElementDescriptor createDesc(String name, String fqn, boolean hasChildren) {
if (hasChildren) {
return new ViewElementDescriptor(name, name, fqn, "", "", new AttributeDescriptor[0],
new AttributeDescriptor[0], new ElementDescriptor[1], false);
//Synthetic comment -- @@ -49,7 +49,7 @@
}
}

    private static UiViewElementNode createNode(UiViewElementNode parent, String fqn,
boolean hasChildren) {
String name = fqn.substring(fqn.lastIndexOf('.') + 1);
ViewElementDescriptor descriptor = createDesc(name, fqn, hasChildren);
//Synthetic comment -- @@ -60,7 +60,7 @@
return (UiViewElementNode) parent.appendNewUiChild(descriptor);
}

    private static UiViewElementNode createNode(String fqn, boolean hasChildren) {
return createNode(null, fqn, hasChildren);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManagerTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManagerTest.java
new file mode 100644
//Synthetic comment -- index 0000000..db18762

//Synthetic comment -- @@ -0,0 +1,120 @@







