//<Beginning of snippet n. 0>
result = ContentUris.withAppendedId(tableUri, rowId);

// Explicit check for media type
if (isAudioMediaType(rowId)) {
    mMediaProvider.update(result, values, null, null);
}

if (mProcessGenres && mGenre != null) {
    String genre = mGenre;
    Uri uri = mGenreCache.get(genre);
    if (uri == null) {
//<End of snippet n. 0>