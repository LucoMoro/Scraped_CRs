/*Fix ListView is not scrolled properly with arrows

Bug: 7346868
Change-Id:I01ab19ad899b5bcb5ab420ddf08c9ffd136933d1*/
//Synthetic comment -- diff --git a/core/java/android/widget/ListView.java b/core/java/android/widget/ListView.java
//Synthetic comment -- index 69e3177..4436fbb 100644

//Synthetic comment -- @@ -2429,7 +2429,9 @@
View selectedView = getSelectedView();
int selectedPos = mSelectedPosition;

        int nextSelectedPosition = lookForSelectablePositionOnScreen(direction);
int amountToScroll = amountToScroll(direction, nextSelectedPosition);

// if we are moving focus, we may OVERRIDE the default behavior
//Synthetic comment -- @@ -2641,14 +2643,18 @@
final int listBottom = getHeight() - mListPadding.bottom;
final int listTop = mListPadding.top;

        final int numChildren = getChildCount();

if (direction == View.FOCUS_DOWN) {
int indexToMakeVisible = numChildren - 1;
if (nextSelectedPosition != INVALID_POSITION) {
indexToMakeVisible = nextSelectedPosition - mFirstPosition;
}

final int positionToMakeVisible = mFirstPosition + indexToMakeVisible;
final View viewToMakeVisible = getChildAt(indexToMakeVisible);

//Synthetic comment -- @@ -2682,6 +2688,12 @@
if (nextSelectedPosition != INVALID_POSITION) {
indexToMakeVisible = nextSelectedPosition - mFirstPosition;
}
final int positionToMakeVisible = mFirstPosition + indexToMakeVisible;
final View viewToMakeVisible = getChildAt(indexToMakeVisible);
int goalTop = listTop;







