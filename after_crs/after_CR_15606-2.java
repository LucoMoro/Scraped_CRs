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
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.PropertySheetPage2;
import com.android.ide.eclipse.adt.internal.editors.ui.tree.UiActions;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
//Synthetic comment -- @@ -78,7 +79,7 @@
/** Implementation of the {@link IContentOutlinePage} for this editor */
private IContentOutlinePage mOutline;
/** Custom implementation of {@link IPropertySheetPage} for this editor */
    private IPropertySheetPage mPropertyPage;

private UiEditorActions mUiEditorActions;

//Synthetic comment -- @@ -303,8 +304,8 @@
}
}

    /**
     * Returns the custom IContentOutlinePage or IPropertySheetPage when asked for it.
*/
@SuppressWarnings("unchecked")
@Override
//Synthetic comment -- @@ -331,8 +332,11 @@
}

if (IPropertySheetPage.class == adapter && mGraphicalEditor != null) {
            if (mPropertyPage == null && mGraphicalEditor instanceof GraphicalLayoutEditor) {
mPropertyPage = new UiPropertySheetPage();

            } else if (mPropertyPage == null && mGraphicalEditor instanceof GraphicalEditorPart) {
                mPropertyPage = new PropertySheetPage2();
}

return mPropertyPage;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java
//Synthetic comment -- index 767a5c6..2781d1a 100755

//Synthetic comment -- @@ -21,21 +21,29 @@
import com.android.layoutlib.api.ILayoutResult.ILayoutViewInfo;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.IPropertySource;

import java.util.ArrayList;

/**
* Maps a {@link ILayoutViewInfo} in a structure more adapted to our needs.
* The only large difference is that we keep both the original bounds of the view info
 * and we pre-compute the selection bounds which are absolute to the rendered image
 * (whereas the original bounds are relative to the parent view.)
* <p/>
 * Each view also knows its parent and children.
* <p/>
* We can't alter {@link ILayoutViewInfo} as it is part of the LayoutBridge and needs to
* have a fixed API.
 * <p/>
 * The view info also implements {@link IPropertySource}, which enables a linked
 * {@link IPropertySheetPage} to display the attributes of the selected element.
 * This class actually delegates handling of {@link IPropertySource} to the underlying
 * {@link UiViewElementNode}, if any.
*/
public class CanvasViewInfo implements IPropertySource {

/**
* Minimal size of the selection, in case an empty view or layout is selected.
//Synthetic comment -- @@ -180,9 +188,58 @@
* Returns the name of the {@link CanvasViewInfo}.
* Could be null, although unlikely.
* Experience shows this is the full qualified Java name of the View.
     *
* @see ILayoutViewInfo#getName()
*/
public String getName() {
return mName;
}

    // ---- Implementation of IPropertySource

    public Object getEditableValue() {
        UiViewElementNode uiView = getUiViewKey();
        if (uiView != null) {
            return ((IPropertySource) uiView).getEditableValue();
        }
        return null;
    }

    public IPropertyDescriptor[] getPropertyDescriptors() {
        UiViewElementNode uiView = getUiViewKey();
        if (uiView != null) {
            return ((IPropertySource) uiView).getPropertyDescriptors();
        }
        return null;
    }

    public Object getPropertyValue(Object id) {
        UiViewElementNode uiView = getUiViewKey();
        if (uiView != null) {
            return ((IPropertySource) uiView).getPropertyValue(id);
        }
        return null;
    }

    public boolean isPropertySet(Object id) {
        UiViewElementNode uiView = getUiViewKey();
        if (uiView != null) {
            return ((IPropertySource) uiView).isPropertySet(id);
        }
        return false;
    }

    public void resetPropertyValue(Object id) {
        UiViewElementNode uiView = getUiViewKey();
        if (uiView != null) {
            ((IPropertySource) uiView).resetPropertyValue(id);
        }
    }

    public void setPropertyValue(Object id, Object value) {
        UiViewElementNode uiView = getUiViewKey();
        if (uiView != null) {
            ((IPropertySource) uiView).setPropertyValue(id, value);
        }
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GCWrapper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GCWrapper.java
//Synthetic comment -- index fef9e1b..39e24d1 100755

//Synthetic comment -- @@ -21,7 +21,6 @@
import com.android.ide.eclipse.adt.editors.layout.gscripts.IViewRule;
import com.android.ide.eclipse.adt.editors.layout.gscripts.Point;
import com.android.ide.eclipse.adt.editors.layout.gscripts.Rect;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
//Synthetic comment -- @@ -68,11 +67,11 @@
private int mFontHeight = 0;

/** The scaling of the canvas in X. */
    private final ICanvasTransform mHScale;
/** The scaling of the canvas in Y. */
    private final ICanvasTransform mVScale;

    public GCWrapper(ICanvasTransform hScale, ICanvasTransform vScale) {
mHScale = hScale;
mVScale = vScale;
mGc = null;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index f75fa94..d2d82f6 100755

//Synthetic comment -- @@ -65,6 +65,8 @@
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
//Synthetic comment -- @@ -76,6 +78,9 @@
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.ide.IDE;
//Synthetic comment -- @@ -92,29 +97,50 @@

/**
* Graphical layout editor part, version 2.
 * <p/>
 * The main component of the editor part is the {@link LayoutCanvasViewer}, which
 * actually delegates its work to the {@link LayoutCanvas} control.
 * <p/>
 * The {@link LayoutCanvasViewer} is set as the site's {@link ISelectionProvider}:
 * when the selection changes in the canvas, it is thus broadcasted to anyone listening
 * on the site's selection service.
 * <p/>
 * This part is also an {@link ISelectionListener}. It listens to the site's selection
 * service and thus receives selection changes from itself as well as the associated
 * outline and property sheet (these are registered by {@link LayoutEditor#getAdapter(Class)}).
*
* @since GLE2
*
* TODO List:
* - display error icon
* - finish palette (see palette's todo list)
 * - finish canvas (see canvas' todo list)
* - completly rethink the property panel
*/
public class GraphicalEditorPart extends EditorPart
    implements IGraphicalLayoutEditor, ISelectionListener, INullSelectionListener {

/*
* Useful notes:
* To understand Drag'n'drop:
*   http://www.eclipse.org/articles/Article-Workbench-DND/drag_drop.html
     *
     * To understand the site's selection listener, selection provider, and the
     * confusion of different-yet-similarly-named interfaces, consult this:
     *   http://www.eclipse.org/articles/Article-WorkbenchSelections/article.html
     *
     * To summarize the selection mechanism:
     * - The workbench site selection service can be seen as "centralized"
     *   service that registers selection providers and selection listeners.
     * - The editor part and the outline are selection providers.
     * - The editor part, the outline and the property sheet are listeners
     *   which all listen to each others indirectly.
*/

/** Reference to the layout editor */
private final LayoutEditor mLayoutEditor;

    /** Reference to the file being edited. Can also be used to access the {@link IProject}. */
private IFile mEditedFile;

/** The current clipboard. Must be disposed later. */
//Synthetic comment -- @@ -125,17 +151,21 @@

/** The sash that splits the palette from the canvas. */
private SashForm mSashPalette;

    /** The sash that splits the palette from the error view.
     * The error view is shown only when needed. */
private SashForm mSashError;

/** The palette displayed on the left of the sash. */
private PaletteComposite mPalette;

/** The layout canvas displayed to the right of the sash. */
    private LayoutCanvasViewer mCanvasViewer;

/** The Groovy Rules Engine associated with this editor. It is project-specific. */
private RulesEngine mRulesEngine;

    /** Styled text displaying the most recent error in the error view. */
private StyledText mErrorLabel;

private Map<String, Map<String, IResourceValue>> mConfiguredFrameworkRes;
//Synthetic comment -- @@ -152,7 +182,7 @@

private ReloadListener mReloadListener;

    private boolean mUseExplodeMode;


public GraphicalEditorPart(LayoutEditor layoutEditor) {
//Synthetic comment -- @@ -238,7 +268,7 @@
) {
@Override
public void onSelected(boolean newState) {
                        mCanvasViewer.getCanvas().setShowOutline(newState);
}
}
};
//Synthetic comment -- @@ -254,7 +284,7 @@
mSashError = new SashForm(mSashPalette, SWT.VERTICAL | SWT.BORDER);
mSashError.setLayoutData(new GridData(GridData.FILL_BOTH));

        mCanvasViewer = new LayoutCanvasViewer(mLayoutEditor, mRulesEngine, mSashError, SWT.NONE);

mErrorLabel = new StyledText(mSashError, SWT.READ_ONLY);
mErrorLabel.setEditable(false);
//Synthetic comment -- @@ -263,12 +293,28 @@

mSashPalette.setWeights(new int[] { 20, 80 });
mSashError.setWeights(new int[] { 80, 20 });
        mSashError.setMaximizedControl(mCanvasViewer.getControl());

setupEditActions();

// Initialize the state
reloadPalette();

        getSite().setSelectionProvider(mCanvasViewer);
        getSite().getPage().addSelectionListener(this);
    }

    /**
     * Listens to workbench selections that does NOT come from {@link LayoutEditor}
     * (those are generated by ourselves).
     * <p/>
     * Selection can be null, as indicated by this class implementing
     * {@link INullSelectionListener}.
     */
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if (!(part instanceof LayoutEditor)) {
            mCanvasViewer.setSelection(selection);
        }
}

/**
//Synthetic comment -- @@ -276,7 +322,7 @@
* @param direction +1 for zoom in, -1 for zoom out
*/
private void rescale(int direction) {
        double s = mCanvasViewer.getCanvas().getScale();

if (direction > 0) {
s = s * 2;
//Synthetic comment -- @@ -284,7 +330,7 @@
s = s / 2;
}

        mCanvasViewer.getCanvas().setScale(s);

}

//Synthetic comment -- @@ -296,7 +342,7 @@
@Override
public void run() {
// TODO enable copy only when there's a selection
                mCanvasViewer.getCanvas().onCopy(mClipboard);
}
});

