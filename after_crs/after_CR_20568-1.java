/*DO NOT MERGE - Nuke AbsListViewTest#testOnScroll..

This is broken like GridViewTest due to a bug in TouchUtils
and overscroll. It will have to be revisited later.

Change-Id:I06eb857f133c9678d5e0ef51e5f3d51229d61759*/




//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/AbsListViewTest.java b/tests/tests/widget/src/android/widget/cts/AbsListViewTest.java
//Synthetic comment -- index 0995f28..4bf19d2 100644

//Synthetic comment -- @@ -199,55 +199,6 @@
});
mInstrumentation.waitForIdleSync();
}

@TestTargetNew(
level = TestLevel.COMPLETE,







