/*ADT GLE2: Outline with selection synchronized from canvas.

Next CL will be selection synchro outline->canvas.

Change-Id:Ic86b3ae4bd8a86d4d81ee5efb5f0208d7683d096*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java
//Synthetic comment -- index 63246bd..0d3310e 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gle1.GraphicalLayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gle1.UiContentOutlinePage;
import com.android.ide.eclipse.adt.internal.editors.layout.gle1.UiPropertySheetPage;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.OutlinePage2;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.ui.tree.UiActions;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
//Synthetic comment -- @@ -70,7 +71,8 @@
private IGraphicalLayoutEditor mGraphicalEditor;
private int mGraphicalEditorIndex;
/** Implementation of the {@link IContentOutlinePage} for this editor */
    private UiContentOutlinePage mOutline1;
    private IContentOutlinePage mOutline;
/** Custom implementation of {@link IPropertySheetPage} for this editor */
private UiPropertySheetPage mPropertyPage;

//Synthetic comment -- @@ -267,8 +269,8 @@
selectDefaultPage(Integer.toString(currentPage));

// update the outline
        if (mOutline1 != null && mGraphicalEditor != null) {
            mOutline1.reloadModel();
}
}

//Synthetic comment -- @@ -291,8 +293,8 @@
mGraphicalEditor.onXmlModelChanged();
}

        if (mOutline1 != null) {
            mOutline1.reloadModel();
}
}

//Synthetic comment -- @@ -308,10 +310,13 @@
if (IContentOutlinePage.class == adapter && mGraphicalEditor != null) {

if (mOutline == null && mGraphicalEditor instanceof GraphicalLayoutEditor) {
                mOutline1 = new UiContentOutlinePage(
(GraphicalLayoutEditor) mGraphicalEditor,
new TreeViewer());
                mOutline = mOutline1;

            } else if (mOutline == null && mGraphicalEditor instanceof GraphicalEditorPart) {
                mOutline = new OutlinePage2();
}

return mOutline;
//Synthetic comment -- @@ -532,8 +537,8 @@
mUiRootNode.reloadFromXmlNode(mUiRootNode.getXmlDocument());
}

        if (mOutline1 != null) {
            mOutline1.reloadModel();
}

if (mGraphicalEditor != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ContentOutline2.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ContentOutline2.java
deleted file mode 100755
//Synthetic comment -- index c284b3b..0000000

//Synthetic comment -- @@ -1,89 +0,0 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index fb5ee04..a8a86a3 100755

//Synthetic comment -- @@ -32,6 +32,7 @@
import com.android.layoutlib.api.ILayoutResult;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.dnd.Clipboard;
//Synthetic comment -- @@ -64,6 +65,7 @@
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
//Synthetic comment -- @@ -189,6 +191,9 @@

private DragSource mSource;

    /** The current Outline Page, to synchronize the selection both ways. */
    private OutlinePage2 mOutlinePage;

public LayoutCanvas(LayoutEditor layoutEditor, RulesEngine rulesEngine, Composite parent, int style) {
super(parent, style | SWT.DOUBLE_BUFFERED | SWT.V_SCROLL | SWT.H_SCROLL);
mLayoutEditor = layoutEditor;
//Synthetic comment -- @@ -256,6 +261,12 @@
SimpleXmlTransfer.getInstance()
} );
mSource.addDragListener(new CanvasDragSourceListener());

        // Get the outline associated with this editor, if any and of the right type.
        Object outline = layoutEditor.getAdapter(IContentOutlinePage.class);
        if (outline instanceof OutlinePage2) {
            mOutlinePage = (OutlinePage2) outline;
        }
}

@Override
//Synthetic comment -- @@ -341,6 +352,7 @@
setImage(result.getImage());

updateNodeProxies(mLastValidViewInfoRoot);
            mOutlinePage.setModel(mLastValidViewInfoRoot);

// Check if the selection is still the same (based on the object keys)
// and eventually recompute their bounds.
//Synthetic comment -- @@ -359,6 +371,7 @@
it.add(new CanvasSelection(vi, mRulesEngine, mNodeFactory));
}
}
            updateOulineSelection();

