/*Don't update PagerTitleStrip titles when scrolled.

If the layout of a PagerTitleStrip tab is updated it forces an update of
the tab titles. If the PagerTitleStrip is scrolled during this event
it may no longer be centered over the currently active layout which
causes incorrect titles to be displayed.

There is no need for the labels to be forced during the time the user
is scrolling the tabs because the event handler for the scroll event also
updates the labels, as well as taking into account if the view has been
scrolled away from the active layout.

Change-Id:I34808462110bbf4a8fd09d3b58ef8138c7e0ef18*/
//Synthetic comment -- diff --git a/v4/java/android/support/v4/view/PagerTitleStrip.java b/v4/java/android/support/v4/view/PagerTitleStrip.java
//Synthetic comment -- index 0224854..c512b08 100644

//Synthetic comment -- @@ -448,7 +448,11 @@
protected void onLayout(boolean changed, int l, int t, int r, int b) {
if (mPager != null) {
final float offset = mLastKnownPositionOffset >= 0 ? mLastKnownPositionOffset : 0;
            updateTextPositions(mPager.getCurrentItem(), offset, true);
}
}








