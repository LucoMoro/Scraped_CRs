/*Android crash after turning off WLAN immediately after turn on

Null pointer exceptions appear when turning WLAN on and off rapidly

Change-Id:I6898417e0d6262b11d9ad3f5686a8b5316f217f4Signed-off-by: Christian Bejram <christian.bejram@stericsson.com>*/
//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiStateTracker.java b/wifi/java/android/net/wifi/WifiStateTracker.java
//Synthetic comment -- index bf2d033..8af0b72 100644

//Synthetic comment -- @@ -934,7 +934,8 @@
resetConnections(true);
}
// When supplicant dies, kill the DHCP thread
                mDhcpTarget.getLooper().quit();

mContext.removeStickyBroadcast(new Intent(WifiManager.NETWORK_STATE_CHANGED_ACTION));
if (ActivityManagerNative.isSystemReady()) {
//Synthetic comment -- @@ -1406,8 +1407,10 @@
NetworkUtils.resetConnections(mInterfaceName);

// Stop DHCP
        mDhcpTarget.setCancelCallback(true);
        mDhcpTarget.removeMessages(EVENT_DHCP_START);

if (!NetworkUtils.stopDhcp(mInterfaceName)) {
Log.e(TAG, "Could not stop DHCP");







