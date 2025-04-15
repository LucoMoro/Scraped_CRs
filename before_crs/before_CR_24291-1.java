/*Update lists for SMS

Update the lists NO_DELIVERY_REPORTS, UNSUPPORT_DATA_SMS_MESSAGES
and UNSUPPORT_MULTIPART_SMS_MESSAGES

Change-Id:Ia706ca9539ac06b95ca6cd1de3d9da4a6955fa18*/
//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index bd2cbbf..83a5df5 100644

//Synthetic comment -- @@ -79,21 +79,33 @@
"44075",    // KDDI
"44076",    // KDDI
"311870",   // Boost Mobile
                    "311220"    // USCC
);

// List of network operators that doesn't support Data(binary) SMS message
private static final List<String> UNSUPPORT_DATA_SMS_MESSAGES =
Arrays.asList(
"44010",    // NTT DOCOMO
                    "44020"     // SBM
);

// List of network operators that doesn't support Maltipart SMS message
private static final List<String> UNSUPPORT_MULTIPART_SMS_MESSAGES =
Arrays.asList(
"44010",    // NTT DOCOMO
                    "44020"     // SBM
);

private TelephonyManager mTelephonyManager;







