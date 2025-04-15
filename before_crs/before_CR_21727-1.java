/*Rounds up scaled width/height

In framework, the scaled value is calculated by rounding up.
(e.x. android.util.TypedValue#complexToDimensionPixelSize())
This test should be calculated same that framework does do.

Change-Id:I1f60f0446e79cc75fb85a7b45cfe79229838721e*/
//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/ScrollViewTest.java b/tests/tests/widget/src/android/widget/cts/ScrollViewTest.java
//Synthetic comment -- index b852e5b..cff8142 100644

//Synthetic comment -- @@ -78,10 +78,10 @@

// calculate pixel positions from dpi constants.
final float density = getActivity().getResources().getDisplayMetrics().density;
        mItemWidth = (int) (ITEM_WIDTH_DPI * density);
        mItemHeight = (int) (ITEM_HEIGHT_DPI * density);
        mPageWidth = (int) (PAGE_WIDTH_DPI * density);
        mPageHeight = (int) (PAGE_HEIGHT_DPI * density);

mScrollBottom = mItemHeight * ITEM_COUNT - mPageHeight;
mScrollRight = mItemWidth - mPageWidth;







