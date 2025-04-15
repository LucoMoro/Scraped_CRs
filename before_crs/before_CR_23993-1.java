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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -377,11 +381,11 @@
.toString();
}

    public String getLastRequestUrl() {
return mLastQuery;
}

    public int getRequestCount() {
return mRequestCount;
}

//Synthetic comment -- @@ -390,7 +394,7 @@
* value, the server will include a "Expires" header.
* @param timeMillis The time, in milliseconds, for which any future response will be valid.
*/
    public void setDocumentValidity(long timeMillis) {
mDocValidity = timeMillis;
}

//Synthetic comment -- @@ -399,7 +403,7 @@
* a "Last-Modified" header calculated from the value.
* @param timeMillis The age, in milliseconds, of any document served in the future.
*/
    public void setDocumentAge(long timeMillis) {
mDocAge = timeMillis;
}

//Synthetic comment -- @@ -411,10 +415,14 @@
private HttpResponse getResponse(HttpRequest request) throws InterruptedException, IOException {
RequestLine requestLine = request.getRequestLine();
HttpResponse response = null;
        mRequestCount += 1;
Log.i(TAG, requestLine.getMethod() + ": " + requestLine.getUri());
String uriString = requestLine.getUri();
        mLastQuery = uriString;
URI uri = URI.create(uriString);
String path = uri.getPath();
String query = uri.getQuery();
//Synthetic comment -- @@ -585,15 +593,17 @@

private void setDateHeaders(HttpResponse response) {
long time = System.currentTimeMillis();
        if (mDocValidity != 0) {
            String expires =
                    DateUtils.formatDate(new Date(time + mDocValidity), DateUtils.PATTERN_RFC1123);
            response.addHeader("Expires", expires);
        }
        if (mDocAge != 0) {
            String modified =
                    DateUtils.formatDate(new Date(time - mDocAge), DateUtils.PATTERN_RFC1123);
            response.addHeader("Last-Modified", modified);
}
response.addHeader("Date", DateUtils.formatDate(new Date(), DateUtils.PATTERN_RFC1123));
}
//Synthetic comment -- @@ -601,7 +611,7 @@
/**
* Create an empty response with the given status.
*/
    private HttpResponse createResponse(int status) {
HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_0, status, null);

// Fill in error reason. Avoid use of the ReasonPhraseCatalog, which is Locale-dependent.
//Synthetic comment -- @@ -620,7 +630,7 @@
/**
* Create a string entity for the given content.
*/
    private StringEntity createEntity(String content) {
try {
StringEntity entity = new StringEntity(content);
entity.setContentType("text/html");
//Synthetic comment -- @@ -631,7 +641,7 @@
return null;
}

    private HttpResponse createTestDownloadResponse(Uri uri) throws IOException {
String downloadId = uri.getQueryParameter(DOWNLOAD_ID_PARAMETER);
int numBytes = uri.getQueryParameter(NUM_BYTES_PARAMETER) != null
? Integer.parseInt(uri.getQueryParameter(NUM_BYTES_PARAMETER))
//Synthetic comment -- @@ -642,7 +652,7 @@
return response;
}

    private FileEntity createFileEntity(String downloadId, int numBytes) throws IOException {
String storageState = Environment.getExternalStorageState();
if (Environment.MEDIA_MOUNTED.equalsIgnoreCase(storageState)) {
File storageDir = Environment.getExternalStorageDirectory();
//Synthetic comment -- @@ -668,6 +678,7 @@
private boolean mIsSsl;
private boolean mIsCancelled;
private SSLContext mSslContext;

/**
* Defines the keystore contents for the server, BKS version. Holds just a
//Synthetic comment -- @@ -749,12 +760,13 @@
}

public void run() {
            HttpParams params = new BasicHttpParams();
            params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_0);
while (!mIsCancelled) {
try {
Socket socket = mSocket.accept();
DefaultHttpServerConnection conn = new DefaultHttpServerConnection();
conn.bind(socket, params);

// Determine whether we need to shutdown early before
//Synthetic comment -- @@ -764,19 +776,12 @@
if (isShutdownRequest(request)) {
mIsCancelled = true;
}

                    HttpResponse response = mServer.getResponse(request);
                    conn.sendResponseHeader(response);
                    conn.sendResponseEntity(response);
                    conn.close();

} catch (IOException e) {
// normal during shutdown, ignore
Log.w(TAG, e);
} catch (HttpException e) {
Log.w(TAG, e);
                } catch (InterruptedException e) {
                    Log.w(TAG, e);
} catch (UnsupportedOperationException e) {
// DefaultHttpServerConnection's close() throws an
// UnsupportedOperationException.
//Synthetic comment -- @@ -784,18 +789,44 @@
}
}
try {
mSocket.close();
} catch (IOException ignored) {
// safe to ignore
}
}

        private boolean isShutdownRequest(HttpRequest request) {
RequestLine requestLine = request.getRequestLine();
String uriString = requestLine.getUri();
URI uri = URI.create(uriString);
String path = uri.getPath();
return path.equals(SHUTDOWN_PREFIX);
}
}
}








//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/http/cts/ApacheHttpClientTest.java b/tests/tests/net/src/android/net/http/cts/ApacheHttpClientTest.java
new file mode 100644
//Synthetic comment -- index 0000000..7d82a83

//Synthetic comment -- @@ -0,0 +1,197 @@







