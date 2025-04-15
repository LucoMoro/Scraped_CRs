/*GLE2: Extract the dynamic context menu handling.

This is a pure refactoring that extracts the dynamic context
menu handling in a class outside of LayoutCanvas. The original
is long enough like this. It's used by the ouline view too.

Change-Id:I3a9e59bde2e9ceb69479490ca9179aab29cc13a6*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java
new file mode 100755
//Synthetic comment -- index 0000000..da7d643

//Synthetic comment -- @@ -0,0 +1,398 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index fbac3f0..e9708f2 100755

//Synthetic comment -- @@ -19,8 +19,6 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.editors.layout.gscripts.IDragElement;
import com.android.ide.eclipse.adt.editors.layout.gscripts.INode;
import com.android.ide.eclipse.adt.editors.layout.gscripts.IViewRule;
import com.android.ide.eclipse.adt.editors.layout.gscripts.MenuAction;
import com.android.ide.eclipse.adt.editors.layout.gscripts.Point;
import com.android.ide.eclipse.adt.editors.layout.gscripts.Rect;
import com.android.ide.eclipse.adt.editors.layout.gscripts.IDragElement.IDragAttribute;
//Synthetic comment -- @@ -42,11 +40,11 @@
import com.android.sdklib.SdkConstants;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
//Synthetic comment -- @@ -108,24 +106,17 @@
import org.eclipse.wst.xml.core.internal.document.NodeContainer;
import org.w3c.dom.Node;

import groovy.lang.Closure;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
* Displays the image rendered by the {@link GraphicalEditorPart} and handles
//Synthetic comment -- @@ -194,6 +185,9 @@
/** The current selection list. The list is never null, however it can be empty. */
private final LinkedList<CanvasSelection> mSelections = new LinkedList<CanvasSelection>();

/** CanvasSelection border color. Do not dispose, it's a system color. */
private Color mSelectionFgColor;

//Synthetic comment -- @@ -271,6 +265,7 @@
/** Copy action for the Edit or context menu. */
private Action mCopyAction;

private MenuManager mMenuManager;

private CanvasDragSourceListener mDragSourceListener;
//Synthetic comment -- @@ -445,6 +440,20 @@
}

/**
* Sets the result of the layout rendering. The result object indicates if the layout
* rendering succeeded. If it did, it contains a bitmap and the objects rectangles.
*
//Synthetic comment -- @@ -1789,8 +1798,7 @@
* {@link #setupStaticMenuActions(IMenuManager)}.
* <p/>
* There's also a dynamic part that is populated by the groovy rules of the
     * selected elements. This part is created by {@link #populateDynamicContextMenu(MenuManager)}
     * when the {@link MenuManager}'s <code>menuAboutToShow</code> method is invoked.
*/
private void createContextMenu() {

//Synthetic comment -- @@ -1802,50 +1810,14 @@
}
};

        // Fill the menu manager with the static actions.
setupStaticMenuActions(mMenuManager);
        setupDynamicMenuActions(mMenuManager);
Menu menu = mMenuManager.createContextMenu(this);
setMenu(menu);
}

