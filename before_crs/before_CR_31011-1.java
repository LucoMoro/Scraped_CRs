/*SIP-API: Miscellaneous bugfixes and enhancements.

1) Add missing try-catch clauses.
2) Secure pointer checks to avoid NullPointerExceptions.
3) Add/modify some methods in SipHelper class for future extensions.
4) Reorganize INVITE response handling for readability.

Change-Id:I4a8337f0690532d8405da3ee150c599063379627Signed-off-by: Masahiko Endo <masahiko.endo@gmail.com>*/
//Synthetic comment -- diff --git a/voip/java/com/android/server/sip/SipHelper.java b/voip/java/com/android/server/sip/SipHelper.java
//Synthetic comment -- index 113f007..46aca3f5 100644

//Synthetic comment -- @@ -22,6 +22,8 @@
import gov.nist.javax.sip.header.extensions.ReferencesHeader;
import gov.nist.javax.sip.header.extensions.ReferredByHeader;
import gov.nist.javax.sip.header.extensions.ReplacesHeader;

import android.net.sip.SipProfile;
import android.util.Log;
//Synthetic comment -- @@ -29,6 +31,7 @@
import java.text.ParseException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -47,21 +50,27 @@
import javax.sip.SipStack;
import javax.sip.Transaction;
import javax.sip.TransactionAlreadyExistsException;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.TransactionUnavailableException;
import javax.sip.TransactionState;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.Header;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Message;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
//Synthetic comment -- @@ -82,6 +91,10 @@
private HeaderFactory mHeaderFactory;
private MessageFactory mMessageFactory;

public SipHelper(SipStack sipStack, SipProvider sipProvider)
throws PeerUnavailableException {
mSipStack = sipStack;
//Synthetic comment -- @@ -93,6 +106,39 @@
mMessageFactory = sipFactory.createMessageFactory();
}

private FromHeader createFromHeader(SipProfile profile, String tag)
throws ParseException {
return mHeaderFactory.createFromHeader(profile.getSipAddress(), tag);
//Synthetic comment -- @@ -194,6 +240,10 @@
return uri;
}

public ClientTransaction sendOptions(SipProfile caller, SipProfile callee,
String tag) throws SipException {
try {
//Synthetic comment -- @@ -205,7 +255,11 @@
mSipProvider.getNewClientTransaction(request);
clientTransaction.sendRequest();
return clientTransaction;
        } catch (Exception e) {
throw new SipException("sendOptions()", e);
}
}
//Synthetic comment -- @@ -228,7 +282,179 @@
clientTransaction.sendRequest();
return clientTransaction;
} catch (ParseException e) {
throw new SipException("sendRegister()", e);
}
}

//Synthetic comment -- @@ -248,26 +474,11 @@
Request request = mMessageFactory.createRequest(requestURI,
requestType, callIdHeader, cSeqHeader, fromHeader,
toHeader, viaHeaders, maxForwards);
        Header userAgentHeader = mHeaderFactory.createHeader("User-Agent",
                "SIPAUA/0.1.001");
request.addHeader(userAgentHeader);
return request;
}

    public ClientTransaction handleChallenge(ResponseEvent responseEvent,
            AccountManager accountManager) throws SipException {
        AuthenticationHelper authenticationHelper =
                ((SipStackExt) mSipStack).getAuthenticationHelper(
                        accountManager, mHeaderFactory);
        ClientTransaction tid = responseEvent.getClientTransaction();
        ClientTransaction ct = authenticationHelper.handleChallenge(
                responseEvent.getResponse(), tid, mSipProvider, 5);
        if (DEBUG) Log.d(TAG, "send request with challenge response: "
                + ct.getRequest());
        ct.sendRequest();
        return ct;
    }

