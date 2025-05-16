//<Beginning of snippet n. 0>
result = ContentUris.withAppendedId(tableUri, rowId);
if (isAudioMediaType(mediaType)) {
    mMediaProvider.update(result, values, null, null);
}
if (mProcessGenres && mGenre != null && isAudioMediaType(mediaType)) {
    String genre = mGenre;
    Uri uri = mGenreCache.get(genre);
    if (uri == null) {
//<End of snippet n. 0>