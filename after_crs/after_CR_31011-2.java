/*SIP-API: Miscellaneous bugfixes and enhancements.

1) Add missing try-catch clauses.
2) Secure pointer checks to avoid NullPointerExceptions.
3) Add/modify some methods in SipHelper class for future extensions.
4) Reorganize INVITE response handling for readability.

Change-Id:I4a8337f0690532d8405da3ee150c599063379627Signed-off-by: Masahiko Endo <masahiko.endo@gmail.com>*/




//Synthetic comment -- diff --git a/voip/java/com/android/server/sip/SipHelper.java b/voip/java/com/android/server/sip/SipHelper.java
//Synthetic comment -- index 113f007..bd02508 100644

//Synthetic comment -- @@ -22,6 +22,8 @@
import gov.nist.javax.sip.header.extensions.ReferencesHeader;
import gov.nist.javax.sip.header.extensions.ReferredByHeader;
import gov.nist.javax.sip.header.extensions.ReplacesHeader;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;

import android.net.sip.SipProfile;
import android.util.Log;
//Synthetic comment -- @@ -29,6 +31,7 @@
import java.text.ParseException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -47,21 +50,27 @@
import javax.sip.SipStack;
import javax.sip.Transaction;
import javax.sip.TransactionAlreadyExistsException;
import javax.sip.TransactionDoesNotExistException;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.TransactionUnavailableException;
import javax.sip.TransactionState;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.AllowHeader;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.Header;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ReasonHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.UserAgentHeader;
import javax.sip.header.ViaHeader;
import javax.sip.header.WarningHeader;
import javax.sip.message.Message;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
//Synthetic comment -- @@ -82,6 +91,10 @@
private HeaderFactory mHeaderFactory;
private MessageFactory mMessageFactory;

    /*-----------------------------------------------------------------*
     * Constructor
     *-----------------------------------------------------------------*/

public SipHelper(SipStack sipStack, SipProvider sipProvider)
throws PeerUnavailableException {
mSipStack = sipStack;
//Synthetic comment -- @@ -93,6 +106,39 @@
mMessageFactory = sipFactory.createMessageFactory();
}

    /*-----------------------------------------------------------------*
     * Create various headers
     *-----------------------------------------------------------------*/

    private AllowHeader createAllowHeader()
            throws ParseException {
        String methods = "ACK,BYE,CANCEL,INVITE,OPTIONS,REGISTER,NOTIFY,REFER";
        return mHeaderFactory.createAllowHeader(methods);
    }

    private WarningHeader createWarningHeader(
        String agentName, int warningCode, String warningMessage)
            throws ParseException, InvalidArgumentException {
        return mHeaderFactory.createWarningHeader(
                    agentName, warningCode, warningMessage);
    }

    private ReasonHeader createReasonHeader(
        String protocol, int cause, String text)
            throws ParseException, InvalidArgumentException {
        return mHeaderFactory.createReasonHeader(protocol, cause, text);
    }

    private UserAgentHeader createUserAgentHeader(List product)
            throws ParseException {
        List list = product;
        if (list == null) {
            list = new LinkedList();
            list.add("SIPAUA/0.1.001");
        }
        return mHeaderFactory.createUserAgentHeader(list);
    }

private FromHeader createFromHeader(SipProfile profile, String tag)
throws ParseException {
return mHeaderFactory.createFromHeader(profile.getSipAddress(), tag);
//Synthetic comment -- @@ -194,6 +240,10 @@
return uri;
}

    /*-----------------------------------------------------------------*
     * Sending request messages
     *-----------------------------------------------------------------*/

