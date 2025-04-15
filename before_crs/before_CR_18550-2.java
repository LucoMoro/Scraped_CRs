/*Dont store video genre metadata as audio genre in MediaScanner

MediaScanner mistake Video_ID for Auido_ID when MediaScanner
register genre of mp4 video content that this content have
genre data.

Add an explicit check for audio.

Change-Id:Ic27638e9e59af44e388a8c38d774a077f23520b7*/
//Synthetic comment -- diff --git a/media/java/android/media/MediaScanner.java b/media/java/android/media/MediaScanner.java
//Synthetic comment -- index 3333268..966266e 100644

//Synthetic comment -- @@ -781,7 +781,7 @@
result = ContentUris.withAppendedId(tableUri, rowId);
mMediaProvider.update(result, values, null, null);
}
            if (mProcessGenres && mGenre != null) {
String genre = mGenre;
Uri uri = mGenreCache.get(genre);
if (uri == null) {







