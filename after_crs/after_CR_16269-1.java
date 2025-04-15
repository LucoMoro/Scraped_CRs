/*Support for Exchange Server IP port added. Always shows the port in use in the settings screen

Change-Id:I5d052bb40f8317eb62b7c9f89bc9c00b1347fd80*/




//Synthetic comment -- diff --git a/src/com/android/email/activity/setup/AccountSetupExchange.java b/src/com/android/email/activity/setup/AccountSetupExchange.java
//Synthetic comment -- index cb4b590..e1b668d 100644

//Synthetic comment -- @@ -289,6 +289,9 @@

if (hostAuth.mAddress != null) {
mServerView.setText(hostAuth.mAddress);
            
            if (hostAuth.mPort > -1)
            	mServerView.setText(hostAuth.mAddress + ":" + hostAuth.mPort);
}

boolean ssl = 0 != (hostAuth.mFlags & HostAuth.FLAG_SSL);
//Synthetic comment -- @@ -436,15 +439,19 @@
userName = userName.substring(1);
}
mCacheLoginCredential = userName;
        
String userInfo = userName + ":" + mPasswordView.getText().toString().trim();
        
        String[] server_parts = mServerView.getText().toString().trim().split(":");
        String host = server_parts[0];
        int port = server_parts.length > 1?Integer.parseInt(server_parts[1]):0;
String path = null;

URI uri = new URI(
scheme,
userInfo,
host,
                port,
path,
null,
null);








//Synthetic comment -- diff --git a/src/com/android/email/mail/store/ExchangeStore.java b/src/com/android/email/mail/store/ExchangeStore.java
//Synthetic comment -- index 9935d50..e4e1e22 100644

//Synthetic comment -- @@ -165,6 +165,7 @@
private final Context mContext;

private String mHost;
        private int mPort;
private String mDomain;
private String mUsername;
private String mPassword;
//Synthetic comment -- @@ -209,6 +210,8 @@
if (mHost == null) {
throw new MessagingException("host not specified");
}
            
            mPort = uri.getPort();

mDomain = uri.getPath();
if (!TextUtils.isEmpty(mDomain)) {
//Synthetic comment -- @@ -237,9 +240,12 @@
boolean ssl = uri.getScheme().contains("+ssl");
boolean tssl = uri.getScheme().contains("+trustallcerts");
try {
                
                if (mPort == -1)
                	mPort = ssl ? 443 : 80;
                
int result = ExchangeUtils.getExchangeEmailService(mContext, null)
                    .validate("eas", mHost, mUsername, mPassword, mPort, ssl, tssl);
if (result != MessagingException.NO_ERROR) {
if (result == MessagingException.AUTHENTICATION_FAILED) {
throw new AuthenticationFailedException("Authentication failed.");








//Synthetic comment -- diff --git a/src/com/android/exchange/EasSyncService.java b/src/com/android/exchange/EasSyncService.java
//Synthetic comment -- index a1a76dc..d20fd3e 100644

//Synthetic comment -- @@ -185,6 +185,7 @@
/*package*/ String mAuthString = null;
private String mCmdString = null;
public String mHostAddress;
    public int mHostPort;
public String mUserName;
public String mPassword;
private boolean mSsl = true;
//Synthetic comment -- @@ -388,6 +389,7 @@
svc.mHostAddress = hostAddress;
svc.mUserName = userName;
svc.mPassword = password;
            svc.mHostPort = port;
svc.mSsl = ssl;
svc.mTrustSsl = trustCertificates;
// We mustn't use the "real" device id or we'll screw up current accounts
//Synthetic comment -- @@ -788,6 +790,7 @@
try {
svc.mContext = context;
svc.mHostAddress = ha.mAddress;
                svc.mHostPort = ha.mPort;
svc.mUserName = ha.mLogin;
svc.mPassword = ha.mPassword;
svc.mSsl = (ha.mFlags & HostAuth.FLAG_SSL) != 0;
//Synthetic comment -- @@ -1090,7 +1093,7 @@
if (mAuthString == null || mCmdString == null) {
cacheAuthAndCmdString();
}
        String us = (mSsl ? (mTrustSsl ? "httpts" : "https") : "http") + "://" + mHostAddress + ":" + mHostPort +
"/Microsoft-Server-ActiveSync";
if (cmd != null) {
us += "?Cmd=" + cmd + mCmdString;
//Synthetic comment -- @@ -1098,6 +1101,8 @@
if (extra != null) {
us += extra;
}
        
        Log.d("EasSyncService.makeUriString",us);
return us;
}

//Synthetic comment -- @@ -2134,6 +2139,7 @@
HostAuth ha = HostAuth.restoreHostAuthWithId(mContext, mAccount.mHostAuthKeyRecv);
if (ha == null) return false;
mHostAddress = ha.mAddress;
        mHostPort = ha.mPort;
mUserName = ha.mLogin;
mPassword = ha.mPassword;








