/*fix CTS issue for case testSetOnScrollListener

The country list is too short for 1080p device. It can be displayed fullly without scrollbar.
The fix is to make the list longer enough for scrolling.*/
//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/AbsListViewTest.java b/tests/tests/widget/src/android/widget/cts/AbsListViewTest.java
//Synthetic comment -- index 0995f28..2539a183 100644

//Synthetic comment -- @@ -59,6 +59,8 @@
@TestTargetClass(AbsListView.class)
public class AbsListViewTest extends ActivityInstrumentationTestCase2<ListViewStubActivity> {
private final String[] mCountryList = new String[] {
"Argentina", "Australia", "China", "France", "Germany", "Italy", "Japan", "United States"
};








