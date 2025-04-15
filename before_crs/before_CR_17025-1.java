/*NPE in DownloadTouchIcon because of getPreferredHttpHost

The call getPreferredHttpHost needs a context, however there
are two constructors of the class, one of them does not take
a context as input. Added context parameter to the second
constructor.

Change-Id:Ibe670c5b8848ced88b0756d83d5c13e0961652ae*/
//Synthetic comment -- diff --git a/src/com/android/browser/AddBookmarkPage.java b/src/com/android/browser/AddBookmarkPage.java
//Synthetic comment -- index 1104d5e..594f985 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
//Synthetic comment -- @@ -116,7 +117,9 @@
*/
private class SaveBookmarkRunnable implements Runnable {
private Message mMessage;
        public SaveBookmarkRunnable(Message msg) {
mMessage = msg;
}
public void run() {
//Synthetic comment -- @@ -135,7 +138,7 @@
final ContentResolver cr = getContentResolver();
Bookmarks.addBookmark(null, cr, url, title, thumbnail, true);
if (touchIconUrl != null) {
                    new DownloadTouchIcon(cr, url).execute(mTouchIconUrl);
}
mMessage.arg1 = 1;
} catch (IllegalStateException e) {
//Synthetic comment -- @@ -237,7 +240,7 @@
Message msg = Message.obtain(mHandler, SAVE_BOOKMARK);
msg.setData(bundle);
// Start a new thread so as to not slow down the UI
            Thread t = new Thread(new SaveBookmarkRunnable(msg));
t.start();
setResult(RESULT_OK);
LogTag.logBookmarkAdded(url, "bookmarkview");








//Synthetic comment -- diff --git a/src/com/android/browser/DownloadTouchIcon.java b/src/com/android/browser/DownloadTouchIcon.java
//Synthetic comment -- index 14404ff..e8a912c 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//Synthetic comment -- @@ -46,12 +47,12 @@
private final String mOriginalUrl;
private final String mUrl;
private final String mUserAgent;
    private final BrowserActivity mActivity;
/* package */ Tab mTab;

    public DownloadTouchIcon(Tab tab, BrowserActivity activity, ContentResolver cr, WebView view) {
mTab = tab;
        mActivity = activity;
mContentResolver = cr;
// Store these in case they change.
mOriginalUrl = view.getOriginalUrl();
//Synthetic comment -- @@ -59,9 +60,9 @@
mUserAgent = view.getSettings().getUserAgentString();
}

    public DownloadTouchIcon(ContentResolver cr, String url) {
mTab = null;
        mActivity = null;
mContentResolver = cr;
mOriginalUrl = null;
mUrl = url;
//Synthetic comment -- @@ -77,7 +78,7 @@

AndroidHttpClient client = AndroidHttpClient.newInstance(
mUserAgent);
            HttpHost httpHost = Proxy.getPreferredHttpHost(mActivity, url);
if (httpHost != null) {
ConnRouteParams.setDefaultProxy(client.getParams(), httpHost);
}







