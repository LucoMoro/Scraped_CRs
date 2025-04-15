/*Changing connect and response timeout.

In bad network conditions and where switches often occur between 2G and
3G the timeout of 20s is too short.

Setting this timeout to 60 seconds will improve functionality in bad conditions
while it will not affect functionality in good networks. This change also aligns
the timeouts with the timeouts used by the Browser (Connection.java).

Change-Id:I0fbe3cbfe734f8d55a41bfa5d8ab6b332a19f912*/
//Synthetic comment -- diff --git a/core/java/android/net/http/AndroidHttpClient.java b/core/java/android/net/http/AndroidHttpClient.java
//Synthetic comment -- index e07ee59..df09933 100644

//Synthetic comment -- @@ -79,6 +79,9 @@
// Gzip of data shorter than this probably won't be worthwhile
public static long DEFAULT_SYNC_MIN_GZIP_BYTES = 256;

private static final String TAG = "AndroidHttpClient";


//Synthetic comment -- @@ -107,9 +110,8 @@
// and it's not worth it to pay the penalty of checking every time.
HttpConnectionParams.setStaleCheckingEnabled(params, false);

        // Default connection and socket timeout of 20 seconds.  Tweak to taste.
        HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
        HttpConnectionParams.setSoTimeout(params, 20 * 1000);
HttpConnectionParams.setSocketBufferSize(params, 8192);

// Don't handle redirects -- return them to the caller.  Our code
//Synthetic comment -- @@ -125,7 +127,7 @@
schemeRegistry.register(new Scheme("http",
PlainSocketFactory.getSocketFactory(), 80));
schemeRegistry.register(new Scheme("https",
                SSLCertificateSocketFactory.getHttpSocketFactory(30 * 1000, sessionCache), 443));

ClientConnectionManager manager =
new ThreadSafeClientConnManager(params, schemeRegistry);







