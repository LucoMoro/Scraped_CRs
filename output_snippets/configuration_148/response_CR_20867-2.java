//<Beginning of snippet n. 0>

old mode 100644
new mode 100755

static final String SC_PUK          = "05";
static final String SC_PUK2         = "052";

//***** Event Constants

static final int EVENT_SET_COMPLETE         = 1;

ret = new GsmMmiCode(phone);
} else if (isShortCode(dialString, phone) || isTwoDigitRange(dialString)) {
// this may be a short code, as defined in TS 22.030, 6.5.3.2
ret = new GsmMmiCode(phone);
}

/**
* Helper function for newFromDialString.  Returns true if dialString appears to be a short code
* AND conditions are correct for it to be treated as such.
*/
private boolean isShortCode(String dialString, Phone phone) {
    if (dialString.length() < 3 || isTwoDigitRange(dialString)) {
        return false;
    }
    // Existing implementation
}

private boolean isTwoDigitRange(String dialString) {
    if (dialString.length() == 2) {
        int number = Integer.parseInt(dialString);
        return number >= 92 && number <= 96;
    }
    return false;
}

//<End of snippet n. 0>