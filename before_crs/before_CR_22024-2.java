/*Correctly calculate the FastScroller's scrollTo position when using
ExpandableListView.

The current code calculates the target as a flat list position, but then
incorrectly uses it as a group index by passing it to
getPackedPositionForGroup(). As a result, if there are expanded items,
we will potentially scroll to the bottom of the list much too early.

This patch assumes that the desired behavior is having the fast scroll
feature target the individual groups. The alternative would be to target
each individual flat list item while scrolling.

Change-Id:Ie62f781c26edcf3073d7818cd37eb9fd4a8aa14d*/
//Synthetic comment -- diff --git a/core/java/android/widget/FastScroller.java b/core/java/android/widget/FastScroller.java
//Synthetic comment -- index aa68a74..4b8e93e 100644

//Synthetic comment -- @@ -382,15 +382,19 @@
mList.setSelection(index + mListOffset);
}
} else {
            int index = (int) (position * count);
if (mList instanceof ExpandableListView) {
ExpandableListView expList = (ExpandableListView) mList;
expList.setSelectionFromTop(expList.getFlatListPosition(
ExpandableListView.getPackedPositionForGroup(index + mListOffset)), 0);
            } else if (mList instanceof ListView) {
                ((ListView)mList).setSelectionFromTop(index + mListOffset, 0);
            } else {
                mList.setSelection(index + mListOffset);
}
sectionIndex = -1;
}







