/*Retry failed redirections when socket is recycled.

When an HTTP request fails, getResponse() attempts to retry once
if the socket was recycled from a previous request. This extends
that logic to retry failed redirects by marking the connection as
recycled.http://code.google.com/p/android/issues/detail?id=41576Change-Id:I5aea566a498e48d753d3893101d14cd4ab76db2b*/
//Synthetic comment -- diff --git a/luni/src/main/java/libcore/net/http/HttpEngine.java b/luni/src/main/java/libcore/net/http/HttpEngine.java
//Synthetic comment -- index 42c9b96..8d81c38 100644

//Synthetic comment -- @@ -473,6 +473,12 @@
}
}

/**
* Releases this engine so that its resources may be either reused or
* closed.








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/net/http/HttpURLConnectionImpl.java b/luni/src/main/java/libcore/net/http/HttpURLConnectionImpl.java
//Synthetic comment -- index 9507745..3e6503f 100644

//Synthetic comment -- @@ -329,6 +329,8 @@

if (retry == Retry.DIFFERENT_CONNECTION) {
httpEngine.automaticallyReleaseConnectionToPool();
}

httpEngine.release(true);