// remove the current alternate selection views
mAltSelection = null;
//Synthetic comment -- @@ -432,6 +445,8 @@
selectAllViewInfos(mLastValidViewInfoRoot);
redraw();
}

        updateOulineSelection();
}

/**
//Synthetic comment -- @@ -735,7 +750,7 @@
}
}

            if (mShowOutline && mLastValidViewInfoRoot != null) {
gc.setForeground(mOutlineColor);
gc.setLineStyle(SWT.LINE_DOT);
drawOutline(gc, mLastValidViewInfoRoot);
//Synthetic comment -- @@ -866,6 +881,7 @@

// otherwise add it.
mSelections.add(new CanvasSelection(vi, mRulesEngine, mNodeFactory));
                    updateOulineSelection();
redraw();
}

//Synthetic comment -- @@ -890,6 +906,7 @@
CanvasViewInfo vi2 = mAltSelection.getCurrent();
if (vi2 != null) {
mSelections.addFirst(new CanvasSelection(vi2, mRulesEngine, mNodeFactory));
                        updateOulineSelection();
}
} else {
// We're trying to cycle through the current alternate selection.
//Synthetic comment -- @@ -901,6 +918,7 @@
vi2 = mAltSelection.getNext();
if (vi2 != null) {
mSelections.addFirst(new CanvasSelection(vi2, mRulesEngine, mNodeFactory));
                        updateOulineSelection();
}
}
redraw();
//Synthetic comment -- @@ -935,10 +953,15 @@
if (vi != null) {
mSelections.add(new CanvasSelection(vi, mRulesEngine, mNodeFactory));
}
        updateOulineSelection();
redraw();
}

    /**
     * Deselects a view info.
     * Returns true if the object was actually selected.
     * Callers are responsible for calling redraw() and updateOulineSelection() after.
     */
