/*Improve the URLConnection timeout documentation slightly.

Change-Id:Ia4fbe6466f6db3f1186e6a007988d59140fc9f40*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/net/URLConnection.java b/luni/src/main/java/java/net/URLConnection.java
//Synthetic comment -- index c832bfb..4859d08 100644

//Synthetic comment -- @@ -951,8 +951,8 @@
* Sets the maximum time to wait for a connect to complete before giving up.
* Connecting to a server will fail with a {@link SocketTimeoutException} if
* the timeout elapses before a connection is established. The default value
     * of {@code 0} disables connect timeouts; connect attempts may wait
     * indefinitely.
*
* <p><strong>Warning:</strong> if the hostname resolves to multiple IP
* addresses, this client will try each in <a
//Synthetic comment -- @@ -971,8 +971,7 @@
}

/**
     * Returns the connect timeout in milliseconds, or {@code 0} if connect
     * attempts never timeout.
*/
public int getConnectTimeout() {
return connectTimeout;







