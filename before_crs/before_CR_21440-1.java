/*Fix for suppporting USSD code that including "#" delimiter like "*400#DIGIT#"
Android doesn't support "#" delimeter in USSD code, but MTNL SIM(INDIA) uses
the USSD code starts with "*400#" like "*400#1234567890123456#".
so, added the condition for this.

Change-Id:Id993b5c750428fa5bffb110381d46768a82e6403*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index aa16fa3..3642775 100644

//Synthetic comment -- @@ -174,16 +174,21 @@

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







