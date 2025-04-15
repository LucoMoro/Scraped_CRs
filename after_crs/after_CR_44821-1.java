/*DO NOT MERGE Fix bug #7297028 Settings app crash when swiping between Downloaded / Running / All for Apps

- fix draw invariant for ViewPager (you cannot layout during drawing)
- add more logging

Change-Id:Ibfc38a16b40b8c63b0ecbf9b423d8867c28b66ed*/




//Synthetic comment -- diff --git a/v4/java/android/support/v4/view/ViewPager.java b/v4/java/android/support/v4/view/ViewPager.java
//Synthetic comment -- index bf6f926..a5eebb9 100644

//Synthetic comment -- @@ -218,6 +218,13 @@
*/
public static final int SCROLL_STATE_SETTLING = 2;

    private final Runnable mEndScrollRunnable = new Runnable() {
        public void run() {
            setScrollState(SCROLL_STATE_IDLE);
            populate();
        }
    };

private int mScrollState = SCROLL_STATE_IDLE;

/**
//Synthetic comment -- @@ -329,6 +336,12 @@
}
}

    @Override
    protected void onDetachedFromWindow() {
        removeCallbacks(mEndScrollRunnable);
        super.onDetachedFromWindow();
    }

private void setScrollState(int newState) {
if (mScrollState == newState) {
return;
//Synthetic comment -- @@ -489,7 +502,7 @@
if (dispatchSelected && mInternalPageChangeListener != null) {
mInternalPageChangeListener.onPageSelected(item);
}
            completeScroll(false);
scrollTo(destX, 0);
}
}
//Synthetic comment -- @@ -656,7 +669,7 @@
int dx = x - sx;
int dy = y - sy;
if (dx == 0 && dy == 0) {
            completeScroll(false);
populate();
setScrollState(SCROLL_STATE_IDLE);
return;
//Synthetic comment -- @@ -837,6 +850,10 @@
if (pos == ii.position && !ii.scrolling) {
mItems.remove(itemIndex);
mAdapter.destroyItem(this, pos, ii.object);
                        if (DEBUG) {
                            Log.i(TAG, "populate() - destroyItem() with pos: " + pos +
                                    " view: " + ((View) ii.object));
                        }
itemIndex--;
curIndex--;
ii = itemIndex >= 0 ? mItems.get(itemIndex) : null;
//Synthetic comment -- @@ -865,6 +882,10 @@
if (pos == ii.position && !ii.scrolling) {
mItems.remove(itemIndex);
mAdapter.destroyItem(this, pos, ii.object);
                            if (DEBUG) {
                                Log.i(TAG, "populate() - destroyItem() with pos: " + pos +
                                        " view: " + ((View) ii.object));
                            }
ii = itemIndex < mItems.size() ? mItems.get(itemIndex) : null;
}
} else if (ii != null && pos == ii.position) {
//Synthetic comment -- @@ -1283,7 +1304,7 @@
final float scrollOffset = ii != null ? Math.min(ii.offset, mLastOffset) : 0;
final int scrollPos = (int) (scrollOffset * width);
if (scrollPos != getScrollX()) {
                completeScroll(false);
scrollTo(scrollPos, getScrollY());
}
}
//Synthetic comment -- @@ -1419,7 +1440,7 @@
}

// Done with scroll, clean up state.
        completeScroll(true);
}

private boolean pageScrolled(int xpos) {
//Synthetic comment -- @@ -1512,7 +1533,7 @@
mCalledSuper = true;
}

    private void completeScroll(boolean postEvents) {
boolean needPopulate = mScrollState == SCROLL_STATE_SETTLING;
if (needPopulate) {
// Done with scroll, no longer want to cache view drawing.
//Synthetic comment -- @@ -1525,7 +1546,6 @@
if (oldX != x || oldY != y) {
scrollTo(x, y);
}
}
mPopulatePending = false;
for (int i=0; i<mItems.size(); i++) {
//Synthetic comment -- @@ -1536,7 +1556,11 @@
}
}
if (needPopulate) {
            if (postEvents) {
                ViewCompat.postOnAnimation(this, mEndScrollRunnable);
            } else {
                mEndScrollRunnable.run();
            }
}
}

//Synthetic comment -- @@ -1660,7 +1684,7 @@
mIsBeingDragged = true;
setScrollState(SCROLL_STATE_DRAGGING);
} else {
                    completeScroll(false);
mIsBeingDragged = false;
}








