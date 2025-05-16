//<Beginning of snippet n. 0>
private IGraphicalLayoutEditor mGraphicalEditor;
private int mGraphicalEditorIndex;
private UiContentOutlinePage mOutline1;
private IContentOutlinePage mOutline;
private UiPropertySheetPage mPropertyPage;

createAndroidPages();
selectDefaultPage(Integer.toString(currentPage));

if (mOutline1 != null && mGraphicalEditor != null) {
    Object currentSelection = mOutline1.getSelectedElement();
    List<TreePath> expandedPaths = mOutline1.getExpandedPaths();
    mOutline1.reloadModel();

    if (currentSelection != null) {
        mOutline1.setSelection(currentSelection);
    }
    if (expandedPaths != null) {
        mOutline1.setExpandedPaths(expandedPaths);
    }
}

mGraphicalEditor.onXmlModelChanged();

if (mOutline1 != null) {
    Object currentSelection = mOutline1.getSelectedElement();
    List<TreePath> expandedPaths = mOutline1.getExpandedPaths();
    mOutline1.reloadModel();

    if (currentSelection != null) {
        mOutline1.setSelection(currentSelection);
    }
    if (expandedPaths != null) {
        mOutline1.setExpandedPaths(expandedPaths);
    }
}

if (IContentOutlinePage.class == adapter && mGraphicalEditor != null) {
    if (mOutline == null && mGraphicalEditor instanceof GraphicalLayoutEditor) {
        mOutline1 = new UiContentOutlinePage((GraphicalLayoutEditor) mGraphicalEditor, new TreeViewer());
        mOutline = mOutline1;
    } else if (mOutline == null && mGraphicalEditor instanceof GraphicalEditorPart) {
        mOutline = new OutlinePage2();
        mUiRootNode.reloadFromXmlNode(mUiRootNode.getXmlDocument());
    }

    if (mOutline1 != null) {
        Object currentSelection = mOutline1.getSelectedElement();
        List<TreePath> expandedPaths = mOutline1.getExpandedPaths();
        mOutline1.reloadModel();

        if (currentSelection != null) {
            mOutline1.setSelection(currentSelection);
        }
        if (expandedPaths != null) {
            mOutline1.setExpandedPaths(expandedPaths);
        }
    }
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

public class OutlinePage2 implements IContentOutlinePage {
    private TreeViewer mTreeViewer;
    private UiDocumentNode mUiDocumentNode;

    public OutlinePage2() {
        Tree tree = new Tree(parent, SWT.MULTI);
        mTreeViewer = new TreeViewer(tree);
        mTreeViewer.setContentProvider(new ContentProvider());
        mTreeViewer.setLabelProvider(new LabelProvider());
    }

    public void dispose() {
        mTreeViewer = null;
        c.dispose();
    }

    public void setModel(CanvasViewInfo rootViewInfo) {
        mTreeViewer.setInput(rootViewInfo);
    }

    public Control getControl() {
        // Assuming 'parent' is properly defined in the context
        return mTreeViewer.getControl();
    }

    public void refresh() {
        mTreeViewer.refresh();
    }
    
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

        public Object[] getElements(Object inputElement) {
            return getChildren(inputElement);
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            // pass
        }
    }

    public void addListener(ILabelProviderListener listener) {}
    
    public void dispose() {}
    
    public boolean isLabelProperty(Object element, String property) { return false; }
    
    public void removeListener(ILabelProviderListener listener) {}
}
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
public class UiModelTreeContentProvider implements ITreeContentProvider {
    // Implementation code remains the same
}
//<End of snippet n. 3>