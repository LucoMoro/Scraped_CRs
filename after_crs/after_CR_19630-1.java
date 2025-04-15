/*Fix issue #13366. Take account of stackFromBottom in coomputeVerticalScrollOffset().

Change-Id:I984180af4b5e6a4a17714a662b43ffffc7d72fc2*/




//Synthetic comment -- diff --git a/core/java/android/widget/GridView.java b/core/java/android/widget/GridView.java
//Synthetic comment -- index 2f86d75..8215e01 100644

//Synthetic comment -- @@ -1862,8 +1862,13 @@
int height = view.getHeight();
if (height > 0) {
final int numColumns = mNumColumns;
final int rowCount = (mItemCount + numColumns - 1) / numColumns;
                // In case of stackFromBottom the calculation of whichRow needs
                // to take into account that counting from the top the first row
                // might not be entirely filled.
                final int oddItemsOnFirstRow = isStackFromBottom() ? ((rowCount * numColumns) - 
                        mItemCount) : 0;
                final int whichRow = (mFirstPosition + oddItemsOnFirstRow) / numColumns;
return Math.max(whichRow * 100 - (top * 100) / height +
(int) ((float) mScrollY / getHeight() * rowCount * 100), 0);
}







