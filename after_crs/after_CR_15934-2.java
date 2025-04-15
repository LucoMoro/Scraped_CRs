/*ADT GLE2: add 'Show In' submenu to canvas' context menu.

Change-Id:Ibad11e7872c529e3154b8f0abbfd02d26cac52cc*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 811d896..6a946bd 100755

//Synthetic comment -- @@ -84,7 +84,9 @@
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.TextActionHandler;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
//Synthetic comment -- @@ -1609,6 +1611,7 @@
mMenuManager = new MenuManager();
createMenuAction(mMenuManager);
setMenu(mMenuManager.createContextMenu(this));

/*
TODO insert generated menus/actions here.

//Synthetic comment -- @@ -1775,6 +1778,15 @@
manager.add(mSelectAllAction);

// TODO add view-sensitive menu items.

        manager.add(new Separator());

        String showInLabel = IDEWorkbenchMessages.Workbench_showIn;
        MenuManager showInSubMenu= new MenuManager(showInLabel);
        showInSubMenu.add(
                ContributionItemFactory.VIEWS_SHOW_IN.create(
                        mLayoutEditor.getSite().getWorkbenchWindow()));
        manager.add(showInSubMenu);
}

/**







