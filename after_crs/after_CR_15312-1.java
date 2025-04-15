/*Added Unit Tests for the new SQLiteDatabase.count() methods

Change-Id:Ie8407946f864872b25586f7309ff5e4ee6cb7dbe*/




//Synthetic comment -- diff --git a/tests/tests/database/src/android/database/sqlite/cts/SQLiteDatabaseTest.java b/tests/tests/database/src/android/database/sqlite/cts/SQLiteDatabaseTest.java
//Synthetic comment -- index 0791bcd..df3ffdc 100644

//Synthetic comment -- @@ -514,6 +514,64 @@
cursor.close();
}

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "Test count(String)",
            method = "count",
            args = {java.lang.String.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "Test count(String, String)",
            method = "count",
            args = {java.lang.String.class, java.lang.String.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "Test count(String, String, String[])",
            method = "count",
            args = {java.lang.String.class, java.lang.String.class, java.lang.String[].class}
        )
    })
    public void testCount() {
        mDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY, "
                + "name TEXT, age INTEGER, address TEXT);");

        ContentValues values = new ContentValues();
        values.put("name", "Jack");
        values.put("age", 20);
        values.put("address", "LA");
        mDatabase.insert(TABLE_NAME, "name", values);

        values = new ContentValues();
        values.put("name", "Christian");
        values.put("age", 25);
        values.put("address", "AT");
        mDatabase.insert(TABLE_NAME, "name", values);

        values = new ContentValues();
        values.put("name", "Sharon");
        values.put("age", 20);
        values.put("address", "LA");
        mDatabase.insert(TABLE_NAME, "name", values);

        long count = mDatabase.count("test");
        assertEquals(3, count);

        count = mDatabase.count(TABLE_NAME, "WHERE AGE = 20");
        assertEquals(2, count);

        count = mDatabase.count(TABLE_NAME, " WHERE AGE = 20");
        assertEquals(2, count);

        count = mDatabase.count(TABLE_NAME, "AGE = 20");
        assertEquals(2, count);

        count = mDatabase.count(TABLE_NAME, "WHERE AGE = ?", new String[] { "25" });
        assertEquals(1, count);
    }

@TestTargetNew(
level = TestLevel.COMPLETE,
notes = "Test delete(String, String, String[])",







