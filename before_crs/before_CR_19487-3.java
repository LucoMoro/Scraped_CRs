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
            } else if (dialingNumber != null) {
                // We should have no dialing numbers here
throw new RuntimeException ("Invalid or Unsupported MMI Code");
} else if (sc != null && sc.equals(SC_CLIP)) {
Log.d(LOG_TAG, "is CLIP");
//Synthetic comment -- @@ -788,7 +791,12 @@
throw new RuntimeException ("Invalid or Unsupported MMI Code");
}
} else if (poundString != null) {
                sendUssd(poundString);
} else {
throw new RuntimeException ("Invalid or Unsupported MMI Code");
}







