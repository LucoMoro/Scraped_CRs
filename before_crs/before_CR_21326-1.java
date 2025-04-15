/*Fix for issue related to expandable list.

The user experience is that of a lost touch event. Typically the user
first expands an item successfully and then attempt to collapse the item,
only to fail on the first attempt. A more technical description would be
that the first touch event will trigger a scrolling procedure, which in
a certain case will fail because of inconsistent parameter (scrolling
distance equal to or less then zero). This fix do not change existing code, but
rather adds handling of this special state. Reproduce 10/10, by first
expanding a list item, then expanding one more list item further down
in the list and if all items in the fully expanded list is visible on the
screen (scrolling is not necessary).

Change-Id:Id083e9d8a1085845d1347069452b156ae2c33e03*/
//Synthetic comment -- diff --git a/core/java/android/widget/AbsListView.java b/core/java/android/widget/AbsListView.java
//Synthetic comment -- index 9a38acc..cfd9fd2 100644

//Synthetic comment -- @@ -3071,8 +3071,10 @@
final int lastViewPixelsShowing = listHeight - lastViewTop;
final int extraScroll = lastPos < mItemCount - 1 ? mExtraScroll : mListPadding.bottom;

                smoothScrollBy(lastViewHeight - lastViewPixelsShowing + extraScroll,
                        mScrollDuration);

mLastSeenPos = lastPos;
if (lastPos < mTargetPos) {








//Synthetic comment -- diff --git a/core/tests/coretests/src/android/widget/expandablelistview/ExpandableListBasicTest.java b/core/tests/coretests/src/android/widget/expandablelistview/ExpandableListBasicTest.java
//Synthetic comment -- index e23b516..8709fe9 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package android.widget.expandablelistview;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.ExpandableListScenario;
import android.util.ListUtil;
//Synthetic comment -- @@ -123,6 +124,40 @@
}

@MediumTest
public void testContextMenus() {
ExpandableListTester tester = new ExpandableListTester(mExpandableListView, this);
tester.testContextMenus();







