/*Adding processing of special USSD codes

Inputing an uncommon USSD string, with "#" in the middile,
like "*#220#00131537201846#", could not be processed
correctly because it is identified as an SS code.

According to the TS 22.090 5.1.2 the phone should not
forbid the string, but rather let the network handle it.

Change-Id:Iebda812be75f972734e6d6783373a6a7e78cf864*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index 16d3129..17946b0 100644

//Synthetic comment -- @@ -182,7 +182,7 @@
// Is this formatted like a standard supplementary service code?
if (m.matches()) {
ret = new GsmMmiCode(phone);
            ret.poundString = dialString.endsWith("#") ? dialString : null;
ret.action = makeEmptyNull(m.group(MATCH_GROUP_ACTION));
ret.sc = makeEmptyNull(m.group(MATCH_GROUP_SERVICE_CODE));
ret.sia = makeEmptyNull(m.group(MATCH_GROUP_SIA));
//Synthetic comment -- @@ -621,8 +621,9 @@
Log.d(LOG_TAG, "isShortCode");
// These just get treated as USSD.
sendUssd(dialingNumber);
            } else if (dialingNumber != null && poundString == null) {
                // Make sure we allow cases where we have a */#code#numbers# but don't allow
                // call setup strings. See TS 22.030.
throw new RuntimeException ("Invalid or Unsupported MMI Code");
} else if (sc != null && sc.equals(SC_CLIP)) {
Log.d(LOG_TAG, "is CLIP");







