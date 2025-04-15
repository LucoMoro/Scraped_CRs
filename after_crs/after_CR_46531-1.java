/*Fixed typo and space.

Signed-off-by: Yoshihiko Ikenaga <yoshihiko.ikenaga@jp.sony.com>*/




//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiStateMachine.java b/wifi/java/android/net/wifi/WifiStateMachine.java
//Synthetic comment -- index dafa8e8..9898b53 100644

//Synthetic comment -- @@ -127,7 +127,7 @@
private final boolean mBackgroundScanSupported;

private String mInterfaceName;
    /* Tethering interface could be separate from wlan interface */
private String mTetherInterfaceName;

private int mLastSignalLevel = -1;
//Synthetic comment -- @@ -248,7 +248,7 @@
static final int CMD_START_DRIVER                     = BASE + 13;
/* Stop the driver */
static final int CMD_STOP_DRIVER                      = BASE + 14;
    /* Indicates Static IP succeeded */
static final int CMD_STATIC_IP_SUCCESS                = BASE + 15;
/* Indicates Static IP failed */
static final int CMD_STATIC_IP_FAILURE                = BASE + 16;
//Synthetic comment -- @@ -263,7 +263,7 @@

/* Start the soft access point */
static final int CMD_START_AP                         = BASE + 21;
    /* Indicates soft ap start succeeded */
static final int CMD_START_AP_SUCCESS                 = BASE + 22;
/* Indicates soft ap start failed */
static final int CMD_START_AP_FAILURE                 = BASE + 23;
//Synthetic comment -- @@ -883,22 +883,22 @@
* TODO: doc
*/
public void setScanOnlyMode(boolean enable) {
        if (enable) {
            sendMessage(obtainMessage(CMD_SET_SCAN_MODE, SCAN_ONLY_MODE, 0));
        } else {
            sendMessage(obtainMessage(CMD_SET_SCAN_MODE, CONNECT_MODE, 0));
        }
}

/**
* TODO: doc
*/
public void setScanType(boolean active) {
        if (active) {
            sendMessage(obtainMessage(CMD_SET_SCAN_TYPE, SCAN_ACTIVE, 0));
        } else {
            sendMessage(obtainMessage(CMD_SET_SCAN_TYPE, SCAN_PASSIVE, 0));
        }
}

/**








//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/p2p/WifiP2pService.java b/wifi/java/android/net/wifi/p2p/WifiP2pService.java
//Synthetic comment -- index 4a4320c..d95d624 100644

//Synthetic comment -- @@ -165,11 +165,11 @@
/* Commands to the WifiStateMachine */
public static final int P2P_CONNECTION_CHANGED          =   BASE + 11;

    /* These commands are used to temporarily disconnect wifi when we detect
* a frequency conflict which would make it impossible to have with p2p
* and wifi active at the same time.
*
     * If the user chooses to disable wifi temporarily, we keep wifi disconnected
* until the p2p connection is done and terminated at which point we will
* bring back wifi up
*
//Synthetic comment -- @@ -389,7 +389,7 @@
* not get latest updates about the device without being in discovery state.
*
* From the framework perspective, the device is still there since we are connecting or
         * connected to it. so we keep these devices in a separate list, so that they are removed
* when connection is cancelled or lost
*/
private final WifiP2pDeviceList mPeersLostDuringConnection = new WifiP2pDeviceList();
//Synthetic comment -- @@ -1055,48 +1055,48 @@
//and wait instead for the GO_NEGOTIATION_REQUEST_EVENT.
//Handling provision discovery and issuing a p2p_connect before
//group negotiation comes through causes issues
                    break;
case WifiP2pManager.CREATE_GROUP:
                    mAutonomousGroup = true;
                    int netId = message.arg1;
                    boolean ret = false;
                    if (netId == WifiP2pGroup.PERSISTENT_NET_ID) {
                        // check if the go persistent group is present.
                        netId = mGroups.getNetworkId(mThisDevice.deviceAddress);
                        if (netId != -1) {
                            ret = mWifiNative.p2pGroupAdd(netId);
                        } else {
                            ret = mWifiNative.p2pGroupAdd(true);
                        }
                    } else {
                        ret = mWifiNative.p2pGroupAdd(false);
                    }

                    if (ret) {
                        replyToMessage(message, WifiP2pManager.CREATE_GROUP_SUCCEEDED);
                        transitionTo(mGroupNegotiationState);
                    } else {
                        replyToMessage(message, WifiP2pManager.CREATE_GROUP_FAILED,
                                WifiP2pManager.ERROR);
                        // remain at this state.
                    }
                    break;
case WifiMonitor.P2P_GROUP_STARTED_EVENT:
                    mGroup = (WifiP2pGroup) message.obj;
                    if (DBG) logd(getName() + " group started");

// We hit this scenario when a persistent group is reinvoked
                    if (mGroup.getNetworkId() == WifiP2pGroup.PERSISTENT_NET_ID) {
                        mAutonomousGroup = false;
                        deferMessage(message);
                        transitionTo(mGroupNegotiationState);
                    } else {
                        loge("Unexpected group creation, remove " + mGroup);
                        mWifiNative.p2pGroupRemove(mGroup.getInterface());
                    }
                    break;
default:
                    return NOT_HANDLED;
}
return HANDLED;
}







