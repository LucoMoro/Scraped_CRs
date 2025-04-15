/*SIP-API: Add support for 3xx redirection.

If the UAC gets 3xx response for outgoing INVITE request, try
redirection cycle until the given contact list being exhausted,
or succeed to make a redirected call.

Change-Id:Ic9722cf646b8ba9e1694d552bc69ce6e73990577Signed-off-by: Masahiko Endo <masahiko.endo@gmail.com>*/
//Synthetic comment -- diff --git a/voip/java/com/android/server/sip/SipHelper.java b/voip/java/com/android/server/sip/SipHelper.java
//Synthetic comment -- index bd02508..95155c1 100644

//Synthetic comment -- @@ -31,12 +31,15 @@
import java.text.ParseException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogTerminatedEvent;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
//Synthetic comment -- @@ -48,6 +51,7 @@
import javax.sip.SipFactory;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.Transaction;
import javax.sip.TransactionAlreadyExistsException;
import javax.sip.TransactionDoesNotExistException;
//Synthetic comment -- @@ -58,6 +62,7 @@
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.AllowHeader;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
//Synthetic comment -- @@ -66,6 +71,7 @@
import javax.sip.header.Header;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ReasonHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.UserAgentHeader;
//Synthetic comment -- @@ -110,6 +116,11 @@
* Create various headers
*-----------------------------------------------------------------*/

private AllowHeader createAllowHeader()
throws ParseException {
String methods = "ACK,BYE,CANCEL,INVITE,OPTIONS,REGISTER,NOTIFY,REFER";
//Synthetic comment -- @@ -331,6 +342,114 @@
}
}

