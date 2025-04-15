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
import android.net.wifi.p2p.WifiP2pProvDiscEvent;
import android.net.wifi.p2p.nsd.WifiP2pServiceResponse;
import android.net.wifi.StateChangeResult;
//Synthetic comment -- @@ -186,7 +188,7 @@

/* P2P-GROUP-STARTED p2p-wlan0-0 [client|GO] ssid="DIRECT-W8" freq=2437
[psk=2182b2e50e53f260d04f3c7b25ef33c965a3291b9b36b455a82d77fd82ca15bc|passphrase="fKG4jMe3"]
       go_dev_addr=fa:7b:7a:42:02:13 */
private static final String P2P_GROUP_STARTED_STR = "P2P-GROUP-STARTED";

/* P2P-GROUP-REMOVED p2p-wlan0-0 [client|GO] reason=REQUESTED */
//Synthetic comment -- @@ -594,7 +596,13 @@
if (tokens.length != 2) return;
String[] nameValue = tokens[1].split("=");
if (nameValue.length != 2) return;
                mStateMachine.sendMessage(P2P_INVITATION_RESULT_EVENT, nameValue[1]);
} else if (dataString.startsWith(P2P_PROV_DISC_PBC_REQ_STR)) {
mStateMachine.sendMessage(P2P_PROV_DISC_PBC_REQ_EVENT,
new WifiP2pProvDiscEvent(dataString));








//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiNative.java b/wifi/java/android/net/wifi/WifiNative.java
//Synthetic comment -- index 4bf1ca3..3c482f4 100644

//Synthetic comment -- @@ -547,10 +547,9 @@
break;
}

        //TODO: Add persist behavior once the supplicant interaction is fixed for both
        // group and client scenarios
        /* Persist unless there is an explicit request to not do so*/
        //if (config.persist != WifiP2pConfig.Persist.NO) args.add("persistent");

if (joinExistingGroup) {
args.add("join");
//Synthetic comment -- @@ -592,10 +591,17 @@
return false;
}

    public boolean p2pGroupAdd() {
return doBooleanCommand("P2P_GROUP_ADD");
}

public boolean p2pGroupRemove(String iface) {
if (TextUtils.isEmpty(iface)) return false;
return doBooleanCommand("P2P_GROUP_REMOVE " + iface);
//Synthetic comment -- @@ -624,6 +630,14 @@
return doBooleanCommand("P2P_INVITE persistent=" + netId + " peer=" + deviceAddress);
}


public String p2pGetDeviceAddress() {
String status = status();
//Synthetic comment -- @@ -665,6 +679,24 @@
return doStringCommand("P2P_PEER " + deviceAddress);
}

public boolean p2pServiceAdd(WifiP2pServiceInfo servInfo) {
/*
* P2P_SERVICE_ADD bonjour <query hexdump> <RDATA hexdump>








//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/p2p/WifiP2pConfig.java b/wifi/java/android/net/wifi/p2p/WifiP2pConfig.java
//Synthetic comment -- index 6aea090..a2ed29f 100644

//Synthetic comment -- @@ -46,18 +46,8 @@
*/
public int groupOwnerIntent = -1;

    /**
     * Indicates whether the configuration is saved
     * @hide
     */
    public enum Persist {
        SYSTEM_DEFAULT,
        YES,
        NO
    }

/** @hide */
    public Persist persist = Persist.SYSTEM_DEFAULT;

public WifiP2pConfig() {
//set defaults
//Synthetic comment -- @@ -110,7 +100,7 @@
sbuf.append("\n address: ").append(deviceAddress);
sbuf.append("\n wps: ").append(wps);
sbuf.append("\n groupOwnerIntent: ").append(groupOwnerIntent);
        sbuf.append("\n persist: ").append(persist.toString());
return sbuf.toString();
}

//Synthetic comment -- @@ -125,7 +115,7 @@
deviceAddress = source.deviceAddress;
wps = new WpsInfo(source.wps);
groupOwnerIntent = source.groupOwnerIntent;
            persist = source.persist;
}
}