//Synthetic comment -- @@ -304,14 +350,14 @@
@Override
public void run() {
// TODO enable cut only when there's a selection
                mCanvasViewer.getCanvas().onCut(mClipboard);
}
});

actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), new Action("Paste") {
@Override
public void run() {
                mCanvasViewer.getCanvas().onPaste(mClipboard);
}
});

//Synthetic comment -- @@ -319,7 +365,7 @@
new Action("Select All") {
@Override
public void run() {
                mCanvasViewer.getCanvas().onSelectAll();
}
});
}
//Synthetic comment -- @@ -338,11 +384,15 @@

/** Displays the canvas and hides the error label. */
private void hideError() {
        mSashError.setMaximizedControl(mCanvasViewer.getControl());
}

@Override
public void dispose() {

        getSite().getPage().removeSelectionListener(this);
        getSite().setSelectionProvider(null);

if (mTargetListener != null) {
AdtPlugin.getDefault().removeTargetListener(mTargetListener);
mTargetListener = null;
//Synthetic comment -- @@ -791,8 +841,8 @@

if (mRulesEngine == null) {
mRulesEngine = new RulesEngine(mEditedFile.getProject());
            if (mCanvasViewer != null) {
                mCanvasViewer.getCanvas().setRulesEngine(mRulesEngine);
}
}
}
//Synthetic comment -- @@ -1039,7 +1089,7 @@
configuredProjectRes, frameworkResources, mProjectCallback,
mLogger);

                            mCanvasViewer.getCanvas().setResult(result);

