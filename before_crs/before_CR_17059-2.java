/*GLE2: perform all context menu edits in the same undo session.

Change-Id:I9516066edaa1704725ab234f1a65664d5eabfb39*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/editors/layout/gscripts/MenuAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/editors/layout/gscripts/MenuAction.java
//Synthetic comment -- index 63454a4..d3f34e5 100755

//Synthetic comment -- @@ -157,9 +157,9 @@
/**
* A closure executed when the action is selected in the context menu.
*
         * @see #getAction() for details on the closure parameters.
*/
        private final Closure mAction;
/**
* An optional group id, to place the action in a given sub-menu.
* @null This value can be null.
//Synthetic comment -- @@ -174,13 +174,13 @@
* @param title The title of the action. Must not be null.
* @param groupId The optional group id, to place the action in a given sub-menu.
*                Can be null.
         * @param action The closure executed when the action is selected. Must not be null.
         *               See {@link #getAction()} for the closure parameters.
*/
        private Action(String id, String title, String groupId, Closure action) {
super(id, title);
mGroupId = groupId;
            mAction = action;
}

/**
//Synthetic comment -- @@ -196,8 +196,8 @@
*      useful; however for flags it allows one to add or remove items to the flag's
*      choices.
*/
        public Closure getAction() {
            return mAction;
}

/**
//Synthetic comment -- @@ -250,10 +250,10 @@
* @param id The unique id of the action. Cannot be null.
* @param title The UI-visible title of the context menu item. Cannot be null.
* @param isChecked Whether the context menu item has a check mark.
         * @param action A closure to execute when the context menu item is selected.
*/
        public Toggle(String id, String title, boolean isChecked, Closure action) {
            this(id, title, isChecked, null /*group-id*/, action);
}

/**
//Synthetic comment -- @@ -264,10 +264,10 @@
* @param isChecked Whether the context menu item has a check mark.
* @param groupId The optional group id, to place the action in a given sub-menu.
*                Can be null.
         * @param action A closure to execute when the context menu item is selected.
*/
        public Toggle(String id, String title, boolean isChecked, String groupId, Closure action) {
            super(id, title, groupId, action);
mIsChecked = isChecked;
}

