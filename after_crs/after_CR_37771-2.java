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

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_GRAVITY;
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_BOTTOM;
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_CENTER;
//Synthetic comment -- @@ -27,21 +26,41 @@
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_LEFT;
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_TOP;
import static com.android.util.XmlUtils.ANDROID_URI;

import org.w3c.dom.Element;

/** Helper class for looking up the gravity masks of gravity attributes */
public class GravityHelper {
    /** Bitmask for a gravity which includes left */
public static final int GRAVITY_LEFT         = 1 << 0;

    /** Bitmask for a gravity which includes right */
public static final int GRAVITY_RIGHT        = 1 << 1;

    /** Bitmask for a gravity which includes center horizontal */
public static final int GRAVITY_CENTER_HORIZ = 1 << 2;

    /** Bitmask for a gravity which includes fill horizontal */
public static final int GRAVITY_FILL_HORIZ   = 1 << 3;

    /** Bitmask for a gravity which includes center vertical */
public static final int GRAVITY_CENTER_VERT  = 1 << 4;

    /** Bitmask for a gravity which includes fill vertical */
public static final int GRAVITY_FILL_VERT    = 1 << 5;

    /** Bitmask for a gravity which includes top */
public static final int GRAVITY_TOP          = 1 << 6;

    /** Bitmask for a gravity which includes bottom */
public static final int GRAVITY_BOTTOM       = 1 << 7;

    /** Bitmask for a gravity which includes any horizontal constraint */
public static final int GRAVITY_HORIZ_MASK = GRAVITY_CENTER_HORIZ | GRAVITY_FILL_HORIZ
| GRAVITY_LEFT | GRAVITY_RIGHT;

    /** Bitmask for a gravity which any vertical constraint */
public static final int GRAVITY_VERT_MASK = GRAVITY_CENTER_VERT | GRAVITY_FILL_VERT
| GRAVITY_TOP | GRAVITY_BOTTOM;

//Synthetic comment -- @@ -96,4 +115,90 @@

return gravity;
}

    /**
     * Returns true if the given gravity bitmask is constrained horizontally
     *
     * @param gravity the gravity bitmask
     * @return true if the given gravity bitmask is constrained horizontally
     */
    public static boolean isConstrainedHorizontally(int gravity) {
        return (gravity & GRAVITY_HORIZ_MASK) != 0;
    }

    /**
     * Returns true if the given gravity bitmask is constrained vertically
     *
     * @param gravity the gravity bitmask
     * @return true if the given gravity bitmask is constrained vertically
     */
    public static boolean isConstrainedVertically(int gravity) {
        return (gravity & GRAVITY_VERT_MASK) != 0;
    }

    /**
     * Returns true if the given gravity bitmask is left aligned
     *
     * @param gravity the gravity bitmask
     * @return true if the given gravity bitmask is left aligned
     */
    public static boolean isLeftAligned(int gravity) {
        return (gravity & GRAVITY_LEFT) != 0;
    }

    /**
     * Returns true if the given gravity bitmask is top aligned
     *
     * @param gravity the gravity bitmask
     * @return true if the given gravity bitmask is aligned
     */
    public static boolean isTopAligned(int gravity) {
        return (gravity & GRAVITY_TOP) != 0;
    }

    /** Returns a gravity value string from the given gravity bitmask
     *
     * @param gravity the gravity bitmask
     * @return the corresponding gravity string suitable as an XML attribute value
     */
    public static String getGravity(int gravity) {
        if (gravity == 0) {
            return "";
        }

        if ((gravity & (GRAVITY_CENTER_HORIZ | GRAVITY_CENTER_VERT)) ==
                (GRAVITY_CENTER_HORIZ | GRAVITY_CENTER_VERT)) {
            return GRAVITY_VALUE_CENTER;
        }

        StringBuilder sb = new StringBuilder(30);
        int horizontal = gravity & GRAVITY_HORIZ_MASK;
        int vertical = gravity & GRAVITY_VERT_MASK;

        if ((horizontal & GRAVITY_LEFT) != 0) {
            sb.append(GRAVITY_VALUE_LEFT);
        } else if ((horizontal & GRAVITY_RIGHT) != 0) {
            sb.append(GRAVITY_VALUE_RIGHT);
        } else if ((horizontal & GRAVITY_CENTER_HORIZ) != 0) {
            sb.append(GRAVITY_VALUE_CENTER_HORIZONTAL);
        } else if ((horizontal & GRAVITY_FILL_HORIZ) != 0) {
            sb.append(GRAVITY_VALUE_FILL_HORIZONTAL);
        }

        if (sb.length() > 0 && vertical != 0) {
            sb.append('|');
        }

        if ((vertical & GRAVITY_TOP) != 0) {
            sb.append(GRAVITY_VALUE_TOP);
        } else if ((vertical & GRAVITY_BOTTOM) != 0) {
            sb.append(GRAVITY_VALUE_BOTTOM);
        } else if ((vertical & GRAVITY_CENTER_VERT) != 0) {
            sb.append(GRAVITY_VALUE_CENTER_VERTICAL);
        } else if ((vertical & GRAVITY_FILL_VERT) != 0) {
            sb.append(GRAVITY_VALUE_FILL_VERTICAL);
        }

        return sb.toString();
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GridLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GridLayoutRule.java
//Synthetic comment -- index a737251..787a2c2 100644

//Synthetic comment -- @@ -205,7 +205,7 @@
return;
}

                        GridModel grid = GridModel.get(mRulesEngine, parentNode, null);
if (id.equals(ACTION_ADD_ROW)) {
grid.addRow(children);
} else if (id.equals(ACTION_REMOVE_ROW)) {
//Synthetic comment -- @@ -285,6 +285,9 @@
@Override
public DropFeedback onDropMove(@NonNull INode targetNode, @NonNull IDragElement[] elements,
@Nullable DropFeedback feedback, @NonNull Point p) {
        if (feedback == null) {
            return null;
        }
feedback.requestPaint = true;

GridDropHandler handler = (GridDropHandler) feedback.userData;
//Synthetic comment -- @@ -296,6 +299,10 @@
@Override
public void onDropped(final @NonNull INode targetNode, final @NonNull IDragElement[] elements,
@Nullable DropFeedback feedback, @NonNull Point p) {
        if (feedback == null) {
            return;
        }

Rect b = targetNode.getBounds();
if (!b.isValid()) {
return;
//Synthetic comment -- @@ -334,13 +341,14 @@
return;
}

        if (GridModel.isSpace(node.getFqcn())) {
            return;
        }

// Attempt to set "fill" properties on newly added views such that for example
// a text field will stretch horizontally.
String fqcn = node.getFqcn();
IViewMetadata metadata = mRulesEngine.getMetadata(fqcn);
FillPreference fill = metadata.getFillPreference();
String gravity = computeDefaultGravity(fill);
if (gravity != null) {
//Synthetic comment -- @@ -400,17 +408,8 @@

// Attempt to clean up spacer objects for any newly-empty rows or columns
// as the result of this deletion
        GridModel grid = GridModel.get(mRulesEngine, parent, null);
        grid.onDeleted(deleted);
}

@Override
//Synthetic comment -- @@ -442,7 +441,7 @@
private GridModel getGrid(ResizeState resizeState) {
GridModel grid = (GridModel) resizeState.clientData;
if (grid == null) {
            grid = GridModel.get(mRulesEngine, resizeState.layout, resizeState.layoutView);
resizeState.clientData = grid;
}

//Synthetic comment -- @@ -543,10 +542,10 @@
}
}
GridLayoutPainter.paintStructure(DrawingStyle.GUIDELINE_DASHED,
                        parentNode, graphics, GridModel.get(mRulesEngine, parentNode, view));
} else if (sDebugGridLayout) {
GridLayoutPainter.paintStructure(DrawingStyle.GRID,
                    parentNode, graphics, GridModel.get(mRulesEngine, parentNode, view));
}

// TBD: Highlight the cells around the selection, and display easy controls








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridDropHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridDropHandler.java
//Synthetic comment -- index 74c1b59..6d938fb 100644

//Synthetic comment -- @@ -15,6 +15,7 @@
*/
package com.android.ide.common.layout.grid;

import static com.android.ide.common.layout.GravityHelper.getGravity;
import static com.android.ide.common.layout.GridLayoutRule.GRID_SIZE;
import static com.android.ide.common.layout.GridLayoutRule.MARGIN_SIZE;
import static com.android.ide.common.layout.GridLayoutRule.MAX_CELL_DIFFERENCE;
//Synthetic comment -- @@ -25,9 +26,6 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_GRAVITY;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ROW;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ROW_SPAN;
import static com.android.ide.common.layout.grid.GridModel.UNDEFINED;
import static java.lang.Math.abs;

//Synthetic comment -- @@ -40,6 +38,7 @@
import com.android.ide.common.api.Rect;
import com.android.ide.common.api.SegmentType;
import com.android.ide.common.layout.BaseLayoutRule;
import com.android.ide.common.layout.GravityHelper;
import com.android.ide.common.layout.GridLayoutRule;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.ViewMetadataRepository;

//Synthetic comment -- @@ -66,7 +65,7 @@
*/
public GridDropHandler(GridLayoutRule gridLayoutRule, INode layout, Object view) {
mRule = gridLayoutRule;
        mGrid = GridModel.get(mRule.getRulesEngine(), layout, view);
}

/**
//Synthetic comment -- @@ -522,9 +521,7 @@
int rowSpan = endRow - row + 1;

// Make sure my math was right:
        assert mRowMatch.type != SegmentType.BASELINE || rowSpan == 1 : rowSpan;

// If the item almost fits into the row (at most N % bigger) then just enlarge
// the row; don't add a rowspan since that will defeat baseline alignment etc
//Synthetic comment -- @@ -588,9 +585,6 @@
if (insertMarginColumn) {
column++;
}
}

// Split cells to make a new  row
//Synthetic comment -- @@ -628,7 +622,6 @@
if (insertMarginRow) {
row++;
}
}

// Figure out where to insert the new child
//Synthetic comment -- @@ -649,22 +642,33 @@
next.applyPositionAttributes();
}

        // Set the cell position (gravity) of the new widget
        int gravity = 0;
if (mColumnMatch.type == SegmentType.RIGHT) {
            gravity |= GravityHelper.GRAVITY_RIGHT;
} else if (mColumnMatch.type == SegmentType.CENTER_HORIZONTAL) {
            gravity |= GravityHelper.GRAVITY_CENTER_HORIZ;
}
mGrid.setGridAttribute(newChild, ATTR_LAYOUT_COLUMN, column);
if (mRowMatch.type == SegmentType.BOTTOM) {
            gravity |= GravityHelper.GRAVITY_BOTTOM;
        } else if (mRowMatch.type == SegmentType.CENTER_VERTICAL) {
            gravity |= GravityHelper.GRAVITY_CENTER_VERT;
}
        // Ensure that we have at least one horizontal and vertical constraint, otherwise
        // the new item will be fixed. As an example, if we have a single button in the
        // table which we inserted *without* a gravity, and we then insert a button
        // above it with a vertical gravity, then only the top column would be considered
        // stretchable, and it will fill all available vertical space and the previous
        // button will jump to the bottom.
        if (!GravityHelper.isConstrainedHorizontally(gravity)) {
            gravity |= GravityHelper.GRAVITY_LEFT;
        }
        if (!GravityHelper.isConstrainedVertically(gravity)) {
            gravity |= GravityHelper.GRAVITY_TOP;
        }
        mGrid.setGridAttribute(newChild, ATTR_LAYOUT_GRAVITY, getGravity(gravity));

mGrid.setGridAttribute(newChild, ATTR_LAYOUT_ROW, row);

// Apply spans to ensure that the widget can fit without pushing columns
//Synthetic comment -- @@ -700,7 +704,6 @@
newChild, UNDEFINED, false, UNDEFINED, UNDEFINED);
}
if (mRowMatch.createCell) {
mGrid.addRow(mRowMatch.cellIndex, newChild, UNDEFINED, false, UNDEFINED, UNDEFINED);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridLayoutPainter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridLayoutPainter.java
//Synthetic comment -- index 461ca2b..9f68afb 100644

//Synthetic comment -- @@ -234,15 +234,27 @@
gc.drawLine(x, b.y, x, b.y2());
}

            // Draw preview rectangles for all the dragged elements
gc.useStyle(DrawingStyle.DROP_PREVIEW);
            offsetX += x - bounds.x;
            offsetY += y - bounds.y;

            for (IDragElement element : mElements) {
                if (element == first) {
                    mRule.drawElement(gc, first, offsetX, offsetY);
                    // Preview baseline as well
                    if (feedback.dragBaseline != -1) {
                        int x1 = dragBounds.x + offsetX;
                        int y1 = dragBounds.y + offsetY + feedback.dragBaseline;
                        gc.drawLine(x1, y1, x1 + dragBounds.w, y1);
                    }
                } else {
                    b = element.getBounds();
                    if (b.isValid()) {
                        gc.drawRect(b.x + offsetX, b.y + offsetY,
                                b.x + offsetX + b.w, b.y + offsetY + b.h);
                    }
                }
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridMatch.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridMatch.java
//Synthetic comment -- index aaca4bc..186e7d0 100644

//Synthetic comment -- @@ -88,39 +88,39 @@
public String getDisplayName(INode layout) {
switch (type) {
case BASELINE:
                return String.format("Align baseline in row %1$d", cellIndex + 1);
case CENTER_HORIZONTAL:
return "Center horizontally";
case LEFT:
if (!createCell) {
                    return String.format("Insert into column %1$d", cellIndex + 1);
}
if (margin != UNDEFINED) {
if (cellIndex == 0 && margin != 0) {
return "Add one margin distance from the left";
}
                    return String.format("Add next to column %1$d", cellIndex + 1);
}
return String.format("Align left at x=%1$d", matchedLine - layout.getBounds().x);
case RIGHT:
if (!createCell) {
                    return String.format("Insert right-aligned into column %1$d", cellIndex + 1);
}
return String.format("Align right at x=%1$d", matchedLine - layout.getBounds().x);
case TOP:
if (!createCell) {
                    return String.format("Insert into row %1$d", cellIndex + 1);
}
if (margin != UNDEFINED) {
if (cellIndex == 0 && margin != 0) {
return "Add one margin distance from the top";
}
                    return String.format("Add below row %1$d", cellIndex + 1);
}
return String.format("Align top at y=%1d", matchedLine - layout.getBounds().y);
case BOTTOM:
if (!createCell) {
                    return String.format("Insert into bottom of row %1$d", cellIndex + 1);
}
return String.format("Align bottom at y=%1d", matchedLine - layout.getBounds().y);
case CENTER_VERTICAL:








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridModel.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridModel.java
//Synthetic comment -- index 1d17c9b..65a61b4 100644

//Synthetic comment -- @@ -19,7 +19,6 @@
import static com.android.ide.common.layout.GravityHelper.GRAVITY_CENTER_HORIZ;
import static com.android.ide.common.layout.GravityHelper.GRAVITY_CENTER_VERT;
import static com.android.ide.common.layout.GravityHelper.GRAVITY_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_COLUMN_COUNT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_COLUMN;
//Synthetic comment -- @@ -42,10 +41,13 @@
import static com.android.ide.common.layout.LayoutConstants.VALUE_N_DP;
import static com.android.ide.common.layout.LayoutConstants.VALUE_TOP;
import static com.android.ide.common.layout.LayoutConstants.VALUE_VERTICAL;
import static com.android.util.XmlUtils.ANDROID_URI;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.IClientRulesEngine;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewMetadata;
//Synthetic comment -- @@ -54,10 +56,14 @@
import com.android.ide.common.layout.GravityHelper;
import com.android.ide.common.layout.GridLayoutRule;
import com.android.util.Pair;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
//Synthetic comment -- @@ -67,8 +73,6 @@
import java.util.List;
import java.util.Map;
import java.util.Set;

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

/**
* An actual instance of a GridLayout object that this grid model corresponds to.
*/
//Synthetic comment -- @@ -161,13 +162,44 @@
* @param node the GridLayout node
* @param viewObject an actual GridLayout instance, or null
*/
    private GridModel(IClientRulesEngine rulesEngine, INode node, Object viewObject) {
mRulesEngine = rulesEngine;
layout = node;
mViewObject = viewObject;
loadFromXml();
}

    // Factory cache for most recent item (used primarily because during paints and drags
    // the grid model is called repeatedly for the same view object.)
    private static WeakReference<Object> sCachedViewObject = new WeakReference<Object>(null);
    private static WeakReference<GridModel> sCachedViewModel;

    /**
     * Factory which returns a grid model for the given node.
     *
     * @param rulesEngine the associated rules engine
     * @param node the GridLayout node
     * @param viewObject an actual GridLayout instance, or null
     * @return a new model
     */
    @NonNull
    public static GridModel get(
            @NonNull IClientRulesEngine rulesEngine,
            @NonNull INode node,
            @Nullable Object viewObject) {
        if (viewObject != null && viewObject == sCachedViewObject.get()) {
            GridModel model = sCachedViewModel.get();
            if (model != null) {
                return model;
            }
        }

        GridModel model = new GridModel(rulesEngine, node, viewObject);
        sCachedViewModel = new WeakReference<GridModel>(model);
        sCachedViewObject = new WeakReference<Object>(viewObject);
        return model;
    }

/**
* Returns the {@link ViewData} for the child at the given index
*
//Synthetic comment -- @@ -365,7 +397,7 @@
/**
* Loads a {@link GridModel} from the XML model.
*/
    private void loadFromXml() {
INode[] children = layout.getChildren();

declaredRowCount = getGridAttribute(layout, ATTR_ROW_COUNT, UNDEFINED);
//Synthetic comment -- @@ -381,17 +413,17 @@
}

// Assign row/column positions to all cells that do not explicitly define them
        if (!assignRowsAndColumnsFromViews(mChildViews)) {
            assignRowsAndColumnsFromXml(
                    declaredRowCount == UNDEFINED ? children.length : declaredRowCount,
                    declaredColumnCount == UNDEFINED ? children.length : declaredColumnCount);
        }

assignCellBounds();

for (int i = 0; i <= actualRowCount; i++) {
mBaselines[i] = UNDEFINED;
}
}

