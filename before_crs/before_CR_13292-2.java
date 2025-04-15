/*Changes to enable adhoc wifi connections

Change-Id:I4a9792fa3e973cd00bd798097774cc988b86ab74*/
//Synthetic comment -- diff --git a/src/com/android/settings/wifi/AccessPointState.java b/src/com/android/settings/wifi/AccessPointState.java
//Synthetic comment -- index 5aefa55..dc154f4 100644

//Synthetic comment -- @@ -75,6 +75,8 @@
public NetworkInfo.DetailedState status;
public String security;
public boolean disabled;

/**
* Use this for sorting based on signal strength. It is a heavily-damped
//Synthetic comment -- @@ -139,6 +141,8 @@
ssid = "";
networkId = NETWORK_ID_NOT_SET;
hiddenSsid = false;
}

void setContext(Context context) {
//Synthetic comment -- @@ -243,6 +247,20 @@
requestRefresh();
}
}

public void setLinkSpeed(int linkSpeed) {
if (this.linkSpeed != linkSpeed) {
//Synthetic comment -- @@ -309,6 +327,11 @@
}
setSignal(scanResult.level);
setSecurity(getScanResultSecurity(scanResult));
unblockRefresh();
}

//Synthetic comment -- @@ -348,6 +371,8 @@
setNetworkId(wifiConfig.networkId);
setPriority(wifiConfig.priority);
setHiddenSsid(wifiConfig.hiddenSSID);
setSsid(wifiConfig.SSID);
setConfigured(true);
setDisabled(wifiConfig.status == WifiConfiguration.Status.DISABLED);
//Synthetic comment -- @@ -478,6 +503,8 @@
config.BSSID = getWpaSupplicantBssid();
config.priority = priority;
config.hiddenSSID = hiddenSsid;
config.SSID = convertToQuotedString(ssid);
config.eap.setValue(mEap);

//Synthetic comment -- @@ -845,6 +872,8 @@
dest.writeInt(primary ? 1 : 0);
dest.writeInt(priority);
dest.writeInt(hiddenSsid ? 1 : 0);
dest.writeString(security);
dest.writeInt(seen ? 1 : 0);
dest.writeInt(disabled ? 1 : 0);
//Synthetic comment -- @@ -874,6 +903,8 @@
state.primary = in.readInt() == 1;
state.priority = in.readInt();
state.hiddenSsid = in.readInt() == 1;
state.security = in.readString();
state.seen = in.readInt() == 1;
state.disabled = in.readInt() == 1;








//Synthetic comment -- diff --git a/src/com/android/settings/wifi/WifiLayer.java b/src/com/android/settings/wifi/WifiLayer.java
//Synthetic comment -- index ce518e1..9845358 100644

//Synthetic comment -- @@ -348,6 +348,15 @@
return false;
}

/*
* Give it highest priority, this could cause a network ID change, so do
* it after any modifications to the network we're connecting to
//Synthetic comment -- @@ -1063,6 +1072,11 @@

} else if (isDisconnected) {

/*
* When we drop off a network (for example, the router is powered
* down when we were connected), we received a DISCONNECT event
//Synthetic comment -- @@ -1076,6 +1090,12 @@

} else if (detailedState.equals(DetailedState.FAILED)) {

/*
* Doh, failed for whatever reason. Unset the primary AP, but set
* failed status on the AP that failed.
//Synthetic comment -- @@ -1147,9 +1167,10 @@
* Ignore adhoc, enterprise-secured, or hidden networks.
* Hidden networks show up with empty SSID.
*/
                    if (AccessPointState.isAdhoc(scanResult)
                            || TextUtils.isEmpty(scanResult.SSID)) {
                        continue;
}

final String ssid = AccessPointState.convertToQuotedString(scanResult.SSID);







