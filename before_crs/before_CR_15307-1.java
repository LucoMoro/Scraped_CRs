/*Use proxy for downloads.

Use the new Proxy method getPreferredHttpHost to use proxy for
downloads.

Change-Id:I4224e29ba4b37bd570d84382764e08f9babe6530*/
//Synthetic comment -- diff --git a/src/com/android/browser/BrowserActivity.java b/src/com/android/browser/BrowserActivity.java
//Synthetic comment -- index 60dffac..cc5dee2 100644

//Synthetic comment -- @@ -3634,6 +3634,12 @@
path = path.substring(0, idx);
}
}
uri = new URI(w.mScheme, w.mAuthInfo, w.mHost, w.mPort, path,
query, frag);
} catch (Exception e) {








//Synthetic comment -- diff --git a/src/com/android/browser/DownloadTouchIcon.java b/src/com/android/browser/DownloadTouchIcon.java
//Synthetic comment -- index 07d2d3a..d70df27 100644

//Synthetic comment -- @@ -23,14 +23,17 @@
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
//Synthetic comment -- @@ -70,6 +73,10 @@

AndroidHttpClient client = AndroidHttpClient.newInstance(
mUserAgent);
HttpGet request = new HttpGet(url);

// Follow redirects








//Synthetic comment -- diff --git a/src/com/android/browser/FetchUrlMimeType.java b/src/com/android/browser/FetchUrlMimeType.java
//Synthetic comment -- index 0081d32..0ae83d6 100644

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
mValues.getAsString(Downloads.COLUMN_USER_AGENT));
HttpHead request = new HttpHead(uri);

String cookie = mValues.getAsString(Downloads.COLUMN_COOKIE_DATA);







