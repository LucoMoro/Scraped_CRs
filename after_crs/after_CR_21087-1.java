/*Fix included-in overlay mask

When you show a view as included within another layout, there should
be a semi-translucent mask over the surrounding context. This did not
work in all cases (in particular, when there was no merge cookie).

This changeset makes it work in non-mergecookie scenarios as well.

Change-Id:I70d43565009905bf6d9b03eab86213aeebf22e6a*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java
//Synthetic comment -- index e08d4fd..6c32445 100755

//Synthetic comment -- @@ -551,7 +551,7 @@

List<Rectangle> includedBounds = new ArrayList<Rectangle>();
for (CanvasViewInfo vi : rootView.getChildren()) {
                    if (vi.getNodeSiblings() == null || vi.isPrimaryNodeSibling()) {
includedBounds.add(vi.getAbsRect());
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfoTest.java
//Synthetic comment -- index 9c1ac7d..a240f90 100644

//Synthetic comment -- @@ -547,6 +547,38 @@
assertEquals(new Rectangle(0, 20, 49, 19), bounds.get(1));
}

    public void testIncludeBounds2() throws Exception {
        UiViewElementNode rootNode = createNode("android.widget.LinearLayout", true);
        ViewInfo root = new ViewInfo("included", null, 0, 0, 100, 100);

        UiViewElementNode node1 = createNode(rootNode, "childNode1", false);
        UiViewElementNode node2 = createNode(rootNode, "childNode2", false);

        // Sets alternating merge cookies and checks whether the node sibling lists are
        // okay and merged correctly

        ViewInfo childView1 = new ViewInfo("childView1", node1, 0, 20, 50, 40);
        ViewInfo childView2 = new ViewInfo("childView2", node2, 0, 40, 50, 60);

        root.setChildren(Arrays.asList(childView1, childView2));

        Pair<CanvasViewInfo, List<Rectangle>> result = CanvasViewInfo.create(root);
        CanvasViewInfo rootView = result.getFirst();
        List<Rectangle> bounds = result.getSecond();
        assertNotNull(rootView);

        assertEquals("included", rootView.getName());
        assertNull(rootView.getParent());
        assertNull(rootView.getUiViewNode());
        assertEquals(2, rootView.getChildren().size());
        assertEquals(2, rootView.getUniqueChildren().size());

        Rectangle bounds1 = bounds.get(0);
        Rectangle bounds2 = bounds.get(1);
        assertEquals(new Rectangle(0, 20, 49, 19), bounds1);
        assertEquals(new Rectangle(0, 40, 49, 19), bounds2);
    }

public void testGestureOverlayView() throws Exception {
// Test rendering of included views on layoutlib 5+ (e.g. has <include> tag)








