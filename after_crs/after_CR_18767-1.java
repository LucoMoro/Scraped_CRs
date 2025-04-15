/*QuickSearch: Fix the network connection timeout constant

Searchdialog creates a http client by defining the network connection
timeout constant. The constant name has been modified in the latest
apache version. Hence, modified the suggestion provider to use this
constant.

Change-Id:I7d83e70d919b2a92a51cdecef4973b5a77fc6f6d*/




//Synthetic comment -- diff --git a/src/com/android/quicksearchbox/google/GoogleSuggestionProvider.java b/src/com/android/quicksearchbox/google/GoogleSuggestionProvider.java
//Synthetic comment -- index b461a98..3529b4a 100644

//Synthetic comment -- @@ -59,7 +59,7 @@
private static final int HTTP_TIMEOUT_MS = 1000;

// TODO: this should be defined somewhere
    private static final String HTTP_TIMEOUT = "http.conn-manager.timeout";

// Indexes into COLUMNS
private static final int COL_ID = 0;







