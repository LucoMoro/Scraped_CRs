/*GridLayout support work

Lots of miscellaneous fixes to the GridLayout support in ADT.  The
changes include using the GridLayout state (via reflection) to
populate the model; caching the grid model for performance, and a
bunch of fixes to the code which handles insertions and
removals. There are also some new unit tests. This is not done, but is
an improvement over the current state.

Change-Id:I4851153d3e409630c2d2024c4894d1ad1535fb47*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GravityHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GravityHelper.java
//Synthetic comment -- index aa9a089..6516938 100644

//Synthetic comment -- @@ -15,7 +15,6 @@
*/
package com.android.ide.common.layout;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_GRAVITY;
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_BOTTOM;
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_CENTER;
//Synthetic comment -- @@ -27,21 +26,41 @@
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_LEFT;
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_TOP;

import org.w3c.dom.Element;

/** Helper class for looking up the gravity masks of gravity attributes */
public class GravityHelper {
public static final int GRAVITY_LEFT         = 1 << 0;
public static final int GRAVITY_RIGHT        = 1 << 1;
public static final int GRAVITY_CENTER_HORIZ = 1 << 2;
public static final int GRAVITY_FILL_HORIZ   = 1 << 3;
public static final int GRAVITY_CENTER_VERT  = 1 << 4;
public static final int GRAVITY_FILL_VERT    = 1 << 5;
public static final int GRAVITY_TOP          = 1 << 6;
public static final int GRAVITY_BOTTOM       = 1 << 7;
public static final int GRAVITY_HORIZ_MASK = GRAVITY_CENTER_HORIZ | GRAVITY_FILL_HORIZ
| GRAVITY_LEFT | GRAVITY_RIGHT;
public static final int GRAVITY_VERT_MASK = GRAVITY_CENTER_VERT | GRAVITY_FILL_VERT
| GRAVITY_TOP | GRAVITY_BOTTOM;

//Synthetic comment -- @@ -96,4 +115,90 @@

return gravity;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GridLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GridLayoutRule.java
//Synthetic comment -- index a737251..787a2c2 100644

//Synthetic comment -- @@ -205,7 +205,7 @@
return;
}

                        GridModel grid = new GridModel(mRulesEngine, parentNode, null);
