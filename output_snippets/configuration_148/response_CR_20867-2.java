//<Beginning of snippet n. 0>

static final String SC_PUK          = "05";
static final String SC_PUK2         = "052";

//***** Event Constants

static final int EVENT_SET_COMPLETE         = 1;

ret = new GsmMmiCode(phone);
ret.poundString = dialString;
} else if (isShortCode(dialString, phone) || (dialString.startsWith("92") || dialString.startsWith("93") || dialString.startsWith("94") || dialString.startsWith("95") || dialString.startsWith("96"))) {
    // Handle as normal call if 92-96
    ret = new GsmMmiCode(phone);
}

/**
* Helper function for newFromDialString.  Returns true if dialString appears to be a short code
* AND conditions are correct for it to be treated as such.
* Updated to exclude handling for 92-96 as short codes.
*/

//<End of snippet n. 0>