/*Default action for view rules.

For now, text-oriented widgets declare their default action to
be to set the text attribute. Also hook up the default rename
keybinding to setting the id.

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
//Synthetic comment -- index 680f417..27c98d0 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_FRAGMENT;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.SdkConstants;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.RuleAction;
//Synthetic comment -- @@ -56,6 +57,7 @@
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;

//Synthetic comment -- @@ -79,6 +81,8 @@
* they are both linked to the current selection state of the {@link LayoutCanvas}.
*/
class DynamicContextMenu {
    public static String DEFAULT_ACTION_SHORTCUT = "F2"; //$NON-NLS-1$
    public static int DEFAULT_ACTION_KEY = SWT.F2;

/** The XML layout editor that contains the canvas that uses this menu. */
private final LayoutEditorDelegate mEditorDelegate;
//Synthetic comment -- @@ -197,8 +201,9 @@
// the set of all selected nodes to that first action. Actions are required
// to work this way to facilitate multi selection and actions which apply
// to multiple nodes.
        NodeProxy first = (NodeProxy) nodes.get(0);
        List<RuleAction> firstSelectedActions = allActions.get(first);
        String defaultId = getDefaultActionId(first);
for (RuleAction action : firstSelectedActions) {
if (!availableIds.contains(action.getId())
&& !(action instanceof RuleAction.Separator)) {
//Synthetic comment -- @@ -206,7 +211,7 @@
continue;
}

            items.add(createContributionItem(action, nodes, defaultId));
}

return items;
//Synthetic comment -- @@ -369,15 +374,26 @@
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
//Synthetic comment -- @@ -389,7 +405,7 @@
} else if (action instanceof Toggle) {
return new ActionContributionItem(createToggleAction(action, nodes));
} else {
            return new ActionContributionItem(createPlainAction(action, nodes, defaultId));
}
}

//Synthetic comment -- @@ -415,7 +431,8 @@
return a;
}

    private IAction createPlainAction(final RuleAction action, final List<INode> nodes,
            final String defaultId) {
IAction a = new Action(action.getTitle(), IAction.AS_PUSH_BUTTON) {
@Override
public void run() {
//Synthetic comment -- @@ -430,7 +447,29 @@
});
}
};

        String id = action.getId();
        if (defaultId != null && id.equals(defaultId)) {
            a.setAccelerator(DEFAULT_ACTION_KEY);
            String text = a.getText();
            text = text + '\t' + DEFAULT_ACTION_SHORTCUT;
            a.setText(text);

        } else if (ATTR_ID.equals(id)) {
            // Keep in sync with {@link LayoutCanvas#handleKeyPressed}
            if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_DARWIN) {
                a.setAccelerator('R' | SWT.MOD1 | SWT.MOD3);
                // Option+Command
                a.setText(a.getText().trim() + "\t\u2325\u2318R"); //$NON-NLS-1$
            } else if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_LINUX) {
                a.setAccelerator('R' | SWT.MOD2 | SWT.MOD3);
                a.setText(a.getText() + "\tShift+Alt+R"); //$NON-NLS-1$
            } else {
                a.setAccelerator('R' | SWT.MOD2 | SWT.MOD3);
                a.setText(a.getText() + "\tAlt+Shift+R"); //$NON-NLS-1$
            }
        }
        a.setId(id);
return a;
}

//Synthetic comment -- @@ -495,7 +534,10 @@
}

Set<String> availableIds = computeApplicableActionIds(allActions);

            NodeProxy first = (NodeProxy) mNodes.get(0);
            String defaultId = getDefaultActionId(first);
            List<RuleAction> firstSelectedActions = allActions.get(first);

int count = 0;
for (RuleAction firstAction : firstSelectedActions) {
//Synthetic comment -- @@ -505,7 +547,7 @@
continue;
}

                createContributionItem(firstAction, mNodes, defaultId).fill(menu, -1);
count++;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 3aa81ae..78c8cba 100644

