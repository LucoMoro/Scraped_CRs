/*Added Testcases for ArrayAdapter add and addAll

Change-Id:I7f824889b88141ec1283a691f856034d7b4bb72c*/




//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/ArrayAdapterTest.java b/tests/tests/widget/src/android/widget/cts/ArrayAdapterTest.java
//Synthetic comment -- index a268cba..4541e9d 100644

//Synthetic comment -- @@ -526,6 +526,94 @@
assertEquals(2, mockDataSetObserver.getCalledOnChangedCount());
}

    /**
     * insert multiple items via add, notify data changed
     * check count and content
     */
    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "add",
            args = {Object.class}
        )
    })
    public void testAdd() {
        mArrayAdapter.setNotifyOnChange(true);
        final MockDataSetObserver mockDataSetObserver = new MockDataSetObserver();
        mArrayAdapter.registerDataSetObserver(mockDataSetObserver);

        mArrayAdapter.clear();
        assertEquals(mArrayAdapter.getCount(), 0);

        mArrayAdapter.add("testing");
        mArrayAdapter.add("android");
        assertEquals(mArrayAdapter.getCount(), 2);
        assertEquals(mArrayAdapter.getItem(0), "testing");
        assertEquals(mArrayAdapter.getItem(1), "android");
    }

    /**
     * insert multiple items via addAll, notify data changed
     * check count and content
     */
    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "addAll",
            args = {java.util.Collection.class}
        )
    })
    public void testAddAllCollection() {
        mArrayAdapter.setNotifyOnChange(true);
        final MockDataSetObserver mockDataSetObserver = new MockDataSetObserver();
        mArrayAdapter.registerDataSetObserver(mockDataSetObserver);

        List<String> list = new ArrayList<String>();
        list.add("");
        list.add("hello");
        list.add("android");
        list.add("!");

        mArrayAdapter.clear();
        assertEquals(mArrayAdapter.getCount(), 0);

        mArrayAdapter.addAll(list);
        assertEquals(mArrayAdapter.getCount(), list.size());

        assertEquals(mArrayAdapter.getItem(0), list.get(0));
        assertEquals(mArrayAdapter.getItem(1), list.get(1));
        assertEquals(mArrayAdapter.getItem(2), list.get(2));
        assertEquals(mArrayAdapter.getItem(3), list.get(3));
    }

    /**
     * insert multiple items via addAll, notify data changed
     * check count and content
     */
    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "addAll",
            args = {Object[].class}
        )
    })
    public void testAddAllParams() {
        mArrayAdapter.setNotifyOnChange(true);
        final MockDataSetObserver mockDataSetObserver = new MockDataSetObserver();
        mArrayAdapter.registerDataSetObserver(mockDataSetObserver);

        mArrayAdapter.clear();
        assertEquals(mArrayAdapter.getCount(), 0);

        mArrayAdapter.addAll("this", "is", "a", "unit", "test");
        assertEquals(mArrayAdapter.getCount(), 5);
        assertEquals(mArrayAdapter.getItem(0), "this");
        assertEquals(mArrayAdapter.getItem(1), "is");
        assertEquals(mArrayAdapter.getItem(2), "a");
        assertEquals(mArrayAdapter.getItem(3), "unit");
        assertEquals(mArrayAdapter.getItem(4), "test");
    }

private static class MockDataSetObserver extends DataSetObserver {

private int mCalledOnChangedCount;







