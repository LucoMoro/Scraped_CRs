/*Close bluetooth headset to plug a leak

When connection process run, mBluetoothHeadset started as new
command, but it does not close when WLAN is disconnected.

Change-Id:I3f05ae3eb4b63fd9f3f0653e8bdb76315f783dc8*/
//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiStateTracker.java b/wifi/java/android/net/wifi/WifiStateTracker.java
//Synthetic comment -- index bf2d033..819cb53 100644

//Synthetic comment -- @@ -935,6 +935,7 @@
}
// When supplicant dies, kill the DHCP thread
mDhcpTarget.getLooper().quit();

mContext.removeStickyBroadcast(new Intent(WifiManager.NETWORK_STATE_CHANGED_ACTION));
if (ActivityManagerNative.isSystemReady()) {
//Synthetic comment -- @@ -2511,6 +2512,12 @@
int state = mBluetoothHeadset.getState(mBluetoothHeadset.getCurrentHeadset());
return state == BluetoothHeadset.STATE_DISCONNECTED;
}
}

private void checkUseStaticIp() {







