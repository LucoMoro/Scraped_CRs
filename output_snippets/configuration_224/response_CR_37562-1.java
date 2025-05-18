//<Beginning of snippet n. 0>
throw new IllegalArgumentException("Illegal PDU");
}

int messageIdentifier = ((pdu[2] & 0xff) << 8) | (pdu[3] & 0xff);

if (messageIdentifier >= 0 && messageIdentifier <= 255) {
    format = FORMAT_ETWS_PRIMARY;
    geographicalScope = -1;
    messageCode = -1;
    updateNumber = -1;
    dataCodingScheme = -1;
    pageIndex = -1;
    nrOfPages = -1;
    etwsEmergencyUserAlert = (pdu[4] & 0x1) != 0;
    etwsPopup = (pdu[5] & 0x80) != 0;
    etwsWarningType = (pdu[4] & 0xfe) >> 1;
} else if (messageIdentifier >= 256 && messageIdentifier <= 511) {
    format = FORMAT_GSM;
    geographicalScope = (pdu[0] & 0xc0) >> 6;
    messageCode = ((pdu[0] & 0x3f) << 4) | ((pdu[1] & 0xf0) >> 4);
    updateNumber = pdu[1] & 0x0f;
    dataCodingScheme = pdu[4] & 0xff;

    int localPageIndex = (pdu[5] & 0xf0) >> 4;
    int localNrOfPages = pdu[5] & 0x0f;

    if (localPageIndex == 0 || localNrOfPages == 0 || localPageIndex > localNrOfPages) {
        localPageIndex = 1;
        localNrOfPages = 1;
    }
    this.pageIndex = localPageIndex;
    this.nrOfPages = localNrOfPages;
    etwsEmergencyUserAlert = false;
    etwsPopup = false;
    etwsWarningType = -1;
} else {
    throw new IllegalArgumentException("Invalid message identifier: " + messageIdentifier);
}
//<End of snippet n. 0>