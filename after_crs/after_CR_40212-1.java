/*Added a persistent feature in WiFi Direct.

Provide a new feature to use persistent group at default.
In the normal connect sequence, if the persistent profile has been
stored, try to use it. Otherwise, a new persistent group is created.
If the persistent profiles are stored over 32, an old profile is
deleted automatically.

Change-Id:I878841480281fb1b5f89ecd53d69e324874f2c01Signed-off-by: Yoshihiko Ikenaga <yoshihiko.ikenaga@jp.sony.com>*/




//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiMonitor.java b/wifi/java/android/net/wifi/WifiMonitor.java
//Synthetic comment -- index a447c86..17c930b 100644

//Synthetic comment -- @@ -20,6 +20,8 @@
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pService;
import android.net.wifi.p2p.WifiP2pService.P2pStatus;
import android.net.wifi.p2p.WifiP2pProvDiscEvent;
import android.net.wifi.p2p.nsd.WifiP2pServiceResponse;
import android.net.wifi.StateChangeResult;
//Synthetic comment -- @@ -186,7 +188,7 @@

/* P2P-GROUP-STARTED p2p-wlan0-0 [client|GO] ssid="DIRECT-W8" freq=2437
[psk=2182b2e50e53f260d04f3c7b25ef33c965a3291b9b36b455a82d77fd82ca15bc|passphrase="fKG4jMe3"]
       go_dev_addr=fa:7b:7a:42:02:13 [PERSISTENT] */
private static final String P2P_GROUP_STARTED_STR = "P2P-GROUP-STARTED";

/* P2P-GROUP-REMOVED p2p-wlan0-0 [client|GO] reason=REQUESTED */
//Synthetic comment -- @@ -594,7 +596,13 @@
if (tokens.length != 2) return;
String[] nameValue = tokens[1].split("=");
if (nameValue.length != 2) return;
                P2pStatus err = P2pStatus.UNKNOWN;
                try {
                    err = P2pStatus.valueOf(Integer.parseInt(nameValue[1]));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                mStateMachine.sendMessage(P2P_INVITATION_RESULT_EVENT, err);
} else if (dataString.startsWith(P2P_PROV_DISC_PBC_REQ_STR)) {
mStateMachine.sendMessage(P2P_PROV_DISC_PBC_REQ_EVENT,
new WifiP2pProvDiscEvent(dataString));








//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiNative.java b/wifi/java/android/net/wifi/WifiNative.java
//Synthetic comment -- index 4bf1ca3..3c482f4 100644

//Synthetic comment -- @@ -547,10 +547,9 @@
break;
}

        if (config.netId == WifiP2pGroup.PERSISTNET_NET_ID) {
            args.add("persistent");
        }

if (joinExistingGroup) {
args.add("join");
//Synthetic comment -- @@ -592,10 +591,17 @@
return false;
}

    public boolean p2pGroupAdd(boolean persistent) {
        if (persistent) {
            return doBooleanCommand("P2P_GROUP_ADD persistent");
        }
return doBooleanCommand("P2P_GROUP_ADD");
}

    public boolean p2pGroupAdd(int netId) {
        return doBooleanCommand("P2P_GROUP_ADD persistent=" + netId);
    }

public boolean p2pGroupRemove(String iface) {
if (TextUtils.isEmpty(iface)) return false;
return doBooleanCommand("P2P_GROUP_REMOVE " + iface);
//Synthetic comment -- @@ -624,6 +630,14 @@
return doBooleanCommand("P2P_INVITE persistent=" + netId + " peer=" + deviceAddress);
}

    public String p2pGetInterfaceAddress(String deviceAddress) {
        //TODO: update from interface_addr when wpa_supplicant implementation is fixed
        return p2pGetParam(deviceAddress, "intended_addr");
    }

    public String p2pGetSsid(String deviceAddress) {
        return p2pGetParam(deviceAddress, "oper_ssid");
    }

public String p2pGetDeviceAddress() {
String status = status();
//Synthetic comment -- @@ -665,6 +679,24 @@
return doStringCommand("P2P_PEER " + deviceAddress);
}

    private String p2pGetParam(String deviceAddress, String key) {
        if (deviceAddress == null) return null;

        String peerInfo = p2pPeer(deviceAddress);
        if (peerInfo == null) return null;
        String[] tokens= peerInfo.split("\n");

        key += "=";
        for (String token : tokens) {
            if (token.startsWith(key)) {
                String[] nameValue = token.split("=");
                if (nameValue.length != 2) break;
                return nameValue[1];
            }
        }
        return null;
    }

