/*Fixed ANR in dir.list

Thumbnails are no longer used. Fixed very unlikely ANR in dir.list
(directory listing is a file operation even for an empty directory),
by moving directory listing to an async task.
The async task is needed because the user may have updated from
Android 2.1 where thumbnails are used.

Change-Id:I93b9dc88f4c3417dcdc8e4ffbc49ae1d6df4dd0f*/
//Synthetic comment -- diff --git a/src/com/android/browser/BrowserActivity.java b/src/com/android/browser/BrowserActivity.java
//Synthetic comment -- index 44ab307..15531d5 100644

//Synthetic comment -- @@ -146,13 +146,14 @@
private final static boolean LOGV_ENABLED = com.android.browser.Browser.LOGV_ENABLED;
private final static boolean LOGD_ENABLED = com.android.browser.Browser.LOGD_ENABLED;

    private static class ClearThumbnails extends AsyncTask<File, Void, Void> {
@Override
        public Void doInBackground(File... files) {
if (files != null) {
for (File f : files) {
if (!f.delete()) {
                      Log.e(LOGTAG, f.getPath() + " was not deleted");
}
}
}
//Synthetic comment -- @@ -311,10 +312,6 @@
registerReceiver(mPackageInstallationReceiver, filter);

if (!mTabControl.restoreState(icicle)) {
            // clear up the thumbnail directory if we can't restore the state as
            // none of the files in the directory are referenced any more.
            new ClearThumbnails().execute(
                    mTabControl.getThumbnailDir().listFiles());
// there is no quit on Android. But if we can't restore the state,
// we can treat it as a new Browser, remove the old session cookies.
CookieManager.getInstance().removeSessionCookie();
//Synthetic comment -- @@ -354,14 +351,9 @@
attachTabToContentView(mTabControl.getCurrentTab());
}

        // Delete old thumbnails to save space
        File dir = mTabControl.getThumbnailDir();
        if (dir.exists()) {
            for (String child : dir.list()) {
                File f = new File(dir, child);
                f.delete();
            }
        }

// Read JavaScript flags if it exists.
String jsFlags = mSettings.getJsFlags();








//Synthetic comment -- diff --git a/src/com/android/browser/TabControl.java b/src/com/android/browser/TabControl.java
//Synthetic comment -- index afd4ea8..5deea00 100644

//Synthetic comment -- @@ -45,8 +45,6 @@
// A private instance of BrowserActivity to interface with when adding and
// switching between tabs.
private final BrowserActivity mActivity;
    // Directory to store thumbnails for each WebView.
    private final File mThumbnailDir;

/**
* Construct a new TabControl object that interfaces with the given
//Synthetic comment -- @@ -56,11 +54,6 @@
*/
TabControl(BrowserActivity activity) {
mActivity = activity;
        mThumbnailDir = activity.getDir("thumbnails", 0);
    }

    File getThumbnailDir() {
        return mThumbnailDir;
}

BrowserActivity getBrowserActivity() {







