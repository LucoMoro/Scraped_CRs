/*LinearLayout guide fixes

When we	have bounds for	the dragged items, we show a preview rectangle
where the item will appear after the drop. We show this rectangle
midway between the two siblings that share the insert
position. However, when you are inserting *after* the last item in the
LinearLayout, there is no reason to show the item midway, since at
this point nothing below the insert position needs to be shifted
down. This checkin changes this such that for the last insert
position, we show the rectangle fully below the insert line (or to the
right of it in case of a horizontal layout).

This changeset also fixes a bug	where the last (available, not active)
dropzone would not be shown on a palette drag.

Change-Id:If449b43582e072c9e0ad6d7741afbe8e845e8df9*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java
//Synthetic comment -- index caaef5d..bc2716a 100644

//Synthetic comment -- @@ -147,7 +147,7 @@
// course we happened to be dragging the last element
if (!lastDragged) {
int v = last + 1;
            indexes.add(new MatchPos(v, pos));
}

return new DropFeedback(new LinearDropData(indexes, isVertical, selfPos),
//Synthetic comment -- @@ -223,6 +223,8 @@
}

if (be.isValid()) {
                boolean isLast = (data.getInsertPos() == data.getIndexes().size());

// At least the first element has a bound. Draw rectangles for
// all dropped elements with valid bounds, offset at the drop
// point.
//Synthetic comment -- @@ -230,9 +232,10 @@
int offsetY;
if (isVertical) {
offsetX = b.x - be.x;
                    offsetY = currY - be.y - (isLast ? 0 : (be.h / 2));

} else {
                    offsetX = currX - be.x - (isLast ? 0 : (be.w / 2));
offsetY = b.y - be.y;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LinearLayoutRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LinearLayoutRuleTest.java
//Synthetic comment -- index 42dbb3c..c5c43fd 100644

//Synthetic comment -- @@ -69,7 +69,8 @@
"[useStyle(DROP_RECIPIENT), "
+
// Bounds rectangle
                        "drawRect(Rect[0,0,240,480]), "
                        + "useStyle(DROP_ZONE), drawLine(1,0,1,480), "
+ "useStyle(DROP_ZONE_ACTIVE), " + "useStyle(DROP_PREVIEW), " +
// Insert position line
"drawLine(1,0,1,480)" + (haveBounds ?
//Synthetic comment -- @@ -83,7 +84,7 @@
rule.onDropped(targetNode, elements, feedback, dropPoint);
assertEquals(1, targetNode.getChildren().length);
assertEquals("@+id/Button01", targetNode.getChildren()[0].getStringAttr(
                BaseView.ANDROID_URI, BaseView.ATTR_ID));
}

// Utility for other tests
//Synthetic comment -- @@ -91,7 +92,7 @@
int insertIndex, int currentIndex,
String... graphicsFragments) {
INode linearLayout = TestNode.create("android.widget.LinearLayout").id(
                "@+id/LinearLayout01").bounds(new Rect(0, 0, 240, 480)).set(BaseView.ANDROID_URI,
LinearLayoutRule.ATTR_ORIENTATION,
vertical ? LinearLayoutRule.VALUE_VERTICAL : LinearLayoutRule.VALUE_HORIZONTAL)
.add(
//Synthetic comment -- @@ -130,7 +131,7 @@
LinearLayoutRule rule = new LinearLayoutRule();
INode node = TestNode.create("android.widget.Button").id("@+id/Button012");

        assertNull(node.getStringAttr(BaseView.ANDROID_URI, LinearLayoutRule.ATTR_ORIENTATION));

List<MenuAction> contextMenu = rule.getContextMenu(node);
assertEquals(4, contextMenu.size());
//Synthetic comment -- @@ -143,11 +144,11 @@
IMenuCallback callback = choices.getCallback();
callback.action(orientationAction, LinearLayoutRule.VALUE_VERTICAL, true);

        String orientation = node.getStringAttr(BaseView.ANDROID_URI,
LinearLayoutRule.ATTR_ORIENTATION);
assertEquals(LinearLayoutRule.VALUE_VERTICAL, orientation);
callback.action(orientationAction, LinearLayoutRule.VALUE_HORIZONTAL, true);
        orientation = node.getStringAttr(BaseView.ANDROID_URI, LinearLayoutRule.ATTR_ORIENTATION);
assertEquals(LinearLayoutRule.VALUE_HORIZONTAL, orientation);
}

//Synthetic comment -- @@ -174,7 +175,8 @@

// Drop zones
"useStyle(DROP_ZONE), drawLine(0,0,240,0), drawLine(0,90,240,90), "
                        + "drawLine(0,190,240,190), drawLine(0,290,240,290), "
                        + "drawLine(0,381,240,381)",

// Active nearest line
"useStyle(DROP_ZONE_ACTIVE), useStyle(DROP_PREVIEW), drawLine(0,0,240,0)",
//Synthetic comment -- @@ -302,6 +304,34 @@
"useStyle(DROP_ZONE_ACTIVE), useStyle(DROP_PREVIEW), drawRect(Rect[0,100,100,80])");
}

    public void testDragToLastPosition() {
        // Drag a button to the last position -- and confirm that the preview rectangle
        // is now shown midway between the second to last and last positions, but fully
        // below the drop zone line:
        dragInto(true,
                // Bounds of the dragged item
                new Rect(0, 100, 100, 80),
                // Drag point
                new Point(0, 400),
                // Expected insert location
                3,
                // Dragging 1st item
                1,

                // Bounds rectangle
                "useStyle(DROP_RECIPIENT), drawRect(Rect[0,0,240,480])",

                // Drop Zones
                "useStyle(DROP_ZONE), drawLine(0,0,240,0), drawLine(0,290,240,290), " +
                "drawLine(0,381,240,381), ",

                // Active Drop Zone
                "useStyle(DROP_ZONE_ACTIVE), useStyle(DROP_PREVIEW), drawLine(0,381,240,381)",

                // Drop Preview
                "seStyle(DROP_PREVIEW), drawRect(Rect[0,381,100,80])");
    }

// Left to test:
// Check inserting at last pos with multiple children
// Check inserting with no bounds rectangle for dragged element







