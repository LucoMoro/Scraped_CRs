/*GridLayout bug fix

Handle baseline alignment better

Change-Id:I331ebd2617612479eb686a456bd6f862f5acd7ea*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridDropHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridDropHandler.java
//Synthetic comment -- index 6d938fb..0292429 100644

//Synthetic comment -- @@ -650,7 +650,12 @@
gravity |= GravityHelper.GRAVITY_CENTER_HORIZ;
}
mGrid.setGridAttribute(newChild, ATTR_LAYOUT_COLUMN, column);
        if (mRowMatch.type == SegmentType.BOTTOM) {
gravity |= GravityHelper.GRAVITY_BOTTOM;
} else if (mRowMatch.type == SegmentType.CENTER_VERTICAL) {
gravity |= GravityHelper.GRAVITY_CENTER_VERT;
//Synthetic comment -- @@ -664,7 +669,13 @@
if (!GravityHelper.isConstrainedHorizontally(gravity)) {
gravity |= GravityHelper.GRAVITY_LEFT;
}
        if (!GravityHelper.isConstrainedVertically(gravity)) {
gravity |= GravityHelper.GRAVITY_TOP;
}
mGrid.setGridAttribute(newChild, ATTR_LAYOUT_GRAVITY, getGravity(gravity));