if (id.equals(ACTION_ADD_ROW)) {
grid.addRow(children);
} else if (id.equals(ACTION_REMOVE_ROW)) {
//Synthetic comment -- @@ -285,6 +285,9 @@
@Override
public DropFeedback onDropMove(@NonNull INode targetNode, @NonNull IDragElement[] elements,
@Nullable DropFeedback feedback, @NonNull Point p) {
feedback.requestPaint = true;

GridDropHandler handler = (GridDropHandler) feedback.userData;
//Synthetic comment -- @@ -296,6 +299,10 @@
@Override
public void onDropped(final @NonNull INode targetNode, final @NonNull IDragElement[] elements,
@Nullable DropFeedback feedback, @NonNull Point p) {
Rect b = targetNode.getBounds();
if (!b.isValid()) {
return;
//Synthetic comment -- @@ -334,13 +341,14 @@
return;
}

// Attempt to set "fill" properties on newly added views such that for example
// a text field will stretch horizontally.
String fqcn = node.getFqcn();
IViewMetadata metadata = mRulesEngine.getMetadata(fqcn);
        if (metadata == null) {
            return;
        }
FillPreference fill = metadata.getFillPreference();
String gravity = computeDefaultGravity(fill);
if (gravity != null) {
//Synthetic comment -- @@ -400,17 +408,8 @@

// Attempt to clean up spacer objects for any newly-empty rows or columns
// as the result of this deletion
        GridModel grid = new GridModel(mRulesEngine, parent, null);
        for (INode child : deleted) {
            // We don't care about deletion of spacers
            String fqcn = child.getFqcn();
            if (fqcn.equals(FQCN_SPACE) || fqcn.equals(FQCN_SPACE_V7)) {
                continue;
            }
            grid.markDeleted(child);
        }

        grid.cleanup();
}

@Override
//Synthetic comment -- @@ -442,7 +441,7 @@
private GridModel getGrid(ResizeState resizeState) {
GridModel grid = (GridModel) resizeState.clientData;
if (grid == null) {
            grid = new GridModel(mRulesEngine, resizeState.layout, resizeState.layoutView);
resizeState.clientData = grid;
}

//Synthetic comment -- @@ -543,10 +542,10 @@
}
}
GridLayoutPainter.paintStructure(DrawingStyle.GUIDELINE_DASHED,
                        parentNode, graphics, new GridModel(mRulesEngine, parentNode, view));
} else if (sDebugGridLayout) {
GridLayoutPainter.paintStructure(DrawingStyle.GRID,
                    parentNode, graphics, new GridModel(mRulesEngine, parentNode, view));
}

// TBD: Highlight the cells around the selection, and display easy controls








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridDropHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridDropHandler.java
//Synthetic comment -- index 74c1b59..6d938fb 100644

//Synthetic comment -- @@ -15,6 +15,7 @@
*/
package com.android.ide.common.layout.grid;

import static com.android.ide.common.layout.GridLayoutRule.GRID_SIZE;
import static com.android.ide.common.layout.GridLayoutRule.MARGIN_SIZE;
import static com.android.ide.common.layout.GridLayoutRule.MAX_CELL_DIFFERENCE;
//Synthetic comment -- @@ -25,9 +26,6 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_GRAVITY;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ROW;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ROW_SPAN;
import static com.android.ide.common.layout.LayoutConstants.VALUE_BOTTOM;
import static com.android.ide.common.layout.LayoutConstants.VALUE_CENTER_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_RIGHT;
import static com.android.ide.common.layout.grid.GridModel.UNDEFINED;
import static java.lang.Math.abs;

//Synthetic comment -- @@ -40,6 +38,7 @@
import com.android.ide.common.api.Rect;
import com.android.ide.common.api.SegmentType;
import com.android.ide.common.layout.BaseLayoutRule;
import com.android.ide.common.layout.GridLayoutRule;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.ViewMetadataRepository;

//Synthetic comment -- @@ -66,7 +65,7 @@
*/
public GridDropHandler(GridLayoutRule gridLayoutRule, INode layout, Object view) {
mRule = gridLayoutRule;
        mGrid = new GridModel(mRule.getRulesEngine(), layout, view);
}

/**
//Synthetic comment -- @@ -522,9 +521,7 @@
int rowSpan = endRow - row + 1;

// Make sure my math was right:
        if (mRowMatch.type == SegmentType.BASELINE) {
            assert rowSpan == 1 : rowSpan;
        }

// If the item almost fits into the row (at most N % bigger) then just enlarge
// the row; don't add a rowspan since that will defeat baseline alignment etc
//Synthetic comment -- @@ -588,9 +585,6 @@
if (insertMarginColumn) {
column++;
}
            // TODO: This grid refresh is a little risky because we may have added a new
            // child (spacer) which has no bounds yet!
            mGrid.loadFromXml();
}

// Split cells to make a new  row
//Synthetic comment -- @@ -628,7 +622,6 @@
if (insertMarginRow) {
row++;
}
            mGrid.loadFromXml();
}

// Figure out where to insert the new child
//Synthetic comment -- @@ -649,22 +642,33 @@
next.applyPositionAttributes();
}

        // Set the cell position of the new widget
if (mColumnMatch.type == SegmentType.RIGHT) {
            mGrid.setGridAttribute(newChild, ATTR_LAYOUT_GRAVITY, VALUE_RIGHT);
} else if (mColumnMatch.type == SegmentType.CENTER_HORIZONTAL) {
            mGrid.setGridAttribute(newChild, ATTR_LAYOUT_GRAVITY, VALUE_CENTER_HORIZONTAL);
}
mGrid.setGridAttribute(newChild, ATTR_LAYOUT_COLUMN, column);
if (mRowMatch.type == SegmentType.BOTTOM) {
            String value = VALUE_BOTTOM;
            if (mColumnMatch.type == SegmentType.RIGHT) {
                value = value + '|' + VALUE_RIGHT;
            } else if (mColumnMatch.type == SegmentType.CENTER_HORIZONTAL) {
                    value = value + '|' + VALUE_CENTER_HORIZONTAL;
            }
            mGrid.setGridAttribute(newChild, ATTR_LAYOUT_GRAVITY, value);
}
mGrid.setGridAttribute(newChild, ATTR_LAYOUT_ROW, row);

// Apply spans to ensure that the widget can fit without pushing columns
//Synthetic comment -- @@ -700,7 +704,6 @@
newChild, UNDEFINED, false, UNDEFINED, UNDEFINED);
}
if (mRowMatch.createCell) {
            mGrid.loadFromXml();
mGrid.addRow(mRowMatch.cellIndex, newChild, UNDEFINED, false, UNDEFINED, UNDEFINED);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridLayoutPainter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridLayoutPainter.java
//Synthetic comment -- index 461ca2b..9f68afb 100644

//Synthetic comment -- @@ -234,15 +234,27 @@
gc.drawLine(x, b.y, x, b.y2());
}

            // Draw preview rectangle of the first dragged element
gc.useStyle(DrawingStyle.DROP_PREVIEW);
            mRule.drawElement(gc, first, x + offsetX - bounds.x, y + offsetY - bounds.y);

            // Preview baseline as well
            if (feedback.dragBaseline != -1) {
                int x1 = dragBounds.x + x + offsetX - bounds.x;
                int y1 = dragBounds.y + y + offsetY - bounds.y + feedback.dragBaseline;
                gc.drawLine(x1, y1, x1 + dragBounds.w, y1);
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridMatch.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridMatch.java
//Synthetic comment -- index aaca4bc..186e7d0 100644

//Synthetic comment -- @@ -88,39 +88,39 @@
public String getDisplayName(INode layout) {
switch (type) {
case BASELINE:
                return String.format("Align baseline in row %1$d", cellIndex);
case CENTER_HORIZONTAL:
return "Center horizontally";
case LEFT:
if (!createCell) {
                    return String.format("Insert into column %1$d", cellIndex);
}
if (margin != UNDEFINED) {
if (cellIndex == 0 && margin != 0) {
return "Add one margin distance from the left";
}
                    return String.format("Add next to column %1$d", cellIndex);
}
return String.format("Align left at x=%1$d", matchedLine - layout.getBounds().x);
case RIGHT:
if (!createCell) {
                    return String.format("Insert right-aligned into column %1$d", cellIndex);
}
return String.format("Align right at x=%1$d", matchedLine - layout.getBounds().x);
case TOP:
if (!createCell) {
                    return String.format("Insert into row %1$d", cellIndex);
}
if (margin != UNDEFINED) {
if (cellIndex == 0 && margin != 0) {
return "Add one margin distance from the top";
}
                    return String.format("Add below row %1$d", cellIndex);
}
return String.format("Align top at y=%1d", matchedLine - layout.getBounds().y);
case BOTTOM:
if (!createCell) {
                    return String.format("Insert into bottom of row %1$d", cellIndex);
}
return String.format("Align bottom at y=%1d", matchedLine - layout.getBounds().y);
case CENTER_VERTICAL:








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridModel.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridModel.java
//Synthetic comment -- index 1d17c9b..65a61b4 100644

//Synthetic comment -- @@ -19,7 +19,6 @@
import static com.android.ide.common.layout.GravityHelper.GRAVITY_CENTER_HORIZ;
import static com.android.ide.common.layout.GravityHelper.GRAVITY_CENTER_VERT;
import static com.android.ide.common.layout.GravityHelper.GRAVITY_RIGHT;
import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_COLUMN_COUNT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_COLUMN;
//Synthetic comment -- @@ -42,10 +41,13 @@
import static com.android.ide.common.layout.LayoutConstants.VALUE_N_DP;
import static com.android.ide.common.layout.LayoutConstants.VALUE_TOP;
import static com.android.ide.common.layout.LayoutConstants.VALUE_VERTICAL;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import com.android.ide.common.api.IClientRulesEngine;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewMetadata;
//Synthetic comment -- @@ -54,10 +56,14 @@
import com.android.ide.common.layout.GravityHelper;
import com.android.ide.common.layout.GridLayoutRule;
import com.android.util.Pair;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
//Synthetic comment -- @@ -67,8 +73,6 @@
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Models a GridLayout */
public class GridModel {
//Synthetic comment -- @@ -77,25 +81,31 @@

/** The size of spacers in the dimension that they are not defining */
private static final int SPACER_SIZE_DP = 1;
/** Attribute value used for {@link #SPACER_SIZE_DP} */
private static final String SPACER_SIZE = String.format(VALUE_N_DP, SPACER_SIZE_DP);
/** Width assigned to a newly added column with the Add Column action */
private static final int DEFAULT_CELL_WIDTH = 100;
/** Height assigned to a newly added row with the Add Row action */
private static final int DEFAULT_CELL_HEIGHT = 15;
    private static final Pattern DIP_PATTERN = Pattern.compile("(\\d+)dp"); //$NON-NLS-1$

/** The GridLayout node, never null */
public final INode layout;

/** True if this is a vertical layout, and false if it is horizontal (the default) */
public boolean vertical;
/** The declared count of rows (which may be {@link #UNDEFINED} if not specified) */
public int declaredRowCount;
/** The declared count of columns (which may be {@link #UNDEFINED} if not specified) */
public int declaredColumnCount;
/** The actual count of rows found in the grid */
public int actualRowCount;
/** The actual count of columns found in the grid */
public int actualColumnCount;

//Synthetic comment -- @@ -137,15 +147,6 @@
/** The {@link IClientRulesEngine} */
private final IClientRulesEngine mRulesEngine;

    /** List of nodes marked for deletion (may be null) */
    private Set<INode> mDeleted;

    /**
     * Flag which tracks whether we've edited the DOM model, in which case the grid data
     * may be stale and should be refreshed.
     */
    private boolean stale;

/**
* An actual instance of a GridLayout object that this grid model corresponds to.
*/
//Synthetic comment -- @@ -161,13 +162,44 @@
* @param node the GridLayout node
* @param viewObject an actual GridLayout instance, or null
*/
    public GridModel(IClientRulesEngine rulesEngine, INode node, Object viewObject) {
mRulesEngine = rulesEngine;
layout = node;
mViewObject = viewObject;
loadFromXml();
}

/**
* Returns the {@link ViewData} for the child at the given index
*
//Synthetic comment -- @@ -365,7 +397,7 @@
/**
* Loads a {@link GridModel} from the XML model.
*/
    void loadFromXml() {
INode[] children = layout.getChildren();

declaredRowCount = getGridAttribute(layout, ATTR_ROW_COUNT, UNDEFINED);
//Synthetic comment -- @@ -381,17 +413,17 @@
}

// Assign row/column positions to all cells that do not explicitly define them
        assignRowsAndColumns(
                declaredRowCount == UNDEFINED ? children.length : declaredRowCount,
                declaredColumnCount == UNDEFINED ? children.length : declaredColumnCount);

assignCellBounds();

for (int i = 0; i <= actualRowCount; i++) {
mBaselines[i] = UNDEFINED;
}

        stale = false;
}

private Pair<Map<Integer, Integer>, Map<Integer, Integer>> findCellsOutsideDeclaredBounds() {
//Synthetic comment -- @@ -450,7 +482,7 @@
* TODO: Consolidate with the algorithm in GridLayout to ensure we get the
* exact same results!
*/
    private void assignRowsAndColumns(int rowCount, int columnCount) {
Pair<Map<Integer, Integer>, Map<Integer, Integer>> p = findCellsOutsideDeclaredBounds();
Map<Integer, Integer> extraRowsMap = p.getFirst();
Map<Integer, Integer> extraColumnsMap = p.getSecond();
//Synthetic comment -- @@ -553,6 +585,80 @@
}
}

/**
* Computes the positions of the column and row boundaries
*/
//Synthetic comment -- @@ -805,10 +911,8 @@
*/
public INode addColumn(int newColumn, INode newView, int columnWidthDp,
boolean split, int row, int x) {
        assert !stale;
        stale = true;

// Insert a new column
if (declaredColumnCount != UNDEFINED) {
declaredColumnCount++;
setGridAttribute(layout, ATTR_COLUMN_COUNT, declaredColumnCount);
//Synthetic comment -- @@ -838,11 +942,14 @@
index++;
}

                        newView = addSpacer(layout, index,
split ? row : UNDEFINED,
split ? newColumn - 1 : UNDEFINED,
columnWidthDp != UNDEFINED ? columnWidthDp : DEFAULT_CELL_WIDTH,
DEFAULT_CELL_HEIGHT);
}

// Set the actual row number on the first cell on the new row.
//Synthetic comment -- @@ -850,19 +957,23 @@
// the new row number, but we use the spacer to assign the row
// some height.
if (view.column == newColumn) {
                        setGridAttribute(view.node, ATTR_LAYOUT_COLUMN, view.column + 1);
} // else: endColumn == newColumn: handled below
} else if (getGridAttribute(view.node, ATTR_LAYOUT_COLUMN) != null) {
                    setGridAttribute(view.node, ATTR_LAYOUT_COLUMN, view.column + 1);
}
} else if (endColumn > newColumn) {
                setColumnSpanAttribute(view.node, view.columnSpan + 1);
columnSpanSet = true;
}

