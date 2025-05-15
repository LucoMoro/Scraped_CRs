//<Beginning of snippet n. 0>
result = ContentUris.withAppendedId(tableUri, rowId);

// Explicit Media Type Check
if (isAudioMediaType()) {
    mMediaProvider.update(result, values, null, null);
    if (mProcessGenres && mGenre != null) {
        String genre = mGenre;
        Uri uri = mGenreCache.get(genre);
        if (uri == null) {
            // Logic to handle genre registration
        }
    }
}
//<End of snippet n. 0>