/*Added Testcases for ArrayAdapter add and addAll

I know your current working branch is eclair, but the addAll is new on the
master branch, so i have to commit it here.
This commit needs change 15233

Change-Id:I7f824889b88141ec1283a691f856034d7b4bb72c*/




//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/ArrayAdapterTest.java b/tests/tests/widget/src/android/widget/cts/ArrayAdapterTest.java
//Synthetic comment -- index a268cba..a6cf0be 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import android.widget.TextView;

import com.android.cts.stub.R;
import com.google.debug.Assert;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
//Synthetic comment -- @@ -526,6 +527,93 @@
assertEquals(2, mockDataSetObserver.getCalledOnChangedCount());
}

    /**
     * insert multiple items via add, notify data changed
     * check count
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
        Assert.assertEquals(mArrayAdapter.getCount(), 0);

        for (int i = 0; i < 10; i++) {
            mArrayAdapter.add(i+"");
            Assert.assertEquals(mArrayAdapter.getCount(), i+1);
        }
    }

    /**
     * insert multiple items via addAll, notify data changed
     * check count
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
        int listSize = list.size();
        Assert.assertEquals(listSize, 4);

        mArrayAdapter.clear();
        int currentCount = 0;
        Assert.assertEquals(mArrayAdapter.getCount(), currentCount);

        for (int i = 0; i < 10; i++) {
            mArrayAdapter.addAll(list);
            currentCount += listSize;
            Assert.assertEquals(mArrayAdapter.getCount(), currentCount);
        }
    }

    /**
     * insert multiple items via addAll, notify data changed
     * check count
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
        int currentCount = 0;
        Assert.assertEquals(mArrayAdapter.getCount(), currentCount);

        for (int i = 0; i < 10; i++) {
            mArrayAdapter.addAll("this", "is", "a", "unit", "test");
            currentCount += 5;
            Assert.assertEquals(mArrayAdapter.getCount(), currentCount);
        }
    }

private static class MockDataSetObserver extends DataSetObserver {

private int mCalledOnChangedCount;







