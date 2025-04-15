/*add test on notifications of WifiManager

Change-Id:Ibbc0f676df06f891cd8f84811f5a096b664c8f0e*/
//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/wifi/cts/WifiManagerTest.java b/tests/tests/net/src/android/net/wifi/cts/WifiManagerTest.java
//Synthetic comment -- index e2a583b..1429fb4 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration.Status;
import android.net.wifi.WifiManager.WifiLock;
import android.test.AndroidTestCase;
import android.util.Log;

//Synthetic comment -- @@ -65,6 +66,12 @@
private static final int WAIT_MSEC = 60;
private static final int DURATION = 10000;
private IntentFilter mIntentFilter;
private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
@Override
public void onReceive(Context context, Intent intent) {
//Synthetic comment -- @@ -79,10 +86,16 @@
}
}
} else if (action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
synchronized (mMySync) {
mMySync.expectedState = STATE_WIFI_CHANGED;
mMySync.notify();
}
}
}
};
//Synthetic comment -- @@ -278,10 +291,19 @@
)
})
public void testWifiManagerProperties() throws Exception {
setWifiEnabled(true);
assertTrue(mWifiManager.isWifiEnabled());
assertNotNull(mWifiManager.getDhcpInfo());
assertEquals(WifiManager.WIFI_STATE_ENABLED, mWifiManager.getWifiState());
mWifiManager.getConnectionInfo();
setWifiEnabled(false);
assertFalse(mWifiManager.isWifiEnabled());