private Pair<Map<Integer, Integer>, Map<Integer, Integer>> findCellsOutsideDeclaredBounds() {
//Synthetic comment -- @@ -450,7 +482,7 @@
* TODO: Consolidate with the algorithm in GridLayout to ensure we get the
* exact same results!
*/
    private void assignRowsAndColumnsFromXml(int rowCount, int columnCount) {
Pair<Map<Integer, Integer>, Map<Integer, Integer>> p = findCellsOutsideDeclaredBounds();
Map<Integer, Integer> extraRowsMap = p.getFirst();
Map<Integer, Integer> extraColumnsMap = p.getSecond();
//Synthetic comment -- @@ -553,6 +585,80 @@
}
}

    private static boolean sAttemptSpecReflection = true;

    private boolean assignRowsAndColumnsFromViews(List<ViewData> views) {
        if (!sAttemptSpecReflection) {
            return false;
        }

        try {
            // Lazily initialized reflection methods
            Field spanField = null;
            Field rowSpecField = null;
            Field colSpecField = null;
            Field minField = null;
            Field maxField = null;
            Method getLayoutParams = null;

            for (ViewData view : views) {
                // TODO: If the element *specifies* anything in XML, use that instead
                Object child = mRulesEngine.getViewObject(view.node);
                if (child == null) {
                    // Fallback to XML model
                    return false;
                }

                if (getLayoutParams == null) {
                    getLayoutParams = child.getClass().getMethod("getLayoutParams"); //$NON-NLS-1$
                }
                Object layoutParams = getLayoutParams.invoke(child);
                if (rowSpecField == null) {
                    Class<? extends Object> layoutParamsClass = layoutParams.getClass();
                    rowSpecField = layoutParamsClass.getDeclaredField("rowSpec");    //$NON-NLS-1$
                    colSpecField = layoutParamsClass.getDeclaredField("columnSpec"); //$NON-NLS-1$
                    rowSpecField.setAccessible(true);
                    colSpecField.setAccessible(true);
                }
                assert colSpecField != null;

                Object rowSpec = rowSpecField.get(layoutParams);
                Object colSpec = colSpecField.get(layoutParams);
                if (spanField == null) {
                    spanField = rowSpec.getClass().getDeclaredField("span"); //$NON-NLS-1$
                    spanField.setAccessible(true);
                }
                assert spanField != null;
                Object rowInterval = spanField.get(rowSpec);
                Object colInterval = spanField.get(colSpec);
                if (minField == null) {
                    Class<? extends Object> intervalClass = rowInterval.getClass();
                    minField = intervalClass.getDeclaredField("min"); //$NON-NLS-1$
                    maxField = intervalClass.getDeclaredField("max"); //$NON-NLS-1$
                    minField.setAccessible(true);
                    maxField.setAccessible(true);
                }
                assert maxField != null;

                int row = minField.getInt(rowInterval);
                int col = minField.getInt(colInterval);
                int rowEnd = maxField.getInt(rowInterval);
                int colEnd = maxField.getInt(colInterval);

                view.column = col;
                view.row = row;
                view.columnSpan = colEnd - col;
                view.rowSpan = rowEnd - row;
            }

            return true;

        } catch (Throwable e) {
            sAttemptSpecReflection = false;
            return false;
        }
    }

/**
* Computes the positions of the column and row boundaries
*/
//Synthetic comment -- @@ -805,10 +911,8 @@
*/
public INode addColumn(int newColumn, INode newView, int columnWidthDp,
boolean split, int row, int x) {
// Insert a new column
        actualColumnCount++;
if (declaredColumnCount != UNDEFINED) {
declaredColumnCount++;
setGridAttribute(layout, ATTR_COLUMN_COUNT, declaredColumnCount);
//Synthetic comment -- @@ -838,11 +942,14 @@
index++;
}

                        ViewData newViewData = addSpacer(layout, index,
split ? row : UNDEFINED,
split ? newColumn - 1 : UNDEFINED,
columnWidthDp != UNDEFINED ? columnWidthDp : DEFAULT_CELL_WIDTH,
DEFAULT_CELL_HEIGHT);
                        newViewData.column = newColumn - 1;
                        newViewData.row = row;
                        newView = newViewData.node;
}

// Set the actual row number on the first cell on the new row.
//Synthetic comment -- @@ -850,19 +957,23 @@
// the new row number, but we use the spacer to assign the row
// some height.
if (view.column == newColumn) {
                        view.column++;
                        setGridAttribute(view.node, ATTR_LAYOUT_COLUMN, view.column);
} // else: endColumn == newColumn: handled below
} else if (getGridAttribute(view.node, ATTR_LAYOUT_COLUMN) != null) {
                    view.column++;
                    setGridAttribute(view.node, ATTR_LAYOUT_COLUMN, view.column);
}
} else if (endColumn > newColumn) {
                view.columnSpan++;
                setColumnSpanAttribute(view.node, view.columnSpan);
columnSpanSet = true;
}

if (split && !columnSpanSet && view.node.getBounds().x2() > x) {
if (view.node.getBounds().x < x) {
                    view.columnSpan++;
                    setColumnSpanAttribute(view.node, view.columnSpan);
}
}
}
//Synthetic comment -- @@ -896,18 +1007,17 @@
return;
}

// Figure out which columns should be removed
        Set<Integer> removeColumns = new HashSet<Integer>();
        Set<ViewData> removedViews = new HashSet<ViewData>();
for (INode child : selectedChildren) {
ViewData view = getView(child);
            removedViews.add(view);
            removeColumns.add(view.column);
}
// Sort them in descending order such that we can process each
// deletion independently
        List<Integer> removed = new ArrayList<Integer>(removeColumns);
Collections.sort(removed, Collections.reverseOrder());

for (int removedColumn : removed) {
//Synthetic comment -- @@ -916,9 +1026,9 @@
// TODO: Don't do this if the column being deleted is outside
// the declared column range!
// TODO: Do this under a write lock? / editXml lock?
            actualColumnCount--;
if (declaredColumnCount != UNDEFINED) {
declaredColumnCount--;
}

// Remove any elements that begin in the deleted columns...
//Synthetic comment -- @@ -935,21 +1045,44 @@
int columnWidth = getColumnWidth(removedColumn, view.columnSpan) -
getColumnWidth(removedColumn, 1);
int columnWidthDip = mRulesEngine.pxToDp(columnWidth);
                        ViewData spacer = addSpacer(layout, index, UNDEFINED, UNDEFINED,
                                columnWidthDip, SPACER_SIZE_DP);
                        spacer.row = 0;
                        spacer.column = removedColumn;
}
layout.removeChild(view.node);
} else if (view.column < removedColumn
&& view.column + view.columnSpan > removedColumn) {
// Subtract column span to skip this item
                    view.columnSpan--;
                    setColumnSpanAttribute(view.node, view.columnSpan);
} else if (view.column > removedColumn) {
                    view.column--;
if (getGridAttribute(view.node, ATTR_LAYOUT_COLUMN) != null) {
                        setGridAttribute(view.node, ATTR_LAYOUT_COLUMN, view.column);
}
}
}
}

        // Remove children from child list!
        if (removedViews.size() <= 2) {
            mChildViews.removeAll(removedViews);
        } else {
            List<ViewData> remaining =
                    new ArrayList<ViewData>(mChildViews.size() - removedViews.size());
            for (ViewData view : mChildViews) {
                if (!removedViews.contains(view)) {
                    remaining.add(view);
                }
            }
            mChildViews = remaining;
        }

        //if (declaredColumnCount != UNDEFINED) {
            setGridAttribute(layout, ATTR_COLUMN_COUNT, actualColumnCount);
        //}

}

/**
//Synthetic comment -- @@ -991,14 +1124,12 @@
*/
public INode addRow(int newRow, INode newView, int rowHeightDp, boolean split,
int column, int y) {
        actualRowCount++;
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
                        ViewData newViewData = addSpacer(layout, index,
split ? newRow - 1 : UNDEFINED,
split ? column : UNDEFINED,
SPACER_SIZE_DP,
rowHeightDp != UNDEFINED ? rowHeightDp : DEFAULT_CELL_HEIGHT);
                        newViewData.column = column;
                        newViewData.row = newRow - 1;
                        newView = newViewData.node;
}