//Synthetic comment -- @@ -134,7 +124,7 @@
dest.writeString(deviceAddress);
dest.writeParcelable(wps, flags);
dest.writeInt(groupOwnerIntent);
        dest.writeString(persist.name());
}

/** Implement the Parcelable interface */
//Synthetic comment -- @@ -145,7 +135,7 @@
config.deviceAddress = in.readString();
config.wps = (WpsInfo) in.readParcelable(null);
config.groupOwnerIntent = in.readInt();
                config.persist = Persist.valueOf(in.readString());
return config;
}









//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/p2p/WifiP2pDevice.java b/wifi/java/android/net/wifi/p2p/WifiP2pDevice.java
//Synthetic comment -- index afdc9be..c86ec8b 100644

//Synthetic comment -- @@ -231,11 +231,26 @@
return (deviceCapability & DEVICE_CAPAB_SERVICE_DISCOVERY) != 0;
}

/** Returns true if the device is a group owner */
public boolean isGroupOwner() {
return (groupCapability & GROUP_CAPAB_GROUP_OWNER) != 0;
}

@Override
public boolean equals(Object obj) {
if (this == obj) return true;








//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/p2p/WifiP2pGroup.java b/wifi/java/android/net/wifi/p2p/WifiP2pGroup.java
//Synthetic comment -- index c30cc73..7f065aa 100644

//Synthetic comment -- @@ -33,6 +33,16 @@
*/
public class WifiP2pGroup implements Parcelable {

/** The network name */
private String mNetworkName;

//Synthetic comment -- @@ -50,13 +60,17 @@

private String mInterface;

/** P2P group started string pattern */
private static final Pattern groupStartedPattern = Pattern.compile(
"ssid=\"(.+)\" " +
"freq=(\\d+) " +
"(?:psk=)?([0-9a-fA-F]{64})?" +
"(?:passphrase=)?(?:\"(.{8,63})\")? " +
        "go_dev_addr=((?:[0-9a-f]{2}:){5}[0-9a-f]{2})"
);

public WifiP2pGroup() {
//Synthetic comment -- @@ -67,13 +81,15 @@
*
*  P2P-GROUP-STARTED p2p-wlan0-0 [client|GO] ssid="DIRECT-W8" freq=2437
*  [psk=2182b2e50e53f260d04f3c7b25ef33c965a3291b9b36b455a82d77fd82ca15bc|
     *  passphrase="fKG4jMe3"] go_dev_addr=fa:7b:7a:42:02:13
*
*  P2P-GROUP-REMOVED p2p-wlan0-0 [client|GO] reason=REQUESTED
*
*  P2P-INVITATION-RECEIVED sa=fa:7b:7a:42:02:13 go_dev_addr=f8:7b:7a:42:02:13
*  bssid=fa:7b:7a:42:82:13 unknown-network
*
*  Note: The events formats can be looked up in the wpa_supplicant code
*  @hide
*/
//Synthetic comment -- @@ -100,16 +116,36 @@
//String psk = match.group(3);
mPassphrase = match.group(4);
mOwner = new WifiP2pDevice(match.group(5));

} else if (tokens[0].equals("P2P-INVITATION-RECEIVED")) {
for (String token : tokens) {
String[] nameValue = token.split("=");
if (nameValue.length != 2) continue;

if (nameValue[0].equals("go_dev_addr")) {
mOwner = new WifiP2pDevice(nameValue[1]);
continue;
}
}
} else {
throw new IllegalArgumentException("Malformed supplicant event");
//Synthetic comment -- @@ -212,6 +248,16 @@
return mInterface;
}

public String toString() {
StringBuffer sbuf = new StringBuffer();
sbuf.append("network: ").append(mNetworkName);
//Synthetic comment -- @@ -221,6 +267,7 @@
sbuf.append("\n Client: ").append(client);
}
sbuf.append("\n interface: ").append(mInterface);
return sbuf.toString();
}

//Synthetic comment -- @@ -238,6 +285,7 @@
for (WifiP2pDevice d : source.getClientList()) mClients.add(d);
mPassphrase = source.getPassphrase();
mInterface = source.getInterface();
}
}

