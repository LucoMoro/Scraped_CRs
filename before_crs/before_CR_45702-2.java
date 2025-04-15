/*Potential NPE in SQLiteConnection

Change-Id:I4d768bda5ea2fccb4a7c6ce7a5f074bf41563eacSigned-off-by: Sylvain Becuwe <sylvain.becuwe@gmail.com>*/
//Synthetic comment -- diff --git a/core/java/android/database/sqlite/SQLiteConnection.java b/core/java/android/database/sqlite/SQLiteConnection.java
//Synthetic comment -- index 6f7c1f3..b2b6106 100644

//Synthetic comment -- @@ -974,7 +974,7 @@
if (count != statement.mNumParameters) {
throw new SQLiteBindOrColumnIndexOutOfRangeException(
"Expected " + statement.mNumParameters + " bind arguments but "
                    + bindArgs.length + " were provided.");
}
if (count == 0) {
return;







