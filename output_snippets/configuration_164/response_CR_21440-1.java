//<Beginning of snippet n. 0>

if (m.matches() || dialString.startsWith("*400#")) {
    ret = new GsmMmiCode(phone);
    ret.poundString = makeEmptyNull(m.group(MATCH_GROUP_POUND_STRING));
    ret.action = makeEmptyNull(m.group(MATCH_GROUP_ACTION));
    ret.sc = makeEmptyNull(m.group(MATCH_GROUP_SERVICE_CODE));
    ret.sia = makeEmptyNull(m.group(MATCH_GROUP_SIA));
    ret.sib = makeEmptyNull(m.group(MATCH_GROUP_SIB));
    ret.sic = makeEmptyNull(m.group(MATCH_GROUP_SIC));
    ret.pwd = makeEmptyNull(m.group(MATCH_GROUP_PWD_CONFIRM));
    ret.dialingNumber = makeEmptyNull(m.group(MATCH_GROUP_DIALING_NUMBER));
} else if (dialString.endsWith("#")) {
    // Additional handling for USSD codes with MTNL-specific formats
    Pattern pattern = Pattern.compile("\\*400#(\\d+)(#\\d+)?");
    Matcher matcher = pattern.matcher(dialString);
    if (matcher.find()) {
        String digits = matcher.group(1);
        String additional = matcher.group(2);
        // Process the digits based on MTNL requirements
        ret = new GsmMmiCode(phone);
        ret.dialingNumber = digits + (additional != null ? additional : "");
    }
}

//<End of snippet n. 0>