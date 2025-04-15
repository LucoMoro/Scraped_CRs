/*Fix startSearch condition on Dialog, because pressing search key causes ANR popup.
If search key is pressed on Dialog and there is no search item then do nothing.
How to reproduce the issue:
Settings > About phone > Legal information > Google legal > Press search key > Touch list item or press back key > ANR popup is shown

Change-Id:I9c24d83ca3b7c20976bb7daebeff7fd694ce3a2f*/




//Synthetic comment -- diff --git a/core/java/android/app/Dialog.java b/core/java/android/app/Dialog.java
//Synthetic comment -- index da8c9e5..d70ec0b 100644

//Synthetic comment -- @@ -823,7 +823,7 @@

// associate search with owner activity
final ComponentName appName = getAssociatedActivity();
        if (appName != null && searchManager.getSearchableInfo(appName) != null) {
searchManager.startSearch(null, false, appName, null, false);
dismiss();
return true;







