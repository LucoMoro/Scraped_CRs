/*Fix for truncated sentence in "Find on Page" field

When typing a text in find on page, then deleting it again the
default text in the field "Find on Page" is truncated by
an invisible view.

This commit fixes the problem by setting the visibility of
mMatches view changes to View.GONE instead of View.INVISIBLE.

Change-Id:I9889f22a254cdabb0cbaba72bff4b14158e5c9ff*/




//Synthetic comment -- diff --git a/core/java/android/webkit/FindActionModeCallback.java b/core/java/android/webkit/FindActionModeCallback.java
//Synthetic comment -- index 1a4ccfa..6a627e1 100644

//Synthetic comment -- @@ -152,7 +152,7 @@
mActiveMatchIndex = matchIndex;
updateMatchesString();
} else {
            mMatches.setVisibility(View.GONE);
mNumberOfMatches = 0;
}
}







