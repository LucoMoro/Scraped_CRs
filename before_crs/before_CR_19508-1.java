/*Avoid getting null BSSID when phone connects to an AP

When switching from one AP to another and receiving the NETWORK_STATE_CHANGED
event WifiStateTracker will clear the value of BSSID of the previous AP and
send null value of BSSID when broadcast event to WifiLayer. To avoid this
issue, preserve value of BSSID before WifiStateTracker clear it. And then
broadcast the preserved value to WifiLayer.

Change-Id:I4c5ba03268f2a41c6e188b2727e3039cdbb580cb*/
//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiStateTracker.java b/wifi/java/android/net/wifi/WifiStateTracker.java
//Synthetic comment -- index aef93c6..2d52a0e 100644

//Synthetic comment -- @@ -1056,7 +1056,7 @@
setDetailedState(DetailedState.OBTAINING_IPADDR);
}
}
                sendNetworkStateChangeBroadcast(mWifiInfo.getBSSID());
break;

case EVENT_SCAN_RESULTS_AVAILABLE:







