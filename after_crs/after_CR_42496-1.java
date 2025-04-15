/*Default action for view rules.

For now, text-oriented widgets declare their default action to
be to set the text attribute.

Change-Id:I14e8e06d0842759b1ac05e7e9494deb30b3cc40f*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java
//Synthetic comment -- index 3fe90d2..a53859d 100644

//Synthetic comment -- @@ -482,6 +482,17 @@
actions.add(properties);
}

    @Override
    @Nullable
    public String getDefaultActionId(@NonNull final INode selectedNode) {
        IAttributeInfo textAttribute = selectedNode.getAttributeInfo(ANDROID_URI, ATTR_TEXT);
        if (textAttribute != null) {
            return PROP_PREFIX + ATTR_TEXT;
        }

        return null;
    }

private static String getPropertyMapKey(INode node) {
// Compute the key for mAttributesMap. This depends on the type of this
// node and its parent in the view hierarchy.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java
//Synthetic comment -- index 680f417..f8e50b6 100644

//Synthetic comment -- @@ -56,6 +56,7 @@
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;

//Synthetic comment -- @@ -79,6 +80,8 @@
* they are both linked to the current selection state of the {@link LayoutCanvas}.
*/
class DynamicContextMenu {
    public static String DEFAULT_ACTION_SHORTCUT = "F2"; //$NON-NLS-1$
    public static int DEFAULT_ACTION_KEY = SWT.F2;

/** The XML layout editor that contains the canvas that uses this menu. */
private final LayoutEditorDelegate mEditorDelegate;
//Synthetic comment -- @@ -197,8 +200,9 @@
// the set of all selected nodes to that first action. Actions are required
// to work this way to facilitate multi selection and actions which apply
// to multiple nodes.
        NodeProxy first = (NodeProxy) nodes.get(0);
        List<RuleAction> firstSelectedActions = allActions.get(first);
        String defaultId = getDefaultActionId(first);
for (RuleAction action : firstSelectedActions) {
if (!availableIds.contains(action.getId())
&& !(action instanceof RuleAction.Separator)) {
//Synthetic comment -- @@ -206,7 +210,7 @@
continue;
}

            items.add(createContributionItem(action, nodes, defaultId));
}

return items;
//Synthetic comment -- @@ -369,15 +373,26 @@
}

/**
     * Returns the default action id, or null
     *
     * @param node the node to look up the default action for
     * @return the action id, or null
     */
    private String getDefaultActionId(NodeProxy node) {
        return mCanvas.getRulesEngine().callGetDefaultActionId(node);
    }

    /**
* Creates a {@link ContributionItem} for the given {@link RuleAction}.
*
* @param action the action to create a {@link ContributionItem} for
* @param nodes the set of nodes the action should be applied to
     * @param defaultId if not non null, the id of an action which should be considered default
* @return a new {@link ContributionItem} which implements the given action
*         on the given nodes
*/
private ContributionItem createContributionItem(final RuleAction action,
            final List<INode> nodes, final String defaultId) {
if (action instanceof RuleAction.Separator) {
return new Separator();
} else if (action instanceof NestedAction) {
//Synthetic comment -- @@ -389,7 +404,7 @@
} else if (action instanceof Toggle) {
return new ActionContributionItem(createToggleAction(action, nodes));
} else {
            return new ActionContributionItem(createPlainAction(action, nodes, defaultId));
}
}

//Synthetic comment -- @@ -415,7 +430,8 @@
return a;
}

    private IAction createPlainAction(final RuleAction action, final List<INode> nodes,
            final String defaultId) {
IAction a = new Action(action.getTitle(), IAction.AS_PUSH_BUTTON) {
@Override
public void run() {
//Synthetic comment -- @@ -430,6 +446,13 @@
});
}
};

        if (defaultId != null && action.getId().equals(defaultId)) {
            a.setAccelerator(DEFAULT_ACTION_KEY);
            String text = a.getText();
            text = text + '\t' + DEFAULT_ACTION_SHORTCUT;
            a.setText(text);
        }
a.setId(action.getId());
return a;
}
//Synthetic comment -- @@ -495,7 +518,10 @@
}

Set<String> availableIds = computeApplicableActionIds(allActions);

            NodeProxy first = (NodeProxy) mNodes.get(0);
            String defaultId = getDefaultActionId(first);
            List<RuleAction> firstSelectedActions = allActions.get(first);

int count = 0;
for (RuleAction firstAction : firstSelectedActions) {
//Synthetic comment -- @@ -505,7 +531,7 @@
continue;
}

                createContributionItem(firstAction, mNodes, defaultId).fill(menu, -1);
count++;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 3aa81ae..b9366f6 100644

