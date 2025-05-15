//<Beginning of snippet n. 0>

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration.Status;
import android.test.AndroidTestCase;
import android.util.Log;

private static final int WAIT_MSEC = 60;
private static final int DURATION = 10000;
private IntentFilter mIntentFilter;
private final Object mMySync = new Object(); // Added synchronization object
private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
            // Handling different WiFi states
            if (intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE) != null) {
                WifiManager.SupplicantState state = (WifiManager.SupplicantState) intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
                switch (state) {
                    case COMPLETED:
                        Log.d("WiFiReceiver", "Connected to WiFi");
                        break;
                    case DISCONNECTED:
                        Log.d("WiFiReceiver", "Disconnected from WiFi");
                        break;
                    case FAILED:
                        Log.d("WiFiReceiver", "WiFi connection failed");
                        break;
                    default:
                        break;
                }
            }
            synchronized (mMySync) {
                mMySync.expectedState = STATE_WIFI_CHANGED; // `STATE_WIFI_CHANGED` should be defined elsewhere
                mMySync.notify();
            }
        }
    }
};

// Set up the IntentFilter
mIntentFilter = new IntentFilter();
mIntentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);

public void setUp() throws Exception {
    super.setUp();
    // Register receiver before tests
    getContext().registerReceiver(mReceiver, mIntentFilter);
}

public void tearDown() throws Exception {
    // Unregister receiver after tests
    getContext().unregisterReceiver(mReceiver);
    super.tearDown();
}

public void testWifiManagerProperties() throws Exception {
    setWifiEnabled(true);
    assertTrue(mWifiManager.isWifiEnabled());
    assertNotNull(mWifiManager.getDhcpInfo());
    assertEquals(WifiManager.WIFI_STATE_ENABLED, mWifiManager.getWifiState());
    mWifiManager.getConnectionInfo();
    setWifiEnabled(false);
    assertFalse(mWifiManager.isWifiEnabled());
}

//<End of snippet n. 0>