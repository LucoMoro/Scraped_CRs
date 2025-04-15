/*wifi: don't avoid poor connections if mobile isn't connected and avoid
NPE on wifi only devices.

Change-Id:I7dcbbe682ee69bc23b8d9fe8cbed49cab7cea04b*/




//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiWatchdogStateMachine.java b/wifi/java/android/net/wifi/WifiWatchdogStateMachine.java
//Synthetic comment -- index c6d3eae..60ed113 100644

//Synthetic comment -- @@ -262,6 +262,8 @@
ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
Context.CONNECTIVITY_SERVICE);
sWifiOnly = (cm.isNetworkSupported(ConnectivityManager.TYPE_MOBILE) == false);
        boolean noMobileConnection = sWifiOnly ? true : (cm.getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE).isConnected() == false);

// Watchdog is always enabled. Poor network detection & walled garden detection
// can individually be turned on/off
//Synthetic comment -- @@ -270,7 +272,7 @@
putSettingsBoolean(contentResolver, Settings.Secure.WIFI_WATCHDOG_ON, true);

// Disable poor network avoidance, but keep watchdog active for walled garden detection
        if (sWifiOnly || noMobileConnection) {
log("Disabling poor network avoidance for wi-fi only device");
putSettingsBoolean(contentResolver,
Settings.Secure.WIFI_WATCHDOG_POOR_NETWORK_TEST_ENABLED, false);