private boolean deselect(CanvasViewInfo canvasViewInfo) {
if (canvasViewInfo == null) {
return false;
//Synthetic comment -- @@ -955,7 +978,10 @@
return false;
}

    /**
     * Deselects multiple view infos.
     * Callers are responsible for calling redraw() and updateOulineSelection() after.
     */
private void deselectAll(List<CanvasViewInfo> canvasViewInfos) {
for (ListIterator<CanvasSelection> it = mSelections.listIterator(); it.hasNext(); ) {
CanvasSelection s = it.next();
//Synthetic comment -- @@ -1078,6 +1104,33 @@
}
}

    /**
     * Update the selection in the outline page to match the current one from {@link #mSelections}
     */
    private void updateOulineSelection() {
        if (mOutlinePage == null) {
            return;
        }

        if (mSelections.size() == 0) {
            mOutlinePage.selectAndReveal(null);
            return;
        }

        ArrayList<CanvasViewInfo> selectedVis = new ArrayList<CanvasViewInfo>();
        for (CanvasSelection cs : mSelections) {
            CanvasViewInfo vi = cs.getViewInfo();
            if (vi != null) {
                selectedVis.add(vi);
            }
        }

        mOutlinePage.selectAndReveal(selectedVis.toArray(new CanvasViewInfo[selectedVis.size()]));
    }


    //---------------

private class CanvasDragSourceListener implements DragSourceListener {

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java
new file mode 100755
//Synthetic comment -- index 0000000..adc9452

//Synthetic comment -- @@ -0,0 +1,265 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.ui.ErrorImageComposite;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import java.util.ArrayList;

/**
 * An outline page for the GLE2 canvas view.
 * <p/>
 * The page is created by {@link LayoutEditor}.
 * Selection is synchronized by {@link LayoutCanvas}.
 */
public class OutlinePage2 implements IContentOutlinePage {

    private TreeViewer mTreeViewer;
    private UiDocumentNode mUiDocumentNode;

    public OutlinePage2() {
    }

    public void createControl(Composite parent) {
        Tree tree = new Tree(parent, SWT.MULTI /*style*/);
        mTreeViewer = new TreeViewer(tree);

        mTreeViewer.setContentProvider(new ContentProvider());
        mTreeViewer.setLabelProvider(new LabelProvider());
    }

    public void dispose() {
        Control c = getControl();
        if (c != null && !c.isDisposed()) {
            mTreeViewer = null;
            c.dispose();
        }
    }

    public void setModel(CanvasViewInfo rootViewInfo) {
        mTreeViewer.setInput(rootViewInfo);
    }

    public Control getControl() {
        return mTreeViewer == null ? null : mTreeViewer.getControl();
    }

    public ISelection getSelection() {
        return mTreeViewer == null ? null : mTreeViewer.getSelection();
    }

    /**
     * Selects the given {@link CanvasViewInfo} elements and reveals them.
     *
     * @param selectedInfos The {@link CanvasViewInfo} elements to selected.
     *   This can be null or empty to remove any selection.
     */
    public void selectAndReveal(CanvasViewInfo[] selectedInfos) {
        if (mTreeViewer == null) {
            return;
        }

        if (selectedInfos == null || selectedInfos.length == 0) {
            mTreeViewer.setSelection(TreeSelection.EMPTY);
            return;
        }

        int n = selectedInfos.length;
        TreePath[] paths = new TreePath[n];
        for (int i = 0; i < n; i++) {
            ArrayList<Object> segments = new ArrayList<Object>();
            CanvasViewInfo vi = selectedInfos[i];
            while (vi != null) {
                segments.add(0, vi);
                vi = vi.getParent();
            }
            paths[i] = new TreePath(segments.toArray());
        }

        mTreeViewer.setSelection(new TreeSelection(paths), true /*reveal*/);
    }

    public void setSelection(ISelection selection) {
        if (mTreeViewer != null) {
            mTreeViewer.setSelection(selection);
        }
    }

    public void setFocus() {
        Control c = getControl();
        if (c != null) {
            c.setFocus();
        }
    }

    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        if (mTreeViewer != null) {
            mTreeViewer.addSelectionChangedListener(listener);
        }
    }

    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
        if (mTreeViewer != null) {
            mTreeViewer.removeSelectionChangedListener(listener);
        }
    }

    public void setActionBars(IActionBars barts) {
        // TODO Auto-generated method stub
    }

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

        public Object getParent(Object element) {
            if (element instanceof CanvasViewInfo) {
                return ((CanvasViewInfo) element).getParent();
            }
            return null;
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
        }

        public void dispose() {
            // pass
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            // pass
        }

    }

    /**
     * Label provider for the Outline model.
     * Objects are going to be {@link CanvasViewInfo}.
     */
    private static class LabelProvider implements ILabelProvider {

        /**
         * Returns the element's logo with a fallback on the android logo.
         */
        public Image getImage(Object element) {
            if (element instanceof CanvasViewInfo) {
                element = ((CanvasViewInfo) element).getUiViewKey();
            }

            if (element instanceof UiElementNode) {
                UiElementNode node = (UiElementNode) element;
                ElementDescriptor desc = node.getDescriptor();
                if (desc != null) {
                    Image img = desc.getIcon();
                    if (img != null) {
                        if (node.hasError()) {
                            return new ErrorImageComposite(img).createImage();
                        } else {
                            return img;
                        }
                    }
                }
            }

            return AdtPlugin.getAndroidLogo();
        }

        /**
         * Uses UiElementNode.shortDescription for the label for this tree item.
         */
        public String getText(Object element) {
            if (element instanceof CanvasViewInfo) {
                element = ((CanvasViewInfo) element).getUiViewKey();
            }

            if (element instanceof UiElementNode) {
                UiElementNode node = (UiElementNode) element;
                return node.getShortDescription();
            }

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
//Synthetic comment -- index 71ef9c0..55ab5c1 100644

//Synthetic comment -- @@ -28,8 +28,8 @@
* UiModelTreeContentProvider is a trivial implementation of {@link ITreeContentProvider}
* where elements are expected to be instances of {@link UiElementNode}.
*/
public class UiModelTreeContentProvider implements ITreeContentProvider {

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








