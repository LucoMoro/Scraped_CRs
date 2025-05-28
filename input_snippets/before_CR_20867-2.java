
//<Beginning of snippet n. 0>

old mode 100644
new mode 100755

static final String SC_PUK          = "05";
static final String SC_PUK2         = "052";

//***** Event Constants

static final int EVENT_SET_COMPLETE         = 1;

ret = new GsmMmiCode(phone);
ret.poundString = dialString;
} else if (isShortCode(dialString, phone)) {
// this may be a short code, as defined in TS 22.030, 6.5.3.2
ret = new GsmMmiCode(phone);

}

/**
* Helper function for newFromDialString.  Returns true if dialString appears to be a short code
* AND conditions are correct for it to be treated as such.

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








