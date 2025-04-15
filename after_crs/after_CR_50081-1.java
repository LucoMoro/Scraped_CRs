/*p2p: avoid p2p_find being issued on group removal.

invocation of exit function of groupcreatedstate will send a
connection change broadcast request which causes wifip2psettings
broadcast handler to send p2p_find. this causing issue if user
initiates connection immediately.
Signed-off-by: mahesh kumar <mahe209069@gmail.com>

Change-Id:I6f947b2a33066b741e58f9896d6284a14cafdacb*/




//Synthetic comment -- diff --git a/src/com/android/settings/wifi/p2p/WifiP2pSettings.java b/src/com/android/settings/wifi/p2p/WifiP2pSettings.java
//Synthetic comment -- index 2496d8e..83659ee 100644

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
    private static boolean group_formed = false;

private PreferenceGroup mPeersGroup;
private PreferenceGroup mPersistentGroup;
//Synthetic comment -- @@ -126,12 +128,16 @@
if (mWifiP2pManager != null) {
mWifiP2pManager.requestGroupInfo(mChannel, WifiP2pSettings.this);
}
                WifiP2pInfo wifip2pinfo = (WifiP2pInfo) intent.getParcelableExtra(
                        WifiP2pManager.EXTRA_WIFI_P2P_INFO);
if (networkInfo.isConnected()) {
if (DBG) Log.d(TAG, "Connected");
                } else if (group_formed != true) {
//start a search when we are disconnected
                    //but not on group removed broadcast event
startSearch();
}
                group_formed = wifip2pinfo.groupFormed;
} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
mThisDevice = (WifiP2pDevice) intent.getParcelableExtra(
WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);







