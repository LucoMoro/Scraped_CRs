/*Support split-tunneling in VPN services.

mRouteList member in VpnProfile contains comma-separated list of
subnets to be accessed through VPN in form of address/mask.
mExcludeRouteList contains comma-separated list of subnets excluded
from VPN in a format of "address/mask,address/mask". If mRouteList
and mExcludeRouteList are empty, then all traffic gets redirected
to VPN (default behaviour).

Change-Id:Id476c7eba86907b2ecc685b9beb6e8792f763673*/
//Synthetic comment -- diff --git a/packages/VpnServices/src/com/android/server/vpn/L2tpIpsecPskService.java b/packages/VpnServices/src/com/android/server/vpn/L2tpIpsecPskService.java
//Synthetic comment -- index 50e0de1..6b50085 100644

//Synthetic comment -- @@ -42,6 +42,6 @@
// L2TP
daemons.startL2tp(serverIp,
(p.isSecretEnabled() ? p.getSecretString() : null),
                username, password);
}
}








//Synthetic comment -- diff --git a/packages/VpnServices/src/com/android/server/vpn/L2tpIpsecService.java b/packages/VpnServices/src/com/android/server/vpn/L2tpIpsecService.java
//Synthetic comment -- index 663b0e8..fb17f10 100644

//Synthetic comment -- @@ -45,6 +45,6 @@
// L2TP
daemons.startL2tp(serverIp,
(p.isSecretEnabled() ? p.getSecretString() : null),
                username, password);
}
}








//Synthetic comment -- diff --git a/packages/VpnServices/src/com/android/server/vpn/L2tpService.java b/packages/VpnServices/src/com/android/server/vpn/L2tpService.java
//Synthetic comment -- index 784a366..285275e 100644

//Synthetic comment -- @@ -30,6 +30,6 @@
L2tpProfile p = getProfile();
getDaemons().startL2tp(serverIp,
(p.isSecretEnabled() ? p.getSecretString() : null),
                username, password);
}
}








//Synthetic comment -- diff --git a/packages/VpnServices/src/com/android/server/vpn/PptpService.java b/packages/VpnServices/src/com/android/server/vpn/PptpService.java
//Synthetic comment -- index de12710..ce625f5 100644

//Synthetic comment -- @@ -29,6 +29,6 @@
throws IOException {
PptpProfile p = getProfile();
getDaemons().startPptp(serverIp, username, password,
                p.isEncryptionEnabled());
}
}








//Synthetic comment -- diff --git a/packages/VpnServices/src/com/android/server/vpn/VpnDaemons.java b/packages/VpnServices/src/com/android/server/vpn/VpnDaemons.java
//Synthetic comment -- index 499195f..3d18ab93 100644

//Synthetic comment -- @@ -46,15 +46,17 @@
private List<DaemonProxy> mDaemonList = new ArrayList<DaemonProxy>();

public DaemonProxy startL2tp(String serverIp, String secret,
            String username, String password) throws IOException {
return startMtpd(L2TP, serverIp, L2TP_PORT, secret, username, password,
                false);
}

public DaemonProxy startPptp(String serverIp, String username,
            String password, boolean encryption) throws IOException {
return startMtpd(PPTP, serverIp, PPTP_PORT, null, username, password,
                encryption);
}

public DaemonProxy startIpsecForL2tp(String serverIp, String pskKey)
//Synthetic comment -- @@ -117,12 +119,14 @@

private DaemonProxy startMtpd(String protocol,
String serverIp, String port, String secret, String username,
            String password, boolean encryption) throws IOException {
ArrayList<String> args = new ArrayList<String>();
args.addAll(Arrays.asList(protocol, serverIp, port));
if (secret != null) args.add(secret);
args.add(PPP_ARGS_SEPARATOR);
        addPppArguments(args, serverIp, username, password, encryption);

DaemonProxy mtpd = startDaemon(MTPD);
mtpd.sendCommand(args.toArray(new String[args.size()]));
//Synthetic comment -- @@ -130,8 +134,8 @@
}

private static void addPppArguments(ArrayList<String> args, String serverIp,
            String username, String password, boolean encryption)
            throws IOException {
args.addAll(Arrays.asList(
"linkname", VPN_LINKNAME,
"name", username,
//Synthetic comment -- @@ -143,5 +147,13 @@
if (encryption) {
args.add("+mppe");
}
}
}








//Synthetic comment -- diff --git a/vpn/java/android/net/vpn/VpnProfile.java b/vpn/java/android/net/vpn/VpnProfile.java
//Synthetic comment -- index bd6c809..b3f463b 100644

//Synthetic comment -- @@ -33,7 +33,8 @@
private String mId; // unique identifier
private String mServerName; // VPN server name
private String mDomainSuffices; // space separated list
    private String mRouteList; // space separated list
private String mSavedUsername;
private boolean mIsCustomized;
private transient VpnState mState = VpnState.IDLE;
//Synthetic comment -- @@ -87,7 +88,7 @@
* Sets the routing info for this VPN connection.
*
* @param entries a comma-separated list of routes; each entry is in the
     *      format of "(network address)/(network mask)"
*/
public void setRouteList(String entries) {
mRouteList = entries;
//Synthetic comment -- @@ -97,6 +98,20 @@
return mRouteList;
}

public void setSavedUsername(String name) {
mSavedUsername = name;
}
//Synthetic comment -- @@ -140,6 +155,7 @@
mServerName = in.readString();
mDomainSuffices = in.readString();
mRouteList = in.readString();
mSavedUsername = in.readString();
}

//Synthetic comment -- @@ -168,6 +184,7 @@
parcel.writeString(mServerName);
parcel.writeString(mDomainSuffices);
parcel.writeString(mRouteList);
parcel.writeString(mSavedUsername);
}








