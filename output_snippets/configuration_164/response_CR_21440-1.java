//<Beginning of snippet n. 0>
String ussdPattern = "^\\*400#[0-9]+(#[0-9]+)*$";
Pattern pattern = Pattern.compile(ussdPattern);
Matcher m = pattern.matcher(dialString);
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
} else {
    throw new IllegalArgumentException("Invalid USSD code format");
}
//<End of snippet n. 0>