// Set the actual row number on the first cell on the new row.
// This means we don't really need the spacer above to imply
// the new row number, but we use the spacer to assign the row
// some height.
                    view.row++;
                    setGridAttribute(view.node, ATTR_LAYOUT_ROW, view.row);

added = true;
} else if (getGridAttribute(view.node, ATTR_LAYOUT_ROW) != null) {
                    view.row++;
                    setGridAttribute(view.node, ATTR_LAYOUT_ROW, view.row);
}
} else {
int endRow = view.row + view.rowSpan;
if (endRow > newRow) {
                    view.rowSpan++;
                    setRowSpanAttribute(view.node, view.rowSpan);
} else if (split && view.node.getBounds().y2() > y) {
if (view.node.getBounds().y < y) {
                        view.rowSpan++;
                        setRowSpanAttribute(view.node, view.rowSpan);
}
}
}
//Synthetic comment -- @@ -1043,9 +1181,13 @@
if (!added) {
// Append a row at the end
if (newView == null) {
                ViewData newViewData = addSpacer(layout, -1, UNDEFINED, UNDEFINED,
SPACER_SIZE_DP,
rowHeightDp != UNDEFINED ? rowHeightDp : DEFAULT_CELL_HEIGHT);
                newViewData.column = column;
                // TODO: MAke sure this row number is right!
                newViewData.row = split ? newRow - 1 : newRow;
                newView = newViewData.node;
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

// Figure out which rows should be removed
        Set<ViewData> removedViews = new HashSet<ViewData>();
        Set<Integer> removedRows = new HashSet<Integer>();
for (INode child : selectedChildren) {
ViewData view = getView(child);
            removedViews.add(view);
            removedRows.add(view.row);
}
// Sort them in descending order such that we can process each
// deletion independently
        List<Integer> removed = new ArrayList<Integer>(removedRows);
Collections.sort(removed, Collections.reverseOrder());

for (int removedRow : removed) {
//Synthetic comment -- @@ -1089,6 +1229,7 @@
// First, adjust row count.
// TODO: Don't do this if the row being deleted is outside
// the declared row range!
            actualRowCount--;
if (declaredRowCount != UNDEFINED) {
declaredRowCount--;
setGridAttribute(layout, ATTR_ROW_COUNT, declaredRowCount);
//Synthetic comment -- @@ -1104,18 +1245,35 @@
// We don't have to worry about a rowSpan > 1 here, because even
// if it is, those rowspans are not used to assign default row/column
// positions for other cells
// TODO: Check this; it differs from the removeColumns logic!
layout.removeChild(view.node);
} else if (view.row > removedRow) {
                    view.row--;
if (getGridAttribute(view.node, ATTR_LAYOUT_ROW) != null) {
                        setGridAttribute(view.node, ATTR_LAYOUT_ROW, view.row);
}
} else if (view.row < removedRow
&& view.row + view.rowSpan > removedRow) {
// Subtract row span to skip this item
                    view.rowSpan--;
                    setRowSpanAttribute(view.node, view.rowSpan);
}
}
}

        // Remove children from child list!
        if (removedViews.size() <= 2) {
            mChildViews.removeAll(removedViews);
        } else {
            List<ViewData> remaining =
                    new ArrayList<ViewData>(mChildViews.size() - removedViews.size());
            for (ViewData view : mChildViews) {
                if (!removedViews.contains(view)) {
                    remaining.add(view);
                }
            }
            mChildViews = remaining;
        }
}

/**
//Synthetic comment -- @@ -1364,10 +1522,6 @@
*/
@Override
public String toString() {
// Dump out the view table
int cellWidth = 25;

//Synthetic comment -- @@ -1380,9 +1534,6 @@
rowList.add(columnList);
}
for (ViewData view : mChildViews) {
for (int i = 0; i < view.rowSpan; i++) {
if (view.row + i > mTop.length) { // Guard against bogus span values
break;
//Synthetic comment -- @@ -1456,8 +1607,7 @@
* @param x the x coordinate of the new column
*/
public void splitColumn(int newColumn, boolean insertMarginColumn, int columnWidthDp, int x) {
        actualColumnCount++;

// Insert a new column
if (declaredColumnCount != UNDEFINED) {
//Synthetic comment -- @@ -1525,14 +1675,24 @@
//   skipped column!

//if (getGridAttribute(node, ATTR_LAYOUT_COLUMN) != null) {
                view.column += insertMarginColumn ? 2 : 1;
                setGridAttribute(node, ATTR_LAYOUT_COLUMN, view.column);
//}
} else if (!view.isSpacer()) {
                // Adjust the column span? We must increase it if
                //  (1) the new column is inside the range [column, column + columnSpan]
                //  (2) the new column is within the last cell in the column span,
                //      and the exact X location of the split is within the horizontal
                //      *bounds* of this node (provided it has gravity=left)
                //  (3) the new column is within the last cell and the cell has gravity
                //      right or gravity center
int endColumn = column + view.columnSpan;
if (endColumn > newColumn
                        || endColumn == newColumn && (view.node.getBounds().x2() > x
                                || !GravityHelper.isLeftAligned(view.gravity))) {
// This cell spans the new insert position, so increment the column span
                    view.columnSpan += insertMarginColumn ? 2 : 1;
                    setColumnSpanAttribute(node, view.columnSpan);
}
}
}
//Synthetic comment -- @@ -1548,8 +1708,9 @@
if (remaining > 0) {
prevColumnSpacer.node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH,
String.format(VALUE_N_DP, remaining));
                prevColumnSpacer.column = insertMarginColumn ? newColumn + 1 : newColumn;
setGridAttribute(prevColumnSpacer.node, ATTR_LAYOUT_COLUMN,
                        prevColumnSpacer.column);
}
}

//Synthetic comment -- @@ -1575,6 +1736,8 @@
* @param y the y coordinate of the new row
*/
public void splitRow(int newRow, boolean insertMarginRow, int rowHeightDp, int y) {
        actualRowCount++;

// Insert a new row
if (declaredRowCount != UNDEFINED) {
declaredRowCount++;
//Synthetic comment -- @@ -1603,14 +1766,17 @@
int row = view.row;
if (row > newRow || (row == newRow && view.node.getBounds().y2() > y)) {
//if (getGridAttribute(node, ATTR_LAYOUT_ROW) != null) {
                view.row += insertMarginRow ? 2 : 1;
                setGridAttribute(node, ATTR_LAYOUT_ROW, view.row);
//}
} else if (!view.isSpacer()) {
int endRow = row + view.rowSpan;
if (endRow > newRow
                        || endRow == newRow && (view.node.getBounds().y2() > y
                                || !GravityHelper.isTopAligned(view.gravity))) {
// This cell spans the new insert position, so increment the row span
                    view.rowSpan += insertMarginRow ? 2 : 1;
                    setRowSpanAttribute(node, view.rowSpan);
}
}
}
//Synthetic comment -- @@ -1626,8 +1792,8 @@
if (remaining > 0) {
prevRowSpacer.node.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT,
String.format(VALUE_N_DP, remaining));
                prevRowSpacer.row = insertMarginRow ? newRow + 1 : newRow;
                setGridAttribute(prevRowSpacer.node, ATTR_LAYOUT_ROW, prevRowSpacer.row);
}
}

//Synthetic comment -- @@ -1664,12 +1830,8 @@

/** Applies the column and row fields into the XML model */
void applyPositionAttributes() {
            setGridAttribute(node, ATTR_LAYOUT_COLUMN, column);
            setGridAttribute(node, ATTR_LAYOUT_ROW, row);
}

/** Returns the id of this node, or makes one up for display purposes */
//Synthetic comment -- @@ -1688,8 +1850,7 @@

/** Returns true if this {@link ViewData} represents a spacer */
boolean isSpacer() {
            return isSpace(node.getFqcn());
}

/**
//Synthetic comment -- @@ -1755,46 +1916,52 @@
}

/**
     * Update the model to account for the given nodes getting deleted. The nodes
     * are not actually deleted by this method; that is assumed to be performed by the
     * caller. Instead this method performs whatever model updates are necessary to
     * preserve the grid structure.
*
     * @param nodes the nodes to be deleted
*/
    public void onDeleted(@NonNull List<INode> nodes) {
        if (nodes.size() == 0) {
return;
}

        // Attempt to clean up spacer objects for any newly-empty rows or columns
        // as the result of this deletion

        Set<INode> deleted = new HashSet<INode>();

        for (INode child : nodes) {
            // We don't care about deletion of spacers
            String fqcn = child.getFqcn();
            if (fqcn.equals(FQCN_SPACE) || fqcn.equals(FQCN_SPACE_V7)) {
                continue;
            }
            deleted.add(child);
        }

Set<Integer> usedColumns = new HashSet<Integer>(actualColumnCount);
        Set<Integer> usedRows = new HashSet<Integer>(actualRowCount);
        Multimap<Integer, ViewData> columnSpacers = ArrayListMultimap.create(actualColumnCount, 2);
        Multimap<Integer, ViewData> rowSpacers = ArrayListMultimap.create(actualRowCount, 2);
        Set<ViewData> removedViews = new HashSet<ViewData>();

for (ViewData view : mChildViews) {
            if (deleted.contains(view.node)) {
                removedViews.add(view);
            } else if (view.isColumnSpacer()) {
columnSpacers.put(view.column, view);
} else if (view.isRowSpacer()) {
rowSpacers.put(view.row, view);
            } else {
usedColumns.add(Integer.valueOf(view.column));
usedRows.add(Integer.valueOf(view.row));
}
}

        if (usedColumns.size() == 0 || usedRows.size() == 0) {
// No more views - just remove all the spacers
for (ViewData spacer : columnSpacers.values()) {
layout.removeChild(spacer.node);
//Synthetic comment -- @@ -1802,158 +1969,263 @@
for (ViewData spacer : rowSpacers.values()) {
layout.removeChild(spacer.node);
}
            mChildViews.clear();
            actualColumnCount = 0;
            declaredColumnCount = 2;
            actualRowCount = 0;
            declaredRowCount = UNDEFINED;
setGridAttribute(layout, ATTR_COLUMN_COUNT, 2);

return;
}

        // Determine columns to introduce spacers into:
        // This is tricky; I should NOT combine spacers if there are cells tied to
        // individual ones

        // TODO: Invalidate column sizes too! Otherwise repeated updates might get confused!
        // Similarly, inserts need to do the same!

        // Produce map of old column numbers to new column numbers
        // Collapse regions of consecutive space and non-space ranges together
        int[] columnMap = new int[actualColumnCount + 1]; // +1: Easily handle columnSpans as well
        int newColumn = 0;
        boolean prevUsed = usedColumns.contains(0);
        for (int column = 1; column < actualColumnCount; column++) {
            boolean used = usedColumns.contains(column);
            if (used || prevUsed != used) {
                newColumn++;
                prevUsed = used;
            }
            columnMap[column] = newColumn;
        }
        newColumn++;
        columnMap[actualColumnCount] = newColumn;
        assert columnMap[0] == 0;

        int[] rowMap = new int[actualRowCount + 1]; // +1: Easily handle rowSpans as well
        int newRow = 0;
        prevUsed = usedRows.contains(0);
        for (int row = 1; row < actualRowCount; row++) {
            boolean used = usedRows.contains(row);
            if (used || prevUsed != used) {
                newRow++;
                prevUsed = used;
            }
            rowMap[row] = newRow;
        }
        newRow++;
        rowMap[actualRowCount] = newRow;
        assert rowMap[0] == 0;


        // Adjust column and row numbers to account for deletions: for a given cell, if it
        // is to the right of a deleted column, reduce its column number, and if it only
        // spans across the deleted column, reduce its column span.
        for (ViewData view : mChildViews) {
            if (removedViews.contains(view)) {
                continue;
            }
            int newColumnStart = columnMap[Math.min(columnMap.length - 1, view.column)];
            // Gracefully handle rogue/invalid columnSpans in the XML
            int newColumnEnd = columnMap[Math.min(columnMap.length - 1,
                    view.column + view.columnSpan)];
            if (newColumnStart != view.column) {
                view.column = newColumnStart;
                setGridAttribute(view.node, ATTR_LAYOUT_COLUMN, view.column);
            }

            int columnSpan = newColumnEnd - newColumnStart;
            if (columnSpan != view.columnSpan) {
                if (columnSpan >= 1) {
                    view.columnSpan = columnSpan;
                    setColumnSpanAttribute(view.node, view.columnSpan);
                } // else: merging spacing columns together
            }


            int newRowStart = rowMap[Math.min(rowMap.length - 1, view.row)];
            int newRowEnd = rowMap[Math.min(rowMap.length - 1, view.row + view.rowSpan)];
            if (newRowStart != view.row) {
                view.row = newRowStart;
                setGridAttribute(view.node, ATTR_LAYOUT_ROW, view.row);
            }

            int rowSpan = newRowEnd - newRowStart;
            if (rowSpan != view.rowSpan) {
                if (rowSpan >= 1) {
                    view.rowSpan = rowSpan;
                    setRowSpanAttribute(view.node, view.rowSpan);
                } // else: merging spacing rows together
            }
        }

        // Merge spacers (and add spacers for newly empty columns)
        int start = 0;
        while (start < actualColumnCount) {
            // Find next unused span
            while (start < actualColumnCount && usedColumns.contains(start)) {
                start++;
            }
            if (start == actualColumnCount) {
                break;
            }
            assert !usedColumns.contains(start);
            // Find the next span of unused columns and produce a SINGLE
            // spacer for that range (unless it's a zero-sized columns)
            int end = start + 1;
            for (; end < actualColumnCount; end++) {
                if (usedColumns.contains(end)) {
                    break;
}
            }

            // Add up column sizes
            int width = getColumnWidth(start, end - start);

            // Find all spacers: the first one found should be moved to the start column
            // and assigned to the full height of the columns, and
            // the column count reduced by the corresponding amount

            // TODO: if width = 0, fully remove

            boolean isFirstSpacer = true;
            for (int column = start; column < end; column++) {
                Collection<ViewData> spacers = columnSpacers.get(column);
                if (spacers != null && !spacers.isEmpty()) {
                    // Avoid ConcurrentModificationException since we're inserting into the
                    // map within this loop (always at a different index, but the map doesn't
                    // know that)
                    spacers = new ArrayList<ViewData>(spacers);
                    for (ViewData spacer : spacers) {
                        if (isFirstSpacer) {
                            isFirstSpacer = false;
                            spacer.column = columnMap[start];
                            setGridAttribute(spacer.node, ATTR_LAYOUT_COLUMN, spacer.column);
                            if (end - start > 1) {
                                // Compute a merged width for all the spacers (not needed if
                                // there's just one spacer; it should already have the correct width)
                                int columnWidthDp = mRulesEngine.pxToDp(width);
                                spacer.node.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH,
                                        String.format(VALUE_N_DP, columnWidthDp));
                            }
                            columnSpacers.put(start, spacer);
                        } else {
                            removedViews.add(spacer); // Mark for model removal
                            layout.removeChild(spacer.node);
}
}
}
}

            if (isFirstSpacer) {
                // No spacer: create one
                int columnWidthDp = mRulesEngine.pxToDp(width);
                addSpacer(layout, -1, UNDEFINED, columnMap[start], columnWidthDp, DEFAULT_CELL_HEIGHT);
            }

            start = end;
}
        actualColumnCount = newColumn;
//if (usedColumns.contains(newColumn)) {
//    // TODO: This may be totally wrong for right aligned content!
//    actualColumnCount++;
//}

        // Merge spacers for rows
        start = 0;
        while (start < actualRowCount) {
            // Find next unused span
            while (start < actualRowCount && usedRows.contains(start)) {
                start++;
            }
            if (start == actualRowCount) {
                break;
            }
            assert !usedRows.contains(start);
            // Find the next span of unused rows and produce a SINGLE
            // spacer for that range (unless it's a zero-sized rows)
            int end = start + 1;
            for (; end < actualRowCount; end++) {
                if (usedRows.contains(end)) {
                    break;
}
            }

            // Add up row sizes
            int height = getRowHeight(start, end - start);

            // Find all spacers: the first one found should be moved to the start row
            // and assigned to the full height of the rows, and
            // the row count reduced by the corresponding amount

            // TODO: if width = 0, fully remove

            boolean isFirstSpacer = true;
            for (int row = start; row < end; row++) {
                Collection<ViewData> spacers = rowSpacers.get(row);
                if (spacers != null && !spacers.isEmpty()) {
                    // Avoid ConcurrentModificationException since we're inserting into the
                    // map within this loop (always at a different index, but the map doesn't
                    // know that)
                    spacers = new ArrayList<ViewData>(spacers);
                    for (ViewData spacer : spacers) {
                        if (isFirstSpacer) {
                            isFirstSpacer = false;
                            spacer.row = rowMap[start];
                            setGridAttribute(spacer.node, ATTR_LAYOUT_ROW, spacer.row);
                            if (end - start > 1) {
                                // Compute a merged width for all the spacers (not needed if
                                // there's just one spacer; it should already have the correct height)
                                int rowHeightDp = mRulesEngine.pxToDp(height);
                                spacer.node.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT,
                                        String.format(VALUE_N_DP, rowHeightDp));
                            }
                            rowSpacers.put(start, spacer);
                        } else {
                            removedViews.add(spacer); // Mark for model removal
                            layout.removeChild(spacer.node);
}
}
}
}

            if (isFirstSpacer) {
                // No spacer: create one
                int rowWidthDp = mRulesEngine.pxToDp(height);
                addSpacer(layout, -1, rowMap[start], UNDEFINED, DEFAULT_CELL_WIDTH, rowWidthDp);
            }

            start = end;
}
        actualRowCount = newRow;
