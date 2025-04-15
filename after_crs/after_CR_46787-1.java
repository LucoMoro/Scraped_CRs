/*Try go-negotiation when invocation request is deferred.

Devices setting persistent_reconnect to 0 in wpa_supplicant returns "information is currently unable" error.
In that case, the connection establishment was always failed when the other device does NOT handle the invitation request.
So, try go-negotiation for interoperability.

Change-Id:I17f02568c7a06e003770c3d3e200167f071538d6Signed-off-by: Yoshihiko Ikenaga <yoshihiko.ikenaga@jp.sony.com>*/




//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/p2p/WifiP2pService.java b/wifi/java/android/net/wifi/p2p/WifiP2pService.java
//Synthetic comment -- index 4a4320c..f2f61e7 100644

//Synthetic comment -- @@ -1364,7 +1364,18 @@
removeClientFromList(netId, mSavedPeerConfig.deviceAddress, true);
}

                        // invocation is failed. Try another way to connect.
                        mSavedPeerConfig.netId = WifiP2pGroup.PERSISTENT_NET_ID;
                        if (connect(mSavedPeerConfig, NO_REINVOCATION) == CONNECT_FAILURE) {
                            handleGroupCreationFailure();
                            transitionTo(mInactiveState);
                        }
                    } else if (status == P2pStatus.INFORMATION_IS_CURRENTLY_UNAVAILABLE) {

                        // Devices setting persistent_reconnect to 0 in wpa_supplicant
                        // always defer the invocation request and return
                        // "information is currently unable" error.
                        // So, try another way to connect for interoperability.
mSavedPeerConfig.netId = WifiP2pGroup.PERSISTENT_NET_ID;
if (connect(mSavedPeerConfig, NO_REINVOCATION) == CONNECT_FAILURE) {
handleGroupCreationFailure();







