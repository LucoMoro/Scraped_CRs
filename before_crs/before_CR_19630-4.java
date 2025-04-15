/*Fix issue #13366. Take account of stackFromBottom in computeVerticalScrollOffset().

Change-Id:I984180af4b5e6a4a17714a662b43ffffc7d72fc2*/
//Synthetic comment -- diff --git a/core/java/android/widget/GridView.java b/core/java/android/widget/GridView.java
//Synthetic comment -- index a7300c2..d7f3602 100644

//Synthetic comment -- @@ -1862,8 +1862,13 @@
int height = view.getHeight();
if (height > 0) {
final int numColumns = mNumColumns;
                final int whichRow = mFirstPosition / numColumns;
final int rowCount = (mItemCount + numColumns - 1) / numColumns;
return Math.max(whichRow * 100 - (top * 100) / height +
(int) ((float) mScrollY / getHeight() * rowCount * 100), 0);
}







