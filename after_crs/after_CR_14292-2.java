/*Fix ListViewTest#testGetMaxScrollAmount

Issue 7425

The test depends on the device's screen size. The SDK comments on
the method and our goal not to depend on device screen size forces
us to reduce the test to testing no scrolling for an empty adapter
and some scrolling for a non-empty adapter.

Change-Id:I5a5d5a5d643f139edc74fc09af8a897e1999f93e*/




//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/ListViewTest.java b/tests/tests/widget/src/android/widget/cts/ListViewTest.java
//Synthetic comment -- index f283681..e7b872a 100644

//Synthetic comment -- @@ -61,6 +61,7 @@
private final String[] mNameList = new String[] {
"Jacky", "David", "Kevin", "Michael", "Andy"
};
    private final String[] mEmptyList = new String[0];

private ListView mListView;
private Activity mActivity;
//Synthetic comment -- @@ -68,6 +69,7 @@
private AttributeSet mAttributeSet;
private ArrayAdapter<String> mAdapter_countries;
private ArrayAdapter<String> mAdapter_names;
    private ArrayAdapter<String> mAdapter_empty;

public ListViewTest() {
super("com.android.cts.stub", ListViewStubActivity.class);
//Synthetic comment -- @@ -85,6 +87,8 @@
android.R.layout.simple_list_item_1, mCountryList);
mAdapter_names = new ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1,
mNameList);
        mAdapter_empty = new ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1,
                mEmptyList);

mListView = (ListView) mActivity.findViewById(R.id.listview_default);
}
//Synthetic comment -- @@ -147,26 +151,22 @@
)
})
public void testGetMaxScrollAmount() {
        setAdapter(mAdapter_empty);
        int scrollAmount = mListView.getMaxScrollAmount();
        assertEquals(0, scrollAmount);

        setAdapter(mAdapter_names);
        scrollAmount = mListView.getMaxScrollAmount();
        assertTrue(scrollAmount > 0);
    }

    private void setAdapter(final ArrayAdapter<String> adapter) {
mInstrumentation.runOnMainSync(new Runnable() {
public void run() {
                mListView.setAdapter(adapter);
}
});
mInstrumentation.waitForIdleSync();
}

@TestTargets({







