/*GLE2: Context menu in Outline view.

Change-Id:I9e149310cd0b0ada5bb16262a2d6d833a54b3c93*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 007b423..fbac3f0 100755

//Synthetic comment -- @@ -1786,10 +1786,10 @@
* <p/>
* The menu has a static part with actions that are always available such as
* copy, cut, paste and show in > explorer. This is created by
     * {@link #createStaticMenuActions(IMenuManager)}.
* <p/>
* There's also a dynamic part that is populated by the groovy rules of the
     * selected elements. This part is created by {@link #populateDynamicContextMenu()}
* when the {@link MenuManager}'s <code>menuAboutToShow</code> method is invoked.
*/
private void createContextMenu() {
//Synthetic comment -- @@ -1803,29 +1803,43 @@
};

// Fill the menu manager with the static actions.
        createStaticMenuActions(mMenuManager);
Menu menu = mMenuManager.createContextMenu(this);
setMenu(menu);

// Remember how many static actions we have. Then each time the menu is
// shown, find dynamic contributions based on the current selection and insert
// them at the beginning of the menu.
        final int numStaticActions = mMenuManager.getSize();
        mMenuManager.addMenuListener(new IMenuListener() {
public void menuAboutToShow(IMenuManager manager) {

// Remove any previous dynamic contributions to keep only the
// default static items.
                int n = mMenuManager.getSize() - numStaticActions;
if (n > 0) {
                    IContributionItem[] items = mMenuManager.getItems();
for (int i = 0; i < n; i++) {
                        mMenuManager.remove(items[i]);
}
}

// Now add all the dynamic menu actions depending on the current selection.
                populateDynamicContextMenu();
}
});

//Synthetic comment -- @@ -1841,7 +1855,7 @@
* created by {@link #setupGlobalActionHandlers()}, so this method must be
* invoked after that one.
*/
    private void createStaticMenuActions(IMenuManager manager) {
manager.removeAll();

manager.add(mCutAction);
//Synthetic comment -- @@ -1869,8 +1883,9 @@
* This is invoked by <code>menuAboutToShow</code> on {@link #mMenuManager}.
* All previous dynamic menu actions have been removed and this method can now insert
* any new actions that depend on the current selection.
*/
    private void populateDynamicContextMenu() {
// Map action-id => action object (one per selected view that defined it)
final TreeMap<String /*id*/, ArrayList<MenuAction>> actionsMap =
new TreeMap<String, ArrayList<MenuAction>>();
//Synthetic comment -- @@ -1881,11 +1896,11 @@
int maxMenuSelection = collectDynamicMenuActions(actionsMap, groupsMap);

// Now create the actual menu contributions
        String endId = mMenuManager.getItems()[0].getId();

Separator sep = new Separator();
sep.setId("-dyn-gle-sep");  //$NON-NLS-1$
        mMenuManager.insertBefore(endId, sep);
endId = sep.getId();

// First create the groups
//Synthetic comment -- @@ -1894,7 +1909,7 @@
String id = group.getId();
MenuManager submenu = new MenuManager(group.getTitle(), id);
menuGroups.put(id, submenu);
            mMenuManager.insertBefore(endId, submenu);
endId = id;
}

//Synthetic comment -- @@ -1940,10 +1955,10 @@

sep = new Separator();
sep.setId("-dyn-gle-sep2");  //$NON-NLS-1$
                        mMenuManager.insertBefore(endId, sep);
endId = sep.getId();
}
                    mMenuManager.insertBefore(endId, contrib);
}
}
}
//Synthetic comment -- @@ -1951,7 +1966,8 @@

/**
* Collects all the {@link MenuAction} contributed by the {@link IViewRule} of the
     * current selection. This is the first step of {@link #populateDynamicContextMenu()}.
*
* @param outActionsMap Map that collects all the contributed actions.
* @param outGroupsMap Map that collects all the contributed groups (sub-menus).
//Synthetic comment -- @@ -2017,7 +2033,7 @@
}

/**
     * Invoked by {@link #populateDynamicContextMenu()} to create a new menu item
* for a {@link MenuAction.Toggle}.
* <p/>
* Toggles are represented by a checked menu item.
//Synthetic comment -- @@ -2056,7 +2072,7 @@
}

/**
     * Invoked by {@link #populateDynamicContextMenu()} to create a new menu item
* for a {@link MenuAction.Choices}.
* <p/>
* Multiple-choices are represented by a sub-menu containing checked items.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java
//Synthetic comment -- index ec945b6..01decfd 100755

//Synthetic comment -- @@ -417,12 +417,10 @@
mMenuManager.add(new DelegateAction(prefix + ActionFactory.DELETE.getId()));
mMenuManager.add(new DelegateAction(prefix + ActionFactory.SELECT_ALL.getId()));

        getControl().setMenu(mMenuManager.createContextMenu(getControl()));

mMenuManager.addMenuListener(new IMenuListener() {
public void menuAboutToShow(IMenuManager manager) {
// Update all actions to match their LayoutCanvas counterparts
                for (IContributionItem contrib : mMenuManager.getItems()) {
if (contrib instanceof ActionContributionItem) {
IAction action = ((ActionContributionItem) contrib).getAction();
if (action instanceof DelegateAction) {
//Synthetic comment -- @@ -432,6 +430,10 @@
}
}
});
}

/**
//Synthetic comment -- @@ -445,6 +447,7 @@

public DelegateAction(String canvasActionId) {
super(canvasActionId);
mCanvasActionId = canvasActionId;
}








