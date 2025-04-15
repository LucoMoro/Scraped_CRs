/*Keep native callbacks when primary SQLiteConnection is recreated

When a database was ATTACHed/DETACHed to a database that had Write-Ahead
Logging enabled, the primary SQLConnection was recreated but the
registered native callbacks were not restored.

Change-Id:I8787fee78e68197ae472e05cc694d11381defa71*/
//Synthetic comment -- diff --git a/core/java/android/database/sqlite/SQLiteConnection.java b/core/java/android/database/sqlite/SQLiteConnection.java
//Synthetic comment -- index 6f7c1f3..e89a25f 100644

//Synthetic comment -- @@ -216,6 +216,13 @@
setJournalSizeLimit();
setAutoCheckpointInterval();
setLocaleFromConfiguration();
}

private void dispose(boolean finalized) {







