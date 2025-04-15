/*fix failing test testWifiInfoProperties for non-telephony devices

Device can still have a valid networkId even after disconnection
if it was associated with AP, getWifiState is a better check to
get valid state.

Change-Id:I31a8ca38f304fbf639e1f3111be4b5ed406e3b3c*/




//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/wifi/cts/WifiInfoTest.java b/tests/tests/net/src/android/net/wifi/cts/WifiInfoTest.java
//Synthetic comment -- index 42243c8..c7c0477 100644

//Synthetic comment -- @@ -168,8 +168,6 @@
args = {}
)
})
public void testWifiInfoProperties() throws Exception {
// this test case should in Wifi environment
WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
//Synthetic comment -- @@ -187,8 +185,7 @@
wifiInfo.getMacAddress();
setWifiEnabled(false);
Thread.sleep(DURATION);
        assertEquals(WifiManager.WIFI_STATE_DISABLED, mWifiManager.getWifiState());
}

}







