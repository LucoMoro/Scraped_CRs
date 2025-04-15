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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
//Synthetic comment -- @@ -157,18 +156,7 @@
}
// Select the newly dropped nodes
final SelectionManager selectionManager = canvas.getSelectionManager();
        selectionManager.updateOutlineSelection(added);

canvas.redraw();









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java
//Synthetic comment -- index 33032de..b16f0e5 100755

//Synthetic comment -- @@ -16,13 +16,21 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static org.eclipse.jface.action.IAction.AS_PUSH_BUTTON;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.InsertType;
import com.android.ide.common.layout.BaseLayoutRule;
import com.android.ide.common.layout.Pair;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder.Reference;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.ui.ErrorImageComposite;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.sdklib.annotations.VisibleForTesting;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
//Synthetic comment -- @@ -48,8 +56,14 @@
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.IPageLayout;
//Synthetic comment -- @@ -61,6 +75,9 @@
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* An outline page for the layout canvas view.
//Synthetic comment -- @@ -83,7 +100,7 @@
private final GraphicalEditorPart mGraphicalEditorPart;

/**
     * RootWrapper is a workaround: we can't set the input of the TreeView to its root
* element, so we introduce a fake parent.
*/
private final RootWrapper mRootWrapper = new RootWrapper();
//Synthetic comment -- @@ -108,6 +125,44 @@
}
};

    /** Action for moving items up in the tree */
    private Action mMoveUpAction = new Action("Move Up\t-", AS_PUSH_BUTTON) {

        @Override
        public String getId() {
            return "adt.outline.moveup"; //$NON-NLS-1$
        }

        @Override
        public boolean isEnabled() {
            return canMove(false);
        }

        @Override
        public void run() {
            move(false);
        }
    };

    /** Action for moving items down in the tree */
    private Action mMoveDownAction = new Action("Move Down\t+", AS_PUSH_BUTTON) {

        @Override
        public String getId() {
            return "adt.outline.movedown"; //$NON-NLS-1$
        }

        @Override
        public boolean isEnabled() {
            return canMove(true);
        }

        @Override
        public void run() {
            move(true);
        }
    };

public OutlinePage(GraphicalEditorPart graphicalEditorPart) {
super();
mGraphicalEditorPart = graphicalEditorPart;
//Synthetic comment -- @@ -184,6 +239,25 @@
dispose();
}
});

        Tree tree = tv.getTree();
        tree.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
                if (e.character == '-') {
                    if (mMoveUpAction.isEnabled()) {
                        mMoveUpAction.run();
                    }
                } else if (e.character == '+') {
                    if (mMoveDownAction.isEnabled()) {
                        mMoveDownAction.run();
                    }
                }
            }

            public void keyReleased(KeyEvent e) {
            }
        });
}

@Override
//Synthetic comment -- @@ -442,6 +516,10 @@
mMenuManager = new MenuManager();
mMenuManager.removeAll();

        mMenuManager.add(mMoveUpAction);
        mMenuManager.add(mMoveDownAction);
        mMenuManager.add(new Separator());

final String prefix = LayoutCanvas.PREFIX_CANVAS_ACTION;
mMenuManager.add(new DelegateAction(prefix + ActionFactory.CUT.getId()));
mMenuManager.add(new DelegateAction(prefix + ActionFactory.COPY.getId()));
//Synthetic comment -- @@ -472,6 +550,13 @@
mMenuManager);

getControl().setMenu(mMenuManager.createContextMenu(getControl()));

        // Update Move Up/Move Down state only when the menu is opened
        getControl().addMenuDetectListener(new MenuDetectListener() {
            public void menuDetected(MenuDetectEvent e) {
                mMenuManager.update(IAction.ENABLED);
            }
        });
}

/**
//Synthetic comment -- @@ -518,7 +603,11 @@
super.run();
}

        /**
         * Updates this action to delegate to its counterpart in the given editor part
         *
         * @param editorPart The editor being updated
         */
