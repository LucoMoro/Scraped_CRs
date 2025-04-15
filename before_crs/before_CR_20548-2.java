/*SIP: use expiry time from Contact header

RFC3261 section 10.3 does not mention the "Expires" header,
but does say the Contact headers MUST be present, and that
the expires parameter in those MUST be present.

At least one registrar (sipgate.com) does not provide an
Expires header in the response, but does provide the expires
parameter in the appropriate Contact header.

Change-Id:I2de0c12d8fa04b6e2b3a756c2e89f7a62ede3ae3*/
//Synthetic comment -- diff --git a/voip/java/com/android/server/sip/SipHelper.java b/voip/java/com/android/server/sip/SipHelper.java
//Synthetic comment -- index 13e6f14..aa2e1bd 100644

//Synthetic comment -- @@ -145,15 +145,22 @@
return viaHeaders;
}

    private ContactHeader createContactHeader(SipProfile profile)
throws ParseException, SipException {
ListeningPoint lp = getListeningPoint();
SipURI contactURI =
createSipUri(profile.getUserName(), profile.getProtocol(), lp);

Address contactAddress = mAddressFactory.createAddress(contactURI);
contactAddress.setDisplayName(profile.getDisplayName());

return mHeaderFactory.createContactHeader(contactAddress);
}









//Synthetic comment -- diff --git a/voip/java/com/android/server/sip/SipSessionGroup.java b/voip/java/com/android/server/sip/SipSessionGroup.java
//Synthetic comment -- index 50ce7dc..e0d30d8 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import gov.nist.javax.sip.header.ProxyAuthenticate;
import gov.nist.javax.sip.header.WWWAuthenticate;
import gov.nist.javax.sip.message.SIPMessage;

import android.net.sip.ISipSession;
import android.net.sip.ISipSessionListener;
//Synthetic comment -- @@ -66,6 +67,7 @@
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.MinExpiresHeader;
//Synthetic comment -- @@ -774,6 +776,22 @@
ExpiresHeader expiresHeader = (ExpiresHeader)
response.getHeader(ExpiresHeader.NAME);
if (expiresHeader != null) expires = expiresHeader.getExpires();
expiresHeader = (ExpiresHeader)
response.getHeader(MinExpiresHeader.NAME);
if (expiresHeader != null) {







