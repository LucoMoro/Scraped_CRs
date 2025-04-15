/*SipService: rediscover local IP on VPN on/off

Change-Id:Ief3d8df50eee5da1b0a0f98b826fe7b1bb753d91*/
//Synthetic comment -- diff --git a/voip/java/com/android/server/sip/SipService.java b/voip/java/com/android/server/sip/SipService.java
//Synthetic comment -- index f480fec..720227d 100644

//Synthetic comment -- @@ -32,6 +32,8 @@
import android.net.sip.SipProfile;
import android.net.sip.SipSession;
import android.net.sip.SipSessionAdapter;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Bundle;
//Synthetic comment -- @@ -94,6 +96,7 @@
new HashMap<String, ISipSession>();

private ConnectivityReceiver mConnectivityReceiver;
private boolean mWifiEnabled;
private SipWakeLock mMyWakeLock;

//Synthetic comment -- @@ -113,8 +116,11 @@
if (DEBUG) Log.d(TAG, " service started!");
mContext = context;
mConnectivityReceiver = new ConnectivityReceiver();
context.registerReceiver(mConnectivityReceiver,
new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
context.registerReceiver(mWifiStateReceiver,
new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
mMyWakeLock = new SipWakeLock((PowerManager)
//Synthetic comment -- @@ -1108,6 +1114,55 @@
}
}

/**
* Timer that can schedule events to occur even when the device is in sleep.
* Only used internally in this package.








//Synthetic comment -- diff --git a/vpn/java/android/net/vpn/VpnManager.java b/vpn/java/android/net/vpn/VpnManager.java
//Synthetic comment -- index ce40b5d..8ae9a9d 100644

//Synthetic comment -- @@ -41,7 +41,7 @@
*/
public class VpnManager {
// Action for broadcasting a connectivity state.
    private static final String ACTION_VPN_CONNECTIVITY = "vpn.connectivity";
/** Key to the profile name of a connectivity broadcast event. */
public static final String BROADCAST_PROFILE_NAME = "profile_name";
/** Key to the connectivity state of a connectivity broadcast event. */