public ClientTransaction sendOptions(SipProfile caller, SipProfile callee,
String tag) throws SipException {
try {
//Synthetic comment -- @@ -205,7 +255,11 @@
mSipProvider.getNewClientTransaction(request);
clientTransaction.sendRequest();
return clientTransaction;
        } catch (ParseException e) {
            /* this.createRequest() */
            throw new SipException("sendOptions()", e);
        } catch (TransactionUnavailableException e) {
            /* Provider.getNewClientTransaction() */
throw new SipException("sendOptions()", e);
}
}
//Synthetic comment -- @@ -228,7 +282,179 @@
clientTransaction.sendRequest();
return clientTransaction;
} catch (ParseException e) {
            /* this.createRequest() */
throw new SipException("sendRegister()", e);
        } catch (TransactionUnavailableException e) {
            /* Provider.getNewClientTransaction() */
            throw new SipException("sendRegister()", e);
        }
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
            if (sessionDescription != null) {
                setSdpMessage((Message)request, sessionDescription);
            }
            ClientTransaction clientTransaction =
                    mSipProvider.getNewClientTransaction(request);
            if (DEBUG) Log.d(TAG, "send INVITE: " + request);
            clientTransaction.sendRequest();
            return clientTransaction;
        } catch (ParseException e) {
            /* this.createRequest() */
            throw new SipException("sendInvite()", e);
        } catch (TransactionUnavailableException e) {
            /* Provider.getNewClientTransaction() */
            throw new SipException("sendInvite()", e);
        }
    }

    public ClientTransaction sendReinvite(Dialog dialog,
            String sessionDescription) throws SipException {
        try {
            Request request = dialog.createRequest(Request.INVITE);
            if (sessionDescription != null) {
                setSdpMessage((Message)request, sessionDescription);
            }

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
        } catch (TransactionUnavailableException e) {
            /* Provider.getNewClientTransaction() */
            throw new SipException("sendReinvite()", e);
        } catch (TransactionDoesNotExistException e) {
            /* Dialog.sendRequest() */
            throw new SipException("sendReinvite()", e);
        }
    }

    /**
     * @param event the INVITE ACK request event
     */
    public void sendInviteAck(ResponseEvent event, Dialog dialog)
            throws SipException {
        try {
            Response response = event.getResponse();
            long cseq = ((CSeqHeader) response.getHeader(CSeqHeader.NAME))
                    .getSeqNumber();
            Request request = dialog.createAck(cseq);
            if (DEBUG) Log.d(TAG, "send ACK: " + request);
            dialog.sendAck(request);
        } catch (InvalidArgumentException e) {
            /* Dialog.createAck() */
            throw new SipException("sendInviteAck()", e);
        }
    }

    public void sendBye(Dialog dialog, int statusCode) throws SipException {
        try {
            Request request = dialog.createRequest(Request.BYE);
            if (statusCode > 0) {
                /* Add Reason header (RFC3326) for better UI */
                try {
                    String reasonPhrase =
                        SIPResponse.getReasonPhrase(statusCode);
                    ReasonHeader reasonHeader =
                        createReasonHeader("SIP", statusCode, reasonPhrase);
                    request.addHeader(reasonHeader);
                } catch (ParseException e) {
                    Log.w(TAG, "sendBye(): createReasonHeader: ", e);
                } catch (InvalidArgumentException e) {
                    Log.w(TAG, "sendBye(): createReasonHeader: ", e);
                }
            }
            if (DEBUG) Log.d(TAG, "send BYE: " + request);
            dialog.sendRequest(
                mSipProvider.getNewClientTransaction(request));
        } catch (TransactionDoesNotExistException e) {
            /* Dialog.sendRequest() */
            throw new SipException("sendBye()", e);
        }
    }

    public void sendCancel(ClientTransaction inviteTransaction, int statusCode)
            throws SipException {
        try {
            Request request = inviteTransaction.createCancel();
            if (statusCode > 0) {
                /* Add Reason header (RFC3326) for better UI */
                try {
                    String reasonPhrase =
                        SIPResponse.getReasonPhrase(statusCode);
                    ReasonHeader reasonHeader =
                        createReasonHeader("SIP", statusCode, reasonPhrase);
                    request.addHeader(reasonHeader);
                } catch (ParseException e) {
                    Log.w(TAG, "sendCancel(): createReasonHeader: ", e);
                } catch (InvalidArgumentException e) {
                    Log.w(TAG, "sendCancel(): createReasonHeader: ", e);
                }
            }
            if (DEBUG) Log.d(TAG, "send CANCEL: " + request);
            ClientTransaction clientTransaction =
                    mSipProvider.getNewClientTransaction(request);
            clientTransaction.sendRequest();
        } catch (TransactionUnavailableException e) {
            /* Provider.getNewClientTransaction() */
            throw new SipException("sendCancel()", e);
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
            /* HeaderFactory.createSubscriptionStateHeader() */
            throw new SipException("sendReferNotify()", e);
        } catch (TransactionUnavailableException e) {
            /* Provider.getNewClientTransaction() */
            throw new SipException("sendReferNotify()", e);
        } catch (TransactionDoesNotExistException e) {
            /* Dialog.sendRequest() */
            throw new SipException("sendReferNotify()", e);
}
}

