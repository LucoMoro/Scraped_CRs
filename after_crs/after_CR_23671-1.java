/*CherryPick 8f21dc3e from master to r12. do not merge.

Workaround view info cookie bug

This changeset works around the case where a ViewInfo cookie is
identical to its parent. This is for example the case for a
ZoomControls widget, where the child views have MergeCookies whole
value points to the parent ZoomControl.

Change-Id:I6c85dab318eb5b5a4e5dd95d4c7ead61c81f435d*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java
//Synthetic comment -- index 7471e54..c37ffe8 100755

//Synthetic comment -- @@ -779,6 +779,12 @@
assert viewInfo.getCookie() != null;

CanvasViewInfo view = createView(parent, viewInfo, parentX, parentY);
            // Bug workaround: Ensure that we never have a child node identical
            // to its parent node: this can happen for example when rendering a
            // ZoomControls view where the merge cookies point to the parent.
            if (parent != null && view.mUiViewNode == parent.mUiViewNode) {
                return null;
            }

// Process children:
parentX += viewInfo.getLeft();
//Synthetic comment -- @@ -792,7 +798,9 @@
if (cookie instanceof UiViewElementNode || cookie instanceof MergeCookie) {
CanvasViewInfo childView = createSubtree(view, child,
parentX, parentY);
                        if (childView != null) {
                            view.addChild(childView);
                        }
} // else: null cookies, adapter item references, etc: No child views.
}

//Synthetic comment -- @@ -930,7 +938,9 @@
ViewInfo child = children.get(index);
if (child.getCookie() != null) {
CanvasViewInfo childView = createSubtree(parentView, child, parentX, parentY);
                    if (childView != null) {
                        parentView.addChild(childView);
                    }
if (child.getCookie() instanceof UiViewElementNode) {
afterNode = (UiViewElementNode) child.getCookie();
}
//Synthetic comment -- @@ -1084,7 +1094,7 @@
// not MergeCookies.
if (viewInfo.getCookie() != null) {
CanvasViewInfo subtree = createSubtree(parent, viewInfo, parentX, parentY);
                if (parent != null && subtree != null) {
parent.mChildren.add(subtree);
}
return subtree;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfoTest.java
//Synthetic comment -- index 2df472e..bca7cae 100644

//Synthetic comment -- @@ -623,6 +623,34 @@
assertEquals(new Rectangle(0, 40, 49, 19), bounds2);
}

    public void testCookieWorkaround() throws Exception {
        UiViewElementNode rootNode = createNode("android.widget.LinearLayout", true);
        ViewInfo root = new ViewInfo("included", null, 0, 0, 100, 100);

        UiViewElementNode node2 = createNode(rootNode, "childNode2", false);
        MergeCookie mergeCookie = new MergeCookie(root);

        ViewInfo childView1 = new ViewInfo("childView1", mergeCookie, 0, 20, 50, 40);
        ViewInfo childView2 = new ViewInfo("childView2", node2, 0, 40, 50, 60);

        root.setChildren(Arrays.asList(childView1, childView2));

        Pair<CanvasViewInfo, List<Rectangle>> result = CanvasViewInfo.create(root, true);
        CanvasViewInfo rootView = result.getFirst();
        List<Rectangle> bounds = result.getSecond();
        assertNotNull(rootView);

        assertEquals("included", rootView.getName());
        assertNull(rootView.getParent());
        assertNull(rootView.getUiViewNode());
        // childView1 should have been removed since it has the wrong merge cookie
        assertEquals(1, rootView.getChildren().size());
        assertEquals(1, rootView.getUniqueChildren().size());

        Rectangle bounds1 = bounds.get(0);
        assertEquals(new Rectangle(0, 40, 49, 19), bounds1);
    }

public void testGestureOverlayView() throws Exception {
boolean layoutlib5 = true;








