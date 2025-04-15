/*ADT GLE2: Outline with selection synchronized from canvas.

Next CL will be selection synchro outline->canvas.

Change-Id:I0e395313f0365830db7eb96e33077844d68f301e*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java
//Synthetic comment -- index 63246bd..0d3310e 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gle1.GraphicalLayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle1.UiContentOutlinePage;
import com.android.ide.eclipse.adt.internal.editors.layout.gle1.UiPropertySheetPage;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.ui.tree.UiActions;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
//Synthetic comment -- @@ -70,7 +71,8 @@
private IGraphicalLayoutEditor mGraphicalEditor;
private int mGraphicalEditorIndex;
/** Implementation of the {@link IContentOutlinePage} for this editor */
    private UiContentOutlinePage mOutline;
/** Custom implementation of {@link IPropertySheetPage} for this editor */
private UiPropertySheetPage mPropertyPage;

//Synthetic comment -- @@ -267,8 +269,8 @@
selectDefaultPage(Integer.toString(currentPage));

// update the outline
        if (mOutline != null && mGraphicalEditor != null) {
            mOutline.reloadModel();
}
}

//Synthetic comment -- @@ -291,8 +293,8 @@
mGraphicalEditor.onXmlModelChanged();
}

        if (mOutline != null) {
            mOutline.reloadModel();
}
}

//Synthetic comment -- @@ -308,10 +310,13 @@
if (IContentOutlinePage.class == adapter && mGraphicalEditor != null) {

if (mOutline == null && mGraphicalEditor instanceof GraphicalLayoutEditor) {
                // TODO add support for GLE2
                mOutline = new UiContentOutlinePage(
(GraphicalLayoutEditor) mGraphicalEditor,
new TreeViewer());
}

return mOutline;
//Synthetic comment -- @@ -532,8 +537,8 @@
mUiRootNode.reloadFromXmlNode(mUiRootNode.getXmlDocument());
}

        if (mOutline != null) {
            mOutline.reloadModel();
}

if (mGraphicalEditor != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index fb5ee04..a8a86a3 100755

//Synthetic comment -- @@ -32,6 +32,7 @@
import com.android.layoutlib.api.ILayoutResult;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.dnd.Clipboard;
//Synthetic comment -- @@ -64,6 +65,7 @@
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
//Synthetic comment -- @@ -189,6 +191,9 @@

private DragSource mSource;

public LayoutCanvas(LayoutEditor layoutEditor, RulesEngine rulesEngine, Composite parent, int style) {
super(parent, style | SWT.DOUBLE_BUFFERED | SWT.V_SCROLL | SWT.H_SCROLL);
mLayoutEditor = layoutEditor;
//Synthetic comment -- @@ -256,6 +261,12 @@
SimpleXmlTransfer.getInstance()
} );
mSource.addDragListener(new CanvasDragSourceListener());
}

@Override
//Synthetic comment -- @@ -341,6 +352,7 @@
setImage(result.getImage());

updateNodeProxies(mLastValidViewInfoRoot);

// Check if the selection is still the same (based on the object keys)
// and eventually recompute their bounds.
//Synthetic comment -- @@ -359,6 +371,7 @@
it.add(new CanvasSelection(vi, mRulesEngine, mNodeFactory));
}
}

// remove the current alternate selection views
mAltSelection = null;
//Synthetic comment -- @@ -432,6 +445,8 @@
selectAllViewInfos(mLastValidViewInfoRoot);
redraw();
}
}

/**
//Synthetic comment -- @@ -735,7 +750,7 @@
}
}

            if (mShowOutline) {
gc.setForeground(mOutlineColor);
gc.setLineStyle(SWT.LINE_DOT);
drawOutline(gc, mLastValidViewInfoRoot);
//Synthetic comment -- @@ -866,6 +881,7 @@

// otherwise add it.
mSelections.add(new CanvasSelection(vi, mRulesEngine, mNodeFactory));
redraw();
}

//Synthetic comment -- @@ -890,6 +906,7 @@
CanvasViewInfo vi2 = mAltSelection.getCurrent();
if (vi2 != null) {
mSelections.addFirst(new CanvasSelection(vi2, mRulesEngine, mNodeFactory));
}
} else {
// We're trying to cycle through the current alternate selection.
//Synthetic comment -- @@ -901,6 +918,7 @@
vi2 = mAltSelection.getNext();
if (vi2 != null) {
mSelections.addFirst(new CanvasSelection(vi2, mRulesEngine, mNodeFactory));
}
}
redraw();
//Synthetic comment -- @@ -935,10 +953,15 @@
if (vi != null) {
mSelections.add(new CanvasSelection(vi, mRulesEngine, mNodeFactory));
}
redraw();
}

    /** Deselects a view info. Returns true if the object was actually selected. */
private boolean deselect(CanvasViewInfo canvasViewInfo) {
if (canvasViewInfo == null) {
return false;
//Synthetic comment -- @@ -955,7 +978,10 @@
return false;
}

    /** Deselects multiple view infos, */
private void deselectAll(List<CanvasViewInfo> canvasViewInfos) {
for (ListIterator<CanvasSelection> it = mSelections.listIterator(); it.hasNext(); ) {
CanvasSelection s = it.next();
//Synthetic comment -- @@ -1078,6 +1104,33 @@
}
}

private class CanvasDragSourceListener implements DragSourceListener {

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java
new file mode 100755
//Synthetic comment -- index 0000000..adc9452

//Synthetic comment -- @@ -0,0 +1,265 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiModelTreeContentProvider.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiModelTreeContentProvider.java
//Synthetic comment -- index 71ef9c0..55ab5c1 100644

//Synthetic comment -- @@ -28,8 +28,8 @@
* UiModelTreeContentProvider is a trivial implementation of {@link ITreeContentProvider}
* where elements are expected to be instances of {@link UiElementNode}.
*/
class UiModelTreeContentProvider implements ITreeContentProvider {
    
/** The descriptor of the elements to be displayed as root in this tree view. All elements
*  of the same type in the root will be displayed. */
private ElementDescriptor[] mDescriptorFilters;
//Synthetic comment -- @@ -41,7 +41,7 @@
mUiRootNode = uiRootNode;
mDescriptorFilters = descriptorFilters;
}
    
/* (non-java doc)
* Returns all the UI node children of the given element or null if not the right kind
* of object. */
//Synthetic comment -- @@ -99,7 +99,7 @@
}
}
}
        
return roots.toArray();
}








