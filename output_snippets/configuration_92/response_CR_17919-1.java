//<Beginning of snippet n. 0>
public void handleDrag(int x, int y) {
    boolean insideSelection = false;
    boolean rootSelected = false;

    for (CanvasSelection cs : mSelections) {
        if (cs.getRect().contains(x, y)) {
            insideSelection = true;
            if (cs.isRoot()) {
                rootSelected = true;
            }
        }
    }
    
    if (insideSelection) {
        if (rootSelected && mSelections.size() == 1) {
            return;
        } else if (!rootSelected) {
            mDragSelection.clear();
            mDragSelection.addAll(mSelections);
        }
    } else if (!rootSelected) {
        resetSelection();
    }
}

/**
* Returns the selected view info. Cannot be null.
*/
public CanvasViewInfo getViewInfo() {
//<End of snippet n. 0>