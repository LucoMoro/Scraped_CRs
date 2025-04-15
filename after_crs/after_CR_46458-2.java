/*reset network configuration when reconnect.
If we set Ethernet higher priority than wifi, enable wifi during using
ethernet,policy will tear down wifi.Unplug ethernet,wifi will reconnect.
but can't be routed out. so add reset network configuration here.

Signed-off-by: Jianzheng Zhou <jianzheng.zhou@freescale.com>

Change-Id:I4da24a7f6d8e3038be91eb206d338826acf686fb*/




//Synthetic comment -- diff --git a/services/java/com/android/server/ConnectivityService.java b/services/java/com/android/server/ConnectivityService.java
//Synthetic comment -- index ad1dfb2..2333624 100644

//Synthetic comment -- @@ -1801,6 +1801,10 @@
if (!checkInfo.isConnectedOrConnecting() || checkTracker.isTeardownRequested()) {
checkInfo.setFailover(true);
checkTracker.reconnect();
                    if (checkTracker.isTeardownRequested()) {
                        checkTracker.setTeardownRequested(false);
                        handleConnectivityChange(checkInfo.getType(), false);
                    }
}
if (DBG) log("Attempting to switch to " + checkInfo.getTypeName());
}








//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiStateTracker.java b/wifi/java/android/net/wifi/WifiStateTracker.java
//Synthetic comment -- index 55ea34f..59b5211 100644

//Synthetic comment -- @@ -97,7 +97,6 @@
* TODO: do away with return value after making MobileDataStateTracker async
*/
public boolean teardown() {
mWifiManager.stopWifi();
return true;
}
//Synthetic comment -- @@ -106,7 +105,6 @@
* Re-enable connectivity to a network after a {@link #teardown()}.
*/
public boolean reconnect() {
mWifiManager.startWifi();
return true;
}







