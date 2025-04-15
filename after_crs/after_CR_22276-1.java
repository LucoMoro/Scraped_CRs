/*Usability fix for the layout actions bar

The layout actions bar shows actions of two types:

* Actions which edit attriubutes of the "current layout"; typically
  the parent of the currently selected views.  For example, the
  "orientation" or "baseline" attributes of a LinearLayout.

* Actions which edit the layout parameters of the selected views. For
  example, the "weight" attribute of children in a LinearLayout.

One thing which was missing is adding in layout actions for views that
are children. For example, the TableView now has an "Insert Row"
action.  If you select the table itself, rather than a child within
the table, you would not see the Insert Row action. Similarly, if you
drop a new LinearLayout, you cannot toggle its orientation attribute;
it won't be shown, or if it is within another LinearLayout, you will
see an orientation toggle but it controls the parent, not the newly
selected LinearLayout.

This changeset addresses this by adding a new section of actions on
the right hand side of the actions bar, which contains the layout
actions which apply to the selection, regardless of the parent type.

For example, if you have a LinearLayout containing a TableLayout, and
you have selected the TableLayout, you will first see the LinearLayout
actions, then the LinearLayout layoutparams actions (which will be
applied to the TableLayout), and finally the TableLayout layout
actions (insert and remove row).

This changeset also improves the TableLayout insert row action to
insert the row before the current selected row (if any) rather than
unconditionally appending it to the end. It also selects the table
after creation to make it more obvious where it was added.  The new
ability to select nodes from layout rules is also used in a couple of
other places.

Change-Id:I7cd8f75e61fc916bc75ed5ad156440f0f8cbd786*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/api/IClientRulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/api/IClientRulesEngine.java
//Synthetic comment -- index e849340..6aa4776 100755

//Synthetic comment -- @@ -19,6 +19,8 @@

import com.android.annotations.Nullable;

import java.util.Collection;

/**
* A Client Rules Engine is a set of methods that {@link IViewRule}s can use to
* access the client public API of the Rules Engine. Rules can access it via
//Synthetic comment -- @@ -143,5 +145,12 @@
* @return the layout resource to include
*/
String displayIncludeSourceInput();

    /**
     * Select the given nodes
     *
     * @param nodes the nodes to be selected, never null
     */
    void select(Collection<INode> nodes);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java
//Synthetic comment -- index 1571e03..96e8d7f 100644

