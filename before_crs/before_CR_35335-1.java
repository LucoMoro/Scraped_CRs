/*P2P: Specify WPS authentication mechanism before invitation

As per wpa_supplicant spec, while inviting a member to an already
existing P2P group, an explicit selection of WPS authentication
mechanism (PBC/PIN) is required. This can be done through wpa_cli
using the wps_pbc or wps_pin commands before invoking p2p_invite.
However, apparently, there is no API exposed through the SDK for
the app developers to accomplish the same. The SDK provides only
the connect() call for group formation.

Hence, if the connect() call translates to a p2p invitation, the
framework must ensure to natively call wps_pbc or wps_pin (as per
the authentication mechanism stated in the connect() call) before
actually invoking p2p_invite.

Change-Id:I6a4bc8ceee76e5b6a048583458cd407ed35f6c09Signed-off-by: Kaustav Dey Biswas <kaustavdeybiswas@gmail.com>*/
//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/p2p/WifiP2pService.java b/wifi/java/android/net/wifi/p2p/WifiP2pService.java
//Synthetic comment -- index 6bb22a4..af41531 100644

//Synthetic comment -- @@ -1028,6 +1028,10 @@
case WifiP2pManager.CONNECT:
WifiP2pConfig config = (WifiP2pConfig) message.obj;
logd("Inviting device : " + config.deviceAddress);
if (WifiNative.p2pInvite(mGroup, config.deviceAddress)) {
updateDeviceStatus(config.deviceAddress, WifiP2pDevice.INVITED);
sendP2pPeersChangedBroadcast();







