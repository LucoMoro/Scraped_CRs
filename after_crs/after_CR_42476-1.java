/*AbsListView.smoothScrollToPositionFromTop doesn't work correctly.http://code.google.com/p/android/issues/detail?id=36627When last row is a little bit visible and
call smoothScrollToPositionFromTop(getListView().getLastVisiblePosition()+1, 0);
it moves just 1 row.

Change-Id:I7438129310f3a744b9f547cfe3c211335852b86dSigned-off-by: NoraBora <noranbora@gmail.com>*/




//Synthetic comment -- diff --git a/core/java/android/widget/AbsListView.java b/core/java/android/widget/AbsListView.java
//Synthetic comment -- index 19aef8e..2d939b1 100644

//Synthetic comment -- @@ -4275,6 +4275,7 @@
private int mLastSeenPos;
private int mScrollDuration;
private final int mExtraScroll;
        private final int mDirection;

private int mOffsetFromTop;

//Synthetic comment -- @@ -4445,8 +4446,10 @@
int viewTravelCount;
if (mTargetPos < firstPos) {
viewTravelCount = firstPos - mTargetPos;
                mDirection = MOVE_UP_POS;
} else if (mTargetPos > lastPos) {
viewTravelCount = mTargetPos - lastPos;
                mDirection = MOVE_DOWN_POS;
} else {
// On-screen, just scroll.
final int targetTop = getChildAt(mTargetPos - firstPos).getTop();
//Synthetic comment -- @@ -4650,13 +4653,23 @@
}

case MOVE_OFFSET: {
                if (mDirection == MOVE_UP_POS) {
                    if (mLastSeenPos == firstPos) {
                        // No new views, let things keep going.
                        postOnAnimation(this);
                        return;
                    }

                    mLastSeenPos = firstPos;
                } else {
                    if (mLastSeenPos == lastPos) {
                        // No new views, let things keep going.
                        postOnAnimation(this);
                        return;
                    }

                    mLastSeenPos = lastPos;
                }

final int childCount = getChildCount();
final int position = mTargetPos;
//Synthetic comment -- @@ -4676,12 +4689,29 @@
if (position < firstPos) {
final int distance = (int) (-getHeight() * modifier);
final int duration = (int) (mScrollDuration * modifier);

                    // distance is sometimes not big enough to change mLastSeenPos. so check first view top.
                    final View firstView = getChildAt(0); 
                    if (firstView != null) {
                        final int scrollBy = firstView.getTop() - 1; // -1 is important to go to next view
                        smoothScrollBy(Math.min(scrollBy, distance), duration, true);
                    } else {
                        smoothScrollBy(distance, duration, true);
                    }
postOnAnimation(this);
} else if (position > lastPos) {
final int distance = (int) (getHeight() * modifier);
final int duration = (int) (mScrollDuration * modifier);


                    // distance is sometimes not big enough to change mLastSeenPos. so check last view top.
                    final View lastView = getChildAt(getChildCount() - 1); 
                    if (lastView != null) {
                        final int scrollBy = lastView.getHeight() - (listHeight - lastView.getTop()) + 1; // +1 is important to go to next view
                        smoothScrollBy(Math.min(scrollBy, distance), duration, true);
                    } else {
                        smoothScrollBy(distance, duration, true);
                    }
postOnAnimation(this);
} else {
// On-screen, just scroll.







