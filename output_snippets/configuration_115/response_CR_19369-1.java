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
public void testLoadingThreadClose() throws Exception {
    mDatabase.execSQL("CREATE TABLE test (_id INTEGER PRIMARY KEY, data INT);");
//<End of snippet n. 0>