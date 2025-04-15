/*Avoid crash in browser in systems with no search provider

If there's no activity registered for the intent (action)
ACTION_WEB_SEARCH the browser will crash if a search
query is entered in the browser. The crash is avoided
by catching the ActivityNotFoundException.

Change-Id:I021d3210654bb3aa08ccdd15abea86ef415282b1*/




//Synthetic comment -- diff --git a/src/com/android/browser/BrowserActivity.java b/src/com/android/browser/BrowserActivity.java
//Synthetic comment -- index 5e55789..29e333a 100644

//Synthetic comment -- @@ -629,7 +629,13 @@
intent.putExtra(SearchManager.EXTRA_DATA_KEY, extraData);
}
intent.putExtra(Browser.EXTRA_APPLICATION_ID, getPackageName());

        // can't be sure there is an activity for the Intent
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            return false;
        }

return true;
}







