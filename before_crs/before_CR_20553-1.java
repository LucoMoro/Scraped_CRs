/*frameworks/base: Fix to clear ObserverCall in ContentService

A list of ObserverCall(s) is created by ContentService as it notifies
changes to the registered content observers. This object holds
reference to ObserverNode and IContentObserver and is not being
cleared after notification. Fix is to clear the list.

Change-Id:I49438ccc80973e7b59ffbfc19382972c2dbf3659*/
//Synthetic comment -- diff --git a/core/java/android/content/ContentService.java b/core/java/android/content/ContentService.java
//Synthetic comment -- index fc2dfc0..a79f9de 100644

//Synthetic comment -- @@ -160,6 +160,7 @@
}
}
}
if (syncToNetwork) {
SyncManager syncManager = getSyncManager();
if (syncManager != null) {







