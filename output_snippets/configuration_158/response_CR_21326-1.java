//<Beginning of snippet n. 0>

final int lastViewPixelsShowing = listHeight - lastViewTop;
final int extraScroll = lastPos < mItemCount - 1 ? mExtraScroll : mListPadding.bottom;

if (!areAllItemsFullyExpanded()) {
    int scrollDistance = lastViewHeight - lastViewPixelsShowing + extraScroll;
    if (scrollDistance > 0) {
        boolean scrolled = smoothScrollBy(scrollDistance, mScrollDuration);
        if (scrolled) {
            mLastSeenPos = lastPos;
        }
    }
}

if (lastPos < mTargetPos) {
    // Additional logic for scrolling if necessary
}

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