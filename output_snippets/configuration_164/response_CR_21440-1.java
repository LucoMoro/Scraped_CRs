//<Beginning of snippet n. 0>

String trimmedDialString = dialString.trim();
if (!trimmedDialString.isEmpty() && trimmedDialString.matches("^\\*400#\\d{1,15}#$")) {
    Matcher m = Pattern.compile("^\\*400#(\\d{1,15})#$").matcher(trimmedDialString);
    if (m.matches()) {
        if (m.groupCount() >= 1) {
            ret = new GsmMmiCode(phone);
            ret.poundString = makeEmptyNull(m.group(0));
            ret.action = makeEmptyNull(m.group(1)); 
            ret.sc = makeEmptyNull(m.group(MATCH_GROUP_SERVICE_CODE));
            ret.sia = makeEmptyNull(m.group(MATCH_GROUP_SIA));
            ret.sib = makeEmptyNull(m.group(MATCH_GROUP_SIB));
            ret.sic = makeEmptyNull(m.group(MATCH_GROUP_SIC));
            ret.pwd = makeEmptyNull(m.group(MATCH_GROUP_PWD_CONFIRM));
            ret.dialingNumber = makeEmptyNull(m.group(MATCH_GROUP_DIALING_NUMBER));
        } else {
            throw new IllegalArgumentException("Matched but group data unavailable: " + trimmedDialString);
        }
    } else {
        throw new IllegalArgumentException("Malformed USSD format: " + trimmedDialString);
    }
} else {
    throw new IllegalArgumentException("Invalid USSD format: " + trimmedDialString);
}

//<End of snippet n. 0>