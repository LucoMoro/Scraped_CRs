/*QuickSearchBox: Avoid false positive log in finalize

CursorBackedSuggestionCursor, when created with a null cursor
by CursorBackedSourceResult, doesn't require a close to be
called on the cursor. However, the log in finalize misleads
of a leak.

Change-Id:Ib24e42a5c82edbd92d17d184f19ae4fc07dc0281*/




//Synthetic comment -- diff --git a/src/com/android/quicksearchbox/CursorBackedSuggestionCursor.java b/src/com/android/quicksearchbox/CursorBackedSuggestionCursor.java
//Synthetic comment -- index 179542f..960a9d7 100644

//Synthetic comment -- @@ -101,7 +101,7 @@

@Override
protected void finalize() {
        if (mCursor != null && !mClosed) {
Log.e(TAG, "LEAK! Finalized without being closed: " + toString());
}
}







