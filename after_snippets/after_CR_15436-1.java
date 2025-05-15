
//<Beginning of snippet n. 0>



private IGraphicalLayoutEditor mGraphicalEditor;
private int mGraphicalEditorIndex;
    /**
     * Implementation of the {@link IContentOutlinePage} for this editor.
     * @deprecated Used for backward compatibility with GLE1.
     */
    private UiContentOutlinePage mOutlineForGle1;
/** Implementation of the {@link IContentOutlinePage} for this editor */
private IContentOutlinePage mOutline;
/** Custom implementation of {@link IPropertySheetPage} for this editor */
private UiPropertySheetPage mPropertyPage;
createAndroidPages();
selectDefaultPage(Integer.toString(currentPage));

        // update the GLE1 outline. The GLE2 outline doesn't need this call anymore.
        if (mOutlineForGle1 != null) {
            mOutlineForGle1.reloadModel();
}
}

mGraphicalEditor.onXmlModelChanged();
}

        // update the GLE1 outline. The GLE2 outline doesn't need this call anymore.
        if (mOutlineForGle1 != null) {
            mOutlineForGle1.reloadModel();
}
}

if (IContentOutlinePage.class == adapter && mGraphicalEditor != null) {

if (mOutline == null && mGraphicalEditor instanceof GraphicalLayoutEditor) {
                // Create the GLE1 outline. We need to keep a specific reference to it in order
                // to call its reloadModel() method. The GLE2 outline no longer relies on this
                // and can be casted to the base interface.
                mOutlineForGle1 = new UiContentOutlinePage(
(GraphicalLayoutEditor) mGraphicalEditor,
new TreeViewer());
                mOutline = mOutlineForGle1;

} else if (mOutline == null && mGraphicalEditor instanceof GraphicalEditorPart) {
mOutline = new OutlinePage2();
mUiRootNode.reloadFromXmlNode(mUiRootNode.getXmlDocument());
}

        if (mOutlineForGle1 != null) {
            mOutlineForGle1.reloadModel();
}

if (mGraphicalEditor != null) {

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import com.android.layoutlib.api.ILayoutResult;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.dnd.Clipboard;

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.ui.ErrorImageComposite;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;

import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;

import java.util.ArrayList;

/*
 * TODO -- missing features:
 * - synchronize selection tree=>canvas
 * - right-click context menu *shared* with the one from canvas (simply delegate action)
 * - drag'n'drop initiated from Palette to Outline
 * - drag'n'drop from Outline to Outline
 * - drag'n'drop from Canvas to Outline
 * - drag'n'drop from Outline to Canvas
 * - => Check if we can handle all the d'n'd cases a simply delegating the action to the canvas.
 *      There's a *lot* of logic in the CanvasDropListener we don't want to replicate.
 *      That should be fairly trivial, except that we can't provide X/Y coordinates in the drop
 *      move. We could just fake them by using the topleft/middle point of the tree item's bounds
 *      or something like that.
 */

/**
* An outline page for the GLE2 canvas view.
* <p/>
public class OutlinePage2 implements IContentOutlinePage {

private TreeViewer mTreeViewer;

    /**
     * RootWrapper is a workaround: we can't set the input of the treeview to its root
     * element, so we introduce a fake parent.
     */
    private final RootWrapper mRootWrapper = new RootWrapper();

public OutlinePage2() {
}
Tree tree = new Tree(parent, SWT.MULTI /*style*/);
mTreeViewer = new TreeViewer(tree);

        mTreeViewer.setAutoExpandLevel(2);
mTreeViewer.setContentProvider(new ContentProvider());
mTreeViewer.setLabelProvider(new LabelProvider());
        mTreeViewer.setInput(mRootWrapper);

        // The tree viewer will hold CanvasViewInfo instances, however these
        // change each time the canvas is reloaded. OTOH liblayout gives us
        // constant UiView keys which we can use to perform tree item comparisons.
        mTreeViewer.setComparer(new IElementComparer() {
            public int hashCode(Object element) {
                if (element instanceof CanvasViewInfo) {
                    UiViewElementNode key = ((CanvasViewInfo) element).getUiViewKey();
                    if (key != null) {
                        return key.hashCode();
                    }
                }
                if (element != null) {
                    return element.hashCode();
                }
                return 0;
            }

            public boolean equals(Object a, Object b) {
                if (a instanceof CanvasViewInfo && b instanceof CanvasViewInfo) {
                    UiViewElementNode keyA = ((CanvasViewInfo) a).getUiViewKey();
                    UiViewElementNode keyB = ((CanvasViewInfo) b).getUiViewKey();
                    if (keyA != null) {
                        return keyA.equals(keyB);
                    }
                }
                if (a != null) {
                    return a.equals(b);
                }
                return false;
            }
        });
}

public void dispose() {
mTreeViewer = null;
c.dispose();
}
        mRootWrapper.setRoot(null);
}