//Synthetic comment -- @@ -248,26 +474,11 @@
Request request = mMessageFactory.createRequest(requestURI,
requestType, callIdHeader, cSeqHeader, fromHeader,
toHeader, viaHeaders, maxForwards);
        Header userAgentHeader = createUserAgentHeader(null);
request.addHeader(userAgentHeader);
return request;
}

private Request createRequest(String requestType, SipProfile caller,
SipProfile callee, String tag) throws ParseException, SipException {
FromHeader fromHeader = createFromHeader(caller, tag);
//Synthetic comment -- @@ -286,212 +497,211 @@
return request;
}

    /*-----------------------------------------------------------------*
     * Sending response messages
     *-----------------------------------------------------------------*/

public ServerTransaction getServerTransaction(RequestEvent event)
throws SipException {
ServerTransaction transaction = event.getServerTransaction();
if (transaction == null) {
Request request = event.getRequest();
            try {
                transaction = mSipProvider.getNewServerTransaction(request);
            } catch (TransactionAlreadyExistsException e) {
                throw new SipException("getServerTransaction()", e);
            } catch (TransactionUnavailableException e) {
                throw new SipException("getServerTransaction()", e);
            }
}
        return transaction;
}

    public void sendNotAcceptableHere(RequestEvent event,
            String agentName, int warningCode, String warningMessage)
throws SipException {
try {
            ArrayList<Header> customHeaders = new ArrayList<Header>();
            WarningHeader warningHeader =
                createWarningHeader(agentName, warningCode, warningMessage);
            customHeaders.add(warningHeader);

            sendResponse(event, Response.NOT_ACCEPTABLE_HERE, customHeaders);
} catch (ParseException e) {
            throw new SipException("sendNotAcceptableHere()", e);
        } catch (InvalidArgumentException e) {
            throw new SipException("sendNotAcceptableHere()", e);
}
}

/**
* @param event the INVITE request event
*/
    public ServerTransaction sendRinging(RequestEvent event, String toTag)
            throws SipException {
        /*
         * Different from other cases which calls sendResponse(),
         * we need to return the ServerTransaction to the caller.
         */
        return sendResponse(event, Response.RINGING, null, null, toTag, null);
    }

    /**
     * @param event the INVITE request event
     */
    public void sendInviteOk(RequestEvent event,
SipProfile localProfile, String sessionDescription,
ServerTransaction inviteTransaction, String externalIp,
int externalPort) throws SipException {
        if (inviteTransaction.getState() != TransactionState.COMPLETED) {
            try {
                ArrayList<Header> customHeaders = new ArrayList<Header>();
                ContactHeader contactHeader =
                    createContactHeader(
                        localProfile, externalIp, externalPort);
                customHeaders.add(contactHeader);

                sendResponse(event, Response.OK, customHeaders,
                    sessionDescription, null, inviteTransaction);
            } catch (ParseException e) {
                /* this.createContactHeader() */
                throw new SipException("sendInviteOk()", e);
}
        } else {
            Log.w(TAG, "sendInviteOk(): transaction already completed");
}
}