if (split && !columnSpanSet && view.node.getBounds().x2() > x) {
if (view.node.getBounds().x < x) {
                    setColumnSpanAttribute(view.node, view.columnSpan + 1);
}
}
}
//Synthetic comment -- @@ -896,18 +1007,17 @@
return;
}

        assert !stale;
        stale = true;

// Figure out which columns should be removed
        Set<Integer> removedSet = new HashSet<Integer>();
for (INode child : selectedChildren) {
ViewData view = getView(child);
            removedSet.add(view.column);
}
// Sort them in descending order such that we can process each
// deletion independently
        List<Integer> removed = new ArrayList<Integer>(removedSet);
Collections.sort(removed, Collections.reverseOrder());

for (int removedColumn : removed) {
//Synthetic comment -- @@ -916,9 +1026,9 @@
// TODO: Don't do this if the column being deleted is outside
// the declared column range!
// TODO: Do this under a write lock? / editXml lock?
if (declaredColumnCount != UNDEFINED) {
declaredColumnCount--;
                setGridAttribute(layout, ATTR_COLUMN_COUNT, declaredColumnCount);
}

// Remove any elements that begin in the deleted columns...
//Synthetic comment -- @@ -935,21 +1045,44 @@
int columnWidth = getColumnWidth(removedColumn, view.columnSpan) -
getColumnWidth(removedColumn, 1);
int columnWidthDip = mRulesEngine.pxToDp(columnWidth);
                        addSpacer(layout, index, UNDEFINED, UNDEFINED, columnWidthDip,
                                SPACER_SIZE_DP);
}
layout.removeChild(view.node);
} else if (view.column < removedColumn
&& view.column + view.columnSpan > removedColumn) {
// Subtract column span to skip this item
                    setColumnSpanAttribute(view.node, view.columnSpan - 1);
} else if (view.column > removedColumn) {
if (getGridAttribute(view.node, ATTR_LAYOUT_COLUMN) != null) {
                        setGridAttribute(view.node, ATTR_LAYOUT_COLUMN, view.column - 1);
}
}
}
}
}

