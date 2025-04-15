/*Stop WifiManagerTest from disabling current network.

Bug 3181376

Change-Id:I7dad90ba830678357b900709359f10319070c96e*/
//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/wifi/cts/WifiManagerTest.java b/tests/tests/net/src/android/net/wifi/cts/WifiManagerTest.java
//Synthetic comment -- index 132ca92..e2a583b 100644

//Synthetic comment -- @@ -16,7 +16,10 @@

package android.net.wifi.cts;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
//Synthetic comment -- @@ -28,10 +31,11 @@
import android.net.wifi.WifiConfiguration.Status;
import android.net.wifi.WifiManager.WifiLock;
import android.test.AndroidTestCase;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

@TestTargetClass(WifiManager.class)
public class WifiManagerTest extends AndroidTestCase {
//Synthetic comment -- @@ -341,58 +345,87 @@
)
})
public void testWifiManagerNetWork() throws Exception {
        WifiConfiguration wifiConfiguration;
        // add a WifiConfig
        final int notExist = -1;
        List<WifiConfiguration> wifiConfiguredNetworks = mWifiManager.getConfiguredNetworks();
        int pos = findConfiguredNetworks(SSID1, wifiConfiguredNetworks);
        if (notExist != pos) {
wifiConfiguration = wifiConfiguredNetworks.get(pos);
            mWifiManager.removeNetwork(wifiConfiguration.networkId);
}
        pos = findConfiguredNetworks(SSID1, wifiConfiguredNetworks);
        assertEquals(notExist, pos);
        final int size = wifiConfiguredNetworks.size();

        wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = SSID1;
        int netId = mWifiManager.addNetwork(wifiConfiguration);
        assertTrue(existSSID(SSID1));

        wifiConfiguredNetworks = mWifiManager.getConfiguredNetworks();
        assertEquals(size + 1, wifiConfiguredNetworks.size());
        pos = findConfiguredNetworks(SSID1, wifiConfiguredNetworks);
        assertTrue(notExist != pos);

        // Enable & disable network
        boolean disableOthers = false;
        assertTrue(mWifiManager.enableNetwork(netId, disableOthers));
        wifiConfiguration = mWifiManager.getConfiguredNetworks().get(pos);
        assertDisableOthers(wifiConfiguration, disableOthers);
        assertEquals(Status.ENABLED, wifiConfiguration.status);
        disableOthers = true;
        assertTrue(mWifiManager.enableNetwork(netId, disableOthers));
        wifiConfiguration = mWifiManager.getConfiguredNetworks().get(pos);
        assertDisableOthers(wifiConfiguration, disableOthers);

        assertTrue(mWifiManager.disableNetwork(netId));
        wifiConfiguration = mWifiManager.getConfiguredNetworks().get(pos);
        assertEquals(Status.DISABLED, wifiConfiguration.status);

        // Update a WifiConfig
        wifiConfiguration = wifiConfiguredNetworks.get(pos);
        wifiConfiguration.SSID = SSID2;
        netId = mWifiManager.updateNetwork(wifiConfiguration);
        assertFalse(existSSID(SSID1));
        assertTrue(existSSID(SSID2));

        // Remove a WifiConfig
        assertTrue(mWifiManager.removeNetwork(netId));
        assertFalse(mWifiManager.removeNetwork(notExist));
        assertFalse(existSSID(SSID1));
        assertFalse(existSSID(SSID2));

        assertTrue(mWifiManager.saveConfiguration());
}

@TestTargets({







