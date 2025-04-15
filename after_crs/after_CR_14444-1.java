/*Tests for setting/getting fast scroller thumb side.

Test for setFastScrollSide and getFastScrollSide in AbsListView.

Change-Id:Idb7f5e4684120d36f35b842b5cfd59873ac81155*/




//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/AbsListViewTest.java b/tests/tests/widget/src/android/widget/cts/AbsListViewTest.java
//Synthetic comment -- index a70aec6..ff39241 100644

//Synthetic comment -- @@ -143,6 +143,32 @@
@TestTargets({
@TestTargetNew(
level = TestLevel.COMPLETE,
            method = "setFastScrollSide",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getFastScrollSide",
            args = {}
        )
    })
    public void testAccessFastScrollSide() {
        // default should be right
        assertEquals(AbsListView.FAST_SCROLL_SIDE_RIGHT, mListView.getFastScrollSide());

        mListView.setFastScrollSide(AbsListView.FAST_SCROLL_SIDE_RIGHT);
        assertEquals(AbsListView.FAST_SCROLL_SIDE_RIGHT, mListView.getFastScrollSide());

        mListView.setFastScrollSide(AbsListView.FAST_SCROLL_SIDE_LEFT);
        assertEquals(AbsListView.FAST_SCROLL_SIDE_LEFT, mListView.getFastScrollSide());

        mListView.setFastScrollSide(AbsListView.FAST_SCROLL_SIDE_BOTH);
        assertEquals(AbsListView.FAST_SCROLL_SIDE_BOTH, mListView.getFastScrollSide());
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
method = "setSmoothScrollbarEnabled",
args = {boolean.class}
),







