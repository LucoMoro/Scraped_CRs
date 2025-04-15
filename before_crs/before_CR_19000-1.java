/*Cleanup and comment the refresh code in GLE2.

Change-Id:I95a922c463992600edfc8c25edfc688dc38f79bd*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 4335754..73711aa 100755

//Synthetic comment -- @@ -184,7 +184,6 @@
private ProjectCallback mProjectCallback;
private ILayoutLog mLogger;

    private boolean mNeedsXmlReload = false;
private boolean mNeedsRecompute = false;

private TargetListener mTargetListener;
//Synthetic comment -- @@ -874,7 +873,7 @@
* Responds to a page change that made the Graphical editor page the activated page.
*/
public void activated() {
        if (mNeedsRecompute || mNeedsXmlReload) {
recomputeLayout();
}
}
//Synthetic comment -- @@ -965,41 +964,28 @@
* Callback for XML model changed. Only update/recompute the layout if the editor is visible
*/
public void onXmlModelChanged() {
if (mLayoutEditor.isGraphicalEditorActive()) {
            doXmlReload(true /* force */);
recomputeLayout();
} else {
            mNeedsXmlReload = true;
        }
    }

    /**
     * Actually performs the XML reload
     * @see #onXmlModelChanged()
     */
    private void doXmlReload(boolean force) {
        if (force || mNeedsXmlReload) {

            // TODO : update the mLayoutCanvas, preserving the current selection if possible.

//            GraphicalViewer viewer = getGraphicalViewer();
//
//            // try to preserve the selection before changing the content
//            SelectionManager selMan = viewer.getSelectionManager();
//            ISelection selection = selMan.getSelection();
//
//            try {
//                viewer.setContents(getModel());
//            } finally {
//                selMan.setSelection(selection);
//            }

            mNeedsXmlReload = false;
}
}

public void recomputeLayout() {
        doXmlReload(false /* force */);
try {
if (!ensureFileValid()) {
return;