// update the UiElementNode with the layout info.
if (result.getSuccess() == ILayoutResult.SUCCESS) {
//Synthetic comment -- @@ -1186,7 +1236,7 @@
// However there's no recompute, as it could not be needed
// (for instance a new layout)
// If a resource that's not a layout changed this will trigger a recompute anyway.
                mCanvasViewer.getControl().getDisplay().asyncExec(new Runnable() {
public void run() {
mConfigComposite.updateLocales();
}
//Synthetic comment -- @@ -1227,7 +1277,7 @@
}

if (recompute) {
                mCanvasViewer.getControl().getDisplay().asyncExec(new Runnable() {
public void run() {
if (mLayoutEditor.isGraphicalEditorActive()) {
recomputeLayout();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ICanvasTransform.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ICanvasTransform.java
new file mode 100755
//Synthetic comment -- index 0000000..57069f5

//Synthetic comment -- @@ -0,0 +1,60 @@
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

public interface ICanvasTransform {
    /**
     * Margin around the rendered image.
     * Should be enough space to display the layout width and height pseudo widgets.
     */
    public static final int IMAGE_MARGIN = 25;

    /**
     * Computes the transformation from a X/Y canvas image coordinate
     * to client pixel coordinate.
     * <p/>
     * This takes into account the {@link #IMAGE_MARGIN},
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
     * This takes into account the {@link #IMAGE_MARGIN},
     * the current scaling and the current translation.
     * <p/>
     * This is the inverse of {@link #translate(int)}.
     *
     * @param screenX A client pixel space coordinate (X or Y).
     * @return The transformed coordinate in canvas image space.
     */
    public int inverseTranslate(int screenX);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 723b974..82c3fe4 100755

//Synthetic comment -- @@ -31,12 +31,16 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.layoutlib.api.ILayoutResult;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.dnd.Clipboard;
//Synthetic comment -- @@ -91,6 +95,16 @@
* Displays the image rendered by the {@link GraphicalEditorPart} and handles
* the interaction with the widgets.
* <p/>
 * {@link LayoutCanvas} implements the "Canvas" control. The editor part
 * actually uses the {@link LayoutCanvasViewer}, which is a JFace viewer wrapper
 * around this control.
 * <p/>
 * This class implements {@link ISelectionProvider} so that it can delegate
 * the selection provider from the {@link LayoutCanvasViewer}.
 * <p/>
 * Note that {@link LayoutCanvasViewer} sets a selection change listener on this
 * control so that it can invoke its own fireSelectionChanged when the control's
 * selection changes.
*
* @since GLE2
*
//Synthetic comment -- @@ -101,9 +115,8 @@
* - context menu handling of layout + local props (via IViewRules)
* - outline should include same context menu + delete/copy/paste ops.
* - outline should include drop support (from canvas or from palette)
*/
class LayoutCanvas extends Canvas implements ISelectionProvider {

/** The layout editor that uses this layout canvas. */
private final LayoutEditor mLayoutEditor;
//Synthetic comment -- @@ -194,17 +207,19 @@
/** Horizontal scaling & scrollbar information. */
private ScaleInfo mHScale;

    /** Drag source associated with this canvas. */
private DragSource mSource;

    /** List of clients listening to selection changes. */
    private final ListenerList mSelectionListeners = new ListenerList();

    /** The current Outline Page, to set its model. */
private OutlinePage2 mOutlinePage;

    /** Barrier set when updating the selection to prevent from recursively
     * invoking ourselves. */
    private boolean mInsideUpdateSelection;


public LayoutCanvas(LayoutEditor layoutEditor,
RulesEngine rulesEngine,
//Synthetic comment -- @@ -281,9 +296,6 @@
Object outline = layoutEditor.getAdapter(IContentOutlinePage.class);
if (outline instanceof OutlinePage2) {
mOutlinePage = (OutlinePage2) outline;
}
}

//Synthetic comment -- @@ -293,9 +305,9 @@
public void dispose() {
super.dispose();

        if (mOutlinePage != null) {
            mOutlinePage.setModel(null);
            mOutlinePage = null;
}

if (mHoverFgColor != null) {
//Synthetic comment -- @@ -396,7 +408,7 @@
it.add(new CanvasSelection(vi, mRulesEngine, mNodeFactory));
}
}
            fireSelectionChanged();

// remove the current alternate selection views
mAltSelection = null;
//Synthetic comment -- @@ -471,7 +483,7 @@
redraw();
}

        fireSelectionChanged();
}

/**
//Synthetic comment -- @@ -499,52 +511,123 @@
return new Point(x, y);
}


    //----
    // Implementation of ISelectionProvider

    /**
     * Returns a {@link TreeSelection} compatible with a TreeViewer
     * where each {@link TreePath} item is actually a {@link CanvasViewInfo}.
     */
    public ISelection getSelection() {
        if (mSelections.size() == 0) {
            return TreeSelection.EMPTY;
        }

        ArrayList<TreePath> paths = new ArrayList<TreePath>();

        for (CanvasSelection cs : mSelections) {
            CanvasViewInfo vi = cs.getViewInfo();
            if (vi != null) {
                ArrayList<Object> segments = new ArrayList<Object>();
                while (vi != null) {
                    segments.add(0, vi);
                    vi = vi.getParent();
                }
                paths.add(new TreePath(segments.toArray()));
            }
        }

        return new TreeSelection(paths.toArray(new TreePath[paths.size()]));
}

    /**
     * Sets the selection. It must be an {@link ITreeSelection} where each segment
     * of the tree path is a {@link CanvasViewInfo}. A null selection is considered
     * as an empty selection.
     */
    public void setSelection(ISelection selection) {
        if (mInsideUpdateSelection) {
            return;
        }

        try {
            mInsideUpdateSelection = true;

            if (selection == null) {
                selection = TreeSelection.EMPTY;
            }

            if (selection instanceof ITreeSelection) {
                ITreeSelection treeSel = (ITreeSelection) selection;

                if (treeSel.isEmpty()) {
                    // Clear existing selection, if any
                    if (mSelections.size() > 0) {
                        mSelections.clear();
                        mAltSelection = null;
                        redraw();
                    }
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
            mInsideUpdateSelection = false;
        }
    }

    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        mSelectionListeners.add(listener);
    }

    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
        mSelectionListeners.remove(listener);
    }

    //---

    private class ScaleInfo implements ICanvasTransform {
/** Canvas image size (original, before zoom), in pixels */
private int mImgSize;

//Synthetic comment -- @@ -906,7 +989,7 @@

// otherwise add it.
mSelections.add(new CanvasSelection(vi, mRulesEngine, mNodeFactory));
                    fireSelectionChanged();
redraw();
}

//Synthetic comment -- @@ -931,7 +1014,7 @@
CanvasViewInfo vi2 = mAltSelection.getCurrent();
if (vi2 != null) {
mSelections.addFirst(new CanvasSelection(vi2, mRulesEngine, mNodeFactory));
                        fireSelectionChanged();
}
} else {
// We're trying to cycle through the current alternate selection.
//Synthetic comment -- @@ -943,7 +1026,7 @@
vi2 = mAltSelection.getNext();
if (vi2 != null) {
mSelections.addFirst(new CanvasSelection(vi2, mRulesEngine, mNodeFactory));
                        fireSelectionChanged();
}
}
redraw();
//Synthetic comment -- @@ -978,7 +1061,7 @@
if (vi != null) {
mSelections.add(new CanvasSelection(vi, mRulesEngine, mNodeFactory));
}
        fireSelectionChanged();
redraw();
}

//Synthetic comment -- @@ -1130,120 +1213,32 @@
}

/**
     * Notifies listeners that the selection has changed.
*/
    private void fireSelectionChanged() {
        if (mInsideUpdateSelection) {
            return;
        }
try {
            mInsideUpdateSelection = true;

            final SelectionChangedEvent event = new SelectionChangedEvent(this, getSelection());

            SafeRunnable.run(new SafeRunnable() {
                public void run() {
                    for (Object listener : mSelectionListeners.getListeners()) {
                        ((ISelectionChangedListener)listener).selectionChanged(event);
                    }
}
            });
} finally {
            mInsideUpdateSelection = false;
}
}


//---------------

private class CanvasDragSourceListener implements DragSourceListener {

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvasViewer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvasViewer.java
new file mode 100755
//Synthetic comment -- index 0000000..1978c34

//Synthetic comment -- @@ -0,0 +1,117 @@
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

import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditor;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;


/**
 * JFace {@link Viewer} wrapper around {@link LayoutCanvas}.
 * <p/>
 * The viewer is owned by {@link GraphicalEditorPart}.
 * <p/>
 * The viewer is an {@link ISelectionProvider} instance and is set as the
 * site's main {@link ISelectionProvider} by the editor part. Consequently
 * canvas' selection changes are broadcasted to anyone listening, which includes
 * the part itself as well as the associated outline and property sheet pages.
 */
class LayoutCanvasViewer extends Viewer {

    private LayoutCanvas mCanvas;
    private final LayoutEditor mLayoutEditor;

    public LayoutCanvasViewer(LayoutEditor layoutEditor,
            RulesEngine rulesEngine,
            Composite parent,
            int style) {
        mLayoutEditor = layoutEditor;
        mCanvas = new LayoutCanvas(layoutEditor, rulesEngine, parent, style);

        mCanvas.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                fireSelectionChanged(event);
            }
        });
    }

    @Override
    public Control getControl() {
        return mCanvas;
    }

    /**
     * Returns the underlying {@link LayoutCanvas}.
     * This is the same control as returned by {@link #getControl()} but clients
     * have it already casted in the right type.
     * <p/>
     * This can never be null.
     */
    public LayoutCanvas getCanvas() {
        return mCanvas;
    }

    /**
     * Returns the current layout editor's input.
     */
    @Override
    public Object getInput() {
        return mLayoutEditor.getEditorInput();
    }

    /**
     * Unused. We don't support switching the input.
     */
    @Override
    public void setInput(Object input) {
    }

    /**
     * Returns a new {@link TreeSelection} where each {@link TreePath} item
     * is a {@link CanvasViewInfo}.
     */
    @Override
    public ISelection getSelection() {
        return mCanvas.getSelection();
    }

    /**
     * Sets a new selection. <code>reveal</code> is ignored right now.
     * <p/>
     * The selection can be null, which is interpreted as an empty selection.
     */
    @Override
    public void setSelection(ISelection selection, boolean reveal) {
        mCanvas.setSelection(selection);
    }

    /** Unused. Refreshing is done solely by the owning {@link LayoutEditor}. */
    @Override
    public void refresh() {
        // ignore
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java
//Synthetic comment -- index 96f0e3f..b1086c2 100755

//Synthetic comment -- @@ -27,19 +27,18 @@
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import java.util.ArrayList;

//Synthetic comment -- @@ -60,18 +59,17 @@
/**
* An outline page for the GLE2 canvas view.
* <p/>
 * The page is created by {@link LayoutEditor#getAdapter(Class)}.
 * It sets itself as a listener on the site's selection service in order to be
 * notified of the canvas' selection changes.
 * The underlying page is also a selection provider (via IContentOutlinePage)
 * and as such it will broadcast selection changes to the site's selection service
 * (on which both the layout editor part and the property sheet page listen.)
 *
 * @since GLE2
*/
public class OutlinePage2 extends ContentOutlinePage
    implements ISelectionListener, INullSelectionListener {

/**
* RootWrapper is a workaround: we can't set the input of the treeview to its root
//Synthetic comment -- @@ -80,21 +78,23 @@
private final RootWrapper mRootWrapper = new RootWrapper();

public OutlinePage2() {
        super();
}

    @Override
public void createControl(Composite parent) {
        super.createControl(parent);

        TreeViewer tv = getTreeViewer();
        tv.setAutoExpandLevel(2);
        tv.setContentProvider(new ContentProvider());
        tv.setLabelProvider(new LabelProvider());
        tv.setInput(mRootWrapper);

// The tree viewer will hold CanvasViewInfo instances, however these
// change each time the canvas is reloaded. OTOH liblayout gives us
// constant UiView keys which we can use to perform tree item comparisons.
        tv.setComparer(new IElementComparer() {
public int hashCode(Object element) {
if (element instanceof CanvasViewInfo) {
UiViewElementNode key = ((CanvasViewInfo) element).getUiViewKey();
//Synthetic comment -- @@ -122,96 +122,81 @@
return false;
}
});

        // Listen to selection changes from the layout editor
        getSite().getPage().addSelectionListener(this);
}

    @Override
public void dispose() {
mRootWrapper.setRoot(null);
        getSite().getPage().removeSelectionListener(this);
        super.dispose();
}

/**
     * Invoked by {@link LayoutCanvas} to set the model (aka the root view info).
*
     * @param rootViewInfo The root of the view info hierarchy. Can be null.
*/
    public void setModel(CanvasViewInfo rootViewInfo) {
        mRootWrapper.setRoot(rootViewInfo);

        TreeViewer tv = getTreeViewer();
        if (tv != null) {
            Object[] expanded = tv.getExpandedElements();
            tv.refresh();
            tv.setExpandedElements(expanded);
}
}

    /**
     * Returns the current tree viewer selection. Shouldn't be null,
     * although it can be {@link TreeSelection#EMPTY}.
     */
    @Override
    public ISelection getSelection() {
        return super.getSelection();
    }

    /**
     * Sets the outline selection.
     *
     * @param selection Only {@link ITreeSelection} will be used, otherwise the
     *   selection will be cleared (including a null selection).
     */
    @Override
public void setSelection(ISelection selection) {
        // TreeViewer should be able to deal with a null selection, but let's make it safe
        if (selection == null) {
            selection = TreeSelection.EMPTY;
        }

        super.setSelection(selection);

        TreeViewer tv = getTreeViewer();
        if (tv == null || !(selection instanceof ITreeSelection) || selection.isEmpty()) {
            return;
        }

        // auto-reveal the selection
        ITreeSelection treeSel = (ITreeSelection) selection;
        for (TreePath p : treeSel.getPaths()) {
            tv.expandToLevel(p, 1);
}
}

    /**
     * Listens to a workbench selection.
     * Only listen on selection coming from {@link LayoutEditor}, which avoid
     * picking up our own selections.
     */
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if (part instanceof LayoutEditor) {
            setSelection(selection);
}
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.views.properties.PropertySheetEntry;
import org.eclipse.ui.views.properties.PropertySheetPage;

/**
 * A customized property sheet page for the graphical layout editor v2.
 * <p/>
 * Currently it just provides a custom tooltip to display attributes javadocs.
 * <p/>
 * The property sheet is linked to the current site's selection service.
 * <p/>
 * Note: this is an exact copy of GLE1's UiPropertySheetPage implementation.
 * The idea is that eventually GLE1 will go away and we'll upgrade this to be
 * a more robust property editor (it currently lacks on so many levels, it's not
 * even worth listing the flaws.)
 *
 * @since GLE2
 */
public class PropertySheetPage2 extends PropertySheetPage {


    public PropertySheetPage2() {
        super();
    }

    @Override
    public void createControl(Composite parent) {
        super.createControl(parent);

        setupTooltip();
    }

    /**
     * Sets up a custom tooltip when hovering over tree items.
     * <p/>
     * The tooltip will display the element's javadoc, if any, or the item's getText otherwise.
     */
    private void setupTooltip() {
        final Tree tree = (Tree) getControl();

        /*
         * Reference:
         * http://dev.eclipse.org/viewcvs/index.cgi/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets/Snippet125.java?view=markup
         */

        final Listener listener = new Listener() {
            Shell tip = null;
            Label label  = null;

            public void handleEvent(Event event) {
                switch(event.type) {
                case SWT.Dispose:
                case SWT.KeyDown:
                case SWT.MouseExit:
                case SWT.MouseDown:
                case SWT.MouseMove:
                    if (tip != null) {
                        tip.dispose();
                        tip = null;
                        label = null;
                    }
                    break;
                case SWT.MouseHover:
                    if (tip != null) {
                        tip.dispose();
                        tip = null;
                        label = null;
                    }

                    String tooltip = null;

                    TreeItem item = tree.getItem(new Point(event.x, event.y));
                    if (item != null) {
                        Object data = item.getData();
                        if (data instanceof PropertySheetEntry) {
                            tooltip = ((PropertySheetEntry) data).getDescription();
                        }

                        if (tooltip == null) {
                            tooltip = item.getText();
                        } else {
                            tooltip = item.getText() + ":\r" + tooltip;
                        }

                        if (tooltip != null) {
                            Shell shell = tree.getShell();
                            Display display = tree.getDisplay();

                            tip = new Shell(shell, SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
                            tip.setBackground(display .getSystemColor(SWT.COLOR_INFO_BACKGROUND));
                            FillLayout layout = new FillLayout();
                            layout.marginWidth = 2;
                            tip.setLayout(layout);
                            label = new Label(tip, SWT.NONE);
                            label.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
                            label.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
                            label.setData("_TABLEITEM", item);
                            label.setText(tooltip);
                            label.addListener(SWT.MouseExit, this);
                            label.addListener(SWT.MouseDown, this);
                            Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
                            Rectangle rect = item.getBounds(0);
                            Point pt = tree.toDisplay(rect.x, rect.y);
                            tip.setBounds(pt.x, pt.y, size.x, size.y);
                            tip.setVisible(true);
                        }
                    }
                }
            }
        };

        tree.addListener(SWT.Dispose, listener);
        tree.addListener(SWT.KeyDown, listener);
        tree.addListener(SWT.MouseMove, listener);
        tree.addListener(SWT.MouseHover, listener);

    }

}







