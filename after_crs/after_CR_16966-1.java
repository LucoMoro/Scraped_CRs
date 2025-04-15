/*GLE2: Fix comments, refactor context menu code.

Simple refactor of the main method populating the dynamic context menu.
It was started to grow a bit too much.
Also added some comments to explain what's going on.

Change-Id:Id58779da5082bb454c1bcae914582e4471364a12*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index b22999e..007b423 100755

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.editors.layout.gscripts.IDragElement;
import com.android.ide.eclipse.adt.editors.layout.gscripts.INode;
import com.android.ide.eclipse.adt.editors.layout.gscripts.IViewRule;
import com.android.ide.eclipse.adt.editors.layout.gscripts.MenuAction;
import com.android.ide.eclipse.adt.editors.layout.gscripts.Point;
import com.android.ide.eclipse.adt.editors.layout.gscripts.Rect;
//Synthetic comment -- @@ -1780,8 +1781,20 @@
action.setDisabledImageDescriptor(wa.getDisabledImageDescriptor());
}

    /**
     * Creates the context menu for the canvas. This is called once from the canvas' constructor.
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

        // This manager is the root of the context menu.
mMenuManager = new MenuManager() {
@Override
public boolean isDynamic() {
//Synthetic comment -- @@ -1789,7 +1802,8 @@
}
};

        // Fill the menu manager with the static actions.
        createStaticMenuActions(mMenuManager);
Menu menu = mMenuManager.createContextMenu(this);
setMenu(menu);

//Synthetic comment -- @@ -1810,6 +1824,7 @@
}
}

                // Now add all the dynamic menu actions depending on the current selection.
populateDynamicContextMenu();
}
});
//Synthetic comment -- @@ -1817,16 +1832,16 @@
}

/**
     * Invoked by {@link #createContextMenu()} to create our *static* context menu once.
* <p/>
* The content of the menu itself does not change. However the state of the
* various items is controlled by their associated actions.
* <p/>
     * For cut/copy/paste/delete/select-all, we explicitly reuse the actions
* created by {@link #setupGlobalActionHandlers()}, so this method must be
* invoked after that one.
*/
    private void createStaticMenuActions(IMenuManager manager) {
manager.removeAll();

manager.add(mCutAction);
//Synthetic comment -- @@ -1840,75 +1855,30 @@

manager.add(new Separator());

        // Create a "Show In" sub-menu and automatically populate it using standard
        // actions contributed by the workbench.
String showInLabel = IDEWorkbenchMessages.Workbench_showIn;
MenuManager showInSubMenu= new MenuManager(showInLabel);
        showInSubMenu.add(
                ContributionItemFactory.VIEWS_SHOW_IN.create(
                        mLayoutEditor.getSite().getWorkbenchWindow()));
manager.add(showInSubMenu);
}

    /**
     * This is invoked by <code>menuAboutToShow</code> on {@link #mMenuManager}.
     * All previous dynamic menu actions have been removed and this method can now insert
     * any new actions that depend on the current selection.
     */