public boolean p2pServiceAdd(WifiP2pServiceInfo servInfo) {
/*
* P2P_SERVICE_ADD bonjour <query hexdump> <RDATA hexdump>








//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/p2p/WifiP2pConfig.java b/wifi/java/android/net/wifi/p2p/WifiP2pConfig.java
//Synthetic comment -- index 6aea090..a2ed29f 100644

//Synthetic comment -- @@ -46,18 +46,8 @@
*/
public int groupOwnerIntent = -1;

/** @hide */
    public int netId = WifiP2pGroup.PERSISTNET_NET_ID;

public WifiP2pConfig() {
//set defaults
//Synthetic comment -- @@ -110,7 +100,7 @@
sbuf.append("\n address: ").append(deviceAddress);
sbuf.append("\n wps: ").append(wps);
sbuf.append("\n groupOwnerIntent: ").append(groupOwnerIntent);
        sbuf.append("\n persist: ").append(netId);
return sbuf.toString();
}

//Synthetic comment -- @@ -125,7 +115,7 @@
deviceAddress = source.deviceAddress;
wps = new WpsInfo(source.wps);
groupOwnerIntent = source.groupOwnerIntent;
            netId = source.netId;
}
}

//Synthetic comment -- @@ -134,7 +124,7 @@
dest.writeString(deviceAddress);
dest.writeParcelable(wps, flags);
dest.writeInt(groupOwnerIntent);
        dest.writeInt(netId);
}

/** Implement the Parcelable interface */
//Synthetic comment -- @@ -145,7 +135,7 @@
config.deviceAddress = in.readString();
config.wps = (WpsInfo) in.readParcelable(null);
config.groupOwnerIntent = in.readInt();
                config.netId = in.readInt();
return config;
}









//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/p2p/WifiP2pDevice.java b/wifi/java/android/net/wifi/p2p/WifiP2pDevice.java
//Synthetic comment -- index afdc9be..c86ec8b 100644

//Synthetic comment -- @@ -231,11 +231,26 @@
return (deviceCapability & DEVICE_CAPAB_SERVICE_DISCOVERY) != 0;
}

    /** Returns true if the device is capable of invitation {@hide}*/
    public boolean isInvitationCapable() {
        return (deviceCapability & DEVICE_CAPAB_INVITATION_PROCEDURE) != 0;
    }

    /** Returns true if the device reaches the limit. {@hide}*/
    public boolean isDeviceLimit() {
        return (deviceCapability & DEVICE_CAPAB_DEVICE_LIMIT) != 0;
    }

/** Returns true if the device is a group owner */
public boolean isGroupOwner() {
return (groupCapability & GROUP_CAPAB_GROUP_OWNER) != 0;
}

    /** Returns true if the group reaches the limit. {@hide}*/
    public boolean isGroupLimit() {
        return (groupCapability & GROUP_CAPAB_GROUP_LIMIT) != 0;
    }

@Override
public boolean equals(Object obj) {
if (this == obj) return true;








//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/p2p/WifiP2pGroup.java b/wifi/java/android/net/wifi/p2p/WifiP2pGroup.java
//Synthetic comment -- index c30cc73..7f065aa 100644

//Synthetic comment -- @@ -33,6 +33,16 @@
*/
public class WifiP2pGroup implements Parcelable {

    /** The temporary network id.
     * {@hide} */
    public static final int TEMPORARY_NET_ID = -1;

    /** The persistent network id.
     * If the persistent profile has been stored before, use it.
     * Otherwise, create a new persistent profile
     * {@hide} */
    public static final int PERSISTNET_NET_ID = -2;

/** The network name */
private String mNetworkName;

//Synthetic comment -- @@ -50,13 +60,17 @@

private String mInterface;

    /** The network id in the wpa_supplicant */
    private int mNetId;

/** P2P group started string pattern */
private static final Pattern groupStartedPattern = Pattern.compile(
"ssid=\"(.+)\" " +
"freq=(\\d+) " +
"(?:psk=)?([0-9a-fA-F]{64})?" +
"(?:passphrase=)?(?:\"(.{8,63})\")? " +
        "go_dev_addr=((?:[0-9a-f]{2}:){5}[0-9a-f]{2})" +
        " ?(\\[PERSISTENT\\])?"
);

public WifiP2pGroup() {
//Synthetic comment -- @@ -67,13 +81,15 @@
*
*  P2P-GROUP-STARTED p2p-wlan0-0 [client|GO] ssid="DIRECT-W8" freq=2437
*  [psk=2182b2e50e53f260d04f3c7b25ef33c965a3291b9b36b455a82d77fd82ca15bc|
     *  passphrase="fKG4jMe3"] go_dev_addr=fa:7b:7a:42:02:13 [PERSISTENT]
*
*  P2P-GROUP-REMOVED p2p-wlan0-0 [client|GO] reason=REQUESTED
*
*  P2P-INVITATION-RECEIVED sa=fa:7b:7a:42:02:13 go_dev_addr=f8:7b:7a:42:02:13
*  bssid=fa:7b:7a:42:82:13 unknown-network
*
     *  P2P-INVITATION-RECEIVED sa=b8:f9:34:2a:c7:9d persistent=0
     *
*  Note: The events formats can be looked up in the wpa_supplicant code
*  @hide
*/
//Synthetic comment -- @@ -100,16 +116,36 @@
//String psk = match.group(3);
mPassphrase = match.group(4);
mOwner = new WifiP2pDevice(match.group(5));
            if (match.group(6) != null) {
                mNetId = PERSISTNET_NET_ID;
            }
} else if (tokens[0].equals("P2P-INVITATION-RECEIVED")) {
            String sa = null;
            mNetId = PERSISTNET_NET_ID;
for (String token : tokens) {
String[] nameValue = token.split("=");
if (nameValue.length != 2) continue;

                if (nameValue[0].equals("sa")) {
                    sa = nameValue[1];

                    // set source address into the client list.
                    WifiP2pDevice dev = new WifiP2pDevice();
                    dev.deviceAddress = nameValue[1];
                    mClients.add(dev);
                    continue;
                }

if (nameValue[0].equals("go_dev_addr")) {
mOwner = new WifiP2pDevice(nameValue[1]);
continue;
}

                if (nameValue[0].equals("persistent")) {
                    mOwner = new WifiP2pDevice(sa);
                    mNetId = Integer.parseInt(nameValue[1]);
                    continue;
                }
}
} else {
throw new IllegalArgumentException("Malformed supplicant event");
//Synthetic comment -- @@ -212,6 +248,16 @@
return mInterface;
}

    /** @hide */
    public int getNetworkId() {
        return mNetId;
    }

    /** @hide */
    public void setNetworkId(int netId) {
        this.mNetId = netId;
    }

public String toString() {
StringBuffer sbuf = new StringBuffer();
sbuf.append("network: ").append(mNetworkName);
//Synthetic comment -- @@ -221,6 +267,7 @@
sbuf.append("\n Client: ").append(client);
}
sbuf.append("\n interface: ").append(mInterface);
        sbuf.append("\n networkId: ").append(mNetId);
return sbuf.toString();
}

//Synthetic comment -- @@ -238,6 +285,7 @@
for (WifiP2pDevice d : source.getClientList()) mClients.add(d);
mPassphrase = source.getPassphrase();
mInterface = source.getInterface();
            mNetId = source.getNetworkId();
}
}

