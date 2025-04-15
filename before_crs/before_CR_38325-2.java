/*GridLayout bug fix

This changeset fixes a bug in the vertical handling of views.

(cherry picked from commit 8a76d00976eabe38e83d1c2ded2297c0fa0b8d47)

Change-Id:I1c976eeca981cda384ef96c779deb623f75b9312*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridDropHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridDropHandler.java
//Synthetic comment -- index 0292429..e01e045 100644

//Synthetic comment -- @@ -669,6 +669,8 @@
if (!GravityHelper.isConstrainedHorizontally(gravity)) {
gravity |= GravityHelper.GRAVITY_LEFT;
}
if (!GravityHelper.isConstrainedVertically(gravity)
// There is no baseline constant, so we have to leave it unconstrained instead
&& mRowMatch.type != SegmentType.BASELINE
//Synthetic comment -- @@ -678,6 +680,7 @@
&& !mRowMatch.createCell) {
gravity |= GravityHelper.GRAVITY_TOP;
}
mGrid.setGridAttribute(newChild, ATTR_LAYOUT_GRAVITY, getGravity(gravity));

mGrid.setGridAttribute(newChild, ATTR_LAYOUT_ROW, row);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridModel.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridModel.java
//Synthetic comment -- index 65a61b4..8cc9f90 100644

//Synthetic comment -- @@ -1689,7 +1689,8 @@
int endColumn = column + view.columnSpan;
if (endColumn > newColumn
|| endColumn == newColumn && (view.node.getBounds().x2() > x
                                || !GravityHelper.isLeftAligned(view.gravity))) {
// This cell spans the new insert position, so increment the column span
view.columnSpan += insertMarginColumn ? 2 : 1;
setColumnSpanAttribute(node, view.columnSpan);
//Synthetic comment -- @@ -1773,7 +1774,8 @@
int endRow = row + view.rowSpan;
if (endRow > newRow
|| endRow == newRow && (view.node.getBounds().y2() > y
                                || !GravityHelper.isTopAligned(view.gravity))) {
// This cell spans the new insert position, so increment the row span
view.rowSpan += insertMarginRow ? 2 : 1;
setRowSpanAttribute(node, view.rowSpan);







