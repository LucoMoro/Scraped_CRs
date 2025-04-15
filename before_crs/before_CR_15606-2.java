/*ADT GLE2: synchronized selection between canvas, outline and properties.

This CL adds a LayoutCanvasViewer, a JFace viewer wrapping the
LayoutCanvas control. This allows the canvas to participate in the
workbench "site" selection.

To summarize:
- The workbench site selection service can be seen as "centralized"
  service that registers selection providers and selection listeners.
- The editor part and the ouline are selection providers.
- The editor part, the outline and the property sheet are listener
  which all listen to each others indirectly.
- Hilarity ensues.

I tried to add enough javadoc in the classes to explain what's
going on, so please tell me if more is needed. (Editor part also
has a link to the one web page article that brings some sense to
this stuff. I recommend reading the web page first.)

Change-Id:Ief83f9fe2fc1cb5c0c1fa9ae174a58c8daf17ac4*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutEditor.java
//Synthetic comment -- index 69b69f2..7d01fd6 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gle1.UiPropertySheetPage;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.OutlinePage2;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.ui.tree.UiActions;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
//Synthetic comment -- @@ -78,7 +79,7 @@
/** Implementation of the {@link IContentOutlinePage} for this editor */
private IContentOutlinePage mOutline;
/** Custom implementation of {@link IPropertySheetPage} for this editor */
    private UiPropertySheetPage mPropertyPage;

private UiEditorActions mUiEditorActions;

//Synthetic comment -- @@ -303,8 +304,8 @@
}
}

    /* (non-java doc)
     * Returns the IContentOutlinePage when asked for it.
*/
@SuppressWarnings("unchecked")
@Override
//Synthetic comment -- @@ -331,8 +332,11 @@
}

