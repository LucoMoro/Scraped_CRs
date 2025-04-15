/*DO NOT MERGE Modify browser file origin policy.

Bug: 6212665

Modify browser's file origin policy to match Chrome's. This is
a cherry-pick from master. sha:
   559317c0ef684a8e9a77443343ff61a263e31168

Change-Id:Idafb4f93f61d9503df6cb0d6cedcc85a00a9827b*/




//Synthetic comment -- diff --git a/src/com/android/browser/BrowserSettings.java b/src/com/android/browser/BrowserSettings.java
//Synthetic comment -- index 2369554..3327e4c 100644

//Synthetic comment -- @@ -318,6 +318,9 @@
settings.setAppCachePath(getAppCachePath());
settings.setDatabasePath(mContext.getDir("databases", 0).getPath());
settings.setGeolocationDatabasePath(mContext.getDir("geolocation", 0).getPath());
        // origin policy for file access
        settings.setAllowUniversalAccessFromFileURLs(false);
        settings.setAllowFileAccessFromFileURLs(false);
}

private void syncSharedSettings() {







