/*Fixes Issue 3731 in the public bugs database (http://code.google.com/p/android/issues/detail?id=3731).

`getColumnNames` of `android.database.sqlite.SQLiteCursor` was simply
returning the `mColumns` instance variable. Any modification to the returned
array of column names also affected `mColumns`.

Change-Id:I6512883f49545812b46b48f9c6a17cab541d7b7a*/
//Synthetic comment -- diff --git a/core/java/android/database/sqlite/SQLiteCursor.java b/core/java/android/database/sqlite/SQLiteCursor.java
//Synthetic comment -- index c7e58faf..1d4ae33 100644

//Synthetic comment -- @@ -377,7 +377,9 @@

@Override
public String[] getColumnNames() {
        return mColumns;
}

/**







