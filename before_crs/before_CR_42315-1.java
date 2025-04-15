/*Fix: Not enter provision request state in the case of join.

Join command in wpa_supplicant automatically sends provision
request message before starting WPS sequence, so we don't
enter the provision request state in the case of join.

Change-Id:Iffe272c88bfcc03a34c8f52a6fdee0b0e57723d1Signed-off-by: Yoshihiko Ikenaga <yoshihiko.ikenaga@jp.sony.com>*/
//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/p2p/WifiP2pService.java b/wifi/java/android/net/wifi/p2p/WifiP2pService.java
//Synthetic comment -- index 0be2b27..4196ef3e 100644

//Synthetic comment -- @@ -1792,8 +1792,8 @@
return CONNECT_FAILURE;
}

        boolean isResp = (mSavedPeerConfig != null &&
                config.deviceAddress.equals(mSavedPeerConfig.deviceAddress));
mSavedPeerConfig = config;

WifiP2pDevice dev = mPeers.get(config.deviceAddress);
//Synthetic comment -- @@ -1857,7 +1857,8 @@
//Stop discovery before issuing connect
mWifiNative.p2pStopFind();

        if (!isResp) {
return NEEDS_PROVISION_REQ;
}








