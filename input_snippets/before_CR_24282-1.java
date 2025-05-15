
//<Beginning of snippet n. 0>


import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration.Status;
import android.net.wifi.WifiManager.WifiLock;
import android.test.AndroidTestCase;
import android.util.Log;

private static final int WAIT_MSEC = 60;
private static final int DURATION = 10000;
private IntentFilter mIntentFilter;
private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
@Override
public void onReceive(Context context, Intent intent) {
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

//<End of snippet n. 0>








