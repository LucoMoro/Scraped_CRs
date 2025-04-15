/*Wifi: replace sendMessageDelayed with alarm

Fix missing driver stop behavior by replacing delayed message with alarm

Change-Id:Iabf62b20b8e594f46052b6efea40f01c361ed02cSigned-off-by: Vishal Mahaveer <vishalm@ti.com>*/




//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiStateMachine.java b/wifi/java/android/net/wifi/WifiStateMachine.java
//Synthetic comment -- index 4539c6b..9b789a4 100644

//Synthetic comment -- @@ -182,6 +182,8 @@

private AlarmManager mAlarmManager;
private PendingIntent mScanIntent;
    private PendingIntent mDriverStopIntent;

/* Tracks current frequency mode */
private AtomicInteger mFrequencyBand = new AtomicInteger(WifiManager.WIFI_FREQUENCY_BAND_AUTO);

//Synthetic comment -- @@ -516,6 +518,11 @@
private static final String ACTION_START_SCAN =
"com.android.server.WifiManager.action.START_SCAN";

    private static final String DELAYED_STOP_COUNTER = "DelayedStopCounter";
    private static final int DRIVER_STOP_REQUEST = 0;
    private static final String ACTION_DELAYED_DRIVER_STOP =
        "com.android.server.WifiManager.action.DELAYED_DRIVER_STOP";

/**
* Keep track of whether WIFI is running.
*/
//Synthetic comment -- @@ -604,6 +611,16 @@
},
new IntentFilter(ACTION_START_SCAN));

        mContext.registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                       int counter = intent.getIntExtra(DELAYED_STOP_COUNTER,0);
                       sendMessage(obtainMessage(CMD_DELAYED_STOP_DRIVER, counter, 0));
                    }
                },
                new IntentFilter(ACTION_DELAYED_DRIVER_STOP));

mScanResultCache = new LruCache<String, ScanResult>(SCAN_RESULT_CACHE_SIZE);

PowerManager powerManager = (PowerManager)mContext.getSystemService(Context.POWER_SERVICE);
//Synthetic comment -- @@ -2600,14 +2617,21 @@
sendMessage(obtainMessage(CMD_DELAYED_STOP_DRIVER, mDelayedStopCounter, 0));
} else {
/* send regular delayed shut down */

                        Intent driverStopIntent = new Intent(ACTION_DELAYED_DRIVER_STOP, null);
                        driverStopIntent.putExtra(DELAYED_STOP_COUNTER, mDelayedStopCounter);
                        mDriverStopIntent = PendingIntent.getBroadcast(mContext, DRIVER_STOP_REQUEST,
                                            driverStopIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        mAlarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                                + DELAYED_DRIVER_STOP_MS, mDriverStopIntent);
}
break;
case CMD_START_DRIVER:
if (mInDelayedStop) {
mInDelayedStop = false;
mDelayedStopCounter++;
                        mAlarmManager.cancel(mDriverStopIntent);
if (DBG) log("Delayed stop ignored due to start");
}
break;