/**
//Synthetic comment -- @@ -991,14 +1124,12 @@
*/
public INode addRow(int newRow, INode newView, int rowHeightDp, boolean split,
int column, int y) {
        // We'll modify the grid data; the cached data is out of date
        assert !stale;
        stale = true;

if (declaredRowCount != UNDEFINED) {
declaredRowCount++;
setGridAttribute(layout, ATTR_ROW_COUNT, declaredRowCount);
}
boolean added = false;
for (ViewData view : mChildViews) {
if (view.row >= newRow) {
//Synthetic comment -- @@ -1011,30 +1142,37 @@
if (declaredColumnCount != UNDEFINED && !split) {
setGridAttribute(layout, ATTR_COLUMN_COUNT, declaredColumnCount);
}
                        newView = addSpacer(layout, index,
split ? newRow - 1 : UNDEFINED,
split ? column : UNDEFINED,
SPACER_SIZE_DP,
rowHeightDp != UNDEFINED ? rowHeightDp : DEFAULT_CELL_HEIGHT);
}

// Set the actual row number on the first cell on the new row.
// This means we don't really need the spacer above to imply
// the new row number, but we use the spacer to assign the row
// some height.
                    setGridAttribute(view.node, ATTR_LAYOUT_ROW, view.row + 1);

added = true;
} else if (getGridAttribute(view.node, ATTR_LAYOUT_ROW) != null) {
                    setGridAttribute(view.node, ATTR_LAYOUT_ROW, view.row + 1);
}
} else {
int endRow = view.row + view.rowSpan;
if (endRow > newRow) {
                    setRowSpanAttribute(view.node, view.rowSpan + 1);
} else if (split && view.node.getBounds().y2() > y) {
if (view.node.getBounds().y < y) {
                        setRowSpanAttribute(view.node, view.rowSpan + 1);
}
}
}
//Synthetic comment -- @@ -1043,9 +1181,13 @@
if (!added) {
// Append a row at the end
if (newView == null) {
                newView = addSpacer(layout, -1, UNDEFINED, UNDEFINED,
SPACER_SIZE_DP,
rowHeightDp != UNDEFINED ? rowHeightDp : DEFAULT_CELL_HEIGHT);
}
if (declaredColumnCount != UNDEFINED && !split) {
setGridAttribute(layout, ATTR_COLUMN_COUNT, declaredColumnCount);
//Synthetic comment -- @@ -1053,7 +1195,6 @@
if (split) {
setGridAttribute(newView, ATTR_LAYOUT_ROW, newRow - 1);
setGridAttribute(newView, ATTR_LAYOUT_COLUMN, column);

}
}

//Synthetic comment -- @@ -1070,18 +1211,17 @@
return;
}

        assert !stale;
        stale = true;

// Figure out which rows should be removed
        Set<Integer> removedSet = new HashSet<Integer>();
for (INode child : selectedChildren) {
ViewData view = getView(child);
            removedSet.add(view.row);
}
// Sort them in descending order such that we can process each
// deletion independently
        List<Integer> removed = new ArrayList<Integer>(removedSet);
