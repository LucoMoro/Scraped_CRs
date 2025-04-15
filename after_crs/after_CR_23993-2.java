/*Test for Apache HttpClient

Bug 4554251

This test launches multiple downloads at once to check that the
device can open up multiple sockets without OOMing. One test
tries to use wifi if available and the other tries to use
mobile by disconnecting wifi.

The CtsTestServer was modified to handle responses in separate
threads to test concurrent downloads.

Change-Id:Ifa1a11f409c69c1ae3ce621bf0542e0be56b50e0*/




//Synthetic comment -- diff --git a/tests/src/android/webkit/cts/CtsTestServer.java b/tests/src/android/webkit/cts/CtsTestServer.java
//Synthetic comment -- index 2a72d9f..119f55a 100644

//Synthetic comment -- @@ -64,6 +64,10 @@
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -377,11 +381,11 @@
.toString();
}

    public synchronized String getLastRequestUrl() {
return mLastQuery;
}

    public synchronized int getRequestCount() {
return mRequestCount;
}

//Synthetic comment -- @@ -390,7 +394,7 @@
* value, the server will include a "Expires" header.
* @param timeMillis The time, in milliseconds, for which any future response will be valid.
*/
    public synchronized void setDocumentValidity(long timeMillis) {
mDocValidity = timeMillis;
}

//Synthetic comment -- @@ -399,7 +403,7 @@
* a "Last-Modified" header calculated from the value.
* @param timeMillis The age, in milliseconds, of any document served in the future.
*/
    public synchronized void setDocumentAge(long timeMillis) {
mDocAge = timeMillis;
}

