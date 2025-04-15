/*Fix issue #13366. Take account of stackFromBottom in computeVerticalScrollOffset().

Now on master branch.

I have tested it before on the froyo branch in the emulator via eclipse, however often I cannot get the master branch to build in eclipse, or before that in the terminal via make even. Any advice? I presume it is just the state of the master branch that it does not always compile. This resubmit to the master branch has not been tested an sich, but since the code is identical and the change is localized, the risk is minimal.

Change-Id:I984180af4b5e6a4a17714a662b43ffffc7d72fc2*/
//Synthetic comment -- diff --git a/core/java/android/widget/GridView.java b/core/java/android/widget/GridView.java
//Synthetic comment -- index 2f86d75..8215e01 100644

//Synthetic comment -- @@ -1862,8 +1862,13 @@
int height = view.getHeight();
if (height > 0) {
final int numColumns = mNumColumns;
                final int whichRow = mFirstPosition / numColumns;
final int rowCount = (mItemCount + numColumns - 1) / numColumns;
return Math.max(whichRow * 100 - (top * 100) / height +
(int) ((float) mScrollY / getHeight() * rowCount * 100), 0);
}







