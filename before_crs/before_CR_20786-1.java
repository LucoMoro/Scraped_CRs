/*Delete album when all of its tracks have been deleted

When an entire album or the last track from an album is deleted the
entry in the "albums" table is not removed. If tracks from the same
album are later found again by the MediaScanner then the album will
not get any album art.
Fixed by adding a trigger that removes the "albums" table entry if
no tracks with the same album_id are found after a delete.

Change-Id:I31d30bfe9cf067e6378b3bb3c4f53152b9c1e5cf*/
//Synthetic comment -- diff --git a/src/com/android/providers/media/MediaProvider.java b/src/com/android/providers/media/MediaProvider.java
//Synthetic comment -- index 0bc332b..2e21b24 100644

//Synthetic comment -- @@ -417,6 +417,7 @@
db.execSQL("DROP TABLE IF EXISTS audio_playlists");
db.execSQL("DROP TABLE IF EXISTS audio_playlists_map");
db.execSQL("DROP TRIGGER IF EXISTS audio_playlists_cleanup");
db.execSQL("DROP TRIGGER IF EXISTS albumart_cleanup1");
db.execSQL("DROP TRIGGER IF EXISTS albumart_cleanup2");
db.execSQL("DROP TABLE IF EXISTS video");
//Synthetic comment -- @@ -590,6 +591,13 @@
"SELECT _DELETE_FILE(old._data);" +
"END");

// Cleans up album_art table entry when an album is deleted
db.execSQL("CREATE TRIGGER IF NOT EXISTS albumart_cleanup1 DELETE ON albums " +
"BEGIN " +







