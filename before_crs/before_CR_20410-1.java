/*Wait for finishing the scrolling

If AbsListView is applyed the following patch
(https://review.source.android.com/#change,20408)
then testSetOnScrollListener fails.

This patch is to avoid the fail.

Change-Id:I3eea372bcec4f2d3a5a1649361967bfd5b939dfc*/
//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/AbsListViewTest.java b/tests/tests/widget/src/android/widget/cts/AbsListViewTest.java
//Synthetic comment -- index 0995f28..1d14fc6 100644

//Synthetic comment -- @@ -240,6 +240,8 @@
onScrollListener.reset();

TouchUtils.scrollToBottom(this, mActivity, mListView);
assertSame(mListView, onScrollListener.getView());
assertEquals(mListView.getChildCount(), onScrollListener.getVisibleItemCount());
assertEquals(mCountryList.length, onScrollListener.getTotalItemCount());







