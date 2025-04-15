/*Adding null check on Cursor c in MediaProvider.getAlbumArtOutputUri

The Cursor c in MediaProvider.getAlbumArtOutputUri.
MediaProvider.query() can clearly return null.

Adding a null check, setting albumart_uri to null in that case,
should solve the problem, and should be safe as the only caller
of getAlbumArtOutputUri expects and handles a null return.

Change-Id:I5f71dc41a4516d343fe6638117cdd9bdf3eb8f82*/




//Synthetic comment -- diff --git a/src/com/android/providers/media/MediaProvider.java b/src/com/android/providers/media/MediaProvider.java
//Synthetic comment -- index 33ff7dc..c13978a 100644

//Synthetic comment -- @@ -2617,15 +2617,20 @@
if (albumart_uri != null) {
Cursor c = query(albumart_uri, new String [] { "_data" },
null, null, null);
            try {
                if (c != null && c.moveToFirst()) {
                    String albumart_path = c.getString(0);
                    if (ensureFileExists(albumart_path)) {
                        out = albumart_uri;
                    }
                } else {
                    albumart_uri = null;
}
            } finally {
                if (c != null) {
                    c.close();
                }
}
}
if (albumart_uri == null){
ContentValues initialValues = new ContentValues();







