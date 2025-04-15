/*Fix for the handling of SMS Delivery Report in case of CDMA network

Although the carrier MetroPCS has CDMA network,it does not support SMS
Delivery report

Change-Id:I42fe94627b698f18aacba46afd8d0cad270f17adSigned-off-by: papiya <pnath@sta.samsung.com>*/
//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index 8483fbb..381c97f 100755

//Synthetic comment -- @@ -63,7 +63,8 @@
"45005",    // SKT Mobility
"45002",    // SKT Mobility
"45008",    // KT Mobility
                    "45006"     // LGT
);

// List of network operators that doesn't support Data(binary) SMS message
//Synthetic comment -- @@ -103,11 +104,8 @@

if (!mPackageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
mDeliveryReportSupported = false;
        } else if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
            // CDMA supports SMS delivery report
            mDeliveryReportSupported = true;
} else {
            // is this a GSM network that doesn't support SMS delivery report?
String mccmnc = mTelephonyManager.getSimOperator();
mDeliveryReportSupported = !(NO_DELIVERY_REPORTS.contains(mccmnc));
}







