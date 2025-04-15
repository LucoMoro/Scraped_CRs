/*fix issue #1587*/




//Synthetic comment -- diff --git a/core/java/android/database/sqlite/SQLiteQueryBuilder.java b/core/java/android/database/sqlite/SQLiteQueryBuilder.java
//Synthetic comment -- index 519a81c..139fcba 100644

//Synthetic comment -- @@ -351,23 +351,26 @@
String groupBy, String having, String sortOrder, String limit) {
String[] projection = computeProjection(projectionIn);

        StringBuilder where = new StringBuilder();

if (mWhereClause.length() > 0) {
            where.append(mWhereClause.toString());
            where.append(')');
}

// Tack on the user's selection, if present.
if (selection != null && selection.length() > 0) {
if (mWhereClause.length() > 0) {
                where.append(" AND ");
}

            where.append('(');
            where.append(selection);
            where.append(')');
}

return buildQueryString(
                mDistinct, mTables, projection, where.toString(),
groupBy, having, sortOrder, limit);
}