public void sendInviteBusyHere(RequestEvent event,
ServerTransaction inviteTransaction) throws SipException {
        if (inviteTransaction.getState() != TransactionState.COMPLETED) {
            sendInviteResponse(event, Response.BUSY_HERE, inviteTransaction);
        } else {
            Log.w(TAG, "sendInviteBusyHere(): transaction already completed");
}
}

    public void sendInviteRequestTerminated(RequestEvent event,
            ServerTransaction inviteTransaction) throws SipException {
        sendInviteResponse(event, Response.REQUEST_TERMINATED,
            inviteTransaction);
}

    public void sendInviteTimeout(RequestEvent event,
            ServerTransaction inviteTransaction) throws SipException {
        sendInviteResponse(event, Response.SERVER_TIMEOUT, inviteTransaction);
}

    public void sendInviteResponse(RequestEvent event, int responseCode,
            ServerTransaction inviteTransaction) throws SipException {
        sendResponse(event, responseCode, null, null, null, inviteTransaction);
}

public void sendResponse(RequestEvent event, int responseCode)
throws SipException {
        sendResponse(event, responseCode, null, null, null, null);
    }

    public void sendResponse(RequestEvent event, int responseCode,
            ArrayList<Header> customHeaders) throws SipException {
        sendResponse(event, responseCode, customHeaders, null, null, null);
    }

    public void sendResponse(RequestEvent event, int responseCode,
            String sessionDescription) throws SipException {
        sendResponse(event, responseCode, null, sessionDescription, null, null);
    }

    private ServerTransaction sendResponse(
            RequestEvent event,
            int responseCode,
            ArrayList<Header> customHeaders,
            String sessionDescription,
            String toTag,
            ServerTransaction serverTransaction)
            throws SipException {
        ServerTransaction transaction = null;
try {
            /* Build the default response message */
Request request = event.getRequest();
            Response response =
                mMessageFactory.createResponse(responseCode, request);

            /* Set Allow header if required to do so. */
            setAllowHeader(event, response);

            if (toTag != null) {
                ToHeader toHeader =
                    (ToHeader)response.getHeader(ToHeader.NAME);
                toHeader.setTag(toTag);
            }
            if (sessionDescription != null) {
                setSdpMessage((Message)response, sessionDescription);
            }
            if (customHeaders != null) {
                for (int i = 0, n = customHeaders.size(); i < n; i++) {
                    Header header = customHeaders.get(i);
                    response.setHeader(header);
                }
            }

if (DEBUG && (!Request.OPTIONS.equals(request.getMethod())
|| DEBUG_PING)) {
Log.d(TAG, "send response: " + response);
}

            if (serverTransaction != null) {
                transaction = serverTransaction;
            } else {
                transaction = getServerTransaction(event);
            }
            transaction.sendResponse(response);
} catch (ParseException e) {
            /* MessageFactory.createResponse() */
            throw new SipException("sendResponse()", e);
        } catch (InvalidArgumentException e) {
            /* ServerTransaction.sendResponse() */
throw new SipException("sendResponse()", e);
}
        return transaction;
}

    private void setAllowHeader(RequestEvent event, Response response)
