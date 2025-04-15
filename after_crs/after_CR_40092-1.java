/*Fix CTS test failures on Verizon 4g network

The following CTS test cases were failed when test using Verizon
4g network, and the SMS was sent to itself. The Verizon 4G-LTE
network does not send the SMS delivery confirmation.
1.  android.telephony.cts.SmsManagerTest.testSendMessages
2.  android.telephony.gsm.cts.SmsManagerTest.testSendMessages

To fix this problem, Verizon network(311-480) is added to the list
of "NO_DELIVERY_REPORT" networks.

Change-Id:I715fbf43186f4fb5c4095bd969b90b274c57b540*/




//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index bbeffe0..ea8ba43 100755

//Synthetic comment -- @@ -82,7 +82,8 @@
"310000",   // Tracfone
"46003",    // China Telecom
"311230",   // C SPire Wireless + Celluar South
                    "310600",   // Cellcom
                    "311480"    // Verizon Wireless 4G-LTE
);

// List of network operators that doesn't support Data(binary) SMS message







