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
                    if (vi.isPrimaryNodeSibling()) {
includedBounds.add(vi.getAbsRect());
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfoTest.java
//Synthetic comment -- index 9c1ac7d..a240f90 100644

//Synthetic comment -- @@ -547,6 +547,38 @@
assertEquals(new Rectangle(0, 20, 49, 19), bounds.get(1));
}

public void testGestureOverlayView() throws Exception {
// Test rendering of included views on layoutlib 5+ (e.g. has <include> tag)