public void setModel(CanvasViewInfo rootViewInfo) {
        mRootWrapper.setRoot(rootViewInfo);

        Object[] expanded = mTreeViewer.getExpandedElements();
        mTreeViewer.refresh();
        mTreeViewer.setExpandedElements(expanded);
}

public Control getControl() {
vi = vi.getParent();
}
paths[i] = new TreePath(segments.toArray());
            mTreeViewer.expandToLevel(paths[i], 1);
}

mTreeViewer.setSelection(new TreeSelection(paths), true /*reveal*/);

// ----


    /**
     * In theory, the root of the model should be the input of the {@link TreeViewer},
     * which would be the root {@link CanvasViewInfo}.
     * That means in theory {@link ContentProvider#getElements(Object)} should return
     * its own input as the single root node.
     * <p/>
     * However as described in JFace Bug 9262, this case is not properly handled by
     * a {@link TreeViewer} and leads to an infinite recursion in the tree viewer.
     * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=9262
     * <p/>
     * The solution is to wrap the tree viewer input in a dummy root node that acts
     * as a parent. This class does just that.
     */
    private static class RootWrapper {
        private CanvasViewInfo mRoot;

        public void setRoot(CanvasViewInfo root) {
            mRoot = root;
        }

        public CanvasViewInfo getRoot() {
            return mRoot;
        }
    }

/**
* Content provider for the Outline model.
* Objects are going to be {@link CanvasViewInfo}.
*/
private static class ContentProvider implements ITreeContentProvider {

        public Object[] getChildren(Object element) {
            if (element instanceof RootWrapper) {
                CanvasViewInfo root = ((RootWrapper)element).getRoot();
                if (root != null) {
                    return new Object[] { root };
                }
            }
            if (element instanceof CanvasViewInfo) {
                ArrayList<CanvasViewInfo> children = ((CanvasViewInfo) element).getChildren();
                if (children != null) {
                    return children.toArray();
                }
}
return new Object[0];
}

public boolean hasChildren(Object element) {
if (element instanceof CanvasViewInfo) {
                ArrayList<CanvasViewInfo> children = ((CanvasViewInfo) element).getChildren();
                if (children != null) {
                    return children.size() > 0;
                }
}
return false;
}

/**
         * Returns the root element.
         * Semantically, the root element is the single top-level XML element of the XML layout.
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

        public void addListener(ILabelProviderListener listener) {
            // pass
}

public void dispose() {
            // pass
}

        public boolean isLabelProperty(Object element, String property) {
            // pass
return false;
}

        public void removeListener(ILabelProviderListener listener) {
            // pass
}
}

}

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


* UiModelTreeContentProvider is a trivial implementation of {@link ITreeContentProvider}
* where elements are expected to be instances of {@link UiElementNode}.
*/
class UiModelTreeContentProvider implements ITreeContentProvider {

/** The descriptor of the elements to be displayed as root in this tree view. All elements
*  of the same type in the root will be displayed. */

//<End of snippet n. 3>








