/*Fix issue #13366. Take account of stackFromBottom in
computeVerticalScrollOffset().

Change-Id:I743708ef1ac05e358840f37010de36b7d0c6a346Signed-off-by: Pieter-Jan Vandormael <ezelspinguin@gmail.com>*/
//Synthetic comment -- diff --git a/core/java/android/widget/GridView.java b/core/java/android/widget/GridView.java
//Synthetic comment -- index 5d406de..428af836 100644

//Synthetic comment -- @@ -2095,8 +2095,13 @@
int height = view.getHeight();
if (height > 0) {
final int numColumns = mNumColumns;
                final int whichRow = mFirstPosition / numColumns;
final int rowCount = (mItemCount + numColumns - 1) / numColumns;
return Math.max(whichRow * 100 - (top * 100) / height +
(int) ((float) mScrollY / getHeight() * rowCount * 100), 0);
}







