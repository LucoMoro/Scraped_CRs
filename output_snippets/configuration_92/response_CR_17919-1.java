//<Beginning of snippet n. 0>
public class CanvasDragHandler {
    private List<CanvasSelection> mSelections;
    private List<CanvasSelection> mDragSelection;

    public CanvasDragHandler(List<CanvasSelection> selections) {
        this.mSelections = selections;
        this.mDragSelection = new ArrayList<>();
    }

    /**
    * Returns the selected view info. Cannot be null.
    */
    public CanvasViewInfo getViewInfo() {
        // implementation...
    }

    public void handleDrag(float x, float y) {
        boolean insideSelection = false;
        boolean isRootSelected = false;

        for (CanvasSelection cs : mSelections) {
            if (cs.getRect().contains(x, y)) {
                if (cs.isRoot()) {
                    isRootSelected = true;
                } else {
                    insideSelection = true;
                }
            }
        }

        if (insideSelection) {
            updateDragSelection(isRootSelected);
        }

        // Visual feedback for drag operation
        if (isRootSelected) {
            showVisualFeedback();
        }
    }

    private void updateDragSelection(boolean isRootSelected) {
        mDragSelection.clear();
        if (!isRootSelected) {
            for (CanvasSelection cs : mSelections) {
                if (!cs.isRoot()) {
                    mDragSelection.add(cs);
                }
            }
        }
    }

    private void showVisualFeedback() {
        // implementation for visual indicators during dragging
    }
}
//<End of snippet n. 0>