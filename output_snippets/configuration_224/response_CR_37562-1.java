//<Beginning of snippet n. 0>
int messageIdentifier = ((pdu[2] & 0xff) << 8) | (pdu[3] & 0xff);

if (messageIdentifier < 0 || messageIdentifier > 511) {
    throw new IllegalArgumentException("Illegal PDU: Invalid messageIdentifier " + messageIdentifier);
}

switch (messageIdentifier) {
    case MESSAGE_ID_ETWS:
        format = FORMAT_ETWS_PRIMARY;
        geographicalScope = -1; // not applicable
        messageCode = -1;
        updateNumber = -1;
        dataCodingScheme = -1;
        pageIndex = -1;
        nrOfPages = -1;
        etwsEmergencyUserAlert = (pdu[4] & 0x1) != 0;
        etwsPopup = (pdu[5] & 0x80) != 0;
        etwsWarningType = (pdu[4] & 0xfe) >> 1;
        break;
    case MESSAGE_ID_GSM:
        format = FORMAT_GSM;
        geographicalScope = (pdu[0] & 0xc0) >> 6;
        messageCode = ((pdu[0] & 0x3f) << 4) | ((pdu[1] & 0xf0) >> 4);
        updateNumber = pdu[1] & 0x0f;
        dataCodingScheme = pdu[4] & 0xff;

        pageIndex = (pdu[5] & 0xf0) >> 4;
        nrOfPages = pdu[5] & 0x0f;

        if (pageIndex == 0 || nrOfPages == 0 || pageIndex > nrOfPages) {
            pageIndex = 1;
            nrOfPages = 1;
        }

        this.pageIndex = pageIndex;
        this.nrOfPages = nrOfPages;
        etwsEmergencyUserAlert = false;
        etwsPopup = false;
        etwsWarningType = -1;
        break;
    default:
        throw new IllegalArgumentException("Unsupported message identifier: " + messageIdentifier);
}
//<End of snippet n. 0>