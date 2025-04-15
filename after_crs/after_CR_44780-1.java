/*Fix the outOfBoundary issue in ListView for Monkey test.

As missing synchronization between refresh operation of
ListView and adapter, the size of adapter and the number
of view in ListView would be inconsistent, it would cause
outOfBoundary issue. Do workaround to keep the min size of
them.

Change-Id:I4c5e537b6a35e1c06ba6a897364a7fadb7cff107Signed-off-by: Zhijun Peng <pengzhj@marvell.com>*/




//Synthetic comment -- diff --git a/core/java/android/widget/ListView.java b/core/java/android/widget/ListView.java
old mode 100644
new mode 100755
//Synthetic comment -- index e011c13..2360cb1

//Synthetic comment -- @@ -3087,9 +3087,9 @@
bounds.left = mPaddingLeft;
bounds.right = mRight - mLeft - mPaddingRight;

            final int count = Math.min(getChildCount(), mAdapter.getCount());
final int headerCount = mHeaderViewInfos.size();
            final int itemCount = count;
final int footerLimit = itemCount - mFooterViewInfos.size() - 1;
final boolean headerDividers = mHeaderDividersEnabled;
final boolean footerDividers = mFooterDividersEnabled;







