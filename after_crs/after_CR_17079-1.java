/*ADT GLE2: adjust selection on right-click or drag.

Change-Id:I553e2d659f95d727e87df5d174d737a220a1685d*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index dbdd7aa..c143c35 100755

//Synthetic comment -- @@ -1060,19 +1060,35 @@
*/
private void onMouseUp(MouseEvent e) {

boolean isShift = (e.stateMask & SWT.SHIFT) != 0;
boolean isAlt   = (e.stateMask & SWT.ALT)   != 0;

int x = mHScale.inverseTranslate(e.x);
int y = mVScale.inverseTranslate(e.y);

        if (e.button == 3) {
            // Right click button is used to display a context menu.
            // If there's an existing selection and the click is anywhere in this selection
            // and there are no modifiers being used, we don't want to change the selection.
            // Otherwise we select the item under the cursor.

            if (!isAlt && !isShift) {
                for (CanvasSelection cs : mSelections) {
                    if (cs.getRect().contains(x, y)) {
                        // The cursor is inside the selection. Don't change anything.
                        return;
                    }
                }
            }

        } else if (e.button != 1) {
            // Click was done with something else than the left button for normal selection
            // or the right button for context menu.
            // We don't use mouse button 2 yet (middle mouse, or scroll wheel?) for
            // anything, so let's not change the selection.
            return;
        }

CanvasViewInfo vi = findViewInfoAt(x, y);

if (isShift && !isAlt) {
//Synthetic comment -- @@ -1413,10 +1429,10 @@

/**
* The current selection being dragged.
         * This may be a subset of the canvas selection due to the "sanitize" pass.
* Can be empty but never null.
*/
        private final ArrayList<CanvasSelection> mDragSelection = new ArrayList<CanvasSelection>();
private SimpleElement[] mDragElements;

/**
//Synthetic comment -- @@ -1431,8 +1447,8 @@
// We need a selection (simple or multiple) to do any transfer.
// If there's a selection *and* the cursor is over this selection, use all the
// currently selected elements.
            // If there is no selection or the cursor is not over a selected element, *change*
            // the selection to match the element under the cursor and use that.
// If nothing can be selected, abort the drag operation.

mDragSelection.clear();
//Synthetic comment -- @@ -1442,22 +1458,29 @@
int x = mHScale.inverseTranslate(e.x);
int y = mVScale.inverseTranslate(e.y);

                boolean insideSelection = false;

for (CanvasSelection cs : mSelections) {
if (cs.getRect().contains(x, y)) {
                        insideSelection = true;
break;
}
}

                if (!insideSelection) {
CanvasViewInfo vi = findViewInfoAt(x, y);
if (vi != null) {
                        selectSingle(vi);
                        insideSelection = true;
}
}

                if (insideSelection) {
                    // We should now have a proper selection that matches the cursor.
                    // Let's use this one. We make a copy of it since the "sanitize" pass
                    // below might remove some of the selected objects.
                    mDragSelection.addAll(mSelections);
                }
}

sanitizeSelection(mDragSelection);







