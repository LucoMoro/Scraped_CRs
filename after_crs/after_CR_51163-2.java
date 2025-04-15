/*P2PSetting:Avoid p2p_find on group removal event

Immediately after group termination p2p_find is being issued
from settings and this causing issue whenever user sends
connection request immediately after group_removal_event.

Change-Id:I41e5f520f7d9475d0ec890d40eea172d9d66d06e*/




//Synthetic comment -- diff --git a/src/com/android/settings/wifi/p2p/WifiP2pSettings.java b/src/com/android/settings/wifi/p2p/WifiP2pSettings.java
//Synthetic comment -- index 2496d8e..beae432 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
//Synthetic comment -- @@ -88,6 +89,7 @@
private boolean mWifiP2pSearching;
private int mConnectedDevices;
private WifiP2pGroup mConnectedGroup;
    private boolean mLastGroupFormed = false;

private PreferenceGroup mPeersGroup;
private PreferenceGroup mPersistentGroup;
//Synthetic comment -- @@ -123,15 +125,19 @@
if (mWifiP2pManager == null) return;
NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(
WifiP2pManager.EXTRA_NETWORK_INFO);
                WifiP2pInfo wifip2pinfo = (WifiP2pInfo) intent.getParcelableExtra(
                        WifiP2pManager.EXTRA_WIFI_P2P_INFO);
if (mWifiP2pManager != null) {
mWifiP2pManager.requestGroupInfo(mChannel, WifiP2pSettings.this);
}
if (networkInfo.isConnected()) {
if (DBG) Log.d(TAG, "Connected");
                } else if (mLastGroupFormed != true) {
//start a search when we are disconnected
                    //but not on group removed broadcast event
startSearch();
}
                mLastGroupFormed = wifip2pinfo.groupFormed;
} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
mThisDevice = (WifiP2pDevice) intent.getParcelableExtra(
WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);