//        if (usedRows.contains(newRow)) {
//            actualRowCount++;
//        }

        // Update the model: remove removed children from the view data list
        if (removedViews.size() <= 2) {
            mChildViews.removeAll(removedViews);
        } else {
            List<ViewData> remaining =
                    new ArrayList<ViewData>(mChildViews.size() - removedViews.size());
            for (ViewData view : mChildViews) {
                if (!removedViews.contains(view)) {
                    remaining.add(view);
}
}
            mChildViews = remaining;
}

        // Update the final column and row declared attributes
        if (declaredColumnCount != UNDEFINED) {
            declaredColumnCount = actualColumnCount;
            setGridAttribute(layout, ATTR_COLUMN_COUNT, actualColumnCount);
        }
        if (declaredRowCount != UNDEFINED) {
            declaredRowCount = actualRowCount;
            setGridAttribute(layout, ATTR_ROW_COUNT, actualRowCount);
        }
}

/**
//Synthetic comment -- @@ -1968,7 +2240,7 @@
* @param heightDp the height in device independent pixels to assign to the spacer
* @return the newly added spacer
*/
    ViewData addSpacer(INode parent, int index, int row, int column,
int widthDp, int heightDp) {
INode spacer;

//Synthetic comment -- @@ -1984,10 +2256,15 @@
spacer = parent.appendChild(tag);
}

        ViewData view = new ViewData(spacer, index != -1 ? index : mChildViews.size());
        mChildViews.add(view);

if (row != UNDEFINED) {
            view.row = row;
setGridAttribute(spacer, ATTR_LAYOUT_ROW, row);
}
if (column != UNDEFINED) {
            view.column = column;
setGridAttribute(spacer, ATTR_LAYOUT_COLUMN, column);
}
if (widthDp > 0) {
//Synthetic comment -- @@ -2020,7 +2297,7 @@
}


        return view;
}

/**
//Synthetic comment -- @@ -2068,4 +2345,14 @@
public int getViewCount() {
return mChildViews.size();
}

    /**
     * Returns true if the given class name represents a spacer
     *
     * @param fqcn the fully qualified class name
     * @return true if this is a spacer
     */
    public static boolean isSpace(String fqcn) {
        return FQCN_SPACE.equals(fqcn) || FQCN_SPACE_V7.equals(fqcn);
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java
//Synthetic comment -- index 47159c3..bef070b 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import static com.android.ide.common.layout.LayoutConstants.ATTR_COLUMN_COUNT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_COLUMN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_COLUMN_SPAN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_GRAVITY;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ROW;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ROW_SPAN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ROW_COUNT;
//Synthetic comment -- @@ -30,6 +31,7 @@
import static com.android.tools.lint.detector.api.LintConstants.AUTO_URI;
import static com.android.tools.lint.detector.api.LintConstants.URI_PREFIX;
import static com.android.util.XmlUtils.ANDROID_URI;
import static org.eclipse.jface.viewers.StyledString.COUNTER_STYLER;
import static org.eclipse.jface.viewers.StyledString.QUALIFIER_STYLER;

import com.android.annotations.VisibleForTesting;
//Synthetic comment -- @@ -800,11 +802,14 @@

// Temporary diagnostics code when developing GridLayout
if (GridLayoutRule.sDebugGridLayout) {

String namespace;
                        if (e.getNodeName().equals(GRID_LAYOUT) ||
                                e.getParentNode() != null
&& e.getParentNode().getNodeName().equals(GRID_LAYOUT)) {
namespace = ANDROID_URI;
} else {
                            // Else: probably a v7 gridlayout
IProject project = mGraphicalEditorPart.getProject();
ProjectState projectState = Sdk.getProjectState(project);
if (projectState != null && projectState.isLibrary()) {
//Synthetic comment -- @@ -872,6 +877,13 @@
styledString.append(',', QUALIFIER_STYLER);
styledString.append(rowSpan, QUALIFIER_STYLER);
styledString.append(')', QUALIFIER_STYLER);

                            String gravity = e.getAttributeNS(namespace, ATTR_LAYOUT_GRAVITY);
                            if (gravity != null && gravity.length() > 0) {
                                styledString.append(" : ", COUNTER_STYLER);
                                styledString.append(gravity, COUNTER_STYLER);
                            }

}
}

//Synthetic comment -- @@ -886,10 +898,14 @@
text = resolved;
}
}
                            if (styledString.length() < LABEL_MAX_WIDTH - LABEL_SEPARATOR.length()
                                    - 2) {
                                styledString.append(LABEL_SEPARATOR, QUALIFIER_STYLER);

                                styledString.append('"', QUALIFIER_STYLER);
                                styledString.append(truncate(text, styledString), QUALIFIER_STYLER);
                                styledString.append('"', QUALIFIER_STYLER);
                            }
}
} else if (e.hasAttributeNS(ANDROID_URI, ATTR_SRC)) {
// Show ImageView source attributes etc








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java
//Synthetic comment -- index 23e42ef..424be26 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_MERGE;

import com.android.annotations.NonNull;
//Synthetic comment -- @@ -31,6 +32,7 @@

import org.eclipse.swt.graphics.Rectangle;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.awt.image.BufferedImage;
//Synthetic comment -- @@ -446,7 +448,7 @@
} else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
return mDomNodeToView.get(((Attr) node).getOwnerElement());
} else if (node.getNodeType() == Node.DOCUMENT_NODE) {
                return mDomNodeToView.get(((Document) node).getDocumentElement());
}
}

//Synthetic comment -- @@ -600,7 +602,11 @@
* @return A {@link CanvasViewInfo} matching the given key, or null if not
*         found.
*/
    @Nullable
    public CanvasViewInfo findViewInfoFor(@Nullable NodeProxy proxy) {
        if (proxy == null) {
            return null;
        }
return mNodeToView.get(proxy.getNode());
}

//Synthetic comment -- @@ -710,6 +716,7 @@
/**
* Dumps a {@link ViewInfo} hierarchy to stdout
*
     * @param session the corresponding session, if any
* @param info the {@link ViewInfo} object to dump
* @param depth the depth to indent it to
*/
//Synthetic comment -- @@ -736,6 +743,12 @@
sb.append("<"); //$NON-NLS-1$
sb.append(node.getDescriptor().getXmlName());
sb.append(">"); //$NON-NLS-1$

                String id = node.getAttributeValue(ATTR_ID);
                if (id != null && !id.isEmpty()) {
                    sb.append(" ");
                    sb.append(id);
                }
} else if (cookie != null) {
sb.append(" " + cookie); //$NON-NLS-1$
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ClientRulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ClientRulesEngine.java
//Synthetic comment -- index 8337007..60f8365 100644

//Synthetic comment -- @@ -41,10 +41,12 @@
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorDelegate;
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.CanvasViewInfo;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutCanvas;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.RenderService;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.SelectionManager;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.ViewHierarchy;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
//Synthetic comment -- @@ -180,6 +182,18 @@
}