//Synthetic comment -- @@ -337,13 +337,13 @@
* @param current The id(s) of the current choice(s) that will be check marked.
*                Can be null. Can be an id not present in the choices map.
*                There can be more than one id separated by {@link #CHOICE_SEP}.
         * @param action A closure to execute when the context menu item is selected.
*/
public Choices(String id, String title,
Map<String, String> choices,
String current,
                Closure action) {
            this(id, title, choices, current, null /*group-id*/, action);
}

/**
//Synthetic comment -- @@ -357,14 +357,14 @@
*                There can be more than one id separated by {@link #CHOICE_SEP}.
* @param groupId The optional group id, to place the action in a given sub-menu.
*                Can be null.
         * @param action A closure to execute when the context menu item is selected.
*/
public Choices(String id, String title,
Map<String, String> choices,
String current,
String groupId,
                Closure action) {
            super(id, title, groupId, action);
mChoices = choices;
mCurrent = current;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index 68e1146..ed5e79d 100644

//Synthetic comment -- @@ -106,8 +106,12 @@
/** flag set during page creation */
private boolean mIsCreatingPage = false;

    /** flag indicating we're inside {@link #editXmlModel(Runnable)}. */
    private boolean mIsEditXmlModelPending;

/**
* Creates a form editor.
//Synthetic comment -- @@ -634,7 +638,7 @@
* Callers <em>must</em> call model.releaseFromEdit() when done, typically
* in a try..finally clause. Because of this, it is highly recommended
* to <b>NOT</b> use this method directly and instead use the wrapper
     * {@link #editXmlModel(Runnable)} which executes a runnable into a
* properly configured model and then performs whatever cleanup is necessary.
*
* @return The model for the XML document or null if cannot be obtained from the editor
//Synthetic comment -- @@ -662,30 +666,93 @@
* <p/>
* The method is synchronous. As soon as the {@link IStructuredModel#changedModel()} method
* is called, XML model listeners will be triggered.
*
     * @param edit_action Something that will change the XML.
*/
    public final void editXmlModel(Runnable edit_action) {
        IStructuredModel model = getModelForEdit();
try {
            model.aboutToChangeModel();
            mIsEditXmlModelPending = true;
            edit_action.run();
} finally {
            // Notify the model we're done modifying it. This must *always* be executed.
            mIsEditXmlModelPending = false;
            model.changedModel();
            model.releaseFromEdit();
}
}

/**
     * Returns true when the runnable of {@link #editXmlModel(Runnable)} is currently
* being executed. This means it is safe to actually edit the XML model returned
* by {@link #getModelForEdit()}.
*/
public boolean isEditXmlModelPending() {
        return mIsEditXmlModelPending;
}

/**
//Synthetic comment -- @@ -696,6 +763,7 @@
* <p/>
* beginUndoRecording/endUndoRecording calls can be nested (inner calls are ignored, only one
* undo operation is recorded.)
*
* @param label The label for the undo operation. Can be null but we should really try to put
*              something meaningful if possible.
//Synthetic comment -- @@ -722,6 +790,7 @@
* <p/>
* This is the counterpart call to {@link #beginUndoRecording(String)} and should only be
* used if the initial call returned true.
*/
private final void endUndoRecording() {
IStructuredDocument document = getStructuredDocument();
//Synthetic comment -- @@ -737,28 +806,6 @@
}

/**
     * Creates an "undo recording" session by calling the undoableAction runnable
     * using {@link #beginUndoRecording(String)} and {@link #endUndoRecording()}.
     * <p>
     * You can nest several calls to {@link #wrapUndoRecording(String, Runnable)}, only one
     * recording session will be created.
     *
     * @param label The label for the undo operation. Can be null. Ideally we should really try
     *              to put something meaningful if possible.
     */
    public void wrapUndoRecording(String label, Runnable undoableAction) {
        boolean recording = false;
        try {
            recording = beginUndoRecording(label);
            undoableAction.run();
        } finally {
            if (recording) {
                endUndoRecording();
            }
        }
    }

    /**
* Returns the XML {@link Document} or null if we can't get it
*/
protected final Document getXmlDocument(IStructuredModel model) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java
//Synthetic comment -- index 96db650..f9657af 100755

//Synthetic comment -- @@ -16,8 +16,10 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.eclipse.adt.editors.layout.gscripts.IViewRule;
import com.android.ide.eclipse.adt.editors.layout.gscripts.MenuAction;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;

//Synthetic comment -- @@ -38,9 +40,9 @@
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;


//Synthetic comment -- @@ -57,11 +59,16 @@
*/
/* package */ class DynamicContextMenu {

private final LayoutCanvas mCanvas;

/** The root menu manager of the context menu. */
private final MenuManager mMenuManager;

/**
* Creates a new helper responsible for adding and managing the dynamic menu items
* contributed by the {@link IViewRule} groovy instances, based on the current selection
//Synthetic comment -- @@ -72,7 +79,8 @@
* @param rootMenu The root of the context menu displayed. In practice this may be the
*   context menu manager of the {@link LayoutCanvas} or the one from {@link OutlinePage2}.
*/
    public DynamicContextMenu(LayoutCanvas canvas, MenuManager rootMenu) {
mCanvas = canvas;
mMenuManager = rootMenu;

//Synthetic comment -- @@ -159,23 +167,25 @@
continue;
}

            final MenuAction.Action action = (MenuAction.Action) actions.get(0);

IContributionItem contrib = null;

            if (action instanceof MenuAction.Toggle) {
                contrib = createDynamicMenuToggle((MenuAction.Toggle) action, actionsMap);

            } else if (action instanceof MenuAction.Choices) {
                Map<String, String> choiceMap = ((MenuAction.Choices) action).getChoices();
if (choiceMap != null && !choiceMap.isEmpty()) {
contrib = createDynamicChoices(
                            (MenuAction.Choices)action, choiceMap, actionsMap);
}
}

if (contrib != null) {
                MenuManager groupMenu = menuGroups.get(action.getGroupId());
if (groupMenu != null) {
groupMenu.add(contrib);
} else {
//Synthetic comment -- @@ -288,36 +298,59 @@
* <p/>
* Toggles are represented by a checked menu item.
*
     * @param action The toggle action to convert to a menu item.
* @param actionsMap Map of all contributed actions.
* @return a new {@link IContributionItem} to add to the context menu
*/
private IContributionItem createDynamicMenuToggle(
            final MenuAction.Toggle action,
final TreeMap<String, ArrayList<MenuAction>> actionsMap) {

final RulesEngine gre = mCanvas.getRulesEngine();
        final boolean isChecked = action.isChecked();
        Action a = new Action(action.getTitle(), IAction.AS_CHECK_BOX) {
@Override
public void run() {
                // Invoke the closures of all the actions using the same action-id
                for (MenuAction a2 : actionsMap.get(action.getId())) {
                    if (a2 instanceof MenuAction.Action) {
                        Closure c = ((MenuAction.Action) a2).getAction();
                        if (c != null) {
                            gre.callClosure(
                                    ((MenuAction.Action) a2).getAction(),
                                    // Closure parameters are action, valueId, newValue
                                    action,
                                    null, // no valueId for a toggle
                                    !isChecked);
}
}
                }
}
};
        a.setId(action.getId());
a.setChecked(isChecked);

return new ActionContributionItem(a);
//Synthetic comment -- @@ -329,24 +362,25 @@
* <p/>
* Multiple-choices are represented by a sub-menu containing checked items.
*
     * @param action The choices action to convert to a menu item.
* @param actionsMap Map of all contributed actions.
* @return a new {@link IContributionItem} to add to the context menu
*/
private IContributionItem createDynamicChoices(
            final MenuAction.Choices action,
Map<String, String> choiceMap,
final TreeMap<String, ArrayList<MenuAction>> actionsMap) {

final RulesEngine gre = mCanvas.getRulesEngine();
        MenuManager submenu = new MenuManager(action.getTitle(), action.getId());

// Convert to a tree map as needed so that keys be naturally ordered.
if (!(choiceMap instanceof TreeMap<?, ?>)) {
choiceMap = new TreeMap<String, String>(choiceMap);
}

        String current = action.getCurrent();
Set<String> currents = null;
if (current.indexOf(MenuAction.Choices.CHOICE_SEP) >= 0) {
currents = new HashSet<String>(
//Synthetic comment -- @@ -375,24 +409,53 @@
Action a = new Action(title, IAction.AS_CHECK_BOX) {
@Override
public void run() {
                    // Invoke the closures of all the actions using the same action-id
                    for (MenuAction a2 : actionsMap.get(action.getId())) {
                        if (a2 instanceof MenuAction.Action) {
                            gre.callClosure(
                                    ((MenuAction.Action) a2).getAction(),
                                    // Closure parameters are action, valueId, newValue
                                    action,
                                    key,
                                    !isChecked);
                        }
}
}
};
            a.setId(String.format("%s_%s", action.getId(), key));     //$NON-NLS-1$
a.setChecked(isChecked);
submenu.add(a);
}

return submenu;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 9cb6385..dbdd7aa 100755

//Synthetic comment -- @@ -1504,17 +1504,12 @@
// Remove from source. Since we know the selection, we'll simply
// create a cut operation on the existing drag selection.

                // Create an undo wrapper, which takes a runnable
                mLayoutEditor.wrapUndoRecording(
"Remove drag'n'drop source elements",
new Runnable() {
public void run() {
                                // Create an edit-XML wrapper, which takes a runnable
                                mLayoutEditor.editXmlModel(new Runnable() {
                                    public void run() {
                                        deleteSelection("Remove", mDragSelection);
                                    }
                                });
}
});
}
//Synthetic comment -- @@ -1820,7 +1815,7 @@

// Fill the menu manager with the static & dynamic actions
setupStaticMenuActions(mMenuManager);
        new DynamicContextMenu(this, mMenuManager);
Menu menu = mMenuManager.createContextMenu(this);
setMenu(menu);
}
//Synthetic comment -- @@ -1979,21 +1974,17 @@
// the elements. An update XML model event should happen when the model gets released
// which will trigger a recompute of the layout, thus reloading the model thus
// resetting the selection.
        mLayoutEditor.wrapUndoRecording(title, new Runnable() {
public void run() {
                mLayoutEditor.editXmlModel(new Runnable() {
                    public void run() {
                        for (CanvasSelection cs : selection) {
                            CanvasViewInfo vi = cs.getViewInfo();
                            if (vi != null) {
                                UiViewElementNode ui = vi.getUiViewKey();
                                if (ui != null) {
                                    ui.deleteXmlNode();
                                }
                            }
}
}
                });
}
});
}
//Synthetic comment -- @@ -2067,76 +2058,72 @@
}
title = String.format("Paste root %1$s in document", title);

        mLayoutEditor.wrapUndoRecording(title, new Runnable() {
public void run() {
                mLayoutEditor.editXmlModel(new Runnable() {
                    public void run() {
                        UiElementNode uiNew = uiDoc.appendNewUiChild(viewDesc);

                        // A root node requires the Android XMLNS
                        uiNew.setAttributeValue(
                                "android",
                                XmlnsAttributeDescriptor.XMLNS_URI,
                                SdkConstants.NS_RESOURCES,
                                true /*override*/);

                        // Copy all the attributes from the pasted element
                        for (IDragAttribute attr : pastedElement.getAttributes()) {
                            uiNew.setAttributeValue(
                                    attr.getName(),
                                    attr.getUri(),
                                    attr.getValue(),
                                    true /*override*/);
                        }

                        // Adjust the attributes, adding the default layout_width/height
                        // only if they are not present (the original element should have
                        // them though.)
                        DescriptorsUtils.setDefaultLayoutAttributes(uiNew, false /*updateLayout*/);

                        uiNew.createXmlNode();

                        // Now process all children
                        for (IDragElement childElement : pastedElement.getInnerElements()) {
                            addChild(uiNew, childElement);
                        }
                    }

                    private void addChild(UiElementNode uiParent, IDragElement childElement) {
                        String childFqcn = childElement.getFqcn();
                        final ViewElementDescriptor childDesc =
                            mLayoutEditor.getFqcnViewDescritor(childFqcn);
                        if (childDesc == null) {
                            // TODO this could happen if pasting a custom view
                            debugPrintf("Failed to paste element, unknown FQCN %1$s", childFqcn);
                            return;
                        }

                        UiElementNode uiChild = uiParent.appendNewUiChild(childDesc);

                        // Copy all the attributes from the pasted element
                        for (IDragAttribute attr : childElement.getAttributes()) {
                            uiChild.setAttributeValue(
                                    attr.getName(),
                                    attr.getUri(),
                                    attr.getValue(),
                                    true /*override*/);
                        }

                        // Adjust the attributes, adding the default layout_width/height
                        // only if they are not present (the original element should have
                        // them though.)
                        DescriptorsUtils.setDefaultLayoutAttributes(
                                uiChild, false /*updateLayout*/);

                        uiChild.createXmlNode();

                        // Now process all grand children
                        for (IDragElement grandChildElement : childElement.getInnerElements()) {
                            addChild(uiChild, grandChildElement);
                        }
                    }
                });
}
});
}
//Synthetic comment -- @@ -2178,25 +2165,21 @@
}
title = String.format("Create root %1$s in document", title);

        mLayoutEditor.wrapUndoRecording(title, new Runnable() {
public void run() {
                mLayoutEditor.editXmlModel(new Runnable() {
                    public void run() {
                        UiElementNode uiNew = uiDoc.appendNewUiChild(viewDesc);

                        // A root node requires the Android XMLNS
                        uiNew.setAttributeValue(
                                "android",
                                XmlnsAttributeDescriptor.XMLNS_URI,
                                SdkConstants.NS_RESOURCES,
                                true /*override*/);

                        // Adjust the attributes
                        DescriptorsUtils.setDefaultLayoutAttributes(uiNew, false /*updateLayout*/);

                        uiNew.createXmlNode();
                    }
                });
}
});
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java
//Synthetic comment -- index 23fe245..5a7f631 100755

//Synthetic comment -- @@ -431,7 +431,10 @@
}
});

        new DynamicContextMenu(mGraphicalEditorPart.getCanvasControl(), mMenuManager);

