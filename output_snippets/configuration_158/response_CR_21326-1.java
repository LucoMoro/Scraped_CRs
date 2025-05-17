//<Beginning of snippet n. 0>
final int lastViewPixelsShowing = listHeight - lastViewTop;
final int extraScroll = lastPos < mItemCount - 1 ? mExtraScroll : mListPadding.bottom;

if (lastViewHeight - lastViewPixelsShowing + extraScroll > 0) {
    if (!isLastActionCollapse || lastViewPixelsShowing == lastViewHeight) {
        smoothScrollBy(lastViewHeight - lastViewPixelsShowing + extraScroll, mScrollDuration);
    }
}

mLastSeenPos = lastPos;
if (lastPos < mTargetPos) {
    // Perform expand logic
} else {
    isLastActionCollapse = true;
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
    // Add tests for tap on expanded items to collapse
    tester.testTapOnExpandedItem();
    // Provide visual or haptic feedback logic if necessary
}
//<End of snippet n. 1>