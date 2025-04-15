/*ADT GLE2: Synchronize selection outline->canvas

Change-Id:I21458fe649b2d62390edba5ea107afe888185d3f*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 40c3ec6..723b974 100755

//Synthetic comment -- @@ -32,6 +32,11 @@
import com.android.layoutlib.api.ILayoutResult;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.dnd.Clipboard;
//Synthetic comment -- @@ -75,10 +80,12 @@
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
* Displays the image rendered by the {@link GraphicalEditorPart} and handles
//Synthetic comment -- @@ -89,13 +96,12 @@
*
* TODO list:
* - gray on error, keep select but disable d'n'd.
 * - handle drag'n'drop (internal, for moving/duplicating).
* - handle context menu (depending on selection).
* - delete, copy/paste linked with menus and in context menu
* - context menu handling of layout + local props (via IViewRules)
 * - selection synchronization with the outline (both ways).
* - outline should include same context menu + delete/copy/paste ops.
* - outline should include drop support (from canvas or from palette)
*/
/* package */  class LayoutCanvas extends Canvas {

//Synthetic comment -- @@ -193,7 +199,17 @@
/** The current Outline Page, to synchronize the selection both ways. */
private OutlinePage2 mOutlinePage;

    public LayoutCanvas(LayoutEditor layoutEditor, RulesEngine rulesEngine, Composite parent, int style) {
super(parent, style | SWT.DOUBLE_BUFFERED | SWT.V_SCROLL | SWT.H_SCROLL);
mLayoutEditor = layoutEditor;
mRulesEngine = rulesEngine;
//Synthetic comment -- @@ -265,13 +281,23 @@
Object outline = layoutEditor.getAdapter(IContentOutlinePage.class);
if (outline instanceof OutlinePage2) {
mOutlinePage = (OutlinePage2) outline;
}
}

@Override
public void dispose() {
super.dispose();

if (mHoverFgColor != null) {
mHoverFgColor.dispose();
mHoverFgColor = null;
//Synthetic comment -- @@ -1107,29 +1133,117 @@
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
//Synthetic comment -- index 033732d..96f0e3f 100755

//Synthetic comment -- @@ -45,7 +45,6 @@

/*
* TODO -- missing features:
 * - synchronize selection tree=>canvas
* - right-click context menu *shared* with the one from canvas (simply delegate action)
* - drag'n'drop initiated from Palette to Outline
* - drag'n'drop from Outline to Outline







