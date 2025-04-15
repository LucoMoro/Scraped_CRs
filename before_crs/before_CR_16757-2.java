/*Make HttpURLConnection watch for Connection/close headers

Make HttpURLConnectionImpl watch for Connection/cose headers
in order to shut down the sockets at an earlier point and there
by save some battery.

Change-Id:Idb43258358369c497ac031d36ceb59880d81404f*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/luni/internal/net/www/protocol/http/HttpURLConnectionImpl.java b/luni/src/main/java/org/apache/harmony/luni/internal/net/www/protocol/http/HttpURLConnectionImpl.java
//Synthetic comment -- index 687a4f3..a6508c8 100644

//Synthetic comment -- @@ -990,8 +990,24 @@

// BEGIN android-changed
private synchronized void disconnect(boolean closeSocket) {
if (connection != null) {
            if (closeSocket || ((os != null) && !os.closed)) {
/*
* In addition to closing the socket if explicitly
* requested to do so, we also close it if there was







