//<Beginning of snippet n. 0>
final int lastViewPixelsShowing = listHeight - lastViewTop;
final int extraScroll = lastPos < mItemCount - 1 ? mExtraScroll : mListPadding.bottom;

int scrollDistance = lastViewHeight - lastViewPixelsShowing + extraScroll;
if (scrollDistance > 0 && !areAllItemsVisible()) {
    smoothScrollBy(scrollDistance, mScrollDuration);
}

mLastSeenPos = lastPos;
if (lastPos < mTargetPos) {
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
package android.widget.expandablelistview;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.ExpandableListScenario;
import android.util.ListUtil;

@MediumTest
public void testContextMenus() {
    ExpandableListTester tester = new ExpandableListTester(mExpandableListView, this);
    tester.testContextMenus();
}
//<End of snippet n. 1>