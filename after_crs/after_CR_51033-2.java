/*Fix memory leak in WifiManager

Root Cause: ServiceHandler in WifiManager would be hold by the channel created in AsyncServiceHandler in WifiService.
Solution: Force closed when the connection is closed.

Change-Id:Id6e1774e619fb95918ed55e866653e79f47437b6Signed-off-by: Zhijun Peng <pengzhj@marvell.com>*/




//Synthetic comment -- diff --git a/services/java/com/android/server/WifiService.java b/services/java/com/android/server/WifiService.java
//Synthetic comment -- index dfcc72be..9815112 100644

//Synthetic comment -- @@ -234,6 +234,7 @@
*/
private class AsyncServiceHandler extends Handler {

        private AsyncChannel ac = null;
AsyncServiceHandler(android.os.Looper looper) {
super(looper);
}
//Synthetic comment -- @@ -256,11 +257,22 @@
} else {
if (DBG) Slog.d(TAG, "Client connection lost with reason: " + msg.arg1);
}

                    /*Close the channel to avoid memory leak in clients*/
                    if (ac != null) {
                        if (DBG) Slog.d(TAG, "Close channel: " + ac);
                        mClients.remove(ac);
                        ac.disconnected();
                        ac = null;
                    } else {
                        if (DBG) Slog.d(TAG, "Close channel: NULL");
                    }

mClients.remove((AsyncChannel) msg.obj);
break;
}
case AsyncChannel.CMD_CHANNEL_FULL_CONNECTION: {
                    ac = new AsyncChannel();
ac.connect(mContext, this, msg.replyTo);
break;
}








//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiManager.java b/wifi/java/android/net/wifi/WifiManager.java
//Synthetic comment -- index 0e29882..dfe5aa8 100644

//Synthetic comment -- @@ -2001,4 +2001,13 @@
super.finalize();
}
}

    /**
     * release the channel to avoid memory leak
     */
    public void release() {
        if (mAsyncChannel != null) {
            mAsyncChannel.disconnect();
        }
    }
}







