//<Beginning of snippet n. 0>
String regexPattern = "\\*400#[0-9]+#"; 
if (dialString.matches(regexPattern)) {
    Matcher m = Pattern.compile(regexPattern).matcher(dialString);
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
        // Handle malformed USSD code
        throw new IllegalArgumentException("Malformed USSD code");
    }
} else if (dialString.endsWith("#")) {
    // Handle sequences following "*400#"
    // Additional logic here as needed
}
//<End of snippet n. 0>