/*NullPointerException in SQLiteSession

I have found this bug with the FindBugs tool

Change-Id:Ia167882f98c3cdb24332f420e0505e1af929b770Signed-off-by: László Dávid <laszlo.david@gmail.com>*/




//Synthetic comment -- diff --git a/core/java/android/database/sqlite/SQLiteSession.java b/core/java/android/database/sqlite/SQLiteSession.java
//Synthetic comment -- index beb5b3a..d86a91f 100644

//Synthetic comment -- @@ -926,7 +926,7 @@
}

private void throwIfNestedTransaction() {
        if (mTransactionStack != null && mTransactionStack.mParent != null) {
throw new IllegalStateException("Cannot perform this operation because "
+ "a nested transaction is in progress.");
}







