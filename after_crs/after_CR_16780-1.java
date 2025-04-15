/*Making the profiling box slightly transparent

Change-Id:I886a8b1c61db11d521657ec776152b07e7f9fabb*/




//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeView.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeView.java
//Synthetic comment -- index 2c3a13e..e2a0a6d 100644

//Synthetic comment -- @@ -564,12 +564,14 @@
// Draw the profiling box.
if (selectedNode != null) {

                        e.gc.setAlpha(200);

// Draw the little triangle
int x = selectedNode.left + DrawableViewNode.NODE_WIDTH / 2;
int y = (int) selectedNode.top + 4;
e.gc.setBackground(boxColor);
e.gc.fillPolygon(new int[] {
                                x, y, x - 11, y - 11, x + 11, y - 11
});

// Draw the rectangle and update the location.
//Synthetic comment -- @@ -579,6 +581,8 @@
selectedRectangleLocation =
new Rectangle(x - RECT_WIDTH / 2, y, RECT_WIDTH, RECT_HEIGHT);

                        e.gc.setAlpha(255);

// Draw the button
buttonCenter =
new Point(x - BUTTON_RIGHT_OFFSET + (RECT_WIDTH - BUTTON_SIZE) / 2,







