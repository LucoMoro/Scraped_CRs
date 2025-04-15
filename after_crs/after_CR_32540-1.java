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

    private String determineLocalIp(SipProfile localProfile) {
        try {
            DatagramSocket s = new DatagramSocket();
            s.connect(InetAddress.getByName(localProfile.getSipDomain()), 80);
            return s.getLocalAddress().getHostAddress();
        } catch (IOException e) {
            if (DEBUG) Log.d(TAG, "determineLocalIp()", e);
            // dont do anything; there should be a connectivity change going
            return null;
        }
    }

private SipSessionGroupExt createGroup(SipProfile localProfile)
throws SipException {
String key = localProfile.getUriString();
//Synthetic comment -- @@ -361,7 +373,7 @@
SipProfile localProfile, int maxInterval) {
if ((mIntervalMeasurementProcess == null)
&& (mKeepAliveInterval == -1)
                && isBehindNAT(mLocalIp, localProfile)) {
Log.d(TAG, "start NAT port mapping timeout measurement on "
+ localProfile.getUriString());

//Synthetic comment -- @@ -434,7 +446,7 @@
: mKeepAliveInterval;
}

    private boolean isBehindNAT(String address, SipProfile localProfile) {
try {
byte[] d = InetAddress.getByName(address).getAddress();
if ((d[0] == 10) ||
//Synthetic comment -- @@ -442,6 +454,18 @@
((0x000000F0 & ((int)d[1])) == 16)) ||
(((0x000000FF & ((int)d[0])) == 192) &&
((0x000000FF & ((int)d[1])) == 168))) {
                byte[] e = InetAddress.getByName(localProfile.getSipDomain()).getAddress();
                if ((e[0] == 10) ||
                        (((0x000000FF & ((int)e[0])) == 172) &&
                        ((0x000000F0 & ((int)e[1])) == 16)) ||
                        (((0x000000FF & ((int)e[0])) == 192) &&
                        ((0x000000FF & ((int)e[1])) == 168))) {
                        if (DEBUG)Log.d(TAG, "isBehindAT()" + address 
                                + " is false because client and server are private");
                    return false;
                }
                if (DEBUG) Log.d(TAG, "isBehindAT()" + address 
                        + " is true because client is private and server is public");
return true;
}
} catch (UnknownHostException e) {
//Synthetic comment -- @@ -1045,7 +1069,8 @@
restart(duration);

SipProfile localProfile = mSession.getLocalProfile();

                        if ((mKeepAliveSession == null) && (isBehindNAT(determineLocalIp(localProfile), localProfile)
|| localProfile.getSendKeepAlive())) {
startKeepAliveProcess(getKeepAliveInterval());
}







