/*Bugfixes for handling of HTTPS connections over HTTP proxy

Change-Id:Ia9e9c47debb47b0992c72bc47cb4165c56bb12c1*/




//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/luni/internal/net/www/protocol/http/HttpConfiguration.java b/luni/src/main/java/org/apache/harmony/luni/internal/net/www/protocol/http/HttpConfiguration.java
//Synthetic comment -- index 55d762d..6b4d197 100644

//Synthetic comment -- @@ -117,6 +117,15 @@
return hostPort;
}

    // BEGIN android-added
    /**
     * Returns the URI for this configuration
     */
    public URI getURI() {
        return uri;
    }
    // END android-added

@Override
public boolean equals(Object arg0) {
if(!(arg0 instanceof HttpConfiguration)) {
//Synthetic comment -- @@ -135,4 +144,4 @@
return uri.hashCode();
}

\ No newline at end of file
}








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/luni/internal/net/www/protocol/http/HttpConnection.java b/luni/src/main/java/org/apache/harmony/luni/internal/net/www/protocol/http/HttpConnection.java
//Synthetic comment -- index 4c51bdc..3d45190 100644

//Synthetic comment -- @@ -159,7 +159,9 @@

public SSLSocket getSecureSocket(SSLSocketFactory sslSocketFactory, HostnameVerifier hostnameVerifier) throws IOException {
if(!usingSecureSocket) {
            // BEGIN android-changed
            String hostName = config.getURI().getHost();
            // END android-changed
int port = config.getHostPort();
// create the wrapper over connected socket
sslSocket = (SSLSocket) sslSocketFactory.createSocket(socket,








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/luni/internal/net/www/protocol/https/HttpsURLConnectionImpl.java b/luni/src/main/java/org/apache/harmony/luni/internal/net/www/protocol/https/HttpsURLConnectionImpl.java
//Synthetic comment -- index 2ad5b13..6be8bb9 100644

//Synthetic comment -- @@ -365,11 +365,15 @@
if (connected) {
return;
}

            // BEGIN android-changed
            // Bugfix for connection through implicit (system-wide) proxy
            // Call connect() before asking super.usingProxy()
            super.connect();
if (super.usingProxy() && !makingSSLTunnel) {
// SSL Tunnel through the proxy was not established yet, do so
makingSSLTunnel = true;
                
// keep request method
String save_meth = method;
// make SSL Tunnel
//Synthetic comment -- @@ -391,10 +395,8 @@
is.read();
}
makingSSLTunnel = false;
}
            // END android-changed
if (!makingSSLTunnel) {
sslSocket = connection.getSecureSocket(getSSLSocketFactory(), getHostnameVerifier());
setUpTransportIO(connection);







