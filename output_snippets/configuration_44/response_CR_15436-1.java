//<Beginning of snippet n. 0>
private IGraphicalLayoutEditor mGraphicalEditor;
private int mGraphicalEditorIndex;
private UiContentOutlinePage mOutline1;
private IContentOutlinePage mOutline;
private UiPropertySheetPage mPropertyPage;
private Set<CanvasViewInfo> selectedNodes = new HashSet<>();
private Set<CanvasViewInfo> expandedNodes = new HashSet<>();

createAndroidPages();
selectDefaultPage(Integer.toString(currentPage));

boolean outlineInitialized = false;

if (mOutline1 != null && mGraphicalEditor != null) {
    saveState();
    outlineInitialized = true;
}

mGraphicalEditor.onXmlModelChanged();

if (IContentOutlinePage.class == adapter && mGraphicalEditor != null) {
    if (mOutline == null && mGraphicalEditor instanceof GraphicalLayoutEditor) {
        mOutline1 = new UiContentOutlinePage(
            (GraphicalLayoutEditor) mGraphicalEditor,
            new TreeViewer());
        mOutline = mOutline1;
        outlineInitialized = true;
    } else if (mOutline == null && mGraphicalEditor instanceof GraphicalEditorPart) {
        mOutline = new OutlinePage2();
        mUiRootNode.reloadFromXmlNode(mUiRootNode.getXmlDocument());
        outlineInitialized = true;
    }
}

if (outlineInitialized) {
    mOutline1.reloadModel();
    restoreState();
}

// Other logic...
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
*/
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

public void saveState() {
    selectedNodes.clear();
    expandedNodes.clear();
    for (CanvasViewInfo node : mTreeViewer.getElements()) {
        if (mTreeViewer.getSelection().contains(node)) {
            selectedNodes.add(node);
        }
        if (mTreeViewer.getExpandedElements().contains(node)) {
            expandedNodes.add(node);
        }
    }
}

public void restoreState() {
    if (selectedNodes.isEmpty() || expandedNodes.isEmpty()) {
        return;
    }
    for (CanvasViewInfo node : selectedNodes) {
        mTreeViewer.setSelection(new TreeSelection(node), true);
    }
    for (CanvasViewInfo node : expandedNodes) {
        mTreeViewer.expandToLevel(node, TreeViewer.ALL_LEVELS);
    }
}

// Content provider for the Outline model
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

// Label provider implementation...

//<End of snippet n. 2>

//<Beginning of snippet n. 3>
public class UiModelTreeContentProvider implements ITreeContentProvider {
    // Implementation...
}
//<End of snippet n. 3>