/*Adding processing of special USSD codes

Inputing an uncommon USSD string, with "#" in the middile, like
"*#220#00131537201846#", could not be processed correctly because it
is identified as an SS code.

According to the TS 22.090 5.1.2 the phone should not forbid the
string, but rather let the network handle it.

Change-Id:Iebda812be75f972734e6d6783373a6a7e78cf864*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index 2962e0f..ebd2c42 100644

//Synthetic comment -- @@ -621,8 +621,11 @@
Log.d(LOG_TAG, "isShortCode");
// These just get treated as USSD.
sendUssd(dialingNumber);
            } else if (dialingNumber != null &&
                    !(poundString != null &&
                    dialingNumber.charAt(dialingNumber.length() - 1) == '#')) {
                // Make sure we allow cases where we have a */#code#numbers# but don't allow
                // call setup strings. See TS 22.030.
throw new RuntimeException ("Invalid or Unsupported MMI Code");
} else if (sc != null && sc.equals(SC_CLIP)) {
Log.d(LOG_TAG, "is CLIP");
//Synthetic comment -- @@ -788,7 +791,12 @@
throw new RuntimeException ("Invalid or Unsupported MMI Code");
}
} else if (poundString != null) {
                if (dialingNumber != null) {
                    // It is a uncommon USSD,of which there is a "#" in the middle.
                    sendUssd(poundString + dialingNumber);
                } else {
                    sendUssd(poundString);
                }
} else {
throw new RuntimeException ("Invalid or Unsupported MMI Code");
}







