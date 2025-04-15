/*add test on notifications of WifiManager

Change-Id:Ibbc0f676df06f891cd8f84811f5a096b664c8f0e*/




//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/wifi/cts/WifiManagerTest.java b/tests/tests/net/src/android/net/wifi/cts/WifiManagerTest.java
//Synthetic comment -- index e2a583b..1429fb4 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration.Status;
import android.net.wifi.WifiManager.WifiLock;
import android.net.wifi.SupplicantState;
import android.test.AndroidTestCase;
import android.util.Log;

//Synthetic comment -- @@ -65,6 +66,12 @@
private static final int WAIT_MSEC = 60;
private static final int DURATION = 10000;
private IntentFilter mIntentFilter;

    private boolean wifiStateChanged = false;
    private boolean supplicantConnectionChange = false;
    private boolean supplicantStateChanged = false;
    private int extraWifiState = -1;

private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
@Override
public void onReceive(Context context, Intent intent) {
//Synthetic comment -- @@ -79,10 +86,16 @@
}
}
} else if (action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
                supplicantStateChanged = true;
synchronized (mMySync) {
mMySync.expectedState = STATE_WIFI_CHANGED;
mMySync.notify();
}
            } else if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                wifiStateChanged = true;
                extraWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
            } else if(action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
                supplicantConnectionChange = true;
}
}
};
//Synthetic comment -- @@ -278,10 +291,19 @@
)
})
public void testWifiManagerProperties() throws Exception {
        wifiStateChanged = false;
        supplicantStateChanged = false;
        supplicantConnectionChange = false;
        extraWifiState = -1;

setWifiEnabled(true);
assertTrue(mWifiManager.isWifiEnabled());
assertNotNull(mWifiManager.getDhcpInfo());
assertEquals(WifiManager.WIFI_STATE_ENABLED, mWifiManager.getWifiState());
        assertTrue(wifiStateChanged);
        assertTrue(supplicantStateChanged);
        assertTrue(supplicantConnectionChange);
        assertEquals(mWifiManager.getWifiState(), extraWifiState);
mWifiManager.getConnectionInfo();
setWifiEnabled(false);
assertFalse(mWifiManager.isWifiEnabled());







