/*Grid Layout tweaks. DO NOT MERGE

This switches the grid layout rule editor over to the
more traditional grid-oriented editing, and makes some
tweaks to that handling (along with some bug fixes for
namespace handling). This is a better editing experience
than the richer but currently broken free form editing
of GridLayouts.

Change-Id:I31fe4ecf2d950e628bdf6494d2a8ba0d40276954*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GridLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GridLayoutRule.java
//Synthetic comment -- index a197e23..80a23c6 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import static com.android.SdkConstants.GRAVITY_VALUE_FILL_HORIZONTAL;
import static com.android.SdkConstants.GRAVITY_VALUE_FILL_VERTICAL;
import static com.android.SdkConstants.GRAVITY_VALUE_LEFT;
import static com.android.SdkConstants.VALUE_HORIZONTAL;
import static com.android.SdkConstants.VALUE_TRUE;

//Synthetic comment -- @@ -53,6 +54,7 @@
import com.android.ide.common.layout.grid.GridDropHandler;
import com.android.ide.common.layout.grid.GridLayoutPainter;
import com.android.ide.common.layout.grid.GridModel;
import com.android.utils.Pair;

import java.net.URL;
//Synthetic comment -- @@ -143,7 +145,7 @@
* Whether the grid is edited in "grid mode" where the operations are row/column based
* rather than free-form
*/
    public static boolean sGridMode = false;

