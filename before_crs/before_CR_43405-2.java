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
*/
responseHeaders.stripContentEncoding();
responseBodyIn = new GZIPInputStream(transferStream);
} else {
responseBodyIn = transferStream;








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/net/http/ResponseHeaders.java b/luni/src/main/java/libcore/net/http/ResponseHeaders.java
//Synthetic comment -- index 003b445..c0b4200 100644

//Synthetic comment -- @@ -187,6 +187,11 @@
headers.removeAll("Content-Encoding");
}

public boolean isChunked() {
return "chunked".equalsIgnoreCase(transferEncoding);
}







