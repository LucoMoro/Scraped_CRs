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
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;

//Synthetic comment -- @@ -57,6 +58,20 @@
}

/**
     * Constructs a new {@link ControlPoint} from the given menu detect event.
     *
     * @param canvas The {@link LayoutCanvas} this point is within.
     * @param event The menu detect event to construct the {@link ControlPoint} from.
     * @return A {@link ControlPoint} which corresponds to the given
     *         {@link MenuDetectEvent}.
     */
    public static ControlPoint create(LayoutCanvas canvas, MenuDetectEvent event) {
        // The menu detect events are always display-relative.
        org.eclipse.swt.graphics.Point p = canvas.toControl(event.x, event.y);
        return new ControlPoint(canvas, p.x, p.y);
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
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
//Synthetic comment -- @@ -286,6 +288,8 @@
// handle backspace for other platforms as well.
if (e.keyCode == SWT.BS) {
mDeleteAction.run();
                } else if (e.keyCode == SWT.ESC) {
                    mSelectionManager.selectParent();
} else {
// Zooming actions
char c = e.character;
//Synthetic comment -- @@ -519,6 +523,11 @@
return mClipboardSupport;
}

    /** Returns the Select All action bound to this canvas */
    Action getSelectAllAction() {
        return mSelectAllAction;
    }

/**
* Sets the result of the layout rendering. The result object indicates if the layout
* rendering succeeded. If it did, it contains a bitmap and the objects rectangles.
//Synthetic comment -- @@ -1158,6 +1167,16 @@
new DynamicContextMenu(mLayoutEditor, this, mMenuManager);
Menu menu = mMenuManager.createContextMenu(this);
setMenu(menu);

        // Add listener to detect when the menu is about to be posted, such that
        // we can sync the selection. Without this, you can right click on something
        // in the canvas which is NOT selected, and the context menu will show items related
        // to the selection, NOT the item you clicked on!!
        addMenuDetectListener(new MenuDetectListener() {
            public void menuDetected(MenuDetectEvent e) {
                mSelectionManager.menuClick(e);
            }
        });
}

/**
//Synthetic comment -- @@ -1173,15 +1192,13 @@
private void setupStaticMenuActions(IMenuManager manager) {
manager.removeAll();

        manager.add(new SelectionManager.SelectionMenu(mLayoutEditor.getGraphicalEditor()));
        manager.add(new Separator());
manager.add(mCutAction);
manager.add(mCopyAction);
manager.add(mPasteAction);
manager.add(new Separator());
manager.add(mDeleteAction);
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

        mMenuManager.add(new SelectionManager.SelectionMenu(mGraphicalEditorPart));
        mMenuManager.add(new Separator());
final String prefix = LayoutCanvas.PREFIX_CANVAS_ACTION;
mMenuManager.add(new DelegateAction(prefix + ActionFactory.CUT.getId()));
mMenuManager.add(new DelegateAction(prefix + ActionFactory.COPY.getId()));
//Synthetic comment -- @@ -597,7 +598,6 @@
mMenuManager.add(new Separator());

mMenuManager.add(new DelegateAction(prefix + ActionFactory.DELETE.getId()));

mMenuManager.addMenuListener(new IMenuListener() {
public void menuAboutToShow(IMenuManager manager) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java
//Synthetic comment -- index 1aad0f2..4f9a277 100644

//Synthetic comment -- @@ -16,11 +16,17 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.api.INode;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.sdklib.SdkConstants;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
//Synthetic comment -- @@ -30,8 +36,10 @@
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchPartSite;
import org.w3c.dom.Node;

//Synthetic comment -- @@ -240,6 +248,34 @@
}

/**
     * The menu has been activated; ensure that the menu click is over the existing
     * selection, and if not, update the selection.
     *
     * @param e the {@link MenuDetectEvent} which triggered the menu
     */
    public void menuClick(MenuDetectEvent e) {
        LayoutPoint p = ControlPoint.create(mCanvas, e).toLayout();

        // Right click button is used to display a context menu.
        // If there's an existing selection and the click is anywhere in this selection
        // and there are no modifiers being used, we don't want to change the selection.
        // Otherwise we select the item under the cursor.

        for (SelectionItem cs : mSelections) {
            if (cs.isRoot()) {
                continue;
            }
            if (cs.getRect().contains(p.x, p.y)) {
                // The cursor is inside the selection. Don't change anything.
                return;
            }
        }

        CanvasViewInfo vi = mCanvas.getViewHierarchy().findViewInfoAt(p);
        selectSingle(vi);
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

    public void selectNone() {
        mSelections.clear();
        mAltSelection = null;
        fireSelectionChanged();
        redraw();
    }

    /** Selects the parent of the current selection */
    public void selectParent() {
        if (mSelections.size() == 1) {
            CanvasViewInfo parent = mSelections.get(0).getViewInfo().getParent();
            if (parent != null) {
                selectSingle(parent);
            }
        }
    }

    /** Finds all widgets in the layout that have the same type as the primary */
    public void selectSameType() {
        // Find all
        if (mSelections.size() == 1) {
            mSelections.clear();
            mAltSelection = null;
            addSameType(mCanvas.getViewHierarchy().getRoot(),
                    mSelections.get(0).getViewInfo().getUiViewNode().getDescriptor());
            fireSelectionChanged();
            redraw();
        }
    }

    /** Helper for {@link #selectSameType} */
    private void addSameType(CanvasViewInfo root, ElementDescriptor descriptor) {
        if (root.getUiViewNode().getDescriptor() == descriptor) {
            mSelections.add(createSelection(root));
        }

        for (CanvasViewInfo child : root.getChildren()) {
            addSameType(child, descriptor);
        }
    }

    /** Selects the siblings of the primary */
    public void selectSiblings() {
        // Find all
        if (mSelections.size() == 1) {
            CanvasViewInfo vi = mSelections.get(0).getViewInfo();
            mSelections.clear();
            mAltSelection = null;
            CanvasViewInfo parent = vi.getParent();
            if (parent == null) {
                selectNone();
            } else {
                for (CanvasViewInfo child : parent.getChildren()) {
                    mSelections.add(createSelection(child));
                }
                fireSelectionChanged();
                redraw();
            }
        }
    }

/**
* Returns true if and only if there is currently more than one selected
* item.
//Synthetic comment -- @@ -601,11 +697,15 @@
}
});

            LayoutEditor editor = mCanvas.getLayoutEditor();
            if (editor != null) {
                // Update menu actions that depend on the selection
                updateMenuActions();

                // Update the layout actions bar
                LayoutActionBar layoutActionBar = editor.getGraphicalEditor().getLayoutActionBar();
                layoutActionBar.updateSelection();
            }
} finally {
mInsideUpdateSelection = false;
}
//Synthetic comment -- @@ -713,4 +813,111 @@
return new SelectionItem(vi, mCanvas.getRulesEngine(),
mCanvas.getNodeFactory());
}

    /**
     * Returns true if there is nothing selected
     *
     * @return true if there is nothing selected
     */
    public boolean isEmpty() {
        return mSelections.size() == 0;
    }

    /**
     * "Select" context menu which lists various menu options related to selection:
     * <ul>
     * <li> Select All
     * <li> Select Parent
     * <li> Select None
     * <li> Select Siblings
     * <li> Select Same Type
     * </ul>
     * etc.
     */
    public static class SelectionMenu extends SubmenuAction {
        private final GraphicalEditorPart mEditor;

        public SelectionMenu(GraphicalEditorPart editor) {
            super("Select");
            mEditor = editor;
        }

        @Override
        public String getId() {
            return "-selectionmenu"; //$NON-NLS-1$
        }

        @Override
        protected void addMenuItems(Menu menu) {
            LayoutCanvas canvas = mEditor.getCanvasControl();
            SelectionManager selectionManager = canvas.getSelectionManager();
            List<SelectionItem> selections = selectionManager.getSelections();
            boolean selectedOne = selections.size() == 1;
            boolean notRoot = selectedOne && !selections.get(0).isRoot();
            boolean haveSelection = selections.size() > 0;

            Action a;
            a = selectionManager.new SelectAction("Select Parent", SELECT_PARENT);
            new ActionContributionItem(a).fill(menu, -1);
            a.setEnabled(notRoot);
            a.setAccelerator(SWT.ESC);

            a = selectionManager.new SelectAction("Select Siblings", SELECT_SIBLINGS);
            new ActionContributionItem(a).fill(menu, -1);
            a.setEnabled(notRoot);

            a = selectionManager.new SelectAction("Select Same Type", SELECT_SAME_TYPE);
            new ActionContributionItem(a).fill(menu, -1);
            a.setEnabled(selectedOne);

            new Separator().fill(menu, -1);

            // Special case for Select All: Use global action
            a = canvas.getSelectAllAction();
            new ActionContributionItem(a).fill(menu, -1);
            a.setEnabled(true);

            a = selectionManager.new SelectAction("Select None", SELECT_NONE);
            new ActionContributionItem(a).fill(menu, -1);
            a.setEnabled(haveSelection);
        }
    }

    private static final int SELECT_PARENT = 1;
    private static final int SELECT_SIBLINGS = 2;
    private static final int SELECT_SAME_TYPE = 3;
    private static final int SELECT_NONE = 4; // SELECT_ALL is handled separately

    private class SelectAction extends Action {
        private final int mType;

        public SelectAction(String title, int type) {
            super(title, IAction.AS_PUSH_BUTTON);
            mType = type;
        }

        @Override
        public void run() {
            switch (mType) {
                case SELECT_NONE:
                    selectNone();
                    break;
                case SELECT_PARENT:
                    selectParent();
                    break;
                case SELECT_SAME_TYPE:
                    selectSameType();
                    break;
                case SELECT_SIBLINGS:
                    selectSiblings();
                    break;
            }

            List<INode> nodes = new ArrayList<INode>();
            for (SelectionItem item : getSelections()) {
                nodes.add(item.getNode());
            }
            updateOutlineSelection(nodes);
        }
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfoTest.java
//Synthetic comment -- index a240f90..494346a 100644

//Synthetic comment -- @@ -40,7 +40,7 @@

public class CanvasViewInfoTest extends TestCase {

    public static ViewElementDescriptor createDesc(String name, String fqn, boolean hasChildren) {
if (hasChildren) {
return new ViewElementDescriptor(name, name, fqn, "", "", new AttributeDescriptor[0],
new AttributeDescriptor[0], new ElementDescriptor[1], false);
//Synthetic comment -- @@ -49,7 +49,7 @@
}
}

    public static UiViewElementNode createNode(UiViewElementNode parent, String fqn,
boolean hasChildren) {
String name = fqn.substring(fqn.lastIndexOf('.') + 1);
ViewElementDescriptor descriptor = createDesc(name, fqn, hasChildren);
//Synthetic comment -- @@ -60,7 +60,7 @@
return (UiViewElementNode) parent.appendNewUiChild(descriptor);
}

    public static UiViewElementNode createNode(String fqn, boolean hasChildren) {
return createNode(null, fqn, hasChildren);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManagerTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManagerTest.java
new file mode 100644
//Synthetic comment -- index 0000000..db18762

//Synthetic comment -- @@ -0,0 +1,120 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;

import org.eclipse.swt.widgets.Shell;

import java.util.Arrays;

import junit.framework.TestCase;

public class SelectionManagerTest extends TestCase {
    private SelectionManager createManager() {
        LayoutCanvas canvas = new LayoutCanvas(null, null, new Shell(), 0);
        return new SelectionManager(canvas);
    }

    public void testEmpty() {
        SelectionManager manager = createManager();

        assertNotNull(manager.getSelections());
        assertEquals(0, manager.getSelections().size());
        assertFalse(manager.hasMultiSelection());
        assertTrue(manager.isEmpty());
    }

    public void testBasic() {
        SelectionManager manager = createManager();
        assertTrue(manager.isEmpty());

        UiViewElementNode rootNode = CanvasViewInfoTest.createNode("android.widget.LinearLayout",
                true);
        ViewInfo root = new ViewInfo("LinearLayout", rootNode, 10, 10, 100, 100);
        UiViewElementNode child1Node = CanvasViewInfoTest.createNode(rootNode,
                "android.widget.Button", false);
        ViewInfo child1 = new ViewInfo("Button1", child1Node, 0, 0, 50, 20);
        UiViewElementNode child2Node = CanvasViewInfoTest.createNode(rootNode,
                "android.widget.Button", false);
        ViewInfo child2 = new ViewInfo("Button2", child2Node, 0, 20, 70, 25);
        root.setChildren(Arrays.asList(child1, child2));
        CanvasViewInfo rootView = CanvasViewInfo.create(root).getFirst();
        assertNotNull(rootView);

        manager.selectMultiple(Arrays.asList(rootView, rootView.getChildren().get(0), rootView
                .getChildren().get(1)));
        assertEquals(3, manager.getSelections().size());
        assertFalse(manager.isEmpty());
        assertTrue(manager.hasMultiSelection());

        // Expect read-only result; ensure that's the case
        try {
            manager.getSelections().remove(0);
            fail("Result should be read only collection");
        } catch (Exception e) {
            ; //ok, what we expected
        }

        manager.selectNone();
        assertEquals(0, manager.getSelections().size());
        assertTrue(manager.isEmpty());

        manager.selectSingle(rootView);
        assertEquals(1, manager.getSelections().size());
        assertFalse(manager.isEmpty());
        assertSame(rootView, manager.getSelections().get(0).getViewInfo());

        manager.selectMultiple(Arrays.asList(rootView, rootView.getChildren().get(0), rootView
                .getChildren().get(1)));
        assertEquals(3, manager.getSelections().size());

        manager.deselect(rootView.getChildren().get(0));
        assertEquals(2, manager.getSelections().size());
        manager.deselect(rootView);
        assertEquals(1, manager.getSelections().size());
        assertSame(rootView.getChildren().get(1), manager.getSelections().get(0).getViewInfo());
    }

    public void testSelectParent() {
        SelectionManager manager = createManager();
        assertTrue(manager.isEmpty());

        UiViewElementNode rootNode = CanvasViewInfoTest.createNode("android.widget.LinearLayout",
                true);
        ViewInfo root = new ViewInfo("LinearLayout", rootNode, 10, 10, 100, 100);
        UiViewElementNode child1Node = CanvasViewInfoTest.createNode(rootNode,
                "android.widget.Button", false);
        ViewInfo child1 = new ViewInfo("Button1", child1Node, 0, 0, 50, 20);
        UiViewElementNode child2Node = CanvasViewInfoTest.createNode(rootNode,
                "android.widget.Button", false);
        ViewInfo child2 = new ViewInfo("Button2", child2Node, 0, 20, 70, 25);
        root.setChildren(Arrays.asList(child1, child2));
        CanvasViewInfo rootView = CanvasViewInfo.create(root).getFirst();
        assertNotNull(rootView);

        manager.selectMultiple(Arrays.asList(rootView.getChildren().get(0)));
        assertEquals(1, manager.getSelections().size());
        assertFalse(manager.isEmpty());
        assertSame(rootView.getChildren().get(0), manager.getSelections().get(0).getViewInfo());

        manager.selectParent();
        assertEquals(1, manager.getSelections().size());
        assertFalse(manager.isEmpty());
        assertSame(rootView, manager.getSelections().get(0).getViewInfo());
    }
}







