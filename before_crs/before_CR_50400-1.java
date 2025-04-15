/*Complete documentation of whereArgs

Complete the missing documentation for the
whereArgs argument in delete, update and
updateWithOnConflict

Change-Id:I451ec9e0747c7655c612a4506f40152af0adcf3aSigned-off-by: Tim Roes <tim.roes88@googlemail.com>*/
//Synthetic comment -- diff --git a/core/java/android/database/sqlite/SQLiteDatabase.java b/core/java/android/database/sqlite/SQLiteDatabase.java
//Synthetic comment -- index e2d44f2..0badb6c 100644

//Synthetic comment -- @@ -1481,6 +1481,9 @@
* @param table the table to delete from
* @param whereClause the optional WHERE clause to apply when deleting.
*            Passing null will delete all rows.
* @return the number of rows affected if a whereClause is passed in, 0
*         otherwise. To remove all rows and get a count pass "1" as the
*         whereClause.
//Synthetic comment -- @@ -1508,6 +1511,9 @@
*            valid value that will be translated to NULL.
* @param whereClause the optional WHERE clause to apply when updating.
*            Passing null will update all rows.
* @return the number of rows affected
*/
public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
//Synthetic comment -- @@ -1522,6 +1528,9 @@
*            valid value that will be translated to NULL.
* @param whereClause the optional WHERE clause to apply when updating.
*            Passing null will update all rows.
* @param conflictAlgorithm for update conflict resolver
* @return the number of rows affected
*/







