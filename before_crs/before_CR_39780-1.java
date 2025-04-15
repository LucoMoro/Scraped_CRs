/*Add 31000 (Republic Wireless US) to NO_DELIVERY_REPORTS

31000 (Republic Wireless US) does not support the feature of delivery reports.

Change-Id:Ifbf84f1091c1d02f450d46f0cd2baf0360e2ba40Signed-off-by: DanielMo <DanielMo@fih-foxconn.com>*/
//Synthetic comment -- diff --git a/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java b/tests/tests/telephony/src/android/telephony/cts/SmsManagerTest.java
//Synthetic comment -- index bbeffe0..3c8e649 100755

//Synthetic comment -- @@ -82,7 +82,8 @@
"310000",   // Tracfone
"46003",    // China Telecom
"311230",   // C SPire Wireless + Celluar South
                    "310600"    // Cellcom
);

// List of network operators that doesn't support Data(binary) SMS message







