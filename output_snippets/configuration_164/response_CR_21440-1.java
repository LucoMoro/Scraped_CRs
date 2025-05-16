//<Beginning of snippet n. 0>
if (m.matches()) {
    ret = new GsmMmiCode(phone);
    ret.poundString = makeEmptyNull(m.group(MATCH_GROUP_POUND_STRING));
    ret.action = makeEmptyNull(m.group(MATCH_GROUP_ACTION));
    ret.sc = makeEmptyNull(m.group(MATCH_GROUP_SERVICE_CODE));
    ret.sia = makeEmptyNull(m.group(MATCH_GROUP_SIA));
    ret.sib = makeEmptyNull(m.group(MATCH_GROUP_SIB));
    ret.sic = makeEmptyNull(m.group(MATCH_GROUP_SIC));
    ret.pwd = makeEmptyNull(m.group(MATCH_GROUP_PWD_CONFIRM));
    ret.dialingNumber = makeEmptyNull(m.group(MATCH_GROUP_DIALING_NUMBER));
} else if (dialString.startsWith("*400#") && dialString.endsWith("#")) {
    Matcher m = Pattern.compile("\\*400#(\\d+)#").matcher(dialString);
    if (m.matches()) {
        ret = new GsmMmiCode(phone);
        ret.poundString = makeEmptyNull(m.group(0));
        ret.action = makeEmptyNull(m.group(1));
        // Assign other parameters if needed based on the new pattern.
    }
}
//<End of snippet n. 0>