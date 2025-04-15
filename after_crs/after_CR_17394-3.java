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

    public static class SpecialSmsMsg {
        public int msgIndType;
        public int msgCount;
    }

/**
* A header element that is not explicitly parsed, meaning not
     * PortAddrs or ConcatRef or SpecialSmsMsg.
*/
public static class MiscElt {
public int id;
//Synthetic comment -- @@ -94,6 +99,7 @@

public PortAddrs portAddrs;
public ConcatRef concatRef;
    public ArrayList<SpecialSmsMsg> specialSmsMsgList = new ArrayList<SpecialSmsMsg>();
public ArrayList<MiscElt> miscEltList = new ArrayList<MiscElt>();

public SmsHeader() {}
//Synthetic comment -- @@ -157,6 +163,12 @@
portAddrs.areEightBits = false;
smsHeader.portAddrs = portAddrs;
break;
            case ELT_ID_SPECIAL_SMS_MESSAGE_INDICATION:
                SpecialSmsMsg specialSmsMsg = new SpecialSmsMsg();
                specialSmsMsg.msgIndType = inStream.read();
                specialSmsMsg.msgCount = inStream.read();
                smsHeader.specialSmsMsgList.add(specialSmsMsg);
                break;
default:
MiscElt miscElt = new MiscElt();
miscElt.id = id;
//Synthetic comment -- @@ -176,6 +188,7 @@
public static byte[] toByteArray(SmsHeader smsHeader) {
if ((smsHeader.portAddrs == null) &&
(smsHeader.concatRef == null) &&
            (smsHeader.specialSmsMsgList.size() == 0) &&
(smsHeader.miscEltList.size() == 0)) {
return null;
}
//Synthetic comment -- @@ -212,6 +225,12 @@
outStream.write(portAddrs.origPort & 0x00FF);
}
}
        for (SpecialSmsMsg specialSmsMsg : smsHeader.specialSmsMsgList) {
            outStream.write(ELT_ID_SPECIAL_SMS_MESSAGE_INDICATION);
            outStream.write(2);
            outStream.write(specialSmsMsg.msgIndType & 0xFF);
            outStream.write(specialSmsMsg.msgCount & 0xFF);
        }
for (MiscElt miscElt : smsHeader.miscEltList) {
outStream.write(miscElt.id);
outStream.write(miscElt.data.length);
//Synthetic comment -- @@ -243,6 +262,12 @@
builder.append(", areEightBits=" + portAddrs.areEightBits);
builder.append(" }");
}
        for (SpecialSmsMsg specialSmsMsg : specialSmsMsgList) {
            builder.append(", SpecialSmsMsg ");
            builder.append("{ msgIndType=" + specialSmsMsg.msgIndType);
            builder.append(", msgCount=" + specialSmsMsg.msgCount);
            builder.append(" }");
        }
for (MiscElt miscElt : miscEltList) {
builder.append(", MiscElt ");
builder.append("{ id=" + miscElt.id);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 49de5f9..d2c2bea 100644

//Synthetic comment -- @@ -410,9 +410,14 @@
}

/*package*/ void
    updateMessageWaitingIndicator(int mwi) {
        /*
         * mwi = number of voice mails; count is known, set notification mwi =
         * -1; count is unknown, set notification mwi = 0; no unread voicemails
         * , clear notification
         */
// this also calls notifyMessageWaitingIndicator()
        mSIMRecords.setVoiceMessageWaiting(1, mwi);
}

public void








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java b/telephony/java/com/android/internal/telephony/gsm/GsmSMSDispatcher.java
//Synthetic comment -- index 438c811..3c6941a 100644

//Synthetic comment -- @@ -111,13 +111,13 @@