//Synthetic comment -- @@ -252,6 +300,7 @@
}
dest.writeString(mPassphrase);
dest.writeString(mInterface);
        dest.writeInt(mNetId);
}

/** Implement the Parcelable interface */
//Synthetic comment -- @@ -268,6 +317,7 @@
}
group.setPassphrase(in.readString());
group.setInterface(in.readString());
                group.setNetworkId(in.readInt());
return group;
}









//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/p2p/WifiP2pGroupList.java b/wifi/java/android/net/wifi/p2p/WifiP2pGroupList.java
new file mode 100644
//Synthetic comment -- index 0000000..02f3a6d

//Synthetic comment -- @@ -0,0 +1,207 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.net.wifi.p2p;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * A class representing a Wi-Fi P2p group list
 *
 * {@see WifiP2pManager}
 * @hide
 */
public class WifiP2pGroupList {

    private List<WifiP2pGroup> mGroups;

    public WifiP2pGroupList() {
        mGroups = new ArrayList<WifiP2pGroup>();
    }

    /** copy constructor */
    public WifiP2pGroupList(WifiP2pGroupList source) {
        if (source != null) {
            mGroups = source.getGroupList();
        }
    }

    /**
     * Remove all of the groups from this group list.
     * @return true if remove over one or more elements.
     * @hide
     */
    public boolean clear() {
        if (mGroups.isEmpty()) return false;
        mGroups.clear();
        return true;
    }

    /**
     * Add the specified group to this group list.
     * @param group
     * @return true if the element is added.
     * @hide
     */
    public boolean add(WifiP2pGroup group) {
        return mGroups.add(group);
    }

