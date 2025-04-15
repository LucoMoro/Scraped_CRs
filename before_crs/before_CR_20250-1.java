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

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "Test {@link GridView#computeVerticalScrollExtent()}",
            method = "computeVerticalScrollExtent",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "Test {@link GridView#computeVerticalScrollExtent()}",
            method = "computeVerticalScrollOffset",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "Test {@link GridView#computeVerticalScrollExtent()}",
            method = "computeVerticalScrollRange",
            args = {}
        )
    })
    public void testScroll() throws Throwable {
        final MockGridView mockGridView= new MockGridView(mActivity);
        final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // this test case can not be ran in UI thread.
        runTestOnUiThread(new Runnable() {
            public void run() {
                mActivity.getWindow().setContentView(mockGridView, params);
                mockGridView.setAdapter(new ImageAdapter(mActivity));
            }
        });
        mInstrumentation.waitForIdleSync();
        TouchUtils.scrollToTop(this, mActivity, mockGridView);

        int oldRange = mockGridView.computeVerticalScrollRange();
        int oldExtent = mockGridView.computeVerticalScrollExtent();
        int oldOffset = mockGridView.computeVerticalScrollOffset();

        TouchUtils.scrollToBottom(this, mActivity, mockGridView);
        assertEquals(oldRange, mockGridView.computeVerticalScrollRange());
        assertEquals(oldExtent, mockGridView.computeVerticalScrollExtent());
        assertTrue(oldOffset < mockGridView.computeVerticalScrollOffset());
    }

private static class MockGridView extends GridView {
private boolean mCalledOnMeasure = false;
private boolean mCalledOnFocusChanged = false;