private Request createRequest(String requestType, SipProfile caller,
SipProfile callee, String tag) throws ParseException, SipException {
FromHeader fromHeader = createFromHeader(caller, tag);
//Synthetic comment -- @@ -286,212 +497,211 @@
return request;
}

    public ClientTransaction sendInvite(SipProfile caller, SipProfile callee,
            String sessionDescription, String tag, ReferredByHeader referredBy,
            String replaces) throws SipException {
        try {
            Request request = createRequest(Request.INVITE, caller, callee, tag);
            if (referredBy != null) request.addHeader(referredBy);
            if (replaces != null) {
                request.addHeader(mHeaderFactory.createHeader(
                        ReplacesHeader.NAME, replaces));
            }
            request.setContent(sessionDescription,
                    mHeaderFactory.createContentTypeHeader(
                            "application", "sdp"));
            ClientTransaction clientTransaction =
                    mSipProvider.getNewClientTransaction(request);
            if (DEBUG) Log.d(TAG, "send INVITE: " + request);
            clientTransaction.sendRequest();
            return clientTransaction;
        } catch (ParseException e) {
            throw new SipException("sendInvite()", e);
        }
    }

    public ClientTransaction sendReinvite(Dialog dialog,
            String sessionDescription) throws SipException {
        try {
            Request request = dialog.createRequest(Request.INVITE);
            request.setContent(sessionDescription,
                    mHeaderFactory.createContentTypeHeader(
                            "application", "sdp"));

            // Adding rport argument in the request could fix some SIP servers
            // in resolving the initiator's NAT port mapping for relaying the
            // response message from the other end.

            ViaHeader viaHeader = (ViaHeader) request.getHeader(ViaHeader.NAME);
            if (viaHeader != null) viaHeader.setRPort();

            ClientTransaction clientTransaction =
                    mSipProvider.getNewClientTransaction(request);
            if (DEBUG) Log.d(TAG, "send RE-INVITE: " + request);
            dialog.sendRequest(clientTransaction);
            return clientTransaction;
        } catch (ParseException e) {
            throw new SipException("sendReinvite()", e);
        }
    }

public ServerTransaction getServerTransaction(RequestEvent event)
throws SipException {
ServerTransaction transaction = event.getServerTransaction();
if (transaction == null) {
Request request = event.getRequest();
            return mSipProvider.getNewServerTransaction(request);
        } else {
            return transaction;
}
}

    /**
     * @param event the INVITE request event
     */
    public ServerTransaction sendRinging(RequestEvent event, String tag)
throws SipException {
try {
            Request request = event.getRequest();
            ServerTransaction transaction = getServerTransaction(event);

            Response response = mMessageFactory.createResponse(Response.RINGING,
                    request);

            ToHeader toHeader = (ToHeader) response.getHeader(ToHeader.NAME);
            toHeader.setTag(tag);
            response.addHeader(toHeader);
            if (DEBUG) Log.d(TAG, "send RINGING: " + response);
            transaction.sendResponse(response);
            return transaction;
} catch (ParseException e) {
            throw new SipException("sendRinging()", e);
}
}

/**
* @param event the INVITE request event
*/
    public ServerTransaction sendInviteOk(RequestEvent event,
SipProfile localProfile, String sessionDescription,
ServerTransaction inviteTransaction, String externalIp,
int externalPort) throws SipException {
        try {
            Request request = event.getRequest();
            Response response = mMessageFactory.createResponse(Response.OK,
                    request);
            response.addHeader(createContactHeader(localProfile, externalIp,
                    externalPort));
            response.setContent(sessionDescription,
                    mHeaderFactory.createContentTypeHeader(
                            "application", "sdp"));

            if (inviteTransaction == null) {
                inviteTransaction = getServerTransaction(event);
}

            if (inviteTransaction.getState() != TransactionState.COMPLETED) {
                if (DEBUG) Log.d(TAG, "send OK: " + response);
                inviteTransaction.sendResponse(response);
            }

            return inviteTransaction;
        } catch (ParseException e) {
            throw new SipException("sendInviteOk()", e);
}
}