public void updateFromEditorPart(GraphicalEditorPart editorPart) {
LayoutCanvas canvas = editorPart == null ? null : editorPart.getCanvasControl();
if (canvas == null) {
//Synthetic comment -- @@ -564,4 +653,195 @@
actionBars.setGlobalActionHandler(mTreeSelectAllAction.getId(), mTreeSelectAllAction);
actionBars.updateActionBars();
}

    // ---- Move Up/Down Support ----

    /** Returns true if the current selected item can be moved */
    private boolean canMove(boolean forward) {
        CanvasViewInfo viewInfo = getSingleSelectedItem();
        if (viewInfo != null) {
            UiViewElementNode node = viewInfo.getUiViewNode();
            if (forward) {
                return findNext(node) != null;
            } else {
                return findPrevious(node) != null;
            }
        }

        return false;
    }

    /** Moves the current selected item down (forward) or up (not forward) */
    private void move(boolean forward) {
        CanvasViewInfo viewInfo = getSingleSelectedItem();
        if (viewInfo != null) {
            final Pair<UiViewElementNode, Integer> target;
            UiViewElementNode selected = viewInfo.getUiViewNode();
            if (forward) {
                target = findNext(selected);
            } else {
                target = findPrevious(selected);
            }
            if (target != null) {
                final LayoutCanvas canvas = mGraphicalEditorPart.getCanvasControl();
                final SelectionManager selectionManager = canvas.getSelectionManager();
                final ArrayList<SelectionItem> dragSelection = new ArrayList<SelectionItem>();
                dragSelection.add(selectionManager.createSelection(viewInfo));
                SelectionManager.sanitize(dragSelection);

                if (!dragSelection.isEmpty()) {
                    final SimpleElement[] elements = SelectionItem.getAsElements(dragSelection);
                    UiViewElementNode parentNode = target.getFirst();
                    final NodeProxy targetNode = canvas.getNodeFactory().create(parentNode);

                    // Record children of the target right before the drop (such that we
                    // can find out after the drop which exact children were inserted)
                    Set<INode> children = new HashSet<INode>();
                    for (INode node : targetNode.getChildren()) {
                        children.add(node);
                    }

                    String label = MoveGesture.computeUndoLabel(targetNode,
                            elements, DND.DROP_MOVE);
                    canvas.getLayoutEditor().wrapUndoEditXmlModel(label, new Runnable() {
                        public void run() {
                            canvas.getRulesEngine().setInsertType(InsertType.MOVE);
                            int index = target.getSecond();
                            BaseLayoutRule.insertAt(targetNode, elements, false, index);
                            canvas.getClipboardSupport().deleteSelection("Remove", dragSelection);
                        }
                    });

                    // Now find out which nodes were added, and look up their
                    // corresponding CanvasViewInfos
                    final List<INode> added = new ArrayList<INode>();
                    for (INode node : targetNode.getChildren()) {
                        if (!children.contains(node)) {
                            added.add(node);
                        }
                    }

                    selectionManager.updateOutlineSelection(added);
                }
            }
        }
    }

    /**
     * Returns the {@link CanvasViewInfo} for the currently selected item, or null if
     * there are no or multiple selected items
     *
     * @return the current selected item if there is exactly one item selected
     */
    private CanvasViewInfo getSingleSelectedItem() {
        TreeItem[] selection = getTreeViewer().getTree().getSelection();
        if (selection.length == 1) {
            return getViewInfo(selection[0].getData());
        }

        return null;
    }


    /** Returns the pair [parent,index] of the next node (when iterating forward) */
    @VisibleForTesting
    /* package */ static Pair<UiViewElementNode, Integer> findNext(UiViewElementNode node) {
        UiElementNode parent = node.getUiParent();
        if (parent == null) {
            return null;
        }

        UiElementNode next = node.getUiNextSibling();
        if (next != null) {
            if (next.getDescriptor().hasChildren()) {
                return getFirstPosition(next);
            } else {
                return getPositionAfter(next);
            }
        }

        next = parent.getUiNextSibling();
        if (next != null) {
            return getPositionBefore(next);
        } else {
            UiElementNode grandParent = parent.getUiParent();
            if (grandParent != null) {
                return getLastPosition(grandParent);
            }
        }

        return null;
    }

    /** Returns the pair [parent,index] of the previous node (when iterating backward) */
    @VisibleForTesting
    /* package */ static Pair<UiViewElementNode, Integer> findPrevious(UiViewElementNode node) {
        UiElementNode prev = node.getUiPreviousSibling();
        if (prev != null) {
            UiElementNode curr = prev;
            while (true) {
                List<UiElementNode> children = curr.getUiChildren();
                if (children.size() > 0) {
                    curr = children.get(children.size() - 1);
                    continue;
                }
                if (curr.getDescriptor().hasChildren()) {
                    return getFirstPosition(curr);
                } else {
                    if (curr == prev) {
                        return getPositionBefore(curr);
                    } else {
                        return getPositionAfter(curr);
                    }
                }
            }
        }

        return getPositionBefore(node.getUiParent());
    }

    /** Returns the pair [parent,index] of the position immediately before the given node  */
    private static Pair<UiViewElementNode, Integer> getPositionBefore(UiElementNode node) {
        if (node != null) {
            UiElementNode parent = node.getUiParent();
            if (parent != null && parent instanceof UiViewElementNode) {
                return Pair.of((UiViewElementNode) parent, node.getUiSiblingIndex());
            }
        }

        return null;
    }

    /** Returns the pair [parent,index] of the position immediately following the given node  */
    private static Pair<UiViewElementNode, Integer> getPositionAfter(UiElementNode node) {
        if (node != null) {
            UiElementNode parent = node.getUiParent();
            if (parent != null && parent instanceof UiViewElementNode) {
                return Pair.of((UiViewElementNode) parent, node.getUiSiblingIndex() + 1);
            }
        }

        return null;
    }

    /** Returns the pair [parent,index] of the first position inside the given parent */
    private static Pair<UiViewElementNode, Integer> getFirstPosition(UiElementNode parent) {
        if (parent != null && parent instanceof UiViewElementNode) {
            return Pair.of((UiViewElementNode) parent, 0);
        }

        return null;
    }

    /**
     * Returns the pair [parent,index] of the last position after the given node's
     * children
     */
    private static Pair<UiViewElementNode, Integer> getLastPosition(UiElementNode parent) {
        if (parent != null && parent instanceof UiViewElementNode) {
            return Pair.of((UiViewElementNode) parent, parent.getUiChildren().size());
        }

        return null;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java
//Synthetic comment -- index b44deba..cad3301 100644

//Synthetic comment -- @@ -31,6 +31,8 @@
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPartSite;
import org.w3c.dom.Node;

import java.util.ArrayList;
//Synthetic comment -- @@ -677,6 +679,25 @@
return nodes.size() == newChildren.size();
}

    /**
     * Update the outline selection to select the given nodes, asynchronously.
     * @param nodes The nodes to be selected
     */
    public void updateOutlineSelection(final List<INode> nodes) {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                selectDropped(nodes);
                OutlinePage outlinePage = mCanvas.getOutlinePage();
                IWorkbenchPartSite site = outlinePage.getEditor().getSite();
                ISelectionProvider selectionProvider = site.getSelectionProvider();
                ISelection selection = selectionProvider.getSelection();
                if (selection != null) {
                    outlinePage.setSelection(selection);
                }
            }
        });
    }

private void updateMenuActions() {
boolean hasSelection = !mSelections.isEmpty();
mCanvas.updateMenuActionState(hasSelection);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java
//Synthetic comment -- index 92ad589..0bf89bc 100644

//Synthetic comment -- @@ -425,6 +425,27 @@
}

/**
     * Returns the index of this sibling (where the first child has index 0, the second child
     * has index 1, and so on.)
     *
     * @return The sibling index of this node
     */
    public int getUiSiblingIndex() {
        if (mUiParent != null) {
            int index = 0;
            for (UiElementNode node : mUiParent.getUiChildren()) {
                if (node == this) {
                    break;
                }
                index++;
            }
            return index;
        }

        return 0;
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
        assertEquals(0, application.getUiSiblingIndex());

// get /manifest/permission
UiElementNode first_permission = ui_child_it.next();
assertEquals("permission", first_permission.getDescriptor().getXmlName());
assertEquals(0, first_permission.getUiChildren().size());
assertEquals(0, first_permission.getUiAttributes().size());
        assertEquals(1, first_permission.getUiSiblingIndex());
}

public void testLoadFrom_NewTree_N_Nodes() {







