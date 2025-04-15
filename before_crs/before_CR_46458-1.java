/*reset network configuration when reconnect.
If we set Ethernet higher priority than wifi, enable wifi during using
ethernet,policy will tear down wifi.Unplug ethernet,wifi will reconnect.
but can't be routed out. so add reset network configuration here.

Signed-off-by: Jianzheng Zhou <jianzheng.zhou@freescale.com>

Change-Id:I4da24a7f6d8e3038be91eb206d338826acf686fb*/
//Synthetic comment -- diff --git a/services/java/com/android/server/ConnectivityService.java b/services/java/com/android/server/ConnectivityService.java
//Synthetic comment -- index ad1dfb2..fc20fe6 100644

//Synthetic comment -- @@ -1801,6 +1801,7 @@
if (!checkInfo.isConnectedOrConnecting() || checkTracker.isTeardownRequested()) {
checkInfo.setFailover(true);
checkTracker.reconnect();
}
if (DBG) log("Attempting to switch to " + checkInfo.getTypeName());
}







