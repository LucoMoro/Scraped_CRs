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
//Synthetic comment -- index cd0b5c4..de63d82 100644

//Synthetic comment -- @@ -597,7 +597,6 @@
@Override
public void onRegistrationDone(ISipSession session, int duration) {
long expiryTime = duration;
mListener.onRegistrationDone(getUri(session), expiryTime);
}









//Synthetic comment -- diff --git a/voip/java/com/android/server/sip/SipService.java b/voip/java/com/android/server/sip/SipService.java
//Synthetic comment -- index 38a683e..2d119bf 100644

//Synthetic comment -- @@ -445,7 +445,7 @@
return true;
}
} catch (UnknownHostException e) {
            Log.e(TAG, "isBehindNAT()" + address, e);
}
return false;
}
//Synthetic comment -- @@ -794,6 +794,7 @@
private int mBackoff = 1;
private boolean mRegistered;
private long mExpiryTime;
        private int mDesignatedDuration;
private int mErrorCode;
private String mErrorMessage;
private boolean mRunning = false;
//Synthetic comment -- @@ -811,7 +812,10 @@
mSession = (SipSessionGroup.SipSessionImpl)
group.createSession(this);
// return right away if no active network connection.
                if (mSession == null) {
                    mRunning = false; /* reset status */
                    return;
                }

// start unregistration to clear up old registration at server
// TODO: when rfc5626 is deployed, use reg-id and sip.instance
//Synthetic comment -- @@ -902,8 +906,9 @@
public void stop() {
if (!mRunning) return;
mRunning = false;

if (mSession != null) {
                mMyWakeLock.release(mSession);
mSession.setListener(null);
if (mConnected && mRegistered) mSession.unregister();
}
//Synthetic comment -- @@ -930,6 +935,10 @@
synchronized (SipService.this) {
mProxy.setListener(listener);

                if (!mRunning) {
                    /* Suppress calling callback functions at false timing */
                    return;
                }
try {
int state = (mSession == null)
? SipSession.State.READY_TO_CALL
//Synthetic comment -- @@ -981,8 +990,11 @@
mErrorMessage = null;
if (DEBUG) Log.d(TAG, "registering");
if (mConnected) {
                    int duration =
                        ((mDesignatedDuration > 0) ?
                            mDesignatedDuration : EXPIRY_TIME);
mMyWakeLock.acquire(mSession);
                    mSession.register(duration);
}
}
}
//Synthetic comment -- @@ -1029,9 +1041,10 @@
synchronized (SipService.this) {
if (notCurrentSession(session)) return;

if (duration > 0) {
                    mProxy.onRegistrationDone(session, duration);
                    mDesignatedDuration = duration;

mExpiryTime = SystemClock.elapsedRealtime()
+ (duration * 1000);

//Synthetic comment -- @@ -1051,9 +1064,18 @@
}
}
mMyWakeLock.release(session);
                } else if (duration < 0) {
                    /* "423 Interval Too Brief" case */
                    mDesignatedDuration = -duration;
                    restartLater();
} else {
                    /* DEREGISTER case */
mRegistered = false;
mExpiryTime = -1L;

                    mProxy.onRegistrationDone(session, 0);
                    mDesignatedDuration = 0;

if (DEBUG) Log.d(TAG, "Refresh registration immediately");
run();
}








//Synthetic comment -- diff --git a/voip/java/com/android/server/sip/SipSessionGroup.java b/voip/java/com/android/server/sip/SipSessionGroup.java
//Synthetic comment -- index 877a0a4..a4bee36 100644

//Synthetic comment -- @@ -899,16 +899,34 @@
if (time <= 0) {
time = EXPIRY_TIME;
}

            /*
             * Min-Expires header exists only in REGISTER response
             * with error code 423 (Interval Too Brief).
             * [c.f.] RFC3271, Table 2
             *
expires = (ExpiresHeader) response.getHeader(MinExpiresHeader.NAME);
if (expires != null && time < expires.getExpires()) {
time = expires.getExpires();
}
             *//* Don't use the Min-Expires header here */

if (DEBUG) {
Log.v(TAG, "Expiry time = " + time);
}
return time;
}

        private int getMinExpiryTime(Response response) {
            int expires = -1;
            ExpiresHeader expiresHeader = (ExpiresHeader)
                    response.getHeader(MinExpiresHeader.NAME);
            if (expiresHeader != null) {
                expires = expiresHeader.getExpires();
            }
            return expires;
        }

private boolean registeringToReady(EventObject evt)
throws SipException {
if (expectResponse(Request.REGISTER, evt)) {
//Synthetic comment -- @@ -921,14 +939,17 @@
int state = mState;
onRegistrationDone((state == SipSession.State.REGISTERING)
? getExpiryTime(((ResponseEvent) evt).getResponse())
                            : 0);
return true;
case Response.UNAUTHORIZED:
case Response.PROXY_AUTHENTICATION_REQUIRED:
handleAuthentication(event);
return true;
                case Response.INTERVAL_TOO_BRIEF:
                    handleIntervalTooBrief(event);
                    return true;
default:
                    if (statusCode >= 400) {
onRegistrationFailed(response);
return true;
}
//Synthetic comment -- @@ -937,6 +958,26 @@
return false;
}

        private void handleIntervalTooBrief(ResponseEvent event) {
            int minExpire = getMinExpiryTime(event.getResponse());
            if (minExpire < 0) {
                onError(SipErrorCode.SERVER_ERROR,
                        "Missing Min-Expires header on 423 response");
            } else if (minExpire == 0) {
                onError(SipErrorCode.SERVER_ERROR,
                        "Invalid Min-Expires value on 423 response");
            } else {
                /*
                 * We should not simply call register(minExpire) here.
                 * Instead, call mProxy.onRegistrationDone() to pass
                 * designated minExpire value for the regsitration
                 * process, as well as taking pause to avoid flooding.
                 */
                onRegistrationDone(-minExpire);
            }
            return;
        }

private boolean handleAuthentication(ResponseEvent event)
throws SipException {
Response response = event.getResponse();







