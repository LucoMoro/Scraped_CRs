/*AbsListView.smoothScrollToPositionFromTop doesn't work correctly.

Change-Id:I69e9f329ac316356208332d5a3072a0ef4a96a65Signed-off-by: NoraBora <noranbora@gmail.com>*/
//Synthetic comment -- diff --git a/core/java/android/widget/AbsListView.java b/core/java/android/widget/AbsListView.java
//Synthetic comment -- index 2d939b1..e0b0a5e 100644

//Synthetic comment -- @@ -4708,7 +4708,7 @@
final View lastView = getChildAt(getChildCount() - 1); 
if (lastView != null) {
final int scrollBy = lastView.getHeight() - (listHeight - lastView.getTop()) + 1; // +1 is important to go to next view
                        smoothScrollBy(Math.min(scrollBy, distance), duration, true);
} else {
smoothScrollBy(distance, duration, true);
}







