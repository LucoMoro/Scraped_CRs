/*Improve the URLConnection timeout documentation slightly.

Change-Id:Ia4fbe6466f6db3f1186e6a007988d59140fc9f40*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/net/URLConnection.java b/luni/src/main/java/java/net/URLConnection.java
//Synthetic comment -- index c832bfb..18a264e 100644

//Synthetic comment -- @@ -948,11 +948,12 @@
}

/**
     * Sets the maximum time in milliseconds to wait while connecting.
* Connecting to a server will fail with a {@link SocketTimeoutException} if
* the timeout elapses before a connection is established. The default value
     * of {@code 0} causes us to do a blocking connect. This does not mean we
     * will never time out, but it probably means you'll get a TCP timeout
     * after several minutes.
*
* <p><strong>Warning:</strong> if the hostname resolves to multiple IP
* addresses, this client will try each in <a
//Synthetic comment -- @@ -961,7 +962,7 @@
* elapse before the connect attempt throws an exception. Host names that
* support both IPv6 and IPv4 always have at least 2 IP addresses.
*
     * @throws IllegalArgumentException if {@code timeoutMillis &lt; 0}.
*/
public void setConnectTimeout(int timeoutMillis) {
if (timeoutMillis < 0) {
//Synthetic comment -- @@ -971,8 +972,7 @@
}

/**
     * Returns the connect timeout in milliseconds. (See {#setConnectTimeout}.)
*/
public int getConnectTimeout() {
return connectTimeout;







