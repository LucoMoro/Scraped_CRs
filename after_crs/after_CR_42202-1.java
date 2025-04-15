/*Add SIP User-Agent header to dialogs

Allow users of the SIP API to specify the User-Agent when using the SIP API. This allows those building applications with the API to identify their clients on the server side.

Change-Id:I65c4afb543d4f727d8d34b0e4097dc15e5060683Signed-off-by: Justin Milam <jmilam@bandwidth.com>*/




//Synthetic comment -- diff --git a/voip/java/android/net/sip/SipProfile.java b/voip/java/android/net/sip/SipProfile.java
//Synthetic comment -- index 0ef754c..f297a2b 100644

//Synthetic comment -- @@ -57,6 +57,7 @@
private String mProtocol = UDP;
private String mProfileName;
private String mAuthUserName;
    private String mUserAgent;
private int mPort = DEFAULT_PORT;
private boolean mSendKeepAlive = false;
private boolean mAutoRegistration = true;
//Synthetic comment -- @@ -82,6 +83,7 @@
private SipURI mUri;
private String mDisplayName;
private String mProxyAddress;
        private String mUserAgentString;

{
try {
//Synthetic comment -- @@ -108,6 +110,7 @@
mDisplayName = profile.getDisplayName();
mProxyAddress = profile.getProxyAddress();
mProfile.mPort = profile.getPort();
            mUserAgentString = profile.getUserAgent();
}

/**
//Synthetic comment -- @@ -270,6 +273,18 @@
mProfile.mAutoRegistration = flag;
return this;
}
        
        /**
         * Sets the User-Agent string to be used in SIP transactions associated
         * with this SipProfile.
         * 
         * @param userAgent the user agent string
         * @return this builder object
         */
        public Builder setUserAgent(String userAgent) {
            mUserAgentString = userAgent;
            return this;
        }

/**
* Builds and returns the SIP profile object.
//Synthetic comment -- @@ -293,6 +308,11 @@
mUri.setPort(mProfile.mPort);
}
}
                if(!TextUtils.isEmpty(mUserAgentString)) {
                    mProfile.mUserAgent = mUserAgentString;
                } else {
                    mProfile.mUserAgent = "SIPAUA/0.1.001";
                }
mProfile.mAddress = mAddressFactory.createAddress(
mDisplayName, mUri);
} catch (InvalidArgumentException e) {
//Synthetic comment -- @@ -320,6 +340,7 @@
mCallingUid = in.readInt();
mPort = in.readInt();
mAuthUserName = in.readString();
        mUserAgent = in.readString();
}

@Override
//Synthetic comment -- @@ -335,6 +356,7 @@
out.writeInt(mCallingUid);
out.writeInt(mPort);
out.writeString(mAuthUserName);
        out.writeString(mUserAgent);
}

@Override
//Synthetic comment -- @@ -493,6 +515,15 @@
public int getCallingUid() {
return mCallingUid;
}
    
    /**
     * Gets the User-Agent string to use for this profile.
     * 
     * @return the User-Agent string
     */
    public String getUserAgent() {
        return mUserAgent;
    }

private Object readResolve() throws ObjectStreamException {
// For compatibility.








//Synthetic comment -- diff --git a/voip/java/com/android/server/sip/SipHelper.java b/voip/java/com/android/server/sip/SipHelper.java
//Synthetic comment -- index 113f007..4e27501 100644

//Synthetic comment -- @@ -200,7 +200,7 @@
Request request = (caller == callee)
? createRequest(Request.OPTIONS, caller, tag)
: createRequest(Request.OPTIONS, caller, callee, tag);
                    
ClientTransaction clientTransaction =
mSipProvider.getNewClientTransaction(request);
clientTransaction.sendRequest();
//Synthetic comment -- @@ -249,7 +249,7 @@
requestType, callIdHeader, cSeqHeader, fromHeader,
toHeader, viaHeaders, maxForwards);
Header userAgentHeader = mHeaderFactory.createHeader("User-Agent",
                userProfile.getUserAgent());
request.addHeader(userAgentHeader);
return request;
}
//Synthetic comment -- @@ -281,6 +281,10 @@
Request request = mMessageFactory.createRequest(requestURI,
requestType, callIdHeader, cSeqHeader, fromHeader,
toHeader, viaHeaders, maxForwards);
        
        Header userAgentHeader = mHeaderFactory.createHeader("User-Agent", 
                caller.getUserAgent());
        request.addHeader(userAgentHeader);

