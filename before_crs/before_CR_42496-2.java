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

private static String getPropertyMapKey(INode node) {
// Compute the key for mAttributesMap. This depends on the type of this
// node and its parent in the view hierarchy.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java
//Synthetic comment -- index 680f417..27c98d0 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_FRAGMENT;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.RuleAction;
//Synthetic comment -- @@ -56,6 +57,7 @@
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;

//Synthetic comment -- @@ -79,6 +81,8 @@
* they are both linked to the current selection state of the {@link LayoutCanvas}.
*/
class DynamicContextMenu {

/** The XML layout editor that contains the canvas that uses this menu. */
private final LayoutEditorDelegate mEditorDelegate;
//Synthetic comment -- @@ -197,8 +201,9 @@
// the set of all selected nodes to that first action. Actions are required
// to work this way to facilitate multi selection and actions which apply
// to multiple nodes.
        List<RuleAction> firstSelectedActions = allActions.get(nodes.get(0));

for (RuleAction action : firstSelectedActions) {
if (!availableIds.contains(action.getId())
&& !(action instanceof RuleAction.Separator)) {
//Synthetic comment -- @@ -206,7 +211,7 @@
continue;
}

            items.add(createContributionItem(action, nodes));
}

return items;
//Synthetic comment -- @@ -369,15 +374,26 @@
}

/**
* Creates a {@link ContributionItem} for the given {@link RuleAction}.
*
* @param action the action to create a {@link ContributionItem} for
* @param nodes the set of nodes the action should be applied to
* @return a new {@link ContributionItem} which implements the given action
*         on the given nodes
*/
private ContributionItem createContributionItem(final RuleAction action,
            final List<INode> nodes) {
if (action instanceof RuleAction.Separator) {
return new Separator();
} else if (action instanceof NestedAction) {
//Synthetic comment -- @@ -389,7 +405,7 @@
} else if (action instanceof Toggle) {
return new ActionContributionItem(createToggleAction(action, nodes));
} else {
            return new ActionContributionItem(createPlainAction(action, nodes));
}
}

//Synthetic comment -- @@ -415,7 +431,8 @@
return a;
}

    private IAction createPlainAction(final RuleAction action, final List<INode> nodes) {
IAction a = new Action(action.getTitle(), IAction.AS_PUSH_BUTTON) {
@Override
public void run() {
//Synthetic comment -- @@ -430,7 +447,29 @@
});
}
};
        a.setId(action.getId());
return a;
}

//Synthetic comment -- @@ -495,7 +534,10 @@
}

Set<String> availableIds = computeApplicableActionIds(allActions);
            List<RuleAction> firstSelectedActions = allActions.get(mNodes.get(0));

int count = 0;
for (RuleAction firstAction : firstSelectedActions) {
//Synthetic comment -- @@ -505,7 +547,7 @@
continue;
}

                createContributionItem(firstAction, mNodes).fill(menu, -1);
count++;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 3aa81ae..78c8cba 100644

//Synthetic comment -- @@ -396,6 +396,23 @@
mDeleteAction.run();
} else if (e.keyCode == SWT.ESC) {
mSelectionManager.selectParent();
} else {
// Zooming actions
char c = e.character;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java
//Synthetic comment -- index 8ad7085..6f6259c 100644

//Synthetic comment -- @@ -15,28 +15,40 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.FQCN_SPACE;
import static com.android.ide.common.layout.LayoutConstants.FQCN_SPACE_V7;
import static com.android.ide.eclipse.adt.internal.editors.layout.gle2.SelectionHandle.PIXEL_MARGIN;
import static com.android.ide.eclipse.adt.internal.editors.layout.gle2.SelectionHandle.PIXEL_RADIUS;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.INode;
import com.android.ide.common.layout.GridLayoutRule;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorDelegate;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.utils.Pair;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
//Synthetic comment -- @@ -45,6 +57,7 @@
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MouseEvent;
//Synthetic comment -- @@ -1108,4 +1121,102 @@
}
return null;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index 7dc908b..616d4ab 100755

//Synthetic comment -- @@ -19,6 +19,8 @@
import static com.android.ide.common.layout.LayoutConstants.ANDROID_WIDGET_PREFIX;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_MERGE;

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.IGraphics;
//Synthetic comment -- @@ -154,6 +156,7 @@
* @return Null if the rule failed, there's no rule or the rule does not provide
*   any custom menu actions. Otherwise, a list of {@link RuleAction}.
*/
public List<RuleAction> callGetContextMenu(NodeProxy selectedNode) {
// try to find a rule for this element's FQCN
IViewRule rule = loadRule(selectedNode.getNode());
//Synthetic comment -- @@ -177,6 +180,30 @@
}

/**
* Invokes {@link IViewRule#addLayoutActions(List, INode, List)} on the rule
* matching the specified element.
*








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidator.java
//Synthetic comment -- index 5208ed8..be11cf7 100644

//Synthetic comment -- @@ -18,6 +18,8 @@

import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;

import com.android.ide.common.resources.ResourceItem;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.ImageUtils;
//Synthetic comment -- @@ -219,14 +221,18 @@
* @param type the resource type of the resource name being validated
* @return a new {@link ResourceNameValidator}
*/
    public static ResourceNameValidator create(boolean allowXmlExtension, IProject project,
            ResourceType type) {
        Set<String> existing = new HashSet<String>();
        ResourceManager manager = ResourceManager.getInstance();
        ProjectResources projectResources = manager.getProjectResources(project);
        Collection<ResourceItem> items = projectResources.getResourceItemsOfType(type);
        for (ResourceItem item : items) {
            existing.add(item.getName());
}

boolean isFileType = ResourceHelper.isFileBasedResourceType(type);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidatorTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidatorTest.java
//Synthetic comment -- index af0ba2b..2cc2c93 100644

//Synthetic comment -- @@ -19,6 +19,8 @@
import com.android.resources.ResourceFolderType;
import com.android.resources.ResourceType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
//Synthetic comment -- @@ -64,6 +66,15 @@
.isValid("_foo") != null);
}

public void testUniqueOrExists() throws Exception {
Set<String> existing = new HashSet<String>();
existing.add("foo1");








//Synthetic comment -- diff --git a/rule_api/src/com/android/ide/common/api/AbstractViewRule.java b/rule_api/src/com/android/ide/common/api/AbstractViewRule.java
//Synthetic comment -- index 068580f..e23a567 100644

//Synthetic comment -- @@ -63,6 +63,12 @@
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
* Invoked by the Rules Engine to ask the parent layout for the set of layout actions
* to display in the layout bar. The layout rule should add these into the provided
* list. The order the items are added in does not matter; the







