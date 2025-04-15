/*Fix WifiInfoTest

Do not listen to supplicant state change for wifi disable action,
instead depend on wifi state changed action

Bug: 4242273
Change-Id:Ie53ff42d5e51bbc9f28d93a435fa3315611d342e*/
//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/wifi/cts/WifiInfoTest.java b/tests/tests/net/src/android/net/wifi/cts/WifiInfoTest.java
//Synthetic comment -- index 3b1a6c1..44189cd 100644

//Synthetic comment -- @@ -54,7 +54,7 @@
@Override
public void onReceive(Context context, Intent intent) {
final String action = intent.getAction();
            if (action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
synchronized (mMySync) {
mMySync.expectedState = STATE_WIFI_CHANGED;
mMySync.notify();
//Synthetic comment -- @@ -68,14 +68,7 @@
super.setUp();
mMySync = new MySync();
mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        mIntentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        mIntentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
mIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        mIntentFilter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiManager.ACTION_PICK_WIFI_NETWORK);

mContext.registerReceiver(mReceiver, mIntentFilter);
mWifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);







