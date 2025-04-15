/*Use picturelistener instead of delay when creating thumbnails

When a download of a bookmarked page has finished the browser
will create a thumbnail of the page. This was done after a
delay of 500 ms to let the Browser have time to create the
picture of the page needed in order to create a thumbnail.
The delay would not guarantee that the browser was ready with
a new picture, which could result in picture of a blank page
or wrong page. Now a picturelistener is instead added when the
download is finished. The picturelistener will receive an
onNewPicture when the picture is available, which will trigger
the creation of a thumbnail.

Change-Id:If615e00221dd034df7bc6598e5694e7cf131491c*/
//Synthetic comment -- diff --git a/src/com/android/browser/BrowserActivity.java b/src/com/android/browser/BrowserActivity.java
//Synthetic comment -- index 29e333a..c0d8851 100644

//Synthetic comment -- @@ -2154,10 +2154,8 @@
}

// called by a UI or non-UI thread to post the message
    public void postMessage(int what, int arg1, int arg2, Object obj,
            long delayMillis) {
        mHandler.sendMessageDelayed(mHandler.obtainMessage(what, arg1, arg2,
                obj), delayMillis);
}

// called by a UI or non-UI thread to remove the message








//Synthetic comment -- diff --git a/src/com/android/browser/Tab.java b/src/com/android/browser/Tab.java
//Synthetic comment -- index b31abc2..4a66557 100644

//Synthetic comment -- @@ -35,6 +35,7 @@
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
//Synthetic comment -- @@ -66,6 +67,7 @@
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
//Synthetic comment -- @@ -440,6 +442,17 @@
}

// -------------------------------------------------------------------------
// WebViewClient implementation for the main WebView
// -------------------------------------------------------------------------

//Synthetic comment -- @@ -515,9 +528,7 @@
|| !mInForeground) {
// Only update the bookmark screenshot if the user did not
// cancel the load early.
                mActivity.postMessage(
                        BrowserActivity.UPDATE_BOOKMARK_THUMBNAIL, 0, 0, view,
                        500);
}

// finally update the UI in the activity if it is in the foreground







