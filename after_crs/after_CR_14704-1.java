/*Add some documentation about the thread safety of Cursor and some of the SQLite* classes.

Change-Id:Icae51052d1c942d7d60bb958d3703411da001079*/




//Synthetic comment -- diff --git a/core/java/android/database/Cursor.java b/core/java/android/database/Cursor.java
//Synthetic comment -- index 79178f4..4796a9d 100644

//Synthetic comment -- @@ -25,6 +25,9 @@
/**
* This interface provides random read-write access to the result set returned
* by a database query.
 *
 * Cursor implementations are not required to be synchronized so code using a Cursor from multiple
 * threads should perform it's own synchronization when using the Cursor.
*/
public interface Cursor {
/**








//Synthetic comment -- diff --git a/core/java/android/database/sqlite/SQLiteCursor.java b/core/java/android/database/sqlite/SQLiteCursor.java
//Synthetic comment -- index 70b9b83..ce4d809 100644

//Synthetic comment -- @@ -36,6 +36,9 @@
/**
* A Cursor implementation that exposes results from a query on a
* {@link SQLiteDatabase}.
 *
 * SQLiteCursor is not internally synchronized so code using a SQLiteCursor from multiple
 * threads should perform it's own synchronization when using the SQLiteCursor.
*/
public class SQLiteCursor extends AbstractWindowedCursor {
static final String TAG = "Cursor";








//Synthetic comment -- diff --git a/core/java/android/database/sqlite/SQLiteDatabase.java b/core/java/android/database/sqlite/SQLiteDatabase.java
//Synthetic comment -- index 9ebf5d9..342c0f5 100644

//Synthetic comment -- @@ -1019,7 +1019,8 @@
*
* @param sql The raw SQL statement, may contain ? for unknown values to be
*            bound later.
     * @return A pre-compiled {@link SQLiteStatement} object. Note that
     * {@link SQLiteStatement}s are not synchronized, see the documentation for more details.
*/
public SQLiteStatement compileStatement(String sql) throws SQLException {
lock();
//Synthetic comment -- @@ -1057,7 +1058,8 @@
*            default sort order, which may be unordered.
* @param limit Limits the number of rows returned by the query,
*            formatted as LIMIT clause. Passing null denotes no LIMIT clause.
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     * {@link Cursor}s are not synchronized, see the documentation for more details.
* @see Cursor
*/
public Cursor query(boolean distinct, String table, String[] columns,
//Synthetic comment -- @@ -1095,7 +1097,8 @@
*            default sort order, which may be unordered.
* @param limit Limits the number of rows returned by the query,
*            formatted as LIMIT clause. Passing null denotes no LIMIT clause.
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     * {@link Cursor}s are not synchronized, see the documentation for more details.
* @see Cursor
*/
public Cursor queryWithFactory(CursorFactory cursorFactory,
//Synthetic comment -- @@ -1133,7 +1136,8 @@
* @param orderBy How to order the rows, formatted as an SQL ORDER BY clause
*            (excluding the ORDER BY itself). Passing null will use the
*            default sort order, which may be unordered.
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     * {@link Cursor}s are not synchronized, see the documentation for more details.
* @see Cursor
*/
public Cursor query(String table, String[] columns, String selection,
//Synthetic comment -- @@ -1170,7 +1174,8 @@
*            default sort order, which may be unordered.
* @param limit Limits the number of rows returned by the query,
*            formatted as LIMIT clause. Passing null denotes no LIMIT clause.
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     * {@link Cursor}s are not synchronized, see the documentation for more details.
* @see Cursor
*/
public Cursor query(String table, String[] columns, String selection,
//Synthetic comment -- @@ -1188,7 +1193,8 @@
* @param selectionArgs You may include ?s in where clause in the query,
*     which will be replaced by the values from selectionArgs. The
*     values will be bound as Strings.
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     * {@link Cursor}s are not synchronized, see the documentation for more details.
*/
public Cursor rawQuery(String sql, String[] selectionArgs) {
return rawQueryWithFactory(null, sql, selectionArgs, null);
//Synthetic comment -- @@ -1203,7 +1209,8 @@
*     which will be replaced by the values from selectionArgs. The
*     values will be bound as Strings.
* @param editTable the name of the first table, which is editable
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     * {@link Cursor}s are not synchronized, see the documentation for more details.
*/
public Cursor rawQueryWithFactory(
CursorFactory cursorFactory, String sql, String[] selectionArgs,
//Synthetic comment -- @@ -1255,7 +1262,8 @@
*     values will be bound as Strings.
* @param initialRead set the initial count of items to read from the cursor
* @param maxRead set the count of items to read on each iteration after the first
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     * {@link Cursor}s are not synchronized, see the documentation for more details.
*
* This work is incomplete and not fully tested or reviewed, so currently
* hidden.








//Synthetic comment -- diff --git a/core/java/android/database/sqlite/SQLiteProgram.java b/core/java/android/database/sqlite/SQLiteProgram.java
//Synthetic comment -- index 9e85452..4cc5593 100644

//Synthetic comment -- @@ -20,6 +20,9 @@

/**
* A base class for compiled SQLite programs.
 *
 * SQLiteProgram is not internally synchronized so code using a SQLiteProgram from multiple
 * threads should perform it's own synchronization when using the SQLiteProgram.
*/
public abstract class SQLiteProgram extends SQLiteClosable {
private static final String TAG = "SQLiteProgram";








//Synthetic comment -- diff --git a/core/java/android/database/sqlite/SQLiteQuery.java b/core/java/android/database/sqlite/SQLiteQuery.java
//Synthetic comment -- index cdd9f86..507d04e 100644

//Synthetic comment -- @@ -23,6 +23,9 @@
/**
* A SQLite program that represents a query that reads the resulting rows into a CursorWindow.
* This class is used by SQLiteCursor and isn't useful itself.
 *
 * SQLiteQuery is not internally synchronized so code using a SQLiteQuery from multiple
 * threads should perform it's own synchronization when using the SQLiteQuery.
*/
public class SQLiteQuery extends SQLiteProgram {
private static final String TAG = "Cursor";








//Synthetic comment -- diff --git a/core/java/android/database/sqlite/SQLiteStatement.java b/core/java/android/database/sqlite/SQLiteStatement.java
//Synthetic comment -- index 5889ad9..8c2558f 100644

//Synthetic comment -- @@ -24,6 +24,9 @@
* The statement cannot return multiple rows, but 1x1 result sets are allowed.
* Don't use SQLiteStatement constructor directly, please use
* {@link SQLiteDatabase#compileStatement(String)}
 *
 * SQLiteStatement is not internally synchronized so code using a SQLiteStatement from multiple
 * threads should perform it's own synchronization when using the SQLiteStatement.
*/
public class SQLiteStatement extends SQLiteProgram
{