//Synthetic comment -- @@ -659,7 +659,7 @@
public void onChildInserted(INode node, INode parent, InsertType insertType) {
}

    public static String stripIdPrefix(String id) {
if (id.startsWith(NEW_ID_PREFIX)) {
id = id.substring(NEW_ID_PREFIX.length());
} else if (id.startsWith(ID_PREFIX)) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/FrameLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/FrameLayoutRule.java
//Synthetic comment -- index af001c6..83a93be 100755

//Synthetic comment -- @@ -161,7 +161,7 @@
super.addLayoutActions(actions, parentNode, children);
actions.add(MenuAction.createSeparator(25));
actions.add(createMarginAction(parentNode, children));
        if (children != null && children.size() > 0) {
actions.add(createGravityAction(children, ATTR_LAYOUT_GRAVITY));
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TableLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TableLayoutRule.java
//Synthetic comment -- index c3b3bfa..cc67d3a 100644

//Synthetic comment -- @@ -17,6 +17,7 @@

import static com.android.ide.common.layout.LayoutConstants.FQCN_TABLE_ROW;

import com.android.ide.common.api.IClientRulesEngine;
import com.android.ide.common.api.IMenuCallback;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.INodeHandler;
//Synthetic comment -- @@ -25,6 +26,7 @@
import com.android.ide.common.api.MenuAction;

import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
//Synthetic comment -- @@ -69,8 +71,8 @@
IMenuCallback addTab = new IMenuCallback() {
public void action(MenuAction action, final String valueId, Boolean newValue) {
final INode node = selectedNode;
                INode newRow = node.appendChild(FQCN_TABLE_ROW);
                mRulesEngine.select(Collections.singletonList(newRow));
}
};
return concatenate(super.getContextMenu(selectedNode),
//Synthetic comment -- @@ -82,13 +84,14 @@
public void addLayoutActions(List<MenuAction> actions, final INode parentNode,
final List<? extends INode> children) {
super.addLayoutActions(actions, parentNode, children);
        addTableLayoutActions(mRulesEngine, actions, parentNode, children);
}

/**
* Adds layout actions to add and remove toolbar items
*/
    static void addTableLayoutActions(final IClientRulesEngine rulesEngine,
            List<MenuAction> actions, final INode parentNode,
final List<? extends INode> children) {
IMenuCallback actionCallback = new IMenuCallback() {
public void action(final MenuAction action, final String valueId,
//Synthetic comment -- @@ -96,7 +99,36 @@
parentNode.editXml("Add/Remove Table Row", new INodeHandler() {
public void handle(INode n) {
if (action.getId().equals(ACTION_ADD_ROW)) {
                            // Determine the index of the selection, if any; if there is
                            // a selection, insert the row before the current row, otherwise
                            // append it to the table.
                            int index = -1;
                            INode[] rows = parentNode.getChildren();
                            if (children != null) {
                                findTableIndex:
                                for (INode child : children) {
                                    // Find direct child of table layout
                                    while (child != null && child.getParent() != parentNode) {
                                        child = child.getParent();
                                    }
                                    if (child != null) {
                                        // Compute index of direct child of table layout
                                        for (int i = 0; i < rows.length; i++) {
                                            if (rows[i] == child) {
                                                index = i;
                                                break findTableIndex;
                                            }
                                        }
                                    }
                                }
                            }
                            INode newRow;
                            if (index == -1) {
                                newRow = parentNode.appendChild(FQCN_TABLE_ROW);
                            } else {
                                newRow = parentNode.insertChildAt(FQCN_TABLE_ROW, index);
                            }
                            rulesEngine.select(Collections.singletonList(newRow));
} else if (action.getId().equals(ACTION_REMOVE_ROW)) {
// Find the direct children of the TableLayout to delete;
// this is necessary since TableRow might also use








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TableRowRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TableRowRule.java
//Synthetic comment -- index 13e648e..ac03653 100644

//Synthetic comment -- @@ -53,9 +53,12 @@
// Also apply table-specific actions on the table row such that you can
// select something in a table row and still get offered actions on the surrounding
// table.
        if (children != null) {
            INode grandParent = parentNode.getParent();
            if (grandParent != null && grandParent.getFqcn().equals(FQCN_TABLE_LAYOUT)) {
                TableLayoutRule.addTableLayoutActions(mRulesEngine, actions, grandParent,
                        children);
            }
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutActionBar.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutActionBar.java
//Synthetic comment -- index 313064a..0dcd83e 100644

//Synthetic comment -- @@ -15,10 +15,14 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;

import com.android.ide.common.api.MenuAction;
import com.android.ide.common.api.MenuAction.OrderedChoices;
import com.android.ide.common.api.MenuAction.Separator;
import com.android.ide.common.api.MenuAction.Toggle;
import com.android.ide.common.layout.BaseViewRule;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite;
//Synthetic comment -- @@ -116,25 +120,58 @@
}
List<MenuAction> actions = new ArrayList<MenuAction>();
engine.callAddLayoutActions(actions, parent, selectedNodes);

        // Place actions in the correct order (the actions may come from different
        // rules and should be merged properly via sorting keys)
        Collections.sort(actions);

        // Add in actions for the child as well, if there is exactly one.
        // These are not merged into the parent list of actions; they are appended
        // at the end.
        int index = -1;
        String label = null;
        if (selectedNodes.size() == 1) {
            List<MenuAction> itemActions = new ArrayList<MenuAction>();
            NodeProxy selectedNode = selectedNodes.get(0);
            engine.callAddLayoutActions(itemActions, selectedNode, null);
            if (itemActions.size() > 0) {
                Collections.sort(itemActions);

                if (!(itemActions.get(0) instanceof MenuAction.Separator)) {
                    actions.add(MenuAction.createSeparator(0));
                }
                label = selectedNode.getStringAttr(ANDROID_URI, ATTR_ID);
                if (label != null) {
                    label = BaseViewRule.stripIdPrefix(label);
                    index = actions.size();
                }
                actions.addAll(itemActions);
            }
        }

        addActions(actions, index, label);

mLayoutToolBar.pack();
mLayoutToolBar.layout();
}

    private void addActions(List<MenuAction> actions, int labelIndex, String label) {
if (actions.size() > 0) {
// Flag used to indicate that if there are any actions -after- this, it
// should be separated from this current action (we don't unconditionally
// add a separator at the end of these groups in case there are no more
// actions at the end so that we don't have a trailing separator)
boolean needSeparator = false;

            int index = 0;
            for (MenuAction action : actions) {
                if (index == labelIndex) {
                    final ToolItem button = new ToolItem(mLayoutToolBar, SWT.PUSH);
                    button.setText(label);
                    needSeparator = false;
                }
                index++;

if (action instanceof Separator) {
addSeparator(mLayoutToolBar);
needSeparator = false;
//Synthetic comment -- @@ -145,7 +182,7 @@
}

if (action instanceof MenuAction.OrderedChoices) {
                    MenuAction.OrderedChoices choices = (OrderedChoices) action;
if (!choices.isRadio()) {
addDropdown(choices);
} else {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java
//Synthetic comment -- index bf95ee6..bcc433d 100644

//Synthetic comment -- @@ -477,6 +477,17 @@
redraw();
}

    public void select(Collection<INode> nodes) {
        List<CanvasViewInfo> infos = new ArrayList<CanvasViewInfo>(nodes.size());
        for (INode node : nodes) {
            CanvasViewInfo info = mCanvas.getViewHierarchy().findViewInfoFor(node);
            if (info != null) {
                infos.add(info);
            }
        }
        selectMultiple(infos);
    }

/**
* Selects the visual element corresponding to the given XML node
* @param xmlNode The Node whose element we want to select.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index 8c2051f..7b51c3a 100755

//Synthetic comment -- @@ -38,6 +38,8 @@
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutCanvas;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.SelectionManager;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.SimpleElement;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
//Synthetic comment -- @@ -959,5 +961,19 @@

return null;
}

        public void select(final Collection<INode> nodes) {
            LayoutCanvas layoutCanvas = mEditor.getCanvasControl();
            final SelectionManager selectionManager = layoutCanvas.getSelectionManager();
            selectionManager.select(nodes);
            // ALSO run an async select since immediately after nodes are created they
            // may not be selectable. We can't ONLY run an async exec since
            // code may depend on operating on the selection.
            layoutCanvas.getDisplay().asyncExec(new Runnable() {
                public void run() {
                    selectionManager.select(nodes);
                }
            });
        }
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LayoutTestBase.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LayoutTestBase.java
//Synthetic comment -- index a7aae04..18d985e 100644

//Synthetic comment -- @@ -249,6 +249,10 @@
fail("Not supported in tests yet");
return null;
}

        public void select(Collection<INode> nodes) {
            fail("Not supported in tests yet");
        }
}

public void testDummy() {







