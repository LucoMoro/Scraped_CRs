//<Beginning of snippet n. 0>
String regexPattern = "^\\*400#\\d{1,12}#$"; // Updated regex pattern to enforce the entire USSD format
Pattern pattern = Pattern.compile(regexPattern);
Matcher m = pattern.matcher(dialString.trim());

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
    String errorMessage = "Malformed USSD input. Please ensure it starts with '*400#' and ends with '#'.";
    // Log or throw an exception with the error message instead of returning null
    throw new IllegalArgumentException(errorMessage); // Proper error handling
}
//<End of snippet n. 0>