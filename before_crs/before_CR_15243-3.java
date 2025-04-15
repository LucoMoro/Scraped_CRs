/*Added Testcases for ArrayAdapter add and addAll

Change-Id:I7f824889b88141ec1283a691f856034d7b4bb72c*/
//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/ArrayAdapterTest.java b/tests/tests/widget/src/android/widget/cts/ArrayAdapterTest.java
//Synthetic comment -- index a268cba..4541e9d 100644

//Synthetic comment -- @@ -526,6 +526,94 @@
assertEquals(2, mockDataSetObserver.getCalledOnChangedCount());
}

private static class MockDataSetObserver extends DataSetObserver {

private int mCalledOnChangedCount;







