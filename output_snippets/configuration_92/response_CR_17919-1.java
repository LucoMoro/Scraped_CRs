//<Beginning of snippet n. 0>
public CanvasViewInfo getViewInfo() {
    // Existing implementation
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

if (insideSelection) {
    // Only add non-root selections to the drag selection.
    for (CanvasSelection cs : mSelections) {
        if (!cs.isRoot()) {
            mDragSelection.add(cs);
        }
    }
} else if (mSelections.size() == 1 && mSelections.get(0).isRoot()) {
    // Allow dragging of the root alone if no other items are selected.
    mDragSelection.add(mSelections.get(0));
}

//<End of snippet n. 1>