throws SipException {
        /*
         * We SHOULD set the Allow header depending on method type
         * and response code; See RFC3261 Table 2.
         */
        String method = event.getRequest().getMethod();
        if (method.equals(Request.ACK) || method.equals(Request.CANCEL)) {
            ; /* Not applicable */
        } else {
            try {
                switch (response.getStatusCode()) {
                case Response.METHOD_NOT_ALLOWED:
                    /*
                     * Excerpt from RFC3261, section 8.2.1:
                     *
                     * "The UAS MUST also add an Allow header field to
                     * the 405 (Method Not Allowed) response."
                     */
                    response.setHeader(createAllowHeader());
                    break;
                default:
                    if ((response.getStatusCode() / 100) == 2) {
                        response.setHeader(createAllowHeader());
                    }
                    break;
                }
            } catch (ParseException e) {
                throw new SipException("setAllowHeader()", e);
            }
}
}

    /*-----------------------------------------------------------------*
     * Call-ID handling
     *-----------------------------------------------------------------*/

public static String getCallId(EventObject event) {
if (event == null) return null;
//Synthetic comment -- @@ -532,4 +742,30 @@
private static String getCallId(Dialog dialog) {
return dialog.getCallId().getCallId();
}

    /*-----------------------------------------------------------------*
     * Message body
     *-----------------------------------------------------------------*/

    private ContentTypeHeader createContentTypeHeader(
            String type, String subType) throws SipException {
        try {
            ContentTypeHeader contentTypeHeader =
                mHeaderFactory.createContentTypeHeader(type, subType);
            return contentTypeHeader;
        } catch (ParseException e) {
            throw new SipException("createContentTypeHeader()", e);
        }
    }

    private void setSdpMessage(Message message, String sessionDescription)
            throws SipException {
        try {
            ContentTypeHeader contentTypeHeader =
                createContentTypeHeader("application", "sdp");
            message.setContent(sessionDescription, contentTypeHeader);
        } catch (ParseException e) {
            throw new SipException("setSdpMessage()", e);
        }
    }
}








//Synthetic comment -- diff --git a/voip/java/com/android/server/sip/SipSessionGroup.java b/voip/java/com/android/server/sip/SipSessionGroup.java
//Synthetic comment -- index a4bee36..5282175 100644

//Synthetic comment -- @@ -56,6 +56,7 @@
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.ObjectInUseException;
import javax.sip.PeerUnavailableException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
//Synthetic comment -- @@ -69,6 +70,7 @@
import javax.sip.TransactionState;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.TransactionUnavailableException;
import javax.sip.TransportNotSupportedException;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
//Synthetic comment -- @@ -154,7 +156,16 @@

synchronized void reset(String localIp) throws SipException, IOException {
mLocalIp = localIp;
        if (localIp == null) {
            /*
             * This is the only case that the SipSessionGroup class
             * is being created while the localIp has not determined.
             * Even so, once the localIp has determined by monitoring
             * the connectivity, SipService will call this method with
             * non-null localIp.
             */
            return;
        }

SipProfile myself = mLocalProfile;
SipFactory sipFactory = SipFactory.getInstance();
//Synthetic comment -- @@ -168,17 +179,29 @@
properties.setProperty("javax.sip.OUTBOUND_PROXY", outboundProxy
+ ":" + myself.getPort() + "/" + myself.getProtocol());
}

        SipStack stack;
