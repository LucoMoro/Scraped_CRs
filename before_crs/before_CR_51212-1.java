/*telephony : Support for voicemail in TP-UDH of SMS

Added support for processing special message indication
for voice mail in TP user data header for SMS
Spec reference: 3GPP TS 23.040 V6.8.1

CPHS method does not use message count.
0xff is used to indicate unknown message count for CPHS
Ref[Adapt requirement CDR-MWI-120, TC: GSM-BTR-1-4700]

Change-Id:I6a9b9603742fac1d552d4dc74ee7f2e075bce068*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/SmsHeader.java b/src/java/com/android/internal/telephony/SmsHeader.java
//Synthetic comment -- index 30d57b8..b519b70 100644

//Synthetic comment -- @@ -84,9 +84,14 @@
public boolean isEightBits;
}

/**
* A header element that is not explicitly parsed, meaning not
     * PortAddrs or ConcatRef.
*/
public static class MiscElt {
public int id;
//Synthetic comment -- @@ -95,6 +100,7 @@

public PortAddrs portAddrs;
public ConcatRef concatRef;
public ArrayList<MiscElt> miscEltList = new ArrayList<MiscElt>();

/** 7 bit national language locking shift table, or 0 for GSM default 7 bit alphabet. */
//Synthetic comment -- @@ -170,6 +176,12 @@
case ELT_ID_NATIONAL_LANGUAGE_LOCKING_SHIFT:
smsHeader.languageTable = inStream.read();
break;
default:
MiscElt miscElt = new MiscElt();
miscElt.id = id;
//Synthetic comment -- @@ -189,6 +201,7 @@
public static byte[] toByteArray(SmsHeader smsHeader) {
if ((smsHeader.portAddrs == null) &&
(smsHeader.concatRef == null) &&
(smsHeader.miscEltList.isEmpty()) &&
(smsHeader.languageShiftTable == 0) &&
(smsHeader.languageTable == 0)) {
//Synthetic comment -- @@ -238,6 +251,12 @@
outStream.write(1);
outStream.write(smsHeader.languageTable);
}
for (MiscElt miscElt : smsHeader.miscEltList) {
outStream.write(miscElt.id);
outStream.write(miscElt.data.length);
//Synthetic comment -- @@ -275,6 +294,12 @@
if (languageTable != 0) {
builder.append(", languageTable=" + languageTable);
}
for (MiscElt miscElt : miscEltList) {
builder.append(", MiscElt ");
builder.append("{ id=" + miscElt.id);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SmsMessage.java b/src/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index cca9363..74f519d 100644

//Synthetic comment -- @@ -93,6 +93,8 @@
*/
private boolean isStatusReportMessage = false;

public static class SubmitPdu extends SubmitPduBase {
}

//Synthetic comment -- @@ -1150,17 +1152,29 @@

userDataCompressed = false;
boolean active = ((dataCodingScheme & 0x08) == 0x08);

// bit 0x04 reserved

if ((dataCodingScheme & 0x03) == 0x00) {
                isMwi = true;
                mwiSense = active;
mwiDontStore = ((dataCodingScheme & 0xF0) == 0xC0);
} else {
isMwi = false;

                Rlog.w(LOG_TAG, "MWI for fax, email, or other "
+ (dataCodingScheme & 0xff));
}
} else if ((dataCodingScheme & 0xC0) == 0x80) {
//Synthetic comment -- @@ -1184,6 +1198,75 @@
this.userData = p.getUserData();
this.userDataHeader = p.getUserDataHeader();

switch (encodingType) {
case ENCODING_UNKNOWN:
case ENCODING_8BIT:
//Synthetic comment -- @@ -1249,4 +1332,27 @@
return messageClass == MessageClass.CLASS_2 &&
(protocolIdentifier == 0x7f || protocolIdentifier == 0x7c);
}
}