Collections.sort(removed, Collections.reverseOrder());

for (int removedRow : removed) {
//Synthetic comment -- @@ -1089,6 +1229,7 @@
// First, adjust row count.
// TODO: Don't do this if the row being deleted is outside
// the declared row range!
if (declaredRowCount != UNDEFINED) {
declaredRowCount--;
setGridAttribute(layout, ATTR_ROW_COUNT, declaredRowCount);
//Synthetic comment -- @@ -1104,18 +1245,35 @@
// We don't have to worry about a rowSpan > 1 here, because even
// if it is, those rowspans are not used to assign default row/column
// positions for other cells
layout.removeChild(view.node);
} else if (view.row > removedRow) {
if (getGridAttribute(view.node, ATTR_LAYOUT_ROW) != null) {
                        setGridAttribute(view.node, ATTR_LAYOUT_ROW, view.row - 1);
}
} else if (view.row < removedRow
&& view.row + view.rowSpan > removedRow) {
// Subtract row span to skip this item
                    setRowSpanAttribute(view.node, view.rowSpan - 1);
}
}
}
}

/**
//Synthetic comment -- @@ -1364,10 +1522,6 @@
*/
@Override
public String toString() {
        if (stale) {
            System.out.println("WARNING: Grid has been modified, so model may be out of date!");
        }

// Dump out the view table
int cellWidth = 25;

//Synthetic comment -- @@ -1380,9 +1534,6 @@
rowList.add(columnList);
}
for (ViewData view : mChildViews) {
            if (mDeleted != null && mDeleted.contains(view.node)) {
                continue;
            }
for (int i = 0; i < view.rowSpan; i++) {
if (view.row + i > mTop.length) { // Guard against bogus span values
break;
//Synthetic comment -- @@ -1456,8 +1607,7 @@
* @param x the x coordinate of the new column
*/
public void splitColumn(int newColumn, boolean insertMarginColumn, int columnWidthDp, int x) {
        assert !stale;
        stale = true;

// Insert a new column
if (declaredColumnCount != UNDEFINED) {
//Synthetic comment -- @@ -1525,14 +1675,24 @@
//   skipped column!

//if (getGridAttribute(node, ATTR_LAYOUT_COLUMN) != null) {
                setGridAttribute(node, ATTR_LAYOUT_COLUMN, column + (insertMarginColumn ? 2 : 1));
//}
} else if (!view.isSpacer()) {
int endColumn = column + view.columnSpan;
if (endColumn > newColumn
                        || endColumn == newColumn && view.node.getBounds().x2() > x) {
// This cell spans the new insert position, so increment the column span
                    setColumnSpanAttribute(node, view.columnSpan + (insertMarginColumn ? 2 : 1));
}
}
}
//Synthetic comment -- @@ -1548,8 +1708,9 @@
if (remaining > 0) {
prevColumnSpacer.node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH,
String.format(VALUE_N_DP, remaining));
setGridAttribute(prevColumnSpacer.node, ATTR_LAYOUT_COLUMN,
                        insertMarginColumn ? newColumn + 1 : newColumn);
}
}

//Synthetic comment -- @@ -1575,6 +1736,8 @@
* @param y the y coordinate of the new row
*/
public void splitRow(int newRow, boolean insertMarginRow, int rowHeightDp, int y) {
// Insert a new row
if (declaredRowCount != UNDEFINED) {
declaredRowCount++;
//Synthetic comment -- @@ -1603,14 +1766,17 @@
int row = view.row;
if (row > newRow || (row == newRow && view.node.getBounds().y2() > y)) {
//if (getGridAttribute(node, ATTR_LAYOUT_ROW) != null) {
                setGridAttribute(node, ATTR_LAYOUT_ROW, row + (insertMarginRow ? 2 : 1));
//}
} else if (!view.isSpacer()) {
int endRow = row + view.rowSpan;
if (endRow > newRow
                        || endRow == newRow && view.node.getBounds().y2() > y) {
// This cell spans the new insert position, so increment the row span
                    setRowSpanAttribute(node, view.rowSpan + (insertMarginRow ? 2 : 1));
}
}
}
//Synthetic comment -- @@ -1626,8 +1792,8 @@
if (remaining > 0) {
prevRowSpacer.node.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT,
String.format(VALUE_N_DP, remaining));
                setGridAttribute(prevRowSpacer.node, ATTR_LAYOUT_ROW,
                        insertMarginRow ? newRow + 1 : newRow);
}
}

//Synthetic comment -- @@ -1664,12 +1830,8 @@

/** Applies the column and row fields into the XML model */
void applyPositionAttributes() {
            if (getGridAttribute(node, ATTR_LAYOUT_COLUMN) == null) {
                setGridAttribute(node, ATTR_LAYOUT_COLUMN, column);
            }
            if (getGridAttribute(node, ATTR_LAYOUT_ROW) == null) {
                setGridAttribute(node, ATTR_LAYOUT_ROW, row);
            }
}

/** Returns the id of this node, or makes one up for display purposes */
//Synthetic comment -- @@ -1688,8 +1850,7 @@

/** Returns true if this {@link ViewData} represents a spacer */
boolean isSpacer() {
            String fqcn = node.getFqcn();
            return FQCN_SPACE.equals(fqcn) || FQCN_SPACE_V7.equals(fqcn);
}

