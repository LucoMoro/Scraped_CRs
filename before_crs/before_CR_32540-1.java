/*fix NAT detection in SipService

The NAT detection in SipService just checks if the client IP is a
private address. In this case keep alive is enforced. But if you
use a server which also a private address, keep alive should not
be enabled to save battery.
This patch modifies the NAT detection to also check the server IP.
If both are private, NAT is not detected.

Change-Id:I4b7b420d76fe59851550a26c0e0298266de84e36Signed-off-by: Andre Valentin <baddream66@googlemail.com>*/
//Synthetic comment -- diff --git a/voip/java/com/android/server/sip/SipService.java b/voip/java/com/android/server/sip/SipService.java
//Synthetic comment -- index 38a683e..b81b72b 100644

//Synthetic comment -- @@ -289,6 +289,18 @@
}
}

private SipSessionGroupExt createGroup(SipProfile localProfile)
throws SipException {
String key = localProfile.getUriString();
//Synthetic comment -- @@ -361,7 +373,7 @@
SipProfile localProfile, int maxInterval) {
if ((mIntervalMeasurementProcess == null)
&& (mKeepAliveInterval == -1)
                && isBehindNAT(mLocalIp)) {
Log.d(TAG, "start NAT port mapping timeout measurement on "
+ localProfile.getUriString());

//Synthetic comment -- @@ -434,7 +446,7 @@
: mKeepAliveInterval;
}

    private boolean isBehindNAT(String address) {
try {
byte[] d = InetAddress.getByName(address).getAddress();
if ((d[0] == 10) ||
//Synthetic comment -- @@ -442,6 +454,18 @@
((0x000000F0 & ((int)d[1])) == 16)) ||
(((0x000000FF & ((int)d[0])) == 192) &&
((0x000000FF & ((int)d[1])) == 168))) {
return true;
}
} catch (UnknownHostException e) {
//Synthetic comment -- @@ -1045,7 +1069,8 @@
restart(duration);

SipProfile localProfile = mSession.getLocalProfile();
                        if ((mKeepAliveSession == null) && (isBehindNAT(mLocalIp)
|| localProfile.getSendKeepAlive())) {
startKeepAliveProcess(getKeepAliveInterval());
}







