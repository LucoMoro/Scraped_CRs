/*Making it possible to call SIP calls with special allowed chars.

Since String.replaceFirst uses regex and since SIP user names are
allowed to include regex charaters such as '+', the code must
fist convert the string to a literal pattern String before using
replaceFirst method.

Change-Id:I25eac852bd620724ca1c5b2befc023af9dae3c1a*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/sip/SipPhone.java b/telephony/java/com/android/internal/telephony/sip/SipPhone.java
old mode 100755
new mode 100644
//Synthetic comment -- index 461e4fb..e37afda

//Synthetic comment -- @@ -41,6 +41,7 @@

import java.text.ParseException;
import java.util.List;

/**
* {@hide}
//Synthetic comment -- @@ -383,8 +384,8 @@
Connection dial(String originalNumber) throws SipException {
String calleeSipUri = originalNumber;
if (!calleeSipUri.contains("@")) {
                calleeSipUri = mProfile.getUriString().replaceFirst(
                        mProfile.getUserName() + "@",
calleeSipUri + "@");
}
try {








//Synthetic comment -- diff --git a/voip/java/com/android/server/sip/SipHelper.java b/voip/java/com/android/server/sip/SipHelper.java
//Synthetic comment -- index 518543a..f24e3fb 100644

//Synthetic comment -- @@ -27,6 +27,8 @@
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogTerminatedEvent;
//Synthetic comment -- @@ -215,9 +217,11 @@
String tag) throws ParseException, SipException {
FromHeader fromHeader = createFromHeader(userProfile, tag);
ToHeader toHeader = createToHeader(userProfile);
SipURI requestURI = mAddressFactory.createSipURI(
                userProfile.getUriString().replaceFirst(
                userProfile.getUserName() + "@", ""));
List<ViaHeader> viaHeaders = createViaHeaders();
CallIdHeader callIdHeader = createCallIdHeader();
CSeqHeader cSeqHeader = createCSeqHeader(requestType);







