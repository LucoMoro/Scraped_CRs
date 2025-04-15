/*Add WifiConfiguration.Mode and support for ad-hoc wifi.

Change-Id:I488341cbc6fdfb0db282e1156a006fa03766aecc*/
//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiConfigStore.java b/wifi/java/android/net/wifi/WifiConfigStore.java
//Synthetic comment -- index e9f3480..765576f 100644

//Synthetic comment -- @@ -228,6 +228,10 @@

addOrUpdateNetworkNative(config);
mWifiNative.saveConfig();

/* Enable the given network while disabling all other networks */
enableNetworkWithoutBroadcast(netId, true);
//Synthetic comment -- @@ -985,6 +989,16 @@

setVariables: {

if (config.SSID != null &&
!mWifiNative.setNetworkVariable(
netId,
//Synthetic comment -- @@ -1315,7 +1329,16 @@
*/
String value;

value = mWifiNative.getNetworkVariable(netId, WifiConfiguration.ssidVarName);
if (!TextUtils.isEmpty(value)) {
config.SSID = value;
} else {








//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiConfiguration.java b/wifi/java/android/net/wifi/WifiConfiguration.java
//Synthetic comment -- index 0a846fd..71287a7 100644

//Synthetic comment -- @@ -129,6 +129,23 @@
engine, engine_id, key_id, ca_cert };

/**
* Recognized key management schemes.
*/
public static class KeyMgmt {
//Synthetic comment -- @@ -278,6 +295,13 @@
public int disableReason;

/**
* The network's SSID. Can either be an ASCII string,
* which must be enclosed in double quotation marks
* (e.g., {@code "MyNetwork"}, or a string of
//Synthetic comment -- @@ -401,11 +425,12 @@

public WifiConfiguration() {
networkId = INVALID_NETWORK_ID;
SSID = null;
BSSID = null;
priority = 0;
hiddenSSID = false;
        disableReason = DISABLED_UNKNOWN_REASON;
allowedKeyManagement = new BitSet();
allowedProtocols = new BitSet();
allowedAuthAlgorithms = new BitSet();
//Synthetic comment -- @@ -430,8 +455,11 @@
} else if (this.status == WifiConfiguration.Status.DISABLED) {
sbuf.append("- DSBLE: ").append(this.disableReason).append(" ");
}
        sbuf.append("ID: ").append(this.networkId).append(" SSID: ").append(this.SSID).
                append(" BSSID: ").append(this.BSSID).append(" PRIO: ").append(this.priority).
append('\n');
sbuf.append(" KeyMgmt:");
for (int k = 0; k < this.allowedKeyManagement.size(); k++) {
//Synthetic comment -- @@ -568,6 +596,7 @@
networkId = source.networkId;
status = source.status;
disableReason = source.disableReason;
SSID = source.SSID;
BSSID = source.BSSID;
preSharedKey = source.preSharedKey;
//Synthetic comment -- @@ -599,6 +628,7 @@
dest.writeInt(networkId);
dest.writeInt(status);
dest.writeInt(disableReason);
dest.writeString(SSID);
dest.writeString(BSSID);
dest.writeString(preSharedKey);
//Synthetic comment -- @@ -630,6 +660,7 @@
config.networkId = in.readInt();
config.status = in.readInt();
config.disableReason = in.readInt();
config.SSID = in.readString();
config.BSSID = in.readString();
config.preSharedKey = in.readString();







