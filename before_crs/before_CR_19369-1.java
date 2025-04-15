/*Delete DatabaseCursorTest#testLoadingThread

Bug 3188260

This test seems to stress test the device. It will probably fail on
lower end devices and create unnecessary inquiries.

Change-Id:I99309ecac9b90b8db6b329d386ccf84113f825ad*/
//Synthetic comment -- diff --git a/tests/tests/database/src/android/database/cts/DatabaseCursorTest.java b/tests/tests/database/src/android/database/cts/DatabaseCursorTest.java
//Synthetic comment -- index f60638a..340f630 100644

//Synthetic comment -- @@ -16,13 +16,6 @@

package android.database.cts;

import dalvik.annotation.BrokenTest;

import java.io.File;
import java.util.Arrays;
import java.util.Random;

import junit.framework.TestCase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
//Synthetic comment -- @@ -43,6 +36,10 @@
import android.test.suitebuilder.annotation.Suppress;
import android.util.Log;

public class DatabaseCursorTest extends AndroidTestCase implements PerformanceTestCase {
private static final String sString1 = "this is a test";
private static final String sString2 = "and yet another test";
//Synthetic comment -- @@ -398,32 +395,6 @@
}

@LargeTest
    @BrokenTest("Consistently times out, even with count reduced to 30000")
    public void testLoadingThread() throws Exception {
        mDatabase.execSQL("CREATE TABLE test (_id INTEGER PRIMARY KEY, data INT);");

        final int count = 50000;
        String sql = "INSERT INTO test (data) VALUES (?);";
        SQLiteStatement s = mDatabase.compileStatement(sql);
        for (int i = 0; i < count; i++) {
            s.bindLong(1, i);
            s.execute();
        }

        int maxRead = 1000;
        int initialRead = 5;
        SQLiteCursor testCursor = (SQLiteCursor) mDatabase.rawQuery("select * from test;", null,
                initialRead, maxRead);

        TestObserver observer = new TestObserver(count, testCursor);
        testCursor.registerDataSetObserver(observer);
        testCursor.getCount();

        Looper.loop();
        testCursor.close();
    }

    @LargeTest
public void testLoadingThreadClose() throws Exception {
mDatabase.execSQL("CREATE TABLE test (_id INTEGER PRIMARY KEY, data INT);");








