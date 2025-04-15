/*Test for DownloadManager Compliance

Adds a test to enforce CDD section 7.6.1 that says the
DownloadManager must be able to download individual
files of 55MB.

Modify the CtsTestServer to generate files of any size
in response to the download queries. The test downloads
a file of 55MB and checks that the file size is correct
both in the database and by the parcel file descriptor
returned from DownloadManager.

Change-Id:I82f17bfe5937fdd63a80fc71540537463509c241*/
//Synthetic comment -- diff --git a/tests/src/android/webkit/cts/CtsTestServer.java b/tests/src/android/webkit/cts/CtsTestServer.java
//Synthetic comment -- index 49603bc..af6d115 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import org.apache.http.StatusLine;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpServerConnection;
//Synthetic comment -- @@ -38,10 +39,15 @@

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
//Synthetic comment -- @@ -79,7 +85,11 @@

public static final String FAVICON_PATH = "/favicon.ico";
public static final String USERAGENT_PATH = "/useragent.html";
public static final String TEST_DOWNLOAD_PATH = "/download.html";
public static final String ASSET_PREFIX = "/assets/";
public static final String FAVICON_ASSET_PATH = ASSET_PREFIX + "webkit/favicon.png";
public static final String APPCACHE_PATH = "/appcache.html";
//Synthetic comment -- @@ -351,8 +361,19 @@
return sb.toString();
}

    public String getTestDownloadUrl() {
        return getBaseUri() + TEST_DOWNLOAD_PATH;
}

public String getLastRequestUrl() {
//Synthetic comment -- @@ -384,8 +405,9 @@
/**
* Generate a response to the given request.
* @throws InterruptedException
*/
    private HttpResponse getResponse(HttpRequest request) throws InterruptedException {
RequestLine requestLine = request.getRequestLine();
HttpResponse response = null;
mRequestCount += 1;
//Synthetic comment -- @@ -509,9 +531,7 @@
response.setEntity(createEntity("<html><head><title>" + agent + "</title></head>" +
"<body>" + agent + "</body></html>"));
} else if (path.equals(TEST_DOWNLOAD_PATH)) {
            response = createResponse(HttpStatus.SC_OK);
            response.setHeader("Content-Length", "0");
            response.setEntity(createEntity(""));
} else if (path.equals(SHUTDOWN_PREFIX)) {
response = createResponse(HttpStatus.SC_OK);
// We cannot close the socket here, because we need to respond.
//Synthetic comment -- @@ -607,6 +627,37 @@
return null;
}

private static class ServerThread extends Thread {
private CtsTestServer mServer;
private ServerSocket mSocket;








//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/DownloadManagerTest.java b/tests/tests/app/src/android/app/cts/DownloadManagerTest.java
//Synthetic comment -- index fbe57c4..3920d58 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.view.animation.cts.DelayedCheck;
import android.webkit.cts.CtsTestServer;
//Synthetic comment -- @@ -33,6 +34,12 @@

public class DownloadManagerTest extends AndroidTestCase {

private DownloadManager mDownloadManager;

private CtsTestServer mWebServer;
//Synthetic comment -- @@ -42,6 +49,7 @@
super.setUp();
mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
mWebServer = new CtsTestServer(mContext);
}

@Override
//Synthetic comment -- @@ -51,10 +59,9 @@
}

public void testDownloadManager() throws Exception {
        DownloadCompleteReceiver receiver = new DownloadCompleteReceiver();
try {
            removeAllDownloads();

IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
mContext.registerReceiver(receiver, intentFilter);

//Synthetic comment -- @@ -79,9 +86,50 @@
}
}

private class DownloadCompleteReceiver extends BroadcastReceiver {

        private final CountDownLatch mReceiveLatch = new CountDownLatch(2);

@Override
public void onReceive(Context context, Intent intent) {
//Synthetic comment -- @@ -90,11 +138,11 @@

public void waitForDownloadComplete() throws InterruptedException {
assertTrue("Make sure you have WiFi or some other connectivity for this test.",
                    mReceiveLatch.await(3, TimeUnit.SECONDS));
}
}

    private void removeAllDownloads() {
if (getTotalNumberDownloads() > 0) {
Cursor cursor = null;
try {
//Synthetic comment -- @@ -116,13 +164,18 @@
}

private Uri getGoodUrl() {
        return Uri.parse(mWebServer.getTestDownloadUrl());
}

private Uri getBadUrl() {
return Uri.parse(mWebServer.getBaseUri() + "/nosuchurl");
}

private int getTotalNumberDownloads() {
Cursor cursor = null;
try {







