/*Fix for the handling of SMS Delivery Report in case of CDMA network

Although the carrier MetroPCS has CDMA network,it does not support SMS Delivery Report

Change-Id:I42fe94627b698f18aacba46afd8d0cad270f17ad*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index 8483fbb..4665b3e 100755

//Synthetic comment -- @@ -63,7 +63,8 @@
"45005",    // SKT Mobility
"45002",    // SKT Mobility
"45008",    // KT Mobility
                    "45006",    // LGT
                    "311660"    // MetroPCS
);

// List of network operators that doesn't support Data(binary) SMS message
//Synthetic comment -- @@ -103,9 +104,6 @@

if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
mDeliveryReportSupported = false;
} else {
// is this a GSM network that doesn't support SMS delivery report?
String mccmnc = mTelephonyManager.getSimOperator();