//Synthetic comment -- @@ -396,6 +396,8 @@
mDeleteAction.run();
} else if (e.keyCode == SWT.ESC) {
mSelectionManager.selectParent();
        } else if (e.keyCode == DynamicContextMenu.DEFAULT_ACTION_KEY) {
            mSelectionManager.performDefaultAction();
} else {
// Zooming actions
char c = e.character;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java
//Synthetic comment -- index 8ad7085..77c72b2 100644

//Synthetic comment -- @@ -24,10 +24,13 @@
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.RuleAction;
import com.android.ide.common.layout.GridLayoutRule;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorDelegate;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.utils.Pair;
//Synthetic comment -- @@ -1108,4 +1111,59 @@
}
return null;
}

    /** Performs the default action provided by the currently selected view */
    public void performDefaultAction() {
        final List<SelectionItem> selections = getSelections();
        if (selections.size() > 0) {
            NodeProxy primary = selections.get(0).getNode();
            if (primary != null) {
                RulesEngine rulesEngine = mCanvas.getRulesEngine();
                final String id = rulesEngine.callGetDefaultActionId(primary);
                if (id == null) {
                    return;
                }
                final List<RuleAction> actions = rulesEngine.callGetContextMenu(primary);
                if (actions == null) {
                    return;
                }
                RuleAction matching = null;
                for (RuleAction a : actions) {
                    if (id.equals(a.getId())) {
                        matching = a;
                        break;
                    }
                }
                if (matching == null) {
                    return;
                }
                final List<INode> selectedNodes = new ArrayList<INode>();
                for (SelectionItem item : selections) {
                    NodeProxy n = item.getNode();
                    if (n != null) {
                        selectedNodes.add(n);
                    }
                }
                final RuleAction action = matching;
                mCanvas.getEditorDelegate().getEditor().wrapUndoEditXmlModel(action.getTitle(),
                    new Runnable() {
                        @Override
                        public void run() {
                            action.getCallback().action(action, selectedNodes,
                                    action.getId(), null);
                            LayoutCanvas canvas = mCanvas;
                            CanvasViewInfo root = canvas.getViewHierarchy().getRoot();
                            if (root != null) {
                                UiViewElementNode uiViewNode = root.getUiViewNode();
                                NodeFactory nodeFactory = canvas.getNodeFactory();
                                NodeProxy rootNode = nodeFactory.create(uiViewNode);
                                if (rootNode != null) {
                                    rootNode.applyPendingChanges();
                                }
                            }
                        }
                });
            }
        }
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index 7dc908b..616d4ab 100755

//Synthetic comment -- @@ -19,6 +19,8 @@
import static com.android.ide.common.layout.LayoutConstants.ANDROID_WIDGET_PREFIX;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_MERGE;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.IGraphics;
//Synthetic comment -- @@ -154,6 +156,7 @@
* @return Null if the rule failed, there's no rule or the rule does not provide
*   any custom menu actions. Otherwise, a list of {@link RuleAction}.
*/
    @Nullable
public List<RuleAction> callGetContextMenu(NodeProxy selectedNode) {
// try to find a rule for this element's FQCN
IViewRule rule = loadRule(selectedNode.getNode());
//Synthetic comment -- @@ -177,6 +180,30 @@
}

/**
     * Calls the selected node to return its default action
     *
     * @param selectedNode the node to apply the action to
     * @return the default action id
     */
    public String callGetDefaultActionId(@NonNull NodeProxy selectedNode) {
        // try to find a rule for this element's FQCN
        IViewRule rule = loadRule(selectedNode.getNode());

        if (rule != null) {
            try {
                mInsertType = InsertType.CREATE;
                return rule.getDefaultActionId(selectedNode);
            } catch (Exception e) {
                AdtPlugin.log(e, "%s.getDefaultAction() failed: %s",
                        rule.getClass().getSimpleName(),
                        e.toString());
            }
        }

        return null;
    }

    /**
* Invokes {@link IViewRule#addLayoutActions(List, INode, List)} on the rule
* matching the specified element.
*








//Synthetic comment -- diff --git a/rule_api/src/com/android/ide/common/api/AbstractViewRule.java b/rule_api/src/com/android/ide/common/api/AbstractViewRule.java
//Synthetic comment -- index 068580f..e23a567 100644

//Synthetic comment -- @@ -63,6 +63,12 @@
}

@Override
    @Nullable
    public String getDefaultActionId(@NonNull INode node) {
        return null;
    }

    @Override
public void paintSelectionFeedback(@NonNull IGraphics graphics, @NonNull INode parentNode,
@NonNull List<? extends INode> childNodes, @Nullable Object view) {
}








//Synthetic comment -- diff --git a/rule_api/src/com/android/ide/common/api/IViewRule.java b/rule_api/src/com/android/ide/common/api/IViewRule.java
//Synthetic comment -- index b16df26..88b795f 100644

//Synthetic comment -- @@ -102,6 +102,17 @@
void addContextMenuActions(@NonNull List<RuleAction> actions, @NonNull INode node);

/**
     * Returns the id of the default action to invoke for this view, typically when the
     * user presses F2. The id should correspond to the {@link RuleAction#getId()} returned
     * by one of the actions added by {@link #addContextMenuActions(List, INode)}.
     *
     * @param node the primary selected node
     * @return the id of default action, or null if none is default
     */
    @Nullable
    String getDefaultActionId(@NonNull INode node);

    /**
* Invoked by the Rules Engine to ask the parent layout for the set of layout actions
* to display in the layout bar. The layout rule should add these into the provided
* list. The order the items are added in does not matter; the







