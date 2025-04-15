/*Treat NO_CONTENT (204) responses as successful

Change-Id:I23da370c9b144326a36b7cd5ebde1cc0796bdb68*/




//Synthetic comment -- diff --git a/src/com/android/volley/toolbox/BasicNetwork.java b/src/com/android/volley/toolbox/BasicNetwork.java
//Synthetic comment -- index be39f67..4c34165 100644

//Synthetic comment -- @@ -16,8 +16,6 @@

package com.android.volley.toolbox;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
//Synthetic comment -- @@ -39,6 +37,8 @@
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.cookie.DateUtils;

import android.os.SystemClock;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
//Synthetic comment -- @@ -92,10 +92,11 @@
addCacheHeaders(headers, request.getCacheEntry());
httpResponse = mHttpStack.performRequest(request, headers);
StatusLine statusLine = httpResponse.getStatusLine();
                int statusCode = statusLine.getStatusCode();

responseHeaders = convertHeaders(httpResponse.getAllHeaders());
// Handle cache validation.
                if (statusCode == HttpStatus.SC_NOT_MODIFIED) {
return new NetworkResponse(HttpStatus.SC_NOT_MODIFIED,
request.getCacheEntry().data, responseHeaders, true);
}
//Synthetic comment -- @@ -105,11 +106,10 @@
long requestLifetime = SystemClock.elapsedRealtime() - requestStart;
logSlowRequests(requestLifetime, request, responseContents, statusLine);

                if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_NO_CONTENT) {
throw new IOException();
}
                return new NetworkResponse(statusCode, responseContents, responseHeaders, false);
} catch (SocketTimeoutException e) {
attemptRetryOnException("socket", request, new TimeoutError());
} catch (ConnectTimeoutException e) {







