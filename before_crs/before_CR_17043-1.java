/*GLE2: fix executing actions on multi-selection

Change-Id:I057dca413d81c9ae3e00bc074eeb31b032f7e85e*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java
//Synthetic comment -- index da7d643..96db650 100755

//Synthetic comment -- @@ -208,7 +208,7 @@
final TreeMap<String, ArrayList<MenuAction>> outActionsMap,
final TreeMap<String, MenuAction.Group> outGroupsMap) {
int maxMenuSelection = 0;
        for (CanvasSelection selection : mCanvas.getCanvasSelection()) {
List<MenuAction> viewActions = null;
if (selection != null) {
CanvasViewInfo vi = selection.getViewInfo();
//Synthetic comment -- @@ -244,7 +244,7 @@

// All the actions for the same id should have be equal
if (!actions.isEmpty()) {
                    if (action.equals(actions.get(0))) {
// TODO Log verbose error for invalid type mismatch.
continue;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 4de76f9..9cb6385 100755

//Synthetic comment -- @@ -442,11 +442,11 @@
/**
* Returns the native {@link CanvasSelection} list.
*
     * @return An immutable list of {@link CanvasSelection}
* @see #getSelection() {@link #getSelection()} to retrieve a {@link TreeViewer}
*                      compatible {@link ISelection}.
*/
    /* package */ List<CanvasSelection> getCanvasSelection() {
if (mUnmodifiableSelection == null) {
mUnmodifiableSelection = Collections.unmodifiableList(mSelections);
}







