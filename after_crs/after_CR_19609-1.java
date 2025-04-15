/*Remove sticky broadcast at supplicant disconnect

When disabling Wi-Fi while still connected to a network the last
supplicant state sent will be COMPLETED. Since it is sticky the
value will be COMPLETED until a new intent is sent from
WifiStateTracker.java. This might fool an application that Wi-Fi
is in a different state than it actually is. Solved by using
removeStickyBroadcast while handling EVENT_SUPPLICANT_DISCONNECT.

Change-Id:Ie3e57102cff396fe982bfd51093ca4acf571bc6e*/




//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiStateTracker.java b/wifi/java/android/net/wifi/WifiStateTracker.java
//Synthetic comment -- index bcbd07c..19f5a10 100644

//Synthetic comment -- @@ -941,6 +941,8 @@
mDhcpTarget = null;
}
mContext.removeStickyBroadcast(new Intent(WifiManager.NETWORK_STATE_CHANGED_ACTION));
                mContext.removeStickyBroadcast(
                    new Intent(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION));
if (ActivityManagerNative.isSystemReady()) {
intent = new Intent(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
intent.putExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false);







