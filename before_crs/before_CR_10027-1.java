/*Fixed Issue 2682: ContentProvider query() SDK docs deprecated*/
//Synthetic comment -- diff --git a/core/java/android/content/ContentProvider.java b/core/java/android/content/ContentProvider.java
//Synthetic comment -- index 5cc5730..e623c25 100644

//Synthetic comment -- @@ -289,9 +289,10 @@
* Example client call:<p>
* <pre>// Request a specific record.
* Cursor managedCursor = managedQuery(
                Contacts.People.CONTENT_URI.addId(2),
projection,    // Which columns to return.
null,          // WHERE clause.
People.NAME + " ASC");   // Sort order.</pre>
* Example implementation:<p>
* <pre>// SQLiteQueryBuilder is a helper class that creates the
//Synthetic comment -- @@ -327,6 +328,9 @@
*      null all columns are included.
* @param selection A selection criteria to apply when filtering rows.
*      If null then all rows are included.
* @param sortOrder How the rows in the cursor should be sorted.
*        If null then the provider is free to define the sort order.
* @return a Cursor or null.







