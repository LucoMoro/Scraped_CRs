/*Added Unit Tests for the new DatabaseUtils.queryNumEntries methods

Change-Id:Ie8407946f864872b25586f7309ff5e4ee6cb7dbe*/
//Synthetic comment -- diff --git a/tests/tests/database/src/android/database/cts/DatabaseUtilsTest.java b/tests/tests/database/src/android/database/cts/DatabaseUtilsTest.java
//Synthetic comment -- index ad89d5f..d184d16 100644

//Synthetic comment -- @@ -49,6 +49,7 @@
"age",             // 2
"address"          // 3
};

@Override
protected void setUp() throws Exception {
//Synthetic comment -- @@ -59,7 +60,7 @@
}
mDatabase = SQLiteDatabase.openOrCreateDatabase(mDatabaseFile.getPath(), null);
assertNotNull(mDatabase);
        mDatabase.execSQL("CREATE TABLE test (_id INTEGER PRIMARY KEY, " +
"name TEXT, age INTEGER, address TEXT);");
}

//Synthetic comment -- @@ -130,11 +131,12 @@
String address = "LA";

// at the beginning, there are no records in the database.
        Cursor cursor = mDatabase.query("test", TEST_PROJECTION, null, null, null, null, null);
assertNotNull(cursor);
assertEquals(0, cursor.getCount());

        String sql = "INSERT INTO test (name, age, address) VALUES (?, ?, ?);";
SQLiteStatement statement = mDatabase.compileStatement(sql);
DatabaseUtils.bindObjectToProgram(statement, 1, name);
DatabaseUtils.bindObjectToProgram(statement, 2, age);
//Synthetic comment -- @@ -142,7 +144,7 @@
statement.execute();
statement.close();

        cursor = mDatabase.query("test", TEST_PROJECTION, null, null, null, null, null);
assertNotNull(cursor);
assertEquals(1, cursor.getCount());
cursor.moveToFirst();
//Synthetic comment -- @@ -159,8 +161,8 @@
)
public void testCreateDbFromSqlStatements() {
String dbName = "ExampleName";
        String sqls = "CREATE TABLE test (_id INTEGER PRIMARY KEY, name TEXT);\n"
                + "INSERT INTO test (name) VALUES ('Mike');\n";
DatabaseUtils.createDbFromSqlStatements(getContext(), dbName, 1, sqls);

SQLiteDatabase db = getContext().openOrCreateDatabase(dbName, 0, null);
//Synthetic comment -- @@ -168,7 +170,7 @@
"_id",             // 0
"name"             // 1
};
        Cursor cursor = db.query("test", PROJECTION, null, null, null, null, null);
