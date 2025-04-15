/*Use proxy for downloads.

Use the new Proxy method getPreferredHttpHost to use proxy for
downloads.

Change-Id:I4224e29ba4b37bd570d84382764e08f9babe6530*/
//Synthetic comment -- diff --git a/src/com/android/browser/DownloadTouchIcon.java b/src/com/android/browser/DownloadTouchIcon.java
//Synthetic comment -- index b5369ae..14404ff 100644

//Synthetic comment -- @@ -23,15 +23,18 @@
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.provider.Browser;
import android.webkit.WebView;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
//Synthetic comment -- @@ -43,10 +46,12 @@
private final String mOriginalUrl;
private final String mUrl;
private final String mUserAgent;
/* package */ Tab mTab;

    public DownloadTouchIcon(Tab tab, ContentResolver cr, WebView view) {
mTab = tab;
mContentResolver = cr;
// Store these in case they change.
mOriginalUrl = view.getOriginalUrl();
//Synthetic comment -- @@ -56,6 +61,7 @@

public DownloadTouchIcon(ContentResolver cr, String url) {
mTab = null;
mContentResolver = cr;
mOriginalUrl = null;
mUrl = url;
//Synthetic comment -- @@ -71,6 +77,11 @@

AndroidHttpClient client = AndroidHttpClient.newInstance(
mUserAgent);
HttpGet request = new HttpGet(url);

// Follow redirects








//Synthetic comment -- diff --git a/src/com/android/browser/FetchUrlMimeType.java b/src/com/android/browser/FetchUrlMimeType.java
//Synthetic comment -- index 1e4debf..9bd0cf9 100644

//Synthetic comment -- @@ -17,12 +17,15 @@
package com.android.browser;

import android.content.ContentValues;
import android.net.Uri;
import android.net.http.AndroidHttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpHead;

import java.io.IOException;

//Synthetic comment -- @@ -67,6 +70,10 @@
// seems ok with that.
AndroidHttpClient client = AndroidHttpClient.newInstance(
mValues.getAsString(Downloads.Impl.COLUMN_USER_AGENT));
HttpHead request = new HttpHead(uri);

String cookie = mValues.getAsString(Downloads.Impl.COLUMN_COOKIE_DATA);








//Synthetic comment -- diff --git a/src/com/android/browser/Tab.java b/src/com/android/browser/Tab.java
//Synthetic comment -- index 5350a18..b31abc2 100644

//Synthetic comment -- @@ -1023,7 +1023,7 @@
}
// Have only one async task at a time.
if (mTouchIconLoader == null) {
                mTouchIconLoader = new DownloadTouchIcon(Tab.this, cr, view);
mTouchIconLoader.execute(url);
}
}







