/*SIP-API: Add support for 3xx redirection.

If the UAC gets 3xx response for outgoing INVITE request, try
redirection cycle until the given contact list being exhausted,
or succeed to make a redirected call.

Change-Id:Ic9722cf646b8ba9e1694d552bc69ce6e73990577Signed-off-by: Masahiko Endo <masahiko.endo@gmail.com>*/




//Synthetic comment -- diff --git a/voip/java/com/android/server/sip/SipHelper.java b/voip/java/com/android/server/sip/SipHelper.java
//Synthetic comment -- index 46aca3f5..e6c79bf 100644

//Synthetic comment -- @@ -31,12 +31,15 @@
import java.text.ParseException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogState;
import javax.sip.DialogTerminatedEvent;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
//Synthetic comment -- @@ -48,6 +51,7 @@
import javax.sip.SipFactory;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TimeoutEvent;
import javax.sip.Transaction;
import javax.sip.TransactionAlreadyExistsException;
import javax.sip.TransactionDoesNotExistException;
//Synthetic comment -- @@ -58,6 +62,7 @@
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.AllowHeader;
import javax.sip.header.AuthorizationHeader;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
//Synthetic comment -- @@ -66,6 +71,7 @@
import javax.sip.header.Header;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ProxyAuthorizationHeader;
import javax.sip.header.ReasonHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.UserAgentHeader;
//Synthetic comment -- @@ -110,6 +116,11 @@
* Create various headers
*-----------------------------------------------------------------*/

    public Header createGenericHeader(String name, String value)
            throws ParseException {
        return mHeaderFactory.createHeader(name, value);
    }

private AllowHeader createAllowHeader()
throws ParseException {
String methods = SipFeature.getAllowedMethods();
//Synthetic comment -- @@ -331,6 +342,114 @@
}
}

    public ClientTransaction sendRedirectedInvite(
            SipProfile peerProfile,
            String sessionDescription,
            EventObject evt,
            ArrayList<Header> customHeaders) throws SipException {
        /*
         * There are some cases that trigger redirection process.
         */
        ClientTransaction tid = null;
        if (evt instanceof ResponseEvent) {
            tid = ((ResponseEvent)evt).getClientTransaction();
        } else if (evt instanceof TimeoutEvent) {
            tid = ((TransactionTerminatedEvent)evt).getClientTransaction();
        }
        if (tid == null) {
            throw new SipException("Original transaction is unavailable");
        }

        /* Get original request from the transaction. */
        SIPRequest prevRequest = (SIPRequest)tid.getRequest();
        Request nextRequest = null;

        /*
         * Some of code in this method have copied from:
         *     javax.sip.SipProvider.
         *     getNewClientTransaction(javax.sip.message.Request)
         */
        if (prevRequest.getToTag() != null
        ||  tid.getDialog() == null
        ||  tid.getDialog().getState() != DialogState.CONFIRMED)  {
            /* Reuse original request as a template */
            if (DEBUG) {
                Log.d(TAG, "sendRedirectedInvite: Reuse original request");
            }
            nextRequest = (Request)prevRequest.clone();

            /*
             * TODO:
             * If previous INVITE request has built with custom headers,
             * those should be removed before adding new ones, so that
             * not to send garbage headers unintentionally.
             */
        } else {
            if (DEBUG) {
                Log.d(TAG, "sendRedirectedInvite: Going to recreate request");
            }
            nextRequest = tid.getDialog().createRequest(Request.INVITE);
            Iterator<String> headerNames = prevRequest.getHeaderNames();
            while (headerNames.hasNext()) {
                String headerName = headerNames.next();
                if (nextRequest.getHeader(headerName) != null) {
                    ListIterator<Header> iterator =
                        nextRequest.getHeaders(headerName);
                    while (iterator.hasNext()) {
                        nextRequest.addHeader(iterator.next());
                    }
                }
            }
            if (sessionDescription != null) {
                setSdpMessage((Message)nextRequest, sessionDescription);
            }
        }

        /* Reinitialize branches */
        ViaHeader viaHeader =
            (ViaHeader) nextRequest.getHeader(ViaHeader.NAME);
        viaHeader.removeParameter("branch");

        /* Reinitialize authorization status. */
        nextRequest.removeHeader(AuthorizationHeader.NAME);
        nextRequest.removeHeader(ProxyAuthorizationHeader.NAME);

        /* Replace the RURI */
        nextRequest.setRequestURI(peerProfile.getUri());

        /* Add custom headers passed by 3xx response Contact, if any. */
        if (customHeaders != null) {
            for (int i = 0, n = customHeaders.size(); i < n; i++) {
                /* Replace the existing ones. */
                nextRequest.setHeader(customHeaders.get(i));
            }
        }

        /* Increment Cseq value */
        CSeqHeader cSeq =
            (CSeqHeader) nextRequest.getHeader(CSeqHeader.NAME);
        try {
            cSeq.setSeqNumber(cSeq.getSeqNumber() + 1l);
        } catch (InvalidArgumentException ex1) {
            throw new SipException("Invalid CSeq -- could not increment : "
                    + cSeq.getSeqNumber());
        }

        if (DEBUG) {
            Log.d(TAG, "Going to send Redirected-INVITE: " + nextRequest);
        }

        try {
            ClientTransaction clientTransaction =
                mSipProvider.getNewClientTransaction(nextRequest);
            clientTransaction.sendRequest();
            return clientTransaction;
        } catch (TransactionUnavailableException e) {
            /* Provider.getNewClientTransaction() */
            throw new SipException("sendRedirectedInvite()", e);
        }
    }

