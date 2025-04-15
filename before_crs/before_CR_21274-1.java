/*Execute setWifiEnabled(true) when finishing testWifiManagerProperties.

If HW does not have 3G, tests after testWifiManagerProperties which depends on network capability will fail.
To avoid this, I added line to enable WiFi again.*/
//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/wifi/cts/WifiManagerTest.java b/tests/tests/net/src/android/net/wifi/cts/WifiManagerTest.java
//Synthetic comment -- index e2a583b..6e2d40c 100644

//Synthetic comment -- @@ -285,6 +285,8 @@
mWifiManager.getConnectionInfo();
setWifiEnabled(false);
assertFalse(mWifiManager.isWifiEnabled());
}

/**







