//<Beginning of snippet n. 0>
String regexPattern = "\\*400#\\d{1,12}#"; // Updated regex pattern to enforce length constraints
Pattern pattern = Pattern.compile(regexPattern);
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
} else if (dialString.endsWith("#")) {
    // Return an error message or handle malformed input appropriately
    ret = null; // Or some form of error handling
}
//<End of snippet n. 0>