public ClientTransaction sendReinvite(Dialog dialog,
String sessionDescription) throws SipException {
try {








//Synthetic comment -- diff --git a/voip/java/com/android/server/sip/SipRedirection.java b/voip/java/com/android/server/sip/SipRedirection.java
new file mode 100644
//Synthetic comment -- index 0000000..4a51947

//Synthetic comment -- @@ -0,0 +1,594 @@
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

import gov.nist.javax.sip.address.SipUri;
import gov.nist.javax.sip.header.Contact;
import gov.nist.javax.sip.header.ContactList;
import gov.nist.javax.sip.header.SIPHeaderNames;
import gov.nist.javax.sip.message.SIPMessage;
import gov.nist.javax.sip.message.SIPResponse;

import android.net.sip.ISipSession;
import android.net.sip.ISipSessionListener;
import android.net.sip.SipErrorCode;
import android.net.sip.SipProfile;
import android.net.sip.SipSession;
import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.Properties;

import javax.sip.ClientTransaction;
import javax.sip.ListeningPoint;
import javax.sip.ResponseEvent;
import javax.sip.SipException;
import javax.sip.TimeoutEvent;
import javax.sip.address.SipURI;
import javax.sip.header.ContactHeader;
import javax.sip.header.Header;
import javax.sip.header.Parameters;
import javax.sip.message.Request;
import javax.sip.message.Response;

/**
 * This class manages 3xx responses for UAC.
 */
class SipRedirection {
    private static final String TAG = "SipRedirection";
    private static final boolean DEBUG = true;

    private SipSessionGroup mGroup;
    private SipSessionGroup.SipSessionImpl mSession;
    private SipHelper mSipHelper;
    private String mLocalSessionDescription;
    private int mCallTimeout;

    /*
     * To prevent redirection loop, keep track of RURI history.
     */
    private ArrayList<SipURI> mRuriHistory = new ArrayList<SipURI>();

    /*
     * For redirection loop control, keep a (subset) copy of given
     * Contact headers of last received 3xx response.
     */
    private ContactList mContactList = new ContactList();

    /*-----------------------------------------------------------------*
     * constructor                                                     *
     *-----------------------------------------------------------------*/

    public SipRedirection(
            SipSessionGroup group,
            SipSessionGroup.SipSessionImpl session,
            SipHelper helper) {
        mGroup = group;
        mSession = session;
        mSipHelper = helper;
    }

    /*-----------------------------------------------------------------*
     * public methods                                                  *
     *-----------------------------------------------------------------*/

    public void init(
            SipProfile peerProfile, String sessionDescription, int timeout) {
        /* Keep last INVITE parameters for the next INVITE. */
        addRuriHistory(peerProfile.getUri());
        mLocalSessionDescription = sessionDescription;
        mCallTimeout = timeout;
        return;
    }

    public void reset() {
        mContactList.clear();
        resetRuriHistory();
        return;
    }

    public void handleRedirect(EventObject evt) {
        ResponseEvent event = (ResponseEvent) evt;
        Response response = event.getResponse();
        int statusCode = response.getStatusCode();

        switch (statusCode) {
        case Response.MOVED_PERMANENTLY:
        case Response.MOVED_TEMPORARILY:
        case Response.USE_PROXY:
            break;
        case Response.MULTIPLE_CHOICES:
        case Response.ALTERNATIVE_SERVICE:
        default:
            /* Cannot proceed without user intervention. *//* XXX */
            resetRuriHistory();
            mSession.onError(response);
            return;
        }

        ContactList contactList =
                ((SIPResponse)response).getContactHeaders();
        if (contactList == null) {
            /*
             * A proxy might have deleted the Contact headers
             * for privacy reasons.
             */
            mSession.onError(SipErrorCode.SERVER_ERROR,
                "Missing Contact headers on 3xx response");
            return;
        }
        extractAvailableContacts(contactList);

        if (! tryNextCandidate(evt)) {
            mSession.onError(SipErrorCode.SERVER_ERROR,
                "No available Contact on 3xx response");
        }
        return;
    }

    public boolean tryNextCandidate(EventObject evt) {
        boolean processed = false;

        /* Cleanup previous transaction, etc. */
        mSession.reset();

        for (ContactHeader hdr = getNextContact(); hdr != null; ) {
            if (tryRedirect(evt, hdr)) {
                processed = true;
                break;
            }
        }
        if (!processed) {
            if (DEBUG) {
                Log.d(TAG, "Contact list has exhausted, give up.");
            }
        }
        return processed;
    }

    /*-----------------------------------------------------------------*
     * private methods                                                 *
     *-----------------------------------------------------------------*/

    private void addRuriHistory(SipURI uri) {
        if (DEBUG) {
            Log.d(TAG, "addRuriHistory: URI=" + uri.toString());
        }
        mRuriHistory.add(uri);
        return;
    }

    private void resetRuriHistory() {
        if (DEBUG) {
            Log.d(TAG, "resetRuriHistory");
        }
        mRuriHistory.clear();
        return;
    }

    private SipURI getOriginalRuri() {
        return (mRuriHistory.isEmpty() ? null : mRuriHistory.get(0));
    }

    private boolean lookupRuriHistory(SipURI probe) {
        boolean found = false;
        for (int i = 0, n = mRuriHistory.size(); i < n; i++) {
            SipUri sipUri = (SipUri)(mRuriHistory.get(i));
            if (sipUri.equals(probe)) {
                found = true;
                break;
            }
        }
        return found;
    }

    private void extractAvailableContacts(ContactList contactList) {
        /*
         * Contact list might have created by previous 3xx
         * response; reset it before processing.
         */
        mContactList.clear();

        /*
         * Setup my own Contact list by extracting acceptable
         * entries from the given list.
         *
         * NB:
         * Unfortunately, here we cannot simply call method
         * "contactList.clone()", due to possibility of
         * ClassCastException. The exception occures if any
         * element in given Contact list contains non-SIP
         * scheme such like "tel", "mailto" etc.
         *
        mContactList = (ContactList)contactList.clone();
         ***/

        SipProfile localProfile = mSession.getLocalProfile();
        String stackTransport = localProfile.getProtocol();
        SipURI myUri = localProfile.getUri();
        SipURI originalRuri = getOriginalRuri();

        for (int i = 0, n = contactList.size(); i < n; i++) {
            ContactHeader hdr = contactList.get(i);

            /*
             * Insane server might set identical Contact entries
             * multiple times in the list.
             */
            if (lookupContact(hdr)) {
                if (DEBUG) {
                    Log.d(TAG, "REJECT: duplicated contact?");
                }
                continue;
            }

            /*
             * Exclude this entry if it is a wildcard.
             * For 3xx responses, this case should never happen.
             */
            if (hdr.isWildCard()) {
                if (DEBUG) {
                    Log.d(TAG, "REJECT: wildcard contact?");
                }
                continue;
            }

            /*
             * Exclude this entry if it has non-SIP scheme.
             * Otherwise, we will see ClassCastException.
             */
            if (! hdr.getAddress().isSIPAddress()) {
                if (DEBUG) {
                    Log.d(TAG, "REJECT: non-SIP scheme: URI=" +
                            hdr.getAddress().getURI().toString());
                }
                continue;
            }
            SipURI uri = (SipURI)hdr.getAddress().getURI();

            /*
             * Exclude this entry if it has different transport
             * parameter value against current local profile.
             * Otherwise, we will see NullPointerException within
             * "gov.nist.javax.sip.
             *      SipProviderImpl.getNewClientTransaction()".
             */
            String transport = (uri.hasTransport() ?
                uri.getTransportParam() : getDefaultTransportParam(uri));

            if (! stackTransport.equalsIgnoreCase(transport)) {
                if (DEBUG) {
                    Log.d(TAG, "REJECT: transport mismatch: stack=" + stackTransport + ", uri=" + transport);
                }
                continue;
            }

            /*
             * According to RFC3261, section 8.1.3.4, we SHOULD
             * inform to user if the original RURI is SIPS URI
             * and going to recurse to a non-SIPS URI.
             *
             * Since we have no way to inform this situation to
             * user, simply avoid mixing SIP and SIPS in the
             * target list, for now...
             */
            if (originalRuri.isSecure() != uri.isSecure()) {
                if (DEBUG) {
                    Log.d(TAG, "REJECT: security mismatch: org=" + originalRuri.getScheme() + ", uri=" + uri.getScheme());
                }
                continue;
            }

            /*
             * Excerpt from RFC3261, section 19.1.5:
             *
             * "If the URI contains a method parameter, its value MUST
             * be used as the method of the request."
             */
            String method = uri.getMethodParam();
            if (method != null && !method.equals("")) {
                if (! isAcceptableMethod(method)) {
                    Log.d(TAG, "REJECT: unsupported method: " + method);
                    continue;
                }
            }

            /*
             * For now, we don't use any preferences among Contact
             * entries being stored here; q-value will be ignored.
             * Simply follow the appearance order in original list.
             */
            ContactHeader clonedHdr = (ContactHeader)hdr.clone();
            mContactList.add((Contact)clonedHdr);
            if (DEBUG) {
                Log.d(TAG, "ACCEPT: uri=" + uri.toString());
            }
        }
        return;
    }

    private boolean lookupContact(ContactHeader probe) {
        boolean found = false;
        String probeString = probe.toString().toLowerCase();

        for (int i = 0, n = mContactList.size(); i < n; i++) {
            ContactHeader hdr = mContactList.get(i);

            /*** Object comparison seems not to work...
            if (hdr.equals(probe)) {
                found = true;
                break;
            }
            ***/
            /* Try string comparison as an alternative. */
            if (hdr.toString().toLowerCase().equals(probeString)) {
                found = true;
                break;
            }
        }
        return found;
    }

    private ContactHeader getNextContact() {
        ContactHeader found = null;
        for (int i = 0, n = mContactList.size(); i < n; i++) {
            ContactHeader hdr = mContactList.get(i);

            /*
             * SIP-URI specified in Contact header has special syntax.
             * Here we cannot simply use the following method:
             *
             *   SipURI uri = (SipURI)hdr.getAddress().getURI();
             *
             * Instead, we need to consider the modified version of URI
             * to be set as the RURI for the next outgoing message.
             */
            SipURI uri = getRedirectedUri(hdr);

            /* Avoid redirection loop */
            if (! lookupRuriHistory(uri)) {
                found = hdr;
                uri = null; /* make the GC target */
                break;
            }
            uri = null; /* make the GC target */
        }
        return found;
    }

    private SipURI getRedirectedUri(ContactHeader hdr) {
        SipURI uri = (SipURI)hdr.getAddress().getURI();

        /*
         * Excerpt from RFC3261, section 8.1.3.4:
         *
         * "In order to create a request based on a contact address in a 3xx
         * response, a UAC MUST copy the entire URI from the target set into
         * the Request-URI, except for the "method-param" and "header" URI
         * parameters."
         *
         * NB:
         * We also exclude contact-parameters (such like "q", "expires")
         * and generic parameters (such like "isfocus") to formulate the
         * RURI here.
         */
        SipUri sipUri = (SipUri)(((SipUri)uri).clone());
        sipUri.removeMethod();
        sipUri.clearQheaders();

        return (SipURI)sipUri;
    }

    private boolean tryRedirect(EventObject evt, ContactHeader hdr) {
        boolean processed = false;
        SipURI uri = (SipURI)hdr.getAddress().getURI();
        if (DEBUG) {
            Log.d(TAG, "tryRedirect: next=" + uri.toString());
        }

        /*
         * Currently, redirection is supported only for INVITE;
         * we treat timeout case as an INVITE request failure.
         */
        if ((evt instanceof TimeoutEvent)
        ||  (mGroup.expectResponse(Request.INVITE, evt))) {
            String method = uri.getMethodParam();
            if (method == null
            ||  method.equalsIgnoreCase(Request.INVITE)) {
                processed = tryRedirectedInvite(evt, hdr);
            } else {
                Log.w(TAG, "tryRedirect: Unsupported method: " + method);
            }
        } else if (mGroup.expectResponse(Request.REGISTER, evt)) {
            Log.w(TAG, "REGISTER redirection not yet supported");
        } else if (mGroup.expectResponse(Request.OPTIONS, evt)) {
            Log.w(TAG, "OPTIONS redirection not yet supported");
        } else {
            Log.w(TAG, "tryRedirect: Unexpected case?");
        }
        return processed;
    }

    private boolean tryRedirectedInvite(EventObject evt, ContactHeader hdr) {
        boolean processed = false;
        try {
            /* Replace the peer profile using redirected URI */
            SipProfile profile = buildPeerProfile(hdr);
            if (DEBUG) {
                Log.d(TAG, "tryRedirectedInvite: peer=" + profile.getUri());
            }

            /* There may exist "headers" parameter in this Contact */
            ArrayList<Header> hlist = extractHeaderParams(hdr);

            /*
             * Excerpts from RFC3261, section 8.1.3.4:
             *
             * "It is RECOMMENDED that the UAC reuse the same To, From,
             * and Call-ID used in the original redirected request,
             * but the UAC MAY also choose to update the Call-ID header
             * field value for new requests, for example.
             */
            boolean updateCallId = false; /* depends on policy *//* XXX */
            if (updateCallId) {
                mSession.makeCall(profile,
                    mLocalSessionDescription, mCallTimeout);
            } else {
                mSession.makeRedirectedCall(profile,
                    mLocalSessionDescription, mCallTimeout, evt, hlist);
            }

            /*
             * Now that a redirected INVITE will be sent to network,
             * flush this Contact from the local cache.
             * And register the RURI to prevent redirection loop.
             */
            mContactList.remove(hdr);
            addRuriHistory(profile.getUri());

            /* Ok, let's see what happens... */
            processed = true;
        } catch (ParseException e) {
            Log.w(TAG, "tryRedirectedInvite", e);
        }
        return processed;
    }

    private SipProfile buildPeerProfile(ContactHeader hdr)
            throws ParseException {
        SipURI uri = getRedirectedUri(hdr);
        String redirectedUriString = uri.toString();
        uri = null; /* make the GC target */

        SipProfile.Builder builder =
            new SipProfile.Builder(redirectedUriString);

        builder.setSendKeepAlive(false);
        builder.setAutoRegistration(false);

        if (hdr.getAddress().hasDisplayName()) {
            builder.setDisplayName(hdr.getAddress().getDisplayName());
        }
        return builder.build();
    }

    private ArrayList<Header> extractHeaderParams(ContactHeader hdr) {
        ArrayList<Header> hlist = null;
        SipURI uri = (SipURI)hdr.getAddress().getURI();

        Iterator itr = uri.getHeaderNames();
        if (itr.hasNext()) {
            hlist = new ArrayList<Header>();

            while (itr.hasNext()) {
                String name = (String)itr.next();
                String value = uri.getHeader(name);

                if (isUnacceptableHeader(name)) {
                    Log.w(TAG, "We SHOULD NOT honor this header: " + name);
                    continue;
                }
                try {
                    hlist.add(mSipHelper.createGenericHeader(name, value));
                } catch (ParseException e) {
                    Log.w(TAG, "mSipHelper.createGenericHeader: " + name, e);
                }
            }
            if (hlist.isEmpty()) {
                hlist = null;
            }
        }
        return hlist;
    }

    private final String getDefaultTransportParam(SipURI uri) {
         /*
          * The default transport is scheme dependent.
          * [cf] RFC3261, section 19.1.1, Table 1
          */
         return (uri.isSecure() ? ListeningPoint.TCP : ListeningPoint.UDP);
    }

    private boolean isAcceptableMethod(String name) {
        final String[] whitelist = {
            Request.INVITE,
        };

        boolean match = false;
        for (int i = 0, n = whitelist.length; i < n; i++) {
            if (name.equalsIgnoreCase(whitelist[i])) {
                match = true;
                break;
            }
        }
        return match;
    }

    private boolean isUnacceptableHeader(String name) {
        final String[] blacklist = {
            /*
             * Excerpts from RFC3261, section 19.1.5:
             *
             * "An implementation SHOULD NOT honor these obviously
             * dangerous header fields: From, Call-ID, CSeq, Via,
             * and Record-Route."
             */
            SIPHeaderNames.FROM,
            SIPHeaderNames.CALL_ID,
            SIPHeaderNames.CSEQ,
            SIPHeaderNames.VIA,
            SIPHeaderNames.RECORD_ROUTE,

            /*
             * "An implementation SHOULD NOT honor any requested Route
             * header field values in order to not be used as an unwitting
             * agent in malicious attacks."
             */
            SIPHeaderNames.ROUTE,

            /*
             * "An implementation SHOULD NOT honor requests to include
             * header fields that may cause it to falsely advertise its
             * location or capabilities.
             * These include: Accept, Accept-Encoding, Accept-Language,
             * Allow, Contact (in its dialog usage), Organization,
             * Supported, and User-Agent."
             */
            SIPHeaderNames.ACCEPT,
            SIPHeaderNames.ACCEPT_ENCODING,
            SIPHeaderNames.ACCEPT_LANGUAGE,
            SIPHeaderNames.ALLOW,
            SIPHeaderNames.CONTACT,
            SIPHeaderNames.ORGANIZATION,
            SIPHeaderNames.SUPPORTED,
            SIPHeaderNames.USER_AGENT,
        };

        boolean match = false;
        for (int i = 0, n = blacklist.length; i < n; i++) {
            if (name.equalsIgnoreCase(blacklist[i])) {
                match = true;
                break;
            }
        }
        return match;
    }
}








//Synthetic comment -- diff --git a/voip/java/com/android/server/sip/SipSessionGroup.java b/voip/java/com/android/server/sip/SipSessionGroup.java
//Synthetic comment -- index 5282175..35710a9 100644

//Synthetic comment -- @@ -42,6 +42,7 @@
import java.net.DatagramSocket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
//Synthetic comment -- @@ -65,6 +66,7 @@
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.Timeout;
import javax.sip.TimeoutEvent;
import javax.sip.Transaction;
import javax.sip.TransactionState;
//Synthetic comment -- @@ -77,6 +79,7 @@
import javax.sip.header.ContactHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.Header;
import javax.sip.header.HeaderAddress;
import javax.sip.header.MinExpiresHeader;
import javax.sip.header.ReferToHeader;
//Synthetic comment -- @@ -549,6 +552,7 @@
boolean mInCall;
SessionTimer mSessionTimer;
int mAuthenticationRetryCount;
        SipRedirection mSipRedirection;

private KeepAliveProcess mKeepAliveProcess;

//Synthetic comment -- @@ -579,7 +583,24 @@

private void timeout() {
synchronized (SipSessionGroup.this) {
                    /*
                     * For unified event handling, raise a pseudo timeout
                     * event as if it has notified from the SIP stack.
                     */
                    TimeoutEvent event = null;
                    if (mClientTransaction != null) {
                        event = new TimeoutEvent(this/*placeholder*/,
                                    mClientTransaction, Timeout.TRANSACTION);
                    } else if (mServerTransaction != null) {
                        event = new TimeoutEvent(this/*placeholder*/,
                                    mServerTransaction, Timeout.TRANSACTION);
                    }
                    if (event != null) {
                        processTimeout(event);
                        event = null;
                    } else {
                        onError(SipErrorCode.TIME_OUT, "Session timed out!");
                    }
}
}

//Synthetic comment -- @@ -594,13 +615,18 @@

public SipSessionImpl(ISipSessionListener listener) {
setListener(listener);

            /* Prepare for the redirection handler for this session. */
            mSipRedirection =
                new SipRedirection(
                    SipSessionGroup.this, SipSessionImpl.this, mSipHelper);
}

SipSessionImpl duplicate() {
return new SipSessionImpl(mProxy.getListener());
}

        public void reset() {
mInCall = false;
removeSipSession(this);
mPeerProfile = null;
//Synthetic comment -- @@ -691,10 +717,25 @@

public void makeCall(SipProfile peerProfile, String sessionDescription,
int timeout) {
            /* Keep parameters to be prepared for redirection */
            mSipRedirection.init(peerProfile, sessionDescription, timeout);

doCommandAsync(new MakeCallCommand(peerProfile, sessionDescription,
timeout));
}

        public void makeRedirectedCall(
                SipProfile peerProfile,
                String sessionDescription,
                int timeout,
                EventObject evt,
                ArrayList<Header> customHeaders) {
            doCommandAsync(
                new MakeCallCommand(
                    peerProfile, sessionDescription, timeout,
                        evt, customHeaders));
        }

public void answerCall(String sessionDescription, int timeout) {
synchronized (SipSessionGroup.this) {
if (mPeerProfile == null) return;
//Synthetic comment -- @@ -898,8 +939,14 @@
break;
case SipSession.State.INCOMING_CALL:
case SipSession.State.INCOMING_CALL_ANSWERING:
                    onError(SipErrorCode.TIME_OUT, event.toString());
                    break;
case SipSession.State.OUTGOING_CALL:
case SipSession.State.OUTGOING_CALL_CANCELING:
                    if (mSipRedirection.tryNextCandidate((EventObject)event)) {
                        /* New INVITE has sent to the new destination */
                        break;
                    }
onError(SipErrorCode.TIME_OUT, event.toString());
break;

//Synthetic comment -- @@ -1091,13 +1138,27 @@
mState = SipSession.State.OUTGOING_CALL;
MakeCallCommand cmd = (MakeCallCommand) evt;
mPeerProfile = cmd.getPeerProfile();
                String offer = cmd.getSessionDescription();
                ArrayList<Header> customHeaders = cmd.getCustomHeaders();

                EventObject savedEvent = cmd.getEvent();
                if (savedEvent != null) {
                    /*
                     * This is the case for redirected INVITE, triggered
                     * by ResponseEvent or TimeoutEvent.
                     */
                    mClientTransaction = mSipHelper.sendRedirectedInvite(
                        mPeerProfile, offer, savedEvent, customHeaders);
                } else {
                    /* This is the case for initial INVITE */
                    if (mReferSession != null) {
                        mSipHelper.sendReferNotify(mReferSession.mDialog,
                                getResponseString(Response.TRYING));
                    }
                    mClientTransaction = mSipHelper.sendInvite(
                        mLocalProfile, mPeerProfile, offer,
generateTag(), mReferredBy, mReplaces);
                }
mDialog = mClientTransaction.getDialog();
addSipSession(this);
startSessionTimer(cmd.getTimeout());
//Synthetic comment -- @@ -1185,6 +1246,7 @@
mState = SipSession.State.OUTGOING_CALL_CANCELING;
mSipHelper.sendCancel(mClientTransaction, 0/*statusCode*/);
startSessionTimer(CANCEL_CALL_TIMER);
                mSipRedirection.reset();
return true;
} else if (isRequestEvent(Request.INVITE, evt)) {
// Call self? Send BUSY HERE so server may redirect the call to
//Synthetic comment -- @@ -1192,6 +1254,7 @@
RequestEvent event = (RequestEvent) evt;
mSipHelper.sendInviteBusyHere(event,
event.getServerTransaction());
                mSipRedirection.reset();
return true;
}
return false;
//Synthetic comment -- @@ -1370,7 +1433,7 @@
establishCall(true);
break;
case 3:
                mSipRedirection.handleRedirect(evt);
break;
default:
switch (statusCode) {
//Synthetic comment -- @@ -1387,6 +1450,10 @@
/* FALLTHROUGH *//* Treat as an error, for now */
default:
// error: an ack is sent automatically by the stack
                    if (mSipRedirection.tryNextCandidate(evt)) {
                        /* New INVITE has sent to the new destination */
                        break;
                    }
if (mReferSession != null) {
mSipHelper.sendReferNotify(mReferSession.mDialog,
getResponseString(Response.SERVICE_UNAVAILABLE));
//Synthetic comment -- @@ -1457,7 +1524,7 @@
mProxy.onCallBusy(this);
}

        public void onError(int errorCode, String message) {
cancelSessionTimer();
switch (mState) {
case SipSession.State.REGISTERING:
//Synthetic comment -- @@ -1465,19 +1532,21 @@
onRegistrationFailed(errorCode, message);
break;
default:
                    mSipRedirection.reset();
endCallOnError(errorCode, message);
break;
}
}

        public void onError(Throwable exception) {
exception = getRootCause(exception);
onError(getErrorCode(exception), exception.toString());
}

        public void onError(Response response) {
int statusCode = response.getStatusCode();
if (!mInCall && (statusCode == Response.BUSY_HERE)) {
                mSipRedirection.reset();
endCallOnBusy();
} else {
onError(getErrorCode(statusCode), createErrorMessage(response));
//Synthetic comment -- @@ -1738,7 +1807,7 @@
* @return true if the event is a response event and the CSeqHeader method
* match the given arguments; false otherwise
*/
    public boolean expectResponse(
String expectedMethod, EventObject evt) {
if (evt instanceof ResponseEvent) {
ResponseEvent event = (ResponseEvent) evt;
//Synthetic comment -- @@ -1752,7 +1821,7 @@
* @return true if the event is a response event and the response code and
*      CSeqHeader method match the given arguments; false otherwise
*/
    public boolean expectResponse(
int responseCode, String expectedMethod, EventObject evt) {
if (evt instanceof ResponseEvent) {
ResponseEvent event = (ResponseEvent) evt;
//Synthetic comment -- @@ -1843,6 +1912,8 @@
private class MakeCallCommand extends EventObject {
private String mSessionDescription;
private int mTimeout; // in seconds
        private EventObject mEvent; /* for redirection */
        private ArrayList<Header> mHeaders; /* for custom headers */

public MakeCallCommand(SipProfile peerProfile,
String sessionDescription) {
//Synthetic comment -- @@ -1851,9 +1922,17 @@

public MakeCallCommand(SipProfile peerProfile,
String sessionDescription, int timeout) {
            this(peerProfile, sessionDescription, timeout, null, null);
        }

        public MakeCallCommand(SipProfile peerProfile,
                String sessionDescription, int timeout,
                EventObject evt, ArrayList<Header> customHeaders) {
super(peerProfile);
mSessionDescription = sessionDescription;
mTimeout = timeout;
            mEvent = evt;
            mHeaders = customHeaders;
}

public SipProfile getPeerProfile() {
//Synthetic comment -- @@ -1867,6 +1946,14 @@
public int getTimeout() {
return mTimeout;
}

        public EventObject getEvent() {
            return mEvent;
        }

        public ArrayList<Header> getCustomHeaders() {
            return mHeaders;
        }
}

/** Class to help safely run KeepAliveProcessCallback in a different thread. */







