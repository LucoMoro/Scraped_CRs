/*Cleanup and comment the refresh code in GLE2.

Change-Id:I95a922c463992600edfc8c25edfc688dc38f79bd*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 4335754..73711aa 100755

//Synthetic comment -- @@ -184,7 +184,6 @@
private ProjectCallback mProjectCallback;
private ILayoutLog mLogger;

private boolean mNeedsRecompute = false;

private TargetListener mTargetListener;
//Synthetic comment -- @@ -874,7 +873,7 @@
* Responds to a page change that made the Graphical editor page the activated page.
*/
public void activated() {
        if (mNeedsRecompute) {
recomputeLayout();
}
}
//Synthetic comment -- @@ -965,41 +964,28 @@
* Callback for XML model changed. Only update/recompute the layout if the editor is visible
*/
public void onXmlModelChanged() {
        // To optimize the rendering when the user is editing in the XML pane, we don't
        // refresh the editor if it's not the active part.
        //
        // This behavior is acceptable when the editor is the single "full screen" part
        // (as in this case active means visible.)
        // Unfortunately this breaks in 2 cases:
        // - when performing a drag'n'drop from one editor to another, the target is not
        //   properly refreshed before it becomes active.
        // - when duplicating the editor window and placing both editors side by side (xml in one
        //   and canvas in the other one), the canvas may not be refreshed when the XML is edited.
        //
        // TODO find a way to really query whether the pane is visible, not just active.

if (mLayoutEditor.isGraphicalEditorActive()) {
recomputeLayout();
} else {
            // Remember we want to recompute as soon as the editor becomes active.
            mNeedsRecompute = true;
}
}

public void recomputeLayout() {
try {
if (!ensureFileValid()) {
return;







