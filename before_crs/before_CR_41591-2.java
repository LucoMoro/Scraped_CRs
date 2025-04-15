/*[KLOCWORK][SQL]: fixes null dereference

This patch fixes a null dereference issue reported by
klocwork.

Change-Id:Ibf60045cf8a673dec3087eb7a2743063cc20e7ebAuthor: Ovidiu Beldie <ovidiux.beldie@intel.com>
Signed-off-by: Ovidiu Beldie <ovidiux.beldie@intel.com>
Signed-off-by: Christophe Bransiec <christophex.bransiec@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 34345*/
//Synthetic comment -- diff --git a/core/java/android/database/sqlite/SQLiteQueryBuilder.java b/core/java/android/database/sqlite/SQLiteQueryBuilder.java
//Synthetic comment -- index 91884ab..e4dd2a9 100644

//Synthetic comment -- @@ -372,7 +372,7 @@
public Cursor query(SQLiteDatabase db, String[] projectionIn,
String selection, String[] selectionArgs, String groupBy,
String having, String sortOrder, String limit, CancellationSignal cancellationSignal) {
        if (mTables == null) {
return null;
}








