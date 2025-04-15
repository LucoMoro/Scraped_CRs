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

private int mOffsetFromTop;

//Synthetic comment -- @@ -4445,8 +4446,10 @@
int viewTravelCount;
if (mTargetPos < firstPos) {
viewTravelCount = firstPos - mTargetPos;
} else if (mTargetPos > lastPos) {
viewTravelCount = mTargetPos - lastPos;
} else {
// On-screen, just scroll.
final int targetTop = getChildAt(mTargetPos - firstPos).getTop();
//Synthetic comment -- @@ -4650,13 +4653,23 @@
}

case MOVE_OFFSET: {
                if (mLastSeenPos == firstPos) {
                    // No new views, let things keep going.
                    postOnAnimation(this);
                    return;
                }

                mLastSeenPos = firstPos;

final int childCount = getChildCount();
final int position = mTargetPos;
//Synthetic comment -- @@ -4676,12 +4689,29 @@
if (position < firstPos) {
final int distance = (int) (-getHeight() * modifier);
final int duration = (int) (mScrollDuration * modifier);
                    smoothScrollBy(distance, duration, true);
postOnAnimation(this);
} else if (position > lastPos) {
final int distance = (int) (getHeight() * modifier);
final int duration = (int) (mScrollDuration * modifier);
                    smoothScrollBy(distance, duration, true);
postOnAnimation(this);
} else {
// On-screen, just scroll.