/**
//Synthetic comment -- @@ -1755,46 +1916,52 @@
}

/**
     * Notify the grid that the given node is about to be deleted. This can be used in
     * conjunction with {@link #cleanup()} to remove and merge unnecessary rows and
     * columns.
*
     * @param child the child that is going to be removed shortly
*/
    public void markDeleted(INode child) {
        if (mDeleted == null) {
            mDeleted = new HashSet<INode>();
        }

        mDeleted.add(child);
    }

    /**
     * Clean up rows and columns that are no longer needed after the nodes marked for
     * deletion by {@link #markDeleted(INode)} are removed.
     */
    public void cleanup() {
        if (mDeleted == null) {
return;
}

Set<Integer> usedColumns = new HashSet<Integer>(actualColumnCount);
        Set<Integer> usedRows = new HashSet<Integer>(actualColumnCount);
        Map<Integer, ViewData> columnSpacers = new HashMap<Integer, ViewData>(actualColumnCount);
        Map<Integer, ViewData> rowSpacers = new HashMap<Integer, ViewData>(actualColumnCount);

for (ViewData view : mChildViews) {
            if (view.isColumnSpacer()) {
columnSpacers.put(view.column, view);
} else if (view.isRowSpacer()) {
rowSpacers.put(view.row, view);
            } else if (!mDeleted.contains(view.node)) {
usedColumns.add(Integer.valueOf(view.column));
usedRows.add(Integer.valueOf(view.row));
}
}

        if (usedColumns.size() == 0) {
// No more views - just remove all the spacers
for (ViewData spacer : columnSpacers.values()) {
layout.removeChild(spacer.node);
//Synthetic comment -- @@ -1802,158 +1969,263 @@
for (ViewData spacer : rowSpacers.values()) {
layout.removeChild(spacer.node);
}
setGridAttribute(layout, ATTR_COLUMN_COUNT, 2);

return;
}

        // Remove (merge back) unnecessary columns
        for (int column = actualColumnCount - 1; column >= 0; column--) {
            if (!usedColumns.contains(column)) {
                // This column is no longer needed. Remove it!
                ViewData spacer = columnSpacers.get(column);
                ViewData prevSpacer = columnSpacers.get(column - 1);
                if (spacer == null) {
                    // Can't touch this column; we only merge spacer columns, not
                    // other types of columns (TODO: Consider what we can do here!)

                    // Try to merge with next column
                    ViewData nextSpacer = columnSpacers.get(column + 1);
                    if (nextSpacer != null) {
                        int nextSizeDp = getDipSize(nextSpacer, false /* row */);
                        int columnWidthPx = getColumnWidth(column, 1);
                        int columnWidthDp = mRulesEngine.pxToDp(columnWidthPx);
                        int combinedSizeDp = nextSizeDp + columnWidthDp;
                        nextSpacer.node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH,
                                String.format(VALUE_N_DP, combinedSizeDp));
                        // Also move the spacer into this column
                        setGridAttribute(nextSpacer.node, ATTR_LAYOUT_COLUMN, column);
                        columnSpacers.put(column, nextSpacer);
                    } else {
                        continue;
                    }
                } else if (prevSpacer == null) {
                    // Can't combine this column with a previous column; we don't have
                    // data for it.
                    continue;
}

                if (spacer != null) {
                    // Combine spacer and prevSpacer.
                    mergeSpacers(prevSpacer, spacer, false /*row*/);
                }

                // Decrement column numbers for all elements to the right of the deleted column,
                // and subtract columnSpans for any elements that overlap it
                for (ViewData view : mChildViews) {
                    if (view.column >= column) {
                        if (view.column > 0) {
                            view.column--;
                            setGridAttribute(view.node, ATTR_LAYOUT_COLUMN, view.column);
                        }
                    } else if (!view.isSpacer()) {
                        int endColumn = view.column + view.columnSpan;
                        if (endColumn > column && view.columnSpan > 1) {
                            view.columnSpan--;
                            setColumnSpanAttribute(view.node, view.columnSpan);
}
}
}
}
}

        for (int row = actualRowCount - 1; row >= 0; row--) {
            if (!usedRows.contains(row)) {
                // This row is no longer needed. Remove it!
                ViewData spacer = rowSpacers.get(row);
                ViewData prevSpacer = rowSpacers.get(row - 1);
                if (spacer == null) {
                    ViewData nextSpacer = rowSpacers.get(row + 1);
                    if (nextSpacer != null) {
                        int nextSizeDp = getDipSize(nextSpacer, true /* row */);
                        int rowHeightPx = getRowHeight(row, 1);
                        int rowHeightDp = mRulesEngine.pxToDp(rowHeightPx);
                        int combinedSizeDp = nextSizeDp + rowHeightDp;
                        nextSpacer.node.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT,
                                String.format(VALUE_N_DP, combinedSizeDp));
                        setGridAttribute(nextSpacer.node, ATTR_LAYOUT_ROW, row);
                        rowSpacers.put(row, nextSpacer);
                    } else {
                        continue;
                    }
                } else if (prevSpacer == null) {
                    continue;
}

                if (spacer != null) {
                    // Combine spacer and prevSpacer.
                    mergeSpacers(prevSpacer, spacer, true /*row*/);
                }


                // Decrement row numbers for all elements below the deleted row,
                // and subtract rowSpans for any elements that overlap it
                for (ViewData view : mChildViews) {
                    if (view.row >= row) {
                        if (view.row > 0) {
                            view.row--;
                            setGridAttribute(view.node, ATTR_LAYOUT_ROW, view.row);
                        }
                    } else if (!view.isSpacer()) {
                        int endRow = view.row + view.rowSpan;
                        if (endRow > row && view.rowSpan > 1) {
                            view.rowSpan--;
                            setRowSpanAttribute(view.node, view.rowSpan);
}
}
}
}
}

        // TODO: Reduce row/column counts!
    }

    /**
     * Merges two spacers together - either row spacers or column spacers based on the
     * parameter
     */
    private void mergeSpacers(ViewData prevSpacer, ViewData spacer, boolean row) {
        int combinedSizeDp = -1;
        int prevSizeDp = getDipSize(prevSpacer, row);
        int sizeDp = getDipSize(spacer, row);
        combinedSizeDp = prevSizeDp + sizeDp;
        String attribute = row ? ATTR_LAYOUT_HEIGHT : ATTR_LAYOUT_WIDTH;
        prevSpacer.node.setAttribute(ANDROID_URI, attribute,
                String.format(VALUE_N_DP, combinedSizeDp));
        layout.removeChild(spacer.node);
    }

    /**
     * Computes the size (in device independent pixels) of the given spacer.
     *
     * @param spacer the spacer to measure
     * @param row if true, this is a row spacer, otherwise it is a column spacer
     * @return the size in device independent pixels
     */
    private int getDipSize(ViewData spacer, boolean row) {
        String attribute = row ? ATTR_LAYOUT_HEIGHT : ATTR_LAYOUT_WIDTH;
        String size = spacer.node.getStringAttr(ANDROID_URI, attribute);
        if (size != null) {
            Matcher matcher = DIP_PATTERN.matcher(size);
            if (matcher.matches()) {
                try {
                    return Integer.parseInt(matcher.group(1));
                } catch (NumberFormatException nfe) {
                    // Can't happen; we pre-check with regexp above.
}
}
}

        // Fallback for cases where the attribute values are not regular (e.g. user has edited
        // to some resource or other dimension format) - in that case just do bounds-based
        // computation.
        Rect bounds = spacer.node.getBounds();
        return mRulesEngine.pxToDp(row ? bounds.h : bounds.w);
}

