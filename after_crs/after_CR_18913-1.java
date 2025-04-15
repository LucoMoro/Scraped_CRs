/*Added builQuery and builUnionSubQuery methods without misleading selectionArgs parameter.

The signatures of the existing buildQuery and buildUnionSubQuery methods include a selectionArgs
parameter that is not actually being used in the method implementations.  This parameter leads
to the misconception that SQL paramter substitution is carried out by these methods.  I added
new variants of these methods without that parameter and deprecated the old variants.

Change-Id:I1bf770d5c777649e9aac36d93aa93bd65bbcc2a3*/




//Synthetic comment -- diff --git a/core/java/android/database/sqlite/SQLiteQueryBuilder.java b/core/java/android/database/sqlite/SQLiteQueryBuilder.java
//Synthetic comment -- index 610bf70..423997e 100644

//Synthetic comment -- @@ -321,7 +321,7 @@
}

String sql = buildQuery(
                projectionIn, selection, groupBy, having,
sortOrder, limit);

if (Log.isLoggable(TAG, Log.DEBUG)) {
//Synthetic comment -- @@ -345,10 +345,6 @@
*   formatted as an SQL WHERE clause (excluding the WHERE
*   itself).  Passing null will return all rows for the given
*   URL.
* @param groupBy A filter declaring how to group rows, formatted
*   as an SQL GROUP BY clause (excluding the GROUP BY itself).
*   Passing null will cause the rows to not be grouped.
//Synthetic comment -- @@ -365,8 +361,8 @@
* @return the resulting SQL SELECT statement
*/
public String buildQuery(
            String[] projectionIn, String selection, String groupBy,
            String having, String sortOrder, String limit) {
String[] projection = computeProjection(projectionIn);

StringBuilder where = new StringBuilder();
//Synthetic comment -- @@ -394,6 +390,19 @@
}

/**
     * @deprecated This method's signature is misleading since no SQL parameter
     * substitution is carried out.  The selection arguments parameter does not get
     * used at all.  To avoid confusion, call
     * {@link #buildQuery(String[], String, String, String, String, String)} instead.
     */
    @Deprecated
    public String buildQuery(
            String[] projectionIn, String selection, String[] selectionArgs,
            String groupBy, String having, String sortOrder, String limit) {
        return buildQuery(projectionIn, selection, groupBy, having, sortOrder, limit);
    }

    /**
* Construct a SELECT statement suitable for use in a group of
* SELECT statements that will be joined through UNION operators
* in buildUnionQuery.
//Synthetic comment -- @@ -422,10 +431,6 @@
*   formatted as an SQL WHERE clause (excluding the WHERE
*   itself).  Passing null will return all rows for the given
*   URL.
* @param groupBy A filter declaring how to group rows, formatted
*   as an SQL GROUP BY clause (excluding the GROUP BY itself).
*   Passing null will cause the rows to not be grouped.
//Synthetic comment -- @@ -443,7 +448,6 @@
int computedColumnsOffset,
String typeDiscriminatorValue,
String selection,
String groupBy,
String having) {
int unionColumnsCount = unionColumns.length;
//Synthetic comment -- @@ -463,12 +467,35 @@
}
}
return buildQuery(
                projectionIn, selection, groupBy, having,
null /* sortOrder */,
null /* limit */);
}

/**
     * @deprecated This method's signature is misleading since no SQL parameter
     * substitution is carried out.  The selection arguments parameter does not get
     * used at all.  To avoid confusion, call
     * {@link #buildUnionSubQuery(String, String[], Set<String>, int, String, String, String, String)}
     * instead.
     */
    public String buildUnionSubQuery(
            String typeDiscriminatorColumn,
            String[] unionColumns,
            Set<String> columnsPresentInTable,
            int computedColumnsOffset,
            String typeDiscriminatorValue,
            String selection,
            String[] selectionArgs,
            String groupBy,
            String having) {
        return buildUnionSubQuery(
                typeDiscriminatorColumn, unionColumns, columnsPresentInTable,
                computedColumnsOffset, typeDiscriminatorValue, selection,
                groupBy, having);
    }

    /**
* Given a set of subqueries, all of which are SELECT statements,
* construct a query that returns the union of what those
* subqueries return.