getControl().setMenu(mMenuManager.createContextMenu(getControl()));
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeProxy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeProxy.java
//Synthetic comment -- index 562c89e..40f77a4 100755

//Synthetic comment -- @@ -165,26 +165,17 @@
public void editXml(String undoName, final Closure c) {
final AndroidXmlEditor editor = mNode.getEditor();

        if (editor.isEditXmlModelPending()) {
            throw new RuntimeException("Error: calls to INode.editXml cannot be nested!");
        }

if (editor instanceof LayoutEditor) {
            // Create an undo wrapper, which takes a runnable
            ((LayoutEditor) editor).wrapUndoRecording(
undoName,
new Runnable() {
public void run() {
                            // Create an edit-XML wrapper, which takes a runnable
                            editor.editXmlModel(new Runnable() {
                                public void run() {
                                    // Here editor.isEditXmlModelPending returns true and it
                                    // is safe to edit the model using any method from INode.

                                    // Finally execute the closure that will act on the XML
                                    c.call(NodeProxy.this);
                                }
                            });
}
});
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index cbfe763..5327d7c 100755

//Synthetic comment -- @@ -140,6 +140,13 @@
}

/**
* Called by the owner of the {@link RulesEngine} when it is going to be disposed.
* This frees some resources, such as the project's folder monitor.
*/








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/parts/DropFeedback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/parts/DropFeedback.java
//Synthetic comment -- index 390dc76..ec928dd 100644

//Synthetic comment -- @@ -219,7 +219,7 @@
if (where == null) {
return;
}
        uiNode.getEditor().editXmlModel(new Runnable() {
public void run() {
uiNode.setAttributeValue(
LayoutConstants.ATTR_LAYOUT_X,
//Synthetic comment -- @@ -270,7 +270,7 @@
final UiElementEditPart anchorPart = info.targetParts[info.anchorIndex];  // can be null
final int direction = info.direction;

        uiNode.getEditor().editXmlModel(new Runnable() {
public void run() {
HashMap<String, String> map = new HashMap<String, String>();









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/parts/ElementCreateCommand.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/parts/ElementCreateCommand.java
//Synthetic comment -- index 59f2169..128dfd7 100644

//Synthetic comment -- @@ -72,7 +72,7 @@
if (uiParent != null) {
final AndroidXmlEditor editor = uiParent.getEditor();
if (editor instanceof LayoutEditor) {
                ((LayoutEditor) editor).wrapUndoRecording(
String.format("Create %1$s", mDescriptor.getXmlLocalName()),
new Runnable() {
public void run() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/pages/ApplicationToggle.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/pages/ApplicationToggle.java
//Synthetic comment -- index 76a0442..8b20cd1 100644

//Synthetic comment -- @@ -43,8 +43,8 @@
* Appllication Toogle section part for application page.
*/
final class ApplicationToggle extends UiElementPart {
    
    /** Checkbox indicating whether an application node is present */ 
private Button mCheckbox;
/** Listen to changes to the UI node for <application> and updates the checkbox */
private AppNodeUpdateListener mAppNodeUpdateListener;
//Synthetic comment -- @@ -60,7 +60,7 @@
null, /* description */
Section.TWISTIE | Section.EXPANDED);
}
    
@Override
public void dispose() {
super.dispose();
//Synthetic comment -- @@ -69,7 +69,7 @@
mAppNodeUpdateListener = null;
}
}
    
/**
* Changes and refreshes the Application UI node handle by the this part.
*/
//Synthetic comment -- @@ -89,7 +89,7 @@
* <p/>
* This MUST not be called by the constructor. Instead it must be called from
* <code>initialize</code> (i.e. right after the form part is added to the managed form.)
     * 
* @param managedForm The owner managed form
*/
@Override
//Synthetic comment -- @@ -114,14 +114,14 @@
// Initialize the state of the checkbox
mAppNodeUpdateListener.uiElementNodeUpdated(getUiElementNode(),
UiUpdateState.CHILDREN_CHANGED);
        
// Tell the section that the layout has changed.
layoutChanged();
}

/**
* Updates the application tooltip in the form text.
     * If there is no tooltip, the form text is hidden. 
*/
private void updateTooltip() {
boolean isVisible = false;
//Synthetic comment -- @@ -131,13 +131,13 @@
tooltip = DescriptorsUtils.formatFormText(tooltip,
getUiElementNode().getDescriptor(),
Sdk.getCurrent().getDocumentationBaseUrl());
    
mTooltipFormText.setText(tooltip, true /* parseTags */, true /* expandURLs */);
mTooltipFormText.setImage(DescriptorsUtils.IMAGE_KEY, AdtPlugin.getAndroidLogo());
mTooltipFormText.addHyperlinkListener(getEditor().createHyperlinkListener());
isVisible = true;
}
        
mTooltipFormText.setVisible(isVisible);
}

//Synthetic comment -- @@ -156,31 +156,27 @@
public void widgetSelected(SelectionEvent e) {
super.widgetSelected(e);
if (!mInternalModification && getUiElementNode() != null) {
                getUiElementNode().getEditor().wrapUndoRecording(
mCheckbox.getSelection()
? "Create or restore Application node"
: "Remove Application node",
new Runnable() {
public void run() {
                                getUiElementNode().getEditor().editXmlModel(new Runnable() {
                                    public void run() {
                                        if (mCheckbox.getSelection()) {
                                            // The user wants an <application> node.
                                            // Either restore a previous one
                                            // or create a full new one.
                                            boolean create = true;
                                            if (mUndoXmlNode != null) {
                                                create = !restoreApplicationNode();
                                            }
                                            if (create) {
                                                getUiElementNode().createXmlNode();
                                            }
                                        } else {
                                            // Users no longer wants the <application> node.
                                            removeApplicationNode();
                                        }
}
                                });
}
});
}
//Synthetic comment -- @@ -188,7 +184,7 @@

/**
* Restore a previously "saved" application node.
         * 
* @return True if the node could be restored, false otherwise.
*/
private boolean restoreApplicationNode() {
//Synthetic comment -- @@ -226,7 +222,7 @@
mUndoXmlParent.insertBefore(sep, null);  // insert separator before end tag
}
success = true;
            } 

// Remove internal references to avoid using them twice
mUndoXmlParent = null;
//Synthetic comment -- @@ -239,8 +235,8 @@

/**
* Validates that the given xml_node is still either the root node or one of its
         * direct descendants. 
         * 
* @param root_node The root of the node hierarchy to examine.
* @param xml_node The XML node to find.
* @return Returns xml_node if it is, otherwise returns null.
//Synthetic comment -- @@ -291,7 +287,7 @@
* This listener synchronizes the UI (i.e. the checkbox) with the
* actual presence of the application XML node.
*/
    private class AppNodeUpdateListener implements IUiUpdateListener {        
public void uiElementNodeUpdated(UiElementNode ui_node, UiUpdateState state) {
// The UiElementNode for the application XML node always exists, even
// if there is no corresponding XML node in the XML file.
//Synthetic comment -- @@ -307,7 +303,7 @@
} finally {
mInternalModification = false;
}
            
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/UiElementPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/UiElementPart.java
//Synthetic comment -- index 1ad9525..5e7ca30 100644

//Synthetic comment -- @@ -267,7 +267,7 @@
@Override
public void commit(boolean onSave) {
if (mUiElementNode != null) {
            mEditor.editXmlModel(new Runnable() {
public void run() {
for (UiAttributeNode ui_attr : mUiElementNode.getUiAttributes()) {
ui_attr.commit();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiActions.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiActions.java
//Synthetic comment -- index da6db1a..60ec130 100644

//Synthetic comment -- @@ -155,7 +155,7 @@
: "Remove element from Android XML",
String.format("Do you really want to remove %1$s?", sb.toString()))) {
commitPendingXmlChanges();
            getRootNode().getEditor().editXmlModel(new Runnable() {
public void run() {
UiElementNode previous = null;
UiElementNode parent = null;
//Synthetic comment -- @@ -205,7 +205,7 @@
}

commitPendingXmlChanges();
            getRootNode().getEditor().editXmlModel(new Runnable() {
public void run() {
Node xml_node = node.getXmlNode();
if (xml_node != null) {
//Synthetic comment -- @@ -285,7 +285,7 @@
}

commitPendingXmlChanges();
            getRootNode().getEditor().editXmlModel(new Runnable() {
public void run() {
Node xml_node = node.getXmlNode();
if (xml_node != null) {
//Synthetic comment -- @@ -374,7 +374,7 @@
final UiElementNode uiNew = uiParent.insertNewUiChild(index, descriptor);
UiElementNode rootNode = getRootNode();

        rootNode.getEditor().editXmlModel(new Runnable() {
public void run() {
DescriptorsUtils.setDefaultLayoutAttributes(uiNew, updateLayout);
Node xmlNode = uiNew.createXmlNode();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java
//Synthetic comment -- index 9d0927a..dcb451c 100644

//Synthetic comment -- @@ -1256,7 +1256,7 @@
* Note that the caller MUST ensure that modifying the underlying XML model is
* safe and must take care of marking the model as dirty if necessary.
*
     * @see AndroidXmlEditor#editXmlModel(Runnable)
*
* @param uiAttr The attribute node to commit. Must be a child of this UiElementNode.
* @param newValue The new value to set.
//Synthetic comment -- @@ -1302,7 +1302,7 @@
* Note that the caller MUST ensure that modifying the underlying XML model is
* safe and must take care of marking the model as dirty if necessary.
*
     * @see AndroidXmlEditor#editXmlModel(Runnable)
*
* @return True if one or more values were actually modified or removed,
*         false if nothing changed.
//Synthetic comment -- @@ -1662,7 +1662,7 @@

final UiAttributeNode fAttribute = attribute;
AndroidXmlEditor editor = getEditor();
            editor.editXmlModel(new Runnable() {
public void run() {
commitAttributeToXml(fAttribute, newValue);
}