request.addHeader(createContactHeader(caller));
return request;
//Synthetic comment -- @@ -296,6 +300,10 @@
request.addHeader(mHeaderFactory.createHeader(
ReplacesHeader.NAME, replaces));
}
            
            Header userAgentHeader = mHeaderFactory.createHeader("User-Agent", 
                    caller.getUserAgent());
            request.addHeader(userAgentHeader);
request.setContent(sessionDescription,
mHeaderFactory.createContentTypeHeader(
"application", "sdp"));
//Synthetic comment -- @@ -309,14 +317,19 @@
}
}

    public ClientTransaction sendReinvite(SipProfile userProfile,
            Dialog dialog, String sessionDescription) throws SipException {
try {
Request request = dialog.createRequest(Request.INVITE);
            
request.setContent(sessionDescription,
mHeaderFactory.createContentTypeHeader(
"application", "sdp"));

            Header userAgentHeader = mHeaderFactory.createHeader("User-Agent", 
                    userProfile.getUserAgent());
            request.addHeader(userAgentHeader);
            
// Adding rport argument in the request could fix some SIP servers
// in resolving the initiator's NAT port mapping for relaying the
// response message from the other end.
//Synthetic comment -- @@ -348,7 +361,7 @@
/**
* @param event the INVITE request event
*/
    public ServerTransaction sendRinging(SipProfile userProfile, RequestEvent event, String tag)
throws SipException {
try {
Request request = event.getRequest();
//Synthetic comment -- @@ -360,6 +373,10 @@
ToHeader toHeader = (ToHeader) response.getHeader(ToHeader.NAME);
toHeader.setTag(tag);
response.addHeader(toHeader);
            
            Header userAgentHeader = mHeaderFactory.createHeader("User-Agent",
                    userProfile.getUserAgent());
            response.addHeader(userAgentHeader);
if (DEBUG) Log.d(TAG, "send RINGING: " + response);
transaction.sendResponse(response);
return transaction;
//Synthetic comment -- @@ -371,7 +388,7 @@
/**
* @param event the INVITE request event
*/
    public ServerTransaction sendInviteOk( RequestEvent event,
SipProfile localProfile, String sessionDescription,
ServerTransaction inviteTransaction, String externalIp,
int externalPort) throws SipException {
//Synthetic comment -- @@ -381,6 +398,10 @@
request);
response.addHeader(createContactHeader(localProfile, externalIp,
externalPort));
            
            Header userAgentHeader = mHeaderFactory.createHeader("User-Agent",
                    localProfile.getUserAgent());
            response.addHeader(userAgentHeader);
response.setContent(sessionDescription,
mHeaderFactory.createContentTypeHeader(
"application", "sdp"));
//Synthetic comment -- @@ -400,13 +421,17 @@
}
}

    public void sendInviteBusyHere(SipProfile userProfile, RequestEvent event,
ServerTransaction inviteTransaction) throws SipException {
try {
Request request = event.getRequest();
Response response = mMessageFactory.createResponse(
Response.BUSY_HERE, request);

            Header userAgentHeader = mHeaderFactory.createHeader("User-Agent",
                    userProfile.getUserAgent());
            response.addHeader(userAgentHeader);
            
if (inviteTransaction == null) {
inviteTransaction = getServerTransaction(event);
}
//Synthetic comment -- @@ -423,30 +448,58 @@
/**
* @param event the INVITE ACK request event
*/
    public void sendInviteAck(SipProfile userProfile, ResponseEvent event, Dialog dialog)
throws SipException {
Response response = event.getResponse();
long cseq = ((CSeqHeader) response.getHeader(CSeqHeader.NAME))
.getSeqNumber();
Request ack = dialog.createAck(cseq);
        try {
            Header userAgentHeader = mHeaderFactory.createHeader("User-Agent",
                    userProfile.getUserAgent());
            ack.addHeader(userAgentHeader);
        } catch (ParseException e) {
            throw new SipException("sendInviteAck()", e);
        }
if (DEBUG) Log.d(TAG, "send ACK: " + ack);
dialog.sendAck(ack);
}

    public void sendBye(SipProfile userProfile, Dialog dialog) throws SipException {
Request byeRequest = dialog.createRequest(Request.BYE);
        try {
            Header userAgentHeader = mHeaderFactory.createHeader("User-Agent",
                    userProfile.getUserAgent());
            byeRequest.addHeader(userAgentHeader);
        } catch (ParseException e) {
            throw new SipException("sendInviteAck()", e);
        }
if (DEBUG) Log.d(TAG, "send BYE: " + byeRequest);
dialog.sendRequest(mSipProvider.getNewClientTransaction(byeRequest));
}

    public void sendCancel(SipProfile userProfile, ClientTransaction inviteTransaction)
throws SipException {
Request cancelRequest = inviteTransaction.createCancel();
        try {
            Header userAgentHeader = mHeaderFactory.createHeader("User-Agent",
                    userProfile.getUserAgent());
            cancelRequest.addHeader(userAgentHeader);
        } catch (ParseException e) {
            throw new SipException("sendBye()", e);
        }
if (DEBUG) Log.d(TAG, "send CANCEL: " + cancelRequest);
        try {
            Header userAgentHeader = mHeaderFactory.createHeader("User-Agent",
                    userProfile.getUserAgent());
            cancelRequest.addHeader(userAgentHeader);
        } catch (ParseException e) {
            throw new SipException("sendBye()", e);
        }
mSipProvider.getNewClientTransaction(cancelRequest).sendRequest();
}

    public void sendResponse(SipProfile userProfile, RequestEvent event, int responseCode)
throws SipException {
try {
Request request = event.getRequest();
//Synthetic comment -- @@ -456,13 +509,18 @@
|| DEBUG_PING)) {
Log.d(TAG, "send response: " + response);
}
            
            Header userAgentHeader = mHeaderFactory.createHeader("User-Agent",
                    userProfile.getUserAgent());
            request.addHeader(userAgentHeader);
            
getServerTransaction(event).sendResponse(response);
} catch (ParseException e) {
throw new SipException("sendResponse()", e);
}
}

    public void sendReferNotify(SipProfile userProfile, Dialog dialog, String content)
throws SipException {
try {
Request request = dialog.createRequest(Request.NOTIFY);
//Synthetic comment -- @@ -474,6 +532,9 @@
"message", "sipfrag"));
request.addHeader(mHeaderFactory.createEventHeader(
ReferencesHeader.REFER));
            Header userAgentHeader = mHeaderFactory.createHeader("User-Agent",
                    userProfile.getUserAgent());
            request.addHeader(userAgentHeader);
if (DEBUG) Log.d(TAG, "send NOTIFY: " + request);
dialog.sendRequest(mSipProvider.getNewClientTransaction(request));
} catch (ParseException e) {
//Synthetic comment -- @@ -481,11 +542,15 @@
}
}

    public void sendInviteRequestTerminated(SipProfile userProfile, Request inviteRequest,
ServerTransaction inviteTransaction) throws SipException {
try {
Response response = mMessageFactory.createResponse(
Response.REQUEST_TERMINATED, inviteRequest);
            
            Header userAgentHeader = mHeaderFactory.createHeader("User-Agent",
                    userProfile.getUserAgent());
            response.addHeader(userAgentHeader);
if (DEBUG) Log.d(TAG, "send response: " + response);
inviteTransaction.sendResponse(response);
} catch (ParseException e) {








//Synthetic comment -- diff --git a/voip/java/com/android/server/sip/SipSessionGroup.java b/voip/java/com/android/server/sip/SipSessionGroup.java
//Synthetic comment -- index 6acd456..612726f 100644

//Synthetic comment -- @@ -488,12 +488,12 @@
newSession.mProxy.onCallTransferring(newSession,
newSession.mPeerSessionDescription);
} else {
                    mSipHelper.sendResponse(mLocalProfile, event, response);
}
} else {
// New Incoming call.
newSession = createNewSession(event, mProxy,
                        mSipHelper.sendRinging(mLocalProfile, event, generateTag()),
SipSession.State.INCOMING_CALL);
mProxy.onRinging(newSession, newSession.mPeerProfile,
newSession.mPeerSessionDescription);
//Synthetic comment -- @@ -509,7 +509,7 @@
processNewInviteRequest((RequestEvent) evt);
return true;
} else if (isRequestEvent(Request.OPTIONS, evt)) {
                mSipHelper.sendResponse(mLocalProfile, (RequestEvent) evt, Response.OK);
return true;
} else {
return false;
//Synthetic comment -- @@ -793,11 +793,11 @@
private boolean processExceptions(EventObject evt) throws SipException {
if (isRequestEvent(Request.BYE, evt)) {
// terminate the call whenever a BYE is received
                mSipHelper.sendResponse(mLocalProfile, (RequestEvent) evt, Response.OK);
endCallNormally();
return true;
} else if (isRequestEvent(Request.CANCEL, evt)) {
                mSipHelper.sendResponse(mLocalProfile, (RequestEvent) evt,
Response.CALL_OR_TRANSACTION_DOES_NOT_EXIST);
return true;
} else if (evt instanceof TransactionTerminatedEvent) {
//Synthetic comment -- @@ -811,7 +811,7 @@
return true;
}
} else if (isRequestEvent(Request.OPTIONS, evt)) {
                mSipHelper.sendResponse(mLocalProfile, (RequestEvent) evt, Response.OK);
return true;
} else if (evt instanceof DialogTerminatedEvent) {
processDialogTerminated((DialogTerminatedEvent) evt);
//Synthetic comment -- @@ -1037,7 +1037,7 @@
MakeCallCommand cmd = (MakeCallCommand) evt;
mPeerProfile = cmd.getPeerProfile();
if (mReferSession != null) {
                    mSipHelper.sendReferNotify(mLocalProfile, mReferSession.mDialog,
getResponseString(Response.TRYING));
}
mClientTransaction = mSipHelper.sendInvite(
//Synthetic comment -- @@ -1082,14 +1082,14 @@
startSessionTimer(((MakeCallCommand) evt).getTimeout());
return true;
} else if (END_CALL == evt) {
                mSipHelper.sendInviteBusyHere(mLocalProfile, mInviteReceived,
mServerTransaction);
endCallNormally();
return true;
} else if (isRequestEvent(Request.CANCEL, evt)) {
RequestEvent event = (RequestEvent) evt;
                mSipHelper.sendResponse(mLocalProfile, event, Response.OK);
                mSipHelper.sendInviteRequestTerminated(mLocalProfile,
mInviteReceived.getRequest(), mServerTransaction);
endCallNormally();
return true;
//Synthetic comment -- @@ -1138,12 +1138,13 @@
return true;
case Response.OK:
if (mReferSession != null) {
                        mSipHelper.sendReferNotify(mLocalProfile,
                                mReferSession.mDialog,
getResponseString(Response.OK));
// since we don't need to remember the session anymore.
mReferSession = null;
}
                    mSipHelper.sendInviteAck(mLocalProfile, event, mDialog);
mPeerSessionDescription = extractContent(response);
establishCall(true);
return true;
//Synthetic comment -- @@ -1159,7 +1160,8 @@
return true;
default:
if (mReferSession != null) {
                        mSipHelper.sendReferNotify(mLocalProfile,
                                mReferSession.mDialog,
getResponseString(Response.SERVICE_UNAVAILABLE));
}
if (statusCode >= 400) {
//Synthetic comment -- @@ -1178,14 +1180,15 @@
// response comes back yet. We are cheating for not checking
// response.
mState = SipSession.State.OUTGOING_CALL_CANCELING;
                mSipHelper.sendCancel(mLocalProfile, mClientTransaction);
startSessionTimer(CANCEL_CALL_TIMER);
return true;
} else if (isRequestEvent(Request.INVITE, evt)) {
// Call self? Send BUSY HERE so server may redirect the call to
// voice mailbox.
RequestEvent event = (RequestEvent) evt;
                mSipHelper.sendInviteBusyHere(mLocalProfile,
                        event,
event.getServerTransaction());
return true;
}
//Synthetic comment -- @@ -1240,11 +1243,11 @@
String replacesHeader = uri.getHeader(ReplacesHeader.NAME);
String username = uri.getUser();
if (username == null) {
                    mSipHelper.sendResponse(mLocalProfile, event, Response.BAD_REQUEST);
return false;
}
// send notify accepted
                mSipHelper.sendResponse(mLocalProfile, event, Response.ACCEPTED);
SipSessionImpl newSession = createNewSession(event,
this.mProxy.getListener(),
mSipHelper.getServerTransaction(event),
//Synthetic comment -- @@ -1268,7 +1271,7 @@
if (END_CALL == evt) {
// rfc3261#section-15.1.1
mState = SipSession.State.ENDING_CALL;
                mSipHelper.sendBye(mLocalProfile, mDialog);
mProxy.onCallEnded(this);
startSessionTimer(END_CALL_TIMER);
return true;
//Synthetic comment -- @@ -1281,7 +1284,7 @@
mProxy.onRinging(this, mPeerProfile, mPeerSessionDescription);
return true;
} else if (isRequestEvent(Request.BYE, evt)) {
                mSipHelper.sendResponse(mLocalProfile, (RequestEvent) evt, Response.OK);
endCallNormally();
return true;
} else if (isRequestEvent(Request.REFER, evt)) {
//Synthetic comment -- @@ -1289,7 +1292,7 @@
} else if (evt instanceof MakeCallCommand) {
// to change call
mState = SipSession.State.OUTGOING_CALL;
                mClientTransaction = mSipHelper.sendReinvite(mLocalProfile, mDialog,
((MakeCallCommand) evt).getSessionDescription());
startSessionTimer(((MakeCallCommand) evt).getTimeout());
return true;







