/*Fix join scenario with provision discovery

Bug: 7072467
Change-Id:I8aafd5cee1055463aeda26ad313071cfe4e335c6*/
//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/p2p/WifiP2pService.java b/wifi/java/android/net/wifi/p2p/WifiP2pService.java
//Synthetic comment -- index 0212552..0be2b27 100644

//Synthetic comment -- @@ -1120,7 +1120,7 @@

if (mSavedPeerConfig.wps.setup == WpsInfo.PBC) {
if (DBG) logd("Found a match " + mSavedPeerConfig);
                        mWifiNative.p2pConnect(mSavedPeerConfig, FORM_GROUP);
transitionTo(mGroupNegotiationState);
}
break;
//Synthetic comment -- @@ -1133,7 +1133,7 @@
if (DBG) logd("Found a match " + mSavedPeerConfig);
/* we already have the pin */
if (!TextUtils.isEmpty(mSavedPeerConfig.wps.pin)) {
                            mWifiNative.p2pConnect(mSavedPeerConfig, FORM_GROUP);
transitionTo(mGroupNegotiationState);
} else {
mJoinExistingGroup = false;
//Synthetic comment -- @@ -1152,7 +1152,7 @@
if (mSavedPeerConfig.wps.setup == WpsInfo.DISPLAY) {
if (DBG) logd("Found a match " + mSavedPeerConfig);
mSavedPeerConfig.wps.pin = provDisc.pin;
                        mWifiNative.p2pConnect(mSavedPeerConfig, FORM_GROUP);
if (!sendShowPinReqToFrontApp(provDisc.pin)) {
notifyInvitationSent(provDisc.pin, device.deviceAddress);
}
//Synthetic comment -- @@ -1861,7 +1861,7 @@
return NEEDS_PROVISION_REQ;
}

        p2pConnectWithPinDisplay(config, join);
return CONNECT_SUCCESS;
}

//Synthetic comment -- @@ -1968,8 +1968,14 @@
return deviceAddress;
}

    private void p2pConnectWithPinDisplay(WifiP2pConfig config, boolean join) {
        String pin = mWifiNative.p2pConnect(config, join);
try {
Integer.parseInt(pin);
if (!sendShowPinReqToFrontApp(pin)) {







