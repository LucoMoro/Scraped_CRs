//<Beginning of snippet n. 0>
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration.Status;
import android.net.wifi.WifiManager.WifiLock;
import android.test.AndroidTestCase;

private static final int WAIT_MSEC = 60;
private static final int DURATION = 10000;
private IntentFilter mIntentFilter;
private final Object mMySync = new Object();
private WifiManager mWifiManager;

private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
            synchronized (mMySync) {
                mMySync.expectedState = STATE_WIFI_CHANGED;
                mMySync.notify();
                assertNotNull(mWifiManager.getConnectionInfo());
            }
        }
    }
};

@Override
protected void setUp() throws Exception {
    super.setUp();
    mIntentFilter = new IntentFilter();
    mIntentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
    getContext().registerReceiver(mReceiver, mIntentFilter);
    mWifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
}

@Override
protected void tearDown() throws Exception {
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