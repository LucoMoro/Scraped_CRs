
//<Beginning of snippet n. 0>


}

/**
     * Returns true when this selection item represents the root, the top level
     * layout element in the editor.
     */
    public boolean isRoot() {
        return mNodeProxy.getParent() == null;
    }

    /**
* Returns the selected view info. Cannot be null.
*/
public CanvasViewInfo getViewInfo() {

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


boolean insideSelection = false;

for (CanvasSelection cs : mSelections) {
                    if (!cs.isRoot() && cs.getRect().contains(x, y)) {
insideSelection = true;
break;
}
// We should now have a proper selection that matches the cursor.
// Let's use this one. We make a copy of it since the "sanitize" pass
// below might remove some of the selected objects.
                    if (mSelections.size() == 1) {
                        // You are dragging just the root -- we will let you do
                        // that since you seem to be trying to.
                        mDragSelection.addAll(mSelections);
                    } else {
                        // Only drag non-root items.
                        for (CanvasSelection cs : mSelections) {
                            if (!cs.isRoot()) {
                                mDragSelection.add(cs);
                            }
                        }
                    }
}
}


//<End of snippet n. 1>








