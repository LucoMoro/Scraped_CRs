/*SearchWidget: passing APP_DATA to voice intent

SearchView uses SearchManager.APP_DATA to pass data between the origin activity
and the search activity. This works for text searches (keyboard), but it's not
implemented by voice recognition searches. The method createVoiceAppSearchIntent
initializes a empty bundle that is filled with the QUERY parameter.
This patch adds the SearchManager.APP_DATA parameter if is present, let the
search activity access to the data passed by the origin activity

Patch 2: Fix code style

Change-Id:I52dce34e1efd07a30ad74d52bfc60fbae1c4310b*/




//Synthetic comment -- diff --git a/core/java/android/widget/SearchView.java b/core/java/android/widget/SearchView.java
//Synthetic comment -- index c44ce8a..86433d4 100644

//Synthetic comment -- @@ -1506,6 +1506,9 @@
// because the voice search activity will always need to insert "QUERY" into
// it anyway.
Bundle queryExtras = new Bundle();
        if (mAppSearchData != null) {
            queryExtras.putParcelable(SearchManager.APP_DATA, mAppSearchData);
        }

// Now build the intent to launch the voice search.  Add all necessary
// extras to launch the voice recognizer, and then all the necessary extras