//Synthetic comment -- @@ -396,6 +396,23 @@
mDeleteAction.run();
} else if (e.keyCode == SWT.ESC) {
mSelectionManager.selectParent();
        } else if (e.keyCode == DynamicContextMenu.DEFAULT_ACTION_KEY) {
            mSelectionManager.performDefaultAction();
        } else if (e.keyCode == 'r') {
            // Keep key bindings in sync with {@link DynamicContextMenu#createPlainAction}
            // TODO: Find a way to look up the Eclipse key bindings and attempt
            // to use the current keymap's rename action.
            if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_DARWIN) {
                // Command+Option+R
                if ((e.stateMask & (SWT.MOD1 | SWT.MOD3)) == (SWT.MOD1 | SWT.MOD3)) {
                    mSelectionManager.performRename();
                }
            } else {
                // Alt+Shift+R
                if ((e.stateMask & (SWT.MOD2 | SWT.MOD3)) == (SWT.MOD2 | SWT.MOD3)) {
                    mSelectionManager.performRename();
                }
            }
} else {
// Zooming actions
char c = e.character;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java
//Synthetic comment -- index 8ad7085..6f6259c 100644

//Synthetic comment -- @@ -15,28 +15,40 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.FQCN_SPACE;
import static com.android.ide.common.layout.LayoutConstants.FQCN_SPACE_V7;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.ide.eclipse.adt.internal.editors.layout.gle2.SelectionHandle.PIXEL_MARGIN;
import static com.android.ide.eclipse.adt.internal.editors.layout.gle2.SelectionHandle.PIXEL_RADIUS;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.RuleAction;
import com.android.ide.common.layout.BaseViewRule;
import com.android.ide.common.layout.GridLayoutRule;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorDelegate;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.resources.ResourceNameValidator;
import com.android.resources.ResourceType;
import com.android.utils.Pair;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
//Synthetic comment -- @@ -45,6 +57,7 @@
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MouseEvent;
//Synthetic comment -- @@ -1108,4 +1121,102 @@
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

    /** Performs renaming the selected views */
    public void performRename() {
        final List<SelectionItem> selections = getSelections();
        if (selections.size() > 0) {
            NodeProxy primary = selections.get(0).getNode();
            if (primary != null) {
                String currentId = primary.getStringAttr(ANDROID_URI, ATTR_ID);
                currentId = BaseViewRule.stripIdPrefix(currentId);
                InputDialog d = new InputDialog(
                            AdtPlugin.getDisplay().getActiveShell(),
                            "Set ID",
                            "New ID:",
                            currentId,
                            ResourceNameValidator.create(false, (IProject) null, ResourceType.ID));
                if (d.open() == Window.OK) {
                    final String s = d.getValue();
                    mCanvas.getEditorDelegate().getEditor().wrapUndoEditXmlModel("Set ID",
                            new Runnable() {
                        @Override
                        public void run() {
                            String newId = s;
                            newId = NEW_ID_PREFIX + BaseViewRule.stripIdPrefix(s);
                            for (SelectionItem item : selections) {
                                item.getNode().setAttribute(ANDROID_URI, ATTR_ID, newId);
                            }

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








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidator.java
//Synthetic comment -- index 5208ed8..be11cf7 100644

//Synthetic comment -- @@ -18,6 +18,8 @@

import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.resources.ResourceItem;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.ImageUtils;
//Synthetic comment -- @@ -219,14 +221,18 @@
* @param type the resource type of the resource name being validated
* @return a new {@link ResourceNameValidator}
*/
    public static ResourceNameValidator create(boolean allowXmlExtension,
            @Nullable IProject project,
            @NonNull ResourceType type) {
        Set<String> existing = null;
        if (project != null) {
            existing = new HashSet<String>();
            ResourceManager manager = ResourceManager.getInstance();
            ProjectResources projectResources = manager.getProjectResources(project);
            Collection<ResourceItem> items = projectResources.getResourceItemsOfType(type);
            for (ResourceItem item : items) {
                existing.add(item.getName());
            }
}

boolean isFileType = ResourceHelper.isFileBasedResourceType(type);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidatorTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidatorTest.java
//Synthetic comment -- index af0ba2b..2cc2c93 100644

//Synthetic comment -- @@ -19,6 +19,8 @@
import com.android.resources.ResourceFolderType;
import com.android.resources.ResourceType;

import org.eclipse.core.resources.IProject;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
//Synthetic comment -- @@ -64,6 +66,15 @@
.isValid("_foo") != null);
}

    public void testIds() throws Exception {
        ResourceNameValidator validator = ResourceNameValidator.create(false, (IProject) null,
                ResourceType.ID);
        assertTrue(validator.isValid("foo") == null);
        assertTrue(validator.isValid(" foo") != null);
        assertTrue(validator.isValid("foo ") != null);
        assertTrue(validator.isValid("foo@") != null);
    }

public void testUniqueOrExists() throws Exception {
Set<String> existing = new HashSet<String>();
existing.add("foo1");








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







