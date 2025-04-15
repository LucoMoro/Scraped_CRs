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

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
//Synthetic comment -- @@ -526,6 +527,93 @@
assertEquals(2, mockDataSetObserver.getCalledOnChangedCount());
}

private static class MockDataSetObserver extends DataSetObserver {

private int mCalledOnChangedCount;