@Override
    @Nullable
    public Object getViewObject(@NonNull INode node) {
        ViewHierarchy views = mRulesEngine.getEditor().getCanvasControl().getViewHierarchy();
        CanvasViewInfo vi = views.findViewInfoFor(node);
        if (vi != null) {
            return vi.getViewObject();
        }

        return null;
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
import static com.android.ide.common.layout.GravityHelper.GRAVITY_FILL_HORIZ;
import static com.android.ide.common.layout.GravityHelper.GRAVITY_FILL_VERT;
import static com.android.ide.common.layout.GravityHelper.GRAVITY_LEFT;
import static com.android.ide.common.layout.GravityHelper.GRAVITY_RIGHT;
import static com.android.ide.common.layout.GravityHelper.GRAVITY_TOP;
import static com.android.ide.common.layout.GravityHelper.getGravity;
import junit.framework.TestCase;

@SuppressWarnings("javadoc")
//Synthetic comment -- @@ -29,4 +34,52 @@
assertEquals(GRAVITY_CENTER_HORIZ | GRAVITY_CENTER_VERT,
GravityHelper.getGravity("center", 0));
}

    public void testGravityString() throws Exception {
        assertEquals("left", getGravity(GRAVITY_LEFT));
        assertEquals("right", getGravity(GRAVITY_RIGHT));
        assertEquals("top", getGravity(GRAVITY_TOP));
        assertEquals("bottom", getGravity(GRAVITY_BOTTOM));
        assertEquals("center_horizontal", getGravity(GRAVITY_CENTER_HORIZ));
        assertEquals("center_vertical", getGravity(GRAVITY_CENTER_VERT));
        assertEquals("fill_horizontal", getGravity(GRAVITY_FILL_HORIZ));
        assertEquals("fill_vertical", getGravity(GRAVITY_FILL_VERT));

        assertEquals("center", getGravity(GRAVITY_CENTER_HORIZ|GRAVITY_CENTER_VERT));

        assertEquals("left|bottom", getGravity(GRAVITY_LEFT|GRAVITY_BOTTOM));
        assertEquals("center_horizontal|top", getGravity(GRAVITY_CENTER_HORIZ|GRAVITY_TOP));
    }

    public void testConstrained() throws Exception {
        assertTrue(GravityHelper.isConstrainedHorizontally(GRAVITY_LEFT));
        assertTrue(GravityHelper.isConstrainedHorizontally(GRAVITY_RIGHT));
        assertTrue(GravityHelper.isConstrainedHorizontally(GRAVITY_CENTER_HORIZ));
        assertTrue(GravityHelper.isConstrainedHorizontally(GRAVITY_FILL_HORIZ));

        assertFalse(GravityHelper.isConstrainedVertically(GRAVITY_LEFT));
        assertFalse(GravityHelper.isConstrainedVertically(GRAVITY_RIGHT));
        assertFalse(GravityHelper.isConstrainedVertically(GRAVITY_CENTER_HORIZ));
        assertFalse(GravityHelper.isConstrainedVertically(GRAVITY_FILL_HORIZ));

        assertTrue(GravityHelper.isConstrainedVertically(GRAVITY_TOP));
        assertTrue(GravityHelper.isConstrainedVertically(GRAVITY_BOTTOM));
        assertTrue(GravityHelper.isConstrainedVertically(GRAVITY_CENTER_VERT));
        assertTrue(GravityHelper.isConstrainedVertically(GRAVITY_FILL_VERT));

        assertFalse(GravityHelper.isConstrainedHorizontally(GRAVITY_TOP));
        assertFalse(GravityHelper.isConstrainedHorizontally(GRAVITY_BOTTOM));
        assertFalse(GravityHelper.isConstrainedHorizontally(GRAVITY_CENTER_VERT));
        assertFalse(GravityHelper.isConstrainedHorizontally(GRAVITY_FILL_VERT));
    }

    public void testAligned() throws Exception {
        assertTrue(GravityHelper.isLeftAligned(GRAVITY_LEFT|GRAVITY_TOP));
        assertTrue(GravityHelper.isLeftAligned(GRAVITY_LEFT));
        assertFalse(GravityHelper.isLeftAligned(GRAVITY_RIGHT));

        assertTrue(GravityHelper.isTopAligned(GRAVITY_LEFT|GRAVITY_TOP));
        assertTrue(GravityHelper.isTopAligned(GRAVITY_TOP));
        assertFalse(GravityHelper.isTopAligned(GRAVITY_BOTTOM));
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LayoutTestBase.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LayoutTestBase.java
//Synthetic comment -- index 826f36c..b59144a 100644

//Synthetic comment -- @@ -192,10 +192,10 @@
rule.onInitialize(fqn, new TestRulesEngine(fqn));
}

    public static class TestRulesEngine implements IClientRulesEngine {
private final String mFqn;

        public TestRulesEngine(String fqn) {
mFqn = fqn;
}

//Synthetic comment -- @@ -320,8 +320,14 @@

@Override
public int pxToDp(int px) {
            // Arbitrary conversion
            return px / 3;
        }

        @Override
        public int dpToPx(int dp) {
            // Arbitrary conversion
            return 3 * dp;
}

@Override
//Synthetic comment -- @@ -333,13 +339,7 @@
@Override
public int screenToLayout(int pixels) {
fail("Not supported in tests yet");
            return pixels;
}

@Override
//Synthetic comment -- @@ -347,6 +347,12 @@
fail("Not supported in tests yet");
return null;
}

        @Override
        public @Nullable Object getViewObject(@NonNull INode node) {
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

import static com.android.ide.common.layout.LayoutConstants.ANDROID_WIDGET_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -25,14 +30,30 @@
import com.android.ide.common.api.INodeHandler;
import com.android.ide.common.api.Margins;
import com.android.ide.common.api.Rect;
import com.android.ide.eclipse.adt.internal.editors.formatting.XmlFormatPreferences;
import com.android.ide.eclipse.adt.internal.editors.formatting.XmlFormatStyle;
import com.android.ide.eclipse.adt.internal.editors.formatting.XmlPrettyPrinter;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.google.common.base.Splitter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;

/** Test/mock implementation of {@link INode} */
@SuppressWarnings("javadoc")
public class TestNode implements INode {
private TestNode mParent;

//Synthetic comment -- @@ -193,8 +214,9 @@

@Override
public String toString() {
        String id = getStringAttr(ANDROID_URI, ATTR_ID);
        return "TestNode [id=" + (id != null ? id : "?") + ", fqn=" + mFqcn + ", infos="
                + mAttributeInfos + ", attributes=" + mAttributes + ", bounds=" + mBounds + "]";
}

@Override
//Synthetic comment -- @@ -215,4 +237,197 @@
public void setAttributeSources(List<String> attributeSources) {
mAttributeSources = attributeSources;
}

    /** Create a test node from the given XML */
    public static TestNode createFromXml(String xml) {
        Document document = DomUtilities.parseDocument(xml, false);
        assertNotNull(document);
        assertNotNull(document.getDocumentElement());

        return createFromNode(document.getDocumentElement());
    }

    public static String toXml(TestNode node) {
        assertTrue("This method only works with nodes constructed from XML",
                node instanceof TestXmlNode);
        Document document = ((TestXmlNode) node).mElement.getOwnerDocument();
        // Insert new whitespace nodes etc
        String xml = dumpDocument(document);
        document = DomUtilities.parseDocument(xml, false);

        XmlPrettyPrinter printer = new XmlPrettyPrinter(XmlFormatPreferences.create(),
                XmlFormatStyle.LAYOUT, "\n");
        StringBuilder sb = new StringBuilder(1000);
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        printer.prettyPrint(-1, document, null, null, sb, false);
        return sb.toString();
    }

    @SuppressWarnings("deprecation")
    private static String dumpDocument(Document document) {
        // Diagnostics: print out the XML that we're about to render
        org.apache.xml.serialize.OutputFormat outputFormat =
                new org.apache.xml.serialize.OutputFormat(
                        "XML", "ISO-8859-1", true); //$NON-NLS-1$ //$NON-NLS-2$
        outputFormat.setIndent(2);
        outputFormat.setLineWidth(100);
        outputFormat.setIndenting(true);
        outputFormat.setOmitXMLDeclaration(true);
        outputFormat.setOmitDocumentType(true);
        StringWriter stringWriter = new StringWriter();
        // Using FQN here to avoid having an import above, which will result
        // in a deprecation warning, and there isn't a way to annotate a single
        // import element with a SuppressWarnings.
        org.apache.xml.serialize.XMLSerializer serializer =
                new org.apache.xml.serialize.XMLSerializer(stringWriter, outputFormat);
        serializer.setNamespaces(true);
        try {
            serializer.serialize(document.getDocumentElement());
            return stringWriter.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static TestNode createFromNode(Element element) {
        String fqcn = ANDROID_WIDGET_PREFIX + element.getTagName();
        TestNode node = new TestXmlNode(fqcn, element);

        for (Element child : DomUtilities.getChildren(element)) {
            node.add(createFromNode(child));
        }

        return node;
    }

    @Nullable
    public static TestNode findById(TestNode node, String id) {
        id = BaseLayoutRule.stripIdPrefix(id);
        return node.findById(id);
    }

    private TestNode findById(String targetId) {
        String id = getStringAttr(ANDROID_URI, ATTR_ID);
        if (id != null && targetId.equals(BaseLayoutRule.stripIdPrefix(id))) {
            return this;
        }

        for (TestNode child : mChildren) {
            TestNode result = child.findById(targetId);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    private static String getTagName(String fqcn) {
        return fqcn.substring(fqcn.lastIndexOf('.') + 1);
    }

    private static class TestXmlNode extends TestNode {
        private final Element mElement;

        public TestXmlNode(String fqcn, Element element) {
            super(fqcn);
            mElement = element;
        }

        @Override
        public boolean setAttribute(String uri, String localName, String value) {
            mElement.setAttributeNS(uri, localName, value);
            return super.setAttribute(uri, localName, value);
        }

        @Override
        public INode appendChild(String viewFqcn) {
            Element child = mElement.getOwnerDocument().createElement(getTagName(viewFqcn));
            mElement.appendChild(child);
            return new TestXmlNode(viewFqcn, child);
        }

        @Override
        public INode insertChildAt(String viewFqcn, int index) {
            if (index == -1) {
                return appendChild(viewFqcn);
            }
            Element child = mElement.getOwnerDocument().createElement(getTagName(viewFqcn));
            List<Element> children = DomUtilities.getChildren(mElement);
            if (children.size() >= index) {
                Element before = children.get(index);
                mElement.insertBefore(child, before);
            } else {
                fail("Unexpected index");
                mElement.appendChild(child);
            }
            return new TestXmlNode(viewFqcn, child);
        }

        @Override
        public String getStringAttr(String uri, String name) {
            String value;
            if (uri == null) {
                value = mElement.getAttribute(name);
            } else {
                value = mElement.getAttributeNS(uri, name);
            }
            if (value.isEmpty()) {
                value = null;
            }

            return value;
        }

        @Override
        public void removeChild(INode node) {
            assert node instanceof TestXmlNode;
            mElement.removeChild(((TestXmlNode) node).mElement);
        }

        @Override
        public void removeChild(int index) {
            List<Element> children = DomUtilities.getChildren(mElement);
            assertTrue(index < children.size());
            Element oldChild = children.get(index);
            mElement.removeChild(oldChild);
        }
    }

    // Recursively initialize this node with the bounds specified in the given hierarchy
    // dump (from ViewHierarchy's DUMP_INFO flag
    public void assignBounds(String bounds) {
        Iterable<String> split = Splitter.on('\n').trimResults().split(bounds);
        assignBounds(split.iterator());
    }

    private void assignBounds(Iterator<String> iterator) {
        assertTrue(iterator.hasNext());
        String desc = iterator.next();

        Pattern pattern = Pattern.compile("^\\s*(.+)\\s+\\[(.+)\\]\\s*(<.+>)?\\s*(\\S+)?\\s*$");
        Matcher matcher = pattern.matcher(desc);
        assertTrue(matcher.matches());
        String fqn = matcher.group(1);
        assertEquals(getFqcn(), fqn);
        String boundsString = matcher.group(2);
        String[] bounds = boundsString.split(",");
        assertEquals(boundsString, 4, bounds.length);
        try {
            int left = Integer.parseInt(bounds[0]);
            int top = Integer.parseInt(bounds[1]);
            int right = Integer.parseInt(bounds[2]);
            int bottom = Integer.parseInt(bounds[3]);
            mBounds = new Rect(left, top, right - left, bottom - top);
        } catch (NumberFormatException nufe) {
            Assert.fail(nufe.getLocalizedMessage());
        }
        String tag = matcher.group(3);

        for (INode child : getChildren()) {
            assertTrue(iterator.hasNext());
            ((TestNode) child).assignBounds(iterator);
        }
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/grid/GridModelTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/grid/GridModelTest.java
//Synthetic comment -- index 680b7ca..f3405c1 100644

//Synthetic comment -- @@ -15,15 +15,24 @@
*/
package com.android.ide.common.layout.grid;

import static com.android.ide.common.layout.LayoutConstants.ATTR_COLUMN_COUNT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_COLUMN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_COLUMN_SPAN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ROW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_BUTTON;
import static com.android.util.XmlUtils.ANDROID_URI;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.Rect;
import com.android.ide.common.layout.LayoutTestBase;
import com.android.ide.common.layout.TestNode;
import com.android.ide.common.layout.grid.GridModel.ViewData;

import java.util.Arrays;
import java.util.Collections;


@SuppressWarnings("javadoc")
public class GridModelTest extends LayoutTestBase {
public void testRemoveFlag() {
assertEquals("left", GridModel.removeFlag("top", "top|left"));
//Synthetic comment -- @@ -38,7 +47,7 @@
TestNode targetNode = TestNode.create("android.widget.GridLayout").id("@+id/GridLayout1")
.bounds(new Rect(0, 0, 240, 480)).set(ANDROID_URI, ATTR_COLUMN_COUNT, "3");

        GridModel model = GridModel.get(null, targetNode, null);
assertEquals(3, model.declaredColumnCount);
assertEquals(1, model.actualColumnCount);
assertEquals(1, model.actualRowCount);
//Synthetic comment -- @@ -48,9 +57,796 @@
targetNode.add(TestNode.create(FQCN_BUTTON).id("@+id/Button3"));
targetNode.add(TestNode.create(FQCN_BUTTON).id("@+id/Button4"));

        model = GridModel.get(null, targetNode, null);
assertEquals(3, model.declaredColumnCount);
assertEquals(3, model.actualColumnCount);
assertEquals(2, model.actualRowCount);
}

    public void testSplitColumn() {
        TestNode targetNode = TestNode.create("android.widget.GridLayout").id("@+id/GridLayout1")
                .bounds(new Rect(0, 0, 240, 480)).set(ANDROID_URI, ATTR_COLUMN_COUNT, "3");
        TestNode b1 = TestNode.create(FQCN_BUTTON).id("@+id/Button1");
        TestNode b2 = TestNode.create(FQCN_BUTTON).id("@+id/Button2");
        TestNode b3 = TestNode.create(FQCN_BUTTON).id("@+id/Button3");
        TestNode b4 = TestNode.create(FQCN_BUTTON).id("@+id/Button4");
        targetNode.add(b1);
        targetNode.add(b2);
        targetNode.add(b3);
        targetNode.add(b4);
        b4.setAttribute(ANDROID_URI, ATTR_LAYOUT_COLUMN_SPAN, "2");

        GridModel model = GridModel.get(new LayoutTestBase.TestRulesEngine(targetNode.getFqcn()),
                targetNode, null);
        assertEquals(3, model.declaredColumnCount);
        assertEquals(3, model.actualColumnCount);
        assertEquals(2, model.actualRowCount);

        model.applyPositionAttributes();
        assertEquals("0", b1.getStringAttr(ANDROID_URI, ATTR_LAYOUT_COLUMN));
        assertEquals("0", b1.getStringAttr(ANDROID_URI, ATTR_LAYOUT_ROW));

        assertEquals("1", b2.getStringAttr(ANDROID_URI, ATTR_LAYOUT_COLUMN));
        assertEquals("0", b2.getStringAttr(ANDROID_URI, ATTR_LAYOUT_ROW));

        assertEquals("2", b3.getStringAttr(ANDROID_URI, ATTR_LAYOUT_COLUMN));
        assertEquals("0", b3.getStringAttr(ANDROID_URI, ATTR_LAYOUT_ROW));

        assertEquals("0", b4.getStringAttr(ANDROID_URI, ATTR_LAYOUT_COLUMN));
        assertEquals("1", b4.getStringAttr(ANDROID_URI, ATTR_LAYOUT_ROW));
        assertEquals("2", b4.getStringAttr(ANDROID_URI, ATTR_LAYOUT_COLUMN_SPAN));

        model.splitColumn(1, false /*insertMarginColumn*/, 100 /*columnWidthDp*/, 300 /* x */);
        model.applyPositionAttributes();

        assertEquals(4, model.declaredColumnCount);
        assertEquals(4, model.actualColumnCount);
        assertEquals(2, model.actualRowCount);

        assertEquals("0", b1.getStringAttr(ANDROID_URI, ATTR_LAYOUT_COLUMN));
        assertEquals("0", b1.getStringAttr(ANDROID_URI, ATTR_LAYOUT_ROW));

        assertEquals("1", b2.getStringAttr(ANDROID_URI, ATTR_LAYOUT_COLUMN));
        assertEquals("2", b2.getStringAttr(ANDROID_URI, ATTR_LAYOUT_COLUMN_SPAN));
        assertEquals("0", b2.getStringAttr(ANDROID_URI, ATTR_LAYOUT_ROW));

        assertEquals("3", b3.getStringAttr(ANDROID_URI, ATTR_LAYOUT_COLUMN));
        assertEquals("0", b3.getStringAttr(ANDROID_URI, ATTR_LAYOUT_ROW));

        assertEquals("0", b4.getStringAttr(ANDROID_URI, ATTR_LAYOUT_COLUMN));
        assertEquals("1", b4.getStringAttr(ANDROID_URI, ATTR_LAYOUT_ROW));
        assertEquals("3", b4.getStringAttr(ANDROID_URI, ATTR_LAYOUT_COLUMN_SPAN));
    }

    public void testDeletion1() {
        String xml =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<GridLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"match_parent\"\n" +
            "    android:columnCount=\"4\" >\n" +
            "\n" +
            "    <Button\n" +
            "        android:id=\"@+id/button1\"\n" +
            "        android:layout_column=\"1\"\n" +
            "        android:layout_gravity=\"left|top\"\n" +
            "        android:layout_row=\"1\"\n" +
            "        android:text=\"Button\" />\n" +
            "\n" +
            "    <TextView\n" +
            "        android:id=\"@+id/TextView1\"\n" +
            "        android:layout_column=\"3\"\n" +
            "        android:layout_gravity=\"left|top\"\n" +
            "        android:layout_row=\"1\"\n" +
            "        android:text=\"Text\" />\n" +
            "\n" +
            "    <Space\n" +
            "        android:id=\"@+id/wspace1\"\n" +
            "        android:layout_width=\"21dp\"\n" +
            "        android:layout_height=\"1dp\"\n" +
            "        android:layout_column=\"0\"\n" +
            "        android:layout_row=\"0\" />\n" +
            "\n" +
            "    <Space\n" +
            "        android:id=\"@+id/hspace1\"\n" +
            "        android:layout_width=\"1dp\"\n" +
            "        android:layout_height=\"55dp\"\n" +
            "        android:layout_column=\"0\"\n" +
            "        android:layout_row=\"0\" />\n" +
            "\n" +
            "    <Space\n" +
            "        android:id=\"@+id/wspace2\"\n" +
            "        android:layout_width=\"10dp\"\n" +
            "        android:layout_height=\"1dp\"\n" +
            "        android:layout_column=\"2\"\n" +
            "        android:layout_row=\"0\" />\n" +
            "\n" +
            "</GridLayout>";

        TestNode targetNode = TestNode.createFromXml(xml);
        assertNotNull(targetNode);

        TestNode button1 = TestNode.findById(targetNode, "@+id/button1");
        TestNode textView1 = TestNode.findById(targetNode, "@+id/TextView1");
        TestNode wspace1 = TestNode.findById(targetNode, "@+id/wspace1");
        TestNode wspace2 = TestNode.findById(targetNode, "@+id/wspace2");
        TestNode hspace1 = TestNode.findById(targetNode, "@+id/hspace1");
        assertNotNull(wspace1);
        assertNotNull(hspace1);
        assertNotNull(wspace2);
        assertNotNull(button1);
        assertNotNull(textView1);

        // Assign some bounds such that the model makes sense when merging spacer sizes
        // TODO: MAke test utility method to automatically assign half divisions!!
        button1.bounds(new Rect(90, 10, 100, 40));
        textView1.bounds(new Rect(200, 10, 100, 40));
        wspace1.bounds(new Rect(0, 0, 90, 1));
        wspace1.bounds(new Rect(190, 0, 10, 1));
        hspace1.bounds(new Rect(0, 0, 1, 10));

        GridModel model = GridModel.get(new LayoutTestBase.TestRulesEngine(targetNode.getFqcn()),
                targetNode, null);
        assertEquals(4, model.declaredColumnCount);
        assertEquals(4, model.actualColumnCount);
        assertEquals(2, model.actualRowCount);

        ViewData textViewData = model.getView(textView1);
        assertEquals(3, textViewData.column);

        // Delete button1
        button1.getParent().removeChild(button1);
        model.onDeleted(Arrays.<INode>asList(button1));
        model.applyPositionAttributes();

        assertEquals(2, model.declaredColumnCount);
        assertEquals(2, model.actualColumnCount);
        assertEquals(2, model.actualRowCount);
        assertNotNull(model.getView(textView1));
        assertNull(model.getView(button1));

        assertEquals(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<GridLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    android:layout_width=\"match_parent\"\n" +
                "    android:layout_height=\"match_parent\"\n" +
                "    android:columnCount=\"2\">\n" +
                "\n" +
                "    <TextView\n" +
                "        android:id=\"@+id/TextView1\"\n" +
                "        android:layout_column=\"1\"\n" +
                "        android:layout_gravity=\"left|top\"\n" +
                "        android:layout_row=\"1\"\n" +
                "        android:text=\"Text\">\n" +
                "    </TextView>\n" +
                "\n" +
                "    <Space\n" +
                "        android:id=\"@+id/wspace1\"\n" +
                "        android:layout_width=\"66dp\"\n" +
                "        android:layout_height=\"1dp\"\n" +
                "        android:layout_column=\"0\"\n" +
                "        android:layout_row=\"0\">\n" +
                "    </Space>\n" +
                "\n" +
                "    <Space\n" +
                "        android:id=\"@+id/hspace1\"\n" +
                "        android:layout_width=\"1dp\"\n" +
                "        android:layout_height=\"55dp\"\n" +
                "        android:layout_column=\"0\"\n" +
                "        android:layout_row=\"0\">\n" +
                "    </Space>\n" +
                "\n" +
                "</GridLayout>", TestNode.toXml(targetNode));

        // Delete textView1

        textView1.getParent().removeChild(textView1);
        model.onDeleted(Arrays.<INode>asList(textView1));
        model.applyPositionAttributes();

        assertEquals(2, model.declaredColumnCount);
        assertEquals(0, model.actualColumnCount);
        assertEquals(0, model.actualRowCount);
        assertNull(model.getView(textView1));
        assertNull(model.getView(button1));

        assertEquals(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<GridLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    android:layout_width=\"match_parent\"\n" +
                "    android:layout_height=\"match_parent\"\n" +
                "    android:columnCount=\"0\">\n" +
                "\n" +
                "</GridLayout>", TestNode.toXml(targetNode));

    }

    public void testDelete2() throws Exception {
        String xml =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<GridLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"match_parent\"\n" +
            "    android:columnCount=\"4\"\n" +
            "    android:orientation=\"vertical\" >\n" +
            "\n" +
            "    <Button\n" +
            "        android:id=\"@+id/button1\"\n" +
            "        android:layout_column=\"0\"\n" +
            "        android:layout_columnSpan=\"2\"\n" +
            "        android:layout_gravity=\"left|top\"\n" +
            "        android:layout_row=\"0\"\n" +
            "        android:text=\"Button1\" />\n" +
            "\n" +
            "    <Button\n" +
            "        android:id=\"@+id/button3\"\n" +
            "        android:layout_column=\"1\"\n" +
            "        android:layout_columnSpan=\"2\"\n" +
            "        android:layout_gravity=\"left|top\"\n" +
            "        android:layout_row=\"1\"\n" +
            "        android:text=\"Button2\" />\n" +
            "\n" +
            "    <Button\n" +
            "        android:id=\"@+id/button2\"\n" +
            "        android:layout_column=\"2\"\n" +
            "        android:layout_gravity=\"left|top\"\n" +
            "        android:layout_row=\"0\"\n" +
            "        android:text=\"Button3\" />\n" +
            "\n" +
            "    <Space\n" +
            "        android:id=\"@+id/spacer_177\"\n" +
            "        android:layout_width=\"46dp\"\n" +
            "        android:layout_height=\"1dp\"\n" +
            "        android:layout_column=\"0\"\n" +
            "        android:layout_row=\"0\" />\n" +
            "\n" +
            "</GridLayout>";

        TestNode targetNode = TestNode.createFromXml(xml);
        assertNotNull(targetNode);

        TestNode button1 = TestNode.findById(targetNode, "@+id/button1");
        TestNode button2 = TestNode.findById(targetNode, "@+id/button2");
        TestNode button3 = TestNode.findById(targetNode, "@+id/button3");
        TestNode hspacer = TestNode.findById(targetNode, "@+id/spacer_177");
        assertNotNull(button1);
        assertNotNull(button2);
        assertNotNull(button3);
        assertNotNull(hspacer);

        // Assign some bounds such that the model makes sense when merging spacer sizes
        // TODO: MAke test utility method to automatically assign half divisions!!
        button1.bounds(new Rect(0, 0, 100, 40));
        button2.bounds(new Rect(100, 0, 100, 40));
        button3.bounds(new Rect(50, 40, 100, 40));
        hspacer.bounds(new Rect(0, 0, 50, 1));

        GridModel model = GridModel.get(new LayoutTestBase.TestRulesEngine(targetNode.getFqcn()),
                targetNode, null);
        assertEquals(4, model.declaredColumnCount);
        assertEquals(3, model.actualColumnCount);
        assertEquals(2, model.actualRowCount);

        ViewData buttonData = model.getView(button1);
        assertEquals(0, buttonData.column);

        // Delete button1
        button1.getParent().removeChild(button1);
        model.onDeleted(Arrays.<INode>asList(button1));
        model.applyPositionAttributes();

        assertEquals(3, model.declaredColumnCount);
        assertEquals(3, model.actualColumnCount);
        assertEquals(2, model.actualRowCount);
        assertNull(model.getView(button1));

        assertEquals(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<GridLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    android:layout_width=\"match_parent\"\n" +
                "    android:layout_height=\"match_parent\"\n" +
                "    android:columnCount=\"3\"\n" +
                "    android:orientation=\"vertical\">\n" +
                "\n" +
                "    <Button\n" +
                "        android:id=\"@+id/button3\"\n" +
                "        android:layout_column=\"1\"\n" +
                "        android:layout_columnSpan=\"2\"\n" +
                "        android:layout_gravity=\"left|top\"\n" +
                "        android:layout_row=\"1\"\n" +
                "        android:text=\"Button2\">\n" +
                "    </Button>\n" +
                "\n" +
                "    <Button\n" +
                "        android:id=\"@+id/button2\"\n" +
                "        android:layout_column=\"2\"\n" +
                "        android:layout_gravity=\"left|top\"\n" +
                "        android:layout_row=\"0\"\n" +
                "        android:text=\"Button3\">\n" +
                "    </Button>\n" +
                "\n" +
                "    <Space\n" +
                "        android:id=\"@+id/spacer_177\"\n" +
                "        android:layout_width=\"46dp\"\n" +
                "        android:layout_height=\"1dp\"\n" +
                "        android:layout_column=\"0\"\n" +
                "        android:layout_row=\"0\">\n" +
                "    </Space>\n" +
                "\n" +
                "</GridLayout>", TestNode.toXml(targetNode));
    }

    public void testDelete3_INCOMPLETE() throws Exception {
        String xml =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<GridLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
            "  android:layout_width=\"match_parent\" android:layout_height=\"match_parent\"\n" +
            "  android:columnCount=\"6\">\n" +
            "  <Button android:id=\"@+id/button1\" android:layout_column=\"1\"\n" +
            "    android:layout_columnSpan=\"2\" android:layout_gravity=\"left|top\"\n" +
            "    android:layout_row=\"1\" android:layout_rowSpan=\"2\" android:text=\"Button\" />\n" +
            "  <TextView android:id=\"@+id/TextView1\" android:layout_column=\"4\"\n" +
            "    android:layout_gravity=\"left|top\" android:layout_row=\"1\"\n" +
            "    android:text=\"Text\" />\n" +
            "  <Button android:id=\"@+id/button3\" android:layout_column=\"5\"\n" +
            "    android:layout_gravity=\"left|top\" android:layout_row=\"2\"\n" +
            "    android:layout_rowSpan=\"2\" android:text=\"Button\" />\n" +
            "  <Button android:id=\"@+id/button2\" android:layout_column=\"2\"\n" +
            "    android:layout_columnSpan=\"3\" android:layout_gravity=\"left|top\"\n" +
            "    android:layout_row=\"4\" android:text=\"Button\" />\n" +
            "  <Space android:id=\"@+id/wspace1\" android:layout_width=\"21dp\"\n" +
            "    android:layout_height=\"1dp\" android:layout_column=\"0\"\n" +
            "    android:layout_row=\"0\" />\n" +
            "  <Space android:id=\"@+id/spacer_630\" android:layout_width=\"1dp\"\n" +
            "    android:layout_height=\"55dp\" android:layout_column=\"0\"\n" +
            "    android:layout_row=\"0\" />\n" +
            "  <Space android:id=\"@+id/wspace2\" android:layout_width=\"10dp\"\n" +
            "    android:layout_height=\"1dp\" android:layout_column=\"3\"\n" +
            "    android:layout_row=\"0\" />\n" +
            "  <Space android:id=\"@+id/spacer_619\" android:layout_width=\"59dp\"\n" +
            "    android:layout_height=\"1dp\" android:layout_column=\"1\"\n" +
            "    android:layout_row=\"0\" />\n" +
            "  <Space android:id=\"@+id/spacer_102\" android:layout_width=\"1dp\"\n" +
            "    android:layout_height=\"30dp\" android:layout_column=\"0\"\n" +
            "    android:layout_row=\"1\" />\n" +
            "  <Space android:id=\"@+id/spacer_109\" android:layout_width=\"1dp\"\n" +
            "    android:layout_height=\"28dp\" android:layout_column=\"0\"\n" +
            "    android:layout_row=\"2\" />\n" +
            "  <Space android:id=\"@+id/spacer_146\" android:layout_width=\"1dp\"\n" +
            "    android:layout_height=\"70dp\" android:layout_column=\"0\"\n" +
            "    android:layout_row=\"3\" />\n" +
            "</GridLayout>";
        TestNode targetNode = TestNode.createFromXml(xml);
        assertNotNull(targetNode);
        targetNode.assignBounds(
            "android.widget.GridLayout [0,109,480,800] <GridLayout>\n" +
            "    android.widget.Button [32,83,148,155] <Button> @+id/button1\n" +
            "    android.widget.TextView [163,83,205,109] <TextView> @+id/TextView1\n" +
            "    android.widget.Button [237,128,353,200] <Button> @+id/button3\n" +
            "    android.widget.Button [121,275,237,347] <Button> @+id/button2\n" +
            "    android.widget.Space [0,0,32,2] <Space> @+id/wspace1\n" +
            "    android.widget.Space [0,0,2,83] <Space> @+id/spacer_630\n" +
            "    android.widget.Space [148,0,163,2] <Space> @+id/wspace2\n" +
            "    android.widget.Space [32,0,121,2] <Space> @+id/spacer_619\n" +
            "    android.widget.Space [0,83,2,128] <Space> @+id/spacer_102\n" +
            "    android.widget.Space [0,128,2,170] <Space> @+id/spacer_109\n" +
            "    android.widget.Space [0,170,2,275] <Space> @+id/spacer_146\n");
        TestNode layout = TestNode.findById(targetNode, "@+id/GridLayout1");
        //TestNode button1 = TestNode.findById(targetNode, "@+id/button1");
        TestNode button2 = TestNode.findById(targetNode, "@+id/button2");
        TestNode button3 = TestNode.findById(targetNode, "@+id/button3");

        GridModel model = GridModel.get(new LayoutTestBase.TestRulesEngine(targetNode.getFqcn()),
                targetNode, null);
        assertEquals(6, model.declaredColumnCount);
        assertEquals(6, model.actualColumnCount);
        assertEquals(5, model.actualRowCount);

        // TODO: Delete button2 or button3: bad stuff happens visually
        fail("Finish test");
    }

    public void testDelete4_INCOMPLETE() {
        String xml = "" +
            "<GridLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
            "  xmlns:tools=\"http://schemas.android.com/tools\" " +
            "  android:layout_width=\"match_parent\"\n" +
            "  android:layout_height=\"match_parent\" android:columnCount=\"3\"\n" +
            "  android:gravity=\"center\" android:text=\"@string/hello_world\"\n" +
            "  tools:context=\".MainActivity\">\n" +
            "  <Button android:id=\"@+id/button2\" android:layout_column=\"1\"\n" +
            "    android:layout_columnSpan=\"2\" android:layout_gravity=\"left|top\"\n" +
            "    android:layout_row=\"1\" android:text=\"Button\" />\n" +
            "  <Button android:id=\"@+id/button1\" android:layout_column=\"1\"\n" +
            "    android:layout_columnSpan=\"2\" android:layout_gravity=\"left|top\"\n" +
            "    android:layout_row=\"3\" android:text=\"Button\" />\n" +
            "  <Space android:id=\"@+id/spacer_167\" android:layout_width=\"74dp\"\n" +
            "    android:layout_height=\"1dp\" android:layout_column=\"0\"\n" +
            "    android:layout_row=\"0\" />\n" +
            "  <Space android:id=\"@+id/spacer_133\" android:layout_width=\"1dp\"\n" +
            "    android:layout_height=\"21dp\" android:layout_column=\"0\"\n" +
            "    android:layout_row=\"0\" />\n" +
            "  <Space android:id=\"@+id/spacer_142\" android:layout_width=\"1dp\"\n" +
            "    android:layout_height=\"26dp\" android:layout_column=\"0\"\n" +
            "    android:layout_row=\"2\" />\n" +
            "  <Space android:id=\"@+id/spacer_673\" android:layout_width=\"43dp\"\n" +
            "    android:layout_height=\"1dp\" android:layout_column=\"1\"\n" +
            "    android:layout_row=\"0\" />\n" +
            "  <Space android:id=\"@+id/spacer_110\" android:layout_width=\"202dp\"\n" +
            "    android:layout_height=\"15dp\" android:layout_column=\"2\" />\n" +
            "</GridLayout>";
        TestNode targetNode = TestNode.createFromXml(xml);
        assertNotNull(targetNode);
        targetNode.assignBounds(
                "android.widget.GridLayout [0,109,480,800] <GridLayout>\n" +
                "    android.widget.Button [111,32,227,104] <Button> @+id/button2\n" +
                "    android.widget.Button [111,143,227,215] <Button> @+id/button1\n" +
                "    android.widget.Space [0,0,111,2] <Space> @+id/spacer_167\n" +
                "    android.widget.Space [0,0,2,32] <Space> @+id/spacer_133\n" +
                "    android.widget.Space [0,104,2,143] <Space> @+id/spacer_142\n" +
                "    android.widget.Space [111,0,176,2] <Space> @+id/spacer_673\n" +
                "    android.widget.Space [176,668,479,691] <Space> @+id/spacer_110");


        // Remove button2; button1 shifts to the right!

        //TestNode layout = TestNode.findById(targetNode, "@+id/GridLayout1");
        //TestNode button1 = TestNode.findById(targetNode, "@+id/button1");
        TestNode button2 = TestNode.findById(targetNode, "@+id/button2");
        TestNode button3 = TestNode.findById(targetNode, "@+id/button3");
        assertEquals(new Rect(111, 32, 227 - 111, 104 - 32), button2.getBounds());

        GridModel model = GridModel.get(new LayoutTestBase.TestRulesEngine(targetNode.getFqcn()),
                targetNode, null);
        assertEquals(3, model.declaredColumnCount);
        assertEquals(3, model.actualColumnCount);
        assertEquals(4, model.actualRowCount);
        fail("Finish test");
    }

    public void testDelete5_INCOMPLETE() {
        String xml =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<GridLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "  android:id=\"@+id/GridLayout1\" android:layout_width=\"match_parent\"\n" +
                "  android:layout_height=\"match_parent\" android:columnCount=\"4\"\n" +
                "  android:orientation=\"vertical\">\n" +
                "  <Button android:id=\"@+id/button1\" android:layout_column=\"0\"\n" +
                "    android:layout_gravity=\"center_horizontal|bottom\"\n" +
                "    android:layout_row=\"0\" android:text=\"Button\" />\n" +
                "  <Space android:layout_width=\"66dp\" android:layout_height=\"1dp\"\n" +
                "    android:layout_column=\"0\" android:layout_row=\"0\" />\n" +
                "  <Button android:id=\"@+id/button3\" android:layout_column=\"2\"\n" +
                "    android:layout_gravity=\"left|bottom\" android:layout_row=\"0\"\n" +
                "    android:text=\"Button\" />\n" +
                "  <Button android:id=\"@+id/button2\" android:layout_column=\"3\"\n" +
                "    android:layout_columnSpan=\"2\" android:layout_gravity=\"left|bottom\"\n" +
                "    android:layout_row=\"0\" android:text=\"Button\" />\n" +
                "  <Space android:id=\"@+id/spacer_109\" android:layout_width=\"51dp\"\n" +
                "    android:layout_height=\"1dp\" android:layout_column=\"1\"\n" +
                "    android:layout_row=\"0\" />\n" +
                "  <Space android:layout_width=\"129dp\" android:layout_height=\"1dp\"\n" +
                "    android:layout_column=\"2\" android:layout_row=\"0\" />\n" +
                "  <Space android:layout_width=\"62dp\" android:layout_height=\"1dp\"\n" +
                "    android:layout_column=\"3\" android:layout_row=\"0\" />\n" +
                "  <Space android:id=\"@+id/spacer_397\" android:layout_width=\"1dp\"\n" +
                "    android:layout_height=\"103dp\" android:layout_column=\"0\"\n" +
                "    android:layout_row=\"0\" />\n" +
                "  <Space android:layout_width=\"1dp\" android:layout_height=\"356dp\"\n" +
                "    android:layout_column=\"0\" android:layout_row=\"1\" />\n" +
                "</GridLayout>";
        TestNode targetNode = TestNode.createFromXml(xml);
        assertNotNull(targetNode);

        targetNode.assignBounds(
                "android.widget.GridLayout [0,109,480,800] <GridLayout> @+id/GridLayout1\n" +
                "    android.widget.Button [0,83,116,155] <Button> @+id/button1\n" +
                "    android.widget.Space [0,0,99,2] <Space>\n" +
                "    android.widget.Button [193,83,309,155] <Button> @+id/button3\n" +
                "    android.widget.Button [387,83,503,155] <Button> @+id/button2\n" +
                "    android.widget.Space [116,0,193,2] <Space> @+id/spacer_109\n" +
                "    android.widget.Space [193,0,387,2] <Space>\n" +
                "    android.widget.Space [387,0,480,2] <Space>\n" +
                "    android.widget.Space [0,0,2,155] <Space> @+id/spacer_397\n" +
                "    android.widget.Space [0,155,2,689] <Space>");

        // Delete button3. This causes an array out of bounds exception currently.

        //TestNode layout = TestNode.findById(targetNode, "@+id/GridLayout1");
        //TestNode button1 = TestNode.findById(targetNode, "@+id/button1");
        TestNode button2 = TestNode.findById(targetNode, "@+id/button2");
        TestNode button3 = TestNode.findById(targetNode, "@+id/button3");
        assertEquals(new Rect(387, 83, 503 - 387, 155- 83), button2.getBounds());

        GridModel model = GridModel.get(new LayoutTestBase.TestRulesEngine(targetNode.getFqcn()),
                targetNode, null);
        assertEquals(4, model.declaredColumnCount);
        assertEquals(4, model.actualColumnCount);
        assertEquals(2, model.actualRowCount);

        model.onDeleted(Collections.<INode>singletonList(button3));
        // Exception fixed; todo: Test that the model updates are correct.

        assertEquals(3, model.declaredColumnCount);
        assertEquals(3, model.actualColumnCount);
        assertEquals(2, model.actualRowCount);

        fail("Finish test");
    }

    public void testInsert1() throws Exception {
        String xml =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<GridLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
            "    android:id=\"@+id/GridLayout1\"\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"match_parent\"\n" +
            "    android:columnCount=\"4\"\n" +
            "    android:orientation=\"vertical\" >\n" +
            "\n" +
            "    <Button\n" +
            "        android:id=\"@+id/button1\"\n" +
            "        android:layout_column=\"0\"\n" +
            "        android:layout_columnSpan=\"4\"\n" +
            "        android:layout_gravity=\"center_horizontal|bottom\"\n" +
            "        android:layout_row=\"0\"\n" +
            "        android:text=\"Button\" />\n" +
            "\n" +
            "    <Button\n" +
            "        android:id=\"@+id/button2\"\n" +
            "        android:layout_column=\"2\"\n" +
            "        android:layout_gravity=\"left|top\"\n" +
            "        android:layout_row=\"1\"\n" +
            "        android:text=\"Button\" />\n" +
            "\n" +
            "    <Button\n" +
            "        android:id=\"@+id/button3\"\n" +
            "        android:layout_column=\"3\"\n" +
            "        android:layout_gravity=\"left|top\"\n" +
            "        android:layout_row=\"1\"\n" +
            "        android:text=\"Button\" />\n" +
            "\n" +
            "    <Space\n" +
            "        android:id=\"@+id/spacer_393\"\n" +
            "        android:layout_width=\"81dp\"\n" +
            "        android:layout_height=\"1dp\"\n" +
            "        android:layout_column=\"1\"\n" +
            "        android:layout_row=\"0\" />\n" +
            "\n" +
            "    <Space\n" +
            "        android:id=\"@+id/spacer_397\"\n" +
            "        android:layout_width=\"1dp\"\n" +
            "        android:layout_height=\"103dp\"\n" +
            "        android:layout_column=\"0\"\n" +
            "        android:layout_row=\"0\" />\n" +
            "\n" +
            "</GridLayout>";

        TestNode targetNode = TestNode.createFromXml(xml);
        assertNotNull(targetNode);

        TestNode layout = TestNode.findById(targetNode, "@+id/GridLayout1");
        TestNode button1 = TestNode.findById(targetNode, "@+id/button1");
        TestNode button2 = TestNode.findById(targetNode, "@+id/button2");
        TestNode button3 = TestNode.findById(targetNode, "@+id/button3");
        TestNode hspacer = TestNode.findById(targetNode, "@+id/spacer_393");
        TestNode vspacer = TestNode.findById(targetNode, "@+id/spacer_397");
        assertNotNull(layout);
        assertNotNull(button1);
        assertNotNull(button2);
        assertNotNull(button3);
        assertNotNull(hspacer);

        // Obtained by setting ViewHierarchy.DUMP_INFO=true:
        layout.bounds(new Rect(0, 109, 480, 800-109));
        button1.bounds(new Rect(182, 83, 298-182, 155-83));
        button2.bounds(new Rect(124, 155, 240-124, 227-155));
        button3.bounds(new Rect(240, 155, 356-240, 227-155));
        hspacer.bounds(new Rect(2, 0, 124-2, 2));
        vspacer.bounds(new Rect(0, 0, 2, 155));

        GridModel model = GridModel.get(new LayoutTestBase.TestRulesEngine(targetNode.getFqcn()),
                targetNode, null);
        assertEquals(4, model.declaredColumnCount);
        assertEquals(4, model.actualColumnCount);
        assertEquals(2, model.actualRowCount);


        model.splitColumn(1, false, 21, 32);
        int index = model.getInsertIndex(2, 1);
        GridModel.ViewData next = model.getView(index);
        INode newChild = targetNode.insertChildAt(FQCN_BUTTON, index);
        next.applyPositionAttributes();
        model.setGridAttribute(newChild, ATTR_LAYOUT_COLUMN, 1);
        model.setGridAttribute(newChild, ATTR_LAYOUT_COLUMN_SPAN, 3);
    }

    public void testInsert2() throws Exception {
        // Drop into a view where there is a centered view: when dropping to the right of
        // it (on a row further down), ensure that the row span is increased for the
        // non-left-justified centered view which does not horizontally overlap the view
        String xml =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<GridLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    android:id=\"@+id/GridLayout1\"\n" +
                "    android:layout_width=\"match_parent\"\n" +
                "    android:layout_height=\"match_parent\"\n" +
                "    android:columnCount=\"3\"\n" +
                "    android:orientation=\"vertical\" >\n" +
                "\n" +
                "    <Button\n" +
                "        android:id=\"@+id/button1\"\n" +
                "        android:layout_column=\"0\"\n" +
                "        android:layout_columnSpan=\"3\"\n" +
                "        android:layout_gravity=\"center_horizontal|bottom\"\n" +
                "        android:layout_row=\"0\"\n" +
                "        android:text=\"Button\" />\n" +
                "\n" +
                "    <Button\n" +
                "        android:id=\"@+id/button2\"\n" +
                "        android:layout_column=\"1\"\n" +
                "        android:layout_gravity=\"left|top\"\n" +
                "        android:layout_row=\"1\"\n" +
                "        android:text=\"Button\" />\n" +
                "\n" +
                "    <Button\n" +
                "        android:id=\"@+id/button3\"\n" +
                "        android:layout_column=\"2\"\n" +
                "        android:layout_gravity=\"left|top\"\n" +
                "        android:layout_row=\"1\"\n" +
                "        android:text=\"Button\" />\n" +
                "\n" +
                "    <Space\n" +
                "        android:id=\"@+id/spacer_393\"\n" +
                "        android:layout_width=\"81dp\"\n" +
                "        android:layout_height=\"1dp\"\n" +
                "        android:layout_column=\"0\"\n" +
                "        android:layout_row=\"0\" />\n" +
                "\n" +
                "    \n" +
                "    <Space\n" +
                "        android:id=\"@+id/spacer_397\"\n" +
                "        android:layout_width=\"1dp\"\n" +
                "        android:layout_height=\"103dp\"\n" +
                "        android:layout_column=\"0\"\n" +
                "        android:layout_row=\"0\" />\n" +
                "\n" +
                "    \n" +
                "</GridLayout>";

        TestNode targetNode = TestNode.createFromXml(xml);
        assertNotNull(targetNode);

        TestNode layout = TestNode.findById(targetNode, "@+id/GridLayout1");
        TestNode button1 = TestNode.findById(targetNode, "@+id/button1");
        TestNode button2 = TestNode.findById(targetNode, "@+id/button2");
        TestNode button3 = TestNode.findById(targetNode, "@+id/button3");
        TestNode hspacer = TestNode.findById(targetNode, "@+id/spacer_393");
        TestNode vspacer = TestNode.findById(targetNode, "@+id/spacer_397");
        assertNotNull(layout);
        assertNotNull(button1);
        assertNotNull(button2);
        assertNotNull(button3);
        assertNotNull(hspacer);

        // Obtained by setting ViewHierarchy.DUMP_INFO=true:
        layout.bounds(new Rect(0, 109, 480, 800-109));
        button1.bounds(new Rect(182, 83, 298-182, 155-83));
        button2.bounds(new Rect(122, 155, 238-122, 227-155));
        button3.bounds(new Rect(238, 155, 354-238, 227-155));
        hspacer.bounds(new Rect(0, 0, 122, 2));
        vspacer.bounds(new Rect(0, 0, 2, 155));

        GridModel model = GridModel.get(new LayoutTestBase.TestRulesEngine(targetNode.getFqcn()),
                targetNode, null);
        assertEquals(3, model.declaredColumnCount);
        assertEquals(3, model.actualColumnCount);
        assertEquals(2, model.actualRowCount);

        ViewData view = model.getView(button1);
        assertNotNull(view);
        assertEquals(0, view.column);
        assertEquals(3, view.columnSpan);
        assertEquals("3", view.node.getStringAttr(ANDROID_URI, ATTR_LAYOUT_COLUMN_SPAN));

        model.splitColumn(3, false, 53, 318);
        assertEquals(0, view.column);
        assertEquals(4, view.columnSpan);
        assertEquals("4", view.node.getStringAttr(ANDROID_URI, ATTR_LAYOUT_COLUMN_SPAN));
    }

    public void testInsert3_BROKEN() throws Exception {
        // Check that when we insert a new gap column near an existing column, the
        // view in that new column does not get moved
        String xml =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<GridLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    android:id=\"@+id/GridLayout1\"\n" +
                "    android:layout_width=\"match_parent\"\n" +
                "    android:layout_height=\"match_parent\"\n" +
                "    android:columnCount=\"3\"\n" +
                "    android:orientation=\"vertical\" >\n" +
                "\n" +
                "    <Button\n" +
                "        android:id=\"@+id/button1\"\n" +
                "        android:layout_column=\"0\"\n" +
                "        android:layout_columnSpan=\"3\"\n" +
                "        android:layout_gravity=\"center_horizontal|bottom\"\n" +
                "        android:layout_row=\"0\"\n" +
                "        android:text=\"Button\" />\n" +
                "\n" +
                "    <Button\n" +
                "        android:id=\"@+id/button2\"\n" +
                "        android:layout_column=\"1\"\n" +
                "        android:layout_gravity=\"left|top\"\n" +
                "        android:layout_row=\"1\"\n" +
                "        android:text=\"Button\" />\n" +
                "\n" +
                "    <Button\n" +
                "        android:id=\"@+id/button3\"\n" +
                "        android:layout_column=\"2\"\n" +
                "        android:layout_gravity=\"left|top\"\n" +
                "        android:layout_row=\"1\"\n" +
                "        android:text=\"Button\" />\n" +
                "\n" +
                "    <Space\n" +
                "        android:id=\"@+id/spacer_393\"\n" +
                "        android:layout_width=\"81dp\"\n" +
                "        android:layout_height=\"1dp\"\n" +
                "        android:layout_column=\"0\"\n" +
                "        android:layout_row=\"0\" />\n" +
                "\n" +
                "    \n" +
                "    <Space\n" +
                "        android:id=\"@+id/spacer_397\"\n" +
                "        android:layout_width=\"1dp\"\n" +
                "        android:layout_height=\"103dp\"\n" +
                "        android:layout_column=\"0\"\n" +
                "        android:layout_row=\"0\" />\n" +
                "\n" +
                "    \n" +
                "</GridLayout>";

        TestNode targetNode = TestNode.createFromXml(xml);
        assertNotNull(targetNode);

        TestNode layout = TestNode.findById(targetNode, "@+id/GridLayout1");
        TestNode button1 = TestNode.findById(targetNode, "@+id/button1");
        TestNode button2 = TestNode.findById(targetNode, "@+id/button2");
        TestNode button3 = TestNode.findById(targetNode, "@+id/button3");
        TestNode hspacer = TestNode.findById(targetNode, "@+id/spacer_393");
        TestNode vspacer = TestNode.findById(targetNode, "@+id/spacer_397");
        assertNotNull(layout);
        assertNotNull(button1);
        assertNotNull(button2);
        assertNotNull(button3);
        assertNotNull(hspacer);

        // Obtained by setting ViewHierarchy.DUMP_INFO=true:
        layout.bounds(new Rect(0, 109, 480, 800-109));
        button1.bounds(new Rect(182, 83, 298-182, 155-83));
        button2.bounds(new Rect(122, 155, 238-122, 227-155));
        button3.bounds(new Rect(238, 155, 354-238, 227-155));
        hspacer.bounds(new Rect(0, 0, 122, 2));
        vspacer.bounds(new Rect(0, 0, 2, 155));

        GridModel model = GridModel.get(new LayoutTestBase.TestRulesEngine(targetNode.getFqcn()),
                targetNode, null);
        assertEquals(3, model.declaredColumnCount);
        assertEquals(3, model.actualColumnCount);
        assertEquals(2, model.actualRowCount);

        ViewData view = model.getView(button3);
        assertNotNull(view);
        assertEquals(2, view.column);
        assertEquals(1, view.columnSpan);
        assertNull("1", view.node.getStringAttr(ANDROID_URI, ATTR_LAYOUT_COLUMN_SPAN));

        model.splitColumn(2, true, 10, 253);
        // TODO: Finish this test: Assert that the cells are in the right place
        //assertEquals(4, view.column);
        //assertEquals(1, view.columnSpan);
        //assertEquals("4", view.node.getStringAttr(ANDROID_URI, ATTR_LAYOUT_COLUMN_SPAN));
        fail("Finish test");
    }
}








//Synthetic comment -- diff --git a/rule_api/src/com/android/ide/common/api/IClientRulesEngine.java b/rule_api/src/com/android/ide/common/api/IClientRulesEngine.java
//Synthetic comment -- index 329f38c..08630c7 100644

//Synthetic comment -- @@ -44,6 +44,15 @@
String getFqcn();

/**
     * Returns the most recently rendered View object for this node, if any.
     *
     * @param node the node to look up the view object for
     * @return the corresponding view object, or null
     */
    @Nullable
    Object getViewObject(@NonNull INode node);

    /**
* Prints a debug line in the Eclipse console using the ADT formatter.
*
* @param msg A String format message.







