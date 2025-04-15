/*VpnServices: supply authentication method parameter to racoon

This patch causes VpnServices to supply the new authentication
method parameter when invoking racoon.

Change-Id:I3eaf3ffe0276de6f191fab3021dcaf97bdde5b2b*/
//Synthetic comment -- diff --git a/packages/VpnServices/src/com/android/server/vpn/VpnDaemons.java b/packages/VpnServices/src/com/android/server/vpn/VpnDaemons.java
//Synthetic comment -- index 499195f..9bf33dd 100644

//Synthetic comment -- @@ -60,14 +60,14 @@
public DaemonProxy startIpsecForL2tp(String serverIp, String pskKey)
throws IOException {
DaemonProxy ipsec = startDaemon(IPSEC);
        ipsec.sendCommand(serverIp, L2TP_PORT, pskKey);
return ipsec;
}

public DaemonProxy startIpsecForL2tp(String serverIp, String userKeyKey,
String userCertKey, String caCertKey) throws IOException {
DaemonProxy ipsec = startDaemon(IPSEC);
        ipsec.sendCommand(serverIp, L2TP_PORT, userKeyKey, userCertKey,
caCertKey);
return ipsec;
}







