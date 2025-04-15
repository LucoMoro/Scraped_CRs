/*Telephony: Add support for SMS-SUBMIT.

Change-Id:I2b0887ae6b28decb38576cac60f1314ff8b73e1dSigned-off-by: Bin Li <libin@marvell.com>*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SmsMessage.java b/src/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index f23ec98..d216fc4 100644

//Synthetic comment -- @@ -924,6 +924,9 @@
//This should be processed in the same way as MTI == 0 (Deliver)
parseSmsDeliver(p, firstByte);
break;
        case 1:
            parseSmsSubmit(p, firstByte);
            break;
case 2:
parseSmsStatusReport(p, firstByte);
break;
//Synthetic comment -- @@ -1019,6 +1022,66 @@
}

/**
     * Parses a SMS-SUBMIT message.
     *
     * @param p A PduParser, cued past the first byte.
     * @param firstByte The first byte of the PDU, which contains MTI, etc.
     */
    private void parseSmsSubmit(PduParser p, int firstByte) {
        replyPathPresent = (firstByte & 0x80) == 0x80;

        // TP-MR (TP-Message Reference)
        messageRef = p.getByte();

        recipientAddress = p.getAddress();

        if (recipientAddress != null) {
            if (false) Rlog.v(LOG_TAG, "SMS recipient address: "
                    + recipientAddress.address);
        }

        // TP-Protocol-Identifier (TP-PID)
        // TS 23.040 9.2.3.9
        protocolIdentifier = p.getByte();

        // TP-Data-Coding-Scheme
        // see TS 23.038
        dataCodingScheme = p.getByte();

        if (false) {
            Rlog.v(LOG_TAG, "SMS TP-PID:" + protocolIdentifier
                    + " data coding scheme: " + dataCodingScheme);
        }

        // TP-Validity-Period-Format
        int validityPeriodLength = 0;
        int validityPeriodFormat = ((firstByte>>3) & 0x3);
        if (0x0 == validityPeriodFormat) /* 00, TP-VP field not present*/
        {
            validityPeriodLength = 0;
        }
        else if (0x2 == validityPeriodFormat) /* 10, TP-VP: relative format*/
        {
            validityPeriodLength = 1;
        }
        else /* other case, 11 or 01, TP-VP: absolute or enhanced format*/
        {
            validityPeriodLength = 7;
        }

        // TP-Validity-Period is not used on phone, so just ignore it for now.
        while (validityPeriodLength > 0)
        {
            p.getByte();
            validityPeriodLength--;
        }

        boolean hasUserDataHeader = (firstByte & 0x40) == 0x40;

        parseUserData(p, hasUserDataHeader);
    }

    /**
* Parses the User Data of an SMS.
*
* @param p The current PduParser.







