/*Move the "Show Included In" menu item

The "Show Included In" context menu was grouped as part of the
view-specific actions (setting width, height, orientation,
etc). That's not really a good place since this option is really about
the view as a whole. This changeset moves the item down to the bottom
of the context menu, next to the "Show In" action (which lets you show
the content in another Eclipse view.)

The code was tweaked a bit to be computed lazily (e.g. the contents of
the include list is not computed until the actual Show Included menu
is opened.)

Change-Id:Ic18997271e9ffde491a1a63e337ff015d762a20a*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java
//Synthetic comment -- index e37b0ae..6a85bed 100644

//Synthetic comment -- @@ -19,26 +19,19 @@
import com.android.ide.common.api.IMenuCallback;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.MenuAction;
import com.android.ide.common.rendering.api.Capability;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder.Reference;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

import java.util.ArrayList;
import java.util.HashMap;
//Synthetic comment -- @@ -207,88 +200,6 @@
}
}
}

        // Showing includes is not supported for all the targeted platforms
        if (mEditor.getGraphicalEditor().renderingSupports(Capability.EMBEDDED_LAYOUT)) {
            insertShowIncludedMenu(endId);
        }
    }

    /**
     * Inserts a "Show Included In" context menu, if the current view is included in this
     * view.
     */
    private void insertShowIncludedMenu(String beforeId) {
        IFile file = mEditor.getGraphicalEditor().getEditedFile();
        IProject project = file.getProject();
        final List<Reference> includedBy = IncludeFinder.get(project).getIncludedBy(file);

        Action includeAction = new Action("Show Included In", IAction.AS_DROP_DOWN_MENU) {
            @Override
            public IMenuCreator getMenuCreator() {
                return new IMenuCreator() {
                    private Menu mMenu;

                    public void dispose() {
                        if (mMenu != null) {
                            mMenu.dispose();
                            mMenu = null;
                        }
                    }

                    public Menu getMenu(Control parent) {
                        return null;
                    }

                    public Menu getMenu(Menu parent) {
                        mMenu = new Menu(parent);
                        if (includedBy != null && includedBy.size() > 0) {
                            for (final Reference reference : includedBy) {
                                String title = reference.getDisplayName();
                                IAction action = new ShowWithinAction(title, reference);
                                new ActionContributionItem(action).fill(mMenu, -1);
                            }
                            new Separator().fill(mMenu, -1);
                        }
                        IAction action = new ShowWithinAction("Nothing", null);
                        if (includedBy == null || includedBy.size() == 0) {
                            action.setEnabled(false);
                        }
                        new ActionContributionItem(action).fill(mMenu, -1);

                        return mMenu;
                    }

                };
            }
        };
        mMenuManager.insertBefore(beforeId, includeAction);
    }

    private class ShowWithinAction extends Action {
        private Reference mReference;

        public ShowWithinAction(String title, Reference reference) {
            super(title, IAction.AS_RADIO_BUTTON);
            mReference = reference;
        }

        @Override
        public boolean isChecked() {
            Reference within = mEditor.getGraphicalEditor().getIncludedWithin();
            if (within == null) {
                return mReference == null;
            } else {
                return within.equals(mReference);
            }
        }

        @Override
        public void run() {
            if (!isChecked()) {
                mEditor.getGraphicalEditor().showIn(mReference);
            }
        }
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index b3d9311..8283415 100755

//Synthetic comment -- @@ -1124,6 +1124,10 @@

manager.add(new Separator());

// Create a "Show In" sub-menu and automatically populate it using standard
// actions contributed by the workbench.
String showInLabel = IDEWorkbenchMessages.Workbench_showIn;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ShowWithinMenuAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ShowWithinMenuAction.java
new file mode 100644
//Synthetic comment -- index 0000000..f51fd8e

//Synthetic comment -- @@ -0,0 +1,122 @@







