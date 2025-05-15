//<Beginning of snippet n. 0>

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
        String action = intent.getAction();
        if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
            if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
                Log.d("WifiReceiver", "Wifi is enabled");
            } else if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
                Log.d("WifiReceiver", "Wifi is disabled");
            }
        } else if (action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
            synchronized (mMySync) {
                mMySync.expectedState = STATE_WIFI_CHANGED;
                mMySync.notify();
            }
        }
    }
};

public void testWifiManagerProperties() throws Exception {
    mIntentFilter = new IntentFilter();
    mIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
    mIntentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
    getContext().registerReceiver(mReceiver, mIntentFilter);
    
    setWifiEnabled(true);
    assertTrue(mWifiManager.isWifiEnabled());
    assertNotNull(mWifiManager.getDhcpInfo());
    assertEquals(WifiManager.WIFI_STATE_ENABLED, mWifiManager.getWifiState());
    mWifiManager.getConnectionInfo();
    setWifiEnabled(false);
    assertFalse(mWifiManager.isWifiEnabled());

    getContext().unregisterReceiver(mReceiver);
}

//<End of snippet n. 0>