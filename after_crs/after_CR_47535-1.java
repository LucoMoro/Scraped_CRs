/*Grid Layout tweaks

This switches the grid layout rule editor over to the
more traditional grid-oriented editing, and makes some
tweaks to that handling (along with some bug fixes for
namespace handling). This is a better editing experience
than the richer but currently broken free form editing
of GridLayouts.

Change-Id:I4db2c604ddf34e37c6fe4dd2b8270d20c68dc12f*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GridLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GridLayoutRule.java
//Synthetic comment -- index a197e23..80a23c6 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import static com.android.SdkConstants.GRAVITY_VALUE_FILL_HORIZONTAL;
import static com.android.SdkConstants.GRAVITY_VALUE_FILL_VERTICAL;
import static com.android.SdkConstants.GRAVITY_VALUE_LEFT;
import static com.android.SdkConstants.GRID_LAYOUT;
import static com.android.SdkConstants.VALUE_HORIZONTAL;
import static com.android.SdkConstants.VALUE_TRUE;

//Synthetic comment -- @@ -53,6 +54,7 @@
import com.android.ide.common.layout.grid.GridDropHandler;
import com.android.ide.common.layout.grid.GridLayoutPainter;
import com.android.ide.common.layout.grid.GridModel;
import com.android.ide.common.layout.grid.GridModel.ViewData;
import com.android.utils.Pair;

import java.net.URL;
//Synthetic comment -- @@ -143,7 +145,7 @@
* Whether the grid is edited in "grid mode" where the operations are row/column based
* rather than free-form
*/
    public static boolean sGridMode = true;

