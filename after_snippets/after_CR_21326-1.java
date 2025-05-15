
//<Beginning of snippet n. 0>


final int lastViewPixelsShowing = listHeight - lastViewTop;
final int extraScroll = lastPos < mItemCount - 1 ? mExtraScroll : mListPadding.bottom;

                final int scrollDistance = lastViewHeight - lastViewPixelsShowing + extraScroll;
                if (scrollDistance > 0) {
                    smoothScrollBy(scrollDistance, mScrollDuration);
                }

mLastSeenPos = lastPos;
if (lastPos < mTargetPos) {

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


package android.widget.expandablelistview;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.ExpandableListScenario;
import android.util.ListUtil;
}

@MediumTest
    public void testMultipleExpansionsAndCollapsesByTouch() {
        // Create a minimal list for a non-scrolling list on devices with smaller screens
        List<MyGroup> groups = mActivity.getGroups();
        groups.clear();
        MyGroup insertedGroup1 = new MyGroup(1);
        groups.add(0, insertedGroup1);
        MyGroup insertedGroup2 = new MyGroup(1);
        groups.add(1, insertedGroup2);
        MyGroup insertedGroup3 = new MyGroup(0);
        groups.add(2, insertedGroup3);

        // Notify data change
        final BaseExpandableListAdapter adapter = (BaseExpandableListAdapter) mAdapter;
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
        getInstrumentation().waitForIdleSync();

        // Expand groups
        TouchUtils.clickView(this, mExpandableListView.getChildAt(0));
        assertTrue("Group did not expand", mExpandableListView.isGroupExpanded(0));
        TouchUtils.clickView(this, mExpandableListView.getChildAt(2));
        assertTrue("Group did not expand", mExpandableListView.isGroupExpanded(1));

        // Collapse groups
        TouchUtils.clickView(this, mExpandableListView.getChildAt(2));
        assertFalse("Group did not collapse", mExpandableListView.isGroupExpanded(1));
        TouchUtils.clickView(this, mExpandableListView.getChildAt(0));
        assertFalse("Group did not collapse", mExpandableListView.isGroupExpanded(0));
    }

    @MediumTest
public void testContextMenus() {
ExpandableListTester tester = new ExpandableListTester(mExpandableListView, this);
tester.testContextMenus();

//<End of snippet n. 1>








