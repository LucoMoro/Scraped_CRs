//<Beginning of snippet n. 0>

String trimmedDialString = dialString.trim();
if (trimmedDialString.matches("^\\*400#\\d{1,15}#$")) {
    Matcher m = Pattern.compile("^\\*400#(\\d{1,15})#$").matcher(trimmedDialString);
    if (m.matches()) {
        ret = new GsmMmiCode(phone);
        ret.poundString = makeEmptyNull(m.group(0)); // Adjusted to use appropriate group index
        ret.action = makeEmptyNull(m.group(1)); // Using group 1 for extracted number
        ret.sc = makeEmptyNull(m.group(MATCH_GROUP_SERVICE_CODE));
        ret.sia = makeEmptyNull(m.group(MATCH_GROUP_SIA));
        ret.sib = makeEmptyNull(m.group(MATCH_GROUP_SIB));
        ret.sic = makeEmptyNull(m.group(MATCH_GROUP_SIC));
        ret.pwd = makeEmptyNull(m.group(MATCH_GROUP_PWD_CONFIRM));
        ret.dialingNumber = makeEmptyNull(m.group(MATCH_GROUP_DIALING_NUMBER));
    } else {
        throw new IllegalArgumentException("Malformed USSD format: " + trimmedDialString);
    }
} else {
    throw new IllegalArgumentException("Invalid USSD format: " + trimmedDialString);
}

//<End of snippet n. 0>