try {
            stack = mSipStack = sipFactory.createSipStack(properties);
SipProvider provider = stack.createSipProvider(
stack.createListeningPoint(localIp, allocateLocalPort(),
myself.getProtocol()));
provider.addSipListener(this);
mSipHelper = new SipHelper(stack, provider);
        } catch (PeerUnavailableException e) {
            /* SipFactory.createSipStack() */
            throw new SipException("SipSessionGroup constructor", e);
        } catch (ObjectInUseException e) {
            /* SipStack.createSipProvider() */
            throw new SipException("SipSessionGroup constructor", e);
        } catch (TransportNotSupportedException e) {
            /* SipStack.createListeningPoint() */
            throw new IOException(e.getMessage());
} catch (InvalidArgumentException e) {
            /* SipStack.createListeningPoint() */
throw new IOException(e.getMessage());
} catch (TooManyListenersException e) {
            /* SipProvider.addSipListener() */
// must never happen
throw new SipException("SipSessionGroup constructor", e);
}
//Synthetic comment -- @@ -1106,7 +1129,7 @@
if (evt instanceof MakeCallCommand) {
// answer call
mState = SipSession.State.INCOMING_CALL_ANSWERING;
                mSipHelper.sendInviteOk(mInviteReceived,
mLocalProfile,
((MakeCallCommand) evt).getSessionDescription(),
mServerTransaction,
//Synthetic comment -- @@ -1119,10 +1142,13 @@
endCallNormally();
return true;
} else if (isRequestEvent(Request.CANCEL, evt)) {
                /* Send 200 response for CANCEL */
RequestEvent event = (RequestEvent) evt;
mSipHelper.sendResponse(event, Response.OK);

                /* Send 487 response for INVITE */
mSipHelper.sendInviteRequestTerminated(
                        mInviteReceived, mServerTransaction);
endCallNormally();
return true;
}
//Synthetic comment -- @@ -1151,66 +1177,13 @@

private boolean outgoingCall(EventObject evt) throws SipException {
if (expectResponse(Request.INVITE, evt)) {
                return handleInviteResponse(evt);
} else if (END_CALL == evt) {
// RFC says that UA should not send out cancel when no
// response comes back yet. We are cheating for not checking
// response.
mState = SipSession.State.OUTGOING_CALL_CANCELING;
                mSipHelper.sendCancel(mClientTransaction, 0/*statusCode*/);
startSessionTimer(CANCEL_CALL_TIMER);
return true;
} else if (isRequestEvent(Request.INVITE, evt)) {
//Synthetic comment -- @@ -1300,7 +1273,7 @@
if (END_CALL == evt) {
// rfc3261#section-15.1.1
mState = SipSession.State.ENDING_CALL;
                mSipHelper.sendBye(mDialog, 0/*statusCode*/);
mProxy.onCallEnded(this);
startSessionTimer(END_CALL_TIMER);
return true;
//Synthetic comment -- @@ -1353,6 +1326,79 @@
return false;
}

        private boolean handleInviteResponse(EventObject evt)
                throws SipException {
            ResponseEvent event = (ResponseEvent)evt;
            Response response = event.getResponse();

            int statusCode = response.getStatusCode();
            switch (statusCode/100) {
            case 1:
                // feedback any provisional responses (except TRYING) as
                // ring back for better UX
                if (statusCode == Response.TRYING) {
                    /* Nothing to do at UAC */
                    break;
                }
                if (mState == SipSession.State.OUTGOING_CALL) {
                    mState = SipSession.State.OUTGOING_CALL_RING_BACK;
                    cancelSessionTimer();
                    mProxy.onRingingBack(this);
                }
                break;
            case 2:
                /*
                 * RFC5359 section 2.4:
                 * If we got a 2xx response from refer target, send NOTIFY
                 * to the referrer if the subscription created by REFER
                 * still exists.
                 */
                if (mReferSession != null) {
                    mSipHelper.sendReferNotify(mReferSession.mDialog,
                            getResponseString(Response.OK));
                    // since we don't need to remember the session anymore.
                    mReferSession = null;
                }

                /*
                 * RFC3261 section 13.2.2.4:
                 * Every 2xx response must be ack'ed.
                 */
                mSipHelper.sendInviteAck(event, mDialog);

                mPeerSessionDescription = extractContent(response);
                establishCall(true);
                break;
            case 3:
                // TODO: handle 3xx (redirect)
                break;
            default:
                switch (statusCode) {
                case Response.UNAUTHORIZED:
                case Response.PROXY_AUTHENTICATION_REQUIRED:
                    if (handleAuthentication(event)) {
                        addSipSession(this);
                    }
                    /* onError() has called upon Authenticatoin failure */
                    break;
                case Response.REQUEST_PENDING:
                    // TODO:
                    // rfc3261#section-14.1; re-schedule invite
                    /* FALLTHROUGH *//* Treat as an error, for now */
                default:
                    // error: an ack is sent automatically by the stack
                    if (mReferSession != null) {
                        mSipHelper.sendReferNotify(mReferSession.mDialog,
                                getResponseString(Response.SERVICE_UNAVAILABLE));
                    }
                    onError(response);
                    break;
                }
                break;
            }
            return true;
        }

// timeout in seconds
private void startSessionTimer(int timeout) {
if (timeout > 0) {
//Synthetic comment -- @@ -1420,10 +1466,10 @@
break;
default:
endCallOnError(errorCode, message);
                    break;
}
}

private void onError(Throwable exception) {
exception = getRootCause(exception);
onError(getErrorCode(exception), exception.toString());








//Synthetic comment -- diff --git a/voip/java/com/android/server/sip/SipWarning.java b/voip/java/com/android/server/sip/SipWarning.java
new file mode 100644
//Synthetic comment -- index 0000000..3d2c07f

//Synthetic comment -- @@ -0,0 +1,77 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.server.sip;

/**
 * This class defines a collection of waning codes and corresponding
 * descriptions to be carried by Warning header.
 */
public class SipWarning {
    /*
     * SIP warning codes registered to IANA.
     * [cf] http://www.iana.org/assignments/sip-parameters
     */
    public static final int INCOMPATIBLE_NETWORK_PROTOCOL = 300;
    public static final int INCOMPATIBLE_NETWORK_ADDRESS_FORMATS = 301;
    public static final int INCOMPATIBLE_TRANSPORT_PROTOCOL = 302;
    public static final int INCOMPATIBLE_BANDWIDTH_UNITS = 303;
    public static final int MEDIA_TYPE_NOT_AVAILABLE = 304;
    public static final int INCOMPATIBLE_MEDIA_FORMAT = 305;
    public static final int ATTRIBUTE_NOT_UNDERSTOOD = 306;
    public static final int SESSION_DESCRIPTION_PARAMETER_NOT_UNDERSTOOD = 307;
    public static final int MULTICAST_NOT_AVAILABLE = 330;
    public static final int UNICAST_NOT_AVAILABLE = 331;
    public static final int INSUFFICIENT_BANDWIDTH = 370;
    public static final int SIPS_NOT_ALLOWED = 380;
    public static final int SIPS_REQUIRED = 381;
    public static final int MISCELLANEOUS_WARNING = 399;

    public static String toString(int code) {
        switch (code) {
        case INCOMPATIBLE_NETWORK_PROTOCOL:
            return "Incompatible network protocol";
        case INCOMPATIBLE_NETWORK_ADDRESS_FORMATS:
            return "Incompatible network address formats";
        case INCOMPATIBLE_TRANSPORT_PROTOCOL:
            return "Incompatible transport protocol";
        case INCOMPATIBLE_BANDWIDTH_UNITS:
            return "Incompatible bandwidth units";
        case MEDIA_TYPE_NOT_AVAILABLE:
            return "Media type not available";
        case INCOMPATIBLE_MEDIA_FORMAT:
            return "Incompatible media format";
        case ATTRIBUTE_NOT_UNDERSTOOD:
            return "Attribute not understood";
        case SESSION_DESCRIPTION_PARAMETER_NOT_UNDERSTOOD:
            return "Session description parameter not understood";
        case MULTICAST_NOT_AVAILABLE:
            return "Multicast not available";
        case UNICAST_NOT_AVAILABLE:
            return "Unicast not available";
        case INSUFFICIENT_BANDWIDTH:
            return "Insufficient bandwidth";
        case SIPS_NOT_ALLOWED:
            return "SIPS Not Allowed";
        case SIPS_REQUIRED:
            return "SIPS Required";
        case MISCELLANEOUS_WARNING:
            return null; /* User should specify arbitrary information */
        default:
            return "Unknown warning code";
        }
    }
}







