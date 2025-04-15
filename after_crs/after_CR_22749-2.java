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
//Synthetic comment -- index 49603bc..9fa03c3 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import org.apache.http.StatusLine;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpServerConnection;
//Synthetic comment -- @@ -38,10 +39,15 @@

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
//Synthetic comment -- @@ -79,7 +85,11 @@

public static final String FAVICON_PATH = "/favicon.ico";
public static final String USERAGENT_PATH = "/useragent.html";

public static final String TEST_DOWNLOAD_PATH = "/download.html";
    private static final String DOWNLOAD_ID_PARAMETER = "downloadId";
    private static final String NUM_BYTES_PARAMETER = "numBytes";

public static final String ASSET_PREFIX = "/assets/";
public static final String FAVICON_ASSET_PATH = ASSET_PREFIX + "webkit/favicon.png";
public static final String APPCACHE_PATH = "/appcache.html";
//Synthetic comment -- @@ -351,8 +361,19 @@
return sb.toString();
}

    /**
     * @param downloadId used to differentiate the files created for each test
     * @param numBytes of the content that the CTS server should send back
     * @return url to get the file from
     */
    public String getTestDownloadUrl(String downloadId, int numBytes) {
        return Uri.parse(getBaseUri())
                .buildUpon()
                .path(TEST_DOWNLOAD_PATH)
                .appendQueryParameter(DOWNLOAD_ID_PARAMETER, downloadId)
                .appendQueryParameter(NUM_BYTES_PARAMETER, "" + numBytes)
                .build()
                .toString();
}

public String getLastRequestUrl() {
//Synthetic comment -- @@ -384,8 +405,9 @@
/**
* Generate a response to the given request.
* @throws InterruptedException
     * @throws IOException
*/
    private HttpResponse getResponse(HttpRequest request) throws InterruptedException, IOException {
RequestLine requestLine = request.getRequestLine();
HttpResponse response = null;
mRequestCount += 1;
//Synthetic comment -- @@ -509,9 +531,7 @@
response.setEntity(createEntity("<html><head><title>" + agent + "</title></head>" +
"<body>" + agent + "</body></html>"));
} else if (path.equals(TEST_DOWNLOAD_PATH)) {
            response = createTestDownloadResponse(Uri.parse(uriString));
} else if (path.equals(SHUTDOWN_PREFIX)) {
response = createResponse(HttpStatus.SC_OK);
// We cannot close the socket here, because we need to respond.
//Synthetic comment -- @@ -607,6 +627,35 @@
return null;
}

    private HttpResponse createTestDownloadResponse(Uri uri) throws IOException {
        String downloadId = uri.getQueryParameter(DOWNLOAD_ID_PARAMETER);
        int numBytes = Integer.parseInt(uri.getQueryParameter(NUM_BYTES_PARAMETER));
        HttpResponse response = createResponse(HttpStatus.SC_OK);
        response.setHeader("Content-Length", "" + numBytes);
        response.setEntity(createFileEntity(downloadId, numBytes));
        return response;
    }

    private FileEntity createFileEntity(String downloadId, int numBytes) throws IOException {
        String storageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equalsIgnoreCase(storageState)) {
            File storageDir = Environment.getExternalStorageDirectory();
            File file = new File(storageDir, downloadId + ".bin");
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
            try {
                for (int i = 0; i < numBytes; i++) {
                    stream.write(1);
                }
                stream.flush();
            } finally {
                stream.close();
            }
            return new FileEntity(file, "application/octet-stream");
        } else {
            throw new IllegalStateException("External storage must be mounted for this test!");
        }
    }

private static class ServerThread extends Thread {
private CtsTestServer mServer;
private ServerSocket mSocket;








//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/DownloadManagerTest.java b/tests/tests/app/src/android/app/cts/DownloadManagerTest.java
//Synthetic comment -- index fbe57c4..af0a0fe 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.test.AndroidTestCase;
import android.view.animation.cts.DelayedCheck;
import android.webkit.cts.CtsTestServer;
//Synthetic comment -- @@ -33,6 +34,12 @@

public class DownloadManagerTest extends AndroidTestCase {

    /**
     * According to the CDD Section 7.6.1, the DownloadManager implementation must be able to
     * download individual files of 55 MB.
     */
    private static final int MINIMUM_DOWNLOAD_BYTES = 55 * 1024 * 1024;

private DownloadManager mDownloadManager;

private CtsTestServer mWebServer;
//Synthetic comment -- @@ -42,6 +49,7 @@
super.setUp();
mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
mWebServer = new CtsTestServer(mContext);
        clearDownloads();
}

@Override
//Synthetic comment -- @@ -51,10 +59,9 @@
}

public void testDownloadManager() throws Exception {
        DownloadCompleteReceiver receiver =
                new DownloadCompleteReceiver(2, TimeUnit.SECONDS.toMillis(3));
try {
IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
mContext.registerReceiver(receiver, intentFilter);

//Synthetic comment -- @@ -79,9 +86,50 @@
}
}

    public void testMinimumDownload() throws Exception {
        DownloadCompleteReceiver receiver =
                new DownloadCompleteReceiver(1, TimeUnit.MINUTES.toMillis(1));
        try {
            IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            mContext.registerReceiver(receiver, intentFilter);

            long id = mDownloadManager.enqueue(new Request(getMinimumDownloadUrl()));
            receiver.waitForDownloadComplete();

            ParcelFileDescriptor fileDescriptor = mDownloadManager.openDownloadedFile(id);
            assertEquals(MINIMUM_DOWNLOAD_BYTES, fileDescriptor.getStatSize());

            Cursor cursor = null;
            try {
                cursor = mDownloadManager.query(new Query().setFilterById(id));
                assertTrue(cursor.moveToNext());
                assertEquals(DownloadManager.STATUS_SUCCESSFUL, cursor.getInt(
                        cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)));
                assertEquals(MINIMUM_DOWNLOAD_BYTES, cursor.getInt(
                        cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)));
                assertFalse(cursor.moveToNext());
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            assertRemoveDownload(id, 0);
        } finally {
            mContext.unregisterReceiver(receiver);
        }
    }

private class DownloadCompleteReceiver extends BroadcastReceiver {

        private final CountDownLatch mReceiveLatch;

        private final long waitTimeMs;

        public DownloadCompleteReceiver(int numDownload, long waitTimeMs) {
            this.mReceiveLatch = new CountDownLatch(numDownload);
            this.waitTimeMs = waitTimeMs;
        }

@Override
public void onReceive(Context context, Intent intent) {
//Synthetic comment -- @@ -90,11 +138,11 @@

public void waitForDownloadComplete() throws InterruptedException {
assertTrue("Make sure you have WiFi or some other connectivity for this test.",
                    mReceiveLatch.await(waitTimeMs, TimeUnit.MILLISECONDS));
}
}

    private void clearDownloads() {
if (getTotalNumberDownloads() > 0) {
Cursor cursor = null;
try {
//Synthetic comment -- @@ -116,13 +164,18 @@
}

private Uri getGoodUrl() {
        return Uri.parse(mWebServer.getTestDownloadUrl("cts-good-download", 1337));
}

private Uri getBadUrl() {
return Uri.parse(mWebServer.getBaseUri() + "/nosuchurl");
}

    private Uri getMinimumDownloadUrl() {
        return Uri.parse(mWebServer.getTestDownloadUrl("cts-minimum-download",
                MINIMUM_DOWNLOAD_BYTES));
    }

private int getTotalNumberDownloads() {
Cursor cursor = null;
try {







