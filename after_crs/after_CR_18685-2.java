/*Fixes Issue 3731 in the public bugs database (http://code.google.com/p/android/issues/detail?id=3731).

The documentation of `android.database.Cursor` was updated to mention that
the array that is returned by `getColumnNames` must not be modified.

Change-Id:I6512883f49545812b46b48f9c6a17cab541d7b7a*/




//Synthetic comment -- diff --git a/core/java/android/database/Cursor.java b/core/java/android/database/Cursor.java
//Synthetic comment -- index 6539156..d42bb40 100644

//Synthetic comment -- @@ -198,6 +198,9 @@
* Returns a string array holding the names of all of the columns in the
* result set in the order in which they were listed in the result.
*
     * <p>The returned array must not be modified because it may be a window
     * into internal state.
     *
* @return the names of the columns returned in this query.
*/
String[] getColumnNames();








//Synthetic comment -- diff --git a/core/java/android/database/sqlite/SQLiteCursor.java b/core/java/android/database/sqlite/SQLiteCursor.java
//Synthetic comment -- index c7e58faf..c09bb25f 100644

//Synthetic comment -- @@ -375,6 +375,15 @@
}
}

    /**
     * Returns a string array holding the names of all of the columns in the
     * result set in the order in which they were listed in the result.
     *
     * <p>The returned array must not be modified because it is a window into
     * critical internal state.
     *
     * @return the names of the columns returned in this query.
     */
@Override
public String[] getColumnNames() {
return mColumns;







