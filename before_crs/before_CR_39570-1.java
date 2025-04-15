/*TLS + SNI + proxy => bug : the SNI at the TLS layer is the hostname of the proxy instead of the hostname in the URL

Bug:http://code.google.com/p/android/issues/detail?id=35050Change-Id:Ia12f54e0521cf98eac184d80744192c1f782e7c2*/
//Synthetic comment -- diff --git a/luni/src/main/java/libcore/net/http/HttpConnection.java b/luni/src/main/java/libcore/net/http/HttpConnection.java
//Synthetic comment -- index 66dec4d..2f92706 100644

//Synthetic comment -- @@ -201,7 +201,7 @@
OpenSSLSocketImpl openSslSocket = (OpenSSLSocketImpl) unverifiedSocket;
openSslSocket.setEnabledCompressionMethods(new String[] { "ZLIB"});
openSslSocket.setUseSessionTickets(true);
            openSslSocket.setHostname(address.socketHost);
// use SSLSocketFactory default enabled protocols
} else {
unverifiedSocket.setEnabledProtocols(new String [] { "SSLv3" });







