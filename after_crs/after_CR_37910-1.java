/*GridLayout bug fix

Handle baseline alignment better

Change-Id:I331ebd2617612479eb686a456bd6f862f5acd7ea*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridDropHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridDropHandler.java
//Synthetic comment -- index 6d938fb..0292429 100644

//Synthetic comment -- @@ -650,7 +650,12 @@
gravity |= GravityHelper.GRAVITY_CENTER_HORIZ;
}
mGrid.setGridAttribute(newChild, ATTR_LAYOUT_COLUMN, column);
        if (mRowMatch.type == SegmentType.BASELINE) {
            // There *is* no baseline gravity constant, instead, leave the
            // vertical gravity unspecified and GridLayout will treat it as
            // baseline alignment
            //gravity |= GravityHelper.GRAVITY_BASELINE;
        } else if (mRowMatch.type == SegmentType.BOTTOM) {
gravity |= GravityHelper.GRAVITY_BOTTOM;
} else if (mRowMatch.type == SegmentType.CENTER_VERTICAL) {
gravity |= GravityHelper.GRAVITY_CENTER_VERT;
//Synthetic comment -- @@ -664,7 +669,13 @@
if (!GravityHelper.isConstrainedHorizontally(gravity)) {
gravity |= GravityHelper.GRAVITY_LEFT;
}
        if (!GravityHelper.isConstrainedVertically(gravity)
                // There is no baseline constant, so we have to leave it unconstrained instead
                && mRowMatch.type != SegmentType.BASELINE
                // You also can't baseline align one element with another that has vertical
                // alignment top or bottom, so when we first "freely" place views (e.g.
                // at a particular y location), also place it freely (no constraint).
                && !mRowMatch.createCell) {
gravity |= GravityHelper.GRAVITY_TOP;
}
mGrid.setGridAttribute(newChild, ATTR_LAYOUT_GRAVITY, getGravity(gravity));