public void sendInviteBusyHere(RequestEvent event,
ServerTransaction inviteTransaction) throws SipException {
        try {
            Request request = event.getRequest();
            Response response = mMessageFactory.createResponse(
                    Response.BUSY_HERE, request);

            if (inviteTransaction == null) {
                inviteTransaction = getServerTransaction(event);
            }

            if (inviteTransaction.getState() != TransactionState.COMPLETED) {
                if (DEBUG) Log.d(TAG, "send BUSY HERE: " + response);
                inviteTransaction.sendResponse(response);
            }
        } catch (ParseException e) {
            throw new SipException("sendInviteBusyHere()", e);
}
}

    /**
     * @param event the INVITE ACK request event
     */
    public void sendInviteAck(ResponseEvent event, Dialog dialog)
            throws SipException {
        Response response = event.getResponse();
        long cseq = ((CSeqHeader) response.getHeader(CSeqHeader.NAME))
                .getSeqNumber();
        Request ack = dialog.createAck(cseq);
        if (DEBUG) Log.d(TAG, "send ACK: " + ack);
        dialog.sendAck(ack);
}

    public void sendBye(Dialog dialog) throws SipException {
        Request byeRequest = dialog.createRequest(Request.BYE);
        if (DEBUG) Log.d(TAG, "send BYE: " + byeRequest);
        dialog.sendRequest(mSipProvider.getNewClientTransaction(byeRequest));
}

    public void sendCancel(ClientTransaction inviteTransaction)
            throws SipException {
        Request cancelRequest = inviteTransaction.createCancel();
        if (DEBUG) Log.d(TAG, "send CANCEL: " + cancelRequest);
        mSipProvider.getNewClientTransaction(cancelRequest).sendRequest();
}

public void sendResponse(RequestEvent event, int responseCode)
throws SipException {
try {
Request request = event.getRequest();
            Response response = mMessageFactory.createResponse(
                    responseCode, request);
if (DEBUG && (!Request.OPTIONS.equals(request.getMethod())
|| DEBUG_PING)) {
Log.d(TAG, "send response: " + response);
}
            getServerTransaction(event).sendResponse(response);
} catch (ParseException e) {
throw new SipException("sendResponse()", e);
}
}

    public void sendReferNotify(Dialog dialog, String content)
throws SipException {
        try {
            Request request = dialog.createRequest(Request.NOTIFY);
            request.addHeader(mHeaderFactory.createSubscriptionStateHeader(
                    "active;expires=60"));
            // set content here
            request.setContent(content,
                    mHeaderFactory.createContentTypeHeader(
                            "message", "sipfrag"));
            request.addHeader(mHeaderFactory.createEventHeader(
                    ReferencesHeader.REFER));
            if (DEBUG) Log.d(TAG, "send NOTIFY: " + request);
            dialog.sendRequest(mSipProvider.getNewClientTransaction(request));
        } catch (ParseException e) {
            throw new SipException("sendReferNotify()", e);
}
}

    public void sendInviteRequestTerminated(Request inviteRequest,
            ServerTransaction inviteTransaction) throws SipException {
        try {
            Response response = mMessageFactory.createResponse(
                    Response.REQUEST_TERMINATED, inviteRequest);
            if (DEBUG) Log.d(TAG, "send response: " + response);
            inviteTransaction.sendResponse(response);
        } catch (ParseException e) {
            throw new SipException("sendInviteRequestTerminated()", e);
        }
    }

public static String getCallId(EventObject event) {
if (event == null) return null;
//Synthetic comment -- @@ -532,4 +742,30 @@
private static String getCallId(Dialog dialog) {
return dialog.getCallId().getCallId();
}
}








//Synthetic comment -- diff --git a/voip/java/com/android/server/sip/SipSessionGroup.java b/voip/java/com/android/server/sip/SipSessionGroup.java
//Synthetic comment -- index a4bee36..5282175 100644

//Synthetic comment -- @@ -56,6 +56,7 @@
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.ObjectInUseException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
//Synthetic comment -- @@ -69,6 +70,7 @@
import javax.sip.TransactionState;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.TransactionUnavailableException;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
//Synthetic comment -- @@ -154,7 +156,16 @@

synchronized void reset(String localIp) throws SipException, IOException {
mLocalIp = localIp;
        if (localIp == null) return;

SipProfile myself = mLocalProfile;
SipFactory sipFactory = SipFactory.getInstance();
//Synthetic comment -- @@ -168,17 +179,29 @@
properties.setProperty("javax.sip.OUTBOUND_PROXY", outboundProxy
+ ":" + myself.getPort() + "/" + myself.getProtocol());
}
        SipStack stack = mSipStack = sipFactory.createSipStack(properties);

