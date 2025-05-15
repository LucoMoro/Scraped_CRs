
//<Beginning of snippet n. 0>



package android.database.cts;

import dalvik.annotation.BrokenTest;

import java.io.File;
import java.util.Arrays;
import java.util.Random;

import junit.framework.TestCase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.test.suitebuilder.annotation.Suppress;
import android.util.Log;

public class DatabaseCursorTest extends AndroidTestCase implements PerformanceTestCase {
private static final String sString1 = "this is a test";
private static final String sString2 = "and yet another test";
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


//<End of snippet n. 0>








