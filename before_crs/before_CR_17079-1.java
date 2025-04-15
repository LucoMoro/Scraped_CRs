/*ADT GLE2: adjust selection on right-click or drag.

Change-Id:I553e2d659f95d727e87df5d174d737a220a1685d*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index dbdd7aa..c143c35 100755

//Synthetic comment -- @@ -1060,19 +1060,35 @@
*/
private void onMouseUp(MouseEvent e) {

        // Only perform selection when mouse button 1 is used.
        // This prevents right-click from also changing the selection, since it's
        // used to display a context menu that depends on the current selection.
        if (e.button != 1) {
            return;
        }

boolean isShift = (e.stateMask & SWT.SHIFT) != 0;
boolean isAlt   = (e.stateMask & SWT.ALT)   != 0;

int x = mHScale.inverseTranslate(e.x);
int y = mVScale.inverseTranslate(e.y);

CanvasViewInfo vi = findViewInfoAt(x, y);

if (isShift && !isAlt) {
//Synthetic comment -- @@ -1413,10 +1429,10 @@

/**
* The current selection being dragged.
         * This may be a subset of the canvas selection.
* Can be empty but never null.
*/
        final ArrayList<CanvasSelection> mDragSelection = new ArrayList<CanvasSelection>();
private SimpleElement[] mDragElements;

/**
//Synthetic comment -- @@ -1431,8 +1447,8 @@
// We need a selection (simple or multiple) to do any transfer.
// If there's a selection *and* the cursor is over this selection, use all the
// currently selected elements.
            // If there is no selection or the cursor is not over a selected element, drag
            // the element under the cursor.
// If nothing can be selected, abort the drag operation.

mDragSelection.clear();
//Synthetic comment -- @@ -1442,22 +1458,29 @@
int x = mHScale.inverseTranslate(e.x);
int y = mVScale.inverseTranslate(e.y);

for (CanvasSelection cs : mSelections) {
if (cs.getRect().contains(x, y)) {
                        mDragSelection.addAll(mSelections);
break;
}
}

                if (mDragSelection.isEmpty()) {
                    // There is no selected element under the cursor.
                    // We'll now try to find another element.

CanvasViewInfo vi = findViewInfoAt(x, y);
if (vi != null) {
                        mDragSelection.add(new CanvasSelection(vi, mRulesEngine, mNodeFactory));
}
}
}

sanitizeSelection(mDragSelection);