    /**
     * Remove the group with the specified network id from this group list.
     *
     * @param netId
     * @return true if the element is removed.
     * @hide
     */
    public boolean remove(int netId) {
        for (Iterator<WifiP2pGroup> i = mGroups.iterator(); i.hasNext();) {
            if (i.next().getNetworkId() == netId) {
                i.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Remove the group with the specified device address from this group list.
     *
     * @param netId
     * @return true if the element is removed.
     * @hide
     */
    public boolean remove(String deviceAddress) {
        return remove(getNetworkId(deviceAddress));
    }

    /**
     * Return the network id of the group owner profile with the specified p2p device
     * address.
     * If more than one persistent group of the same address is present in the list,
     * return the first one.
     *
     * @param deviceAddress p2p device address.
     * @return the network id. if not found, return -1.
     * @hide
     */
    public int getNetworkId(String deviceAddress) {
        if (deviceAddress == null) return -1;

        for (WifiP2pGroup group:mGroups) {
            if (deviceAddress.equalsIgnoreCase(group.getOwner().deviceAddress)) {
                return group.getNetworkId();
            }
        }
        return -1;
    }

    /**
     * Return the network id of the group with the specified p2p device address
     * and the ssid.
     *
     * @param deviceAddress p2p device address.
     * @param ssid ssid.
     * @return the network id. if not found, return -1.
     * @hide
     */
    public int getNetworkId(String deviceAddress, String ssid) {
        if (deviceAddress == null || ssid == null) {
            return -1;
        }
        for (WifiP2pGroup group:mGroups) {
            if (deviceAddress.equalsIgnoreCase(group.getOwner().deviceAddress) &&
                    ssid.equals(group.getNetworkName())) {
                return group.getNetworkId();
            }
        }
        return -1;
    }

    /**
     * Return the group owner address of the group with the specified network id
     *
     * @param netId network id.
     * @return the address. if not found, return null.
     * @hide
     */
    public String getOwnerAddr(int netId) {
        if (netId < 0) {
            return null;
        }
        for (WifiP2pGroup group:mGroups) {
            if (group.getNetworkId() == netId) {
                return group.getOwner().deviceAddress;
            }
        }
        return null;
    }

    /**
     * Return true if this group list contains the specified network id.
     * @param netId network id.
     * @return true if the specified network id is present in this group list.
     * @hide
     */
    public boolean contains(int netId) {
        for (WifiP2pGroup group:mGroups) {
            if (group.getNetworkId() == netId) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return the number of the groups in this list.
     * @return the number of the groups in this list.
     * @hide
     */
    public int size() {
        return mGroups.size();
    }

    /**
     * Return true if this group list contains no elements.
     * @return true if this group list contains no elements.
     * @hide
     */
    public boolean isEmpty() {
        return mGroups.isEmpty();
    }

    /**
     * Return the list of p2p group.
     * @return the list of p2p group.
     */
    public List<WifiP2pGroup> getGroupList() {
        return Collections.unmodifiableList(mGroups);
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        for (WifiP2pGroup group : mGroups) {
            sbuf.append("\n").append(group);
        }
        return sbuf.toString();
    }
}








//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/p2p/WifiP2pManager.java b/wifi/java/android/net/wifi/p2p/WifiP2pManager.java
//Synthetic comment -- index 2c25e9d..2d7f1fe 100644

//Synthetic comment -- @@ -990,12 +990,31 @@
*
* <p> Application can request for the group details with {@link #requestGroupInfo}.
*
     * <p> If a credential of persistent p2p group is/are in the system settings,
     * then a first one of the credential list will be used.
     *
* @param c is the channel created at {@link #initialize}
* @param listener for callbacks on success or failure. Can be null.
*/
public void createGroup(Channel c, ActionListener listener) {
checkChannel(c);
        c.mAsyncChannel.sendMessage(CREATE_GROUP, WifiP2pGroup.PERSISTNET_NET_ID,
                c.putListener(listener));
    }

    /**
     * Create a p2p group with the current device as the group owner.
     *
     * @param c is the channel created at {@link #initialize}
     * @param persistent create persistent group if true.
     * @param listener for callbacks on success or failure. Can be null.
     * @hide
     */
    public void createGroup(Channel c, boolean persistent, ActionListener listener) {
        checkChannel(c);
        c.mAsyncChannel.sendMessage(CREATE_GROUP,
                persistent ? WifiP2pGroup.PERSISTNET_NET_ID : WifiP2pGroup.TEMPORARY_NET_ID,
                c.putListener(listener));
}

/**








//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/p2p/WifiP2pService.java b/wifi/java/android/net/wifi/p2p/WifiP2pService.java
//Synthetic comment -- index 9e004d0..0ef26aa 100644

//Synthetic comment -- @@ -84,6 +84,8 @@
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;

//Synthetic comment -- @@ -118,6 +120,10 @@
private static final Boolean JOIN_GROUP = true;
private static final Boolean FORM_GROUP = false;

    private static final int FAILED = -1;
    private static final int SUCCESS = 0;
    private static final int NEEDS_PROVISION_REQ = 1;

/* Two minutes comes from the wpa_supplicant setting */
private static final int GROUP_CREATING_WAIT_TIME_MS = 120 * 1000;
private static int mGroupCreatingTimeoutIndex = 0;
//Synthetic comment -- @@ -150,6 +156,8 @@
/* User rejected a peer request */
private static final int PEER_CONNECTION_USER_REJECT    =   BASE + 3;

    private static final int CREDENTIAL_MAX_NUM             =   32;

private final boolean mP2pSupported;

private WifiP2pDevice mThisDevice = new WifiP2pDevice();
//Synthetic comment -- @@ -191,6 +199,84 @@
private static final String[] DHCP_RANGE = {"192.168.49.2", "192.168.49.254"};
private static final String SERVER_ADDRESS = "192.168.49.1";

    /**
     * Error code definition.
     * see the Table.8 in the WiFi Direct specification for the detail.
     */
    public static enum P2pStatus {
        /* Success. */
        SUCCESS,

        /* The target device is currently unavailable. */
        INFORMATION_IS_CURRENTLY_UNAVAILABLE,

        /* Protocol error. */
        INCOMPATIBLE_PARAMETERS,

        /* The target device reached the limit of the number of the connectable device.
         * For example, device limit or group limit is set. */
        LIMIT_REACHED,

        /* Protocol error. */
        INVALID_PARAMETER,

        /* Unable to accommodate request. */
        UNABLE_TO_ACCOMMODATE_REQUEST,

        /* Previous protocol error, or disruptive behavior. */
        PREVIOUS_PROTOCOL_ERROR,

        /* There is no common channels the both devices can use. */
        NO_COMMON_CHANNE,

        /* Unknown p2p group. For example, Device A tries to invoke the previous persistent group,
         *  but device B has removed the specified credential already. */
        UNKNOWN_P2P_GROUP,

        /* Both p2p devices indicated an intent of 15 in group owner negotiation. */
        BOTH_GO_INTENT_15,

        /* Incompatible provisioning method. */
        INCOMPATIBLE_PROVISIONING_METHOD,

        /* Rejected by user */
        REJECTED_BY_USER,

        /* Unknown error */
        UNKNOWN;

        public static P2pStatus valueOf(int error) {
            switch(error) {
            case 0 :
                return SUCCESS;
            case 1:
                return INFORMATION_IS_CURRENTLY_UNAVAILABLE;
            case 2:
                return INCOMPATIBLE_PARAMETERS;
            case 3:
                return LIMIT_REACHED;
            case 4:
                return INVALID_PARAMETER;
            case 5:
                return UNABLE_TO_ACCOMMODATE_REQUEST;
            case 6:
                return PREVIOUS_PROTOCOL_ERROR;
            case 7:
                return NO_COMMON_CHANNE;
            case 8:
                return UNKNOWN_P2P_GROUP;
            case 9:
                return BOTH_GO_INTENT_15;
            case 10:
                return INCOMPATIBLE_PROVISIONING_METHOD;
            case 11:
                return REJECTED_BY_USER;
            default:
                return UNKNOWN;
            }
        }
    }

public WifiP2pService(Context context) {
mContext = context;

//Synthetic comment -- @@ -273,6 +359,7 @@
private WifiMonitor mWifiMonitor = new WifiMonitor(this, mWifiNative);

private WifiP2pDeviceList mPeers = new WifiP2pDeviceList();
        private WifiP2pGroupList mGroups = new WifiP2pGroupList();
private WifiP2pInfo mWifiP2pInfo = new WifiP2pInfo();
private WifiP2pGroup mGroup;

//Synthetic comment -- @@ -626,6 +713,8 @@
break;
case WifiStateMachine.CMD_DISABLE_P2P:
if (mPeers.clear()) sendP2pPeersChangedBroadcast();
                    mGroups.clear();

mWifiNative.closeSupplicantConnection();
transitionTo(mP2pDisablingState);
break;
//Synthetic comment -- @@ -768,47 +857,24 @@
/* Update group capability before connect */
int gc = mWifiNative.getGroupCapability(config.deviceAddress);
mPeers.updateGroupCapability(config.deviceAddress, gc);
mSavedPeerConfig = config;
                    int connectRet = connect(mSavedPeerConfig, false);
                    if (connectRet == FAILED) {
                        replyToMessage(message, WifiP2pManager.CONNECT_FAILED);
                        break;
}
mPeers.updateStatus(mSavedPeerConfig.deviceAddress, WifiP2pDevice.INVITED);
sendP2pPeersChangedBroadcast();
replyToMessage(message, WifiP2pManager.CONNECT_SUCCEEDED);
                    if (connectRet == NEEDS_PROVISION_REQ) {
                        if (DBG) logd("Sending prov disc");
                        transitionTo(mProvisionDiscoveryState);
                        break;
                    }
                    transitionTo(mGroupNegotiationState);
break;
case WifiMonitor.P2P_GO_NEGOTIATION_REQUEST_EVENT:
mSavedPeerConfig = (WifiP2pConfig) message.obj;
mAutonomousGroup = false;
mJoinExistingGroup = false;
if (!sendConnectNoticeToApp(mPeers.get(mSavedPeerConfig.deviceAddress),
//Synthetic comment -- @@ -865,13 +931,42 @@
break;
case WifiP2pManager.CREATE_GROUP:
mAutonomousGroup = true;
                   checkCredentialNum();
                   int netId = message.arg1;
                   boolean ret = false;
                   if (netId == WifiP2pGroup.PERSISTNET_NET_ID) {
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

                    if (mGroup.getNetworkId() == WifiP2pGroup.PERSISTNET_NET_ID) {
                        // This is an invocation case.
                        mAutonomousGroup = false;
                        deferMessage(message);
transitionTo(mGroupNegotiationState);
                    } else {
                        return NOT_HANDLED;
                    }
break;
default:
return NOT_HANDLED;
//Synthetic comment -- @@ -941,11 +1036,8 @@
@Override
public void enter() {
if (DBG) logd(getName());
notifyInvitationReceived();
}

@Override
public boolean processMessage(Message message) {
//Synthetic comment -- @@ -953,11 +1045,10 @@
boolean ret = HANDLED;
switch (message.what) {
case PEER_CONNECTION_USER_ACCEPT:
                    if (connect(mSavedPeerConfig, false) == FAILED) {
                        handleGroupCreationFailure();
                        transitionTo(mInactiveState);
                        break;
}
mPeers.updateStatus(mSavedPeerConfig.deviceAddress, WifiP2pDevice.INVITED);
sendP2pPeersChangedBroadcast();
//Synthetic comment -- @@ -1017,9 +1108,12 @@
transitionTo(mGroupNegotiationState);
} else {
mJoinExistingGroup = false;
                            if (!sendConnectNoticeToApp(mPeers.get(mSavedPeerConfig.deviceAddress),
                                    mSavedPeerConfig)) {
transitionTo(mUserAuthorizingInvitationState);
}
}
                    }
break;
case WifiMonitor.P2P_PROV_DISC_SHOW_PIN_EVENT:
provDisc = (WifiP2pProvDiscEvent) message.obj;
//Synthetic comment -- @@ -1062,6 +1156,17 @@
case WifiMonitor.P2P_GROUP_STARTED_EVENT:
mGroup = (WifiP2pGroup) message.obj;
if (DBG) logd(getName() + " group started");

                    if (mGroup.getNetworkId() == WifiP2pGroup.PERSISTNET_NET_ID) {
                        /*
                         * update cache information and set network id to mGroup.
                         */
                        updatePersistentNetworks();
                        String devAddr = mGroup.getOwner().deviceAddress;
                        mGroup.setNetworkId(mGroups.getNetworkId(devAddr,
                                mGroup.getNetworkName()));
                    }

if (mGroup.isGroupOwner()) {
startDhcpServer(mGroup.getInterface());
} else {
//Synthetic comment -- @@ -1090,6 +1195,29 @@
// failure causes supplicant issues. Ignore right now.
case WifiMonitor.P2P_GROUP_FORMATION_FAILURE_EVENT:
break;
                case WifiMonitor.P2P_INVITATION_RESULT_EVENT:
                    P2pStatus status = (P2pStatus)message.obj;
                    if (status == P2pStatus.SUCCESS) {
                        // invocation was succeeded.
                        // wait P2P_GROUP_STARTED_EVENT.
                        break;
                    } else if (status == P2pStatus.UNKNOWN_P2P_GROUP) {
                        // target device has already removed the credential.
                        // So, remove this credential accordingly.
                        int netId = mSavedPeerConfig.netId;
                        if (netId >= 0) {
                            if (DBG) logd("Remove unknown client from the list");
                            removeClientFromList(netId, mSavedPeerConfig.deviceAddress, true);
                        }
                    }

                    // invocation is failed or deferred. Try another way to connect.
                    mSavedPeerConfig.netId = WifiP2pGroup.PERSISTNET_NET_ID;
                    if (connect(mSavedPeerConfig, true) == FAILED) {
                        handleGroupCreationFailure();
                        transitionTo(mInactiveState);
                    }
                    break;
default:
return NOT_HANDLED;
}
//Synthetic comment -- @@ -1263,6 +1391,28 @@
}
// TODO: figure out updating the status to declined when invitation is rejected
break;
                case WifiMonitor.P2P_INVITATION_RESULT_EVENT:
                    P2pStatus status = (P2pStatus)message.obj;
                    logd("===> INVITATION RESULT EVENT : " + status);
                    if (status == P2pStatus.SUCCESS) {
                        // invocation was succeeded.
                        break;
                    } else if (status == P2pStatus.UNKNOWN_P2P_GROUP) {
                        // target device has already removed the credential.
                        // So, remove this credential accordingly.
                        int netId = mGroup.getNetworkId();
                        if (netId >= 0) {
                            if (DBG) logd("Remove unknown client from the list");
                            if (!removeClientFromList(netId, mSavedPeerConfig.deviceAddress, false)) {
                                // not found the client on the list
                                Slog.e(TAG, "Already removed the client, ignore");
                                break;
                            }
                            // try invitation.
                            sendMessage(WifiP2pManager.CONNECT, mSavedPeerConfig);
                        }
                    }
                    break;
case WifiMonitor.P2P_PROV_DISC_PBC_REQ_EVENT:
case WifiMonitor.P2P_PROV_DISC_ENTER_PIN_EVENT:
case WifiMonitor.P2P_PROV_DISC_SHOW_PIN_EVENT:
//Synthetic comment -- @@ -1304,7 +1454,6 @@
@Override
public void enter() {
if (DBG) logd(getName());
notifyInvitationReceived();
}

//Synthetic comment -- @@ -1520,11 +1669,297 @@
dialog.show();
}

    private int getDeviceStatus(String deviceAddress) {
        for (WifiP2pDevice d : mPeers.getDeviceList()) {
            if (d.deviceAddress.equals(deviceAddress)) {
                return d.status;
            }
        }
        return WifiP2pDevice.UNAVAILABLE;
    }

    private boolean updateDeviceStatus(String deviceAddress, int status) {
        for (WifiP2pDevice d : mPeers.getDeviceList()) {
            if (d.deviceAddress.equals(deviceAddress)) {
                d.status = status;
                return true;
            }
        }
        return false;
    }

    private boolean isFound(String deviceAddress) {
        for (WifiP2pDevice d : mPeers.getDeviceList()) {
            if (d.deviceAddress.equals(deviceAddress)) {
                return true;
            }
        }
        return false;
    }

    private WifiP2pDevice getP2pDevice(String deviceAddress) {
        for (WifiP2pDevice d : mPeers.getDeviceList()) {
            if (d.deviceAddress.equals(deviceAddress)) {
                return d;
            }
        }
        return null;
    }

    /**
     * Synchronize the persistent group list between
     * wpa_supplicant and mGroups.
     */
    private void updatePersistentNetworks() {
        String listStr = mWifiNative.listNetworks();

        int sz = mGroups.size();
        mGroups.clear();
        String[] lines = listStr.split("\n");
        // Skip the first line, which is a header
       for (int i = 1; i < lines.length; i++) {
           String[] result = lines[i].split("\t");
           if (result == null || result.length < 4) {
               continue;
           }
           // network-id | ssid | bssid | flags
           int netId = -1;
           String ssid = result[1];
           String bssid = result[2];
           String flags = result[3];
           try {
               netId = Integer.parseInt(result[0]);
           } catch(NumberFormatException e) {
               e.printStackTrace();
               continue;
           }

           if (flags.indexOf("[CURRENT]") != -1) {
              continue;
           }
           if (flags.indexOf("[P2P-PERSISTENT]") == -1) {
               /*
                * The unused profile is sometimes remained when the p2p group formation is failed.
                * So, we clean up the p2p group here.
                */
               if (DBG) logd("clean up the unused persistent group. netId=" + netId);
               mWifiNative.removeNetwork(netId);
               continue;
           }

           WifiP2pGroup group = new WifiP2pGroup();
           group.setNetworkId(netId);
           group.setNetworkName(ssid);
           String mode = mWifiNative.getNetworkVariable(netId, "mode");
           if (mode != null && mode.equals("3")) {
               group.setIsGroupOwner(true);
           }
           if (bssid.equalsIgnoreCase(mThisDevice.deviceAddress)) {
               group.setOwner(mThisDevice);
           } else {
               WifiP2pDevice device = new WifiP2pDevice();
               device.deviceAddress = bssid;
               group.setOwner(device);
           }
           mGroups.add(group);
       }
       // Save the persistent profile
       // only if the group size is changed.
       if (mGroups.size() != sz) {
           mWifiNative.saveConfig();
       }
    }

    /**
     * Remove old credentials if the number of credentials are exceeded
     * the threshold value.
     */
    private void checkCredentialNum() {
        boolean removed = false;
        while (mGroups.size() > CREDENTIAL_MAX_NUM) {
            WifiP2pGroup group = mGroups.getGroupList().get(0);
            int netId = group.getNetworkId();
            mGroups.remove(netId);
            mWifiNative.removeNetwork(netId);
            removed = true;
        }

        if (removed) {
            mWifiNative.saveConfig();
        }
    }

    /**
     * Try to connect to the target device.
     *
     * Use the persistent credential if it has been stored.
     *
     * @param config
     * @param noInvitation if true, not use invitation.
     * @return
     */
    private int connect(WifiP2pConfig config, boolean noInvitation) {

        if (config == null) {
            loge("invalid argument.");
            return FAILED;
        }

        boolean isResp = (mSavedPeerConfig != null && config.deviceAddress.equals(
                mSavedPeerConfig.deviceAddress));
        mSavedPeerConfig = config;

        WifiP2pDevice dev = getP2pDevice(config.deviceAddress);
        if (dev == null) {
            loge("target device is not found.");
            return FAILED;
        }

        boolean join = dev.isGroupOwner();
        String ssid = mWifiNative.p2pGetSsid(dev.deviceAddress);
        if (DBG) logd("target ssid is " + ssid + " join:" + join);

        if (join && dev.isGroupLimit()) {
            if (DBG) logd("target device reaches group limit.");

            // if the target device doesn't reach the limit.
            // try group formation.
            join = false;
        } else if (join) {
            int netId = mGroups.getNetworkId(dev.deviceAddress, ssid);
            if (netId >= 0) {
                // Skip WPS and start 4way handshake immediately.
                if (!mWifiNative.p2pGroupAdd(netId)) {
                    return FAILED;
                }
                return SUCCESS;
            }
        }

        if (!join && dev.isDeviceLimit()) {
            loge("target device reaches the device limit.");
            return FAILED;
        }

        if (!join && !noInvitation && dev.isInvitationCapable()) {
            int netId = WifiP2pGroup.PERSISTNET_NET_ID;
            if (config.netId >= 0) {
                if (config.deviceAddress.equals(mGroups.getOwnerAddr(config.netId))) {
                    netId = config.netId;
                }
            } else {
                netId = mGroups.getNetworkId(dev.deviceAddress);
            }
            if (netId < 0) {
                netId = getNetworkIdFromClientList(dev.deviceAddress);
            }
            if (DBG) logd("netId related with " + dev.deviceAddress + " = " + netId);
            if (netId >= 0) {

                // Invoke the persistent group.
                if (!mWifiNative.p2pReinvoke(netId, dev.deviceAddress)) {
                    loge("p2pReinvoke() failed");
                    return FAILED;
                }
                // Save network id. It'll be used when an invitation result event is received.
                mSavedPeerConfig.netId = netId;
                return SUCCESS;
            }
        }
        checkCredentialNum();

        //Stop discovery before issuing connect
        mWifiNative.p2pStopFind();

        if (!isResp) {
            return NEEDS_PROVISION_REQ;
        }

        p2pConnectWithPinDisplay(config, join);
        return SUCCESS;
    }

    /**
     * Return the network id of the group owner profile which has the p2p client with
     * the specified device address in it's client list.
     * If more than one persistent group of the same address is present in its client
     * lists, return the first one.
     *
     * @param deviceAddress p2p device address.
     * @return the network id. if not found, return -1.
     */
    private int getNetworkIdFromClientList(String deviceAddress) {
        if (deviceAddress == null) return -1;

        Collection<WifiP2pGroup> groups = mGroups.getGroupList();
        for (WifiP2pGroup group:groups) {
            int netId = group.getNetworkId();
            String[] p2pClientList = getClientList(netId);
            if (p2pClientList == null) continue;
            for (String client:p2pClientList) {
                if (deviceAddress.equalsIgnoreCase(client)) {
                    return netId;
                }
            }
        }
return -1;
}

    /**
     * Return p2p client list associated with the specified network id.
     * @param netId network id.
     * @return p2p client list. if not found, return null.
     */
    private String[] getClientList(int netId) {
        String p2pClients = mWifiNative.getNetworkVariable(netId, "p2p_client_list");
        if (p2pClients == null) {
            return null;
        }
        return p2pClients.split(" ");
    }

    /**
     * Remove the specified p2p client from the specified profile.
     * @param netId network id of the profile.
     * @param addr p2p client address to be removed.
     * @param isRemovable if true, remove the specified profile if its client list becomes empty.
     * @return whether removing the specified p2p client is successful or not.
     */
    private boolean removeClientFromList(int netId, String addr, boolean isRemovable) {
        StringBuilder modifiedClientList =  new StringBuilder();
        String[] currentClientList = getClientList(netId);
        boolean isClientRemoved = false;
        if (currentClientList != null) {
            for (String client:currentClientList) {
                if (!client.equalsIgnoreCase(addr)) {
                    modifiedClientList.append(" ");
                    modifiedClientList.append(client);
                } else {
                    isClientRemoved = true;
                }
            }
        }
        if (modifiedClientList.length() == 0 && isRemovable) {
            // the client list is empty. so remove it.
            if (DBG) logd("Remove unknown network");
            mGroups.remove(netId);
            mWifiNative.removeNetwork(netId);
            mWifiNative.saveConfig();
            return true;
        }

        if (!isClientRemoved) {
            // specified p2p client is not found. already removed.
            return false;
        }

        if (DBG) logd("Modified client list: " + modifiedClientList);
        mWifiNative.setNetworkVariable(netId,
                "p2p_client_list", modifiedClientList.toString());
        mWifiNative.saveConfig();
        return true;
    }

private void setWifiP2pInfoOnGroupFormation(String serverAddress) {
mWifiP2pInfo.groupFormed = true;
mWifiP2pInfo.isGroupOwner = mGroup.isGroupOwner();
//Synthetic comment -- @@ -1610,6 +2045,8 @@
mWifiNative.p2pServiceFlush();
mServiceTransactionId = 0;
mServiceDiscReqId = null;

        updatePersistentNetworks();
}

private void updateThisDevice(int status) {