// Special case the message waiting indicator messages
if (sms.isMWISetMessage()) {
            mGsmPhone.updateMessageWaitingIndicator(sms.getNumOfVoicemails());
handled |= sms.isMwiDontStore();
if (Config.LOGD) {
Log.d(TAG, "Received voice mail indicator set SMS shouldStore=" + !handled);
}
} else if (sms.isMWIClearMessage()) {
            mGsmPhone.updateMessageWaitingIndicator(0);
handled |= sms.isMwiDontStore();
if (Config.LOGD) {
Log.d(TAG, "Received voice mail indicator clear SMS shouldStore=" + !handled);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java b/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index 4fd62fb..5001384 100644

//Synthetic comment -- @@ -93,6 +93,8 @@
*/
private boolean isStatusReportMessage = false;

    private int mVoiceMailCount = 0;

public static class SubmitPdu extends SubmitPduBase {
}

//Synthetic comment -- @@ -1092,19 +1094,31 @@

userDataCompressed = false;
boolean active = ((dataCodingScheme & 0x08) == 0x08);
// bit 0x04 reserved

            // VM - If TP-UDH is present, these values will be overwritten
if ((dataCodingScheme & 0x03) == 0x00) {
                isMwi = true; /* Indicates vmail */
                mwiSense = active;/* Indicates vmail notification set/clear */
mwiDontStore = ((dataCodingScheme & 0xF0) == 0xC0);

                /* Set voice mail count based on notification bit */
                if (active == true) {
                    mVoiceMailCount = -1; // unknown number of messages waiting
                } else {
                    mVoiceMailCount = 0; // no unread messages
                }

                Log.w(LOG_TAG, "MWI in DCS for Vmail. DCS = "
                        + (dataCodingScheme & 0xff) + " Dont store = "
                        + mwiDontStore + " vmail count = " + mVoiceMailCount);

} else {
isMwi = false;
                Log.w(LOG_TAG, "MWI in DCS for fax/email/other: "
+ (dataCodingScheme & 0xff));
}

} else {
Log.w(LOG_TAG, "3 - Unsupported SMS data coding scheme "
+ (dataCodingScheme & 0xff));
//Synthetic comment -- @@ -1116,6 +1130,75 @@
this.userData = p.getUserData();
this.userDataHeader = p.getUserDataHeader();

        /*
         * Look for voice mail indication in TP_UDH TS23.040 9.2.3.24
         * ieid = 1 (0x1) (SPECIAL_SMS_MSG_IND)
         * ieidl =2 octets
         * ieda msg_ind_type = 0x00 (voice mail; discard sms )or
         *                   = 0x80 (voice mail; store sms)
         * msg_count = 0x00 ..0xFF
         */
        if (hasUserDataHeader && (userDataHeader.specialSmsMsgList.size() != 0)) {
            for (SmsHeader.SpecialSmsMsg msg : userDataHeader.specialSmsMsgList) {
                int msgInd = msg.msgIndType & 0xff;
                /*
                 * TS 23.040 V6.8.1 Sec 9.2.3.24.2
                 * bits 1 0 : basic message indication type
                 * bits 4 3 2 : extended message indication type
                 * bits 6 5 : Profile id bit 7 storage type
                 */
                if ((msgInd == 0) || (msgInd == 0x80)) {
                    isMwi = true;
                    if (msgInd == 0x80) {
                        /* Store message because TP_UDH indicates so*/
                        mwiDontStore = false;
                    } else if (mwiDontStore == false) {
                        /* Storage bit is not set by TP_UDH
                         * Check for conflict
                         * between message storage bit in TP_UDH
                         * & DCS. The message shall be stored if either of
                         * the one indicates so.
                         * TS 23.040 V6.8.1 Sec 9.2.3.24.2
                         */
                        if (!((((dataCodingScheme & 0xF0) == 0xD0)
                               || ((dataCodingScheme & 0xF0) == 0xE0))
                               && ((dataCodingScheme & 0x03) == 0x00))) {
                            /* Even DCS did not have voice mail with Storage bit
                             * 3GPP TS 23.038 V7.0.0 section 4
                             * So clear this flag*/
                            mwiDontStore = true;
                        }
                    }

                    mVoiceMailCount = msg.msgCount & 0xff;

                    /*
                     * In the event of a conflict between message count setting
                     * and DCS then the Message Count in the TP-UDH shall
                     * override the indication in the TP-DCS. Set voice mail
                     * notification based on count in TP-UDH
                     */
                    if (mVoiceMailCount > 0)
                        mwiSense = true;
                    else
                        mwiSense = false;

                    Log.w(LOG_TAG, "MWI in TP-UDH for Vmail. Msg Ind = " + msgInd
                            + " Dont store = " + mwiDontStore + " Vmail count = "
                            + mVoiceMailCount);

                    /*
                     * There can be only one IE for each type of message
                     * indication in TP_UDH. In the event they are duplicated
                     * last occurence will be used. Hence the for loop
                     */
                } else {
                    Log.w(LOG_TAG, "TP_UDH fax/email/"
                            + "extended msg/multisubscriber profile. Msg Ind = " + msgInd);
                }
            } // end of for
        } // end of if UDH

switch (encodingType) {
case ENCODING_UNKNOWN:
case ENCODING_8BIT:
//Synthetic comment -- @@ -1164,4 +1247,10 @@
return messageClass;
}

    /** This function  shall be called to get the number of voicemails.
     * @hide
     */
    public int getNumOfVoicemails() {
        return mVoiceMailCount ;
    }
}







