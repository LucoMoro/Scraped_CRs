/*Front property sheet on outline double click

When you double click on an item in the Outline tree view, open and/or
front the Property Sheet showing the selected item.

This fixes:
13151: Double clicking on an item in the Outline window used to switch
to the Properties tab, but no longer does so with ADT 8.0+

Change-Id:Ic1912d15383ecf15020dc306d2692b2d15e08d00*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java
//Synthetic comment -- index 8ae5c4d..93d32fd 100755

//Synthetic comment -- @@ -32,6 +32,8 @@
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
//Synthetic comment -- @@ -56,8 +58,11 @@
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

//Synthetic comment -- @@ -155,6 +160,17 @@
return false;
}
});

mDragSource = LayoutCanvas.createDragSource(getControl());
mDragSource.addDragListener(new DelegateDragListener());







