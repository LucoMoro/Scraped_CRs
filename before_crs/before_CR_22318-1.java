/*Highlight current drop target in the outline

During a canvas drag & drop operation, highlight the current target
node in the outline. This helps make it obvious which target you are
hitting in cases where it's ambiguous, such as when you have a
hierarchy of parents with mostly overlapping bounds, such as a
TableLayout with TableRows inside.

Change-Id:I7fc4aa23afccb7da4ab5eba14730c7a2c8064782*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/MoveGesture.java
//Synthetic comment -- index 07ab41c..a2cdbe6 100644

//Synthetic comment -- @@ -24,6 +24,9 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
//Synthetic comment -- @@ -120,6 +123,15 @@
return Collections.<Overlay> singletonList(mOverlay);
}

/*
* The cursor has entered the drop target boundaries.
* {@inheritDoc}
//Synthetic comment -- @@ -658,6 +670,26 @@
if (needRedraw || (mFeedback != null && mFeedback.requestPaint)) {
mCanvas.redraw();
}
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlineDropListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlineDropListener.java
//Synthetic comment -- index f591149..d1ca8a9 100644

//Synthetic comment -- @@ -155,7 +155,7 @@
}
// Select the newly dropped nodes
final SelectionManager selectionManager = canvas.getSelectionManager();
        selectionManager.updateOutlineSelection(added);

canvas.redraw();









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java
//Synthetic comment -- index 3c1bd6a..9c27f6e 100755

//Synthetic comment -- @@ -806,7 +806,7 @@
}
}

                    selectionManager.updateOutlineSelection(added);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java
//Synthetic comment -- index bcc433d..3fdbe5a 100644

//Synthetic comment -- @@ -143,12 +143,7 @@
for (SelectionItem cs : mSelections) {
CanvasViewInfo vi = cs.getViewInfo();
if (vi != null) {
                ArrayList<Object> segments = new ArrayList<Object>();
                while (vi != null) {
                    segments.add(0, vi);
                    vi = vi.getParent();
                }
                paths.add(new TreePath(segments.toArray()));
}
}

//Synthetic comment -- @@ -156,6 +151,22 @@
}

/**
* Sets the selection. It must be an {@link ITreeSelection} where each segment
* of the tree path is a {@link CanvasViewInfo}. A null selection is considered
* as an empty selection.
//Synthetic comment -- @@ -811,21 +822,28 @@
* Update the outline selection to select the given nodes, asynchronously.
* @param nodes The nodes to be selected
*/
    public void updateOutlineSelection(final List<INode> nodes) {
Display.getDefault().asyncExec(new Runnable() {
public void run() {
selectDropped(nodes);
                OutlinePage outlinePage = mCanvas.getOutlinePage();
                IWorkbenchPartSite site = outlinePage.getEditor().getSite();
                ISelectionProvider selectionProvider = site.getSelectionProvider();
                ISelection selection = selectionProvider.getSelection();
                if (selection != null) {
                    outlinePage.setSelection(selection);
                }
}
});
}

private void updateMenuActions() {
boolean hasSelection = !mSelections.isEmpty();
mCanvas.updateMenuActionState(hasSelection);
//Synthetic comment -- @@ -943,7 +961,7 @@
for (SelectionItem item : getSelections()) {
nodes.add(item.getNode());
}
            updateOutlineSelection(nodes);
}
}
}







