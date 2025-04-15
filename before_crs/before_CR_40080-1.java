/*HttpResponseCache should return 504, not 502.

The RFC seems pretty clear on this issue.

Bug:http://code.google.com/p/android/issues/detail?id=28294Change-Id:Ia9a979e1fbbd8b38b1607059c042e0fc9ca44270*/
//Synthetic comment -- diff --git a/luni/src/main/java/libcore/net/http/HttpEngine.java b/luni/src/main/java/libcore/net/http/HttpEngine.java
//Synthetic comment -- index 3e4b9d3..a370956 100644

//Synthetic comment -- @@ -69,10 +69,10 @@
* required, use {@link #automaticallyReleaseConnectionToPool()}.
*/
public class HttpEngine {
    private static final CacheResponse BAD_GATEWAY_RESPONSE = new CacheResponse() {
@Override public Map<String, List<String>> getHeaders() throws IOException {
Map<String, List<String>> result = new HashMap<String, List<String>>();
            result.put(null, Collections.singletonList("HTTP/1.1 502 Bad Gateway"));
return result;
}
@Override public InputStream getBody() throws IOException {
//Synthetic comment -- @@ -223,14 +223,15 @@
/*
* The raw response source may require the network, but the request
* headers may forbid network use. In that case, dispose of the network
         * response and use a BAD_GATEWAY response instead.
*/
if (requestHeaders.isOnlyIfCached() && responseSource.requiresConnection()) {
if (responseSource == ResponseSource.CONDITIONAL_CACHE) {
IoUtils.closeQuietly(cachedResponseBody);
}
this.responseSource = ResponseSource.CACHE;
            this.cacheResponse = BAD_GATEWAY_RESPONSE;
RawHeaders rawResponseHeaders = RawHeaders.fromMultimap(cacheResponse.getHeaders());
setResponse(new ResponseHeaders(uri, rawResponseHeaders), cacheResponse.getBody());
}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/net/http/HttpResponseCacheTest.java b/luni/src/test/java/libcore/net/http/HttpResponseCacheTest.java
//Synthetic comment -- index 360ca44..133924e 100644

//Synthetic comment -- @@ -959,7 +959,7 @@

HttpURLConnection connection = (HttpURLConnection) server.getUrl("/").openConnection();
connection.addRequestProperty("Cache-Control", "only-if-cached");
        assertBadGateway(connection);
}

public void testRequestOnlyIfCachedWithFullResponseCached() throws IOException {
//Synthetic comment -- @@ -983,7 +983,7 @@
assertEquals("A", readAscii(server.getUrl("/").openConnection()));
HttpURLConnection connection = (HttpURLConnection) server.getUrl("/").openConnection();
connection.addRequestProperty("Cache-Control", "only-if-cached");
        assertBadGateway(connection);
}

public void testRequestOnlyIfCachedWithUnhelpfulResponseCached() throws IOException {
//Synthetic comment -- @@ -993,7 +993,7 @@
assertEquals("A", readAscii(server.getUrl("/").openConnection()));
HttpURLConnection connection = (HttpURLConnection) server.getUrl("/").openConnection();
connection.addRequestProperty("Cache-Control", "only-if-cached");
        assertBadGateway(connection);
}

public void testRequestCacheControlNoCache() throws Exception {
//Synthetic comment -- @@ -1804,13 +1804,13 @@
}
}

    private void assertBadGateway(HttpURLConnection connection) throws IOException {
try {
connection.getInputStream();
fail();
} catch (FileNotFoundException expected) {
}
        assertEquals(HttpURLConnection.HTTP_BAD_GATEWAY, connection.getResponseCode());
assertEquals(-1, connection.getErrorStream().read());
}