//Synthetic comment -- @@ -252,6 +300,7 @@
}
dest.writeString(mPassphrase);
dest.writeString(mInterface);
}

/** Implement the Parcelable interface */
//Synthetic comment -- @@ -268,6 +317,7 @@
}
group.setPassphrase(in.readString());
group.setInterface(in.readString());
return group;
}









//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/p2p/WifiP2pGroupList.java b/wifi/java/android/net/wifi/p2p/WifiP2pGroupList.java
new file mode 100644
//Synthetic comment -- index 0000000..02f3a6d

//Synthetic comment -- @@ -0,0 +1,207 @@








//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/p2p/WifiP2pManager.java b/wifi/java/android/net/wifi/p2p/WifiP2pManager.java
//Synthetic comment -- index 2c25e9d..2d7f1fe 100644

//Synthetic comment -- @@ -990,12 +990,31 @@
*
* <p> Application can request for the group details with {@link #requestGroupInfo}.
*
* @param c is the channel created at {@link #initialize}
* @param listener for callbacks on success or failure. Can be null.
*/
public void createGroup(Channel c, ActionListener listener) {
checkChannel(c);
        c.mAsyncChannel.sendMessage(CREATE_GROUP, 0, c.putListener(listener));
}

/**








//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/p2p/WifiP2pService.java b/wifi/java/android/net/wifi/p2p/WifiP2pService.java
//Synthetic comment -- index 9e004d0..0ef26aa 100644

//Synthetic comment -- @@ -84,6 +84,8 @@
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

//Synthetic comment -- @@ -118,6 +120,10 @@
private static final Boolean JOIN_GROUP = true;
private static final Boolean FORM_GROUP = false;

/* Two minutes comes from the wpa_supplicant setting */
private static final int GROUP_CREATING_WAIT_TIME_MS = 120 * 1000;
private static int mGroupCreatingTimeoutIndex = 0;
//Synthetic comment -- @@ -150,6 +156,8 @@
/* User rejected a peer request */
private static final int PEER_CONNECTION_USER_REJECT    =   BASE + 3;

private final boolean mP2pSupported;

private WifiP2pDevice mThisDevice = new WifiP2pDevice();
//Synthetic comment -- @@ -191,6 +199,84 @@
private static final String[] DHCP_RANGE = {"192.168.49.2", "192.168.49.254"};
private static final String SERVER_ADDRESS = "192.168.49.1";

public WifiP2pService(Context context) {
mContext = context;

//Synthetic comment -- @@ -273,6 +359,7 @@
private WifiMonitor mWifiMonitor = new WifiMonitor(this, mWifiNative);

private WifiP2pDeviceList mPeers = new WifiP2pDeviceList();
private WifiP2pInfo mWifiP2pInfo = new WifiP2pInfo();
private WifiP2pGroup mGroup;

//Synthetic comment -- @@ -626,6 +713,8 @@
break;
case WifiStateMachine.CMD_DISABLE_P2P:
if (mPeers.clear()) sendP2pPeersChangedBroadcast();
mWifiNative.closeSupplicantConnection();
transitionTo(mP2pDisablingState);
break;
//Synthetic comment -- @@ -768,47 +857,24 @@
/* Update group capability before connect */
int gc = mWifiNative.getGroupCapability(config.deviceAddress);
mPeers.updateGroupCapability(config.deviceAddress, gc);

                    if (mSavedPeerConfig != null && config.deviceAddress.equals(
                                    mSavedPeerConfig.deviceAddress)) {
mSavedPeerConfig = config;

                        //Stop discovery before issuing connect
                        mWifiNative.p2pStopFind();
                        if (mPeers.isGroupOwner(mSavedPeerConfig.deviceAddress)) {
                            p2pConnectWithPinDisplay(mSavedPeerConfig, JOIN_GROUP);
                        } else {
                            p2pConnectWithPinDisplay(mSavedPeerConfig, FORM_GROUP);
                        }
                        transitionTo(mGroupNegotiationState);
                    } else {
                        mSavedPeerConfig = config;
                        int netId = configuredNetworkId(mSavedPeerConfig.deviceAddress);
                        if (netId >= 0) {
                            //TODO: if failure, remove config and do a regular p2pConnect()
                            mWifiNative.p2pReinvoke(netId, mSavedPeerConfig.deviceAddress);
                        } else {
                            //Stop discovery before issuing connect
                            mWifiNative.p2pStopFind();
                            //If peer is a GO, we do not need to send provisional discovery,
                            //the supplicant takes care of it.
                            if (mPeers.isGroupOwner(mSavedPeerConfig.deviceAddress)) {
                                if (DBG) logd("Sending join to GO");
                                p2pConnectWithPinDisplay(mSavedPeerConfig, JOIN_GROUP);
                                transitionTo(mGroupNegotiationState);
                            } else {
                                if (DBG) logd("Sending prov disc");
                                transitionTo(mProvisionDiscoveryState);
                            }
                        }
}
mPeers.updateStatus(mSavedPeerConfig.deviceAddress, WifiP2pDevice.INVITED);
sendP2pPeersChangedBroadcast();
replyToMessage(message, WifiP2pManager.CONNECT_SUCCEEDED);
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
                   if (mWifiNative.p2pGroupAdd()) {
replyToMessage(message, WifiP2pManager.CREATE_GROUP_SUCCEEDED);
} else {
replyToMessage(message, WifiP2pManager.CREATE_GROUP_FAILED,
WifiP2pManager.ERROR);
}
transitionTo(mGroupNegotiationState);
break;
default:
return NOT_HANDLED;
//Synthetic comment -- @@ -941,11 +1036,8 @@
@Override
public void enter() {
if (DBG) logd(getName());
            if (!sendConnectNoticeToApp(mPeers.get(mSavedPeerConfig.deviceAddress),
                    mSavedPeerConfig)) {
notifyInvitationReceived();
}
        }

@Override
public boolean processMessage(Message message) {
//Synthetic comment -- @@ -953,11 +1045,10 @@
boolean ret = HANDLED;
switch (message.what) {
case PEER_CONNECTION_USER_ACCEPT:
                    //TODO: handle persistence
                    if (mJoinExistingGroup) {
                        p2pConnectWithPinDisplay(mSavedPeerConfig, JOIN_GROUP);
                    } else {
                        p2pConnectWithPinDisplay(mSavedPeerConfig, FORM_GROUP);
}
mPeers.updateStatus(mSavedPeerConfig.deviceAddress, WifiP2pDevice.INVITED);
sendP2pPeersChangedBroadcast();
//Synthetic comment -- @@ -1017,9 +1108,12 @@
transitionTo(mGroupNegotiationState);
} else {
mJoinExistingGroup = false;
transitionTo(mUserAuthorizingInvitationState);
}
}
break;
case WifiMonitor.P2P_PROV_DISC_SHOW_PIN_EVENT:
provDisc = (WifiP2pProvDiscEvent) message.obj;
//Synthetic comment -- @@ -1062,6 +1156,17 @@
case WifiMonitor.P2P_GROUP_STARTED_EVENT:
mGroup = (WifiP2pGroup) message.obj;
if (DBG) logd(getName() + " group started");
if (mGroup.isGroupOwner()) {
startDhcpServer(mGroup.getInterface());
} else {
//Synthetic comment -- @@ -1090,6 +1195,29 @@
// failure causes supplicant issues. Ignore right now.
case WifiMonitor.P2P_GROUP_FORMATION_FAILURE_EVENT:
break;
default:
return NOT_HANDLED;
}
//Synthetic comment -- @@ -1263,6 +1391,28 @@
}
// TODO: figure out updating the status to declined when invitation is rejected
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

    //TODO: implement when wpa_supplicant is fixed
    private int configuredNetworkId(String deviceAddress) {
return -1;
}

private void setWifiP2pInfoOnGroupFormation(String serverAddress) {
mWifiP2pInfo.groupFormed = true;
mWifiP2pInfo.isGroupOwner = mGroup.isGroupOwner();
//Synthetic comment -- @@ -1610,6 +2045,8 @@
mWifiNative.p2pServiceFlush();
mServiceTransactionId = 0;
mServiceDiscReqId = null;
}

private void updateThisDevice(int status) {







