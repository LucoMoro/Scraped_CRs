//<Beginning of snippet n. 0>

old mode 100644
new mode 100755

static final String SC_PUK          = "05";
static final String SC_PUK2         = "052";

//***** Event Constants

static final int EVENT_SET_COMPLETE         = 1;

ret = new GsmMmiCode(phone);
ret.poundString = dialString;
} else if (isShortCode(dialString, phone) && !isTwoDigitNumber(dialString)) {
// this may be a short code, as defined in TS 22.030, 6.5.3.2
ret = new GsmMmiCode(phone);
}

/**
* Helper function for newFromDialString.  Returns true if dialString appears to be a short code
* AND conditions are correct for it to be treated as such.
*/
private boolean isShortCode(String dialString, String phone) {
    if (dialString.length() < 2) return false; // Short codes are usually 2 digits or more
    // Check for existing short code criteria
    return false;
}

private boolean isTwoDigitNumber(String dialString) {
    try {
        int number = Integer.parseInt(dialString);
        return number >= 92 && number <= 96;
    } catch (NumberFormatException e) {
        return false;
    }
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

old mode 100644
new mode 100755

return null;
}

// Length = length of MCC + length of MNC
// length of mcc = 3 (TS 23.003 Section 2.2)
return imsi.substring(0, 3 + mncLength);

//<End of snippet n. 1>