/*Workaround view info cookie bug

This changeset works around the case where a ViewInfo cookie is
identical to its parent. This is for example the case for a
ZoomControls widget, where the child views have MergeCookies whole
value points to the parent ZoomControl.

Change-Id:Ie0eb62750fba6eeaa7241edce68e05f853e08a75*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java
//Synthetic comment -- index 7471e54..c37ffe8 100755

//Synthetic comment -- @@ -779,6 +779,12 @@
assert viewInfo.getCookie() != null;

CanvasViewInfo view = createView(parent, viewInfo, parentX, parentY);

// Process children:
parentX += viewInfo.getLeft();
//Synthetic comment -- @@ -792,7 +798,9 @@
if (cookie instanceof UiViewElementNode || cookie instanceof MergeCookie) {
CanvasViewInfo childView = createSubtree(view, child,
parentX, parentY);
                        view.addChild(childView);
} // else: null cookies, adapter item references, etc: No child views.
}

//Synthetic comment -- @@ -930,7 +938,9 @@
ViewInfo child = children.get(index);
if (child.getCookie() != null) {
CanvasViewInfo childView = createSubtree(parentView, child, parentX, parentY);
                    parentView.addChild(childView);
if (child.getCookie() instanceof UiViewElementNode) {
afterNode = (UiViewElementNode) child.getCookie();
}
//Synthetic comment -- @@ -1084,7 +1094,7 @@
// not MergeCookies.
if (viewInfo.getCookie() != null) {
CanvasViewInfo subtree = createSubtree(parent, viewInfo, parentX, parentY);
                if (parent != null) {
parent.mChildren.add(subtree);
}
return subtree;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfoTest.java
//Synthetic comment -- index 2df472e..bca7cae 100644

//Synthetic comment -- @@ -623,6 +623,34 @@
assertEquals(new Rectangle(0, 40, 49, 19), bounds2);
}

public void testGestureOverlayView() throws Exception {
boolean layoutlib5 = true;








