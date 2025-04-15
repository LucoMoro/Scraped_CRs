/*wifiSettings: Updating the networkinfo on supplicant event

In framework we update the networkId on supplicant state change
these changes in the network info should be reflected in settings.
there by updating the networkinfo.
The problem we are facing without this fix is when we have single
saved AP profile Switching off the AP when we are in connecting
tate resulting in a behavior Where UI keep showing connecting
to that AP instead of showing it as out of range.

Change-Id:I04073b9a4068f4b75216ebfd431adc126583f15c*/




//Synthetic comment -- diff --git a/src/com/android/settings/wifi/WifiSettings.java b/src/com/android/settings/wifi/WifiSettings.java
//Synthetic comment -- index aedb79e..fe460ab 100644

//Synthetic comment -- @@ -835,7 +835,12 @@
WifiManager.EXTRA_NEW_STATE);
if (!mConnected.get() && SupplicantState.isHandshakeState(state)) {
updateConnectionState(WifiInfo.getDetailedStateOf(state));
             } else {
                 //supplicant state change from connecting to disconnected state
                 //results in updation of netID in networkinfo to -1 in framework
                 // this changes in networkinfo needs to be updated here.
                 updateConnectionState(null);
             }
} else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
NetworkInfo info = (NetworkInfo) intent.getParcelableExtra(
WifiManager.EXTRA_NETWORK_INFO);







