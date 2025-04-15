/*Improvements to LinearLayout feedback

When you have a small and empty linear layout, and you drag something
larger (such as a button) into it, the drop feedback is a bit
confusing: The drop feedback rectangle is larger than the linear
layout, so the bounds are outside the layout. This changeset addresses
this by forcing the bounds of the drop preview to be at most the
dimensions of the LinearLayout itself.

Second, the fix I applied last week to show the last insert position,
did not work in all cases - in particular when the drag originates
outside the canvas itself. To determine if we are inserting at the
last position, look at the number of target node children, rather than
the number of potential insert positions, since in some cases the
number of insert positions will be smaller than the number of
children.

Finally, there was a theoretical bug that if one of the dragged
elements did not non-zero bounds, then the insert position would be
wrong. This is also fixed by this changeset.

Change-Id:Ia30e99f7a3aa45b8091855b69aaef86ec3699405*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java
//Synthetic comment -- index bc2716a..e5334f8 100644

//Synthetic comment -- @@ -140,6 +140,10 @@

last = isVertical ? (bc.y + bc.h) : (bc.x + bc.w);
lastDragged = isDragged;
}
}

//Synthetic comment -- @@ -150,7 +154,8 @@
indexes.add(new MatchPos(v, pos));
}

        return new DropFeedback(new LinearDropData(indexes, isVertical, selfPos),
new IFeedbackPainter() {

public void paint(IGraphics gc, INode node, DropFeedback feedback) {
//Synthetic comment -- @@ -223,7 +228,7 @@
}

if (be.isValid()) {
                boolean isLast = (data.getInsertPos() == data.getIndexes().size());

// At least the first element has a bound. Draw rectangles for
// all dropped elements with valid bounds, offset at the drop
//Synthetic comment -- @@ -241,7 +246,16 @@

gc.useStyle(DrawingStyle.DROP_PREVIEW);
for (IDragElement element : elements) {
                    drawElement(gc, element, offsetX, offsetY);
}
}
}
//Synthetic comment -- @@ -390,6 +404,9 @@
/** Insert points (pixels + index) */
private final List<MatchPos> mIndexes;

/** Current marker X position */
private Integer mCurrX;

//Synthetic comment -- @@ -409,8 +426,10 @@
/** height of match line if it's a vertical one */
private Integer mHeight;

        public LinearDropData(List<MatchPos> indexes, boolean isVertical, int selfPos) {
this.mIndexes = indexes;
this.mVertical = isVertical;
this.mSelfPos = selfPos;
}
//Synthetic comment -- @@ -479,5 +498,14 @@
private Integer getHeight() {
return mHeight;
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LinearLayoutRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LinearLayoutRuleTest.java
//Synthetic comment -- index c5c43fd..9796104 100644

//Synthetic comment -- @@ -75,7 +75,7 @@
// Insert position line
"drawLine(1,0,1,480)" + (haveBounds ?
// Outline of dragged node centered over position line
                        ", useStyle(DROP_PREVIEW), " + "drawRect(Rect[-49,0,100,80])"
// Nothing when we don't have bounds
: "") + "]", graphics.getDrawn().toString());

//Synthetic comment -- @@ -207,17 +207,17 @@

// Drop zones
"useStyle(DROP_ZONE), drawLine(0,0,240,0), drawLine(0,90,240,90), "
                        + "drawLine(0,190,240,190), drawLine(0,290,240,290)",

// Active nearest line
"useStyle(DROP_ZONE_ACTIVE), useStyle(DROP_PREVIEW), drawLine(0,381,240,381)",

// Preview of the dropped rectangle
                "useStyle(DROP_PREVIEW), drawRect(Rect[0,341,105,80])");

// Check without bounds too
dragInto(true, new Rect(0, 0, 105, 80), new Point(30, 500), 4, -1,
                "useStyle(DROP_PREVIEW), drawRect(Rect[0,341,105,80])");
}

public void testDragInVerticalMiddle() {







