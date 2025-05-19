//<Beginning of snippet n. 0>
private IGraphicalLayoutEditor mGraphicalEditor;
private int mGraphicalEditorIndex;
private UiContentOutlinePage mOutline1;
private IContentOutlinePage mOutline;
private UiPropertySheetPage mPropertyPage;
private boolean isOutline1Selected;
private boolean isOutline1Expanded;

private void updateOutline() {
    if (mOutline1 != null && mGraphicalEditor != null) {
        preserveState();
        mOutline1.reloadModel();
        mOutline1.setModel(mOutline1.getRootViewInfo());
        restoreState();
    }
}

private void preserveState() {
    if (mOutline1 != null) {
        isOutline1Selected = false;
        isOutline1Expanded = false;
        try {
            if (mOutline1.isSelected()) {
                isOutline1Selected = true;
            }
            if (mOutline1.isExpanded()) {
                isOutline1Expanded = true;
            }
        } catch (Exception e) {
            logError("Failed to preserve state", e);
        }
    }
}

private void restoreState() {
    if (mOutline1 != null) {
        try {
            if (isOutline1Selected && mOutline1.isSelected()) {
                mOutline1.select();
            }
            if (isOutline1Expanded && mOutline1.isExpanded()) {
                mOutline1.expand();
            }
        } catch (Exception e) {
            logError("Failed to restore state", e);
        }
    }
}

mGraphicalEditor.onXmlModelChanged();

if (IContentOutlinePage.class == adapter && mGraphicalEditor != null) {
    if (mOutline == null && mGraphicalEditor instanceof GraphicalLayoutEditor) {
        mOutline1 = new UiContentOutlinePage((GraphicalLayoutEditor) mGraphicalEditor, new TreeViewer());
        mOutline = mOutline1;
    } else if (mOutline == null && mGraphicalEditor instanceof GraphicalEditorPart) {
        mOutline = new OutlinePage2();
        mUiRootNode.reloadFromXmlNode(mUiRootNode.getXmlDocument());
    }
    updateOutline();
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
import com.android.ide.eclipse.adt.internal.editors/ui.ErrorImageComposite;
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

    public void createControl(Composite parent) {
        Tree tree = new Tree(parent, SWT.MULTI);
        mTreeViewer = new TreeViewer(tree);
        mTreeViewer.setContentProvider(new ContentProvider());
        mTreeViewer.setLabelProvider(new LabelProvider());
    }

    public void dispose() {
        mTreeViewer.getTree().dispose();
        mTreeViewer = null;
    }
    
    public void setModel(CanvasViewInfo rootViewInfo) {
        mTreeViewer.setInput(rootViewInfo);
    }

    public Control getControl() {
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
            return element instanceof CanvasViewInfo && ((CanvasViewInfo) element).getChildren().size() > 0;
        }

        public Object[] getElements(Object inputElement) {
            return getChildren(inputElement);
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }
    }
    
    public String getText(Object element) {
        return element == null ? "(null)" : element.toString();
    }

    public void addListener(ILabelProviderListener arg0) {
    }

    public void dispose() {
    }

    public boolean isLabelProperty(Object arg0, String arg1) {
        return false;
    }

    public void removeListener(ILabelProviderListener arg0) {
    }
}
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
public class UiModelTreeContentProvider implements ITreeContentProvider {
    // Class content remains unchanged
}
//<End of snippet n. 3>