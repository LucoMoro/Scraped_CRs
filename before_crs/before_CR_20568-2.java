/*DO NOT MERGE - Nuke AbsListViewTest#testOnScroll..

Bug 3062700

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
        method = "setOnScrollListener",
        args = {android.widget.AbsListView.OnScrollListener.class}
    )
    public void testSetOnScrollListener() throws Throwable {
        MockOnScrollListener onScrollListener = new MockOnScrollListener();

        assertNull(onScrollListener.getView());
        assertEquals(0, onScrollListener.getFirstVisibleItem());
        assertEquals(0, onScrollListener.getVisibleItemCount());
        assertEquals(0, onScrollListener.getTotalItemCount());
        assertEquals(-1, onScrollListener.getScrollState());

        assertFalse(onScrollListener.isOnScrollCalled());
        assertFalse(onScrollListener.isOnScrollStateChangedCalled());

        mListView.setOnScrollListener(onScrollListener);
        assertSame(mListView, onScrollListener.getView());
        assertEquals(0, onScrollListener.getFirstVisibleItem());
        assertEquals(0, onScrollListener.getVisibleItemCount());
        assertEquals(0, onScrollListener.getTotalItemCount());
        assertEquals(-1, onScrollListener.getScrollState());

        assertTrue(onScrollListener.isOnScrollCalled());
        assertFalse(onScrollListener.isOnScrollStateChangedCalled());
        onScrollListener.reset();

        setAdapter();

        assertSame(mListView, onScrollListener.getView());
        assertEquals(0, onScrollListener.getFirstVisibleItem());
        assertEquals(mListView.getChildCount(), onScrollListener.getVisibleItemCount());
        assertEquals(mCountryList.length, onScrollListener.getTotalItemCount());
        assertEquals(-1, onScrollListener.getScrollState());

        assertTrue(onScrollListener.isOnScrollCalled());
        assertFalse(onScrollListener.isOnScrollStateChangedCalled());
        onScrollListener.reset();

        TouchUtils.scrollToBottom(this, mActivity, mListView);
        assertSame(mListView, onScrollListener.getView());
        assertEquals(mListView.getChildCount(), onScrollListener.getVisibleItemCount());
        assertEquals(mCountryList.length, onScrollListener.getTotalItemCount());
        assertEquals(OnScrollListener.SCROLL_STATE_IDLE, onScrollListener.getScrollState());

        assertTrue(onScrollListener.isOnScrollCalled());
        assertTrue(onScrollListener.isOnScrollStateChangedCalled());
    }

@TestTargetNew(
level = TestLevel.COMPLETE,







