/*[CTS]fix DatabaseCursorTest#testManyRowsLong test case to use a single transaction.

Change-Id:I3488ee3e6c8eb9068dd4952aecdd34fd8c974121*/
//Synthetic comment -- diff --git a/tests/tests/database/src/android/database/cts/DatabaseCursorTest.java b/tests/tests/database/src/android/database/cts/DatabaseCursorTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 9969f40..7b37f9c

//Synthetic comment -- @@ -415,12 +415,19 @@

@LargeTest
public void testManyRowsLong() throws Exception {
        mDatabase.execSQL("CREATE TABLE test (_id INTEGER PRIMARY KEY, data INT);");

final int count = 9000;
        for (int i = 0; i < count; i++) {
            mDatabase.execSQL("INSERT INTO test (data) VALUES (" + i + ");");
        }

Cursor testCursor = getTestCursor(mDatabase.query("test", new String[] { "data" },
null, null, null, null, null));







