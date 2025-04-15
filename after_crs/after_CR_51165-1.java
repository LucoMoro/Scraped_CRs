/*Notify change of movePlaylistEntry after transaction ends

When a client asks the Media Provider to rearrange a playlist
the notifyChange callback function is called before the database
transaction is ended resulting in that listeners can't get the
new data when they get the callback.

notifyChange() is called before the database transaction is ended
resulting in that listeners can't get the new data when they get the
callback.

This fix will wait until the database transaction is finished
to do the callback.

Change-Id:Ifdfbb047b809bd5fba4752bcdd299927f9082eee*/




//Synthetic comment -- diff --git a/src/com/android/providers/media/MediaProvider.java b/src/com/android/providers/media/MediaProvider.java
//Synthetic comment -- index c73871d..ed8c543 100644

//Synthetic comment -- @@ -4211,8 +4211,8 @@
return 0;
}
db.beginTransaction();
        int numlines = 0;
try {
helper.mNumUpdates += 3;
Cursor c = db.query("audio_playlists_map",
new String [] {"play_order" },
//Synthetic comment -- @@ -4250,13 +4250,15 @@
db.execSQL("UPDATE audio_playlists_map SET play_order=" + to_play_order +
" WHERE play_order=-1 AND playlist_id=" + playlist);
db.setTransactionSuccessful();
} finally {
db.endTransaction();
}

        Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
                .buildUpon().appendEncodedPath(String.valueOf(playlist)).build();
        getContext().getContentResolver().notifyChange(uri, null);

        return numlines;
}

private static final String[] openFileColumns = new String[] {