try {
SipProvider provider = stack.createSipProvider(
stack.createListeningPoint(localIp, allocateLocalPort(),
myself.getProtocol()));
provider.addSipListener(this);
mSipHelper = new SipHelper(stack, provider);
} catch (InvalidArgumentException e) {
throw new IOException(e.getMessage());
} catch (TooManyListenersException e) {
// must never happen
throw new SipException("SipSessionGroup constructor", e);
}
//Synthetic comment -- @@ -1106,7 +1129,7 @@
if (evt instanceof MakeCallCommand) {
// answer call
mState = SipSession.State.INCOMING_CALL_ANSWERING;
                mServerTransaction = mSipHelper.sendInviteOk(mInviteReceived,
mLocalProfile,
((MakeCallCommand) evt).getSessionDescription(),
mServerTransaction,
//Synthetic comment -- @@ -1119,10 +1142,13 @@
endCallNormally();
return true;
} else if (isRequestEvent(Request.CANCEL, evt)) {
RequestEvent event = (RequestEvent) evt;
mSipHelper.sendResponse(event, Response.OK);
mSipHelper.sendInviteRequestTerminated(
                        mInviteReceived.getRequest(), mServerTransaction);
endCallNormally();
return true;
}
//Synthetic comment -- @@ -1151,66 +1177,13 @@

private boolean outgoingCall(EventObject evt) throws SipException {
if (expectResponse(Request.INVITE, evt)) {
                ResponseEvent event = (ResponseEvent) evt;
                Response response = event.getResponse();

                int statusCode = response.getStatusCode();
                switch (statusCode) {
                case Response.RINGING:
                case Response.CALL_IS_BEING_FORWARDED:
                case Response.QUEUED:
                case Response.SESSION_PROGRESS:
                    // feedback any provisional responses (except TRYING) as
                    // ring back for better UX
                    if (mState == SipSession.State.OUTGOING_CALL) {
                        mState = SipSession.State.OUTGOING_CALL_RING_BACK;
                        cancelSessionTimer();
                        mProxy.onRingingBack(this);
                    }
                    return true;
                case Response.OK:
                    if (mReferSession != null) {
                        mSipHelper.sendReferNotify(mReferSession.mDialog,
                                getResponseString(Response.OK));
                        // since we don't need to remember the session anymore.
                        mReferSession = null;
                    }
                    mSipHelper.sendInviteAck(event, mDialog);
                    mPeerSessionDescription = extractContent(response);
                    establishCall(true);
                    return true;
                case Response.UNAUTHORIZED:
                case Response.PROXY_AUTHENTICATION_REQUIRED:
                    if (handleAuthentication(event)) {
                        addSipSession(this);
                    }
                    return true;
                case Response.REQUEST_PENDING:
                    // TODO:
                    // rfc3261#section-14.1; re-schedule invite
                    return true;
                default:
                    if (mReferSession != null) {
                        mSipHelper.sendReferNotify(mReferSession.mDialog,
                                getResponseString(Response.SERVICE_UNAVAILABLE));
                    }
                    if (statusCode >= 400) {
                        // error: an ack is sent automatically by the stack
                        onError(response);
                        return true;
                    } else if (statusCode >= 300) {
                        // TODO: handle 3xx (redirect)
                    } else {
                        return true;
                    }
                }
                return false;
} else if (END_CALL == evt) {
// RFC says that UA should not send out cancel when no
// response comes back yet. We are cheating for not checking
// response.
mState = SipSession.State.OUTGOING_CALL_CANCELING;
                mSipHelper.sendCancel(mClientTransaction);
startSessionTimer(CANCEL_CALL_TIMER);
return true;
} else if (isRequestEvent(Request.INVITE, evt)) {
//Synthetic comment -- @@ -1300,7 +1273,7 @@
if (END_CALL == evt) {
// rfc3261#section-15.1.1
mState = SipSession.State.ENDING_CALL;
                mSipHelper.sendBye(mDialog);
mProxy.onCallEnded(this);
startSessionTimer(END_CALL_TIMER);
return true;
//Synthetic comment -- @@ -1353,6 +1326,79 @@
return false;
}

// timeout in seconds
private void startSessionTimer(int timeout) {
if (timeout > 0) {
//Synthetic comment -- @@ -1420,10 +1466,10 @@
break;
default:
endCallOnError(errorCode, message);
}
}


private void onError(Throwable exception) {
exception = getRootCause(exception);
onError(getErrorCode(exception), exception.toString());








//Synthetic comment -- diff --git a/voip/java/com/android/server/sip/SipWarning.java b/voip/java/com/android/server/sip/SipWarning.java
new file mode 100644
//Synthetic comment -- index 0000000..3d2c07f

//Synthetic comment -- @@ -0,0 +1,77 @@







