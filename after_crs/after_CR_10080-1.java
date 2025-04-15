/*Fixed Issue 2682: ContentProvider query() SDK docs deprecated*/




//Synthetic comment -- diff --git a/core/java/android/content/ContentProvider.java b/core/java/android/content/ContentProvider.java
//Synthetic comment -- index 5cc5730..84cfa54 100644

//Synthetic comment -- @@ -289,9 +289,10 @@
* Example client call:<p>
* <pre>// Request a specific record.
* Cursor managedCursor = managedQuery(
                ContentUris.withAppendedId(Contacts.People.CONTENT_URI, 2),
projection,    // Which columns to return.
null,          // WHERE clause.
                null,          // WHERE clause value substitution
People.NAME + " ASC");   // Sort order.</pre>
* Example implementation:<p>
* <pre>// SQLiteQueryBuilder is a helper class that creates the
//Synthetic comment -- @@ -320,15 +321,18 @@
return c;</pre>
*
* @param uri The URI to query. This will be the full URI sent by the client;
     *      if the client is requesting a specific record, the URI will end in a record number
     *      that the implementation should parse and add to a WHERE or HAVING clause, specifying
     *      that _id value.
* @param projection The list of columns to put into the cursor. If
*      null all columns are included.
* @param selection A selection criteria to apply when filtering rows.
*      If null then all rows are included.
     * @param selectionArgs You may include ?s in selection, which will be replaced by
     *      the values from selectionArgs, in order that they appear in the selection.
     *      The values will be bound as Strings.
* @param sortOrder How the rows in the cursor should be sorted.
     *      If null then the provider is free to define the sort order.
* @return a Cursor or null.
*/
public abstract Cursor query(Uri uri, String[] projection,







