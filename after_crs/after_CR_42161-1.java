/*Fix PagerTitleStrip positioning during layout

Alternative patch for the proposed fix athttps://android-review.googlesource.com/#/c/41811/Preserve the correct titles when offset by more than 50% of a page
during a layout pass.

Change-Id:If9bbd5ce957ce6229bfea5eb7b8bcfef1e1a14b5*/




//Synthetic comment -- diff --git a/v4/java/android/support/v4/view/PagerTitleStrip.java b/v4/java/android/support/v4/view/PagerTitleStrip.java
//Synthetic comment -- index 0224854..dfd860c 100644

//Synthetic comment -- @@ -448,7 +448,12 @@
protected void onLayout(boolean changed, int l, int t, int r, int b) {
if (mPager != null) {
final float offset = mLastKnownPositionOffset >= 0 ? mLastKnownPositionOffset : 0;
            int position = mPager.getCurrentItem();
            if (offset > 0.5f) {
                // Consider ourselves to be on the next page when we're 50% of the way there.
                position++;
            }
            updateTextPositions(position, offset, true);
}
}