if (IPropertySheetPage.class == adapter && mGraphicalEditor != null) {
            if (mPropertyPage == null) {
mPropertyPage = new UiPropertySheetPage();
}

return mPropertyPage;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java
//Synthetic comment -- index 767a5c6..2781d1a 100755

//Synthetic comment -- @@ -21,21 +21,29 @@
import com.android.layoutlib.api.ILayoutResult.ILayoutViewInfo;

import org.eclipse.swt.graphics.Rectangle;

import java.util.ArrayList;

/**
* Maps a {@link ILayoutViewInfo} in a structure more adapted to our needs.
* The only large difference is that we keep both the original bounds of the view info
 * and we pre-compute the selection bounds which are absolute to the rendered image (where
 * as the original bounds are relative to the parent view.)
* <p/>
 * Each view also know its parent, which should be handy later.
* <p/>
* We can't alter {@link ILayoutViewInfo} as it is part of the LayoutBridge and needs to
* have a fixed API.
*/
public class CanvasViewInfo {

/**
* Minimal size of the selection, in case an empty view or layout is selected.
//Synthetic comment -- @@ -180,9 +188,58 @@
* Returns the name of the {@link CanvasViewInfo}.
* Could be null, although unlikely.
* Experience shows this is the full qualified Java name of the View.
* @see ILayoutViewInfo#getName()
*/
public String getName() {
return mName;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GCWrapper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GCWrapper.java
//Synthetic comment -- index fef9e1b..39e24d1 100755

//Synthetic comment -- @@ -21,7 +21,6 @@
import com.android.ide.eclipse.adt.editors.layout.gscripts.IViewRule;
import com.android.ide.eclipse.adt.editors.layout.gscripts.Point;
import com.android.ide.eclipse.adt.editors.layout.gscripts.Rect;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutCanvas.ScaleTransform;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
//Synthetic comment -- @@ -68,11 +67,11 @@
private int mFontHeight = 0;

/** The scaling of the canvas in X. */
    private final ScaleTransform mHScale;
/** The scaling of the canvas in Y. */
    private final ScaleTransform mVScale;

    public GCWrapper(ScaleTransform hScale, ScaleTransform vScale) {
mHScale = hScale;
mVScale = vScale;
mGc = null;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index f75fa94..d2d82f6 100755

//Synthetic comment -- @@ -65,6 +65,8 @@
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
//Synthetic comment -- @@ -76,6 +78,9 @@
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.ide.IDE;
//Synthetic comment -- @@ -92,29 +97,50 @@

/**
* Graphical layout editor part, version 2.
*
* @since GLE2
*
* TODO List:
* - display error icon
* - finish palette (see palette's todo list)
 * - finish canvas (see canva's todo list)
* - completly rethink the property panel
 * - link to the existing outline editor (prolly reuse/adapt for simplier model and will need
 *   to adapt the selection synchronizer.)
*/
public class GraphicalEditorPart extends EditorPart implements IGraphicalLayoutEditor {

/*
* Useful notes:
* To understand Drag'n'drop:
*   http://www.eclipse.org/articles/Article-Workbench-DND/drag_drop.html
*/

/** Reference to the layout editor */
private final LayoutEditor mLayoutEditor;

    /** reference to the file being edited. Can also be used to access the {@link IProject}. */
private IFile mEditedFile;

/** The current clipboard. Must be disposed later. */
//Synthetic comment -- @@ -125,17 +151,21 @@

/** The sash that splits the palette from the canvas. */
private SashForm mSashPalette;
private SashForm mSashError;

/** The palette displayed on the left of the sash. */
private PaletteComposite mPalette;

/** The layout canvas displayed to the right of the sash. */
    private LayoutCanvas mLayoutCanvas;

/** The Groovy Rules Engine associated with this editor. It is project-specific. */
private RulesEngine mRulesEngine;

private StyledText mErrorLabel;

private Map<String, Map<String, IResourceValue>> mConfiguredFrameworkRes;
//Synthetic comment -- @@ -152,7 +182,7 @@

private ReloadListener mReloadListener;

    protected boolean mUseExplodeMode;


public GraphicalEditorPart(LayoutEditor layoutEditor) {
//Synthetic comment -- @@ -238,7 +268,7 @@
) {
@Override
public void onSelected(boolean newState) {
                        mLayoutCanvas.setShowOutline(newState);
}
}
};
//Synthetic comment -- @@ -254,7 +284,7 @@
mSashError = new SashForm(mSashPalette, SWT.VERTICAL | SWT.BORDER);
mSashError.setLayoutData(new GridData(GridData.FILL_BOTH));

        mLayoutCanvas = new LayoutCanvas(mLayoutEditor, mRulesEngine, mSashError, SWT.NONE);

mErrorLabel = new StyledText(mSashError, SWT.READ_ONLY);
mErrorLabel.setEditable(false);
//Synthetic comment -- @@ -263,12 +293,28 @@

mSashPalette.setWeights(new int[] { 20, 80 });
mSashError.setWeights(new int[] { 80, 20 });
        mSashError.setMaximizedControl(mLayoutCanvas);

setupEditActions();

// Initialize the state
reloadPalette();
}

/**
//Synthetic comment -- @@ -276,7 +322,7 @@
* @param direction +1 for zoom in, -1 for zoom out
*/
private void rescale(int direction) {
        double s = mLayoutCanvas.getScale();

if (direction > 0) {
s = s * 2;
//Synthetic comment -- @@ -284,7 +330,7 @@
s = s / 2;
}

        mLayoutCanvas.setScale(s);

}

//Synthetic comment -- @@ -296,7 +342,7 @@
@Override
public void run() {
// TODO enable copy only when there's a selection
                mLayoutCanvas.onCopy(mClipboard);
}
});

//Synthetic comment -- @@ -304,14 +350,14 @@
@Override
public void run() {
// TODO enable cut only when there's a selection
                mLayoutCanvas.onCut(mClipboard);
}
});

actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), new Action("Paste") {
@Override
public void run() {
                mLayoutCanvas.onPaste(mClipboard);
}
});

//Synthetic comment -- @@ -319,7 +365,7 @@
new Action("Select All") {
@Override
public void run() {
                mLayoutCanvas.onSelectAll();
}
});
}
//Synthetic comment -- @@ -338,11 +384,15 @@

/** Displays the canvas and hides the error label. */
private void hideError() {
        mSashError.setMaximizedControl(mLayoutCanvas);
}

