/*SipService: rediscover local IP on VPN on/off

Change-Id:Ief3d8df50eee5da1b0a0f98b826fe7b1bb753d91*/




//Synthetic comment -- diff --git a/voip/java/com/android/server/sip/SipService.java b/voip/java/com/android/server/sip/SipService.java
//Synthetic comment -- index f480fec..720227d 100644

//Synthetic comment -- @@ -32,6 +32,8 @@
import android.net.sip.SipProfile;
import android.net.sip.SipSession;
import android.net.sip.SipSessionAdapter;
import android.net.vpn.VpnManager;
import android.net.vpn.VpnState;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Bundle;
//Synthetic comment -- @@ -94,6 +96,7 @@
new HashMap<String, ISipSession>();

private ConnectivityReceiver mConnectivityReceiver;
    private VpnConnectivityReceiver mVpnConnectivityReceiver;
private boolean mWifiEnabled;
private SipWakeLock mMyWakeLock;

//Synthetic comment -- @@ -113,8 +116,11 @@
if (DEBUG) Log.d(TAG, " service started!");
mContext = context;
mConnectivityReceiver = new ConnectivityReceiver();
        mVpnConnectivityReceiver = new VpnConnectivityReceiver();
context.registerReceiver(mConnectivityReceiver,
new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        context.registerReceiver(mVpnConnectivityReceiver,
                new IntentFilter(VpnManager.ACTION_VPN_CONNECTIVITY));
context.registerReceiver(mWifiStateReceiver,
new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
mMyWakeLock = new SipWakeLock((PowerManager)
//Synthetic comment -- @@ -1108,6 +1114,55 @@
}
}

    private class VpnConnectivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            // Run the handler in MyExecutor to be protected by wake lock
            getExecutor().execute(new Runnable() {
                public void run() {
                    onReceiveInternal(context, intent);
                }
            });
        }

        private void onReceiveInternal(Context context, Intent intent) {
            String action = intent.getAction();
            VpnState state = (VpnState)intent.getExtra(VpnManager.BROADCAST_CONNECTION_STATE);
            if (action.equals(VpnManager.ACTION_VPN_CONNECTIVITY)) {
                if (DEBUG) Log.d(TAG, "VpnListener got a CONNECTIVITY_ACTION");
                switch (state) {
                    case IDLE:
                    case CONNECTED:
                        if (DEBUG) Log.d(TAG, "VpnListener:: CONNECTED");
                        onChanged("vpn", true);
                        break;
                    case CONNECTING:
                    case DISCONNECTING:
                    case CANCELLED:
                    case UNUSABLE:
                    case UNKNOWN:
                    default:
                        break;
                }
            } else {
                if (DEBUG) Log.d(TAG, "VpnListener got a non CONNECTIVITY_ACTION intent " + action.toString());
            }
        }

        private NetworkInfo getActiveNetworkInfo() {
            ConnectivityManager cm = (ConnectivityManager)
                    mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo();
        }

        private void onChanged(String type, boolean connected) {
            synchronized (SipService.this) {
                    mMyWakeLock.acquire(this);
                    onConnectivityChanged(type, connected);
                    mMyWakeLock.release(this);
            }
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
    public static final String ACTION_VPN_CONNECTIVITY = "vpn.connectivity";
/** Key to the profile name of a connectivity broadcast event. */
public static final String BROADCAST_PROFILE_NAME = "profile_name";
/** Key to the connectivity state of a connectivity broadcast event. */







