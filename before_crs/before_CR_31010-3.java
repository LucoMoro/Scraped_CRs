/*SIP-API: bugfixes/enhancements for registration processing.

This patch set fixes problems for registration processing

  o AutoRegistrationProcess.setListener():
    --> Suppress calling mProxy.onRegistrationFailed() at false timing.

  o AutoRegistrationProcess.run():
    --> Once expiryTimer value has given from the registrar server,
        use it for sending further REGISTER messages.

  o Min-Expires header:
    --> According to RFC3271, Table 2, the header exists only in
        REGISTER response with error code 423 (Interval Too Brief).
        Given expiryTimer will be reflected to the next registraiton
        process.

  o SipRegistrationListener.onRegistrationDone():
    --> Argument expiryTime will be set propery so that it comforms
        to the API definition.
        For deregistration case, the value will be 0.

[NB] This is a revised version of the following patch:https://review.source.android.com/25642(cherry picked from commit b786cc0ba8efc842020b6f1a5e880914882c63ea)
Signed-off-by: Masahiko Endo <masahiko.endo@gmail.com>

Change-Id:I3d5db7125c118d49947565124ebf9f68ecc2492a*/
//Synthetic comment -- diff --git a/voip/java/android/net/sip/SipManager.java b/voip/java/android/net/sip/SipManager.java
//Synthetic comment -- index 74c3672..4c09b91 100644

//Synthetic comment -- @@ -604,7 +604,6 @@
@Override
public void onRegistrationDone(ISipSession session, int duration) {
long expiryTime = duration;
            if (duration > 0) expiryTime += System.currentTimeMillis();
mListener.onRegistrationDone(getUri(session), expiryTime);
}









//Synthetic comment -- diff --git a/voip/java/com/android/server/sip/SipService.java b/voip/java/com/android/server/sip/SipService.java
//Synthetic comment -- index 38a683e..2d119bf 100644

//Synthetic comment -- @@ -445,7 +445,7 @@
return true;
}
} catch (UnknownHostException e) {
            Log.e(TAG, "isBehindAT()" + address, e);
}
return false;
}
//Synthetic comment -- @@ -794,6 +794,7 @@
private int mBackoff = 1;
private boolean mRegistered;
private long mExpiryTime;
private int mErrorCode;
private String mErrorMessage;
private boolean mRunning = false;
//Synthetic comment -- @@ -811,7 +812,10 @@
mSession = (SipSessionGroup.SipSessionImpl)
group.createSession(this);
// return right away if no active network connection.
                if (mSession == null) return;

// start unregistration to clear up old registration at server
// TODO: when rfc5626 is deployed, use reg-id and sip.instance
//Synthetic comment -- @@ -902,8 +906,9 @@
public void stop() {
if (!mRunning) return;
mRunning = false;
            mMyWakeLock.release(mSession);
if (mSession != null) {
mSession.setListener(null);
if (mConnected && mRegistered) mSession.unregister();
}
//Synthetic comment -- @@ -930,6 +935,10 @@
synchronized (SipService.this) {
mProxy.setListener(listener);

try {
int state = (mSession == null)
? SipSession.State.READY_TO_CALL
//Synthetic comment -- @@ -981,8 +990,11 @@
mErrorMessage = null;
if (DEBUG) Log.d(TAG, "registering");
if (mConnected) {
mMyWakeLock.acquire(mSession);
                    mSession.register(EXPIRY_TIME);
}
}
}
//Synthetic comment -- @@ -1029,9 +1041,10 @@
synchronized (SipService.this) {
if (notCurrentSession(session)) return;

                mProxy.onRegistrationDone(session, duration);

if (duration > 0) {
mExpiryTime = SystemClock.elapsedRealtime()
+ (duration * 1000);

//Synthetic comment -- @@ -1051,9 +1064,18 @@
}
}
mMyWakeLock.release(session);
} else {
mRegistered = false;
mExpiryTime = -1L;
if (DEBUG) Log.d(TAG, "Refresh registration immediately");
run();
}








//Synthetic comment -- diff --git a/voip/java/com/android/server/sip/SipSessionGroup.java b/voip/java/com/android/server/sip/SipSessionGroup.java
//Synthetic comment -- index 877a0a4..a4bee36 100644

//Synthetic comment -- @@ -899,16 +899,34 @@
if (time <= 0) {
time = EXPIRY_TIME;
}
expires = (ExpiresHeader) response.getHeader(MinExpiresHeader.NAME);
if (expires != null && time < expires.getExpires()) {
time = expires.getExpires();
}
if (DEBUG) {
Log.v(TAG, "Expiry time = " + time);
}
return time;
}

private boolean registeringToReady(EventObject evt)
throws SipException {
if (expectResponse(Request.REGISTER, evt)) {
//Synthetic comment -- @@ -921,14 +939,17 @@
int state = mState;
onRegistrationDone((state == SipSession.State.REGISTERING)
? getExpiryTime(((ResponseEvent) evt).getResponse())
                            : -1);
return true;
case Response.UNAUTHORIZED:
case Response.PROXY_AUTHENTICATION_REQUIRED:
handleAuthentication(event);
return true;
default:
                    if (statusCode >= 500) {
onRegistrationFailed(response);
return true;
}
//Synthetic comment -- @@ -937,6 +958,26 @@
return false;
}

private boolean handleAuthentication(ResponseEvent event)
throws SipException {
Response response = event.getResponse();







