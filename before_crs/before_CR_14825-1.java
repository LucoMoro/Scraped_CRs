/*Clean Up Properly in Database Tests

Issue 8401

Fix SQLiteTransaction exceptions that arise from enabling SQLite
shared cache mode. These occur when not closing database handles
properly...

Change-Id:I24afaaaefe47d77cc4bba9c187bf35906e0727c3*/
//Synthetic comment -- diff --git a/tests/tests/database/src/android/database/cts/AbstractCursorTest.java b/tests/tests/database/src/android/database/cts/AbstractCursorTest.java
//Synthetic comment -- index bc404fe..14462a0 100644

//Synthetic comment -- @@ -67,6 +67,15 @@
mTestAbstractCursor = new TestAbstractCursor(COLUMN_NAMES, list);
}

@TestTargetNew(
level = TestLevel.COMPLETE,
method = "AbstractCursor",








//Synthetic comment -- diff --git a/tests/tests/database/src/android/database/cts/CursorWrapperTest.java b/tests/tests/database/src/android/database/cts/CursorWrapperTest.java
//Synthetic comment -- index c32cd89..507b2a3 100644

//Synthetic comment -- @@ -19,6 +19,8 @@
import java.io.File;
import java.util.Arrays;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IContentProvider;
//Synthetic comment -- @@ -39,7 +41,7 @@
import dalvik.annotation.TestTargetClass;

@TestTargetClass(android.database.CursorWrapper.class)
public class CursorWrapperTest extends DatabaseCursorTest {

private static final String FIRST_NUMBER = "123";
private static final String SECOND_NUMBER = "5555";
//Synthetic comment -- @@ -61,7 +63,6 @@
@Override
protected void setUp() throws Exception {
super.setUp();
        setupTestType(TYPE_CURSORWRAPPER);
setupDatabase();
}









//Synthetic comment -- diff --git a/tests/tests/database/src/android/database/sqlite/cts/SQLiteDatabaseTest.java b/tests/tests/database/src/android/database/sqlite/cts/SQLiteDatabaseTest.java
//Synthetic comment -- index 0791bcd..bc351f5 100644

//Synthetic comment -- @@ -454,17 +454,26 @@
})
@ToBeFixed(bug = "1676383", explanation = "setPageSize does not work as javadoc declares.")
public void testAccessPageSize() {
        mDatabaseFile = new File(mDatabaseDir, "database.db");
        if (mDatabaseFile.exists()) {
            mDatabaseFile.delete();
}
        mDatabase = SQLiteDatabase.openOrCreateDatabase(mDatabaseFile.getPath(), null);

        long initialValue = mDatabase.getPageSize();
        // check that this does not throw an exception
        // setting a different page size may not be supported after the DB has been created
        mDatabase.setPageSize(initialValue);
        assertEquals(initialValue, mDatabase.getPageSize());
}

@TestTargetNew(
//Synthetic comment -- @@ -827,9 +836,16 @@
public void testIsReadOnly() {
assertFalse(mDatabase.isReadOnly());

        mDatabase = SQLiteDatabase.openDatabase(mDatabaseFilePath, null,
                SQLiteDatabase.OPEN_READONLY);
        assertTrue(mDatabase.isReadOnly());
}

@TestTargetNew(







