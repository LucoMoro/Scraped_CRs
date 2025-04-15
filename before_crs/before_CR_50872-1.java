/*Prefer protected Wi-Fi

When both open and protected-and-already-succesfully-used (i.e.
authentication already took place once and was succesful) access points
are available, the protected AP should be prefered over the open AP.
This patch implements this behavior. This behavior is useful to those
having access to the protected AP in environements where both open and
protected access points are simultaneously availables.

Change-Id:I3b0998ff02369e5fb10db90cdb888df954ddb242*/
//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiConfigStore.java b/wifi/java/android/net/wifi/WifiConfigStore.java
//Synthetic comment -- index 84506b6..d2c75ad 100644

//Synthetic comment -- @@ -124,7 +124,8 @@
new HashMap<Integer, Integer>();

/* Tracks the highest priority of configured networks */
    private int mLastPriority = -1;

private static final String ipConfigFile = Environment.getDataDirectory() +
"/misc/wifi/ipconfig.txt";
//Synthetic comment -- @@ -211,23 +212,46 @@
boolean selectNetwork(int netId) {
if (netId == INVALID_NETWORK_ID) return false;

        // Reset the priority of each network at start or if it goes too high.
        if (mLastPriority == -1 || mLastPriority > 1000000) {
            for(WifiConfiguration config : mConfiguredNetworks.values()) {
                if (config.networkId != INVALID_NETWORK_ID) {
                    config.priority = 0;
                    addOrUpdateNetworkNative(config);
}
}
            mLastPriority = 0;
}

        // Set to the highest priority and save the configuration.
        WifiConfiguration config = new WifiConfiguration();
        config.networkId = netId;
        config.priority = ++mLastPriority;

        addOrUpdateNetworkNative(config);
mWifiNative.saveConfig();

/* Enable the given network while disabling all other networks */
//Synthetic comment -- @@ -617,7 +641,8 @@

void loadConfiguredNetworks() {
String listStr = mWifiNative.listNetworks();
        mLastPriority = 0;

mConfiguredNetworks.clear();
mNetworkIds.clear();
//Synthetic comment -- @@ -647,9 +672,18 @@
config.status = WifiConfiguration.Status.ENABLED;
}
readNetworkVariables(config);
            if (config.priority > mLastPriority) {
                mLastPriority = config.priority;
}
mConfiguredNetworks.put(config.networkId, config);
mNetworkIds.put(configKey(config), config.networkId);
}
//Synthetic comment -- @@ -1581,7 +1615,8 @@
String dump() {
StringBuffer sb = new StringBuffer();
String LS = System.getProperty("line.separator");
        sb.append("mLastPriority ").append(mLastPriority).append(LS);
sb.append("Configured networks ").append(LS);
for (WifiConfiguration conf : getConfiguredNetworks()) {
sb.append(conf).append(LS);







