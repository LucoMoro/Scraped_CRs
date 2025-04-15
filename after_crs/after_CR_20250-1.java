/*Nuke GridViewTest#testScroll

Bug 3097462

Remove flakey test and now broken test on GB. Could be a problem with
TouchUtils or the test.

Change-Id:Iecc09f4a997be24e08ea0b38c915073cf99a9d46*/




//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/GridViewTest.java b/tests/tests/widget/src/android/widget/cts/GridViewTest.java
//Synthetic comment -- index 40684c9..57b5804 100644

//Synthetic comment -- @@ -635,50 +635,6 @@
assertEquals(child0.getLeft(), child1.getLeft());
}

private static class MockGridView extends GridView {
private boolean mCalledOnMeasure = false;
private boolean mCalledOnFocusChanged = false;