assertNotNull(cursor);
assertEquals(1, cursor.getCount());
cursor.moveToFirst();
//Synthetic comment -- @@ -183,8 +185,10 @@
android.content.ContentValues.class, java.lang.String.class}
)
public void testCursorDoubleToContentValues() {
        mDatabase.execSQL("INSERT INTO test (name, age, address) VALUES ('Mike', '20', 'LA');");
        Cursor cursor = mDatabase.query("test", TEST_PROJECTION, null, null, null, null, null);
assertNotNull(cursor);

ContentValues contentValues = new ContentValues();
//Synthetic comment -- @@ -208,8 +212,10 @@
)
@ToBeFixed(bug = "1586458", explanation = "It's probably a typo.")
public void testCursorDoubleToCursorValues() {
        mDatabase.execSQL("INSERT INTO test (name, age, address) VALUES ('Mike', '20', 'LA');");
        Cursor cursor = mDatabase.query("test", TEST_PROJECTION, null, null, null, null, null);
assertNotNull(cursor);

ContentValues contentValues = new ContentValues();
//Synthetic comment -- @@ -239,8 +245,9 @@
)
})
public void testCursorIntToContentValues() {
        mDatabase.execSQL("INSERT INTO test (name, age, address) VALUES ('Mike', '20', 'LA');");
        Cursor cursor = mDatabase.query("test", TEST_PROJECTION, null, null, null, null, null);
assertNotNull(cursor);

ContentValues contentValues = new ContentValues();
//Synthetic comment -- @@ -281,8 +288,9 @@
)
})
public void testcursorLongToContentValues() {
        mDatabase.execSQL("INSERT INTO test (name, age, address) VALUES ('Mike', '20', 'LA');");
        Cursor cursor = mDatabase.query("test", TEST_PROJECTION, null, null, null, null, null);
assertNotNull(cursor);

ContentValues contentValues = new ContentValues();
//Synthetic comment -- @@ -314,8 +322,10 @@
args = {android.database.Cursor.class, android.content.ContentValues.class}
)
public void testCursorRowToContentValues() {
        mDatabase.execSQL("INSERT INTO test (name, age, address) VALUES ('Mike', '20', 'LA');");
        Cursor cursor = mDatabase.query("test", TEST_PROJECTION, null, null, null, null, null);
assertNotNull(cursor);

ContentValues contentValues = new ContentValues();
//Synthetic comment -- @@ -341,8 +351,10 @@
)
})
public void testCursorStringToContentValues() {
        mDatabase.execSQL("INSERT INTO test (name, age, address) VALUES ('Mike', '20', 'LA');");
        Cursor cursor = mDatabase.query("test", TEST_PROJECTION, null, null, null, null, null);
assertNotNull(cursor);

ContentValues contentValues = new ContentValues();
//Synthetic comment -- @@ -388,7 +400,8 @@
mDatabase.execSQL("CREATE TABLE test_copy (_id INTEGER PRIMARY KEY, " +
"name TEXT, age INTEGER, address TEXT);");

        mDatabase.execSQL("INSERT INTO test (name, age, address) VALUES ('Mike', '20', 'LA');");
Cursor cursor = mDatabase.query("test_copy", TEST_PROJECTION, null, null, null, null, null);
assertEquals(0, cursor.getCount());

//Synthetic comment -- @@ -397,7 +410,7 @@
int indexAge = insertHelper.getColumnIndex("age");
int indexAddress = insertHelper.getColumnIndex("address");

        cursor = mDatabase.query("test", TEST_PROJECTION, null, null, null, null, null);
cursor.moveToNext();
insertHelper.prepareForInsert();
DatabaseUtils.cursorStringToInsertHelper(cursor, "name", insertHelper, indexName);
//Synthetic comment -- @@ -436,8 +449,10 @@
)
})
public void testDumpCurrentRow() {
        mDatabase.execSQL("INSERT INTO test (name, age, address) VALUES ('Mike', '20', 'LA');");
        Cursor cursor = mDatabase.query("test", TEST_PROJECTION, null, null, null, null, null);
assertNotNull(cursor);
cursor.moveToNext();
String expected = "0 {\n   _id=1\n   name=Mike\n   age=20\n   address=LA\n}\n";
//Synthetic comment -- @@ -481,9 +496,12 @@
)
})
public void testDumpCursor() {
        mDatabase.execSQL("INSERT INTO test (name, age, address) VALUES ('Mike', '20', 'LA');");
        mDatabase.execSQL("INSERT INTO test (name, age, address) VALUES ('Jack', '30', 'London');");
        Cursor cursor = mDatabase.query("test", TEST_PROJECTION, null, null, null, null, null);
assertNotNull(cursor);
int pos = cursor.getPosition();
String expected = ">>>>> Dumping cursor " + cursor + "\n" +
//Synthetic comment -- @@ -562,13 +580,15 @@
)
})
public void testLongForQuery() {
        mDatabase.execSQL("INSERT INTO test (name, age, address) VALUES ('Mike', '20', 'LA');");

        String query = "SELECT age FROM test";
assertEquals(20, DatabaseUtils.longForQuery(mDatabase, query, null));

        mDatabase.execSQL("INSERT INTO test (name, age, address) VALUES ('Jack', '35', 'London');");
        query = "SELECT age FROM test WHERE name = ?";
String[] args = new String[] { "Jack" };
assertEquals(35, DatabaseUtils.longForQuery(mDatabase, query, args));
args = new String[] { "No such name" };
//Synthetic comment -- @@ -579,11 +599,11 @@
// expected
}

        query = "SELECT count(*) FROM test;";
SQLiteStatement statement = mDatabase.compileStatement(query);
assertEquals(2, DatabaseUtils.longForQuery(statement, null));

        query = "SELECT age FROM test WHERE address = ?;";
statement = mDatabase.compileStatement(query);
args = new String[] { "London" };
assertEquals(35, DatabaseUtils.longForQuery(statement, args));
//Synthetic comment -- @@ -598,19 +618,47 @@
statement.close();
}

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "queryNumEntries",
        args = {android.database.sqlite.SQLiteDatabase.class, java.lang.String.class}
    )
public void testQueryNumEntries() {
        assertEquals(0, DatabaseUtils.queryNumEntries(mDatabase, "test"));

        mDatabase.execSQL("INSERT INTO test (name, age, address) VALUES ('Mike', '20', 'LA');");
        assertEquals(1, DatabaseUtils.queryNumEntries(mDatabase, "test"));

        mDatabase.execSQL("INSERT INTO test (name, age, address) VALUES ('Mike', '20', 'LA');");
        assertEquals(2, DatabaseUtils.queryNumEntries(mDatabase, "test"));

try {
DatabaseUtils.queryNumEntries(mDatabase, "NoSuchTable");
//Synthetic comment -- @@ -693,13 +741,15 @@
)
})
public void testStringForQuery() {
        mDatabase.execSQL("INSERT INTO test (name, age, address) VALUES ('Mike', '20', 'LA');");

        String query = "SELECT name FROM test";
assertEquals("Mike", DatabaseUtils.stringForQuery(mDatabase, query, null));

        mDatabase.execSQL("INSERT INTO test (name, age, address) VALUES ('Jack', '35', 'London');");
        query = "SELECT name FROM test WHERE address = ?";
String[] args = new String[] { "London" };
assertEquals("Jack", DatabaseUtils.stringForQuery(mDatabase, query, args));
args = new String[] { "No such address" };
//Synthetic comment -- @@ -710,7 +760,7 @@
// expected
}

        query = "SELECT name FROM test WHERE age = ?;";
SQLiteStatement statement = mDatabase.compileStatement(query);
args = new String[] { "20" };
assertEquals("Mike", DatabaseUtils.stringForQuery(statement, args));
//Synthetic comment -- @@ -724,4 +774,4 @@
}
statement.close();
}
}
\ No newline at end of file







