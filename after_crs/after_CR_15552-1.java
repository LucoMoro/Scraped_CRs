/*ADT GLE2: Synchronize selection outline->canvas

Change-Id:I21458fe649b2d62390edba5ea107afe888185d3f*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 40c3ec6..723b974 100755

//Synthetic comment -- @@ -32,6 +32,11 @@
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
//Synthetic comment -- @@ -75,10 +80,12 @@
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
* Displays the image rendered by the {@link GraphicalEditorPart} and handles
//Synthetic comment -- @@ -89,13 +96,12 @@
*
* TODO list:
* - gray on error, keep select but disable d'n'd.
* - handle context menu (depending on selection).
* - delete, copy/paste linked with menus and in context menu
* - context menu handling of layout + local props (via IViewRules)
* - outline should include same context menu + delete/copy/paste ops.
* - outline should include drop support (from canvas or from palette)
 * - synchronize with property view
*/
/* package */  class LayoutCanvas extends Canvas {

//Synthetic comment -- @@ -193,7 +199,17 @@
/** The current Outline Page, to synchronize the selection both ways. */
private OutlinePage2 mOutlinePage;

    /** Listens to selections on the Outline Page and updates the canvas selection. */
    private OutlineSelectionListener mOutlineSelectionListener;

    /** Barrier set when updating the outline page to match the canvas selection,
     * to prevent it from triggering the outline selection listener. */
    private boolean mInsideUpdateOutlineSelection;

    public LayoutCanvas(LayoutEditor layoutEditor,
            RulesEngine rulesEngine,
            Composite parent,
            int style) {
super(parent, style | SWT.DOUBLE_BUFFERED | SWT.V_SCROLL | SWT.H_SCROLL);
mLayoutEditor = layoutEditor;
mRulesEngine = rulesEngine;
//Synthetic comment -- @@ -265,13 +281,23 @@
Object outline = layoutEditor.getAdapter(IContentOutlinePage.class);
if (outline instanceof OutlinePage2) {
mOutlinePage = (OutlinePage2) outline;
            // Note: we can't set the OutlinePage's SelectionChangeListener now
            // because the TreeViewer backing the Outline Page hasn't been created
            // yet. Instead we will create it "lazily" in updateOulineSelection().
}
}



@Override
public void dispose() {
super.dispose();

        if (mOutlineSelectionListener != null && mOutlinePage != null) {
            mOutlinePage.removeSelectionChangedListener(mOutlineSelectionListener);
            mOutlineSelectionListener = null;
        }

if (mHoverFgColor != null) {
mHoverFgColor.dispose();
mHoverFgColor = null;
//Synthetic comment -- @@ -1107,29 +1133,117 @@
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








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage2.java
//Synthetic comment -- index 033732d..96f0e3f 100755

//Synthetic comment -- @@ -45,7 +45,6 @@

/*
* TODO -- missing features:
* - right-click context menu *shared* with the one from canvas (simply delegate action)
* - drag'n'drop initiated from Palette to Outline
* - drag'n'drop from Outline to Outline







