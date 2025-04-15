/*Workaround for SQLite limitation for ORDER BY calculated columns in UNION queries

Due to unknown reasons SQLite can't compile statements like
	SELECT * FROM a
	UNION
	SELECT * FROM b
	ORDER BY some_function(col)

The most elegant workaround I found is rewriting statement to the form of
	SELECT * FROM
		(SELECT * FROM a
		 UNION
		 SELECT * FROM b)
	ORDER BY some_function(col)

Such queries are usefull when implementing custom ordering for some text labels, i.e. contact names.
If you develop some localized application it may be needed to place label in native language before English labels.

In this situation ORDER BY clause like
ORDER BY
	(CASE
		WHEN SUBSTR(label,1,1) BETWEEN 'A' AND 'Z' THEN 1
		ELSE 0
	 END),
	 label

is obvious way (assuming that labels contains only uppercase letters).
Unfortunately this ORDER BY clause can't be used within SQLiteQueryBuilder.buildUnionQuery method due to SQLite limitation listed above.

Change-Id:Ib60ca6c4bd33f23c67f214e6d6ff8e3effb4bfcc*/




//Synthetic comment -- diff --git a/core/java/android/database/sqlite/SQLiteQueryBuilder.java b/core/java/android/database/sqlite/SQLiteQueryBuilder.java
old mode 100644
new mode 100755
//Synthetic comment -- index 8f8eb6e..b7959d0

//Synthetic comment -- @@ -552,12 +552,29 @@
int subQueryCount = subQueries.length;
String unionOperator = mDistinct ? " UNION " : " UNION ALL ";

		/* d.bulashevich@gmail.com 
		 *
		 * Due to unknown reasons SQLite can't compile statements like
		 * SELECT * FROM a
		 * UNION
		 * SELECT * FROM b
		 * ORDER BY some_function(col)
		 *
		 * The most elegant workaround I found is rewriting statement to the form of
		 * SELECT * FROM
		 * 		(SELECT * FROM a
		 * 		 UNION
		 * 		 SELECT * FROM b)
		 * ORDER BY some_function(col)
		 */
        query.append("SELECT * FROM(");
for (int i = 0; i < subQueryCount; i++) {
if (i > 0) {
query.append(unionOperator);
}
query.append(subQueries[i]);
}
        query.append(")");
appendClause(query, " ORDER BY ", sortOrder);
appendClause(query, " LIMIT ", limit);
return query.toString();







