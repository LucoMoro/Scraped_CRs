/*Fix drag and drop of unselected item

If you have no selection and start dragging a view, nothing
happens. This fixes this such that when dragging begins, the item you
are dragging becomes selected. This also works where you have a
selection and you start dragging some other (non-selected) item.

Change-Id:I20fc400504f375b75c1ed17ec4850420a8500edd*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasDropListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasDropListener.java
//Synthetic comment -- index d5bfa81..b3af5f7 100755

//Synthetic comment -- @@ -290,7 +290,7 @@
* one of the callOnXyz methods of GRE to refresh the fields.
*
* @param df The current {@link DropFeedback}.
     * @param event An optional event to determine if the current operation is copy or move.
*/
private void updateDropFeedback(DropFeedback df, DropTargetEvent event) {
if (event != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 880d76e..88064ce 100755

//Synthetic comment -- @@ -1541,6 +1541,17 @@
}
}

            // If you are dragging a non-selected item, select it
            if (mDragSelection.isEmpty()) {
                int x = mHScale.inverseTranslate(e.x);
                int y = mVScale.inverseTranslate(e.y);
                CanvasViewInfo vi = findViewInfoAt(x, y);
                if (vi != null) {
                    selectSingle(vi);
                    mDragSelection.addAll(mSelections);
                }
            }

sanitizeSelection(mDragSelection);

e.doit = !mDragSelection.isEmpty();