public ClientTransaction sendReinvite(Dialog dialog,
String sessionDescription) throws SipException {
try {








//Synthetic comment -- diff --git a/voip/java/com/android/server/sip/SipRedirection.java b/voip/java/com/android/server/sip/SipRedirection.java
new file mode 100644
//Synthetic comment -- index 0000000..4a51947

//Synthetic comment -- @@ -0,0 +1,594 @@








//Synthetic comment -- diff --git a/voip/java/com/android/server/sip/SipSessionGroup.java b/voip/java/com/android/server/sip/SipSessionGroup.java
//Synthetic comment -- index 5282175..35710a9 100644

//Synthetic comment -- @@ -42,6 +42,7 @@
import java.net.DatagramSocket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
//Synthetic comment -- @@ -65,6 +66,7 @@
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TimeoutEvent;
import javax.sip.Transaction;
import javax.sip.TransactionState;
//Synthetic comment -- @@ -77,6 +79,7 @@
import javax.sip.header.ContactHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderAddress;
import javax.sip.header.MinExpiresHeader;
import javax.sip.header.ReferToHeader;
//Synthetic comment -- @@ -549,6 +552,7 @@
boolean mInCall;
SessionTimer mSessionTimer;
int mAuthenticationRetryCount;

private KeepAliveProcess mKeepAliveProcess;

//Synthetic comment -- @@ -579,7 +583,24 @@

private void timeout() {
synchronized (SipSessionGroup.this) {
                    onError(SipErrorCode.TIME_OUT, "Session timed out!");
}
}

//Synthetic comment -- @@ -594,13 +615,18 @@

public SipSessionImpl(ISipSessionListener listener) {
setListener(listener);
}

SipSessionImpl duplicate() {
return new SipSessionImpl(mProxy.getListener());
}

        private void reset() {
mInCall = false;
removeSipSession(this);
mPeerProfile = null;
//Synthetic comment -- @@ -691,10 +717,25 @@

public void makeCall(SipProfile peerProfile, String sessionDescription,
int timeout) {
doCommandAsync(new MakeCallCommand(peerProfile, sessionDescription,
timeout));
}

public void answerCall(String sessionDescription, int timeout) {
synchronized (SipSessionGroup.this) {
if (mPeerProfile == null) return;
//Synthetic comment -- @@ -898,8 +939,14 @@
break;
case SipSession.State.INCOMING_CALL:
case SipSession.State.INCOMING_CALL_ANSWERING:
case SipSession.State.OUTGOING_CALL:
case SipSession.State.OUTGOING_CALL_CANCELING:
onError(SipErrorCode.TIME_OUT, event.toString());
break;

//Synthetic comment -- @@ -1091,13 +1138,27 @@
mState = SipSession.State.OUTGOING_CALL;
MakeCallCommand cmd = (MakeCallCommand) evt;
mPeerProfile = cmd.getPeerProfile();
                if (mReferSession != null) {
                    mSipHelper.sendReferNotify(mReferSession.mDialog,
                            getResponseString(Response.TRYING));
                }
                mClientTransaction = mSipHelper.sendInvite(
                        mLocalProfile, mPeerProfile, cmd.getSessionDescription(),
generateTag(), mReferredBy, mReplaces);
mDialog = mClientTransaction.getDialog();
addSipSession(this);
startSessionTimer(cmd.getTimeout());
//Synthetic comment -- @@ -1185,6 +1246,7 @@
mState = SipSession.State.OUTGOING_CALL_CANCELING;
mSipHelper.sendCancel(mClientTransaction, 0/*statusCode*/);
startSessionTimer(CANCEL_CALL_TIMER);
return true;
} else if (isRequestEvent(Request.INVITE, evt)) {
// Call self? Send BUSY HERE so server may redirect the call to
//Synthetic comment -- @@ -1192,6 +1254,7 @@
RequestEvent event = (RequestEvent) evt;
mSipHelper.sendInviteBusyHere(event,
event.getServerTransaction());
return true;
}
return false;
//Synthetic comment -- @@ -1370,7 +1433,7 @@
establishCall(true);
break;
case 3:
                // TODO: handle 3xx (redirect)
break;
default:
switch (statusCode) {
//Synthetic comment -- @@ -1387,6 +1450,10 @@
/* FALLTHROUGH *//* Treat as an error, for now */
default:
// error: an ack is sent automatically by the stack
if (mReferSession != null) {
mSipHelper.sendReferNotify(mReferSession.mDialog,
getResponseString(Response.SERVICE_UNAVAILABLE));
//Synthetic comment -- @@ -1457,7 +1524,7 @@
mProxy.onCallBusy(this);
}

        private void onError(int errorCode, String message) {
cancelSessionTimer();
switch (mState) {
case SipSession.State.REGISTERING:
//Synthetic comment -- @@ -1465,19 +1532,21 @@
onRegistrationFailed(errorCode, message);
break;
default:
endCallOnError(errorCode, message);
break;
}
}

        private void onError(Throwable exception) {
exception = getRootCause(exception);
onError(getErrorCode(exception), exception.toString());
}

        private void onError(Response response) {
int statusCode = response.getStatusCode();
if (!mInCall && (statusCode == Response.BUSY_HERE)) {
endCallOnBusy();
} else {
onError(getErrorCode(statusCode), createErrorMessage(response));
//Synthetic comment -- @@ -1738,7 +1807,7 @@
* @return true if the event is a response event and the CSeqHeader method
* match the given arguments; false otherwise
*/
    private static boolean expectResponse(
String expectedMethod, EventObject evt) {
if (evt instanceof ResponseEvent) {
ResponseEvent event = (ResponseEvent) evt;
//Synthetic comment -- @@ -1752,7 +1821,7 @@
* @return true if the event is a response event and the response code and
*      CSeqHeader method match the given arguments; false otherwise
*/
    private static boolean expectResponse(
int responseCode, String expectedMethod, EventObject evt) {
if (evt instanceof ResponseEvent) {
ResponseEvent event = (ResponseEvent) evt;
//Synthetic comment -- @@ -1843,6 +1912,8 @@
private class MakeCallCommand extends EventObject {
private String mSessionDescription;
private int mTimeout; // in seconds

public MakeCallCommand(SipProfile peerProfile,
String sessionDescription) {
//Synthetic comment -- @@ -1851,9 +1922,17 @@

public MakeCallCommand(SipProfile peerProfile,
String sessionDescription, int timeout) {
super(peerProfile);
mSessionDescription = sessionDescription;
mTimeout = timeout;
}

public SipProfile getPeerProfile() {
//Synthetic comment -- @@ -1867,6 +1946,14 @@
public int getTimeout() {
return mTimeout;
}
}

/** Class to help safely run KeepAliveProcessCallback in a different thread. */







