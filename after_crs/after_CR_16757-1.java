/*Make HttpURLConnection watch for Connection/close headers

Make HttpURLConnectionImpl watch for Connection/cose headers
in order to shut down the sockets at an earlier point and there
by save some battery.

Change-Id:Idb43258358369c497ac031d36ceb59880d81404f*/




//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/luni/internal/net/www/protocol/http/HttpURLConnectionImpl.java b/luni/src/main/java/org/apache/harmony/luni/internal/net/www/protocol/http/HttpURLConnectionImpl.java
//Synthetic comment -- index 687a4f3..33bedc4 100644

//Synthetic comment -- @@ -990,8 +990,24 @@

// BEGIN android-changed
private synchronized void disconnect(boolean closeSocket) {
        boolean forceClose = false;
        String connInst = null;
        if (resHeader != null) {
            connInst = resHeader.get("Connection");
            if (connInst != null && connInst.equals("close")) {
                forceClose = true;
            }
        }

        if (!forceClose && reqHeader != null) {
            connInst = reqHeader.get("Connection");
            if (connInst != null && connInst.equals("close")) {
                forceClose = true;
            }
        }

if (connection != null) {
            if (forceClose || closeSocket || ((os != null) && !os.closed)) {
/*
* In addition to closing the socket if explicitly
* requested to do so, we also close it if there was