@Override
public void dispose() {
if (mTargetListener != null) {
AdtPlugin.getDefault().removeTargetListener(mTargetListener);
mTargetListener = null;
//Synthetic comment -- @@ -791,8 +841,8 @@

if (mRulesEngine == null) {
mRulesEngine = new RulesEngine(mEditedFile.getProject());
            if (mLayoutCanvas != null) {
                mLayoutCanvas.setRulesEngine(mRulesEngine);
}
}
}
//Synthetic comment -- @@ -1039,7 +1089,7 @@
configuredProjectRes, frameworkResources, mProjectCallback,
mLogger);

                            mLayoutCanvas.setResult(result);

// update the UiElementNode with the layout info.
if (result.getSuccess() == ILayoutResult.SUCCESS) {
//Synthetic comment -- @@ -1186,7 +1236,7 @@
// However there's no recompute, as it could not be needed
// (for instance a new layout)
// If a resource that's not a layout changed this will trigger a recompute anyway.
                mLayoutCanvas.getDisplay().asyncExec(new Runnable() {
public void run() {
mConfigComposite.updateLocales();
}
//Synthetic comment -- @@ -1227,7 +1277,7 @@
}

if (recompute) {
                mLayoutCanvas.getDisplay().asyncExec(new Runnable() {
public void run() {
if (mLayoutEditor.isGraphicalEditorActive()) {
recomputeLayout();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ICanvasTransform.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ICanvasTransform.java
new file mode 100755
//Synthetic comment -- index 0000000..57069f5

//Synthetic comment -- @@ -0,0 +1,60 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 723b974..82c3fe4 100755

//Synthetic comment -- @@ -31,12 +31,16 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.layoutlib.api.ILayoutResult;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.dnd.Clipboard;
//Synthetic comment -- @@ -91,6 +95,16 @@
* Displays the image rendered by the {@link GraphicalEditorPart} and handles
* the interaction with the widgets.
* <p/>
*
* @since GLE2
*
//Synthetic comment -- @@ -101,9 +115,8 @@
* - context menu handling of layout + local props (via IViewRules)
* - outline should include same context menu + delete/copy/paste ops.
* - outline should include drop support (from canvas or from palette)
 * - synchronize with property view
*/
/* package */  class LayoutCanvas extends Canvas {

/** The layout editor that uses this layout canvas. */
private final LayoutEditor mLayoutEditor;
//Synthetic comment -- @@ -194,17 +207,19 @@
/** Horizontal scaling & scrollbar information. */
private ScaleInfo mHScale;

private DragSource mSource;

    /** The current Outline Page, to synchronize the selection both ways. */
private OutlinePage2 mOutlinePage;

    /** Listens to selections on the Outline Page and updates the canvas selection. */
    private OutlineSelectionListener mOutlineSelectionListener;

    /** Barrier set when updating the outline page to match the canvas selection,
     * to prevent it from triggering the outline selection listener. */
    private boolean mInsideUpdateOutlineSelection;

public LayoutCanvas(LayoutEditor layoutEditor,
RulesEngine rulesEngine,
//Synthetic comment -- @@ -281,9 +296,6 @@
Object outline = layoutEditor.getAdapter(IContentOutlinePage.class);
if (outline instanceof OutlinePage2) {
mOutlinePage = (OutlinePage2) outline;
            // Note: we can't set the OutlinePage's SelectionChangeListener now
            // because the TreeViewer backing the Outline Page hasn't been created
            // yet. Instead we will create it "lazily" in updateOulineSelection().
}
}

//Synthetic comment -- @@ -293,9 +305,9 @@
public void dispose() {
super.dispose();

        if (mOutlineSelectionListener != null && mOutlinePage != null) {
            mOutlinePage.removeSelectionChangedListener(mOutlineSelectionListener);
            mOutlineSelectionListener = null;
}

if (mHoverFgColor != null) {
//Synthetic comment -- @@ -396,7 +408,7 @@
it.add(new CanvasSelection(vi, mRulesEngine, mNodeFactory));
}
}
            updateOulineSelection();

// remove the current alternate selection views
mAltSelection = null;
//Synthetic comment -- @@ -471,7 +483,7 @@
redraw();
}

        updateOulineSelection();
}

/**
//Synthetic comment -- @@ -499,52 +511,123 @@
return new Point(x, y);
}

    //---

    public interface ScaleTransform {
        /**
         * Computes the transformation from a X/Y canvas image coordinate
         * to client pixel coordinate.
         * <p/>
         * This takes into account the {@link ScaleInfo#IMAGE_MARGIN},
         * the current scaling and the current translation.
         *
         * @param canvasX A canvas image coordinate (X or Y).
         * @return The transformed coordinate in client pixel space.
         */
        public int translate(int canvasX);

        /**
         * Computes the transformation from a canvas image size (width or height) to
         * client pixel coordinates.
         *
         * @param canwasW A canvas image size (W or H).
         * @return The transformed coordinate in client pixel space.
         */
        public int scale(int canwasW);

        /**
         * Computes the transformation from a X/Y client pixel coordinate
         * to canvas image coordinate.
         * <p/>
         * This takes into account the {@link ScaleInfo#IMAGE_MARGIN},
         * the current scaling and the current translation.
         * <p/>
         * This is the inverse of {@link #translate(int)}.
         *
         * @param screenX A client pixel space coordinate (X or Y).
         * @return The transformed coordinate in canvas image space.
         */
        public int inverseTranslate(int screenX);
}

    private class ScaleInfo implements ScaleTransform {
        /**
         * Margin around the rendered image.
         * Should be enough space to display the layout width and height pseudo widgets.
         */
        private static final int IMAGE_MARGIN = 25;

/** Canvas image size (original, before zoom), in pixels */
private int mImgSize;

//Synthetic comment -- @@ -906,7 +989,7 @@

// otherwise add it.
mSelections.add(new CanvasSelection(vi, mRulesEngine, mNodeFactory));
                    updateOulineSelection();
redraw();
}

//Synthetic comment -- @@ -931,7 +1014,7 @@
CanvasViewInfo vi2 = mAltSelection.getCurrent();
if (vi2 != null) {
mSelections.addFirst(new CanvasSelection(vi2, mRulesEngine, mNodeFactory));
                        updateOulineSelection();
}
} else {
// We're trying to cycle through the current alternate selection.
//Synthetic comment -- @@ -943,7 +1026,7 @@
vi2 = mAltSelection.getNext();
if (vi2 != null) {
mSelections.addFirst(new CanvasSelection(vi2, mRulesEngine, mNodeFactory));
                        updateOulineSelection();
}
}
redraw();
//Synthetic comment -- @@ -978,7 +1061,7 @@
if (vi != null) {
mSelections.add(new CanvasSelection(vi, mRulesEngine, mNodeFactory));
}
        updateOulineSelection();
redraw();
}

//Synthetic comment -- @@ -1130,120 +1213,32 @@
}

/**
     * Update the selection in the outline page to match the current one from {@link #mSelections}
*/
    private void updateOulineSelection() {
        boolean old = mInsideUpdateOutlineSelection;
try {
            mInsideUpdateOutlineSelection = true;

            if (mOutlinePage == null) {
                return;
            }

            // Add the OutlineSelectionListener as soon as the outline page tree view exists.
            if (mOutlineSelectionListener == null && mOutlinePage.getControl() != null) {
                mOutlineSelectionListener = new OutlineSelectionListener();
                mOutlinePage.addSelectionChangedListener(mOutlineSelectionListener);
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

            mOutlinePage.selectAndReveal(
                    selectedVis.toArray(new CanvasViewInfo[selectedVis.size()]));
} finally {
            mInsideUpdateOutlineSelection = old;
}
}


//---------------

    private class OutlineSelectionListener implements ISelectionChangedListener {
        public void selectionChanged(SelectionChangedEvent event) {
            if (mInsideUpdateOutlineSelection) {
                return;
            }

            try {
                mInsideUpdateOutlineSelection = true;

                ISelection sel = event.getSelection();

                // The selection coming from the OutlinePage2 must be a list of
                // CanvasViewInfo. See the implementation of OutlinePage2#selectAndReveal()
                // for how it is constructed.
                if (sel instanceof ITreeSelection) {
                    ITreeSelection treeSel = (ITreeSelection) sel;

                    if (treeSel.isEmpty() && mSelections.size() > 0) {
                        // Clear existing selection
                        mSelections.clear();
                        mAltSelection = null;
                        redraw();
                        return;
                    }

                    boolean changed = false;

                    // Create a list of all currently selected view infos
                    Set<CanvasViewInfo> oldSelected = new HashSet<CanvasViewInfo>();
                    for (CanvasSelection cs : mSelections) {
                        oldSelected.add(cs.getViewInfo());
                    }

                    // Go thru new selection and take care of selecting new items
                    // or marking those which are the same as in the current selection
                    for (TreePath path : treeSel.getPaths()) {
                        Object seg = path.getLastSegment();
                        if (seg instanceof CanvasViewInfo) {
                            CanvasViewInfo newVi = (CanvasViewInfo) seg;
                            if (oldSelected.contains(newVi)) {
                                // This view info is already selected. Remove it from the
                                // oldSelected list so that we don't de-select it later.
                                oldSelected.remove(newVi);
                            } else {
                                // This view info is not already selected. Select it now.

                                // reset alternate selection if any
                                mAltSelection = null;
                                // otherwise add it.
                                mSelections.add(
                                        new CanvasSelection(newVi, mRulesEngine, mNodeFactory));
                                changed = true;
                            }
                        }
                    }

                    // De-select old selected items that are not in the new one
                    for (CanvasViewInfo vi : oldSelected) {
                        deselect(vi);
                        changed = true;
                    }

                    if (changed) {
                        redraw();
                    }
                }

            } finally {
                mInsideUpdateOutlineSelection = false;
            }
        }
    }

private class CanvasDragSourceListener implements DragSourceListener {

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvasViewer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvasViewer.java
new file mode 100755
//Synthetic comment -- index 0000000..1978c34

//Synthetic comment -- @@ -0,0 +1,117 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java
//Synthetic comment -- index 96f0e3f..b1086c2 100755

//Synthetic comment -- @@ -27,19 +27,18 @@
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

//Synthetic comment -- @@ -60,18 +59,17 @@
/**
* An outline page for the GLE2 canvas view.
* <p/>
 * The page is created by {@link LayoutEditor}.
 * Selection is synchronized by {@link LayoutCanvas}.
*/
public class OutlinePage2 implements IContentOutlinePage {

    /**
     * The current TreeViewer. This is created in {@link #createControl(Composite)}.
     * It is entirely possible for callbacks to be invoked *before* the tree viewer
     * is created, for example if a non-yet shown canvas is modified and it refreshes
     * the model of a non-yet shown outline.
     */
    private TreeViewer mTreeViewer;

/**
* RootWrapper is a workaround: we can't set the input of the treeview to its root
//Synthetic comment -- @@ -80,21 +78,23 @@
private final RootWrapper mRootWrapper = new RootWrapper();

public OutlinePage2() {
}

public void createControl(Composite parent) {
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
//Synthetic comment -- @@ -122,96 +122,81 @@
return false;
}
});
}

public void dispose() {
        Control c = getControl();
        if (c != null && !c.isDisposed()) {
            mTreeViewer = null;
            c.dispose();
        }
mRootWrapper.setRoot(null);
    }

    public void setModel(CanvasViewInfo rootViewInfo) {
        mRootWrapper.setRoot(rootViewInfo);

        if (mTreeViewer != null) {
            Object[] expanded = mTreeViewer.getExpandedElements();
            mTreeViewer.refresh();
            mTreeViewer.setExpandedElements(expanded);
        }
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
            mTreeViewer.expandToLevel(paths[i], 1);
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


//Synthetic comment -- @@ -361,5 +346,4 @@
// pass
}
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PropertySheetPage2.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PropertySheetPage2.java
new file mode 100755
//Synthetic comment -- index 0000000..1259e65

//Synthetic comment -- @@ -0,0 +1,148 @@