private void populateDynamicContextMenu() {
// Map action-id => action object (one per selected view that defined it)
final TreeMap<String /*id*/, ArrayList<MenuAction>> actionsMap =
new TreeMap<String, ArrayList<MenuAction>>();

// Map group-id => actions to place in this group.
        TreeMap<String /*id*/, MenuAction.Group> groupsMap = new TreeMap<String, MenuAction.Group>();

        int maxMenuSelection = collectDynamicMenuActions(actionsMap, groupsMap);

// Now create the actual menu contributions
String endId = mMenuManager.getItems()[0].getId();
//Synthetic comment -- @@ -1920,7 +1890,7 @@

// First create the groups
Map<String, MenuManager> menuGroups = new HashMap<String, MenuManager>();
        for (MenuAction.Group group : groupsMap.values()) {
String id = group.getId();
MenuManager submenu = new MenuManager(group.getTitle(), id);
menuGroups.put(id, submenu);
//Synthetic comment -- @@ -1950,91 +1920,13 @@
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

//Synthetic comment -- @@ -2058,6 +1950,183 @@
}

/**
     * Collects all the {@link MenuAction} contributed by the {@link IViewRule} of the
     * current selection. This is the first step of {@link #populateDynamicContextMenu()}.
     *
     * @param outActionsMap Map that collects all the contributed actions.
     * @param outGroupsMap Map that collects all the contributed groups (sub-menus).
     * @return The max number of selected items that contributed the same action ID.
     *   This is used later to filter on multiple selections so that we can display only
     *   actions that are common to all selected items that contributed at least one action.
     */
    private int collectDynamicMenuActions(
            final TreeMap<String, ArrayList<MenuAction>> outActionsMap,
            final TreeMap<String, MenuAction.Group> outGroupsMap) {
        int maxMenuSelection = 0;
        for (CanvasSelection selection : mSelections) {
            List<MenuAction> viewActions = null;
            if (selection != null) {
                CanvasViewInfo vi = selection.getViewInfo();
                if (vi != null) {
                    viewActions = getMenuActions(vi);
                }
            }
            if (viewActions == null) {
                continue;
            }

            boolean foundAction = false;
            for (MenuAction action : viewActions) {
                if (action.getId() == null || action.getTitle() == null) {
                    // TODO Log verbose error for invalid action.
                    continue;
                }

                String id = action.getId();

                if (action instanceof MenuAction.Group) {
                    if (!outGroupsMap.containsKey(id)) {
                        outGroupsMap.put(id, (MenuAction.Group) action);
                    }
                    continue;
                }

                ArrayList<MenuAction> actions = outActionsMap.get(id);
                if (actions == null) {
                    actions = new ArrayList<MenuAction>();
                    outActionsMap.put(id, actions);
                }

                // All the actions for the same id should have be equal
                if (!actions.isEmpty()) {
                    if (action.equals(actions.get(0))) {
                        // TODO Log verbose error for invalid type mismatch.
                        continue;
                    }
                }

                actions.add(action);
                foundAction = true;
            }

            if (foundAction) {
                maxMenuSelection++;
            }
        }
        return maxMenuSelection;
    }

    /**
     * Invoked by {@link #populateDynamicContextMenu()} to create a new menu item
     * for a {@link MenuAction.Toggle}.
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
        final boolean isChecked = action.isChecked();
        Action a = new Action(action.getTitle(), IAction.AS_CHECK_BOX) {
            @Override
            public void run() {
                // Invoke the closures of all the actions using the same action-id
                for (MenuAction a2 : actionsMap.get(action.getId())) {
                    if (a2 instanceof MenuAction.Action) {
                        Closure c = ((MenuAction.Action) a2).getAction();
                        if (c != null) {
                            mRulesEngine.callClosure(
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
    }

    /**
     * Invoked by {@link #populateDynamicContextMenu()} to create a new menu item
     * for a {@link MenuAction.Choices}.
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
        MenuManager submenu = new MenuManager(action.getTitle(), action.getId());

        // Convert to a tree map as needed so that keys be naturally ordered.
        if (!(choiceMap instanceof TreeMap<?, ?>)) {
            choiceMap = new TreeMap<String, String>(choiceMap);
        }

        String current = action.getCurrent();
        Set<String> currents = null;
        if (current.indexOf(MenuAction.Choices.CHOICE_SEP) >= 0) {
            currents = new HashSet<String>(
                    Arrays.asList(current.split(
                            Pattern.quote(MenuAction.Choices.CHOICE_SEP))));
            current = null;
        }

        for (Entry<String, String> entry : choiceMap.entrySet() ) {
            final String key = entry.getKey();
            String title = entry.getValue();

            if (key == null || title == null) {
                continue;
            }

            if (MenuAction.Choices.SEPARATOR.equals(title)) {
                submenu.add(new Separator());
                continue;
            }

            final boolean isChecked =
                (currents != null && currents.contains(key)) ||
                key.equals(current);

            Action a = new Action(title, IAction.AS_CHECK_BOX) {
                @Override
                public void run() {
                    // Invoke the closures of all the actions using the same action-id
                    for (MenuAction a2 : actionsMap.get(action.getId())) {
                        if (a2 instanceof MenuAction.Action) {
                            mRulesEngine.callClosure(
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

    /**
* Returns the menu actions computed by the groovy rule associated with this view.
*/
public List<MenuAction> getMenuActions(CanvasViewInfo vi) {







