/*Disable usage of WAL in SettingsProvider.

According to SQLite documentation, WAL can not be used when database
partition is not accessible for writing (even when the client just
performs read operation).
However, such situation may happen when the partition is full.
As SettingsProvider should always be able to read a value of ANDROID_ID
from corresponding database, this commit disables usage of WAL.
Without this we risk ending up in a situation where /data is full
and the phone is unable to boot properly.

Change-Id:I1f79bbcd8d0f64bf35dc9d7b846bcfb2664d2eac*/




//Synthetic comment -- diff --git a/packages/SettingsProvider/src/com/android/providers/settings/SettingsProvider.java b/packages/SettingsProvider/src/com/android/providers/settings/SettingsProvider.java
//Synthetic comment -- index 8086bbc..4b51fab 100644

//Synthetic comment -- @@ -385,6 +385,9 @@
// initialization with any of our own locks held, so we're fine.
SQLiteDatabase db = dbhelper.getWritableDatabase();

        // Disalbe WAL to ensure we can read settings even if the filesystem is full.
        db.disableWriteAheadLogging();

// Watch for external modifications to the database files,
// keeping our caches in sync.  We synchronize the observer set
// separately, and of course it has to run after the db file







