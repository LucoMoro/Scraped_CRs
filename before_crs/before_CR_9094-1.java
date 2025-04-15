/*Let the fillGap() be protected, so the List/Grid views can be extended.*/
//Synthetic comment -- diff --git a/core/java/android/widget/AbsListView.java b/core/java/android/widget/AbsListView.java
//Synthetic comment -- index 19ec77d..80626d4 100644

//Synthetic comment -- @@ -2425,7 +2425,7 @@
*
* @param down true if the scroll is going down, false if it is going up
*/
    abstract void fillGap(boolean down);

void hideSelector() {
if (mSelectedPosition != INVALID_POSITION) {








//Synthetic comment -- diff --git a/core/java/android/widget/GridView.java b/core/java/android/widget/GridView.java
//Synthetic comment -- index 38bfc7c..ba8de1e 100644

//Synthetic comment -- @@ -169,7 +169,7 @@
* {@inheritDoc}
*/
@Override
    void fillGap(boolean down) {
final int numColumns = mNumColumns;
final int verticalSpacing = mVerticalSpacing;









//Synthetic comment -- diff --git a/core/java/android/widget/ListView.java b/core/java/android/widget/ListView.java
//Synthetic comment -- index dfc7bc3..b68f513 100644

//Synthetic comment -- @@ -577,7 +577,7 @@
* {@inheritDoc}
*/
@Override
    void fillGap(boolean down) {
final int count = getChildCount();
if (down) {
final int startOffset = count > 0 ? getChildAt(count - 1).getBottom() + mDividerHeight :







