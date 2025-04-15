/*Add support for custom HTTP ports besides 80/443 defaults

Make use of the host port given by the client when making requests to
EasSyncService. A patch for the client application (Email) has been sent
for review, see change with idI080c5e81d60c57010804eb640cb13d0d3ce7026b.

Change-Id:I6ae987e36c366d0f3b1526726b2162af0ed59564Signed-off-by: Mihai Serban <mihai.serban@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/exchange/EasSyncService.java b/src/com/android/exchange/EasSyncService.java
//Synthetic comment -- index 148a348..f087c1f 100644

//Synthetic comment -- @@ -205,6 +205,7 @@
@VisibleForTesting
String mBaseUriString = null;
public String mHostAddress;
    public int mPort = HostAuth.PORT_UNKNOWN;
public String mUserName;
public String mPassword;

//Synthetic comment -- @@ -439,6 +440,7 @@
svc.mProtocolVersionDouble = Eas.getProtocolVersionDouble(protocolVersion);
svc.mContext = context;
svc.mHostAddress = ha.mAddress;
        svc.mPort = ha.mPort;
svc.mUserName = ha.mLogin;
svc.mPassword = ha.mPassword;
try {
//Synthetic comment -- @@ -469,9 +471,21 @@
try {
loc = locHeader.getValue();
// Reset our host address and uncache our base uri
                Uri newUri = Uri.parse(loc);
                mHostAddress = newUri.getHost();
                mPort = newUri.getPort();
                if (mPort == -1) {
                    // Get it from scheme
                    String scheme = newUri.getScheme();
                    if (scheme != null) {
                        mPort = scheme.equalsIgnoreCase("http") ? 80 : 443;
                    } else {
                        mPort = HostAuth.PORT_UNKNOWN;
                    }
                }
mBaseUriString = null;
hostAuth.mAddress = mHostAddress;
                hostAuth.mPort = mPort;
userLog("Redirecting to: " + loc);
return true;
} catch (RuntimeException e) {
//Synthetic comment -- @@ -493,6 +507,7 @@
", ssl = ", hostAuth.shouldUseSsl() ? "1" : "0");
mContext = context;
mHostAddress = hostAuth.mAddress;
            mPort = hostAuth.mPort;
mUserName = hostAuth.mLogin;
mPassword = hostAuth.mPassword;

//Synthetic comment -- @@ -1220,7 +1235,12 @@
"&DeviceType=" + DEVICE_TYPE;
String scheme =
EmailClientConnectionManager.makeScheme(mSsl, mTrustSsl, mClientCertAlias);
            mBaseUriString = scheme + "://" + mHostAddress;
            if ((mSsl && mPort == 443) || (!mSsl && mPort == 80)) {
                mBaseUriString += "/Microsoft-Server-ActiveSync";
            } else {
                mBaseUriString += ":" + mPort + "/Microsoft-Server-ActiveSync";
            }
}
}

//Synthetic comment -- @@ -2513,6 +2533,7 @@
HostAuth ha = HostAuth.restoreHostAuthWithId(mContext, mAccount.mHostAuthKeyRecv);
if (ha == null) return false;
mHostAddress = ha.mAddress;
        mPort = ha.mPort;
mUserName = ha.mLogin;
mPassword = ha.mPassword;








