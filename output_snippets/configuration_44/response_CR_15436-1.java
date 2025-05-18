//<Beginning of snippet n. 0>
private IGraphicalLayoutEditor mGraphicalEditor;
private int mGraphicalEditorIndex;
private UiContentOutlinePage mOutline1;
private IContentOutlinePage mOutline;
private UiPropertySheetPage mPropertyPage;
private List<Object> selectedNodes = new ArrayList<>();
private Set<Object> expandedNodes = new HashSet<>();

createAndroidPages();
selectDefaultPage(Integer.toString(currentPage));

if (mOutline1 != null && mGraphicalEditor != null) {
    saveCurrentState();
    mOutline1.reloadModel();
    mOutline1.setModel(mOutline1.getRootViewInfo());
    restoreState();
}

if (mGraphicalEditor != null) {
    mGraphicalEditor.onXmlModelChanged();
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
        saveCurrentState();
        mOutline1.reloadModel();
        mOutline1.setModel(mOutline1.getRootViewInfo());
        restoreState();
    }
}

// State preservation methods
private void saveCurrentState() {
    try {
        selectedNodes.clear();
        expandedNodes.clear();
        if (mOutline1 != null) {
            IStructuredSelection selection = mOutline1.getTreeViewer().getStructuredSelection();
            selectedNodes.addAll(selection.toList());
            for (Object node : selectedNodes) {
                if (mOutline1.getTreeViewer().getExpandedState(node)) {
                    expandedNodes.add(node);
                }
            }
        }
    } catch (Exception e) {
        // Log error instead of silent catch
        e.printStackTrace();
    }
}

private void restoreState() {
    try {
        if (mOutline1 != null) {
            for (Object node : selectedNodes) {
                mOutline1.getTreeViewer().setSelection(new StructuredSelection(node), true);
            }
            for (Object node : expandedNodes) {
                mOutline1.getTreeViewer().expandToLevel(node, 1);
            }
        }
    } catch (Exception e) {
        // Log error instead of silent catch
        e.printStackTrace();
    }
}
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
    }

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
    return mTreeViewer.getControl(); 
}

public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    // pass
}

private static class ContentProvider implements ITreeContentProvider {
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof CanvasViewInfo) {
            return ((CanvasViewInfo) parentElement).getChildren().toArray();
        }
        return new Object[0];
    }

    public boolean hasChildren(Object element) {
        return element instanceof CanvasViewInfo && ((CanvasViewInfo) element).getChildren().size() > 0;
    }

    public Object[] getElements(Object inputElement) {
        return getChildren(inputElement);
    }
}

public void addListener(ILabelProviderListener listener) {
    // Implementation
}

public void dispose() {
    // Implementation
}

public boolean isLabelProperty(Object element, String property) {
    return false;
}

public void removeListener(ILabelProviderListener listener) {
    // Implementation
}

//<End of snippet n. 2>

//<Beginning of snippet n. 3>
public class UiModelTreeContentProvider implements ITreeContentProvider {
    // Implementation
}
//<End of snippet n. 3>