/** Constructs a new {@link GridLayoutRule} */
public GridLayoutRule() {
//Synthetic comment -- @@ -228,6 +230,9 @@

// Add and Remove Column actions only apply in Grid Mode
if (sGridMode) {
            actions.add(RuleAction.createToggle(ACTION_SHOW_STRUCTURE, "Show Structure",
                    sShowStructure, actionCallback, ICON_SHOW_STRUCT, 147, false));

// Add Row and Add Column
actions.add(RuleAction.createSeparator(150));
actions.add(RuleAction.createAction(ACTION_ADD_COL, "Add Column", actionCallback,
//Synthetic comment -- @@ -366,7 +371,8 @@
public String getNamespace(INode layout) {
String namespace = ANDROID_URI;

        String fqcn = layout.getFqcn();
        if (!fqcn.equals(GRID_LAYOUT) && !fqcn.equals(FQCN_GRID_LAYOUT)) {
namespace = mRulesEngine.getAppNameSpace();
}

//Synthetic comment -- @@ -407,10 +413,12 @@
boolean moved) {
super.onRemovingChildren(deleted, parent, moved);

        if (!sGridMode) {
            // Attempt to clean up spacer objects for any newly-empty rows or columns
            // as the result of this deletion
            GridModel grid = GridModel.get(mRulesEngine, parent, null);
            grid.onDeleted(deleted);
        }
}

@Override
//Synthetic comment -- @@ -454,6 +462,35 @@
Rect oldBounds, Rect newBounds, SegmentType horizontalEdge, SegmentType verticalEdge) {

if (resizingWidget(state)) {
            if (state.fillWidth || state.fillHeight || state.wrapWidth || state.wrapHeight) {
                GridModel grid = getGrid(state);
                ViewData view = grid.getView(node);
                if (view != null) {
                    String gravityString = grid.getGridAttribute(view.node, ATTR_LAYOUT_GRAVITY);
                    int gravity = GravityHelper.getGravity(gravityString, 0);
                    if (view.column > 0 && verticalEdge != null && state.fillWidth) {
                        state.fillWidth = false;
                        state.wrapWidth = true;
                        gravity &= ~GravityHelper.GRAVITY_HORIZ_MASK;
                        gravity |= GravityHelper.GRAVITY_FILL_HORIZ;
                    } else if (verticalEdge != null && state.wrapWidth) {
                        gravity &= ~GravityHelper.GRAVITY_HORIZ_MASK;
                        gravity |= GravityHelper.GRAVITY_LEFT;
                    }
                    if (view.row > 0 && horizontalEdge != null && state.fillHeight) {
                        state.fillHeight = false;
                        state.wrapHeight = true;
                        gravity &= ~GravityHelper.GRAVITY_VERT_MASK;
                        gravity |= GravityHelper.GRAVITY_FILL_VERT;
                    } else if (horizontalEdge != null && state.wrapHeight) {
                        gravity &= ~GravityHelper.GRAVITY_VERT_MASK;
                        gravity |= GravityHelper.GRAVITY_TOP;
                    }
                    gravityString = GravityHelper.getGravity(gravity);
                    grid.setGridAttribute(view.node, ATTR_LAYOUT_GRAVITY, gravityString);
                    // Fall through and set layout_width and/or layout_height to wrap_content
                }
            }
super.setNewSizeBounds(state, node, layout, oldBounds, newBounds, horizontalEdge,
verticalEdge);
} else {
//Synthetic comment -- @@ -463,6 +500,22 @@
GridModel grid = getGrid(state);
grid.setColumnSpanAttribute(node, columnSpan);
grid.setRowSpanAttribute(node, rowSpan);

            ViewData view = grid.getView(node);
            if (view != null) {
                String gravityString = grid.getGridAttribute(view.node, ATTR_LAYOUT_GRAVITY);
                int gravity = GravityHelper.getGravity(gravityString, 0);
                if (verticalEdge != null && columnSpan > 1) {
                    gravity &= ~GravityHelper.GRAVITY_HORIZ_MASK;
                    gravity |= GravityHelper.GRAVITY_FILL_HORIZ;
                }
                if (horizontalEdge != null && rowSpan > 1) {
                    gravity &= ~GravityHelper.GRAVITY_VERT_MASK;
                    gravity |= GravityHelper.GRAVITY_FILL_VERT;
                }
                gravityString = GravityHelper.getGravity(gravity);
                grid.setGridAttribute(view.node, ATTR_LAYOUT_GRAVITY, gravityString);
            }
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridDropHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridDropHandler.java
//Synthetic comment -- index 8a6fdef..8bdb56b 100644

//Synthetic comment -- @@ -15,17 +15,17 @@
*/
package com.android.ide.common.layout.grid;

import static com.android.SdkConstants.ATTR_COLUMN_COUNT;
import static com.android.SdkConstants.ATTR_LAYOUT_COLUMN;
import static com.android.SdkConstants.ATTR_LAYOUT_COLUMN_SPAN;
import static com.android.SdkConstants.ATTR_LAYOUT_GRAVITY;
import static com.android.SdkConstants.ATTR_LAYOUT_ROW;
import static com.android.SdkConstants.ATTR_LAYOUT_ROW_SPAN;
import static com.android.ide.common.layout.GravityHelper.getGravity;
import static com.android.ide.common.layout.GridLayoutRule.GRID_SIZE;
import static com.android.ide.common.layout.GridLayoutRule.MARGIN_SIZE;
import static com.android.ide.common.layout.GridLayoutRule.MAX_CELL_DIFFERENCE;
import static com.android.ide.common.layout.GridLayoutRule.SHORT_GAP_DP;
import static com.android.ide.common.layout.grid.GridModel.UNDEFINED;
import static java.lang.Math.abs;

//Synthetic comment -- @@ -46,6 +46,7 @@
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
* The {@link GridDropHandler} handles drag and drop operations into and within a
//Synthetic comment -- @@ -83,8 +84,10 @@
int x1 = p.x;
int y1 = p.y;

        Rect dragBounds = feedback.dragBounds;
        int w = dragBounds != null ? dragBounds.w : 0;
        int h = dragBounds != null ? dragBounds.h : 0;
if (!GridLayoutRule.sGridMode) {
if (dragBounds != null) {
// Sometimes the items are centered under the mouse so
// offset by the top left corner distance
//Synthetic comment -- @@ -92,8 +95,6 @@
y1 += dragBounds.y;
}

int x2 = x1 + w;
int y2 = y1 + h;

//Synthetic comment -- @@ -185,19 +186,72 @@
int SLOP = 2;
int radius = mRule.getNewCellSize();
if (rightDistance < radius + SLOP) {
                column = Math.min(column + 1, mGrid.actualColumnCount);
leftDistance = rightDistance;
}
if (bottomDistance < radius + SLOP) {
                row = Math.min(row + 1, mGrid.actualRowCount);
topDistance = bottomDistance;
}

            boolean createColumn = leftDistance < radius + SLOP;
            boolean createRow = topDistance < radius + SLOP;
            if (x1 >= bounds.x2()) {
                createColumn = true;
            }
            if (y1 >= bounds.y2()) {
                createRow = true;
            }

            int cellWidth = leftDistance + rightDistance;
            int cellHeight = topDistance + bottomDistance;
            SegmentType horizontalType = SegmentType.LEFT;
            SegmentType verticalType = SegmentType.TOP;
            int minDistance = 10; // Don't center or right/bottom align in tiny cells
            if (!createColumn && leftDistance > minDistance
                    && dragBounds != null && dragBounds.w < cellWidth - 10) {
                if (rightDistance < leftDistance) {
                    horizontalType = SegmentType.RIGHT;
                }

                int centerDistance = Math.abs(cellWidth / 2 - leftDistance);
                if (centerDistance < leftDistance / 2 && centerDistance < rightDistance / 2) {
                    horizontalType = SegmentType.CENTER_HORIZONTAL;
                }
            }
            if (!createRow && topDistance > minDistance
                    && dragBounds != null && dragBounds.h < cellHeight - 10) {
                if (bottomDistance < topDistance) {
                    verticalType = SegmentType.BOTTOM;
                }
                int centerDistance = Math.abs(cellHeight / 2 - topDistance);
                if (centerDistance < topDistance / 2 && centerDistance < bottomDistance / 2) {
                    verticalType = SegmentType.CENTER_VERTICAL;
                }
            }

            mColumnMatch = new GridMatch(horizontalType, 0, x1, column, createColumn, 0);
            mRowMatch = new GridMatch(verticalType, 0, y1, row, createRow, 0);

            StringBuilder description = new StringBuilder(50);
            String rowString = Integer.toString(mColumnMatch.cellIndex + 1);
            String columnString = Integer.toString(mRowMatch.cellIndex + 1);
            if (mRowMatch.createCell && mRowMatch.cellIndex < mGrid.actualRowCount) {
                description.append(String.format("Shift row %1$d down", mRowMatch.cellIndex + 1));
                description.append('\n');
            }
            if (mColumnMatch.createCell && mColumnMatch.cellIndex < mGrid.actualColumnCount) {
                description.append(String.format("Shift column %1$d right",
                        mColumnMatch.cellIndex + 1));
                description.append('\n');
            }
            description.append(String.format("Insert into cell (%1$s,%2$s)",
                    rowString, columnString));
            description.append('\n');
            description.append(String.format("Align %1$s, %2$s",
                    horizontalType.name().toLowerCase(Locale.US),
                    verticalType.name().toLowerCase(Locale.US)));
            feedback.tooltip = description.toString();
}
}

//Synthetic comment -- @@ -713,16 +767,46 @@
String fqcn = element.getFqcn();
INode newChild = targetNode.appendChild(fqcn);

        int column = mColumnMatch.cellIndex;
if (mColumnMatch.createCell) {
            mGrid.addColumn(column,
newChild, UNDEFINED, false, UNDEFINED, UNDEFINED);
}
        int row = mRowMatch.cellIndex;
if (mRowMatch.createCell) {
            mGrid.addRow(row, newChild, UNDEFINED, false, UNDEFINED, UNDEFINED);
}

        mGrid.setGridAttribute(newChild, ATTR_LAYOUT_COLUMN, column);
        mGrid.setGridAttribute(newChild, ATTR_LAYOUT_ROW, row);

        int gravity = 0;
        if (mColumnMatch.type == SegmentType.RIGHT) {
            gravity |= GravityHelper.GRAVITY_RIGHT;
        } else if (mColumnMatch.type == SegmentType.CENTER_HORIZONTAL) {
            gravity |= GravityHelper.GRAVITY_CENTER_HORIZ;
        }
        if (mRowMatch.type == SegmentType.BASELINE) {
            // There *is* no baseline gravity constant, instead, leave the
            // vertical gravity unspecified and GridLayout will treat it as
            // baseline alignment
            //gravity |= GravityHelper.GRAVITY_BASELINE;
        } else if (mRowMatch.type == SegmentType.BOTTOM) {
            gravity |= GravityHelper.GRAVITY_BOTTOM;
        } else if (mRowMatch.type == SegmentType.CENTER_VERTICAL) {
            gravity |= GravityHelper.GRAVITY_CENTER_VERT;
        }
        if (!GravityHelper.isConstrainedHorizontally(gravity)) {
            gravity |= GravityHelper.GRAVITY_LEFT;
        }
        if (!GravityHelper.isConstrainedVertically(gravity)) {
            gravity |= GravityHelper.GRAVITY_TOP;
        }
        mGrid.setGridAttribute(newChild, ATTR_LAYOUT_GRAVITY, getGravity(gravity));

        if (mGrid.declaredColumnCount == UNDEFINED || mGrid.declaredColumnCount < column + 1) {
            mGrid.setGridAttribute(mGrid.layout, ATTR_COLUMN_COUNT, column + 1);
        }

return newChild;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridLayoutPainter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridLayoutPainter.java
//Synthetic comment -- index 9e7cfae..7e2d3a7 100644

//Synthetic comment -- @@ -282,10 +282,12 @@
gc.drawRect(b.x + 2 * radius, b.y + 2 * radius,
b.x2() - 2 * radius, b.y2() - 2 * radius);

            GridMatch columnMatch = data.getColumnMatch();
            GridMatch rowMatch = data.getRowMatch();
            int column = columnMatch.cellIndex;
            int row = rowMatch.cellIndex;
            boolean createColumn = columnMatch.createCell;
            boolean createRow = rowMatch.createCell;

Rect cellBounds = grid.getCellBounds(row, column, 1, 1);

//Synthetic comment -- @@ -312,7 +314,22 @@
}

gc.useStyle(DrawingStyle.DROP_PREVIEW);

            Rect bounds = first.getBounds();
            int x = offsetX;
            int y = offsetY;
            if (columnMatch.type == SegmentType.RIGHT) {
                x += cellBounds.w - bounds.w;
            } else if (columnMatch.type == SegmentType.CENTER_HORIZONTAL) {
                x += cellBounds.w / 2 - bounds.w / 2;
            }
            if (rowMatch.type == SegmentType.BOTTOM) {
                y += cellBounds.h - bounds.h;
            } else if (rowMatch.type == SegmentType.CENTER_VERTICAL) {
                y += cellBounds.h / 2 - bounds.h / 2;
            }

            mRule.drawElement(gc, first, x, y);
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridMatch.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridMatch.java
//Synthetic comment -- index 186e7d0..9bee343 100644

//Synthetic comment -- @@ -124,6 +124,7 @@
}
return String.format("Align bottom at y=%1d", matchedLine - layout.getBounds().y);
case CENTER_VERTICAL:
                return "Center vertically";
case UNKNOWN:
default:
return null;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridModel.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridModel.java
//Synthetic comment -- index fa9a11f..a453147 100644

//Synthetic comment -- @@ -80,7 +80,7 @@
static final int UNDEFINED = Integer.MIN_VALUE;

/** The size of spacers in the dimension that they are not defining */
    static final int SPACER_SIZE_DP = 1;

/** Attribute value used for {@link #SPACER_SIZE_DP} */
private static final String SPACER_SIZE = String.format(VALUE_N_DP, SPACER_SIZE_DP);
//Synthetic comment -- @@ -361,7 +361,8 @@
if (mNamespace == null) {
mNamespace = ANDROID_URI;

            String fqcn = layout.getFqcn();
            if (!fqcn.equals(GRID_LAYOUT) && !fqcn.equals(FQCN_GRID_LAYOUT)) {
mNamespace = mRulesEngine.getAppNameSpace();
}
}
//Synthetic comment -- @@ -681,11 +682,26 @@
if (cellBounds != null) {
int[] xs = cellBounds.getFirst();
int[] ys = cellBounds.getSecond();
                Rect layoutBounds = layout.getBounds();

                // Handle "blank" grid layouts: insert a fake grid of CELL_COUNT^2 cells
                // where the user can do initial placement
                if (actualColumnCount <= 1 && actualRowCount <= 1 && mChildViews.isEmpty()) {
                    final int CELL_COUNT = 1;
                    xs = new int[CELL_COUNT + 1];
                    ys = new int[CELL_COUNT + 1];
                    int cellWidth = layoutBounds.w / CELL_COUNT;
                    int cellHeight = layoutBounds.h / CELL_COUNT;

                    for (int i = 0; i <= CELL_COUNT; i++) {
                        xs[i] = i * cellWidth;
                        ys[i] = i * cellHeight;
                    }
                }

actualColumnCount = xs.length - 1;
actualRowCount = ys.length - 1;

int layoutBoundsX = layoutBounds.x;
int layoutBoundsY = layoutBounds.y;
mLeft = new int[xs.length];
//Synthetic comment -- @@ -1810,7 +1826,7 @@
* Data about a view in a table; this is not the same as a cell because multiple views
* can share a single cell, and a view can span many cells.
*/
    public class ViewData {
public final INode node;
public final int index;
public int row;







