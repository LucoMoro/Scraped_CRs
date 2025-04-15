/*PagerTitleStrip flicker fix, take 2

Change-Id:I2681d4ce0d00cb1f8b881af6777448239bf7b0e7*/




//Synthetic comment -- diff --git a/v4/java/android/support/v4/view/PagerTitleStrip.java b/v4/java/android/support/v4/view/PagerTitleStrip.java
//Synthetic comment -- index dfd860c..3be484e 100644

//Synthetic comment -- @@ -448,12 +448,7 @@
protected void onLayout(boolean changed, int l, int t, int r, int b) {
if (mPager != null) {
final float offset = mLastKnownPositionOffset >= 0 ? mLastKnownPositionOffset : 0;
            updateTextPositions(mLastKnownCurrentPage, offset, true);
}
}








