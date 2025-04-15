/*ADT GLE2: Outline Page, display root, preserve state.

- Correctly displays the root element of the layout.
- Preserve select and expanded state when reloading layout.

Change-Id:Ic5786d08fe4e36f02ff658f23e66c66f372f58f8*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java
//Synthetic comment -- index 0d3310e..69b69f2 100644

//Synthetic comment -- @@ -70,8 +70,12 @@

private IGraphicalLayoutEditor mGraphicalEditor;
private int mGraphicalEditorIndex;
/** Implementation of the {@link IContentOutlinePage} for this editor */
    private UiContentOutlinePage mOutline1;
private IContentOutlinePage mOutline;
/** Custom implementation of {@link IPropertySheetPage} for this editor */
private UiPropertySheetPage mPropertyPage;
//Synthetic comment -- @@ -268,9 +272,9 @@
createAndroidPages();
selectDefaultPage(Integer.toString(currentPage));

        // update the outline
        if (mOutline1 != null && mGraphicalEditor != null) {
            mOutline1.reloadModel();
}
}

//Synthetic comment -- @@ -293,8 +297,9 @@
mGraphicalEditor.onXmlModelChanged();
}

        if (mOutline1 != null) {
            mOutline1.reloadModel();
}
}

//Synthetic comment -- @@ -310,10 +315,13 @@
if (IContentOutlinePage.class == adapter && mGraphicalEditor != null) {

if (mOutline == null && mGraphicalEditor instanceof GraphicalLayoutEditor) {
                mOutline1 = new UiContentOutlinePage(
(GraphicalLayoutEditor) mGraphicalEditor,
new TreeViewer());
                mOutline = mOutline1;

} else if (mOutline == null && mGraphicalEditor instanceof GraphicalEditorPart) {
mOutline = new OutlinePage2();
//Synthetic comment -- @@ -537,8 +545,8 @@
mUiRootNode.reloadFromXmlNode(mUiRootNode.getXmlDocument());
}

        if (mOutline1 != null) {
            mOutline1.reloadModel();
}

if (mGraphicalEditor != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index a8a86a3..40c3ec6 100755

//Synthetic comment -- @@ -32,7 +32,6 @@
import com.android.layoutlib.api.ILayoutResult;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.dnd.Clipboard;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java
//Synthetic comment -- index adc9452..06bd9ad 100755

//Synthetic comment -- @@ -19,10 +19,11 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.ui.ErrorImageComposite;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
//Synthetic comment -- @@ -42,6 +43,21 @@

import java.util.ArrayList;

/**
* An outline page for the GLE2 canvas view.
* <p/>
//Synthetic comment -- @@ -51,7 +67,12 @@
public class OutlinePage2 implements IContentOutlinePage {

private TreeViewer mTreeViewer;
    private UiDocumentNode mUiDocumentNode;

public OutlinePage2() {
}
//Synthetic comment -- @@ -60,8 +81,42 @@
Tree tree = new Tree(parent, SWT.MULTI /*style*/);
mTreeViewer = new TreeViewer(tree);

mTreeViewer.setContentProvider(new ContentProvider());
mTreeViewer.setLabelProvider(new LabelProvider());
}

public void dispose() {
//Synthetic comment -- @@ -70,10 +125,15 @@
mTreeViewer = null;
c.dispose();
}
}

public void setModel(CanvasViewInfo rootViewInfo) {
        mTreeViewer.setInput(rootViewInfo);
}

public Control getControl() {
//Synthetic comment -- @@ -110,6 +170,7 @@
vi = vi.getParent();
}
paths[i] = new TreePath(segments.toArray());
}

mTreeViewer.setSelection(new TreeSelection(paths), true /*reveal*/);
//Synthetic comment -- @@ -146,15 +207,50 @@

// ----

/**
* Content provider for the Outline model.
* Objects are going to be {@link CanvasViewInfo}.
*/
private static class ContentProvider implements ITreeContentProvider {

        public Object[] getChildren(Object parentElement) {
            if (parentElement instanceof CanvasViewInfo) {
                return ((CanvasViewInfo) parentElement).getChildren().toArray();
}
return new Object[0];
}
//Synthetic comment -- @@ -168,14 +264,17 @@

public boolean hasChildren(Object element) {
if (element instanceof CanvasViewInfo) {
                return ((CanvasViewInfo) element).getChildren().size() > 0;
}
return false;
}

/**
         * Returns the root elements for the given input.
         * Here the root elements are all the children of the input model.
*/
public Object[] getElements(Object inputElement) {
return getChildren(inputElement);
//Synthetic comment -- @@ -188,7 +287,6 @@
public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
// pass
}

}

/**
//Synthetic comment -- @@ -239,27 +337,22 @@
return element == null ? "(null)" : element.toString();  //$NON-NLS-1$
}

        public void addListener(ILabelProviderListener arg0) {
            // TODO Auto-generated method stub

}

public void dispose() {
            // TODO Auto-generated method stub

}

        public boolean isLabelProperty(Object arg0, String arg1) {
            // TODO Auto-generated method stub
return false;
}

        public void removeListener(ILabelProviderListener arg0) {
            // TODO Auto-generated method stub

}


}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiModelTreeContentProvider.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/ui/tree/UiModelTreeContentProvider.java
//Synthetic comment -- index 55ab5c1..59260ed 100644

//Synthetic comment -- @@ -28,7 +28,7 @@
* UiModelTreeContentProvider is a trivial implementation of {@link ITreeContentProvider}
* where elements are expected to be instances of {@link UiElementNode}.
*/
public class UiModelTreeContentProvider implements ITreeContentProvider {

/** The descriptor of the elements to be displayed as root in this tree view. All elements
*  of the same type in the root will be displayed. */







