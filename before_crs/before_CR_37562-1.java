/*Telephony: Distinguish GSM vs ETWS bc by message id

Per 3GPP TS 24.012 3.1 GSM CB can be less than 88 byes.
Fix to distinguish GSM vs ETWS broadcast format by checking message id field
rather than length of pdu.

Change-Id:I6aeedcd531a89ded7901db33d89ed45baaabbaa7*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SmsCbHeader.java b/telephony/java/com/android/internal/telephony/gsm/SmsCbHeader.java
//Synthetic comment -- index 8e6b79b..a0d5fb1 100644

//Synthetic comment -- @@ -81,41 +81,43 @@
throw new IllegalArgumentException("Illegal PDU");
}

        if (pdu.length <= PDU_LENGTH_ETWS) {
            format = FORMAT_ETWS_PRIMARY;
            geographicalScope = -1; //not applicable
            messageCode = -1;
            updateNumber = -1;
            messageIdentifier = ((pdu[2] & 0xff) << 8) | (pdu[3] & 0xff);
            dataCodingScheme = -1;
            pageIndex = -1;
            nrOfPages = -1;
            etwsEmergencyUserAlert = (pdu[4] & 0x1) != 0;
            etwsPopup = (pdu[5] & 0x80) != 0;
            etwsWarningType = (pdu[4] & 0xfe) >> 1;
        } else if (pdu.length <= PDU_LENGTH_GSM) {
            // GSM pdus are no more than 88 bytes
            format = FORMAT_GSM;
            geographicalScope = (pdu[0] & 0xc0) >> 6;
            messageCode = ((pdu[0] & 0x3f) << 4) | ((pdu[1] & 0xf0) >> 4);
updateNumber = pdu[1] & 0x0f;
messageIdentifier = ((pdu[2] & 0xff) << 8) | (pdu[3] & 0xff);
            dataCodingScheme = pdu[4] & 0xff;

            // Check for invalid page parameter
            int pageIndex = (pdu[5] & 0xf0) >> 4;
            int nrOfPages = pdu[5] & 0x0f;

            if (pageIndex == 0 || nrOfPages == 0 || pageIndex > nrOfPages) {
                pageIndex = 1;
                nrOfPages = 1;
}

            this.pageIndex = pageIndex;
            this.nrOfPages = nrOfPages;
            etwsEmergencyUserAlert = false;
            etwsPopup = false;
            etwsWarningType = -1;
} else {
// UMTS pdus are always at least 90 bytes since the payload includes
// a number-of-pages octet and also one length octet per page







