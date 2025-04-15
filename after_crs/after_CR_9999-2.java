/*Add is_ringtone into SQL video table.*/




//Synthetic comment -- diff --git a/src/com/android/providers/media/MediaProvider.java b/src/com/android/providers/media/MediaProvider.java
//Synthetic comment -- index 1a2f5a6..1a635ca 100644

//Synthetic comment -- @@ -561,6 +561,11 @@
// To work around this, we drop and recreate the affected view and trigger.
recreateAudioView(db);
}

        if (fromVersion < 73) {
            // Add ringtone flag to video table, matching audio ringtones
            db.execSQL("ALTER TABLE video ADD COLUMN is_ringtone INTEGER;");
        }
}

private static void recreateAudioView(SQLiteDatabase db) {
//Synthetic comment -- @@ -2112,7 +2117,7 @@

private static String TAG = "MediaProvider";
private static final boolean LOCAL_LOGV = true;
    private static final int DATABASE_VERSION = 73;
private static final String INTERNAL_DATABASE_NAME = "internal.db";

// maximum number of cached external databases to keep







