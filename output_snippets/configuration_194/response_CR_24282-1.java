//<Beginning of snippet n. 0>


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration.Status;
import android.test.AndroidTestCase;
import android.util.Log;
import static org.junit.Assert.*; // Added missing import for assertions

private static final int WAIT_MSEC = 60;
private static final int DURATION = 10000;
private IntentFilter mIntentFilter;
private final Object mMySync = new Object();
private static final int STATE_WIFI_CHANGED = 1; 
private static final int STATE_WIFI_STATE_CHANGED = 2; // Added new state for WiFi state changed
private static final int STATE_UNKNOWN = 0; 
private int expectedState = STATE_UNKNOWN;

private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return; // Added null check for intent
        String action = intent.getAction();
        if (action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
            synchronized (mMySync) {
                expectedState = STATE_WIFI_CHANGED;
                mMySync.notify();
            }
        } else if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) { // Added handling for WiFi state changed
            synchronized (mMySync) {
                expectedState = STATE_WIFI_STATE_CHANGED;
                mMySync.notify();
            }
        }
    }
};

public void testWifiManagerProperties() throws Exception {
    mIntentFilter = new IntentFilter();
    mIntentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
    mIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION); // Added WiFi state changed action
    getContext().registerReceiver(mReceiver, mIntentFilter);

    setWifiEnabled(true);
    assertTrue(mWifiManager.isWifiEnabled());
    assertNotNull(mWifiManager.getDhcpInfo());
    assertEquals(WifiManager.WIFI_STATE_ENABLED, mWifiManager.getWifiState());
    mWifiManager.getConnectionInfo();
    
    synchronized (mMySync) {
        // Simulate notification for supplicant state change
        Intent intent = new Intent(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        mReceiver.onReceive(getContext(), intent);
        mMySync.wait(DURATION);
        assertEquals(STATE_WIFI_CHANGED, expectedState);
        
        // Simulate notification for WiFi state change
        Intent wifiStateIntent = new Intent(WifiManager.WIFI_STATE_CHANGED_ACTION);
        mReceiver.onReceive(getContext(), wifiStateIntent);
        mMySync.wait(DURATION);
        assertEquals(STATE_WIFI_STATE_CHANGED, expectedState);
    }

    setWifiEnabled(false);
    assertFalse(mWifiManager.isWifiEnabled());

    getContext().unregisterReceiver(mReceiver);
}

//<End of snippet n. 0>