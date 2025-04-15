/*Modified so that if the network does not support SMS, it skips this test.

When the network is "KDDI", it skips this test
 because "KDDI" (MCC+MNC = 44054) does not support SMS delivery report.

We modified the test case in the same way for the case of "NTT DOCOMO"
 since it will skip test with the same reason.*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index 381c97f..7ff0a18 100755

//Synthetic comment -- @@ -60,6 +60,7 @@
Arrays.asList(
"310410",   // AT&T Mobility
"44010",    // NTT DOCOMO
                    "44054",    // KDDI
"45005",    // SKT Mobility
"45002",    // SKT Mobility
"45008",    // KT Mobility
//Synthetic comment -- @@ -67,18 +68,6 @@
"311660"    // MetroPCS
);

private TelephonyManager mTelephonyManager;
private PackageManager mPackageManager;
private String mDestAddr;
//Synthetic comment -- @@ -176,40 +165,30 @@
}

// send data sms
        byte[] data = mText.getBytes();
        short port = 19989;

        init();
        sendDataMessage(mDestAddr, port, data, mSentIntent, mDeliveredIntent);
        assertTrue(mSendReceiver.waitForCalls(1, TIME_OUT));
        if (mDeliveryReportSupported) {
            assertTrue(mDeliveryReceiver.waitForCalls(1, TIME_OUT));
}

// send multi parts text sms
        init();
        ArrayList<String> parts = divideMessage(LONG_TEXT);
        int numParts = parts.size();
        ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();
        for (int i = 0; i < numParts; i++) {
            sentIntents.add(PendingIntent.getBroadcast(getContext(), 0, mSendIntent, 0));
            deliveryIntents.add(PendingIntent.getBroadcast(getContext(), 0, mDeliveryIntent, 0));
        }
        sendMultiPartTextMessage(mDestAddr, parts, sentIntents, deliveryIntents);
        assertTrue(mSendReceiver.waitForCalls(numParts, TIME_OUT));
        if (mDeliveryReportSupported) {
          assertTrue(mDeliveryReceiver.waitForCalls(numParts, TIME_OUT));
}
}








