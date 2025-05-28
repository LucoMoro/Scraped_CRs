
//<Beginning of snippet n. 0>



// Is this formatted like a standard supplementary service code?
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
// TS 22.030 sec 6.5.3.2
// "Entry of any characters defined in the 3GPP TS 23.038 [8] Default Alphabet

//<End of snippet n. 0>








