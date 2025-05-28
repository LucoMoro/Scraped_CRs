
//<Beginning of snippet n. 0>



private IGraphicalLayoutEditor mGraphicalEditor;
private int mGraphicalEditorIndex;
/** Implementation of the {@link IContentOutlinePage} for this editor */
    private UiContentOutlinePage mOutline1;
private IContentOutlinePage mOutline;
/** Custom implementation of {@link IPropertySheetPage} for this editor */
private UiPropertySheetPage mPropertyPage;
createAndroidPages();
selectDefaultPage(Integer.toString(currentPage));

        // update the outline
        if (mOutline1 != null && mGraphicalEditor != null) {
            mOutline1.reloadModel();
}
}

mGraphicalEditor.onXmlModelChanged();
}

        if (mOutline1 != null) {
            mOutline1.reloadModel();
}
}

if (IContentOutlinePage.class == adapter && mGraphicalEditor != null) {

if (mOutline == null && mGraphicalEditor instanceof GraphicalLayoutEditor) {
                mOutline1 = new UiContentOutlinePage(
(GraphicalLayoutEditor) mGraphicalEditor,
new TreeViewer());
                mOutline = mOutline1;

} else if (mOutline == null && mGraphicalEditor instanceof GraphicalEditorPart) {
mOutline = new OutlinePage2();
mUiRootNode.reloadFromXmlNode(mUiRootNode.getXmlDocument());
}

        if (mOutline1 != null) {
            mOutline1.reloadModel();
}

if (mGraphicalEditor != null) {

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import com.android.layoutlib.api.ILayoutResult;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.dnd.Clipboard;

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.ui.ErrorImageComposite;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;

import java.util.ArrayList;

/**
* An outline page for the GLE2 canvas view.
* <p/>
public class OutlinePage2 implements IContentOutlinePage {

private TreeViewer mTreeViewer;
    private UiDocumentNode mUiDocumentNode;

public OutlinePage2() {
}
Tree tree = new Tree(parent, SWT.MULTI /*style*/);
mTreeViewer = new TreeViewer(tree);

mTreeViewer.setContentProvider(new ContentProvider());
mTreeViewer.setLabelProvider(new LabelProvider());
}

public void dispose() {
mTreeViewer = null;
c.dispose();
}
}

public void setModel(CanvasViewInfo rootViewInfo) {
        mTreeViewer.setInput(rootViewInfo);
}

public Control getControl() {
vi = vi.getParent();
}
paths[i] = new TreePath(segments.toArray());
}

mTreeViewer.setSelection(new TreeSelection(paths), true /*reveal*/);

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
public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
// pass
}

}

/**
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

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


* UiModelTreeContentProvider is a trivial implementation of {@link ITreeContentProvider}
* where elements are expected to be instances of {@link UiElementNode}.
*/
public class UiModelTreeContentProvider implements ITreeContentProvider {

/** The descriptor of the elements to be displayed as root in this tree view. All elements
*  of the same type in the root will be displayed. */

//<End of snippet n. 3>








