/*Support for voicemail in TP-UDH of SMS

Added support for processing special message indication
for voice mail in TP user data header for SMS
Spec reference: 3GPP TS 23.040 V6.8.1

Change-Id:I9073774ee9881386baf5b9c9f0a6b3bcb6547b7d*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/SmsHeader.java b/telephony/java/com/android/internal/telephony/SmsHeader.java
//Synthetic comment -- index 7872eec..03209b1 100644

//Synthetic comment -- @@ -83,9 +83,14 @@
public boolean isEightBits;
}

/**
* A header element that is not explicitly parsed, meaning not
     * PortAddrs or ConcatRef.
*/
public static class MiscElt {
public int id;
//Synthetic comment -- @@ -94,6 +99,7 @@

public PortAddrs portAddrs;
public ConcatRef concatRef;
public ArrayList<MiscElt> miscEltList = new ArrayList<MiscElt>();

public SmsHeader() {}
//Synthetic comment -- @@ -157,6 +163,12 @@
portAddrs.areEightBits = false;
smsHeader.portAddrs = portAddrs;
break;
default:
MiscElt miscElt = new MiscElt();
miscElt.id = id;
//Synthetic comment -- @@ -176,6 +188,7 @@
public static byte[] toByteArray(SmsHeader smsHeader) {
if ((smsHeader.portAddrs == null) &&
(smsHeader.concatRef == null) &&
(smsHeader.miscEltList.size() == 0)) {
return null;
}
//Synthetic comment -- @@ -212,6 +225,12 @@
outStream.write(portAddrs.origPort & 0x00FF);
}
}
for (MiscElt miscElt : smsHeader.miscEltList) {
outStream.write(miscElt.id);
outStream.write(miscElt.data.length);
//Synthetic comment -- @@ -243,6 +262,12 @@
builder.append(", areEightBits=" + portAddrs.areEightBits);
builder.append(" }");
}
for (MiscElt miscElt : miscEltList) {
builder.append(", MiscElt ");
builder.append("{ id=" + miscElt.id);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 3959c67..9f4b6d3 100644

//Synthetic comment -- @@ -406,9 +406,14 @@
}

/*package*/ void
    updateMessageWaitingIndicator(boolean mwi) {
// this also calls notifyMessageWaitingIndicator()
        mSIMRecords.setVoiceMessageWaiting(1, mwi ? -1 : 0);
}

public void








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java b/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java
//Synthetic comment -- index 8355046..655959e 100755

//Synthetic comment -- @@ -115,13 +115,13 @@

// Special case the message waiting indicator messages
if (sms.isMWISetMessage()) {
            mGsmPhone.updateMessageWaitingIndicator(true);
handled = sms.isMwiDontStore();
if (Config.LOGD) {
Log.d(TAG, "Received voice mail indicator set SMS shouldStore=" + !handled);
}
} else if (sms.isMWIClearMessage()) {
            mGsmPhone.updateMessageWaitingIndicator(false);
handled = sms.isMwiDontStore();
if (Config.LOGD) {
Log.d(TAG, "Received voice mail indicator clear SMS shouldStore=" + !handled);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java b/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index f4c5e6c..750c4d4 100644

//Synthetic comment -- @@ -100,6 +100,8 @@
*/
private boolean isStatusReportMessage = false;

public static class SubmitPdu extends SubmitPduBase {
}

//Synthetic comment -- @@ -1123,19 +1125,31 @@

userDataCompressed = false;
boolean active = ((dataCodingScheme & 0x08) == 0x08);

// bit 0x04 reserved

if ((dataCodingScheme & 0x03) == 0x00) {
                isMwi = true;
                mwiSense = active;
mwiDontStore = ((dataCodingScheme & 0xF0) == 0xC0);
} else {
isMwi = false;

                Log.w(LOG_TAG, "MWI for fax, email, or other "
+ (dataCodingScheme & 0xff));
}
} else {
Log.w(LOG_TAG, "3 - Unsupported SMS data coding scheme "
+ (dataCodingScheme & 0xff));
//Synthetic comment -- @@ -1151,6 +1165,75 @@
this.userData = p.getUserData();
this.userDataHeader = p.getUserDataHeader();

switch (encodingType) {
case ENCODING_UNKNOWN:
case ENCODING_8BIT:
//Synthetic comment -- @@ -1203,4 +1286,23 @@
return messageClass;
}

}







