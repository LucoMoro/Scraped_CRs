/*Try go-negotiation when invocation request is deferred.

Devices setting persistent_reconnect to 0 in wpa_supplicant returns "information is currently unable" error.
In that case, the connection establishment was always failed when the other device does NOT handle the invitation request.
So, try go-negotiation for interoperability.

Signed-off-by: Yoshihiko Ikenaga <yoshihiko.ikenaga@jp.sony.com>*/




//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/p2p/WifiP2pService.java b/wifi/java/android/net/wifi/p2p/WifiP2pService.java
//Synthetic comment -- index 4a4320c..65f40081 100644

//Synthetic comment -- @@ -1355,13 +1355,17 @@
// invocation was succeeded.
// wait P2P_GROUP_STARTED_EVENT.
break;
                    } else if (status == P2pStatus.UNKNOWN_P2P_GROUP ||
                            status == P2pStatus.INFORMATION_IS_CURRENTLY_UNAVAILABLE) {

                        if (status == P2pStatus.UNKNOWN_P2P_GROUP) {
                            // target device has already removed the credential.
                            // So, remove this credential accordingly.
                            int netId = mSavedPeerConfig.netId;
                            if (netId >= 0) {
                                if (DBG) logd("Remove unknown client from the list");
                                removeClientFromList(netId, mSavedPeerConfig.deviceAddress, true);
                            }
}

// invocation is failed or deferred. Try another way to connect.