//Synthetic comment -- @@ -411,10 +415,14 @@
private HttpResponse getResponse(HttpRequest request) throws InterruptedException, IOException {
RequestLine requestLine = request.getRequestLine();
HttpResponse response = null;
Log.i(TAG, requestLine.getMethod() + ": " + requestLine.getUri());
String uriString = requestLine.getUri();

        synchronized (this) {
            mRequestCount += 1;
            mLastQuery = uriString;
        }

URI uri = URI.create(uriString);
String path = uri.getPath();
String query = uri.getQuery();
//Synthetic comment -- @@ -585,15 +593,17 @@

private void setDateHeaders(HttpResponse response) {
long time = System.currentTimeMillis();
        synchronized (this) {
            if (mDocValidity != 0) {
                String expires = DateUtils.formatDate(new Date(time + mDocValidity),
                        DateUtils.PATTERN_RFC1123);
                response.addHeader("Expires", expires);
            }
            if (mDocAge != 0) {
                String modified = DateUtils.formatDate(new Date(time - mDocAge),
                        DateUtils.PATTERN_RFC1123);
                response.addHeader("Last-Modified", modified);
            }
}
response.addHeader("Date", DateUtils.formatDate(new Date(), DateUtils.PATTERN_RFC1123));
}
//Synthetic comment -- @@ -601,7 +611,7 @@
/**
* Create an empty response with the given status.
*/
    private static HttpResponse createResponse(int status) {
HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_0, status, null);

// Fill in error reason. Avoid use of the ReasonPhraseCatalog, which is Locale-dependent.
//Synthetic comment -- @@ -620,7 +630,7 @@
/**
* Create a string entity for the given content.
*/
    private static StringEntity createEntity(String content) {
try {
StringEntity entity = new StringEntity(content);
entity.setContentType("text/html");
//Synthetic comment -- @@ -631,7 +641,7 @@
return null;
}

    private static HttpResponse createTestDownloadResponse(Uri uri) throws IOException {
String downloadId = uri.getQueryParameter(DOWNLOAD_ID_PARAMETER);
int numBytes = uri.getQueryParameter(NUM_BYTES_PARAMETER) != null
? Integer.parseInt(uri.getQueryParameter(NUM_BYTES_PARAMETER))
//Synthetic comment -- @@ -642,7 +652,7 @@
return response;
}

    private static FileEntity createFileEntity(String downloadId, int numBytes) throws IOException {
String storageState = Environment.getExternalStorageState();
if (Environment.MEDIA_MOUNTED.equalsIgnoreCase(storageState)) {
File storageDir = Environment.getExternalStorageDirectory();
//Synthetic comment -- @@ -668,6 +678,7 @@
private boolean mIsSsl;
private boolean mIsCancelled;
private SSLContext mSslContext;
        private ExecutorService mExecutorService = Executors.newFixedThreadPool(20);

/**
* Defines the keystore contents for the server, BKS version. Holds just a
//Synthetic comment -- @@ -749,12 +760,13 @@
}

public void run() {
while (!mIsCancelled) {
try {
Socket socket = mSocket.accept();

DefaultHttpServerConnection conn = new DefaultHttpServerConnection();
                    HttpParams params = new BasicHttpParams();
                    params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_0);
conn.bind(socket, params);

// Determine whether we need to shutdown early before
//Synthetic comment -- @@ -764,19 +776,12 @@
if (isShutdownRequest(request)) {
mIsCancelled = true;
}
                    mExecutorService.submit(new HandleResponseTask(conn, request));
} catch (IOException e) {
// normal during shutdown, ignore
Log.w(TAG, e);
} catch (HttpException e) {
Log.w(TAG, e);
} catch (UnsupportedOperationException e) {
// DefaultHttpServerConnection's close() throws an
// UnsupportedOperationException.
//Synthetic comment -- @@ -784,18 +789,44 @@
}
}
try {
                mExecutorService.shutdown();
                mExecutorService.awaitTermination(1L, TimeUnit.MINUTES);
mSocket.close();
} catch (IOException ignored) {
// safe to ignore
            } catch (InterruptedException e) {
                Log.e(TAG, "Shutting down threads", e);
}
}

        private static boolean isShutdownRequest(HttpRequest request) {
RequestLine requestLine = request.getRequestLine();
String uriString = requestLine.getUri();
URI uri = URI.create(uriString);
String path = uri.getPath();
return path.equals(SHUTDOWN_PREFIX);
}

        private class HandleResponseTask implements Callable<Void> {

            private DefaultHttpServerConnection mConnection;

            private HttpRequest mRequest;

            public HandleResponseTask(DefaultHttpServerConnection connection,
                    HttpRequest request) {
                this.mConnection = connection;
                this.mRequest = request;
            }

            @Override
            public Void call() throws IOException, InterruptedException, HttpException {
                HttpResponse response = mServer.getResponse(mRequest);
                mConnection.sendResponseHeader(response);
                mConnection.sendResponseEntity(response);
                mConnection.close();
                return null;
            }
        }
}
}








//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/http/cts/ApacheHttpClientTest.java b/tests/tests/net/src/android/net/http/cts/ApacheHttpClientTest.java
new file mode 100644
//Synthetic comment -- index 0000000..e4846fd

//Synthetic comment -- @@ -0,0 +1,210 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.net.http.cts;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.test.AndroidTestCase;
import android.util.Log;
import android.webkit.cts.CtsTestServer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ApacheHttpClientTest extends AndroidTestCase {

    private static final String TAG = ApacheHttpClientTest.class.getSimpleName();

    private static final int NUM_DOWNLOADS = 20;

    private static final int SMALL_DOWNLOAD_SIZE = 100 * 1024;

    private CtsTestServer mWebServer;

    private WifiManager mWifiManager;

    private ConnectivityManager mConnectivityManager;

    private boolean mHasTelephony;

    private boolean mHasWifi;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mWebServer = new CtsTestServer(mContext);
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        mConnectivityManager = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        PackageManager packageManager = mContext.getPackageManager();
        mHasTelephony = packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
        mHasWifi = packageManager.hasSystemFeature(PackageManager.FEATURE_WIFI);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mWebServer.shutdown();
    }

    public void testExecute_withMobile() throws Exception {
        if (mHasTelephony) {
            disconnectWifiToConnectToMobile();
        }

        downloadMultipleFiles();

        if (mHasWifi) {
            connectToWifi();
        }
    }

    public void testExecute_withWifi() throws Exception {
        if (mHasWifi) {
            if (!mWifiManager.isWifiEnabled()) {
                connectToWifi();
            }
            downloadMultipleFiles();
        }
    }

    private void downloadMultipleFiles() throws ClientProtocolException, IOException {
        List<HttpResponse> responses = new ArrayList<HttpResponse>();
        for (int i = 0; i < NUM_DOWNLOADS; i++) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(getSmallDownloadUrl(i).toString());
            HttpResponse response = httpClient.execute(request);
            responses.add(response);
        }

        for (int i = 0; i < NUM_DOWNLOADS; i++) {
            assertDownloadResponse("Download " + i, SMALL_DOWNLOAD_SIZE, responses.get(i));
        }
    }

    private Uri getSmallDownloadUrl(int index) {
        return Uri.parse(mWebServer.getTestDownloadUrl("cts-small-download-" + index,
                SMALL_DOWNLOAD_SIZE));
    }

    private void assertDownloadResponse(String message, int expectedNumBytes, HttpResponse response)
            throws IllegalStateException, IOException {
        byte[] buffer = new byte[4096];
        assertEquals(200, response.getStatusLine().getStatusCode());

        InputStream stream = response.getEntity().getContent();
        int numBytes = 0;
        while (true) {
            int bytesRead = stream.read(buffer);
            if (bytesRead < 0) {
                break;
            } else {
                numBytes += bytesRead;
            }
        }
        assertEquals(message, SMALL_DOWNLOAD_SIZE, numBytes);
    }

    private void connectToWifi() throws InterruptedException {
        if (!mWifiManager.isWifiEnabled()) {
            ConnectivityActionReceiver receiver =
                    new ConnectivityActionReceiver(ConnectivityManager.TYPE_WIFI, State.CONNECTED);
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            mContext.registerReceiver(receiver, filter);

            assertTrue(mWifiManager.setWifiEnabled(true));
            assertTrue("Wifi must be configured to connect to an access point for this test.",
                    receiver.waitForStateChange());

            mContext.unregisterReceiver(receiver);
        }
    }

    private void disconnectWifiToConnectToMobile() throws InterruptedException {
        if (mHasWifi && mWifiManager.isWifiEnabled()) {
            ConnectivityActionReceiver connectMobileReceiver =
                    new ConnectivityActionReceiver(ConnectivityManager.TYPE_MOBILE,
                            State.CONNECTED);
            ConnectivityActionReceiver disconnectWifiReceiver =
                    new ConnectivityActionReceiver(ConnectivityManager.TYPE_WIFI,
                            State.DISCONNECTED);
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            mContext.registerReceiver(connectMobileReceiver, filter);
            mContext.registerReceiver(disconnectWifiReceiver, filter);

            assertTrue(mWifiManager.setWifiEnabled(false));
            assertTrue(disconnectWifiReceiver.waitForStateChange());
            assertTrue(connectMobileReceiver.waitForStateChange());

            mContext.unregisterReceiver(connectMobileReceiver);
            mContext.unregisterReceiver(disconnectWifiReceiver);
        }
    }

    /** Receiver that captures the last connectivity change's network type and state. */
    private class ConnectivityActionReceiver extends BroadcastReceiver {

        private final CountDownLatch mReceiveLatch = new CountDownLatch(1);

        private final int mNetworkType;

        private final State mExpectedState;

        ConnectivityActionReceiver(int networkType, State expectedState) {
            mNetworkType = networkType;
            mExpectedState = expectedState;
        }

        public void onReceive(Context context, Intent intent) {
            NetworkInfo networkInfo = intent.getExtras()
                    .getParcelable(ConnectivityManager.EXTRA_NETWORK_INFO);
            int networkType = networkInfo.getType();
            State networkState = networkInfo.getState();
            Log.i(TAG, "Network type: " + networkType + " State: " + networkInfo.getState());
            if (networkType == mNetworkType && networkInfo.getState() == mExpectedState) {
                mReceiveLatch.countDown();
            }
        }

        public boolean waitForStateChange() throws InterruptedException {
            return hasExpectedState() || mReceiveLatch.await(30, TimeUnit.SECONDS);
        }

        private boolean hasExpectedState() {
            return mExpectedState == mConnectivityManager.getNetworkInfo(mNetworkType).getState();
        }
    }
}







