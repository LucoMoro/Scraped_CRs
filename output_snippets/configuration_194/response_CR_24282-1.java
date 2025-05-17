//<Beginning of snippet n. 0>


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.test.AndroidTestCase;
import android.util.Log;

private static final int WAIT_MSEC = 60;
private static final int DURATION = 10000;
private IntentFilter mIntentFilter = new IntentFilter();
private final Object mMySync = new Object();
private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context)
                    .setSmallIcon(android.R.drawable.stat_sys_wifi) // Replace with actual icon
                    .setContentTitle("WiFi State Changed");
            if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
                builder.setContentText("WiFi Enabled");
                Log.d("WiFiReceiver", "WiFi Enabled");
            } else if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
                builder.setContentText("WiFi Disabled");
                Log.d("WiFiReceiver", "WiFi Disabled");
            }
            notificationManager.notify(1, builder.build());
            synchronized (mMySync) {
                mMySync.notifyAll();
            }
        }
    }
};

// Register receiver
mIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
mIntentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);

public void testWifiManagerProperties() throws Exception {
    setWifiEnabled(true);
    assertTrue(mWifiManager.isWifiEnabled());
    assertNotNull(mWifiManager.getDhcpInfo());
    assertEquals(WifiManager.WIFI_STATE_ENABLED, mWifiManager.getWifiState());
    mWifiManager.getConnectionInfo();
    Thread.sleep(WAIT_MSEC);
    setWifiEnabled(false);
    assertFalse(mWifiManager.isWifiEnabled());
}

//<End of snippet n. 0>