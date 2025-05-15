
//<Beginning of snippet n. 0>



import com.android.ide.common.api.IMenuCallback;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.INodeHandler;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
import com.android.ide.common.api.MenuAction;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* An {@link IViewRule} for android.widget.TableLayout.
// the default is vertical, not horizontal
// The fill of all children should be wrap_content

    private static final String ACTION_ADD_ROW = "_addrow"; //$NON-NLS-1$
    private static final String ACTION_REMOVE_ROW = "_removerow"; //$NON-NLS-1$
    private static final URL ICON_ADD_ROW =
        TableLayoutRule.class.getResource("addrow.png"); //$NON-NLS-1$
    private static final URL ICON_REMOVE_ROW =
        TableLayoutRule.class.getResource("removerow.png"); //$NON-NLS-1$

@Override
protected boolean isVertical(INode node) {
// Tables are always vertical
null, addTab));
}

    @Override
    public void addLayoutActions(List<MenuAction> actions, final INode parentNode,
            final List<? extends INode> children) {
        super.addLayoutActions(actions, parentNode, children);
        addTableLayoutActions(actions, parentNode, children);
    }

    /**
     * Adds layout actions to add and remove toolbar items
     */
    static void addTableLayoutActions(List<MenuAction> actions, final INode parentNode,
            final List<? extends INode> children) {
        IMenuCallback actionCallback = new IMenuCallback() {
            public void action(final MenuAction action, final String valueId,
                    final Boolean newValue) {
                parentNode.editXml("Add/Remove Table Row", new INodeHandler() {
                    public void handle(INode n) {
                        if (action.getId().equals(ACTION_ADD_ROW)) {
                            parentNode.appendChild(FQCN_TABLE_ROW);
                        } else if (action.getId().equals(ACTION_REMOVE_ROW)) {
                            // Find the direct children of the TableLayout to delete;
                            // this is necessary since TableRow might also use
                            // this implementation, so the parentNode is the true
                            // TableLayout but the children might be grand children.
                            Set<INode> targets = new HashSet<INode>();
                            for (INode child : children) {
                                while (child != null && child.getParent() != parentNode) {
                                    child = child.getParent();
                                }
                                if (child != null) {
                                    targets.add(child);
                                }
                            }
                            for (INode target : targets) {
                                parentNode.removeChild(target);
                            }
                        }
                    }
                });
            }
        };

        // Add Row
        actions.add(MenuAction.createSeparator(150));
        actions.add(MenuAction.createAction(ACTION_ADD_ROW, "Add Table Row", null, actionCallback,
                ICON_ADD_ROW, 160));

        // Remove Row (if something is selected)
        if (children != null && children.size() > 0) {
            actions.add(MenuAction.createAction(ACTION_REMOVE_ROW, "Remove Table Row", null,
                    actionCallback, ICON_REMOVE_ROW, 170));
        }
    }

    @Override
    public void onCreate(INode node, INode parent, InsertType insertType) {
        super.onCreate(node, parent, insertType);

        if (insertType.isCreate()) {
            // Start the table with 4 rows
            for (int i = 0; i < 4; i++) {
                node.appendChild(FQCN_TABLE_ROW);
            }
        }
    }
}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


*/
package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.FQCN_TABLE_LAYOUT;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
import com.android.ide.common.api.MenuAction;

import java.util.List;

/**
* An {@link IViewRule} for android.widget.TableRow.
// respectively.
}

    @Override
    public void addLayoutActions(List<MenuAction> actions, final INode parentNode,
            final List<? extends INode> children) {
        super.addLayoutActions(actions, parentNode, children);

        // Also apply table-specific actions on the table row such that you can
        // select something in a table row and still get offered actions on the surrounding
        // table.
        INode grandParent = parentNode.getParent();
        if (grandParent != null && grandParent.getFqcn().equals(FQCN_TABLE_LAYOUT)) {
            TableLayoutRule.addTableLayoutActions(actions, grandParent, children);
        }
    }
}

//<End of snippet n. 1>








