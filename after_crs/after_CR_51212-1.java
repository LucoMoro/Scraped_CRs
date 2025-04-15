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
//Synthetic comment -- @@ -95,6 +100,7 @@

public PortAddrs portAddrs;
public ConcatRef concatRef;
    public ArrayList<SpecialSmsMsg> specialSmsMsgList = new ArrayList<SpecialSmsMsg>();
public ArrayList<MiscElt> miscEltList = new ArrayList<MiscElt>();

/** 7 bit national language locking shift table, or 0 for GSM default 7 bit alphabet. */
//Synthetic comment -- @@ -170,6 +176,12 @@
case ELT_ID_NATIONAL_LANGUAGE_LOCKING_SHIFT:
smsHeader.languageTable = inStream.read();
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
//Synthetic comment -- @@ -189,6 +201,7 @@
public static byte[] toByteArray(SmsHeader smsHeader) {
if ((smsHeader.portAddrs == null) &&
(smsHeader.concatRef == null) &&
            (smsHeader.specialSmsMsgList.isEmpty()) &&
(smsHeader.miscEltList.isEmpty()) &&
(smsHeader.languageShiftTable == 0) &&
(smsHeader.languageTable == 0)) {
//Synthetic comment -- @@ -238,6 +251,12 @@
outStream.write(1);
outStream.write(smsHeader.languageTable);
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
//Synthetic comment -- @@ -275,6 +294,12 @@
if (languageTable != 0) {
builder.append(", languageTable=" + languageTable);
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








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SmsMessage.java b/src/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index cca9363..74f519d 100644

//Synthetic comment -- @@ -93,6 +93,8 @@
*/
private boolean isStatusReportMessage = false;

    private int mVoiceMailCount = 0;

public static class SubmitPdu extends SubmitPduBase {
}

//Synthetic comment -- @@ -1150,17 +1152,29 @@

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

                Rlog.w(LOG_TAG, "MWI in DCS for Vmail. DCS = "
                        + (dataCodingScheme & 0xff) + " Dont store = "
                        + mwiDontStore + " vmail count = " + mVoiceMailCount);

} else {
isMwi = false;

                Rlog.w(LOG_TAG, "MWI in DCS for fax/email/other: "
+ (dataCodingScheme & 0xff));
}
} else if ((dataCodingScheme & 0xC0) == 0x80) {
//Synthetic comment -- @@ -1184,6 +1198,75 @@
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

                    Rlog.w(LOG_TAG, "MWI in TP-UDH for Vmail. Msg Ind = " + msgInd
                            + " Dont store = " + mwiDontStore + " Vmail count = "
                            + mVoiceMailCount);

                    /*
                     * There can be only one IE for each type of message
                     * indication in TP_UDH. In the event they are duplicated
                     * last occurence will be used. Hence the for loop
                     */
                } else {
                    Rlog.w(LOG_TAG, "TP_UDH fax/email/"
                            + "extended msg/multisubscriber profile. Msg Ind = " + msgInd);
                }
            } // end of for
        } // end of if UDH

switch (encodingType) {
case ENCODING_UNKNOWN:
case ENCODING_8BIT:
//Synthetic comment -- @@ -1249,4 +1332,27 @@
return messageClass == MessageClass.CLASS_2 &&
(protocolIdentifier == 0x7f || protocolIdentifier == 0x7c);
}

    public int getNumOfVoicemails() {
        /*
         * Order of priority if multiple indications are present is 1.UDH,
         *      2.DCS, 3.CPHS.
         * Voice mail count if voice mail present indication is
         * received
         *  1. UDH (or both UDH & DCS): mVoiceMailCount = 0 to 0xff. Ref[TS 23. 040]
         *  2. DCS only: count is unknown mVoiceMailCount= -1
         *  3. CPHS only: count is unknown mVoiceMailCount = 0xff. Ref[GSM-BTR-1-4700]
         * Voice mail clear, mVoiceMailCount = 0.
         */
        if ((!isMwi) && isCphsMwiMessage()) {
            if (originatingAddress != null
                    && ((GsmSmsAddress) originatingAddress).isCphsVoiceMessageSet()) {
                mVoiceMailCount = 0xff;
            } else {
                mVoiceMailCount = 0;
            }
            Rlog.v(LOG_TAG, "CPHS voice mail message");
        }
        return mVoiceMailCount;
    }
}