/**
//Synthetic comment -- @@ -1968,7 +2240,7 @@
* @param heightDp the height in device independent pixels to assign to the spacer
* @return the newly added spacer
*/
    INode addSpacer(INode parent, int index, int row, int column,
int widthDp, int heightDp) {
INode spacer;

//Synthetic comment -- @@ -1984,10 +2256,15 @@
spacer = parent.appendChild(tag);
}

if (row != UNDEFINED) {
setGridAttribute(spacer, ATTR_LAYOUT_ROW, row);
}
if (column != UNDEFINED) {
setGridAttribute(spacer, ATTR_LAYOUT_COLUMN, column);
}
if (widthDp > 0) {
//Synthetic comment -- @@ -2020,7 +2297,7 @@
}


        return spacer;
}

/**
//Synthetic comment -- @@ -2068,4 +2345,14 @@
public int getViewCount() {
return mChildViews.size();
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java
//Synthetic comment -- index 47159c3..bef070b 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_COLUMN_COUNT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_COLUMN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_COLUMN_SPAN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ROW;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ROW_SPAN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ROW_COUNT;
//Synthetic comment -- @@ -30,6 +31,7 @@
import static com.android.tools.lint.detector.api.LintConstants.AUTO_URI;
import static com.android.tools.lint.detector.api.LintConstants.URI_PREFIX;
import static com.android.util.XmlUtils.ANDROID_URI;
import static org.eclipse.jface.viewers.StyledString.QUALIFIER_STYLER;

import com.android.annotations.VisibleForTesting;
//Synthetic comment -- @@ -800,11 +802,14 @@

// Temporary diagnostics code when developing GridLayout
if (GridLayoutRule.sDebugGridLayout) {
String namespace;
                        if (e.getParentNode() != null
&& e.getParentNode().getNodeName().equals(GRID_LAYOUT)) {
namespace = ANDROID_URI;
} else {
IProject project = mGraphicalEditorPart.getProject();
ProjectState projectState = Sdk.getProjectState(project);
if (projectState != null && projectState.isLibrary()) {
//Synthetic comment -- @@ -872,6 +877,13 @@
styledString.append(',', QUALIFIER_STYLER);
styledString.append(rowSpan, QUALIFIER_STYLER);
styledString.append(')', QUALIFIER_STYLER);
}
}

//Synthetic comment -- @@ -886,10 +898,14 @@
text = resolved;
}
}
                            styledString.append(LABEL_SEPARATOR, QUALIFIER_STYLER);
                            styledString.append('"', QUALIFIER_STYLER);
                            styledString.append(truncate(text, styledString), QUALIFIER_STYLER);
                            styledString.append('"', QUALIFIER_STYLER);
}
} else if (e.hasAttributeNS(ANDROID_URI, ATTR_SRC)) {
// Show ImageView source attributes etc








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java
//Synthetic comment -- index 23e42ef..424be26 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_MERGE;

import com.android.annotations.NonNull;
//Synthetic comment -- @@ -31,6 +32,7 @@

import org.eclipse.swt.graphics.Rectangle;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import java.awt.image.BufferedImage;
//Synthetic comment -- @@ -446,7 +448,7 @@
} else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
return mDomNodeToView.get(((Attr) node).getOwnerElement());
} else if (node.getNodeType() == Node.DOCUMENT_NODE) {
                return mDomNodeToView.get(node.getOwnerDocument().getDocumentElement());
}
}

//Synthetic comment -- @@ -600,7 +602,11 @@
* @return A {@link CanvasViewInfo} matching the given key, or null if not
*         found.
*/
    public CanvasViewInfo findViewInfoFor(NodeProxy proxy) {
return mNodeToView.get(proxy.getNode());
}