/**
     * Setups the menu manager to receive dynamic menu contributions from the {@link IViewRule}s
     * when it's about to be shown.
     * <p/>
     * Implementation detail: this method is package protected as it is also used by
     * {@link OutlinePage2} to create the exact same dynamic context menu. This means that this
     * methods and all its descendant must <em>not</em> access the local {@link #mMenuManager}
     * variable.
     *
     * @param menuManager The menu manager to modify.
     */
    /*package*/ void setupDynamicMenuActions(final MenuManager menuManager) {
        // Remember how many static actions we have. Then each time the menu is
        // shown, find dynamic contributions based on the current selection and insert
        // them at the beginning of the menu.
        final int numStaticActions = menuManager.getSize();
        menuManager.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {

                // Remove any previous dynamic contributions to keep only the
                // default static items.
                int n = menuManager.getSize() - numStaticActions;
                if (n > 0) {
                    IContributionItem[] items = menuManager.getItems();
                    for (int i = 0; i < n; i++) {
                        menuManager.remove(items[i]);
                    }
                }

                // Now add all the dynamic menu actions depending on the current selection.
                populateDynamicContextMenu(menuManager);
            }
        });

    }

    /**
* Invoked by {@link #createContextMenu()} to create our *static* context menu once.
* <p/>
* The content of the menu itself does not change. However the state of the
//Synthetic comment -- @@ -1880,291 +1852,6 @@
}

/**
     * This is invoked by <code>menuAboutToShow</code> on {@link #mMenuManager}.
     * All previous dynamic menu actions have been removed and this method can now insert
     * any new actions that depend on the current selection.
     * @param menuManager
     */
    private void populateDynamicContextMenu(MenuManager menuManager) {
        // Map action-id => action object (one per selected view that defined it)
        final TreeMap<String /*id*/, ArrayList<MenuAction>> actionsMap =
            new TreeMap<String, ArrayList<MenuAction>>();

        // Map group-id => actions to place in this group.
        TreeMap<String /*id*/, MenuAction.Group> groupsMap = new TreeMap<String, MenuAction.Group>();

        int maxMenuSelection = collectDynamicMenuActions(actionsMap, groupsMap);

        // Now create the actual menu contributions
        String endId = menuManager.getItems()[0].getId();

        Separator sep = new Separator();
        sep.setId("-dyn-gle-sep");  //$NON-NLS-1$
        menuManager.insertBefore(endId, sep);
        endId = sep.getId();

        // First create the groups
        Map<String, MenuManager> menuGroups = new HashMap<String, MenuManager>();
        for (MenuAction.Group group : groupsMap.values()) {
            String id = group.getId();
            MenuManager submenu = new MenuManager(group.getTitle(), id);
            menuGroups.put(id, submenu);
            menuManager.insertBefore(endId, submenu);
            endId = id;
        }

        boolean needGroupSep = !menuGroups.isEmpty();

        // Now fill in the actions
        for (ArrayList<MenuAction> actions : actionsMap.values()) {
            // Filter actions... if we have a multiple selection, only accept actions
            // which are common to *all* the selection which actually returned at least
            // one menu action.
            if (actions == null ||
                    actions.isEmpty() ||
                    actions.size() != maxMenuSelection) {
                continue;
            }

            if (!(actions.get(0) instanceof MenuAction.Action)) {
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
                    if (needGroupSep) {
                        needGroupSep = false;

                        sep = new Separator();
                        sep.setId("-dyn-gle-sep2");  //$NON-NLS-1$
                        menuManager.insertBefore(endId, sep);
                        endId = sep.getId();
                    }
                    menuManager.insertBefore(endId, contrib);
                }
            }
        }
    }

    /**
     * Collects all the {@link MenuAction} contributed by the {@link IViewRule} of the
     * current selection.
     * This is the first step of {@link #populateDynamicContextMenu(MenuManager)}.
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
     * Invoked by {@link #populateDynamicContextMenu(MenuManager)} to create a new menu item
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
     * Invoked by {@link #populateDynamicContextMenu(MenuManager)} to create a new menu item
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
        if (vi == null) {
            return null;
        }

        NodeProxy node = mNodeFactory.create(vi);
        if (node == null) {
            return null;
        }

        List<MenuAction> actions = mRulesEngine.callGetContextMenu(node);
        if (actions == null || actions.size() == 0) {
            return null;
        }

        return actions;
    }


    /**
* Invoked by {@link #mSelectAllAction}. It clears the selection and then
* selects everything (all views and all their children).
*/








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java
//Synthetic comment -- index 01decfd..23fe245 100755

//Synthetic comment -- @@ -431,7 +431,7 @@
}
});

        mGraphicalEditorPart.getCanvasControl().setupDynamicMenuActions(mMenuManager);

getControl().setMenu(mMenuManager.createContextMenu(getControl()));
}







