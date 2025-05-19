//<Beginning of snippet n. 0>
}

/**
 * Returns the selected view info. Cannot be null.
 */
public CanvasViewInfo getViewInfo() {
    return new CanvasViewInfo();
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
boolean insideSelection = false;

for (CanvasSelection cs : mSelections) {
    if (cs.getRect().contains(x, y)) {
        insideSelection = true;
        break;
    }
}

// Validate the selection state to check for a valid dragging action
if (insideSelection) {
    boolean rootSelected = mSelections.stream().anyMatch(CanvasSelection::isRootLayout);
    
    if (mSelections.size() == 1 && rootSelected) {
        // Allow dragging if only root is selected
        mDragSelection.clear();
        mDragSelection.addAll(mSelections);
    } else if (mSelections.size() == 0 || !rootSelected) {
        // Allow dragging only if no root is selected or not selected at all
        mDragSelection.clear();
        mDragSelection.addAll(mSelections);
    } else {
        // Prevent dragging if root is selected with other items
        // User feedback can be implemented here if needed
        return;
    }
}
//<End of snippet n. 1>