/*Close bluetooth headset to plug a leak

When connection process run, mBluetoothHeadset started as new
command, but it does not close when WLAN is disconnected.

Change-Id:I3f05ae3eb4b63fd9f3f0653e8bdb76315f783dc8*/




//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiStateTracker.java b/wifi/java/android/net/wifi/WifiStateTracker.java
//Synthetic comment -- index bcbd07c..12ede02 100644

//Synthetic comment -- @@ -938,6 +938,7 @@
// When supplicant dies, kill the DHCP thread
if (mDhcpTarget != null) {
mDhcpTarget.getLooper().quit();
                    mDhcpTarget.closeBluetoothHeadset();
mDhcpTarget = null;
}
mContext.removeStickyBroadcast(new Intent(WifiManager.NETWORK_STATE_CHANGED_ACTION));
//Synthetic comment -- @@ -2508,6 +2509,12 @@
int state = mBluetoothHeadset.getState(mBluetoothHeadset.getCurrentHeadset());
return state == BluetoothHeadset.STATE_DISCONNECTED;
}

        public void closeBluetoothHeadset() {
            if (mBluetoothHeadset != null) {
                mBluetoothHeadset.close();
            }
        }
}

private void checkUseStaticIp() {