/** Constructs a new {@link GridLayoutRule} */
public GridLayoutRule() {
//Synthetic comment -- @@ -228,6 +230,9 @@

// Add and Remove Column actions only apply in Grid Mode
if (sGridMode) {
// Add Row and Add Column
actions.add(RuleAction.createSeparator(150));
actions.add(RuleAction.createAction(ACTION_ADD_COL, "Add Column", actionCallback,
//Synthetic comment -- @@ -366,7 +371,8 @@
public String getNamespace(INode layout) {
String namespace = ANDROID_URI;

        if (!layout.getFqcn().equals(FQCN_GRID_LAYOUT)) {
namespace = mRulesEngine.getAppNameSpace();
}

//Synthetic comment -- @@ -407,10 +413,12 @@
boolean moved) {
super.onRemovingChildren(deleted, parent, moved);

        // Attempt to clean up spacer objects for any newly-empty rows or columns
        // as the result of this deletion
        GridModel grid = GridModel.get(mRulesEngine, parent, null);
        grid.onDeleted(deleted);
}

@Override
//Synthetic comment -- @@ -454,6 +462,35 @@
Rect oldBounds, Rect newBounds, SegmentType horizontalEdge, SegmentType verticalEdge) {

if (resizingWidget(state)) {
super.setNewSizeBounds(state, node, layout, oldBounds, newBounds, horizontalEdge,
verticalEdge);
} else {
//Synthetic comment -- @@ -463,6 +500,22 @@
GridModel grid = getGrid(state);
grid.setColumnSpanAttribute(node, columnSpan);
grid.setRowSpanAttribute(node, rowSpan);
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridDropHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridDropHandler.java
//Synthetic comment -- index 8a6fdef..8bdb56b 100644

//Synthetic comment -- @@ -15,17 +15,17 @@
*/
package com.android.ide.common.layout.grid;

import static com.android.ide.common.layout.GravityHelper.getGravity;
import static com.android.ide.common.layout.GridLayoutRule.GRID_SIZE;
import static com.android.ide.common.layout.GridLayoutRule.MARGIN_SIZE;
import static com.android.ide.common.layout.GridLayoutRule.MAX_CELL_DIFFERENCE;
import static com.android.ide.common.layout.GridLayoutRule.SHORT_GAP_DP;
import static com.android.SdkConstants.ATTR_COLUMN_COUNT;
import static com.android.SdkConstants.ATTR_LAYOUT_COLUMN;
import static com.android.SdkConstants.ATTR_LAYOUT_COLUMN_SPAN;
import static com.android.SdkConstants.ATTR_LAYOUT_GRAVITY;
import static com.android.SdkConstants.ATTR_LAYOUT_ROW;
import static com.android.SdkConstants.ATTR_LAYOUT_ROW_SPAN;
import static com.android.ide.common.layout.grid.GridModel.UNDEFINED;
import static java.lang.Math.abs;

//Synthetic comment -- @@ -46,6 +46,7 @@
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
* The {@link GridDropHandler} handles drag and drop operations into and within a
//Synthetic comment -- @@ -83,8 +84,10 @@
int x1 = p.x;
int y1 = p.y;

if (!GridLayoutRule.sGridMode) {
            Rect dragBounds = feedback.dragBounds;
if (dragBounds != null) {
// Sometimes the items are centered under the mouse so
// offset by the top left corner distance
//Synthetic comment -- @@ -92,8 +95,6 @@
y1 += dragBounds.y;
}

            int w = dragBounds != null ? dragBounds.w : 0;
            int h = dragBounds != null ? dragBounds.h : 0;
int x2 = x1 + w;
int y2 = y1 + h;

//Synthetic comment -- @@ -185,19 +186,72 @@
int SLOP = 2;
int radius = mRule.getNewCellSize();
if (rightDistance < radius + SLOP) {
                column++;
leftDistance = rightDistance;
}
if (bottomDistance < radius + SLOP) {
                row++;
topDistance = bottomDistance;
}

            boolean matchLeft = leftDistance < radius + SLOP;
            boolean matchTop = topDistance < radius + SLOP;

            mColumnMatch = new GridMatch(SegmentType.LEFT, 0, x1, column, matchLeft, 0);
            mRowMatch = new GridMatch(SegmentType.TOP, 0, y1, row, matchTop, 0);
}
}

//Synthetic comment -- @@ -713,16 +767,46 @@
String fqcn = element.getFqcn();
INode newChild = targetNode.appendChild(fqcn);

if (mColumnMatch.createCell) {
            mGrid.addColumn(mColumnMatch.cellIndex,
newChild, UNDEFINED, false, UNDEFINED, UNDEFINED);
}
if (mRowMatch.createCell) {
            mGrid.addRow(mRowMatch.cellIndex, newChild, UNDEFINED, false, UNDEFINED, UNDEFINED);
}

        mGrid.setGridAttribute(newChild, ATTR_LAYOUT_COLUMN, mColumnMatch.cellIndex);
        mGrid.setGridAttribute(newChild, ATTR_LAYOUT_ROW, mRowMatch.cellIndex);

return newChild;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridLayoutPainter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridLayoutPainter.java
//Synthetic comment -- index 9e7cfae..7e2d3a7 100644

//Synthetic comment -- @@ -282,10 +282,12 @@
gc.drawRect(b.x + 2 * radius, b.y + 2 * radius,
b.x2() - 2 * radius, b.y2() - 2 * radius);

            int column = data.getColumnMatch().cellIndex;
            int row = data.getRowMatch().cellIndex;
            boolean createColumn = data.getColumnMatch().createCell;
            boolean createRow = data.getRowMatch().createCell;

Rect cellBounds = grid.getCellBounds(row, column, 1, 1);

//Synthetic comment -- @@ -312,7 +314,22 @@
}

gc.useStyle(DrawingStyle.DROP_PREVIEW);
            mRule.drawElement(gc, first, offsetX, offsetY);
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridMatch.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridMatch.java
//Synthetic comment -- index 186e7d0..9bee343 100644

//Synthetic comment -- @@ -124,6 +124,7 @@
}
return String.format("Align bottom at y=%1d", matchedLine - layout.getBounds().y);
case CENTER_VERTICAL:
case UNKNOWN:
default:
return null;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridModel.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridModel.java
//Synthetic comment -- index fa9a11f..a453147 100644

//Synthetic comment -- @@ -80,7 +80,7 @@
static final int UNDEFINED = Integer.MIN_VALUE;

/** The size of spacers in the dimension that they are not defining */
    private static final int SPACER_SIZE_DP = 1;

/** Attribute value used for {@link #SPACER_SIZE_DP} */
private static final String SPACER_SIZE = String.format(VALUE_N_DP, SPACER_SIZE_DP);
//Synthetic comment -- @@ -361,7 +361,8 @@
if (mNamespace == null) {
mNamespace = ANDROID_URI;

            if (!layout.getFqcn().equals(FQCN_GRID_LAYOUT)) {
mNamespace = mRulesEngine.getAppNameSpace();
}
}
//Synthetic comment -- @@ -681,11 +682,26 @@
if (cellBounds != null) {
int[] xs = cellBounds.getFirst();
int[] ys = cellBounds.getSecond();

actualColumnCount = xs.length - 1;
actualRowCount = ys.length - 1;

                Rect layoutBounds = layout.getBounds();
int layoutBoundsX = layoutBounds.x;
int layoutBoundsY = layoutBounds.y;
mLeft = new int[xs.length];
//Synthetic comment -- @@ -1810,7 +1826,7 @@
* Data about a view in a table; this is not the same as a cell because multiple views
* can share a single cell, and a view can span many cells.
*/
    class ViewData {
public final INode node;
public final int index;
public int row;







