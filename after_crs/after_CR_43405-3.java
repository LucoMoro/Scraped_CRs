/*Strip content length in requests with "transparent" gzip handling.

We need to strip both the Content-Length and the Content-Encoding
for such requests. In such requests, it will be the length of the
compressed response. We hide the fact that compression is taking place
from clients, so we shouldn't give them the content length either.

Change-Id:I80713ab33143945c5e2656f478d83cc9e60226a8*/




//Synthetic comment -- diff --git a/luni/src/main/java/libcore/net/http/HttpEngine.java b/luni/src/main/java/libcore/net/http/HttpEngine.java
//Synthetic comment -- index a370956..8f97c81 100644

//Synthetic comment -- @@ -524,8 +524,13 @@
/*
* If the response was transparently gzipped, remove the gzip header field
* so clients don't double decompress. http://b/3009828
             *
             * Also remove the Content-Length in this case because it contains the length
             * of the gzipped response. This isn't terribly useful and is dangerous because
             * clients can query the content length, but not the content encoding.
*/
responseHeaders.stripContentEncoding();
            responseHeaders.stripContentLength();
responseBodyIn = new GZIPInputStream(transferStream);
} else {
responseBodyIn = transferStream;








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/net/http/ResponseHeaders.java b/luni/src/main/java/libcore/net/http/ResponseHeaders.java
//Synthetic comment -- index 003b445..c0b4200 100644

//Synthetic comment -- @@ -187,6 +187,11 @@
headers.removeAll("Content-Encoding");
}

    public void stripContentLength() {
        contentLength = -1;
        headers.removeAll("Content-Length");
    }

public boolean isChunked() {
return "chunked".equalsIgnoreCase(transferEncoding);
}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/net/URLConnectionTest.java b/luni/src/test/java/libcore/java/net/URLConnectionTest.java
//Synthetic comment -- index 762bac4..d7a4544 100644

//Synthetic comment -- @@ -989,21 +989,25 @@
URLConnection connection = server.getUrl("/").openConnection();
assertEquals("ABCABCABC", readAscii(connection.getInputStream(), Integer.MAX_VALUE));
assertNull(connection.getContentEncoding());
        assertEquals(-1, connection.getContentLength());

RecordedRequest request = server.takeRequest();
assertContains(request.getHeaders(), "Accept-Encoding: gzip");
}

public void testClientConfiguredGzipContentEncoding() throws Exception {
        byte[] bodyBytes = gzip("ABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes("UTF-8"));
server.enqueue(new MockResponse()
                .setBody(bodyBytes)
                .addHeader("Content-Encoding: gzip")
                .addHeader("Content-Length: " + bodyBytes.length));
server.play();

URLConnection connection = server.getUrl("/").openConnection();
connection.addRequestProperty("Accept-Encoding", "gzip");
InputStream gunzippedIn = new GZIPInputStream(connection.getInputStream());
assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", readAscii(gunzippedIn, Integer.MAX_VALUE));
        assertEquals(bodyBytes.length, connection.getContentLength());

RecordedRequest request = server.takeRequest();
assertContains(request.getHeaders(), "Accept-Encoding: gzip");







