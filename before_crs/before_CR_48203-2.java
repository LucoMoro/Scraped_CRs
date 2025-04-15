/*Treat NO_CONTENT (204) responses as successful

Change-Id:I23da370c9b144326a36b7cd5ebde1cc0796bdb68*/
//Synthetic comment -- diff --git a/src/com/android/volley/toolbox/BasicNetwork.java b/src/com/android/volley/toolbox/BasicNetwork.java
//Synthetic comment -- index be39f67..b3c7d45 100644

//Synthetic comment -- @@ -92,10 +92,11 @@
addCacheHeaders(headers, request.getCacheEntry());
httpResponse = mHttpStack.performRequest(request, headers);
StatusLine statusLine = httpResponse.getStatusLine();

responseHeaders = convertHeaders(httpResponse.getAllHeaders());
// Handle cache validation.
                if (statusLine.getStatusCode() == HttpStatus.SC_NOT_MODIFIED) {
return new NetworkResponse(HttpStatus.SC_NOT_MODIFIED,
request.getCacheEntry().data, responseHeaders, true);
}
//Synthetic comment -- @@ -105,11 +106,10 @@
long requestLifetime = SystemClock.elapsedRealtime() - requestStart;
logSlowRequests(requestLifetime, request, responseContents, statusLine);

                if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
throw new IOException();
}
                return new NetworkResponse(HttpStatus.SC_OK,
                        responseContents, responseHeaders, false);
} catch (SocketTimeoutException e) {
attemptRetryOnException("socket", request, new TimeoutError());
} catch (ConnectTimeoutException e) {







