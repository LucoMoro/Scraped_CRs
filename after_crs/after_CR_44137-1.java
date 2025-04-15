/*Fix a bug in tunnel construction and 'Connection: close' headers.

Based onhttps://github.com/square/okhttp/pull/30(git fetch git://github.com/square/okhttp.git && git cherry-pick -x b8c51dc5bdc89d5487cbc110d6fe40389de16480)

Bug:http://code.google.com/p/android/issues/detail?id=37221Change-Id:I5bed33d9ab16002aa38c2f729fa14a2022485469*/




//Synthetic comment -- diff --git a/luni/src/main/java/libcore/net/http/HttpEngine.java b/luni/src/main/java/libcore/net/http/HttpEngine.java
//Synthetic comment -- index 8f97c81..42c9b96 100644

//Synthetic comment -- @@ -491,8 +491,15 @@
reusable = false;
}

            // If the request specified that the connection shouldn't be reused,
            // don't reuse it. This advice doesn't apply to CONNECT requests because
            // the "Connection: close" header goes the origin server, not the proxy.
            if (requestHeaders.hasConnectionClose() && method != CONNECT) {
                reusable = false;
            }

            // If the response specified that the connection shouldn't be reused, don't reuse it.
            if (responseHeaders != null && responseHeaders.hasConnectionClose()) {
reusable = false;
}

//Synthetic comment -- @@ -764,11 +771,6 @@
return agent != null ? agent : ("Java" + System.getProperty("java.version"));
}

protected final String getOriginAddress(URL url) {
int port = url.getPort();
String result = url.getHost();








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/net/URLConnectionTest.java b/luni/src/test/java/libcore/java/net/URLConnectionTest.java
//Synthetic comment -- index d7a4544..e40ba57 100644

//Synthetic comment -- @@ -795,6 +795,27 @@
assertContainsNoneMatching(get.getHeaders(), "Proxy\\-Authorization.*");
}

    // Don't disconnect after building a tunnel with CONNECT
    // http://code.google.com/p/android/issues/detail?id=37221
    public void testProxyWithConnectionClose() throws IOException {
        TestSSLContext testSSLContext = TestSSLContext.create();
        server.useHttps(testSSLContext.serverContext.getSocketFactory(), true);
        server.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.UPGRADE_TO_SSL_AT_END)
                .clearHeaders());
        server.enqueue(new MockResponse().setBody("this response comes via a proxy"));
        server.play();

        URL url = new URL("https://android.com/foo");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection(
                server.toProxyAddress());
        connection.setRequestProperty("Connection", "close");
        connection.setSSLSocketFactory(testSSLContext.clientContext.getSocketFactory());
        connection.setHostnameVerifier(new RecordingHostnameVerifier());

        assertContent("this response comes via a proxy", connection);
    }

public void testDisconnectedConnection() throws IOException {
server.enqueue(new MockResponse().setBody("ABCDEFGHIJKLMNOPQR"));
server.play();







