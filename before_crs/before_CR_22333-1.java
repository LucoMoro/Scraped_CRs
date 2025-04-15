/*Allow wifi (client mode) in parallel to mobile data network

This patch allows an application to communicate to a wifi network (as client mode) and to a mobile data network (Internet) in parallel. This enables applications to communicate with services on a local wifi network (e.g. cars, amplifiers, hifi-systems) and the internet at the same time, without having to explicitly reconfigure any devices on the local wifi network. In contrast to tethering, this patch does not allow devices on the local wifi network to access the mobile data network of the android device.

description
- The user configures in the wifi settings for a specific network that internet access via this network should not be used. By default this option is not activated.
- After a connection to the wifi network is established, both radios (wifi and mobile) remain active.
- The default internet gateway as well as the DNS servers are taken from the mobile data network, so that all internet traffic from applications on the android device will be routed through this interface. DNS servers from the wifi network are not used in this case.
- Applications on the android device can now communicate with local wifi nodes and the Internet simultaneously

actors
- Wifi network without Internet access
- Mobile network like 3G, EDGE
- Device and application

example
A media application wants to receive web radio content from the internet and redirect it to a home entertainment system located on a local wifi network.

implementation details
- A checkbox was added to the wifi settings dialog
- NetworkStateTracker was modified to include the new function useAsDefaultGw(). The default implementation is true, but the WifiStateTracker overrides this to get the value corresponding to the settings
- Depending on the wifi settings:
  - the teardown of the old data connection is prevented
  - default routes are removed after successful dhcp-resolution
  - dns servers are not added

Change-Id:I7c6b093b4f2dd1e7a46c0572b422dc67387c078cSigned-off-by: Michael Fitzner <michael.fitzner@bmw-carit.de>*/
//Synthetic comment -- diff --git a/core/java/android/net/NetworkStateTracker.java b/core/java/android/net/NetworkStateTracker.java
//Synthetic comment -- index 039dfff..655c714 100644

//Synthetic comment -- @@ -410,4 +410,12 @@
public void interpretScanResultsAvailable() {
}

}








//Synthetic comment -- diff --git a/services/java/com/android/server/ConnectivityService.java b/services/java/com/android/server/ConnectivityService.java
//Synthetic comment -- index bc102e4..8f1b914 100644

//Synthetic comment -- @@ -1212,9 +1212,13 @@
boolean isFailover = info.isFailover();
NetworkStateTracker thisNet = mNetTrackers[type];

// if this is a default net and other default is running
// kill the one not preferred
        if (mNetAttributes[type].isDefault()) {
if (mActiveDefaultNetwork != -1 && mActiveDefaultNetwork != type) {
if ((type != mNetworkPreference &&
mNetAttributes[mActiveDefaultNetwork].mPriority >
//Synthetic comment -- @@ -1376,7 +1380,7 @@
private void handleDnsConfigurationChange(int netType) {
// add default net's dns entries
NetworkStateTracker nt = mNetTrackers[netType];
        if (nt != null && nt.getNetworkInfo().isConnected() && !nt.isTeardownRequested()) {
String[] dnsList = nt.getNameServers();
if (mNetAttributes[netType].isDefault()) {
int j = 1;








//Synthetic comment -- diff --git a/services/java/com/android/server/WifiService.java b/services/java/com/android/server/WifiService.java
//Synthetic comment -- index 5aa0111..a80cfd0 100644

//Synthetic comment -- @@ -897,6 +897,14 @@
} catch (NumberFormatException ignore) {
}
}

value = mWifiStateTracker.getNetworkVariable(netId, WifiConfiguration.hiddenSSIDVarName);
config.hiddenSSID = false;
//Synthetic comment -- @@ -1214,6 +1222,15 @@
}
break setVariables;
}

if (config.hiddenSSID && !mWifiStateTracker.setNetworkVariable(
netId,








//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiConfiguration.java b/wifi/java/android/net/wifi/WifiConfiguration.java
//Synthetic comment -- index 01bc919..eeaa6cc 100644

//Synthetic comment -- @@ -39,6 +39,8 @@
/** {@hide} */
public static final String wepTxKeyIdxVarName = "wep_tx_keyidx";
/** {@hide} */
public static final String priorityVarName = "priority";
/** {@hide} */
public static final String hiddenSSIDVarName = "scan_ssid";
//Synthetic comment -- @@ -294,6 +296,13 @@
*/
public BitSet allowedGroupCiphers;


public WifiConfiguration() {
networkId = -1;
//Synthetic comment -- @@ -312,6 +321,7 @@
for (EnterpriseField field : enterpriseFields) {
field.setValue(null);
}
}

public String toString() {
//Synthetic comment -- @@ -323,6 +333,7 @@
}
sbuf.append("ID: ").append(this.networkId).append(" SSID: ").append(this.SSID).
append(" BSSID: ").append(this.BSSID).append(" PRIO: ").append(this.priority).
append('\n');
sbuf.append(" KeyMgmt:");
for (int k = 0; k < this.allowedKeyManagement.size(); k++) {
//Synthetic comment -- @@ -438,6 +449,7 @@
dest.writeInt(status);
dest.writeString(SSID);
dest.writeString(BSSID);
dest.writeString(preSharedKey);
for (String wepKey : wepKeys)
dest.writeString(wepKey);
//Synthetic comment -- @@ -465,6 +477,7 @@
config.status = in.readInt();
config.SSID = in.readString();
config.BSSID = in.readString();
config.preSharedKey = in.readString();
for (int i = 0; i < config.wepKeys.length; i++)
config.wepKeys[i] = in.readString();








//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiStateTracker.java b/wifi/java/android/net/wifi/WifiStateTracker.java
//Synthetic comment -- index 27e6a72..d0615e61 100644

//Synthetic comment -- @@ -1356,6 +1356,15 @@
int event;
if (NetworkUtils.configureInterface(mInterfaceName, mDhcpInfo)) {
mHaveIpAddress = true;
event = EVENT_INTERFACE_CONFIGURATION_SUCCEEDED;
if (LOCAL_LOGD) Log.v(TAG, "Static IP configuration succeeded");
} else {
//Synthetic comment -- @@ -1936,7 +1945,13 @@
if (mWifiState.get() != WIFI_STATE_ENABLED) {
return null;
}
        return WifiNative.getNetworkVariableCommand(netId, name);
}

/**
//Synthetic comment -- @@ -1951,7 +1966,13 @@
if (mWifiState.get() != WIFI_STATE_ENABLED) {
return false;
}
        return WifiNative.setNetworkVariableCommand(netId, name, value);
}

/**
//Synthetic comment -- @@ -2468,6 +2489,15 @@
if (NetworkUtils.runDhcp(mInterfaceName, mDhcpInfo)) {
event = EVENT_INTERFACE_CONFIGURATION_SUCCEEDED;
if (LOCAL_LOGD) Log.v(TAG, "DhcpHandler: DHCP request succeeded");
} else {
event = EVENT_INTERFACE_CONFIGURATION_FAILED;
Log.i(TAG, "DhcpHandler: DHCP request failed: " +
//Synthetic comment -- @@ -2672,4 +2702,20 @@
Settings.Secure.WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON, 1) == 1;
}
}
}







