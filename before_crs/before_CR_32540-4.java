/*fix NAT detection in SipService

The NAT detection in SipService just checks if the client IP is a
private address. In this case keep alive is enforced. But if you
use a server which also has a private address, keep alive should not
be enabled to save battery.
This patch modifies the NAT detection to also check the server IP.
If both are private, NAT is not detected.

Change-Id:I4b7b420d76fe59851550a26c0e0298266de84e36Signed-off-by: Andre Valentin <baddream66@googlemail.com>*/
//Synthetic comment -- diff --git a/voip/java/com/android/server/sip/SipService.java b/voip/java/com/android/server/sip/SipService.java
//Synthetic comment -- index a477fd1..60944e5 100644

//Synthetic comment -- @@ -282,6 +282,18 @@
}
}

private SipSessionGroupExt createGroup(SipProfile localProfile)
throws SipException {
String key = localProfile.getUriString();
//Synthetic comment -- @@ -353,7 +365,7 @@
SipProfile localProfile, int maxInterval) {
if ((mIntervalMeasurementProcess == null)
&& (mKeepAliveInterval == -1)
                && isBehindNAT(mLocalIp)) {
Log.d(TAG, "start NAT port mapping timeout measurement on "
+ localProfile.getUriString());

//Synthetic comment -- @@ -426,7 +438,7 @@
: mKeepAliveInterval;
}

    private boolean isBehindNAT(String address) {
try {
byte[] d = InetAddress.getByName(address).getAddress();
if ((d[0] == 10) ||
//Synthetic comment -- @@ -437,7 +449,25 @@
return true;
}
} catch (UnknownHostException e) {
            Log.e(TAG, "isBehindAT()" + address, e);
}
return false;
}
//Synthetic comment -- @@ -999,7 +1029,9 @@
restart(duration);

SipProfile localProfile = mSession.getLocalProfile();
                        if ((mKeepAliveSession == null) && (isBehindNAT(mLocalIp)
|| localProfile.getSendKeepAlive())) {
startKeepAliveProcess(getKeepAliveInterval());
}







