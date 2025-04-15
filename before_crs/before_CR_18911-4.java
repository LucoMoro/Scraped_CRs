/*DO NOT MERGE fix failing test testWifiInfoProperties for non-telephony devices

Device can still have a valid networkId even after disconnection
if it was associated with AP, getWifiState is a better check to
get valid state.

Change-Id:I31a8ca38f304fbf639e1f3111be4b5ed406e3b3c*/
//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/wifi/cts/WifiInfoTest.java b/tests/tests/net/src/android/net/wifi/cts/WifiInfoTest.java
//Synthetic comment -- index 42243c8..d6d7d8e 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;
import dalvik.annotation.ToBeFixed;

import android.content.BroadcastReceiver;
import android.content.Context;
//Synthetic comment -- @@ -168,8 +167,6 @@
args = {}
)
})
    @ToBeFixed(bug="1871573", explanation="android.net.wifi.WifiInfo#getNetworkId() return -1 when"
        + " there is wifi connection")
public void testWifiInfoProperties() throws Exception {
// this test case should in Wifi environment
WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
//Synthetic comment -- @@ -187,8 +184,7 @@
wifiInfo.getMacAddress();
setWifiEnabled(false);
Thread.sleep(DURATION);
        wifiInfo = mWifiManager.getConnectionInfo();
        assertEquals(-1, wifiInfo.getNetworkId());
}

}







