/*Update lists of Japanese network operators

NTT Docomo:
  Support Delivery report, but not support binary and
  multipart SMS
KDDI:
  Not support Delivery report for all message type
SoftBank Mobile:
  Not support binary and multipart SMS completely

Change-Id:Ib605e6a33e38e86e32b12c40db1c691dcf2da143*/
//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index 381c97f..db16255 100755

//Synthetic comment -- @@ -59,24 +59,34 @@
private static final List<String> NO_DELIVERY_REPORTS =
Arrays.asList(
"310410",   // AT&T Mobility
                    "44010",    // NTT DOCOMO
"45005",    // SKT Mobility
"45002",    // SKT Mobility
"45008",    // KT Mobility
"45006",    // LGT
                    "311660"    // MetroPCS
);

// List of network operators that doesn't support Data(binary) SMS message
private static final List<String> UNSUPPORT_DATA_SMS_MESSAGES =
Arrays.asList(
                    "44010"    // NTT DOCOMO
);

// List of network operators that doesn't support Maltipart SMS message
private static final List<String> UNSUPPORT_MULTIPART_SMS_MESSAGES =
Arrays.asList(
                    "44010"    // NTT DOCOMO
);

private TelephonyManager mTelephonyManager;