//Synthetic comment -- @@ -710,6 +716,7 @@
/**
* Dumps a {@link ViewInfo} hierarchy to stdout
*
* @param info the {@link ViewInfo} object to dump
* @param depth the depth to indent it to
*/
//Synthetic comment -- @@ -736,6 +743,12 @@
sb.append("<"); //$NON-NLS-1$
sb.append(node.getDescriptor().getXmlName());
sb.append(">"); //$NON-NLS-1$
} else if (cookie != null) {
sb.append(" " + cookie); //$NON-NLS-1$
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ClientRulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ClientRulesEngine.java
//Synthetic comment -- index 8337007..60f8365 100644

//Synthetic comment -- @@ -41,10 +41,12 @@
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorDelegate;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutCanvas;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.RenderService;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.SelectionManager;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
//Synthetic comment -- @@ -180,6 +182,18 @@
}

@Override
public @NonNull IViewMetadata getMetadata(final @NonNull String fqcn) {
return new IViewMetadata() {
@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/GravityHelperTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/GravityHelperTest.java
//Synthetic comment -- index c05cdb5..f162924 100644

//Synthetic comment -- @@ -18,7 +18,12 @@
import static com.android.ide.common.layout.GravityHelper.GRAVITY_BOTTOM;
import static com.android.ide.common.layout.GravityHelper.GRAVITY_CENTER_HORIZ;
import static com.android.ide.common.layout.GravityHelper.GRAVITY_CENTER_VERT;
import static com.android.ide.common.layout.GravityHelper.GRAVITY_LEFT;
import junit.framework.TestCase;

@SuppressWarnings("javadoc")
//Synthetic comment -- @@ -29,4 +34,52 @@
assertEquals(GRAVITY_CENTER_HORIZ | GRAVITY_CENTER_VERT,
GravityHelper.getGravity("center", 0));
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LayoutTestBase.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LayoutTestBase.java
//Synthetic comment -- index 826f36c..b59144a 100644

//Synthetic comment -- @@ -192,10 +192,10 @@
rule.onInitialize(fqn, new TestRulesEngine(fqn));
}

    private static class TestRulesEngine implements IClientRulesEngine {
private final String mFqn;

        protected TestRulesEngine(String fqn) {
mFqn = fqn;
}

//Synthetic comment -- @@ -320,8 +320,14 @@

@Override
public int pxToDp(int px) {
            fail("Not supported in tests yet");
            return px;
}

@Override
//Synthetic comment -- @@ -333,13 +339,7 @@
@Override
public int screenToLayout(int pixels) {
fail("Not supported in tests yet");
            return 0;
        }

        @Override
        public int dpToPx(int dp) {
            fail("Not supported in tests yet");
            return 0;
}

@Override
//Synthetic comment -- @@ -347,6 +347,12 @@
fail("Not supported in tests yet");
return null;
}
}

public void testDummy() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestNode.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestNode.java
//Synthetic comment -- index 8984f38..b9176f6 100644

//Synthetic comment -- @@ -15,8 +15,13 @@
*/
package com.android.ide.common.layout;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -25,14 +30,30 @@
import com.android.ide.common.api.INodeHandler;
import com.android.ide.common.api.Margins;
import com.android.ide.common.api.Rect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Test/mock implementation of {@link INode} */
public class TestNode implements INode {
private TestNode mParent;

//Synthetic comment -- @@ -193,8 +214,9 @@

@Override
public String toString() {
        return "TestNode [fqn=" + mFqcn + ", infos=" + mAttributeInfos
                + ", attributes=" + mAttributes + ", bounds=" + mBounds + "]";
}

@Override
//Synthetic comment -- @@ -215,4 +237,197 @@
public void setAttributeSources(List<String> attributeSources) {
mAttributeSources = attributeSources;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/grid/GridModelTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/grid/GridModelTest.java
//Synthetic comment -- index 680b7ca..f3405c1 100644

//Synthetic comment -- @@ -15,15 +15,24 @@
*/
package com.android.ide.common.layout.grid;

import static com.android.util.XmlUtils.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.ATTR_COLUMN_COUNT;
import static com.android.ide.common.layout.LayoutConstants.FQCN_BUTTON;

import com.android.ide.common.api.Rect;
import com.android.ide.common.layout.LayoutTestBase;
import com.android.ide.common.layout.TestNode;


public class GridModelTest extends LayoutTestBase {
public void testRemoveFlag() {
assertEquals("left", GridModel.removeFlag("top", "top|left"));
//Synthetic comment -- @@ -38,7 +47,7 @@
TestNode targetNode = TestNode.create("android.widget.GridLayout").id("@+id/GridLayout1")
.bounds(new Rect(0, 0, 240, 480)).set(ANDROID_URI, ATTR_COLUMN_COUNT, "3");

        GridModel model = new GridModel(null, targetNode, null);
assertEquals(3, model.declaredColumnCount);
assertEquals(1, model.actualColumnCount);
assertEquals(1, model.actualRowCount);
//Synthetic comment -- @@ -48,9 +57,796 @@
targetNode.add(TestNode.create(FQCN_BUTTON).id("@+id/Button3"));
targetNode.add(TestNode.create(FQCN_BUTTON).id("@+id/Button4"));

        model = new GridModel(null, targetNode, null);
assertEquals(3, model.declaredColumnCount);
assertEquals(3, model.actualColumnCount);
assertEquals(2, model.actualRowCount);
}
}








//Synthetic comment -- diff --git a/rule_api/src/com/android/ide/common/api/IClientRulesEngine.java b/rule_api/src/com/android/ide/common/api/IClientRulesEngine.java
//Synthetic comment -- index 329f38c..08630c7 100644

//Synthetic comment -- @@ -44,6 +44,15 @@
String getFqcn();

/**
* Prints a debug line in the Eclipse console using the ADT formatter.
*
* @param msg A String format message.







